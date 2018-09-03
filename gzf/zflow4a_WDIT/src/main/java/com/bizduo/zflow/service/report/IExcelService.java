package com.bizduo.zflow.service.report;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;


public interface IExcelService {
	Workbook exportExcel(List<Map<String, List<String[]>>> reportData);
	Workbook exportForm(List<Map<String, String>> c,List<String> array);
}
