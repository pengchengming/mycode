package com.bizduo.zflow.service.formView;

import java.util.Map;

import com.bizduo.zflow.domain.form.ViewPage;
import com.bizduo.zflow.service.base.IBaseService;

public interface IViewPageService extends IBaseService<ViewPage, Long>  {

	ViewPage findViewPageByCode(String code);
	//根据sql 查询
	String findViewPageBySql(String sql,Map<String,String> fieldMap) throws Exception;
 

}
