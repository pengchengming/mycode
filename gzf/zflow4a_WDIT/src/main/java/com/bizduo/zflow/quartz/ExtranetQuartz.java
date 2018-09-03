package com.bizduo.zflow.quartz;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.wdit.IRequestService;
import com.bizduo.zflow.util.TimeUitl;
import com.bizduo.zflow.util.wdit.GenerateJsonQuartzUtil;

/**
 *  外网生成数据
 * @author Root
 *
 */
public class ExtranetQuartz { 
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private IRequestService requestService;
	@Autowired
	private IFormService formService;
	
	
	
	/**
	 * 外网生成json放到外网info中  申请数据；预审数据；预审审批记录 ；资质审批，现场审核
	 * @throws java.text.ParseException 
	 * @throws Exception 
	 */
	public void generateJson() {
		try {
			String nowDateStr="";
			//从info表查询上一次生成时间
			SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet("select exportDate from wdit_info where type in (1,11) order by exportDate desc limit 0,1");
			while (rowSet.next()) {
				if (rowSet.getDate("exportDate")!=null) {
					nowDateStr=TimeUitl.getTimeLongTime(rowSet.getDate("exportDate").getTime(),"yyyy-MM-dd HH:mm:ss");
				}
			}
			//根据wdit_info_reqeust中的type和查询对象表中的时间做判断
			String condition=" id in (select request_id from  wdit_info_request where type in (1,2)";
			if (nowDateStr!=null&&!nowDateStr.equals("")) {
				condition+=" and modifyDate >'"+nowDateStr+"'";
			}
			condition+=" )";
			String subcondition=" request_id in (select request_id from  wdit_info_request where type in (1,2)";
			if (nowDateStr!=null&&!nowDateStr.equals("")) {
				subcondition+=" and modifyDate >'"+nowDateStr+"'";
			}
			subcondition+=" )";
			//申请，预审，预审审批记录
			JSONArray requestArray = GenerateJsonQuartzUtil.getrequestJson(condition,subcondition, requestService);
			//现场审批
			String condition1=" status in (103,105,202,203,204,602,603,604)";
			if (nowDateStr!=null&&!nowDateStr.equals("")) {
				condition1+=" and modifyDate>'"+nowDateStr+"'";
			}
			condition1+=" and acceptanceNumber is not null";
			//人员审批记录
			/*String subcondition1=" request_id in (select id from  wdit_company_request  where  status in (202,203,204,602,603,604) and acceptanceNumber is not null ) ";
			if (nowDateStr!=null&&!nowDateStr.equals("")) {
				subcondition1+=" and modifyDate>'"+nowDateStr+"'";
			}*/
			String nowdate= TimeUitl.getTimeLongTime(new Date().getTime(),"yyyy-MM-dd HH:mm:ss");
			JSONArray approvalArray = GenerateJsonQuartzUtil.getApprovalJson(null,null,null,1,requestService, condition1,null);
			
			JSONArray array=new JSONArray();
			if(requestArray.length()>0){
				for (int i = 0; i < requestArray.length(); i++) {
					array.put(requestArray.getJSONObject(i));
				}
			}
			if(approvalArray.length()>0){
				for (int i = 0; i < approvalArray.length(); i++) {
					array.put(approvalArray.getJSONObject(i));
				}
			}
			int isSuccess=0;
			if (array!=null&&array.length()>0) {
				isSuccess=1;
			}
			System.out.println(array.toString());
			if(array.length()>0){
				//用json包装,保存到外网的wdit_info
				JSONObject saveInfo = new JSONObject();
				JSONObject register = new JSONObject();
				saveInfo.put("formId", 84);
				register.put("exportDate", nowdate);
				register.put("type", 1);
				register.put("jsoninfo", array.toString());
				register.put("isSuccess", isSuccess);	
				saveInfo.put("register", register);
				this.formService.saveFormDataJson(saveInfo.toString());
				System.out.println("=======>外网申请信息,预审信息生成");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
