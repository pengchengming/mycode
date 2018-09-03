package com.bizduo.zflow.util;

import org.apache.poi.hssf.record.CFRuleRecord.ComparisonOperator;
import org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFontFormatting;
import org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

public class ExcelUtil {
	 /** 
     * 设置单元格 边框 风格(粗细，颜色) 
     * @param wb 这一个对象代表着对应的一个Excel文件 
     * @return CellStyle 
     */  
    public static CellStyle makeNewCellStyle(Workbook wb){  
        CellStyle cellStyle = wb.createCellStyle();  
        //设置一个单元格边框粗细(上下左右四边)  
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);  
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);  
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);  
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);  
        //设置一个单元格边框颜色(上下左右四边)  
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());  
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());  
        return cellStyle;  
    } 
    
    /** 
     * 设置文字在单元格里面的 对齐方式 
     * @param cellStyle 
     * @param halign 
     * @param valign decorate 
     * @return 
     */  
    public static CellStyle alignmentDecorate(CellStyle cellStyle, short halign, short valign){  
    	//设置上下居中  
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);  
        //设置左右居中
        //cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
        return cellStyle;  
    }
    /**
     * 
     * @param wb
     * @param bold
     * @param size
     * @param text_align 上下居中
     * @param vertical_align 左右居中
     * @return
     */
    public static CellStyle createTitleStyle(Workbook wb, boolean bold, int size, boolean text_align, boolean vertical_align){
    	CellStyle cs = wb.createCellStyle();
    	cs.setFont(createChineseFonts(wb, bold, size));
    	if(text_align)
    		cs.setAlignment(CellStyle.ALIGN_CENTER);   //设置上下居中
		if(vertical_align)
    		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER); //设置文字左右居中
    	return cs;
    }
    /**
     * 
     * @param wb
     * @param bold 是否粗体
     * @param size 字体大小
     * @param align 上下
     * @param vertical 左右
     * @param isNumber 是否需要格式化数字(3位一分,保留两位小数)
     * @return
     */
    public static CellStyle createColumnStyle(Workbook wb, boolean bold, int size, short align, short vertical, Short backgroundColor, boolean isNumber, Short fontColor){
    	CellStyle cs = wb.createCellStyle();
    	cs.setFont(createArialFonts(wb, bold, size, fontColor));
    	cs.setVerticalAlignment(vertical); //设置文字左右位置
    	cs.setAlignment(align);   //设置上下位置
    	//设置一个单元格边框粗细(上下左右四边)  
        cs.setBorderBottom(CellStyle.BORDER_THIN);  
        cs.setBorderTop(CellStyle.BORDER_THIN);  
        cs.setBorderLeft(CellStyle.BORDER_THIN);  
        cs.setBorderRight(CellStyle.BORDER_THIN);  
        //设置一个单元格边框颜色(上下左右四边)  
        cs.setRightBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //格式化数字
        if(isNumber){
	        HSSFDataFormat df = (HSSFDataFormat) wb.createDataFormat();
	        cs.setDataFormat(df.getFormat("#,##0.00"));
        }
        if(null != backgroundColor){
        	cs.setFillForegroundColor(backgroundColor);
        	cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        return cs;
    }
    public static CellStyle createCellDefault(Workbook wb){
    	CellStyle cs = wb.createCellStyle();//普通单元格
		cs.setBorderBottom(CellStyle.BORDER_THIN); //下边框
		cs.setBorderLeft(CellStyle.BORDER_THIN);//左边框
		cs.setBorderTop(CellStyle.BORDER_THIN);//上边框
		cs.setBorderRight(CellStyle.BORDER_THIN);
		return cs;
    }
    public static CellStyle createCellStyle(Workbook wb, boolean bold, int size, short align, short vertical, Short color, boolean isNumber){
    	CellStyle cs = wb.createCellStyle();
    	cs.setFont(createArialFonts(wb, bold, size, null));
    	cs.setVerticalAlignment(vertical); //设置文字左右位置
    	cs.setAlignment(align);   //设置上下位置
    	//设置一个单元格边框粗细(上下左右四边)  
        cs.setBorderBottom(CellStyle.BORDER_THIN);  
        cs.setBorderTop(CellStyle.BORDER_THIN);  
        cs.setBorderLeft(CellStyle.BORDER_THIN);  
        cs.setBorderRight(CellStyle.BORDER_THIN);  
        //设置一个单元格边框颜色(上下左右四边)  
        cs.setRightBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //格式化数字
        if(isNumber){
	        HSSFDataFormat df = (HSSFDataFormat) wb.createDataFormat();
	        cs.setDataFormat(df.getFormat("#,##0.00%"));
        }
        if(null != color){
        	cs.setFillForegroundColor(color);
        	cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        return cs;
    }
    
    //最多五位小数
    public static CellStyle createNumberStyle(Workbook wb, boolean bold, int size, short align, short vertical, int decimal, Short color){
    	CellStyle cs = wb.createCellStyle();
    	cs.setFont(createArialFonts(wb, bold, size, color));
    	cs.setVerticalAlignment(vertical); //设置文字左右位置
    	cs.setAlignment(align);   //设置上下位置
    	//设置一个单元格边框粗细(上下左右四边)  
        cs.setBorderBottom(CellStyle.BORDER_THIN);  
        cs.setBorderTop(CellStyle.BORDER_THIN);  
        cs.setBorderLeft(CellStyle.BORDER_THIN);  
        cs.setBorderRight(CellStyle.BORDER_THIN);  
        //设置一个单元格边框颜色(上下左右四边)  
        cs.setRightBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
        cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //格式化数字
        HSSFDataFormat df = (HSSFDataFormat) wb.createDataFormat();
        if(0 == decimal){
	        cs.setDataFormat(df.getFormat("#,##0"));
        }else if(1 == decimal){
        	cs.setDataFormat(df.getFormat("#,##0.0"));
        }else if(2 == decimal){
        	cs.setDataFormat(df.getFormat("#,##0.00"));
        }else if(3 == decimal){
        	cs.setDataFormat(df.getFormat("#,##0.000"));
        }else if(4 == decimal){
        	cs.setDataFormat(df.getFormat("#,##0.0000"));
        }else if(5 == decimal){
        	cs.setDataFormat(df.getFormat("#,##0.00000"));
        }
        return cs;
    }
    
    /** 
     * 设置字体 
     * @param wb 
     * @return 
     */  
    public static Font createChineseFonts(Workbook wb, boolean bold, int size){  
        //创建Font对象  
        Font font = wb.createFont();  
        //设置字体  
        font.setFontName("宋体");  
        //着色  
        font.setColor(HSSFColor.BLACK.index);  
        //斜体  
        font.setItalic(false);  
        //粗体
        if(bold)
        	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //字体大小  
        font.setFontHeightInPoints((short)size);
        return font;  
    }
    /** 
     * 设置字体 
     * @param wb 
     * @return 
     */  
    public static Font createArialFonts(Workbook wb, boolean bold, int size, Short color){  
        //创建Font对象  
        Font font = wb.createFont();  
        //设置字体  
        font.setFontName("Arial");  
        //着色  
        if(null == color)
        	font.setColor(HSSFColor.BLACK.index);
        else
        	font.setColor(color);
        //斜体  
        font.setItalic(false);  
        //粗体
        if(bold)
        	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //字体大小  
        font.setFontHeightInPoints((short)size);
        return font;  
    }
    /**
     * 列最大到AN(41)
     * @param index 列No.
     * @return
     */
    public static String getCellCodeByIndex(int index){
    	String[] array = {"A", "B", "C", "D", "E", "F", "G", 
    			"H", "I", "J", "K", "L", "M", "N", "O", "P", 
    			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", 
    			"Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", 
    			"AH", "AI", "AJ", "AK", "AL", "AM", "AN"};
    	return array[index];
    }
    
    public static void setConditionalFormatting(Sheet sheet, int start_row, int end_row, int start_col, int end_col){
		HSSFSheetConditionalFormatting scf = (HSSFSheetConditionalFormatting) sheet.getSheetConditionalFormatting();  
		//设置"条件格式"的规则，本例选择的条件类型是："单元格数据"  
		//如果当前单元格的数据等于0，则改变该单元格的字体颜色,设置为白色  
		HSSFConditionalFormattingRule rule = scf.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0", null);  
		HSSFFontFormatting font_formatting = rule.createFontFormatting();  
		font_formatting.setFontColorIndex(HSSFColor.WHITE.index);
		HSSFConditionalFormattingRule rule1 = scf.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0.00", null);  
		HSSFFontFormatting font_formatting1 = rule.createFontFormatting();  
		font_formatting1.setFontColorIndex(HSSFColor.WHITE.index);
		  
	//	//如果当前单元格的数据等于G，则显示绿色  
	//	HSSFConditionalFormattingRule cf_G_rule = scf.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"G\"", null);  
	//	HSSFPatternFormatting cf_G = cf_G_rule.createPatternFormatting();  
	//	cf_G.setFillBackgroundColor(HSSFColor.GREEN.index);  
		//规则列表  
		HSSFConditionalFormattingRule[] cfRules = {rule, rule1};  
		//条件格式应用的单元格范围  
		CellRangeAddress[] regions = {new CellRangeAddress(start_row, end_row, start_col, end_col)};  
		  
		scf.addConditionalFormatting(regions, cfRules);
    }
    public static void copyRow(Workbook workbook, Sheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        Row newRow = worksheet.getRow(destinationRowNum);
        Row sourceRow = worksheet.getRow(sourceRowNum);
        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
//        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
//            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
//            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
//                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(), 
//                    (newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())), 
//                    cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
//                worksheet.addMergedRegion(newCellRangeAddress);
//            }
//        }
    }
    
    public static void setBorderStyle(int firstRow, int lastRow, int firstCol, int lastCol, int border, int color, Sheet sheet, Workbook workbook){
    	CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		RegionUtil.setBorderBottom(border, region, sheet, workbook);  
		RegionUtil.setBorderLeft(border, region, sheet, workbook);  
		RegionUtil.setBorderRight(border, region, sheet, workbook);  
		RegionUtil.setBorderTop(border, region, sheet, workbook);  
		RegionUtil.setBottomBorderColor(color, region, sheet, workbook);
		RegionUtil.setLeftBorderColor(color, region, sheet, workbook);
		RegionUtil.setRightBorderColor(color, region, sheet, workbook);
		RegionUtil.setTopBorderColor(color, region, sheet, workbook);
    }
}
