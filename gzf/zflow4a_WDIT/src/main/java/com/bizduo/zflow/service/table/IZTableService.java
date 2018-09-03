package com.bizduo.zflow.service.table;

import java.util.List;

import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.service.base.IBaseService;

public interface IZTableService extends IBaseService<Object, Long> {
	/**
	 * 根据tablename查询
	 * @param tableName
	 * @return
	 */
	public ZTable getZTableByName(String tableName);
	
	List<ZTable> getTableAll();
	}
