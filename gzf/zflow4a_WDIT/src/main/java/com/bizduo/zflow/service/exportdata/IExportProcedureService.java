package com.bizduo.zflow.service.exportdata;

import java.util.List;

import com.bizduo.zflow.domain.exportdata.ExportProcedure;

public interface IExportProcedureService {

	public List<ExportProcedure> getByExcelId(Integer excelId);
}
