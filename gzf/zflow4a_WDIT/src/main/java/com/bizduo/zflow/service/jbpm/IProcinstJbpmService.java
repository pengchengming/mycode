package com.bizduo.zflow.service.jbpm;

import java.util.List;

import org.json.JSONArray;

public interface IProcinstJbpmService {
	/**
	 * 查询实例的信息
	 * @param list 
	 * @param sql 
	 * @param pdid
	 * @return
	 */
	JSONArray getJbpmDeployment( String sql, List<String> list);

	
}
