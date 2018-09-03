package com.bizduo.zflow.controller.weixin;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.domain.tableData.DataTableToPage;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.weixin.IWeixinService;

@Controller
@RequestMapping(value = "/wxflows")
public class WexinJbpmController {
	@Autowired
	private IWeixinService weixinService;
	@Autowired
	private IFormService formService;
	
	//跳转报名
	@RequestMapping(value = "wxsignup", method = RequestMethod.GET)
	public ModelAndView wxSignup(@RequestParam(value = "openid", required = true) String openid,@RequestParam(value = "activitycode", required = true) String activitycode) throws JSONException{
		JSONObject tempObj=null;
		ModelAndView mav = null;
		String name="",telephone="",email="",company="",createdate="",activityname="";
		//查询活动信息
		JSONArray activityJsonArray = weixinService.QueryActivity(activitycode);
		if(activityJsonArray.length() > 0) {
			try {
				tempObj = activityJsonArray.getJSONObject(0);
				activityname = (String)tempObj.get("activityname");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		else
		{
			mav = new ModelAndView("/weixinsignup/WeixinErrorHtml");
			mav.addObject("errorcode",1001);
			mav.addObject("errormessage","Error[1001]:您应用的活动编号不存在:["+activitycode+"]");	
		}
			
		
		//判断是否已经登记过用户信息
		JSONArray userJsonArray = weixinService.QuerySignupInfoByUser(openid);
		if(userJsonArray.length() > 0) {
			try {
				tempObj = userJsonArray.getJSONObject(0);
				name = (String)tempObj.get("name");
				telephone = (String)tempObj.get("telephone");
				email = (String)tempObj.get("email");
				company = (String)tempObj.get("company");
				//判断是否已报名参加此活动
				JSONArray userActivityJsonArray = weixinService.QuerySignupInfoByUserActivity(openid,activitycode);
				if(userActivityJsonArray.length() > 0) {
					tempObj = userActivityJsonArray.getJSONObject(0);
					createdate= (String)tempObj.get("createdate");
					mav = new ModelAndView("/weixinsignup/WeixinSignupDupTip");
					mav.addObject("zformId", 45);
					mav.addObject("openid", openid);
					mav.addObject("activitycode", activitycode);
					mav.addObject("activityname", activityname);
					mav.addObject("name", name);
					mav.addObject("telephone", telephone);
					mav.addObject("email", email);
					mav.addObject("company", company);
					mav.addObject("createdate", createdate);
				}
				else
				{
					mav = new ModelAndView("/weixinsignup/WeixinSignupFormHtml");
					mav.addObject("zformId", 45);
					mav.addObject("openid", openid);
					mav.addObject("activitycode", activitycode);					
					mav.addObject("activityname", activityname);
					mav.addObject("name", name);
					mav.addObject("telephone", telephone);
					mav.addObject("email", email);
					mav.addObject("company", company);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		else
		{
		mav = new ModelAndView("/weixinsignup/WeixinSignupFormHtml");
		mav.addObject("zformId", 45);
		mav.addObject("openid", openid);
		mav.addObject("activitycode", activitycode);
		}

		return mav;
	}	
	
	
	//填单
		@SuppressWarnings("unused")
		@RequestMapping(value = "/savesignup", method = RequestMethod.POST)
		@ResponseBody
		public  Map<String, Object> signup(@RequestParam(value = "jsonString", required = true) String jsonString) throws JSONException{
			
		 
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				JSONObject jsonObj = new JSONObject(jsonString); 
				JSONObject tempJsonObj = jsonObj.getJSONObject("register");
				 
				Long formId = jsonObj.getLong("formId");
				if(null == formId || 0L == formId){
					map.put("errorMsg", "表单标识未传入"); 
				}else{
					//保存表单
					Long dataId= this.formService.saveFormData(formId, jsonObj); 
				} 
			} catch (Exception e) {
				map.put("errorMsg", "保存错误"); 
			}
			map.put("successMsg", "保存成功");  
			return map;
		}
			
		//跳转报名
		@RequestMapping(value = "wxsignupresult", method = RequestMethod.GET)
		public ModelAndView wxSignupResult() throws JSONException{
			ModelAndView mav = new ModelAndView("/weixinsignup/WeixinSignupResult");
			mav.addObject("zformId", 45);
			return mav;
		}	
		
//		//报名列表
//		@RequestMapping(value = "wxsignuplist", method = RequestMethod.GET)
//		public ModelAndView wxSignupList() throws JSONException{
//			ModelAndView mav = new ModelAndView("/weixinsignup/WeixinSignupList");
//			mav.addObject("zformId", 45);
//			return mav;
//		}	
	
		///显示报名数据列表  
		@SuppressWarnings("unused")
		@RequestMapping(value = "/wxsignuplist")
		@ResponseBody
		public DataTableToPage formTableData(
				@RequestParam(value = "tableName", required = true) String tableName,
				@RequestParam(value = "parameter", required = true) String parameter,
				@RequestParam(value = "pageIndex", required = true) int pageIndex 
				){

			Map<String,String> fieldMap=new HashMap<String, String>();
			fieldMap.put("id", "id");
			fieldMap.put("createDate", "createDate");
			fieldMap.put("name", "name");
			fieldMap.put("company", "company");
			fieldMap.put("department", "department");
			fieldMap.put("competenceCenter", "competenceCenter");
			fieldMap.put("revenue", "revenue");
			fieldMap.put("phone1", "phone1");
			fieldMap.put("phone2", "phone2");
			fieldMap.put("Topics", "Topics");
			fieldMap.put("specialRFC", "specialRFC");
			fieldMap.put("note", "note");
			fieldMap.put("status", "status");  
			
			String sql="select id,createDate,name,company,department,competenceCenter,revenue,phone1," +
					" phone2, Topics,specialRFC,note,status from "+tableName +"  "+parameter;
			
			//return  clientCallService.getClientCallDatas(fieldMap,sql,pageIndex);
			return null;
		}	
	
}
