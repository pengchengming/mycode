package com.bizduo.zflow.controller.WDIT;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.wdit.IRequestService;
import com.bizduo.zflow.util.TimeUitl;
import com.bizduo.zflow.util.wdit.GenerateJsonQuartzUtil;

@Controller
@RequestMapping("/export")
public class ExportJsonController {
	@Autowired
	public IRequestService requestService;
	@Autowired
	private IFormService formService;

	/***
	 * 资质，内网拿外网生成数据
	 * 
	 * @param nowDate
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "qualifications6")
	@ResponseBody
	public void qualifications6(
			@RequestParam(value = "nowDate", required = false) Long nowDate,
			@RequestParam(value = "type", required = false) Integer type,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			JSONObject results = requestService.getInfoBy(nowDate, type);
			System.out.println("============================>qualifications6qualifications6qualifications6qualifications6");
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(results.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * 资质，内网生成数据推送外网数据
	 * 
	 * @param nowDate
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "qualifications61")
	@ResponseBody
	public void qualifications61(
			@RequestParam(value = "dataJson", required = false) String dataJson,
			@RequestParam(value = "type", required = false) Integer type,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			    System.out.println("==================================>qualifications61qualifications61qualifications61qualifications61qualifications61qualifications61qualifications61qualifications61");
				System.out.println("=============>dataJson:"+dataJson);
				String  nowDate= TimeUitl.getTimeLongTime(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");				
			    int isSuccess=outWebSaveQualificationsDataWhereFromInner(dataJson); //保存数据
			  
				JSONObject saveInfo=new JSONObject();
				saveInfo.put("formId", 84);
				JSONObject register=new JSONObject();
				register.put("type", type);
				register.put("isSuccess", isSuccess);
				register.put("jsoninfo",dataJson); 
				register.put("importDate", nowDate);
				saveInfo.put("register",register);
				//更新到外网info
				//this.formService.saveFormDataJsonSyn(jdbcUrl,user,password,saveInfo.toString());
				this.formService.saveFormDataJson(saveInfo.toString());
				response.setContentType("text/plain; charset=utf-8");
				response.getWriter().print(isSuccess);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int outWebSaveQualificationsDataWhereFromInner(String resultString) {
		System.out.println("outWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInner");
		int isSuccess = 0;
		// requestIdList.clear(); //清理上一次执行该方法可能留下的数据
		JSONObject infoJson = new JSONObject(resultString);
		if (infoJson.has("code") && infoJson.getInt("code") == 1&& infoJson.has("results")&& infoJson.getJSONArray("results").length() > 0) {
			JSONArray infoResultArray = infoJson.getJSONArray("results");// .getJSONObject(0);
			List<String> tmpJsonlist = new ArrayList<String>(); // 临时保存json数据用
			// int arrLength=0;
			JSONObject wdit_info_json = infoResultArray.getJSONObject(0);
			String jsoninfo = wdit_info_json.getString("jsoninfo");

			JSONArray infoJsonArray = new JSONArray(jsoninfo);
			for (int i = 0; i < infoJsonArray.length(); i++) {
				JSONObject requestObject = infoJsonArray.getJSONObject(i);
				/******* 解析每一条json *******/
				JSONObject outRegister = requestObject.getJSONObject("register");
				// int jsonStatus = outRegister.getInt("status");

				boolean isSave = false;
				if (requestObject.getInt("jsonType") == 1) {
					String acceptanceNumber = outRegister.getString("acceptanceNumber");
					String condition = "  acceptanceNumber='"+ acceptanceNumber + "' ";
					JSONObject requestTable = this.requestService.getDataById("wdit_company_request", null, condition);
					if (requestTable.has("code")&& requestTable.getInt("code") == 1) {
						JSONArray resultsTemp = requestTable.getJSONArray("results");
						JSONObject requestCompany = resultsTemp.getJSONObject(0);
						//Integer status = requestCompany.getInt("status");
						//内网无条件推送！！！
						//if (status == 202 || status == 203) {
							isSave = true;
							requestObject.put("tableDataId",requestCompany.getInt("id"));
							
						//}
					}
				} else if (requestObject.getInt("jsonType") == 2) {
					isSave = true;
				}
				if (isSave) {
					tmpJsonlist.add(requestObject.toString());
				
				}

			}

			try {
				// 实现数据同步
				if (tmpJsonlist != null && tmpJsonlist.size() > 0) {
					String[] saveJson = new String[tmpJsonlist.size()];
					for (int i = 0; i < tmpJsonlist.size(); i++) {
						saveJson[i] = tmpJsonlist.get(i);
					}
					//this.formService.saveFormDataJsonSyn(jdbcUrl,user,password,saveJson);
					this.formService.saveFormDataJson(saveJson);
					System.out.println("================================>添加数据到外网");
				}
				isSuccess = 1;
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = 0;
			}
			
		}
		return isSuccess;
	}

	@RequestMapping("pretrial4")
	@ResponseBody
	public void pretrial4(@RequestParam(value="importDateLong",required=false)Long importDateLong,
						  @RequestParam(value="type",required=false)Integer type,
						  HttpServletRequest request,HttpServletResponse response) throws ParseException, IOException{
		try {
			JSONObject results = this.requestService.getInfoBy(importDateLong, type);
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(results.toString());	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@RequestMapping("pretrial41")
	@ResponseBody
	public void pretrial41(@RequestParam(value="jsonDataStr",required=false)String jsonDataStr,
						  @RequestParam(value="type",required=false)Integer type,
						  HttpServletRequest request,HttpServletResponse response) throws ParseException, IOException{
		response.setContentType("text/plain; charset=utf-8");
		try {
			System.out.println("================================================>pretrial41pretrial41pretrial41pretrial41pretrial41pretrial41");

			String  nowDate= TimeUitl.getTimeLongTime(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");
			int isSuccess=savePretrial2Out(jsonDataStr);
			JSONObject saveInfo = new JSONObject();
			JSONObject register = new JSONObject();
			saveInfo.put("formId", 84);
			register.put("isSuccess", isSuccess);
			register.put("type", type);
			register.put("formId", 84);
			register.put("jsoninfo", jsonDataStr);
			register.put("importDate", nowDate);
			saveInfo.put("register", register);
			//this.formService.saveFormDataJson(saveInfo.toString());
			System.out.println("内网初审数据==>外网同步成功");
			response.getWriter().write("1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("内网初审数据==>外网同步失败");
			response.getWriter().write("0");
		}
		
	}

	private int savePretrial2Out(String jsonDataStr) {
		System.out.println("outWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInneroutWebSaveQualificationsDataWhereFromInner");
		int isSuccess = 0;
		// requestIdList.clear(); //清理上一次执行该方法可能留下的数据
		JSONObject infoJson = new JSONObject(jsonDataStr);
		if (infoJson.has("code") && infoJson.getInt("code") == 1&& infoJson.has("results")&& infoJson.getJSONArray("results").length() > 0) {
			JSONArray infoResultArray = infoJson.getJSONArray("results");// .getJSONObject(0);
			List<String> tmpJsonlist = new ArrayList<String>(); // 临时保存json数据用
			// int arrLength=0;
			JSONObject wdit_info_json = infoResultArray.getJSONObject(0);
			String jsoninfo = wdit_info_json.getString("jsoninfo");

			JSONArray infoJsonArray = new JSONArray(jsoninfo);
			for (int i = 0; i < infoJsonArray.length(); i++) {
				JSONObject requestObject = infoJsonArray.getJSONObject(i);
				/******* 解析每一条json *******/
				JSONObject outRegister = requestObject.getJSONObject("register");
				// int jsonStatus = outRegister.getInt("status");

				boolean isSave = false;
				if (requestObject.getInt("jsonType") == 1) {
					String acceptanceNumber = outRegister.getString("acceptanceNumber");
					String condition = "  acceptanceNumber='"+ acceptanceNumber + "' ";
					JSONObject requestTable = this.requestService.getDataById("wdit_company_request", null, condition);
					if (requestTable.has("code")&& requestTable.getInt("code") == 1) {
						JSONArray resultsTemp = requestTable.getJSONArray("results");
						JSONObject requestCompany = resultsTemp.getJSONObject(0);
						//Integer status = requestCompany.getInt("status");
						//内网无条件推送！！！
						//if (status == 202 || status == 203) {
							isSave = true;
							requestObject.put("tableDataId",requestCompany.getInt("id"));
							
						//}
					}
				} else if (requestObject.getInt("jsonType") == 2) {
					isSave = true;
				}
				if (isSave) {
					tmpJsonlist.add(requestObject.toString());
				
				}

			}

			try {
				// 实现数据同步
				if (tmpJsonlist != null && tmpJsonlist.size() > 0) {
					String[] saveJson = new String[tmpJsonlist.size()];
					for (int i = 0; i < tmpJsonlist.size(); i++) {
						saveJson[i] = tmpJsonlist.get(i);
					}
					
					//this.formService.saveFormDataJson(saveJson);
					System.out.println("================================>添加初审数据到外网");
				}
				isSuccess = 1;
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = 0;
			}
			
		}
		return isSuccess;
	}
	
	@RequestMapping("request_pretrial_1_Url")
	@ResponseBody
	/**
	 * 内网发来请求拿数据
	 */
	public void request_pretrial_take(@RequestParam(value="nowDateStr",required=false)Long nowDateStr,
									  @RequestParam(value="type",required=false)Integer type,
									  HttpServletRequest request,HttpServletResponse response){
		try {
			System.out.println("===================>request_pretrial_takerequest_pretrial_takerequest_pretrial_takerequest_pretrial_takerequest_pretrial_take");
			JSONObject infoBy = requestService.getInfoBy(nowDateStr, type);
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(infoBy.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	@RequestMapping("intranet_take_from_extranet")
	@ResponseBody
	public void get_housing_management_json(@RequestParam(value="nowDateStr",required=false)Long nowDateStr,
			@RequestParam(value="nowLong",required=false)Long nowLong,
											@RequestParam(value="type",required=false)Integer type,
											HttpServletRequest request,HttpServletResponse response) throws ParseException, IOException{
		response.setContentType("text/plain; charset=utf-8"); 
		try {
			boolean isExport=true;
			/*if(nowLong!=null){
				Long nowDatelong= new Date().getTime(); 
				

			   if(nowDatelong > nowLong) {  
				   long  diff = nowDatelong - nowDateStr;  

				   long   day = diff / (24 * 60 * 60 * 1000);  
				   long hour = (diff / (60 * 60 * 1000) - day * 24);  
				   long  min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
				   if(min > 2){
						JSONObject saveInfo = new JSONObject(); 
						JSONObject register = new JSONObject(); 
						saveInfo.put("register", register);
						saveInfo.put("code", 0 );
						saveInfo.put("errorMsg", "时间相差一分钟以上");
						isExport=false;
						response.getWriter().print(saveInfo.toString());
				   }
			   }  
			}  */
			if(isExport){
				JSONObject infoBy = this.requestService.getInfoBy(nowDateStr, type);
				System.out.println("=======================>打印内网从外网获取到的json数据");
				response.getWriter().print(infoBy.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	@RequestMapping("intranet_post_data_to_extranet")
	@ResponseBody
	public void intranetCogradient(@RequestParam(value="jsonDataStr",required=false)String jsonDataStr,
								   @RequestParam(value="type",required=false)Integer type,
								   HttpServletRequest request,HttpServletResponse response) {
		//解析数据
		ResourceBundle WB = ResourceBundle.getBundle("synchronizeConfig", Locale.getDefault());
		try {
			String  nowDate= TimeUitl.getTimeLongTime(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");
			int isSuccess = GenerateJsonQuartzUtil.saveImportData(requestService, formService, jsonDataStr);
			JSONObject saveInfo = new JSONObject();
			JSONObject register = new JSONObject();
			saveInfo.put("formId", 84);
			register.put("isSuccess", isSuccess);
			register.put("jsoninfo", jsonDataStr);
			register.put("importDate", nowDate);
			register.put("type", type);
			saveInfo.put("register", register);
			//同步信息保存到外网wdit_info
			this.formService.saveFormDataJson(saveInfo.toString());
			
			//保存一条记录带外网的wdit_info_extranet
			JSONObject info_extranet_obj = new JSONObject();
			JSONObject info_extranet = new JSONObject();
			info_extranet_obj.put("formId", 98);
			info_extranet.put("importDate", nowDate);
			info_extranet.put("isSuccess", isSuccess);
			info_extranet.put("type", type);
			info_extranet.put("jsoninfo", jsonDataStr);
			info_extranet_obj.put("register", info_extranet);
			
			this.formService.saveFormDataJson(info_extranet_obj.toString());
			
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(isSuccess);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
}
