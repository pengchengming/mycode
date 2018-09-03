package com.bizduo.zflow.controller.WDIT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.form.SelectTableList;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.domain.tableData.DataTableToPage;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.ISelectTableListService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.TimeUtil;

@Controller
@RequestMapping("/hmcheck")
public class HMCheckController {

	@RequestMapping("/prelist")
	public String prelist(){
		return "/wdit/hourse/prelist";
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	public ISelectTableListService selectTableListService;
	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;
	
	@RequestMapping("/companyapproval")
	public ModelAndView companyapproval (@RequestParam(value="requreid",required=true)Integer id,
			                             @RequestParam(value="companystutus",required=false)Integer companystutus){
		String viewname = "/wdit/hourse/companyview";
		if(companystutus == null || companystutus == 101 || companystutus==105){//待审核
			viewname = "/wdit/hourse/companyapproval";
		}
		ModelAndView mv = new ModelAndView(viewname);
		mv.addObject("requreid", id);
		mv.addObject("companystutus", companystutus);
		mv.addObject("type", 1);
		return mv;
	}
	
	@RequestMapping("/userapproval")
	public String userapproval (@RequestParam(value="requestUserId",required=true)Integer requestUserId,
			                    @RequestParam(value="status",required=false)Integer status,
			                    @RequestParam(value="requreid",required=false)Integer requreid,
			                    @RequestParam(value="companystutus",required=false)Integer companystutus,Model model){
		model.addAttribute("requestUserId", requestUserId);
		model.addAttribute("requreid", requreid);
//		String  viewname = "/wdit/hourse/userapproval";
//		if(status != null && (status ==1202 || status==1203)||(companystutus != null) && companystutus == 103)
//		    viewname = "/wdit/hourse/userview";	
		
		String  viewname = "/wdit/hourse/userview";
		if(status != null && (status ==1201 || status ==1204) )
		    viewname = "/wdit/hourse/userapproval";	
		if(companystutus != null && companystutus == 103)
			 viewname = "/wdit/hourse/userview";
		return viewname;
	}
	
	@RequestMapping("/printcompany")//打印公司
	public String printcompany(@RequestParam(value="requreid",required=true)Integer requreid,
			                   @RequestParam(value="step",required=false)Integer step,Model model){
        model.addAttribute("requreid",requreid);
        model.addAttribute("step",step);
        return "/wdit/hourse/printcompany";
	}
	
	
	
	@RequestMapping("/printatta")//打印附件
	public String printatta(@RequestParam(value="requreid",required=true)Integer requreid,
			                @RequestParam(value="step",required=false)Integer step,Model model){
        model.addAttribute("requreid",requreid);
        model.addAttribute("step",step);
        return "/wdit/hourse/printatta";
	}
	
	
	/**
	 * 房管局-资质审核
	 * @return
	 */
	@RequestMapping("/hmapprovallist")
	public String hmapprovallist(){
		return "/wdit/hourse/hmapprovallist";
	}
	
	/**
	 * 房管局-资质审核 查看/审核
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping("/hmshowcompany")
	public ModelAndView hmshowcompany (@RequestParam(value="id",required=true)Integer id,Integer type){
		ModelAndView mv = new ModelAndView("/wdit/hourse/hmshowcompany");
		mv.addObject("id", id);
		mv.addObject("type", type);
		return mv;
	}
	
	/**
	 * 房管局—资质审核人员审核
	 * @param requestUserId
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping("/hmuserapproval")
	public String hmuserapproval (@RequestParam(value="requestUserId",required=true)Integer requestUserId,
			                    @RequestParam(value="status",required=false)Integer status,Model model){
		model.addAttribute("requestUserId", requestUserId);
		String viewname = "/wdit/hourse/hmuserapproval";
		return viewname;
	} 
	
	/**
	 * 房管局-现场审核
	 * @return
	 */
	@RequestMapping("/sceneapproval")
	public String sceneapproval(){
		return "/wdit/hourse/sceneapproval";
	}
	
	/**
	 * 房管局-现场审核 查看/审核
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping("/sceneshowcompany")
	public ModelAndView sceneshowcompany (@RequestParam(value="id",required=true)Integer id,Integer type){
		ModelAndView mv = new ModelAndView("/wdit/hourse/sceneshowcompany");
		mv.addObject("id", id);
		mv.addObject("type", type);
		return mv;
	}
	
	/**
	 * 房管局—现场审核人员审核
	 * @param requestUserId
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping("/sceneuserapproval")
	public String sceneuserapproval (@RequestParam(value="requestUserId",required=true)Integer requestUserId,
			                    @RequestParam(value="status",required=false)Integer status,Model model){
		model.addAttribute("requestUserId", requestUserId);
		String viewname = "/wdit/hourse/sceneuserapproval";
		return viewname;
	} 
	
	/**
	 * 查看企业
	 * @return
	 */
	@RequestMapping("/companylist")
	public String companylist(){
		return "/wdit/hourse/companylist";
	}
	
	/**
	 * 查看企业详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/showcompany")
	public ModelAndView showcompany (@RequestParam(value="id",required=true)Integer id){
		ModelAndView mv = new ModelAndView("/wdit/hourse/showcompany");
		mv.addObject("id", id);
		return mv;
	}
	
	/**
	 * 查看接口数据请求
	 * @param id
	 */
	@RequestMapping("/addinfo")
	public void addinfo(){
		//导入接口
		try {
		JSONObject infoJson= new JSONObject();
		infoJson.put("formId", 73);
		JSONObject registerJson= new JSONObject();
		registerJson.put("code", "");
		registerJson.put("type", 1);
		registerJson.put("synchronizationObject", 1);
		Date nowDate= new Date();
		registerJson.put("exportDate", TimeUtil.date2Str(nowDate));
		infoJson.put("register", registerJson);
 
		 
		//查出所有数据
		JSONObject request_Json= new JSONObject();
		request_Json.put("formId",61 );
		String requestListStr= getDataById("wdit_company_request",null,null);
		JSONObject requestTable= new JSONObject(requestListStr);
		if(requestTable.has("code")&&requestTable.getInt("code")==1&&requestTable.has("results")){
			JSONArray results= requestTable.getJSONArray("results");
			JSONArray results1= new JSONArray();
			for (int i = 0; i < results.length(); i++) {
				//添入表数据  wdit_company_request
				JSONObject requestJson=results.getJSONObject(i); 
				JSONObject request = new JSONObject();
				JSONArray resultsarray= new JSONArray();
				Iterator it = requestJson.keys();
				Map<String, Object> request_map = new HashMap<String, Object>();
				while(it.hasNext()){
					String key=(String)it.next();
					if(requestJson.has(key))
						request_map.put(key, requestJson.get(key));
					else
						request_map.put(key, "");
				}
				if(StringUtils.isNotBlank(request_map.get("id").toString()))
					request.put("id", Integer.parseInt(request_map.get("id").toString()));
				else
					request.put("id","");
				if(StringUtils.isNotBlank(request_map.get("companyId").toString()))
					request.put("companyId", Integer.parseInt(request_map.get("companyId").toString()));
				else
					request.put("companyId","");
				if(StringUtils.isNotBlank(request_map.get("acceptanceNumber").toString()))
					request.put("acceptanceNumber", request_map.get("acceptanceNumber"));
				else
					request.put("acceptanceNumber","");
				if(StringUtils.isNotBlank(request_map.get("approvalNum").toString()))
					request.put("approvalNum", Integer.parseInt(request_map.get("approvalNum").toString()));
				else
					request.put("approvalNum","");
				if(StringUtils.isNotBlank(request_map.get("status").toString()))
					request.put("status", Integer.parseInt(request_map.get("status").toString()));
				else
					request.put("status","");
				if(StringUtils.isNotBlank(request_map.get("lastOperationTime").toString()))
					request.put("lastOperationTime", Long.parseLong(request_map.get("lastOperationTime").toString()));
				else
					request.put("lastOperationTime","");
				if(StringUtils.isNotBlank(request_map.get("code").toString()))
					request.put("code", request_map.get("code"));
				else
					request.put("code","");
				if(StringUtils.isNotBlank(request_map.get("isApplication").toString()))
					request.put("isApplication", Integer.parseInt(request_map.get("isApplication").toString()));
				else
					request.put("isApplication","");
				if(StringUtils.isNotBlank(request_map.get("canApplicationNum").toString()))
					request.put("canApplicationNum", Integer.parseInt(request_map.get("canApplicationNum").toString()));
				else
					request.put("canApplicationNum","");
				if(StringUtils.isNotBlank(request_map.get("applicant").toString()))
					request.put("applicant", request_map.get("applicant"));
				else
					request.put("applicant","");
				if(StringUtils.isNotBlank(request_map.get("registerAddress").toString()))
					request.put("registerAddress", request_map.get("registerAddress").toString());
				else
					request.put("registerAddress","");
				if(StringUtils.isNotBlank(request_map.get("officeAddress").toString()))
					request.put("officeAddress", request_map.get("officeAddress").toString());
				else
					request.put("officeAddress","");
				if(StringUtils.isNotBlank(request_map.get("registerMoney").toString()))
					request.put("registerMoney", Double.parseDouble(request_map.get("registerMoney").toString()));
				else
					request.put("registerMoney","");
				if(StringUtils.isNotBlank(request_map.get("oneYearIsTaxAmount").toString()))
					request.put("oneYearIsTaxAmount", Double.parseDouble(request_map.get("oneYearIsTaxAmount").toString()));
				else
					request.put("oneYearIsTaxAmount","");
				if(StringUtils.isNotBlank(request_map.get("staffNum").toString()))
					request.put("staffNum", Integer.parseInt(request_map.get("staffNum").toString()));
				else
					request.put("staffNum","");
				if(StringUtils.isNotBlank(request_map.get("rent").toString()))
					request.put("rent", Integer.parseInt(request_map.get("rent").toString()));
				else
					request.put("rent","");
				if(StringUtils.isNotBlank(request_map.get("companyClassification").toString()))
					request.put("companyClassification", Integer.parseInt(request_map.get("companyClassification").toString()));
				else
					request.put("companyClassification","");
				if(StringUtils.isNotBlank(request_map.get("linkman").toString()))
					request.put("linkman", request_map.get("linkman").toString());
				else
					request.put("linkman","");
				if(StringUtils.isNotBlank(request_map.get("phone").toString()))
					request.put("phone", request_map.get("phone").toString());
				else
					request.put("phone","");
				if(StringUtils.isNotBlank(request_map.get("email").toString()))
					request.put("email", request_map.get("email").toString());
				else
					request.put("email","");
				if(StringUtils.isNotBlank(request_map.get("tel").toString()))
					request.put("tel", request_map.get("tel").toString());
				else
					request.put("tel","");
				if(StringUtils.isNotBlank(request_map.get("applicationCompany").toString()))
					request.put("applicationCompany", request_map.get("applicationCompany").toString());
				else
					request.put("applicationCompany","");
				if(StringUtils.isNotBlank(request_map.get("isReturn").toString()))
					request.put("isReturn", Integer.parseInt(request_map.get("isReturn").toString()));
				else
					request.put("isReturn","");
				if(StringUtils.isNotBlank(request_map.get("createDate").toString()))
					request.put("createDate", request_map.get("createDate"));
				else
					request.put("createDate","");
				if(StringUtils.isNotBlank(request_map.get("createBy").toString()))
					request.put("createBy", Integer.parseInt(request_map.get("createBy").toString()));
				else
					request.put("createBy","");
				if(StringUtils.isNotBlank(request_map.get("modifyBy").toString()))
					request.put("modifyBy", Integer.parseInt(request_map.get("modifyBy").toString()));
				else
					request.put("modifyBy","");
				if(StringUtils.isNotBlank(request_map.get("modifyDate").toString()))
					request.put("modifyDate", request_map.get("modifyDate"));
				else
					request.put("modifyDate","");
				if(StringUtils.isNotBlank(request_map.get("pickDwelling").toString()))
					request.put("pickDwelling", Integer.parseInt(request_map.get("modifyBy").toString()));
				else
					request.put("pickDwelling","");
				
				if(requestJson.has("id")){
					//wdit_company_request_photo
					//***************************企业请求照片********************
					JSONObject detail= new JSONObject();
					detail.put("parentId", "request_id");
					detail.put("formId", 68);
					String photocondition=" request_id ="+Integer.parseInt(request_map.get("id").toString());
					String requestphotoListStr= getDataById("wdit_company_request_photo",null,photocondition);
					JSONObject requestphotoTable= new JSONObject(requestphotoListStr);
					JSONArray results2= requestphotoTable.getJSONArray("results");	
					JSONArray requestPhotoArray=new  JSONArray();
					for (int j = 0; j < results2.length(); j++) {
						JSONObject companyrequestphoto = new JSONObject();
						JSONObject requestJson2=results2.getJSONObject(j);
						Map<String, Object> request_map2 = new HashMap<String, Object>();
						Iterator it0 = requestJson2.keys();
						 while(it0.hasNext()){
								String key=(String)it0.next();
								if(requestJson2.has(key))
									request_map2.put(key, requestJson2.get(key));
								else
									request_map2.put(key, "");
						}
						if(StringUtils.isNotBlank(request_map2.get("id").toString()))
							companyrequestphoto.put("id", Integer.parseInt(request_map2.get("id").toString()));
						else
							companyrequestphoto.put("id","");
						if(StringUtils.isNotBlank(request_map2.get("request_id").toString()))
							companyrequestphoto.put("request_id", request_map2.get("request_id").toString());
						else
							companyrequestphoto.put("request_id","");
						if(StringUtils.isNotBlank(request_map2.get("authorization").toString()))
							companyrequestphoto.put("authorization", request_map2.get("authorization").toString());
						else
							companyrequestphoto.put("authorization","");
						if(StringUtils.isNotBlank(request_map2.get("authorizationSmallPhoto").toString()))
							companyrequestphoto.put("authorizationSmallPhoto", request_map2.get("authorizationSmallPhoto").toString());
						else
							companyrequestphoto.put("authorizationSmallPhoto","");
						if(StringUtils.isNotBlank(request_map2.get("authorizationName").toString()))
							companyrequestphoto.put("authorizationName", request_map2.get("authorizationName").toString());
						else
							companyrequestphoto.put("authorizationName","");
						if(StringUtils.isNotBlank(request_map2.get("taxationPhoto").toString()))
							companyrequestphoto.put("taxationPhoto", request_map2.get("taxationPhoto").toString());
						else
							companyrequestphoto.put("taxationPhoto","");
						if(StringUtils.isNotBlank(request_map2.get("taxationPhotoName").toString()))
							companyrequestphoto.put("taxationPhotoName", request_map2.get("taxationPhotoName").toString());
						else
							companyrequestphoto.put("taxationPhotoName","");
						if(StringUtils.isNotBlank(request_map2.get("taxationSmallPhoto").toString()))
							companyrequestphoto.put("taxationSmallPhoto", request_map2.get("taxationSmallPhoto").toString());
						else
							companyrequestphoto.put("taxationSmallPhoto","");
						if(StringUtils.isNotBlank(request_map2.get("taxRegistersCardNumber").toString()))
							companyrequestphoto.put("taxRegistersCardNumber", request_map2.get("taxRegistersCardNumber").toString());
						else						 
							companyrequestphoto.put("taxRegistersCardNumber","");
						if(StringUtils.isNotBlank(request_map2.get("organizationPhoto").toString()))
							companyrequestphoto.put("organizationPhoto", request_map2.get("organizationPhoto").toString());
						else
							companyrequestphoto.put("organizationPhoto","");
						if(StringUtils.isNotBlank(request_map2.get("organizationPhotoName").toString()))
							companyrequestphoto.put("organizationPhotoName", request_map2.get("organizationPhotoName").toString());
						else
							companyrequestphoto.put("organizationPhotoName","");
						if(StringUtils.isNotBlank(request_map2.get("organizationSmallPhoto").toString()))
							companyrequestphoto.put("organizationSmallPhoto", request_map2.get("organizationSmallPhoto").toString());
						else
							companyrequestphoto.put("organizationSmallPhoto","");
						if(StringUtils.isNotBlank(request_map2.get("organization").toString()))
							companyrequestphoto.put("organization", request_map2.get("organization").toString());
						else
							companyrequestphoto.put("organization","");
						if(StringUtils.isNotBlank(request_map2.get("registrationNumber").toString()))
							companyrequestphoto.put("registrationNumber", request_map2.get("registrationNumber").toString());
						else
							companyrequestphoto.put("registrationNumber","");
						if(StringUtils.isNotBlank(request_map2.get("businessLicense").toString()))
							companyrequestphoto.put("businessLicense", request_map2.get("businessLicense").toString());
						else
							companyrequestphoto.put("businessLicense","");
						if(StringUtils.isNotBlank(request_map2.get("businessLicenseSmallPhoto").toString()))
							companyrequestphoto.put("businessLicenseSmallPhoto", request_map2.get("businessLicenseSmallPhoto").toString());
						else
							companyrequestphoto.put("businessLicenseSmallPhoto","");
						if(StringUtils.isNotBlank(request_map2.get("businessLicenseName").toString()))
							companyrequestphoto.put("businessLicenseName", request_map2.get("businessLicenseName").toString());
						else
							companyrequestphoto.put("businessLicenseName","");
						if(StringUtils.isNotBlank(request_map2.get("unifiedSocialCreditCode").toString()))
							companyrequestphoto.put("unifiedSocialCreditCode", request_map2.get("unifiedSocialCreditCode").toString());
						else
							companyrequestphoto.put("unifiedSocialCreditCode","");
						if(StringUtils.isNotBlank(request_map2.get("licenseNumber").toString()))
							companyrequestphoto.put("licenseNumber", request_map2.get("licenseNumber").toString());
						else
							companyrequestphoto.put("licenseNumber","");
						if(StringUtils.isNotBlank(request_map2.get("photoType").toString()))
							companyrequestphoto.put("photoType", Integer.parseInt(request_map2.get("photoType").toString()));
						else
							companyrequestphoto.put("photoType","");
						if(StringUtils.isNotBlank(request_map2.get("companyId").toString()))
							companyrequestphoto.put("companyId", Integer.parseInt(request_map2.get("companyId").toString()));
						else
							companyrequestphoto.put("companyId","");
						if(StringUtils.isNotBlank(request_map2.get("createDate").toString()))
							companyrequestphoto.put("createDate", request_map2.get("createDate"));
						else
							companyrequestphoto.put("createDate","");
						if(StringUtils.isNotBlank(request_map2.get("createBy").toString()))
							companyrequestphoto.put("createBy", Integer.parseInt(request_map2.get("createBy").toString()));
						else
							companyrequestphoto.put("createBy","");
						if(StringUtils.isNotBlank(request_map2.get("modifyBy").toString()))
							companyrequestphoto.put("modifyBy", Integer.parseInt(request_map2.get("modifyBy").toString()));
						else
							companyrequestphoto.put("modifyBy","");
						if(StringUtils.isNotBlank(request_map2.get("modifyDate").toString()))
							companyrequestphoto.put("modifyDate", request_map2.get("modifyDate"));
						else
							companyrequestphoto.put("modifyDate","");
						requestPhotoArray.put(j,companyrequestphoto);
					}
					detail.put("array", requestPhotoArray);
					resultsarray.put(0,detail);
					//wdit_company_request_user
					//***************************用户请求***************************
					JSONObject detail2= new JSONObject();
					detail2.put("parentId", "request_id");
					detail2.put("formId", 62);
					String conditionuser=" request_id ="+Integer.parseInt(request_map.get("id").toString());
					String requestuserListStr= getDataById("wdit_company_request_user",null,conditionuser);
					JSONObject requestuserTable= new JSONObject(requestuserListStr);
					JSONArray results3= requestuserTable.getJSONArray("results");
					JSONArray requestUserArray= new JSONArray();
					for (int a = 0; a < results3.length(); a++) {
						JSONObject companyrequestuser = new JSONObject();
						JSONArray requestUserArray2= new JSONArray();
						JSONObject requestJson3=results3.getJSONObject(a);
						Iterator it2 = requestJson3.keys();
						Map<String, Object> request_map3 = new HashMap<String, Object>();
						 while(it2.hasNext()){
								String key=(String)it2.next();
								if(requestJson3.has(key))
									request_map3.put(key, requestJson3.get(key));
								else
									request_map3.put(key, "");
							}
						 	if(StringUtils.isNotBlank(request_map3.get("id").toString()))
						 		companyrequestuser.put("id", Integer.parseInt(request_map3.get("id").toString()));
							else
								companyrequestuser.put("id","");
							if(StringUtils.isNotBlank(request_map3.get("companyId").toString()))
								companyrequestuser.put("companyId", Integer.parseInt(request_map3.get("companyId").toString()));
							else
								companyrequestuser.put("companyId","");
							if(StringUtils.isNotBlank(request_map3.get("userName").toString()))
								companyrequestuser.put("userName", request_map3.get("userName").toString());
							else
								companyrequestuser.put("userName","");
							if(StringUtils.isNotBlank(request_map3.get("request_id").toString()))
								companyrequestuser.put("request_id", request_map3.get("request_id").toString());
							else
								companyrequestuser.put("request_id","");
							if(StringUtils.isNotBlank(request_map3.get("isReturn").toString()))
								companyrequestuser.put("isReturn", Integer.parseInt(request_map3.get("isReturn").toString()));
							else
								companyrequestuser.put("isReturn","");
							if(StringUtils.isNotBlank(request_map3.get("status").toString()))
								companyrequestuser.put("status", Integer.parseInt(request_map3.get("status").toString()));
							else
								companyrequestuser.put("status","");
							if(StringUtils.isNotBlank(request_map3.get("applyForFamily").toString()))
								companyrequestuser.put("applyForFamily", Integer.parseInt(request_map3.get("applyForFamily").toString()));
							else
								companyrequestuser.put("applyForFamily","");
							if(StringUtils.isNotBlank(request_map3.get("housingConditionsInTheCity").toString()))
								companyrequestuser.put("housingConditionsInTheCity", Integer.parseInt(request_map3.get("housingConditionsInTheCity").toString()));
							else
								companyrequestuser.put("housingConditionsInTheCity","");
							if(StringUtils.isNotBlank(request_map3.get("maritalStatus").toString()))
								companyrequestuser.put("maritalStatus", Integer.parseInt(request_map3.get("maritalStatus").toString()));
							else
								companyrequestuser.put("maritalStatus","");
							if(StringUtils.isNotBlank(request_map3.get("placeOfDomicile").toString()))
								companyrequestuser.put("placeOfDomicile", Integer.parseInt(request_map3.get("placeOfDomicile").toString()));
							else
								companyrequestuser.put("placeOfDomicile","");
							if(StringUtils.isNotBlank(request_map3.get("acceptanceNumber").toString()))
								companyrequestuser.put("acceptanceNumber", Integer.parseInt(request_map3.get("acceptanceNumber").toString()));
							else
								companyrequestuser.put("acceptanceNumber","");
							if(StringUtils.isNotBlank(request_map3.get("pickDwelling").toString()))
								companyrequestuser.put("pickDwelling", Integer.parseInt(request_map3.get("pickDwelling").toString()));
							else
								companyrequestuser.put("pickDwelling","");
							if(StringUtils.isNotBlank(request_map3.get("address").toString()))
								companyrequestuser.put("address", request_map3.get("address").toString());
							else
								companyrequestuser.put("address","");
							if(StringUtils.isNotBlank(request_map3.get("permanentAddress").toString()))
								companyrequestuser.put("permanentAddress", request_map3.get("permanentAddress").toString());
							else
								companyrequestuser.put("permanentAddress","");
							if(StringUtils.isNotBlank(request_map3.get("housingAccumulationFundAccount").toString()))
								companyrequestuser.put("housingAccumulationFundAccount", request_map3.get("housingAccumulationFundAccount").toString());
							else
								companyrequestuser.put("housingAccumulationFundAccount","");
							if(StringUtils.isNotBlank(request_map3.get("residencePermitNumber").toString()))
								companyrequestuser.put("residencePermitNumber", request_map3.get("residencePermitNumber").toString());
							else
								companyrequestuser.put("residencePermitNumber","");
							if(StringUtils.isNotBlank(request_map3.get("applicantPhone").toString()))
								companyrequestuser.put("applicantPhone", request_map3.get("applicantPhone").toString());
							else
								companyrequestuser.put("applicantPhone","");
							if(StringUtils.isNotBlank(request_map3.get("identityCardNumber").toString()))
								companyrequestuser.put("identityCardNumber", request_map3.get("identityCardNumber").toString());
							else
								companyrequestuser.put("identityCardNumber","");
							if(StringUtils.isNotBlank(request_map3.get("createDate").toString()))
								companyrequestuser.put("createDate", request_map3.get("createDate"));
							else
								companyrequestuser.put("createDate","");
							if(StringUtils.isNotBlank(request_map3.get("createBy").toString()))
								companyrequestuser.put("createBy", Integer.parseInt(request_map3.get("createBy").toString()));
							else
								companyrequestuser.put("createBy","");
							if(StringUtils.isNotBlank(request_map3.get("modifyBy").toString()))
								companyrequestuser.put("modifyBy", Integer.parseInt(request_map3.get("modifyBy").toString()));
							else
								companyrequestuser.put("modifyBy","");
							if(StringUtils.isNotBlank(request_map3.get("modifyDate").toString()))
								companyrequestuser.put("modifyDate", request_map3.get("modifyDate"));
							else
								companyrequestuser.put("modifyDate","");
							
							//通过用户ID查询huosing关联表
							if(requestJson.has("id")){
								//WDIT_Company_Request_User_housing
								JSONObject detailuser1 = new JSONObject();
								//JSONArray detailuserArray1 = new  JSONArray();
								detailuser1.put("parentId", "requestUser_id");
								detailuser1.put("formId", 64);
								String conditionhousing =" requestUser_id ="+Integer.parseInt(request_map3.get("id").toString());
								String requeshousingtListStr = getDataById("WDIT_Company_Request_User_housing",null,conditionhousing);
								JSONObject requestshousingTable= new JSONObject(requeshousingtListStr);
								JSONArray results4= requestshousingTable.getJSONArray("results");
								JSONArray requestUserHousingArray= new JSONArray();
								//通过用户ID查询的多条housing数据
								for (int  k= 0; k < results4.length(); k++) {
									JSONObject companyrequestuserhousing = new JSONObject();
									JSONArray requestUserHousingArray2= new JSONArray();
									JSONObject requestJson4=results4.getJSONObject(k);
									Iterator it3 = requestJson4.keys();
									Map<String, Object> request_map4 = new HashMap<String, Object>();
									while(it3.hasNext()){
										String key=(String)it3.next();
										if(requestJson4.has(key))
											request_map4.put(key, requestJson4.get(key));
										else
											request_map4.put(key, "");
									}
									if(StringUtils.isNotBlank(request_map4.get("id").toString()))
										companyrequestuserhousing.put("id", Integer.parseInt(request_map4.get("id").toString()));
									else
										companyrequestuserhousing.put("id","");
									if(StringUtils.isNotBlank(request_map4.get("requestUser_id").toString()))
										companyrequestuserhousing.put("requestUser_id", Integer.parseInt(request_map4.get("requestUser_id").toString()));
									else
										companyrequestuserhousing.put("requestUser_id","");
									if(StringUtils.isNotBlank(request_map4.get("areaType").toString()))
										companyrequestuserhousing.put("areaType", Integer.parseInt(request_map4.get("areaType").toString()));
									else
										companyrequestuserhousing.put("areaType","");
									if(StringUtils.isNotBlank(request_map4.get("theHousingAllNumpeople").toString()))
										companyrequestuserhousing.put("theHousingAllNumpeople", Integer.parseInt(request_map4.get("theHousingAllNumpeople").toString()));
									else
										companyrequestuserhousing.put("theHousingAllNumpeople","");
									if(StringUtils.isNotBlank(request_map4.get("area").toString()))
										companyrequestuserhousing.put("area", Double.parseDouble(request_map4.get("area").toString()));
									else
										companyrequestuserhousing.put("area","");
									if(StringUtils.isNotBlank(request_map4.get("createDate").toString()))
										companyrequestuserhousing.put("createDate", request_map4.get("createDate"));
									else
										companyrequestuserhousing.put("createDate","");
									if(StringUtils.isNotBlank(request_map4.get("modifyDate").toString()))
										companyrequestuserhousing.put("modifyDate", request_map4.get("modifyDate"));
									else
										companyrequestuserhousing.put("modifyDate","");
									if(StringUtils.isNotBlank(request_map4.get("housingLocatedAddress").toString()))
										companyrequestuserhousing.put("housingLocatedAddress", request_map4.get("housingLocatedAddress").toString());
									else
										companyrequestuserhousing.put("housingLocatedAddress","");
									if(StringUtils.isNotBlank(request_map4.get("propertyOwner").toString()))
										companyrequestuserhousing.put("propertyOwner", request_map4.get("propertyOwner").toString());
									else
										companyrequestuserhousing.put("propertyOwner","");
									if(StringUtils.isNotBlank(request_map4.get("createBy").toString()))
										companyrequestuserhousing.put("createBy", Integer.parseInt(request_map4.get("createBy").toString()));
									else
										companyrequestuserhousing.put("createBy","");
									if(StringUtils.isNotBlank(request_map4.get("modifyBy").toString()))
										companyrequestuserhousing.put("modifyBy", Integer.parseInt(request_map4.get("modifyBy").toString()));
									else
										companyrequestuserhousing.put("modifyBy","");
									
									//通过housing表ID查询图片
									JSONObject detailuserhousing = new  JSONObject();
									detailuserhousing.put("parentId", "user_housing_id");
									detailuserhousing.put("formId", 65);
									String condition2 =" user_housing_id ="+Integer.parseInt(request_map4.get("id").toString());
									String requestListStr21 = getDataById("WDIT_Company_Request_User_photo",null,condition2);
									JSONObject requestTable21= new JSONObject(requestListStr21);
									JSONArray results5= requestTable21.getJSONArray("results");
									JSONArray requestUserPhotoArray= new JSONArray();
									//通过用户ID查询的多条user photo数据
									for (int  l= 0; l < results5.length(); l++) {
										JSONObject companyrequestuserphoto = new JSONObject();
										JSONObject requestJsonphoto1=results5.getJSONObject(l);
										Map<String, Object> request_map5 = new HashMap<String, Object>();
										Iterator it4 = requestJsonphoto1.keys();
										while(it4.hasNext()){
											String key=(String)it4.next();
											if(requestJsonphoto1.has(key))
												request_map5.put(key, requestJsonphoto1.get(key));
											else
												request_map5.put(key, "");
										}
										if(StringUtils.isNotBlank(request_map5.get("id").toString()))
											companyrequestuserphoto.put("id", Integer.parseInt(request_map5.get("id").toString()));
										else
											companyrequestuserphoto.put("id","");
										if(StringUtils.isNotBlank(request_map5.get("requestUser_id").toString()))
											companyrequestuserphoto.put("requestUser_id", Integer.parseInt(request_map5.get("requestUser_id").toString()));
										else
											companyrequestuserphoto.put("requestUser_id","");
										if(StringUtils.isNotBlank(request_map5.get("request_id").toString()))
											companyrequestuserphoto.put("request_id", Integer.parseInt(request_map5.get("request_id").toString()));
										else
											companyrequestuserphoto.put("request_id","");
										if(StringUtils.isNotBlank(request_map5.get("user_relative_id").toString()))
											companyrequestuserphoto.put("user_relative_id", Integer.parseInt(request_map5.get("user_relative_id").toString()));
										else
											companyrequestuserphoto.put("user_relative_id","");
										if(StringUtils.isNotBlank(request_map5.get("user_housing_id").toString()))
											companyrequestuserphoto.put("user_housing_id", request_map5.get("user_housing_id").toString());
										else
											companyrequestuserphoto.put("user_housing_id","");
										if(StringUtils.isNotBlank(request_map5.get("type").toString()))
											companyrequestuserphoto.put("type", request_map5.get("type").toString());
										else
											companyrequestuserphoto.put("type","");
										if(StringUtils.isNotBlank(request_map5.get("smallPhoto").toString()))
											companyrequestuserphoto.put("smallPhoto", request_map5.get("smallPhoto").toString());
										else
											companyrequestuserphoto.put("smallPhoto","");
										if(StringUtils.isNotBlank(request_map5.get("name").toString()))
											companyrequestuserphoto.put("name", request_map5.get("name").toString());
										else
											companyrequestuserphoto.put("name","");
										if(StringUtils.isNotBlank(request_map5.get("photo").toString()))
											companyrequestuserphoto.put("photo", request_map5.get("photo").toString());
										else
											companyrequestuserphoto.put("photo","");
										if(StringUtils.isNotBlank(request_map5.get("createDate").toString()))
											companyrequestuserphoto.put("createDate", request_map5.get("createDate"));
										else
											companyrequestuserphoto.put("createDate","");
										if(StringUtils.isNotBlank(request_map5.get("modifyDate").toString()))
											companyrequestuserphoto.put("modifyDate", request_map5.get("modifyDate"));
										else
											companyrequestuserphoto.put("modifyDate","");
										if(StringUtils.isNotBlank(request_map5.get("createBy").toString()))
											companyrequestuserphoto.put("createBy", Integer.parseInt(request_map5.get("createBy").toString()));
										else
											companyrequestuserphoto.put("createBy","");
										if(StringUtils.isNotBlank(request_map5.get("modifyBy").toString()))
											companyrequestuserphoto.put("modifyBy", Integer.parseInt(request_map5.get("modifyBy").toString()));
										else
											companyrequestuserphoto.put("modifyBy","");
										requestUserPhotoArray.put(l,companyrequestuserphoto);
									}
									detailuserhousing.put("array", requestUserPhotoArray);
									requestUserHousingArray2.put(0,detailuserhousing);
									companyrequestuserhousing.put("detail", requestUserHousingArray2);
									//将一条housing表语句添加到用户表
									requestUserHousingArray.put(k, companyrequestuserhousing);
								}
								
								detailuser1.put("array",requestUserHousingArray);
								
								//WDIT_Company_Request_User_photo
								//*******************************用户请求照片****************************
								JSONObject detailuser2 = new  JSONObject();
								detailuser2.put("parentId", "requestUser_id");
								detailuser2.put("formId", 65);
								String condition2 =" requestUser_id ="+Integer.parseInt(request_map3.get("id").toString());
								String requestListStr2 = getDataById("WDIT_Company_Request_User_photo",null,condition2);
								JSONObject requestTable2= new JSONObject(requestListStr2);
								JSONArray results51= requestTable2.getJSONArray("results");
								JSONArray requestUserPhotoArray= new JSONArray();
								//通过用户ID查询的多条user photo数据
								for (int  b= 0; b < results51.length(); b++) {
									JSONObject companyrequestuserphoto = new JSONObject();
									JSONObject requestJsonphoto=results51.getJSONObject(b);
									Map<String, Object> request_map5 = new HashMap<String, Object>();
									Iterator it4 = requestJsonphoto.keys();
									while(it4.hasNext()){
										String key=(String)it4.next();
										if(requestJsonphoto.has(key))
											request_map5.put(key, requestJsonphoto.get(key));
										else
											request_map5.put(key, "");
									}
									if(StringUtils.isNotBlank(request_map5.get("id").toString()))
										companyrequestuserphoto.put("id", Integer.parseInt(request_map5.get("id").toString()));
									else
										companyrequestuserphoto.put("id","");
									if(StringUtils.isNotBlank(request_map5.get("requestUser_id").toString()))
										companyrequestuserphoto.put("requestUser_id", Integer.parseInt(request_map5.get("requestUser_id").toString()));
									else
										companyrequestuserphoto.put("requestUser_id","");
									if(StringUtils.isNotBlank(request_map5.get("request_id").toString()))
										companyrequestuserphoto.put("request_id", Integer.parseInt(request_map5.get("request_id").toString()));
									else
										companyrequestuserphoto.put("request_id","");
									if(StringUtils.isNotBlank(request_map5.get("user_relative_id").toString()))
										companyrequestuserphoto.put("user_relative_id", Integer.parseInt(request_map5.get("user_relative_id").toString()));
									else
										companyrequestuserphoto.put("user_relative_id","");
									if(StringUtils.isNotBlank(request_map5.get("user_housing_id").toString()))
										companyrequestuserphoto.put("user_housing_id", request_map5.get("user_housing_id").toString());
									else
										companyrequestuserphoto.put("user_housing_id","");
									if(StringUtils.isNotBlank(request_map5.get("type").toString()))
										companyrequestuserphoto.put("type", request_map5.get("type").toString());
									else
										companyrequestuserphoto.put("type","");
									if(StringUtils.isNotBlank(request_map5.get("smallPhoto").toString()))
										companyrequestuserphoto.put("smallPhoto", request_map5.get("smallPhoto").toString());
									else
										companyrequestuserphoto.put("smallPhoto","");
									if(StringUtils.isNotBlank(request_map5.get("name").toString()))
										companyrequestuserphoto.put("name", request_map5.get("name").toString());
									else
										companyrequestuserphoto.put("name","");
									if(StringUtils.isNotBlank(request_map5.get("photo").toString()))
										companyrequestuserphoto.put("photo", request_map5.get("photo").toString());
									else
										companyrequestuserphoto.put("photo","");
									if(StringUtils.isNotBlank(request_map5.get("createDate").toString()))
										companyrequestuserphoto.put("createDate", request_map5.get("createDate"));
									else
										companyrequestuserphoto.put("createDate","");
									if(StringUtils.isNotBlank(request_map5.get("modifyDate").toString()))
										companyrequestuserphoto.put("modifyDate", request_map5.get("modifyDate"));
									else
										companyrequestuserphoto.put("modifyDate","");
									if(StringUtils.isNotBlank(request_map5.get("createBy").toString()))
										companyrequestuserphoto.put("createBy", Integer.parseInt(request_map5.get("createBy").toString()));
									else
										companyrequestuserphoto.put("createBy","");
									if(StringUtils.isNotBlank(request_map5.get("modifyBy").toString()))
										companyrequestuserphoto.put("modifyBy", Integer.parseInt(request_map5.get("modifyBy").toString()));
									else
										companyrequestuserphoto.put("modifyBy","");
									requestUserPhotoArray.put(b,companyrequestuserphoto);
								}
								detailuser2.put("array",requestUserPhotoArray);
								//WDIT_company_request_user_relative
								JSONObject detailuser3 = new  JSONObject();
								detailuser3.put("parentId", "requestUser_id");
								detailuser3.put("formId", 63);
								String condition3 =" requestUser_id ="+Integer.parseInt(request_map3.get("id").toString());
								String requestListStr3 = getDataById("WDIT_company_request_user_relative",null,condition3);
								JSONObject requestTable3= new JSONObject(requestListStr3);
								JSONArray results6= requestTable3.getJSONArray("results");
								JSONArray requestUserRelativeArray= new JSONArray();
								//通过用户ID查询的多条relative数据
								for (int  c= 0; c < results6.length(); c++) {
									JSONObject companyrequestuserrelative = new JSONObject();
									JSONArray requestUserRelativeArray2= new JSONArray();
									JSONObject requestJson5=results6.getJSONObject(c);
									Map<String, Object> request_map6 = new HashMap<String, Object>();
									Iterator it5 = requestJson5.keys();
									while(it5.hasNext()){
										String key=(String)it5.next();
										if(requestJson5.has(key))
											request_map6.put(key, requestJson5.get(key));
										else
											request_map6.put(key, "");
									}
									if(StringUtils.isNotBlank(request_map6.get("id").toString()))
										companyrequestuserrelative.put("id", Integer.parseInt(request_map6.get("id").toString()));
									else
										companyrequestuserrelative.put("id","");
									if(StringUtils.isNotBlank(request_map6.get("requestUser_id").toString()))
										companyrequestuserrelative.put("requestUser_id", Integer.parseInt(request_map6.get("requestUser_id").toString()));
									else
										companyrequestuserrelative.put("requestUser_id","");
									if(StringUtils.isNotBlank(request_map6.get("relative").toString()))
										companyrequestuserrelative.put("relative", Integer.parseInt(request_map6.get("relative").toString()));
									else
										companyrequestuserrelative.put("relative","");
									if(StringUtils.isNotBlank(request_map6.get("identityCardNumber").toString()))
										companyrequestuserrelative.put("identityCardNumber", request_map6.get("identityCardNumber").toString());
									else
										companyrequestuserrelative.put("identityCardNumber","");
									if(StringUtils.isNotBlank(request_map6.get("name").toString()))
										companyrequestuserrelative.put("name", request_map6.get("name").toString());
									else
										companyrequestuserrelative.put("name","");
									if(StringUtils.isNotBlank(request_map6.get("createDate").toString()))
										companyrequestuserrelative.put("createDate", request_map6.get("createDate"));
									else
										companyrequestuserrelative.put("createDate","");
									if(StringUtils.isNotBlank(request_map6.get("modifyDate").toString()))
										companyrequestuserrelative.put("modifyDate", request_map6.get("modifyDate"));
									else
										companyrequestuserrelative.put("modifyDate","");
									if(StringUtils.isNotBlank(request_map6.get("createBy").toString()))
										companyrequestuserrelative.put("createBy", Integer.parseInt(request_map6.get("createBy").toString()));
									else
										companyrequestuserrelative.put("createBy","");
									if(StringUtils.isNotBlank(request_map6.get("modifyBy").toString()))
										companyrequestuserrelative.put("modifyBy", Integer.parseInt(request_map6.get("modifyBy").toString()));
									else
										companyrequestuserrelative.put("modifyBy","");
									
									//通过relative表ID查询图片
									JSONObject detailuserhousing = new  JSONObject();
									detailuserhousing.put("parentId", "user_relative_id");
									detailuserhousing.put("formId", 65);
									String condition21 =" user_relative_id ="+Integer.parseInt(request_map6.get("id").toString());
									String requestListStr21 = getDataById("WDIT_Company_Request_User_photo",null,condition21);
									JSONObject requestTable21= new JSONObject(requestListStr21);
									JSONArray results5= requestTable21.getJSONArray("results");
									JSONArray requestUserPhotoRelativeArray= new JSONArray();
									//通过用户ID查询的多条user photo数据
									for (int  d= 0; d < results5.length(); d++) {
										JSONObject companyrequestuserphoto = new JSONObject();
										JSONObject requestJsonphoto1=results5.getJSONObject(d);
										Map<String, Object> request_map5 = new HashMap<String, Object>();
										Iterator it4 = requestJsonphoto1.keys();
										while(it4.hasNext()){
											String key=(String)it4.next();
											if(requestJsonphoto1.has(key))
												request_map5.put(key, requestJsonphoto1.get(key));
											else
												request_map5.put(key, "");
										}
										if(StringUtils.isNotBlank(request_map5.get("id").toString()))
											companyrequestuserphoto.put("id", Integer.parseInt(request_map5.get("id").toString()));
										else
											companyrequestuserphoto.put("id","");
										if(StringUtils.isNotBlank(request_map5.get("requestUser_id").toString()))
											companyrequestuserphoto.put("requestUser_id", Integer.parseInt(request_map5.get("requestUser_id").toString()));
										else
											companyrequestuserphoto.put("requestUser_id","");
										if(StringUtils.isNotBlank(request_map5.get("request_id").toString()))
											companyrequestuserphoto.put("request_id", Integer.parseInt(request_map5.get("request_id").toString()));
										else
											companyrequestuserphoto.put("request_id","");
										if(StringUtils.isNotBlank(request_map5.get("user_relative_id").toString()))
											companyrequestuserphoto.put("user_relative_id", Integer.parseInt(request_map5.get("user_relative_id").toString()));
										else
											companyrequestuserphoto.put("user_relative_id","");
										if(StringUtils.isNotBlank(request_map5.get("user_housing_id").toString()))
											companyrequestuserphoto.put("user_housing_id", request_map5.get("user_housing_id").toString());
										else
											companyrequestuserphoto.put("user_housing_id","");
										if(StringUtils.isNotBlank(request_map5.get("type").toString()))
											companyrequestuserphoto.put("type", request_map5.get("type").toString());
										else
											companyrequestuserphoto.put("type","");
										if(StringUtils.isNotBlank(request_map5.get("smallPhoto").toString()))
											companyrequestuserphoto.put("smallPhoto", request_map5.get("smallPhoto").toString());
										else
											companyrequestuserphoto.put("smallPhoto","");
										if(StringUtils.isNotBlank(request_map5.get("name").toString()))
											companyrequestuserphoto.put("name", request_map5.get("name").toString());
										else
											companyrequestuserphoto.put("name","");
										if(StringUtils.isNotBlank(request_map5.get("photo").toString()))
											companyrequestuserphoto.put("photo", request_map5.get("photo").toString());
										else
											companyrequestuserphoto.put("photo","");
										if(StringUtils.isNotBlank(request_map5.get("createDate").toString()))
											companyrequestuserphoto.put("createDate", request_map5.get("createDate"));
										else
											companyrequestuserphoto.put("createDate","");
										if(StringUtils.isNotBlank(request_map5.get("modifyDate").toString()))
											companyrequestuserphoto.put("modifyDate", request_map5.get("modifyDate"));
										else
											companyrequestuserphoto.put("modifyDate","");
										if(StringUtils.isNotBlank(request_map5.get("createBy").toString()))
											companyrequestuserphoto.put("createBy", Integer.parseInt(request_map5.get("createBy").toString()));
										else
											companyrequestuserphoto.put("createBy","");
										if(StringUtils.isNotBlank(request_map5.get("modifyBy").toString()))
											companyrequestuserphoto.put("modifyBy", Integer.parseInt(request_map5.get("modifyBy").toString()));
										else
											companyrequestuserphoto.put("modifyBy","");
										requestUserPhotoRelativeArray.put(d,companyrequestuserphoto);
									}
									detailuserhousing.put("array", requestUserPhotoRelativeArray);
									requestUserRelativeArray2.put(0,detailuserhousing);
									companyrequestuserrelative.put("detail", requestUserRelativeArray2);
									requestUserRelativeArray.put(c,companyrequestuserrelative);
								}
								detailuser3.put("array", requestUserRelativeArray);
								requestUserArray2.put(0,detailuser1);
								requestUserArray2.put(1,detailuser2);
								requestUserArray2.put(2,detailuser3);
								companyrequestuser.put("detail", requestUserArray2);
							}
							requestUserArray.put(a,companyrequestuser);
					}
					detail2.put("array", requestUserArray);
					resultsarray.put(1,detail2);
					request.put("detail", resultsarray);
					results1.put(i,request);
				}
			}
			//request_Json.put("detail", detailArray);
			request_Json.put("register", results1);
			request_Json.put("code", 1);
			System.out.println(request_Json);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 用户管理
	 * @return
	 */
	@RequestMapping("/userlist")
	public String userList(){
		
		return "/wdit/userManage/userlist";
	}

	@Autowired
	private IFormService formService;
	public String getDataById(String tableName,Long dataId,String condition){ 
			Map<String,String> map =new HashMap<String, String>();
			if(StringUtils.isNotBlank(tableName))
				map.put("formCode", tableName.trim());
			else {
				JSONObject json=new JSONObject();
				json.put("code", 0);
				json.put("errorMsg", "formCode 不存在");
				return json.toString();
			}
			if(dataId!=null)
				map.put("dataId", dataId.toString().trim());
			if(condition==null)
				condition=" 1=1 ";
			
			PageTrace pageTrace = null;
			 
			JSONObject json= formService.getDataByFormId(map, condition, pageTrace,null,1);
			 return json.toString();
	}
 	
	public Map<String, Object> saveFormDataJson( @RequestParam(value="addressId",required = false)String addressId,
			                                     @RequestParam(value = "vOPCode", required = false) String vOPCode,
			                                     @RequestParam(value="dOPCode",required=false)String dOPCode,
			                                     @RequestParam(value="tOPCode",required=false)String tOPCode,
			                                     @RequestParam(value = "vCreateBy", required = false) String vCreateBy,
			@RequestParam(value = "json", required = true) String json, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			Long id= formService.saveFormDataJson(json);
			
			if(id==null||id.longValue()==0l){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
				return map;
			}
			
			
			map.put("id", id);
			map.put("code", "1");
			map.put("successMsg",  "提示：\n    保存成功!");
		}catch(Exception e){
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
		}
		return map;
	}
	/**
	 * 权限管理
	 * @return
	 */
	@RequestMapping("/authorityList")
	public String authorityManage(){
		return "/wdit/authorityManage/authorityList";
	}
	
	/**
	 * 修改用户信息
	 * @param id
	 * @param request
	 * @return
	 */
	
	@RequestMapping("/goUpdateUser")
	public String goUpdate(int id,HttpServletRequest request) {
		// TODO Auto-generated method stub
		request.setAttribute("dataId", id);
		return "wdit/userManage/updateUser";
	}
	
	
	//添加用户
		@RequestMapping(value = "/saveFormDataJson", method = RequestMethod.POST)
		@ResponseBody   	
		public Map<String, Object> saveFormDataJson(@RequestParam(value = "json", required = true) String json, HttpServletRequest request){
			Map<String, Object> map = new HashMap<String, Object>();
			try{
				JSONObject userJson = new JSONObject(json);
				JSONObject tempJsonObj = userJson.getJSONObject("register");
				String password= passwordEncoder.encode("Gzf@2017_XuHui");
				tempJsonObj.put("password",password);
				json=userJson.toString();
				Long id= formService.saveFormDataJson(json);			
				if(id==null||id.longValue()==0l){
					map.put("code", "0");
					map.put("errorMsg", "保存错误");
					return map;
				}
				User u = (User)userService.findByUserId(Integer.parseInt(id.toString()));
				Collection<Role> roles = new ArrayList<Role>();			
				//roles.add(this.roleService.findObjByKey(Role.class, null));
				u.getRoles().clear();
				u.getRoles().addAll(roles);
				userService.update(u);
				
				map.put("id", id);
				map.put("code", "1");
				map.put("successMsg",  "提示：\n    保存成功!");
			}catch(Exception e){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
			}
			return map;
		}
	
	
	/**
	 * 新增用户信息
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/addUser")
	public String addUser(){
		return "/wdit/userManage/addUser";
	}
	
	
	@RequestMapping(value = "/findHMCListData")
	@ResponseBody	
	public  DataTableToPage  findselectData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("applicant")String  applicant,
			@RequestParam("companytype")String  companytype,
			@RequestParam("begin")String  begin,
			@RequestParam("end")String  end,
			@RequestParam("acceptanceNumber")String  acceptanceNumber,
			@RequestParam("cardnum")String  cardnum,
			@RequestParam("printname")String  printname
			) throws IOException{

		String selectConditionSql= " where (t1.status >200 OR t1.status =102) ";
		if(StringUtils.isNotBlank(applicant)){
			selectConditionSql += " and t1.applicant like '%" + applicant +"%'";
		};
		if(StringUtils.isNotBlank(acceptanceNumber)){
			selectConditionSql += " and t1.acceptanceNumber like '%" + acceptanceNumber +"%' ";
		};
		if(StringUtils.isNotBlank(cardnum)){
			selectConditionSql +=" and exists( select 1 from  wdit_company_request_user where request_id=t1.id and  identityCardNumber like '%"+cardnum+"%') ";
		}
		if(companytype.trim().equals("1")){
			selectConditionSql += " and t1.status in(102,204,205) ";
		}else if(companytype.trim().equals("2")){
			selectConditionSql += " and t1.status not in(102,204,205) ";
		}
		if(StringUtils.isNotBlank(begin))
			selectConditionSql += " and (DATE(t1.createDate) > '" + begin+"' or DATE(t1.createDate) = '"+begin+"')";
		if(StringUtils.isNotBlank(end))
			selectConditionSql +=  " and (DATE(t1.createDate) < '"+ end + "' or DATE(t1.createDate) = '"+end+"') ";
		
		
		if(StringUtils.isNotBlank(printname)){
			if(printname.trim().equals("1"))
				selectConditionSql+= " and EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 2 and type = 1) "+
			            " and EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 2 and type = 2) ";
			else if(printname.trim().equals("0"))
				selectConditionSql+= " and ((not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 2 and type = 1) "+
	                    " and not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 2 and type = 2)) "+
	                    " or (EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 2 and type = 1) and "+
	                    "  not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 2 and type = 2))"+
	                    " or (not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 2 and type = 1) and "+
	                    "  EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 2 and type = 2)))";
		}
		
		 
		String code= request.getParameter("code");
		
		DataTableToPage dataTableToPage = new DataTableToPage();
		if(!(code!=null&&!code.trim().equals(""))){
			dataTableToPage.setCode(0);
			return dataTableToPage;
		}
		   
			SelectTableList selectTableList=new SelectTableList();
			try {
				  selectTableList=selectTableListService.findByTitle(code);
			} catch (Exception e) {
				// TODO: handle exception
			} 
			if(selectTableList==null||selectTableList.getId()==null){
				dataTableToPage.setCode(0);
				dataTableToPage.setErrorMsg("传入的code编号出错");
			}
			PageTrace pageTrace = null;
			if(request.getParameter("pageIndex")!=null){
				Integer pageIndex= Integer.parseInt(request.getParameter("pageIndex"));
				Integer pageSize= Integer.parseInt(request.getParameter("pageSize"));
				pageTrace = new PageTrace(pageSize);
				pageTrace.setPageIndex(pageIndex);
			}
			String json = selectTableListService.selectBySqlPage(selectTableList,selectConditionSql,pageTrace,0);
			try {
				JSONObject jsonObj = new JSONObject(json);
				if (jsonObj.has("code"))
					dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
				else
					dataTableToPage.setCode(0);
				dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
				if (jsonObj.has("paged"))
					dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		return dataTableToPage;
	}
	
	@RequestMapping(value = "/findXCSListData")
	@ResponseBody	
	public  DataTableToPage  findXCSListData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("applicant")String  applicant,
			@RequestParam("companytype")String  companytype,
			@RequestParam("begin")String  begin,
			@RequestParam("end")String  end,
			@RequestParam("acceptanceNumber")String  acceptanceNumber,
			@RequestParam("cardnum")String  cardnum,
			@RequestParam("printname")String  printname
			) throws IOException{
		String selectConditionSql= " where (t1.status >600 OR t1.status =502) ";
		if(StringUtils.isNotBlank(applicant)){
			selectConditionSql += " and t1.applicant like '%" + applicant +"%'";
		};
		if(StringUtils.isNotBlank(acceptanceNumber)){
			selectConditionSql += " and t1.acceptanceNumber like '%" + acceptanceNumber +"%' ";
		};
		if(StringUtils.isNotBlank(cardnum)){
			selectConditionSql+=" and exists( select 1 from  wdit_company_request_user where request_id=t1.id and  identityCardNumber like '%"+cardnum+"%'  ) ";
		};
		if(companytype.trim().equals("1")){
			selectConditionSql += " and t1.status in(502,604) ";
		}else if(companytype.trim().equals("2")){
			selectConditionSql += " and t1.status not in(502,604) ";
		}
		if(StringUtils.isNotBlank(begin))
			selectConditionSql += " and (DATE(t1.createDate) > '" + begin+"' or DATE(t1.createDate) = '"+begin+"')";
		if(StringUtils.isNotBlank(end))
			selectConditionSql +=  " and (DATE(t1.createDate) < '"+ end + "' or DATE(t1.createDate) = '"+end+"') ";
		
		if(StringUtils.isNotBlank(printname)){
			if(printname.trim().equals("1"))
				selectConditionSql+= " and EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 6 and type = 1) "+
			            " and EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 6 and type = 2) ";
			else if(printname.trim().equals("0"))
				selectConditionSql+= " and ((not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 6 and type = 1) "+
	                    " and not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 6 and type = 2)) "+
	                    " or (EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 6 and type = 1) and "+
	                    "  not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 6 and type = 2))"+
	                    " or (not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 6 and type = 1) and "+
	                    "  EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 6 and type = 2)))";
		}
		
		 
		String code= request.getParameter("code");
		
		DataTableToPage dataTableToPage = new DataTableToPage();
		if(!(code!=null&&!code.trim().equals(""))){
			dataTableToPage.setCode(0);
			return dataTableToPage;
		}
		   
			SelectTableList selectTableList=new SelectTableList();
			try {
				  selectTableList=selectTableListService.findByTitle(code);
			} catch (Exception e) {
				// TODO: handle exception
			} 
			if(selectTableList==null||selectTableList.getId()==null){
				dataTableToPage.setCode(0);
				dataTableToPage.setErrorMsg("传入的code编号出错");
			}
			PageTrace pageTrace = null;
			if(request.getParameter("pageIndex")!=null){
				Integer pageIndex= Integer.parseInt(request.getParameter("pageIndex"));
				Integer pageSize= Integer.parseInt(request.getParameter("pageSize"));
				pageTrace = new PageTrace(pageSize);
				pageTrace.setPageIndex(pageIndex);
			}
			String json = selectTableListService.selectBySqlPage(selectTableList,selectConditionSql,pageTrace,0);
			try {
				JSONObject jsonObj = new JSONObject(json);
				if (jsonObj.has("code"))
					dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
				else
					dataTableToPage.setCode(0);
				dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
				if (jsonObj.has("paged"))
					dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		return dataTableToPage;
	}
}
