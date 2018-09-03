package com.bizduo.zflow.service.customform;

import java.util.List;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.form.SelectConditionComplete;
import com.bizduo.zflow.domain.form.SelectListComplete;
import com.bizduo.zflow.domain.form.SelectTableListComplete;

public interface ISelectTableListCompleteService {

	SelectTableListComplete createSelectTableListComplete( SelectTableListComplete selectTableListComplete)throws Exception;

	SelectListComplete createSelectListComplete( SelectListComplete selectListComplete)throws Exception;

	SelectConditionComplete createSelectConditionComplete( SelectConditionComplete selectConditionComplete)throws Exception;

	public SelectTableListComplete findByCode(String code);
	
	public  List<SelectListComplete> findByTableId(Long id);
	
	public List<SelectConditionComplete> findSelectConditionCompleteByTableId(Long id);

	SelectTableListComplete findbyId(Class<SelectTableListComplete> class1,Long id);
	
	public  String selectBySqlPage(SelectTableListComplete selectTableListComplete, String selectConditionSql,PageTrace pageTrace);
}
