package com.bizduo.zflow.service.customform;

import java.util.List;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.form.SelectTableList;
import com.bizduo.zflow.service.base.IBaseService;

public interface ISelectTableListService extends IBaseService<SelectTableList, Long>  {
	public List<SelectTableList>  findSelectTableListByAll();
	
	SelectTableList findByTitle(String title);

	String selectBySqlPage(SelectTableList selectTableList,
			String selectConditionSql,PageTrace pageTrace,int type);

}
