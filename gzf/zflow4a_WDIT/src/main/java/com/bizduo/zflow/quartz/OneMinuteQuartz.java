package com.bizduo.zflow.quartz;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.wdit.IRequestService;
import com.bizduo.zflow.util.FileUtil;
import com.bizduo.zflow.util.HttpUtils;
import com.bizduo.zflow.util.TimeUitl;

public class OneMinuteQuartz {

	protected final Log log = LogFactory.getLog(OneMinuteQuartz.class);  

	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private IRequestService requestService;
	@Autowired
	private IFormService formService;
		
	public void oneMinuteMethod(){
	    	String  nowDateStr="";
	    	try {
	    		//根据导入接口时间 生成申请导入的数据 
	    		SqlRowSet rset = jdbcTemplate.queryForRowSet("select exportDate from WDIT_Info  where type=1  order by exportDate  desc LIMIT 0,1"); 
	            while(rset.next()){
	            		nowDateStr= TimeUitl.getTimeLongTime(rset.getDate("exportDate").getTime(), "yyyy-MM-dd HH:mm:ss");
	            }
	    		JSONArray json = getrequestJson(nowDateStr);
	    		if(json!=null&&json.length()>0){

		    		System.out.println(json.toString());
					JSONObject saveInfo=new JSONObject();
					saveInfo.put("formId", 84);
					JSONObject register=new JSONObject();
					register.put("type", 1);
					register.put("exportDate", new Date());
					register.put("jsoninfo", json.toString() );
					int  isSuccess=0;
					if(json!=null &&json.length()>0){
						isSuccess=1;
					} 
					register.put("isSuccess", isSuccess );
					String  nowDate= TimeUitl.getTimeLongTime(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");
					register.put("exportDate", nowDate);
					saveInfo.put("register",register);
					this.formService.saveFormDataJson(saveInfo.toString());
	    		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("oneMinuteQuartz");
	}
	
	public  void readOneMinuteMethod(){
		 ResourceBundle WB = ResourceBundle.getBundle("synchronizeConfig", Locale.getDefault());
		 String extranetUrl= WB.getString("syn.extranetUrl");
		 String synImageUrl= WB.getString("syn.synImageUrl");
		 String jdbcUrl= WB.getString("syn.jdbcUrl");
		 String user= WB.getString("syn.user");
		 String password= WB.getString("syn.password"); 
		 
		 Map<String,String> map=new HashMap<String, String>();
		 String  importDateStr=null; 
		 try {
			/*SqlRowSet rset = jdbcTemplate.queryForRowSet("select importDate from WDIT_Info "); 
	        while(rset.next()){
	        	importDateStr =Long.toString(rset.getDate("importDate").getTime());
	        }*/

		    JSONObject infoJson= requestService.getInfoBySyn(jdbcUrl, user, password, "where type=2   order by importDate  desc LIMIT 0,1");
	        if(infoJson!=null&&infoJson.has("importDateLong")){
	        	importDateStr =Long.toString(infoJson.getLong("importDateLong"));
	        }
	        if(StringUtils.isNotBlank(importDateStr))
				 map.put("nowDate",importDateStr);
			 String resultString= HttpUtils.postRequest(extranetUrl,map);
			 System.out.println(resultString); 

			 if(StringUtils.isNotBlank(resultString)){
				 JSONObject resultJson=new JSONObject(resultString);
				 if(resultJson.has("code")&&resultJson.has("results")
						 &&resultJson.getJSONArray("results").length()>0){
					 int isSuccess=saveImportData(WB,resultString); //保存数据
					 String resultImgeString= HttpUtils.postRequest(synImageUrl,map);//获取照片路径Json
					 saveImage(WB,resultImgeString);//保存图片
					 
					JSONObject saveInfo=new JSONObject();
					saveInfo.put("formId", 84);
					JSONObject register=new JSONObject();
					register.put("type", 2);
					register.put("isSuccess", isSuccess);
					String  nowDate= TimeUitl.getTimeLongTime(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");
					register.put("jsoninfo", resultString ); 
					register.put("importDate", nowDate);
					saveInfo.put("register",register);
					this.formService.saveFormDataJsonSyn(jdbcUrl,user,password,saveInfo.toString());
				 } 	
			 }
		 } catch (Exception e) { 
			e.printStackTrace();
		}
		System.out.println("readOneMinuteMethod");
	}


	@SuppressWarnings("unchecked")
	private void  saveImage(ResourceBundle WB,String resultImgeString){ 
		 String synWebPath= WB.getString("syn.synWebPath");
		 String synUploadImage= WB.getString("syn.synUploadImage");
		 
		JSONObject pathNames= new JSONObject(resultImgeString);
		if(pathNames!=null&&pathNames.length()>0){
			Iterator<String> sIterator = pathNames.keys();  
			while(sIterator.hasNext()){  
			    // 获得key  
			    String key = sIterator.next();  
			    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可  
			    String value = pathNames.getString(key);  
			    value=value.replaceAll("\\\\", "/");
			    System.out.println("key: "+key+",value"+value);
			    FileUtil.downloadFile( synWebPath+value, synUploadImage+value);	
			}  

		}
	}
	
	//
	private int saveImportData(ResourceBundle WB,String resultString){ 
		 String jdbcUrl= WB.getString("syn.jdbcUrl");
		 String user= WB.getString("syn.user");
		 String password= WB.getString("syn.password"); 
		 int isSuccess=0;
		 JSONObject infoJson= new JSONObject(resultString);
		 if(infoJson.has("code")&&infoJson.getInt("code")==1&&
				 infoJson.has("results")	&&infoJson.getJSONArray("results").length()>0){
				JSONObject infoResult=infoJson.getJSONArray("results").getJSONObject(0);
				if(infoResult.has("jsoninfo")){
					JSONArray results=new JSONArray(infoResult.getString("jsoninfo"));
					if(results!=null &&  results.length()>0){
						String[]  jsons =new String[results.length()];
						for (int i = 0; i < results.length(); i++) {
							jsons[i]=results.getJSONObject(i).toString();
						}
						try {
							this.formService.saveFormDataJsonSyn(jdbcUrl,user,password,jsons); 
							isSuccess=1;
						} catch (Exception e) {
							isSuccess=0;
						}
					}						 
				}
		 }
		 return isSuccess;
	}
	//生成请求数据
	private JSONArray getrequestJson(  String nowDateStr) throws ParseException{
		String condition="  id in (SELECT  request_id from   wdit_info_request where isGenerate =0  and type=1  ";
		if(nowDateStr!=null&& !nowDateStr.equals("")){
			condition +=" and modifyDate >'"+nowDateStr+"' ";
		}
		condition +=" ) ";
		JSONObject requestTable=this.requestService.getDataById("wdit_company_request",null,condition);
		JSONArray resultsTemp= requestTable.getJSONArray("results");
		JSONArray results= new JSONArray();
		if(requestTable.has("code")&&requestTable.getInt("code")==1){
			for (int i = 0; i < resultsTemp.length(); i++) {
				JSONObject requesttempJson=resultsTemp.getJSONObject(i); 
				JSONObject requestJson= new JSONObject();
				requestJson.put("formId", 61);
				requestJson.put("register",requesttempJson);
				
				JSONArray detail= new JSONArray();
				//申请企业照片
				String photocondition=" request_id ="+Integer.parseInt(requesttempJson.get("id").toString());
				JSONObject requestPhotoJson=this.requestService.getDataById("wdit_company_request_photo",null,photocondition);
				if(requestPhotoJson.has("code")&&requestPhotoJson.getInt("code")==1&&
						requestPhotoJson.has("results")	&&requestPhotoJson.getJSONArray("results").length()>0){
					JSONObject detailjson= new JSONObject();
					detailjson.put("parentId", "request_id");
					detailjson.put("formId", 68);
					detailjson.put("array", requestPhotoJson.getJSONArray("results"));
					detail.put(detailjson);
				}
				//申请人员
				String conditionuser=" request_id ="+requesttempJson.get("id");
				JSONObject requestUserJson= this.requestService.getDataById("wdit_company_request_user",null,conditionuser);
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
						//63	WDIT_Company_Request_User_relative
						JSONObject userRelativeTempJson= this.requestService.getDataById("WDIT_Company_Request_User_relative", null, condUser);
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
								JSONObject userRelativePhotoTempJson= this.requestService.getDataById("WDIT_Company_Request_User_photo", null, condrelative);
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
						JSONObject userHousingTempJson= this.requestService.getDataById("WDIT_Company_Request_User_housing", null, condUser);
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
								JSONObject userHousingPhotoTempJson= this.requestService.getDataById("WDIT_Company_Request_User_photo", null, condhousing);
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
						JSONObject userPhotoJson=this.requestService.getDataById("WDIT_Company_Request_User_photo",null,condUser);
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
	
}
