package com.bizduo.zflow.service.importdata;

import com.bizduo.zflow.domain.importdata.ImportTable;
import com.bizduo.zflow.service.base.IBaseService;

public interface IImportTableService extends IBaseService<ImportTable, Integer> {

	public ImportTable getByName(String name);

	public ImportTable getBySheetName(String tempSheetName);
	
}
