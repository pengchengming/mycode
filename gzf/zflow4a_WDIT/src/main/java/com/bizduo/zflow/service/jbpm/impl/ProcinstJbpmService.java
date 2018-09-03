package com.bizduo.zflow.service.jbpm.impl;

import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bizduo.zflow.service.jbpm.IProcinstJbpmService;
import com.bizduo.zflow.util.ExecutionSql;
@Service
public class ProcinstJbpmService implements IProcinstJbpmService{
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

}
