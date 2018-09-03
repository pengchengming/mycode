package com.bizduo.zflow.util.wdit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.wdit.IRequestService;
import com.bizduo.zflow.util.FileUtil;
import com.bizduo.zflow.util.HttpUtils;
import com.google.protobuf.TextFormat.ParseException;

public class GenerateJsonQuartzUtil { 
	/**
	 *  外网 	生成请求数据  从 info_request 中判断 type 1，2 生成数据   	
	 * @param condition 时间差范围的：  info_request Type: 1请求数据，2材料预审；资质审核记录，现场审核记录
	 * @param requestService
	 * @return
	 * @throws ParseException
	 */
		public static JSONArray getrequestJson( String condition,String subCondition ,IRequestService requestService) throws ParseException{		  
			JSONObject requestTable=requestService.getDataById("wdit_company_request",null, condition);
			JSONArray resultsTemp= requestTable.getJSONArray("results");
			JSONArray results= new JSONArray();
			if(requestTable.has("code")&&requestTable.getInt("code")==1){
				for (int i = 0; i < resultsTemp.length(); i++) {
					JSONObject requesttempJson=resultsTemp.getJSONObject(i);
					int extranetRequestId= requesttempJson.getInt("id");
					requesttempJson.put("extranetRequestId", extranetRequestId);
					//requesttempJson.remove("id");
					JSONObject requestJson= new JSONObject();
					requestJson.put("formId", 61);
					requestJson.put("jsonType", 1);
					requestJson.put("register",requesttempJson);
					
					JSONArray detail= new JSONArray();
					//申请企业照片
					String photocondition=" request_id ="+Integer.parseInt(requesttempJson.get("id").toString());
					JSONObject requestPhotoJson=requestService.getDataById("wdit_company_request_photo",null,photocondition);
					if(requestPhotoJson.has("code")&&requestPhotoJson.getInt("code")==1&&
							requestPhotoJson.has("results")	&&requestPhotoJson.getJSONArray("results").length()>0){
						JSONObject detailjson= new JSONObject();
						detailjson.put("parentId", "request_id");
						detailjson.put("formId", 68);
						detailjson.put("jsonType", 1);
						detailjson.put("array", requestPhotoJson.getJSONArray("results"));
						detail.put(detailjson);
					} 
					//企业审批
					String companyApprovalConditionString=" request_id ="+Integer.parseInt(requesttempJson.get("id").toString());
					JSONObject companyApprovalJSONObject   = requestService.getDataById("wdit_company_approval", null, companyApprovalConditionString);
					System.out.println(companyApprovalJSONObject.toString());
					if(companyApprovalJSONObject.has("code")&&companyApprovalJSONObject.getInt("code")==1
							&&companyApprovalJSONObject.has("results")&&companyApprovalJSONObject.getJSONArray("results").length()>0){
						JSONObject companyApproval = new JSONObject();
						companyApproval.put("parentId", "request_Id");
						companyApproval.put("formId", 66);
						companyApproval.put("jsonType", 1);
						JSONArray  resultsApprovals=companyApprovalJSONObject.getJSONArray("results");
						for (int j = 0; j < resultsApprovals.length(); j++) {
							  resultsApprovals.getJSONObject(j).remove("id");
						}
						companyApproval.put("array", resultsApprovals); 
						
						detail.put(companyApproval);
					}
					//申请人员
					String conditionuser=" request_id ="+requesttempJson.get("id");
					JSONObject requestUserJson= requestService.getDataById("wdit_company_request_user",null,conditionuser);
					if(requestUserJson.has("code")&&requestUserJson.getInt("code")==1&&
							requestUserJson.has("results")	&&requestUserJson.getJSONArray("results").length()>0){
						JSONObject userDetailjson= new JSONObject();
						userDetailjson.put("parentId", "request_id");
						userDetailjson.put("formId", 62);
						JSONArray userArray=new JSONArray();
						JSONArray userResults= requestUserJson.getJSONArray("results");
						for (int j = 0; j < userResults.length(); j++) {
							JSONObject userResult=userResults.getJSONObject(j);
							JSONArray userDetail=new JSONArray();
							Long id = userResult.getLong("id");		
							String condUser=" requestUser_id ="+id;
							//人员审批
							String companyUserApprovalConditionString="requestUser_id="+id;
							JSONObject companyUserApprovalJSONObject  = requestService.getDataById("wdit_company_user_approval", null, companyUserApprovalConditionString);
							if (companyUserApprovalJSONObject.has("code")&&companyUserApprovalJSONObject.getInt("code")==1&&
									companyUserApprovalJSONObject.has("results")&&companyUserApprovalJSONObject.getJSONArray("results").length()>0) { 
								JSONObject companyUserApproval= new JSONObject();
								companyUserApproval.put("parentId", "requestUser_id");
								companyUserApproval.put("formId", 67);
								JSONArray  resultsUserApprovals=companyUserApprovalJSONObject.getJSONArray("results");
								for (int k = 0; k < resultsUserApprovals.length(); k++) {
									resultsUserApprovals.getJSONObject(k).remove("id");
								}
								companyUserApproval.put("array", resultsUserApprovals);
								userDetail.put(companyUserApproval);
							} 
							//63	WDIT_Company_Request_User_relative
							JSONObject userRelativeTempJson= requestService.getDataById("WDIT_Company_Request_User_relative", null, condUser);
							if(userRelativeTempJson.has("code")&&userRelativeTempJson.getInt("code")==1&&
									userRelativeTempJson.has("results")	&&userRelativeTempJson.getJSONArray("results").length()>0){							
								JSONObject relativejson= new JSONObject();
								relativejson.put("parentId", "requestUser_id");
								relativejson.put("formId", 63);		
								
								JSONArray userRelativeResults= userRelativeTempJson.getJSONArray("results");
								JSONArray userRelativeArray=new JSONArray();
								for (int k = 0; k < userRelativeResults.length(); k++) {
									JSONObject relativeResult=userRelativeResults.getJSONObject(k);
									Long relativeId= relativeResult.getLong("id");
									String condrelative=" user_relative_id ="+relativeId;
									JSONObject userRelativePhotoTempJson= requestService.getDataById("WDIT_Company_Request_User_photo", null, condrelative);
									if(userRelativePhotoTempJson.has("code")&&userRelativePhotoTempJson.getInt("code")==1&&
											userRelativePhotoTempJson.has("results")	&&userRelativePhotoTempJson.getJSONArray("results").length()>0){
										JSONArray relativePhotoDetail= new JSONArray();
										JSONObject relativePhotojson= new JSONObject();
										relativePhotojson.put("parentId", "user_relative_id");
										relativePhotojson.put("formId", 65);
										relativePhotojson.put("array", userRelativePhotoTempJson.getJSONArray("results"));
										relativePhotoDetail.put(relativePhotojson);
										relativeResult.put("detail", relativePhotoDetail);
									}
									userRelativeArray.put(relativeResult);
									relativejson.put("array", userRelativeArray);
								}
								//relativeDetail.put(relativejson);
								userDetail.put(relativejson);
							}  
							//64	WDIT_Company_Request_User_housing
							JSONObject userHousingTempJson= requestService.getDataById("WDIT_Company_Request_User_housing", null, condUser);
							if(userHousingTempJson.has("code")&&userHousingTempJson.getInt("code")==1&&
									userHousingTempJson.has("results")	&&userHousingTempJson.getJSONArray("results").length()>0){
								JSONObject housingjson= new JSONObject();
								housingjson.put("parentId", "requestUser_id");
								housingjson.put("formId", 64);		
								
								JSONArray userHousingResults= userHousingTempJson.getJSONArray("results");
								JSONArray userHousingArray=new JSONArray();
								for (int k = 0; k < userHousingResults.length(); k++) {
									JSONObject housingResult=userHousingResults.getJSONObject(k);
									Long housingId= housingResult.getLong("id");
									String condhousing=" user_housing_id ="+housingId;
									JSONObject userHousingPhotoTempJson= requestService.getDataById("WDIT_Company_Request_User_photo", null, condhousing);
									if(userHousingPhotoTempJson.has("code")&&userHousingPhotoTempJson.getInt("code")==1&&
											userHousingPhotoTempJson.has("results")	&&userHousingPhotoTempJson.getJSONArray("results").length()>0){
										JSONArray housingPhotoDetail= new JSONArray();
										JSONObject housingPhotojson= new JSONObject();
										housingPhotojson.put("parentId", "user_housing_id");
										housingPhotojson.put("formId", 65);
										housingPhotojson.put("array", userHousingPhotoTempJson.getJSONArray("results"));
										housingPhotoDetail.put(housingPhotojson);
										housingResult.put("detail", housingPhotoDetail);
									}
									userHousingArray.put(housingResult);
									housingjson.put("array", userHousingArray);
								}
								//housingDetail.put(housingjson);
								userDetail.put(housingjson);
							}
							//65	WDIT_Company_Request_User_photo 
							JSONObject userPhotoJson=requestService.getDataById("WDIT_Company_Request_User_photo",null,condUser);
							if(userPhotoJson.has("code")&&userPhotoJson.getInt("code")==1&&
									userPhotoJson.has("results")	&&userPhotoJson.getJSONArray("results").length()>0){
								JSONObject userPhotojson= new JSONObject();
								userPhotojson.put("parentId", "requestUser_id");
								userPhotojson.put("formId", 65);
								userPhotojson.put("array", userPhotoJson.getJSONArray("results"));
								userDetail.put(userPhotojson);
							}
							userResult.put("detail", userDetail);
							userArray.put(userResult);
						}
						userDetailjson.put("array", userArray);
						detail.put(userDetailjson);
					}
					requestJson.put("detail",detail);
					results.put(requestJson);
				}
			}
			return results;
		}
		
		
		/**
		 * 获取外网审批json数据 和 内网审批数据
		 * @param requestService
		 * @param type  1外网 2内网
		 * @param condition  时间差范围的： 委办，人才办，人社局 审批记录
		 * @return
		 */
		public static JSONArray getApprovalJson(String jdbcUrl,String user,String password,int  type, IRequestService requestService,String condition,String subCondition){ 
				//condition +=" and status in  (201,202,203,204,205)";	//限制生成的范围,只生成资质
				JSONObject requestTable=new JSONObject();
				if(type==1)
					requestTable=requestService.getDataById("wdit_company_request",null,condition);
				else if(type==2)
					requestTable=requestService.getDataByIdSyn(jdbcUrl, user, password, "wdit_company_request",condition);
				
				//this.requestService.getInfoBySyn(jdbcUrl, databaseUsername, databasePassword, condition)
				System.out.println(requestTable.toString());
				JSONArray resultsTemp= requestTable.getJSONArray("results");
				JSONArray results= new JSONArray();
				if(requestTable.has("code")&&requestTable.getInt("code")==1){
					for (int i = 0; i < resultsTemp.length(); i++) {
						JSONObject requesttempJson=resultsTemp.getJSONObject(i); 
						JSONObject requestJson= new JSONObject();
						requestJson.put("formId", 61);
						JSONObject register= new JSONObject();
						register.put("status", requesttempJson.get("status"));
						register.put("acceptanceNumber", requesttempJson.get("acceptanceNumber"));
						register.put("applicant", requesttempJson.get("applicant"));
						register.put("extranetRequestId", requesttempJson.getInt("id"));
						requestJson.put("register",register);
						requestJson.put("jsonType", 11);
						int requestId = requesttempJson.getInt("id");
						JSONArray detail= new JSONArray();
						//企业审批
						String companyApprovalConditionString=" request_Id="+ requestId;
						JSONObject companyApprovalJSONObject =new JSONObject();
						if(type==1)
							companyApprovalJSONObject = requestService.getDataById("wdit_company_approval", null, companyApprovalConditionString);
						else if(type==2)
							companyApprovalJSONObject = requestService.getDataByIdSyn(jdbcUrl, user, password,  "wdit_company_approval",  companyApprovalConditionString);
						
						System.out.println(companyApprovalJSONObject.toString());
						if(companyApprovalJSONObject.has("code")&&companyApprovalJSONObject.getInt("code")==1
								&&companyApprovalJSONObject.has("results")&&companyApprovalJSONObject.getJSONArray("results").length()>0){
							JSONObject companyApproval = new JSONObject();
							companyApproval.put("parentId", "request_Id");
							companyApproval.put("formId", 66);
							JSONArray jsonArray = companyApprovalJSONObject.getJSONArray("results");
							JSONArray company_approval_arr = new JSONArray();
							//查询到的企业审批放进去
							for(int i1=0;i1<jsonArray.length();i1++){
								JSONObject partCompanyApproval = jsonArray.getJSONObject(i1);
								partCompanyApproval.remove("id");
								company_approval_arr.put(partCompanyApproval);
							}
							companyApproval.put("array",company_approval_arr );
							detail.put(companyApproval);	
						}
						if(detail.length()>0){
							requestJson.put("detail",detail);
							results.put(requestJson);	
						}
						//申请人员 
						JSONObject requestUserJson=new JSONObject();
						if(type==1)
							requestUserJson= requestService.getDataById("wdit_company_request_user",null," request_Id="+ requestId);
						else if(type==2)
							requestUserJson= requestService.getDataByIdSyn(jdbcUrl, user, password, "wdit_company_request_user"," request_Id="+ requestId);
						System.out.println(requestUserJson.toString());
						if(requestUserJson.has("code")&&requestUserJson.getInt("code")==1&&
								requestUserJson.has("results")	&&requestUserJson.getJSONArray("results").length()>0){ 
							JSONArray userResults= requestUserJson.getJSONArray("results");
							for (int j = 0; j < userResults.length(); j++) {
								JSONObject userResult=userResults.getJSONObject(j);
								Long id = userResult.getLong("id");		
								
								JSONObject userObject=new JSONObject();
								JSONObject userregister=new JSONObject();
								userregister.put("status", userResult.get("status"));
								userObject.put("register", userregister);
								userObject.put("formId", 62);
								userObject.put("tableDataId", id); 
								userObject.put("jsonType",2);
								JSONArray userApproval=new JSONArray();	
								//人员审批
								String companyUserApprovalConditionString="requestUser_id="+id;
								JSONObject companyUserApprovalJSONObject =new JSONObject();
								if(type==1)
									companyUserApprovalJSONObject = requestService.getDataById("wdit_company_user_approval", null, companyUserApprovalConditionString);
								else if (type==2)  
									companyUserApprovalJSONObject = requestService.getDataByIdSyn(jdbcUrl, user, password, "wdit_company_user_approval", companyUserApprovalConditionString);
								
								System.out.println(companyUserApprovalJSONObject.toString());
								if (companyUserApprovalJSONObject.has("code")&&companyUserApprovalJSONObject.getInt("code")==1&&
										companyUserApprovalJSONObject.has("results")&&companyUserApprovalJSONObject.getJSONArray("results").length()>0) {
									JSONObject companyUserApproval = new JSONObject();
									companyUserApproval.put("parentId", "requestUser_id");
									companyUserApproval.put("formId", 67);
									JSONArray jsonArray = companyUserApprovalJSONObject.getJSONArray("results");
									JSONArray company_user_approval_arr = new JSONArray();
									//查询到的人员审批放进去
									for(int i1=0;i1<jsonArray.length();i1++){
										JSONObject partCompanyUserApproval = jsonArray.getJSONObject(i1);
										partCompanyUserApproval.remove("id");
										company_user_approval_arr.put(partCompanyUserApproval);
									}
									companyUserApproval.put("array", company_user_approval_arr);
									userApproval.put(companyUserApproval);
								}
								if(userApproval.length()>0){
									userObject.put("detail", userApproval);
									results.put(userObject);
								}
							} 
						} 
						
					}
				}
				
				return results;
			}
		/**
		 * 外到内 保存数据
		 * @param requestService
		 * @param formService
		 * @param WB
		 * @param resultString
		 * @return
		 * @throws ParseException
		 * @throws SQLException
		 */
		public static int saveImportRequestData(IRequestService requestService,IFormService formService,
				ResourceBundle WB,String resultString,Map<String,String> map) throws ParseException, SQLException{ 
			 String jdbcUrl= WB.getString("syn.jdbcUrl");
			 String user= WB.getString("syn.user");
			 String password= WB.getString("syn.password"); 
			 String synImageUrl= WB.getString("syn.synImageUrl");
			 int isSuccess=0; 
			 //requestIdList.clear();	//清理上一次执行该方法可能留下的数据
			 JSONObject infoJson= new JSONObject(resultString); 
			 if(infoJson.has("code")&&infoJson.getInt("code")==1&&
					 infoJson.has("results")	&&infoJson.getJSONArray("results").length()>0){ 
				JSONArray infoResultArray=infoJson.getJSONArray("results");//.getJSONObject(0);
				List<String> tmpJsonlist=new ArrayList<String>();	//临时保存json数据用
				List<String> tmpJsonlist2=new ArrayList<String>(); //临时保存json审批记录数据用
				List<JSONObject> tmpJsonlisttemp2=new ArrayList<JSONObject>(); //临时保存json审批记录数据用
				//int arrLength=0;

				if(infoResultArray.length()>0){
					for (int j = 0; j < infoResultArray.length(); j++) {
						JSONObject wdit_info_json = infoResultArray.getJSONObject(j); 
						String jsoninfo=wdit_info_json.getString("jsoninfo");
						 System.out.println(jsoninfo);
						 JSONArray infoJsonArray= new JSONArray(jsoninfo); 

						//List<JSONObject> tmpJsonlisttemp=new ArrayList<JSONObject>();	//临时保存json数据用
						for (int i = 0; i < infoJsonArray.length(); i++) {
							 JSONObject requestObjecttemp= infoJsonArray.getJSONObject(i);
							 if(requestObjecttemp.has("jsonType")){
								 if(requestObjecttemp.getInt("jsonType")==1){ 
										JSONObject outRegister = requestObjecttemp.getJSONObject("register");
										if(outRegister.has("extranetRequestId")){
											Integer extranetRequestId = outRegister.getInt("extranetRequestId");
											if(extranetRequestId!=null){
												String condition="  extranetRequestId='"+extranetRequestId+"' ";
												JSONObject requestTable=requestService.getDataById("wdit_company_request",null,condition);
												if(requestTable.has("code")&&requestTable.getInt("code")==1){
													JSONArray resultsTemp= requestTable.getJSONArray("results");
													if(resultsTemp.length()>0){ 
														JSONObject requestCompany= resultsTemp.getJSONObject(0);
														requestObjecttemp.put("tableDataId", requestCompany.getInt("id"));	
													}
												}
											} 
										}
										tmpJsonlist.add(requestObjecttemp.toString());
								 } 
								 else if(requestObjecttemp.getInt("jsonType")==11||requestObjecttemp.getInt("jsonType")==2)
									 tmpJsonlisttemp2.add(requestObjecttemp);
							 }	 
						}
					}
				}
				 
				  
				try {
					//实现数据同步
					if(tmpJsonlist!=null&&tmpJsonlist.size()>0){
						String[] saveJson=new String[tmpJsonlist.size()];
						for (int i = 0; i < tmpJsonlist.size(); i++) {
							saveJson[i]=tmpJsonlist.get(i);
						}
						formService.saveFormDataJsonSyn(jdbcUrl,user,password,saveJson);//一级，数据库中存在 保存，新增，修改
						 String resultImgeString= HttpUtils.postRequest(synImageUrl,map);//获取照片路径Json
						 saveImage(WB,resultImgeString);//保存图片
					}
					if(tmpJsonlisttemp2!=null&&tmpJsonlisttemp2.size()>0){
						 //修改 jsontype=2的
						for (int i = 0; i < tmpJsonlisttemp2.size(); i++) {
							JSONObject requestObject=tmpJsonlisttemp2.get(i);
							JSONObject outRegister = requestObject.getJSONObject("register");
							Integer formId= requestObject.getInt("formId");
							if(formId.intValue()==62){
								tmpJsonlist2.add(requestObject.toString());
								continue;
							}
							if(outRegister.has("extranetRequestId")){
								Integer extranetRequestId = outRegister.getInt("extranetRequestId");
								if(extranetRequestId!=null){
									String condition="  extranetRequestId='"+extranetRequestId+"' ";
									JSONObject requestTable=requestService.getDataById("wdit_company_request",null,condition);
									if(requestTable.has("code")&&requestTable.getInt("code")==1){
										JSONArray resultsTemp= requestTable.getJSONArray("results");
										if(resultsTemp.length()>0){ 
											JSONObject requestCompany= resultsTemp.getJSONObject(0);
											requestObject.put("tableDataId", requestCompany.getInt("id"));	
											tmpJsonlist2.add(requestObject.toString());
										}
									}
								} 
							}
						}
						if(tmpJsonlist2.size()>0){							
							String[] saveJson=new String[tmpJsonlist2.size()];
							for (int i = 0; i < tmpJsonlist2.size(); i++) {
								saveJson[i]=tmpJsonlist2.get(i);
							}
							formService.saveFormDataJsonSyn(jdbcUrl,user,password,saveJson);//一级，数据库中存在 保存，新增，修改	
						}
					}
					isSuccess=1;
				} catch (Exception e) {
					isSuccess=0;
				} 
			 }
			 return isSuccess;
		}
		
		/**
		 * 内推送到外 保存数据
		 * @param requestService
		 * @param formService
		 * @param WB
		 * @param resultString  根据info时间差 获取的json
		 * @return
		 * @throws ParseException
		 * @throws SQLException
		 */
		public static int saveImportData(IRequestService requestService,IFormService formService,String resultString){ 
			 int isSuccess=0; 
			 //requestIdList.clear();	//清理上一次执行该方法可能留下的数据
			 JSONObject infoJson= new JSONObject(resultString); 
			 if(infoJson.has("code")&&infoJson.getInt("code")==1&&
					 infoJson.has("results")	&&infoJson.getJSONArray("results").length()>0){ 
				JSONArray infoResultArray=infoJson.getJSONArray("results");//.getJSONObject(0);
				List<String> tmpJsonlist=new ArrayList<String>();	//临时保存json数据用
				//int arrLength=0;

				JSONObject wdit_info_json = infoResultArray.getJSONObject(0); 
				String jsoninfo=wdit_info_json.getString("jsoninfo");

				 JSONArray infoJsonArray= new JSONArray(jsoninfo); 
				for (int i = 0; i < infoJsonArray.length(); i++) {
						JSONObject requestObject= infoJsonArray.getJSONObject(i);
						/*******解析每一条json*******/
						JSONObject outRegister = requestObject.getJSONObject("register");
						//int jsonStatus = outRegister.getInt("status");

						boolean isSave=false;
						if(requestObject.getInt("jsonType")==11){///企业申请表 
							String acceptanceNumber = outRegister.getString("acceptanceNumber");
							String condition="  acceptanceNumber='"+acceptanceNumber+"' ";
							JSONObject requestTable=requestService.getDataById("wdit_company_request",null,condition);
							if(requestTable.has("code")&&requestTable.getInt("code")==1
									&&requestTable.getJSONArray("results").length()>0){
								JSONArray resultsTemp= requestTable.getJSONArray("results");
								JSONObject requestCompany= resultsTemp.getJSONObject(0);
								//Integer status= requestCompany.getInt("status");
								//if(status!=202 &&status!=203&&status!=204){
									 isSave=true;
									 requestObject.put("tableDataId", requestCompany.getInt("id"));
								//} 
							}
						}else if(requestObject.getInt("jsonType")==2){//申请人员
							 isSave=true;
						}
						if(isSave){
							tmpJsonlist.add(requestObject.toString());
						}
						
				}	
			 
				try {
					//实现数据同步
					if(tmpJsonlist!=null&&tmpJsonlist.size()>0){
						String[] saveJson=new String[tmpJsonlist.size()];
						for (int i = 0; i < tmpJsonlist.size(); i++) {
							saveJson[i]=tmpJsonlist.get(i);
						}
						formService.saveFormDataJson(saveJson);
					}
					isSuccess=1;
				} catch (Exception e) {
					e.printStackTrace();
					isSuccess=0;
				} 
			 }
			 return isSuccess;
		}
		/**
		 * 根据时间差，获取照片路径 
		 * @param WB
		 * @param resultImgeString 图片照片路径JSON
		 */
		@SuppressWarnings("unchecked")
		public static void  saveImage(ResourceBundle WB,String resultImgeString){ 
			 String synWebPath= WB.getString("syn.synWebPath");
			 String synUploadImage= WB.getString("syn.synUploadImage");			 
			JSONObject pathNames= new JSONObject(resultImgeString);
			if(pathNames!=null&&pathNames.length()>0){
				Iterator<String> sIterator = pathNames.keys();  
				while(sIterator.hasNext()){
				    String key = sIterator.next();// 获得key  
				    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可  
				    String value = pathNames.getString(key);  
				    value=value.replaceAll("\\\\", "/");
				    System.out.println("key: "+key+",value"+value);
				    FileUtil.downloadFile( synWebPath+value, synUploadImage+value);	
				}
			}
		}
}
