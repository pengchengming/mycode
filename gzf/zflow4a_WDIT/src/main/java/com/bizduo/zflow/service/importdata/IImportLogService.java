package com.bizduo.zflow.service.importdata;

import java.util.List;

import com.bizduo.zflow.domain.importdata.ImportLog;
import com.bizduo.zflow.service.base.IBaseService;

public interface IImportLogService extends IBaseService<ImportLog, Integer> {
	public List<ImportLog> getByParam(Integer id, String batchNo);
}
