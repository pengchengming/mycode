package com.bizduo.zflow.service.wdit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.util.FileUtil;
import com.bizduo.zflow.util.TimeUitl;
import com.sun.org.apache.bcel.internal.generic.NEW;
@Service
public class RequestService implements IRequestService {
	@Autowired
	private IFormService formService;
	
	public JSONObject getDataById(String tableName,Long dataId,String condition){ 
		Map<String,String> map =new HashMap<String, String>();
		if(StringUtils.isNotBlank(tableName))
			map.put("formCode", tableName.trim());
		else {
			JSONObject json=new JSONObject();
			json.put("code", 0);
			json.put("errorMsg", "formCode 不存在");
			return json;
		}
		if(dataId!=null)
			map.put("dataId", dataId.toString().trim());
		if(condition==null)
			condition=" 1=1 "; 
		PageTrace pageTrace = null;
		JSONObject json= formService.getDataByFormId(map, condition, pageTrace,null,0);
		 return json;
	}
	
	
	
	
	/**
	 * 获取内网的wdit_company_request信息,该函数用于内外网数据同步测试用
	 * @param jdbcUrl
	 * @param user
	 * @param password
	 * @param tableName
	 * @param condition
	 * @return 查询结果结果包装成的JSON
	 */
	
	public JSONObject  getDataByIdSyn(String jdbcUrl,String user,String password,String tableName,String condition){
		int code=1;
		Connection connection = null;
		Statement stmt  = null;
		ResultSet resultSet = null;
		JSONObject jsonContainer = new JSONObject();
		Integer formId=null;
		List<String> formPropertyList = new ArrayList<String>();
		try {
			connection = DriverManager.getConnection(jdbcUrl, user, password); 
        	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
        	stmt =connection.createStatement();
			//根据表名,获取表ID
			resultSet = stmt.executeQuery("select id from zflow_form where formName='"+tableName+"'");
			if (resultSet.next()) {
				formId=resultSet.getInt("id");
			}else{
				return null;	//表不存在,返回null
			}
			
			//根据formId,获取字段值
			resultSet=stmt.executeQuery("select fieldName from zflow_form_property where form_id ="+formId);
			while (resultSet.next()) {
				formPropertyList.add(resultSet.getString("fieldName"));	//保存表属性,仅包含字段名
			}
			formPropertyList.add("id");
			formPropertyList.add("createDate");
			formPropertyList.add("createBy");
			formPropertyList.add("modifyDate");
			formPropertyList.add("modifyBy");
			//开始查询对应表
			JSONArray results = new JSONArray();
	        
        	resultSet = stmt.executeQuery("select * from "+tableName+" where "+condition);
        	while (resultSet.next()) {
        		JSONObject requestJsonObject =this.packageRequestData(resultSet,formPropertyList);	
        		results.put(requestJsonObject);
			}
        	jsonContainer.put("results", results);
        	jsonContainer.put("code", code);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			code=0;
			jsonContainer.put("code", code);
		}finally{
			
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		}
		
		return jsonContainer;
	}
	
	private JSONObject packageRequestData(ResultSet resultSet,List<String>properties) throws JSONException, SQLException, ParseException{
		JSONObject requestJsonObject = new JSONObject();
		int size = properties.size();
		for (int i = 0; i < size ; i++) {
			String fieldName = properties.get(i);
			requestJsonObject.put(fieldName, resultSet.getString(fieldName));
		}
		requestJsonObject.put("nowDate",TimeUitl.getTimeLongTime(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));
		if (!properties.contains("id")) {
			requestJsonObject.put("id",resultSet.getString("id"));
		}
		
		return requestJsonObject;
	}
	
	
	
	
	
	
	public JSONObject getInfoBy(Long importDateLong,Integer type) throws Exception{
		String condition="";		
		condition +="  type="+type+" ";
		if(importDateLong!=null)
		   condition +=" and exportDate > '"+TimeUitl.getTimeLongTime(importDateLong, "yyyy-MM-dd HH:mm:ss") +"'  ";
		JSONObject infoJson= getDataById("WDIT_Info", null, condition);
		if(infoJson.has("code")&&infoJson.getInt("code")==1){
			JSONArray results= infoJson.getJSONArray("results");
			if(results.length()>0){

				String[] saveJson=new String[results.length()];
				for (int i = 0; i < results.length(); i++) {
					JSONObject jsonTemp= results.getJSONObject(i);
					JSONObject json=new JSONObject();
					JSONObject saveInfo = new JSONObject();
					saveInfo.put("formId", 84);
					JSONObject register = new JSONObject();
					register.put("type", 11);  
					saveInfo.put("register", register);
					saveInfo.put("tableDataId", jsonTemp.getInt("id") );
					saveJson[i]=saveInfo.toString();
				}
				formService.saveFormDataJson(saveJson);
			}
		}
		 return infoJson;
	}

	public JSONObject getInfoBySyn(String jdbcUrl,String databaseUsername,String databasePassword,String  condition) throws ParseException, SQLException{
		
		String sql="select * from WDIT_Info  " ;
		if(StringUtils.isNotBlank(condition)){
			sql +=condition;
		}else {
			return null;
		}
		Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;
    	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
    	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
    	stmt =connection.createStatement();
    	JSONObject returnObj = new JSONObject();  
        try {
    		ResultSet rs = stmt.executeQuery(sql);
    		if(rs.getRow()>1){
    			return null;
    		}
    		 while(rs.next()){
    			Long tempId= rs.getLong("id");
    			String code= rs.getString("code");
    			String type= rs.getString("type");
    			String jsoninfo= rs.getString("jsoninfo");
    			if(rs.getTimestamp("exportDate")!=null){
    				Long exportDateLong=rs.getTimestamp("exportDate").getTime();
    				returnObj.put("exportDateLong", exportDateLong);
    			}
    			if(rs.getTimestamp("importDate")!=null){
        			Long importDateLong=rs.getTimestamp("importDate").getTime();
        			returnObj.put("importDateLong", importDateLong);
    			}
    			returnObj.put("id", tempId);
    			returnObj.put("code", code); 
    			returnObj.put("type", type); 
    			returnObj.put("jsoninfo", jsoninfo); 
    			 
    	      } 
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚 
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnObj;
		
	}
	
	public JSONObject getInfoBySyn(Long importDateLong) throws ParseException{
		String condition=null;		
		if(importDateLong!=null)
		   condition=" exportDate > '"+TimeUitl.getTimeLongTime(importDateLong, "yyyy-MM-dd HH:mm:ss") +"'";
		return getDataById("WDIT_Info", null, condition);
	}
	
	/**
	 * 获取外网的照片 按日期
	 * @param nowDate
	 * @return
	 */
	public JSONObject getImageRequest(String synImagePath , Long importDateLong){
		JSONObject filePathNames=new JSONObject();
		List<String> pathNams=new  ArrayList<String>();
		FileUtil.traverseFolder(synImagePath,importDateLong,pathNams); 
		for (int i = 0; i < pathNams.size(); i++) {
			filePathNames.put(Integer.toString(i), pathNams.get(i));
		}
		return filePathNames;
	}
}
