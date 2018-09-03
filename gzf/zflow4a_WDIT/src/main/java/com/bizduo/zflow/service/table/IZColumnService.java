package com.bizduo.zflow.service.table;

import java.util.List;

import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.service.base.IBaseService;

public interface IZColumnService extends IBaseService<ZColumn, Long> {

	public List<ZColumn> getZColumnList(String tableName);
	
	public List<ZColumn> getZColumnListByTableId(Long tableId, String[] colNames);

	public List<ZColumn> getZColumnByTableIdList(Long tableId);
}
