package com.bizduo.zflow.service.base;

import java.util.List;

import com.bizduo.zflow.domain.importdata.ImportColumn;

public interface ISpringJdbcService {

	public String saveDataBySql(String tablename, List<ImportColumn> cols, Object[][] objs, String sheet, String batchNo);
	
	public String getGenerateCode(String name);
	public void updateDataByProc(String tablename, String batchNo);
}
