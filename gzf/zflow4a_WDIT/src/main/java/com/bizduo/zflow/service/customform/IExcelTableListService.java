package com.bizduo.zflow.service.customform;

import java.util.List;
import java.util.Map;

import com.bizduo.zflow.domain.form.ExcelTableList;
import com.bizduo.zflow.service.base.IBaseService;

public interface IExcelTableListService extends IBaseService<ExcelTableList, Long>{
	
	public List<ExcelTableList>  findExcelTableListByAll();
	
	public ExcelTableList findByCode(String code);
	
	public  Map<String,Object> selectBySqlPage(ExcelTableList excelTableList,String selectConditionSql);
}
