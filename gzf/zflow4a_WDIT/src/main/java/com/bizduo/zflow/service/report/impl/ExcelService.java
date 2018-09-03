package com.bizduo.zflow.service.report.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.bizduo.zflow.service.report.IExcelService;
import com.bizduo.zflow.util.ExcelUtil;

/**
 * 2003 & 2007 Excel的生成
 */
@Service
public class ExcelService implements IExcelService{
//    private String path = this.getClass().getClassLoader().getResource("").getPath();
//    protected static ResourceBundle FORM_CONFIG = ResourceBundle.getBundle("REPORTFORMCONFIG", Locale.getDefault()); 
    
    public Workbook exportExcel(List<Map<String, List<String[]>>> reportData){
    	// 产生工作簿对象  
        XSSFWorkbook wb = new XSSFWorkbook();  
        for(Map<String, List<String[]>> map : reportData){
        	XSSFSheet sheet= wb.createSheet();
        	for(Iterator<String> it = map.keySet().iterator(); it.hasNext();){
        		String key = it.next();
        		String[] columns = key.split("\\,");
        		XSSFRow row = sheet.createRow(0);//创建一行
        		XSSFCell cell = null;
				for(int i = 0; i < columns.length; i++){
					cell = row.createCell(i);
					cell.setCellValue(columns[i]);
				}
				List<String[]> rows = map.get(key);
        		for(int i = 1; i <= rows.size(); i++){  
        			row = sheet.createRow(i);
        			String[] values = rows.get(i - 1);
        			for(int j = 0; j < values.length; j++){
        				cell = row.createCell(j);
        				cell.setCellValue(values[j]);
        			}
        		}  
        	}
        }
    	return wb;
    }
    public Workbook exportForm(List<Map<String, String>> c,List<String> array){
    	Workbook workbook = new HSSFWorkbook();
		CellStyle cs_string = ExcelUtil.createColumnStyle(workbook, false, 10, CellStyle.ALIGN_LEFT, CellStyle.VERTICAL_CENTER, null, false, null);
//		CellStyle cs_number = ExcelUtil.createColumnStyle(workbook, false, 10, CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_CENTER, null, true, null);
//		CellStyle cs_lineNo = ExcelUtil.createColumnStyle(workbook, false, 10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, null, false, null);
//		CellStyle cs_coefficient = ExcelUtil.createNumberStyle(workbook, false, 10, CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_CENTER, 5, null);
//		CellStyle cs_number_ = ExcelUtil.createColumnStyle(workbook, false, 8, CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_CENTER, null, true, HSSFColor.BLUE_GREY.index);
//		CellStyle cs_string_ = ExcelUtil.createColumnStyle(workbook, false, 8, CellStyle.ALIGN_LEFT, CellStyle.VERTICAL_CENTER, null, false, HSSFColor.BLUE_GREY.index);
//		CellStyle cs_coefficient_ = ExcelUtil.createNumberStyle(workbook, false, 8, CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_CENTER, 5, HSSFColor.BLUE_GREY.index);
		
		int index = 0, sheet_number = 0;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Sheet sheet = workbook.createSheet(sheet_number + "");
		workbook.setSheetName(sheet_number, "Sheet1");
		sheet.autoSizeColumn(1); 
		sheet.autoSizeColumn(1, true);

		//String[] array = FORM_CONFIG.getString("REPORT_FORM_1").split("\\,");
		Row row = sheet.createRow(index++);
		for(int i = 0; i < array.size(); i++){
			Cell cell = row.createCell(i);
			CellStyle cs = ExcelUtil.createColumnStyle(workbook, true, 10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, null, false, null);
			cs.setBorderBottom(CellStyle.BORDER_DOUBLE);
			row.getCell(i).setCellStyle(cs);
			cell.setCellValue(array.get(i));
		}
		for(Map<String, String> map : c){
			row = sheet.createRow(index++);
			int _cell = 0;
			
			row.createCell(_cell, Cell.CELL_TYPE_BLANK).setCellStyle(cs_string);
			//row.getCell(_cell++).setCellValue(_NO++);
			for(int j = 0; j < array.size(); j++){
				row.createCell(_cell, Cell.CELL_TYPE_BLANK).setCellStyle(cs_string);
				row.getCell(_cell++).setCellValue(null != map.get(array.get(j)) ? map.get(array.get(j)) : "");
			}
		}
		return workbook;
    }
}
