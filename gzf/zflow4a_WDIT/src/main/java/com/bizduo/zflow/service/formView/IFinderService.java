package com.bizduo.zflow.service.formView;

import java.util.Map;

import com.bizduo.zflow.service.base.IBaseService;

public interface IFinderService extends IBaseService<Object, Integer>  {

	public String getBySql(String sql, String type, Map<String, String> fieldMap, Map<String, Integer> pageMap);
	
	public String executionSql(String sql, String type, Map<String, String> fieldMap, Map<String, Integer> pageMap);
}
