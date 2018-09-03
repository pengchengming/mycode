package com.bizduo.zflow.service.customform;

import java.util.List;

import com.bizduo.zflow.domain.form.ExcelList;
import com.bizduo.zflow.service.base.IBaseService;

public interface IExcelListService extends IBaseService<ExcelList, Long>{
	public  List<ExcelList> findByTableId(Long id);
}
