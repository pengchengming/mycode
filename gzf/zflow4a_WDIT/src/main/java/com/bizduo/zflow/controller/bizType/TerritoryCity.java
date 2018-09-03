package com.bizduo.zflow.controller.bizType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.util.SelectToJson;

@Controller
@RequestMapping("/territoryCity")
public class TerritoryCity {
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value="/getTerritory")
	@ResponseBody
	public void getTerritory(HttpServletResponse response) throws IOException{
		Map<String,String> fieldMap=new HashMap<String, String>();
		fieldMap.put("code", "code");
		fieldMap.put("name", "name");
		String sql="select territoryCode as code,Territoryname as name from dm_SalesTerritory order by territoryCode ";
		String territoryJson= SelectToJson.executionsql(jdbcTemplate, sql, "query", fieldMap, null); 
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter().print(territoryJson);
		
	}
	
	@RequestMapping(value="/getProvinces")
	@ResponseBody
	public void getProvinces(HttpServletResponse response, @RequestParam(value = "territoryCode", required = true) String territoryCode  ) throws IOException{
		Map<String,String> fieldMap=new HashMap<String, String>();
		fieldMap.put("code", "code");
		fieldMap.put("name", "name");
		String sql="select code,name from  dm_region_code  where salesterritory_id="+territoryCode+"  and parent_region_code='CN'";
		String provincesJson= SelectToJson.executionsql(jdbcTemplate, sql, "query", fieldMap, null); 
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter().print(provincesJson);
	}
	
	@RequestMapping(value="/getCity")
	@ResponseBody
	public void getCity(HttpServletResponse response, 
			@RequestParam(value = "provincesCode", required = true) String provincesCode  ) throws IOException{
		Map<String,String> fieldMap=new HashMap<String, String>();
		fieldMap.put("code", "code");
		fieldMap.put("name", "name");
		String sql="select code,name from  dm_region_code  where   parent_region_code='"+provincesCode+"'";
		String cityJson= SelectToJson.executionsql(jdbcTemplate, sql, "query", fieldMap, null); 
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter().print(cityJson);
	}
}
