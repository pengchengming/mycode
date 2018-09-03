package com.bizduo.zflow.service.importdata;

import java.util.List;

import com.bizduo.zflow.domain.importdata.ImportColumn;
import com.bizduo.zflow.service.base.IBaseService;

public interface IImportColumnService extends IBaseService<ImportColumn, Integer> {
	List<ImportColumn> getByTableID(Integer id, Integer sheet);
}
