package com.bizduo.zflow.quartz;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.wdit.IRequestService;
import com.bizduo.zflow.util.HttpUtils;
import com.bizduo.zflow.util.TimeUitl;
import com.bizduo.zflow.util.TimeUtil;
import com.bizduo.zflow.util.wdit.GenerateJsonQuartzUtil;


public class IntranetQuartz {

	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private IRequestService requestService;
	@Autowired
	private IFormService formService;
	
	/**
	 * 生成内网数据保存到内网,同时推送到外网
	 * @throws ParseException
	 * @throws SQLException
	 */
	public void cogradient()  {
		try {

			//获取内网上一次的生成时间
			ResourceBundle WB = ResourceBundle.getBundle("synchronizeConfig", Locale.getDefault());
			String jdbcUrl= WB.getString("syn.jdbcUrl");
			String databaseUsername= WB.getString("syn.user");
			String databasePassword= WB.getString("syn.password");
			String intranetCogradient_url=WB.getString("syn.intranet_post_data_to_extranet");
			JSONObject info_jsonObject=this.requestService.getDataByIdSyn(jdbcUrl, databaseUsername, databasePassword, "WDIT_Info"," type=4 order by exportDate desc limit 0,1");
			System.out.println(info_jsonObject.toString());
			String nowDateStr="";
			if(info_jsonObject.getJSONArray("results").length()>0){
				if(info_jsonObject.getJSONArray("results").getJSONObject(0).has("exportDate")){
					nowDateStr = info_jsonObject.getJSONArray("results").getJSONObject(0).getString("exportDate");
				}
			}
			String condition=" status in (205,302,303,304,305,402,403,404,405,502,503,504,505)";
			if (StringUtils.isNotBlank(nowDateStr)) {
				condition+=" and modifyDate >'"+nowDateStr+"'";
			}
			condition+=" and acceptanceNumber is not null";
			 
			String subcondition1=" request_id in (select id from  wdit_company_request  where  status in (205,302,303,304,305,402,403,404,405,502,503,504,505) and acceptanceNumber is not null ) ";
			if (nowDateStr!=null&&!nowDateStr.equals("")) {
				subcondition1+=" and modifyDate>'"+nowDateStr+"'";
			}

			String  nowDate= TimeUitl.getTimeLongTime(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");
			JSONArray results = GenerateJsonQuartzUtil.getApprovalJson(jdbcUrl, databaseUsername, databasePassword, 2, requestService, condition,subcondition1); 
			//保存到本地info
			if (results!=null&&results.length()>0) {
				//生成的json不为null并且里面有数据,就证明生成成功,所以isSuccess直接是1
				int isSuccess=1;
				JSONObject saveInfo = new JSONObject();
				JSONObject register = new JSONObject();
				saveInfo.put("formId", 84);
				register.put("isSuccess", isSuccess);
				register.put("jsoninfo", results.toString());
				register.put("exportDate", nowDate);
				register.put("type", 4);
				saveInfo.put("register", register);
				try {
					this.formService.saveFormDataJsonSyn(jdbcUrl, databaseUsername, databasePassword, saveInfo.toString());
					//包装数据
					JSONObject infoBySyn = this.requestService.getInfoBySyn(jdbcUrl, databaseUsername, databasePassword, "where type = 4 order by exportDate desc limit 0,1");
					JSONObject jsonObject = new JSONObject();
					JSONArray jsonResults = new JSONArray();
				
					jsonResults.put(infoBySyn);
					jsonObject.put("code", 1);
					jsonObject.put("results", jsonResults);
				
					//把数据post到外网
					Map<String,String> paramMap=new HashMap<String,String>();
					paramMap.put("type", "3");
					paramMap.put("jsonDataStr", jsonObject.toString());
					String postRequest = HttpUtils.postRequest(intranetCogradient_url, paramMap);
					if (postRequest.equals("1")) {
						System.out.println("=======================>内网生成数据并且推送给外网成功");
					}else{
						System.out.println("=======================>内网生成数据并且推送给外网失败");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("=======================>内网生成数据并且推送给外网失败_error");
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	
	/**
	 * 内网发送请求同步数据到内网
	 * @throws com.google.protobuf.TextFormat.ParseException 
	 * @throws Exception 
	 */
	public void cogradientExtranetJson()  {
		try {

			String nowDateStr="";
			ResourceBundle WB = ResourceBundle.getBundle("synchronizeConfig", Locale.getDefault());
			String jdbcUrl = WB.getString("syn.jdbcUrl");
			String databaseUsername = WB.getString("syn.user");
			String databasePassword = WB.getString("syn.password");
			String housing_management_url= WB.getString("syn.intranet_take_from_extranet");
			//获取内网上一次导入时间
			JSONObject infoBySyn = this.requestService.getInfoBySyn(jdbcUrl, databaseUsername, databasePassword, "where type=2 order by importDate desc limit 0,1");
			if (infoBySyn.has("importDateLong")) {
				//nowDateStr = Long.toString(infoBySyn.getLong("importDateLong"));
				Calendar calendar = Calendar.getInstance();   
				calendar.setTime(new Date(infoBySyn.getLong("importDateLong")));   
				calendar.add(Calendar.SECOND,-20); 
				nowDateStr = Long.toString(calendar.getTime().getTime()) ;
			}
			Map<String, String>paramMap=new HashMap<String, String>();
			if (StringUtils.isNotBlank(nowDateStr)) {
				paramMap.put("nowDateStr", nowDateStr);
			}
			paramMap.put("nowLong", Long.toString(new Date().getTime()) );
			paramMap.put("type", "1");
			//发送请求,获取外网生成的数据
			String results = HttpUtils.postRequest(housing_management_url, paramMap);
			JSONObject jsonObject = new JSONObject(results);
			if (jsonObject.getJSONArray("results").length()<1) {
				if(jsonObject.has("code")){
					int code=jsonObject.getInt("code");
					if(code==0 && jsonObject.has("errorMsg")){
						System.out.println(jsonObject.getString("errorMsg"));
					}
					
				}
				return ;//没数据,不再运行
			}
			//保存数据到本地
			Map<String,String> map=new HashMap<String, String>();
			map.put("type", "1");
		    if(StringUtils.isNotBlank(nowDateStr))
		    	map.put("nowDate",Long.toString(TimeUtil.subOneDay(new Date(Long.parseLong(nowDateStr))).getTime()) ); 

			String  nowDate= TimeUitl.getTimeLongTime(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");
			int isSuccess = GenerateJsonQuartzUtil.saveImportRequestData(requestService, formService, WB, results,map);
			//保存数据到内网info
			JSONObject saveInfo = new JSONObject();
			JSONObject register = new JSONObject();
			register.put("isSuccess", isSuccess);
			register.put("jsoninfo", results);
			register.put("importDate", nowDate);
			register.put("type", 2);
			saveInfo.put("formId", 84);
			saveInfo.put("register", register);
			this.formService.saveFormDataJsonSyn(jdbcUrl, databaseUsername, databasePassword, saveInfo.toString());
			System.out.println("================>获取房管局json并保存到内网");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
