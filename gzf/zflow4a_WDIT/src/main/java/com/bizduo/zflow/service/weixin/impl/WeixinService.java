package com.bizduo.zflow.service.weixin.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bizduo.zflow.service.weixin.IWeixinService;
import com.bizduo.zflow.util.ExecutionSql;
@Service
public class WeixinService implements IWeixinService {

	public void saveData() {
		// TODO Auto-generated method stub

	}

	@Autowired
	private  JdbcTemplate jdbcTemplate;
	//查询pdid
	public JSONArray getJbpmDeployment(String sql,List<String> list) {
		try {
			JSONArray jsonArray= ExecutionSql.querysqlJSONArray(jdbcTemplate, sql, list);
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public JSONArray QuerySignupInfoByUser(String openid){

		
		String sql="select name,telephone,email,company,createdate from weixinsignup where openid='"+openid+"' order by id desc limit 1";
		List<String> list=new ArrayList<String>();
		list.add("name");
		list.add("telephone");
		list.add("email");
		list.add("company");
		list.add("createdate");
		JSONArray jsonArray = ExecutionSql.querysqlJSONArray(jdbcTemplate, sql, list);
		//String zform = null;
		//String execution=null;
		return jsonArray;
	}
	
	public JSONArray QuerySignupInfoByUserActivity(String openid,String activitycode){
		String sql="select name,telephone,email,company,createdate from weixinsignup where openid='"+openid+"' and activitycode='"+activitycode+"' order by id desc limit 1";
		List<String> list=new ArrayList<String>();
		list.add("name");
		list.add("telephone");
		list.add("email");
		list.add("company");
		list.add("createdate");
		JSONArray jsonArray = ExecutionSql.querysqlJSONArray(jdbcTemplate, sql, list);
		//String zform = null;
		//String execution=null;
		return jsonArray;	
	}
	
//	select activityname from weixinactivity where activitycode='2014032001'	
	public JSONArray QueryActivity(String activitycode){
		String sql="select activityname from weixinactivity where activitycode='"+activitycode+"' order by id desc limit 1";
		List<String> list=new ArrayList<String>();
		list.add("activityname");
		JSONArray jsonArray = ExecutionSql.querysqlJSONArray(jdbcTemplate, sql, list);
		return jsonArray;	
	}
	
}
