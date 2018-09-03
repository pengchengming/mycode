package com.bizduo.zflow.service.weixin;

import java.util.List;

import org.json.JSONArray;

public interface IWeixinService {

	public void saveData();
	/**
	 * 查询实例的信息
	 * @param list 
	 * @param sql 
	 * @param pdid
	 * @return
	 */
	public JSONArray getJbpmDeployment( String sql, List<String> list);
	public JSONArray QuerySignupInfoByUser(String OpenID);
	public JSONArray QuerySignupInfoByUserActivity(String OpenID,String activitycode);
	public JSONArray QueryActivity(String activitycode);
	
}
