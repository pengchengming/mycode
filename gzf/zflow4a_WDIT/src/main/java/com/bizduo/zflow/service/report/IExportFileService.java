package com.bizduo.zflow.service.report;

import org.apache.poi.ss.usermodel.Workbook;

public interface IExportFileService {

	public Workbook exportExcel(String[] param);
}
