package com.bizduo.zflow.controller.uploaddownload;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.exportdata.ExportExcel;
import com.bizduo.zflow.domain.importdata.ImportColumn;
import com.bizduo.zflow.domain.importdata.ImportLog;
import com.bizduo.zflow.domain.importdata.ImportTable;
import com.bizduo.zflow.service.base.ISpringJdbcService;
import com.bizduo.zflow.service.exportdata.IExportExcelService;
import com.bizduo.zflow.service.importdata.IImportColumnService;
import com.bizduo.zflow.service.importdata.IImportLogService;
import com.bizduo.zflow.service.importdata.IImportTableService;
import com.bizduo.zflow.service.report.IExportFileService;
import com.bizduo.zflow.util.FileUtil;
import com.bizduo.zflow.util.MssqlUtil;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping(value = "/uploaddownload")
public class UploadDownloadController extends BaseController {
	
	@Autowired
	private ISpringJdbcService springJdbcService;
	@Autowired
	private IImportLogService importLogService;
	@Autowired
	private IImportTableService importTableService;
	@Autowired
	private IImportColumnService importColumnService;
	@Autowired
	private IExportFileService exportFileService;
	@Autowired
	private IExportExcelService exportExcelService;
	
	@RequestMapping(value = "getBatchNo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getBatchNo(HttpServletResponse response, @RequestParam(value = "code", required = true) String batchNo){
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			result.put("code", 1);
			result.put("batchNo", springJdbcService.getGenerateCode(batchNo));
			result.put("message", "Success !");
		}catch(Exception e){
			result.put("code", 0);
			result.put("message", "Error, System fault !");
		}
		return result;
//		response.setContentType("text/plain; charset=utf-8");
//		try {
//			response.getWriter().print("{\"code\" : 1, \"batchNo\" : \"" + _batchNo + "\"}");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	} 
	
	/**
     * 单文件上传 需要配置xml beans 
     * @param name
     * @RequestParam 取得name字段的值
     * @param file 文件
     * @return
	 * @throws Exception 
     */  
	@RequestMapping(value = "/formUpload", method = RequestMethod.POST)
	@ResponseBody
    public void formUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, 
		HttpServletResponse response, @RequestParam(value = "name", required = true) String name,
		@RequestParam(value = "code", required = false) String code,
		@RequestParam(value = "version", required = false) String version,
		@RequestParam(value = "batchNo", required = true) String batchNo, 
		@RequestParam(value="dateMonth", required = false) Integer dateMonth) throws Exception {
		try {

			 
			String importBy = UserUtil.getUser().getUsername();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//保存当前文件
			ImportLog log = new ImportLog(batchNo, 0, 0, "开始保存上传的Excel文件!", new Date(), importBy);
			importLogService.create(log);
			
			FileUtil.saveFile(request.getSession().getServletContext().getRealPath("/WEB-INF"), file, batchNo);
			
			log = new ImportLog(batchNo, 0, 0, "上传的Excel(" + file.getOriginalFilename() + ")文件保存成功!", new Date(), importBy);
			importLogService.create(log);
			
			String[] array = name.split("\\,");
			for(int idx = 0; idx < array.length; idx++){
				ImportTable table = importTableService.getByName(array[idx]);
				if(null != table){
					List<ImportColumn> cols = importColumnService.getByTableID(table.getId(), null);
					try{
						log = new ImportLog(batchNo, 0, 0, "准备开始读取上传的Excel文件!", new Date(), importBy);
						importLogService.create(log);
//					XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
						XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
						XSSFSheet sheet = wb.getSheetAt(idx);
//					HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
//					HSSFSheet sheet = wb.getSheetAt(0);
						
						//得到总行数
						int rowNum = sheet.getLastRowNum();
						XSSFRow row = sheet.getRow(0);
						//得到总列数
						String[][] objs = new String[rowNum][cols.size() + 5];
//						if(null != code && !("").equals(code))
//							objs = new String[rowNum][cols.size() + 6];
						
//						if(null != dateMonth && !("").equals(dateMonth))
//							objs = new String[rowNum][cols.size() + 6];
						
						int ecount = 0;
						
						log = new ImportLog(batchNo, 0, 0, "开始读取数据!", new Date(), importBy);
						importLogService.create(log);
						
						log = new ImportLog(batchNo, 1, 1, "开始检验列标题!", new Date(), importBy);
						importLogService.create(log);
						
						int i = 0;
						if(null != code && !("").equals(code))
							i = 2;//	原来i = 1,先加入版本号字段;
						
						for(; i < cols.size(); i++){
							ImportColumn col = cols.get(i);
							String value = row.getCell(MssqlUtil.getExcelCol(col.getExlCode())).getStringCellValue().toString();
							if(!col.getDescription().equals(value)){
								log = new ImportLog(batchNo, 4, 0, col.getExlCode() + "列列名与预设置的列名不符，请检查后重试!", new Date(), importBy);
								importLogService.create(log);
								return ;
							}
						}
						
						log = new ImportLog(batchNo, 1, 1, "列标题检验完成!", new Date(), importBy);
						importLogService.create(log);
						
						FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
						ecount = analysis(evaluator, batchNo, code,version, dateMonth, importBy, sdf, sheet, cols, rowNum, objs, ecount);
						
						if(0 == ecount){
//		    			List<String> sc = new ArrayList<String>();
//		    			for(int i = 0; i < objs.length; i++){
//		    				String sql = MssqlUtil.getSql(tablename, cols, objs[i], batchNo);
//		    				sc.add(sql);
//		    			}
							log = new ImportLog(batchNo, 3, 0, "开始保存数据!", new Date(), importBy);
							importLogService.create(log);
							
							springJdbcService.saveDataBySql(array[idx], cols, objs, null == dateMonth ? null : dateMonth + "", batchNo);
							
							log = new ImportLog(batchNo, 3, 0, "已经成功保存了" + objs.length + "条记录!", new Date(), importBy);
							importLogService.create(log);
							
							springJdbcService.updateDataByProc(array[idx], batchNo);
							
							log = new ImportLog(batchNo, 3, 0, "Sheet" + idx + "数据文件处理成功!", new Date(), importBy);
							importLogService.create(log);
						}else{
							log = new ImportLog(batchNo, 3, 0, "当前存在" + ecount + "条数据类型错误，请检查后再上传!", new Date(), importBy);
							importLogService.create(log);
						}
					}catch(IOException e){
						log = new ImportLog(batchNo, 4, 0, "读取Excel数据文件出错，原始文件格式存在问题!", new Date(), importBy);
						try {
							importLogService.create(log);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}catch (Exception e){
						e.printStackTrace();
						log = new ImportLog(batchNo, 4, 0, "数据校验或者转换出错，请检查后再试!", new Date(), importBy);
						try {
							importLogService.create(log);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			log = new ImportLog(batchNo, 4, 0, "数据文件处理成功，Thank you for waiting !", new Date(), importBy);
			importLogService.create(log);
	    
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	/**
     * 单文件上传 需要配置xml beans, 解析多个Sheet的数据 
     * @param name
     * @RequestParam 取得name字段的值
     * @param file 文件
     * @return
	 * @throws Exception 
     */  
	@RequestMapping(value = "/formUpload2", method = RequestMethod.POST)
	@ResponseBody
    public void formUpload2(@RequestParam("file") MultipartFile file, HttpServletRequest request, 
		HttpServletResponse response, @RequestParam(value = "name", required = true) String name, 
		@RequestParam(value = "batchNo", required = true) String batchNo, 
		@RequestParam(value="dateMonth", required = false) Integer dateMonth) throws Exception { 
		String importBy = UserUtil.getUser().getUsername();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//保存当前文件
		ImportLog log = new ImportLog(batchNo, 0, 0, "开始保存上传的Excel文件!", new Date(), importBy);
		importLogService.create(log);
		
		FileUtil.saveFile(request.getSession().getServletContext().getRealPath("/WEB-INF"), file, batchNo);
		
		log = new ImportLog(batchNo, 0, 0, "上传的Excel(" + file.getOriginalFilename() + ")文件保存成功!", new Date(), importBy);
		importLogService.create(log);
		
		ImportTable table = importTableService.getByName(name);
		if(null != table){
	        try{
	        	log = new ImportLog(batchNo, 0, 0, "准备开始读取上传的Excel文件!", new Date(), importBy);
				importLogService.create(log);

				XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
				for(int idx = 0; idx < wb.getNumberOfSheets(); idx++){
					XSSFSheet sheet = wb.getSheetAt(idx);
					//定义列的时候，将上传表的ID加上1000，以区分和单Sheet上传的区别
					List<ImportColumn> cols = importColumnService.getByTableID(table.getId(), idx);
					//sheet.getSheetName();
					//得到总行数
					int rowNum = sheet.getLastRowNum();
					XSSFRow row = sheet.getRow(0);
					//得到总列数，添加一列存放Sheet的index值
//					String[][] objs = new String[rowNum][cols.size() + 5];
//					if(null != dateMonth && !("").equals(dateMonth))
					String[][] objs = new String[rowNum][cols.size() + 6];
					
					int ecount = 0;
					
					log = new ImportLog(batchNo, 0, 0, "开始读取数据!", new Date(), importBy);
					importLogService.create(log);
					
					log = new ImportLog(batchNo, 1, 1, "开始检验列标题!", new Date(), importBy);
					importLogService.create(log);
					
					for(int i = 0; i < cols.size(); i++){
						ImportColumn col = cols.get(i);
						String value = row.getCell(MssqlUtil.getExcelCol(col.getExlCode())).getStringCellValue().toString();
						if(!col.getDescription().equals(value)){
							log = new ImportLog(batchNo, 4, 0, col.getExlCode() + "列列名与预设置的列名不符，请检查后重试!", new Date(), importBy);
							importLogService.create(log);
							return ;
						}
					}
					
					log = new ImportLog(batchNo, 1, 1, "列标题检验完成!", new Date(), importBy);
					importLogService.create(log);
					
					FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
					// 正文内容应该从第二行开始,第一行为表头的标题
					ecount = analysis(evaluator, batchNo, null,null, idx, importBy, sdf, sheet, cols, rowNum, objs, ecount);
					
					if(0 == ecount){
						log = new ImportLog(batchNo, 3, 0, "开始保存数据!", new Date(), importBy);
						importLogService.create(log);
						
						springJdbcService.saveDataBySql(name, cols, objs, idx + "", batchNo);
						
						log = new ImportLog(batchNo, 3, 0, "已经成功保存了" + objs.length + "条记录!", new Date(), importBy);
						importLogService.create(log);
						
						springJdbcService.updateDataByProc(name, batchNo);
						
						log = new ImportLog(batchNo, 3, 0, "Sheet" + idx + "数据文件处理成功!", new Date(), importBy);
						importLogService.create(log);
					}else{
						log = new ImportLog(batchNo, 4, 0, "当前存在" + ecount + "条数据类型错误，请检查后再上传!", new Date(), importBy);
						importLogService.create(log);
					}
				}
				log = new ImportLog(batchNo, 4, 0, "数据文件处理成功，Thank you for waiting !", new Date(), importBy);
				importLogService.create(log);
			}catch(IOException e){
				log = new ImportLog(batchNo, 4, 0, "读取Excel数据文件出错，原始文件格式存在问题!", new Date(), importBy);
				try {
					importLogService.create(log);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}catch (Exception e){
		        e.printStackTrace();
		        log = new ImportLog(batchNo, 4, 0, "数据校验或者转换出错，请检查后再试!", new Date(), importBy);
				try {
					importLogService.create(log);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
	        }
		}
    }
	
	///
	/**
     * 单文件上传 需要配置xml beans 
     * @param name
     * @RequestParam 取得name字段的值
     * @param file 文件
     * @return
	 * @throws Exception 
     */  
	@RequestMapping(value = "/formUploadSheetName", method = RequestMethod.POST)
	@ResponseBody
    public void formUploadSheetName(@RequestParam("file") MultipartFile file, HttpServletRequest request, 
		HttpServletResponse response, @RequestParam(value = "name", required = true) String sheetName,
		@RequestParam(value = "code", required = false) String code,
		@RequestParam(value = "batchNo", required = true) String batchNo, 
		@RequestParam(value="dateMonth", required = false) Integer dateMonth) throws Exception { 
		String importBy = UserUtil.getUser().getUsername();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//保存当前文件
		ImportLog log = new ImportLog(batchNo, 0, 0, "开始保存上传的Excel文件!", new Date(), importBy);
		importLogService.create(log);
		
		FileUtil.saveFile(request.getSession().getServletContext().getRealPath("/WEB-INF"), file, batchNo);
		log = new ImportLog(batchNo, 0, 0, "上传的Excel(" + file.getOriginalFilename() + ")文件保存成功!", new Date(), importBy);
		importLogService.create(log);
		XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
		boolean isExist=false;
		for(int idx = 0; idx < wb.getNumberOfSheets(); idx++){
			XSSFSheet sheet = wb.getSheetAt(idx);
			String tempSheetName= sheet.getSheetName();
			if(!tempSheetName.equals(sheetName)){	
				continue;
			} 
			isExist=true;
			ImportTable table = importTableService.getBySheetName(tempSheetName);
			if(null != table){
				List<ImportColumn> cols = importColumnService.getByTableID(table.getId(), null);
				try{
					log = new ImportLog(batchNo, 0, 0, "准备开始读取上传的Excel文件!", new Date(), importBy);
					importLogService.create(log);
					//得到总行数
					int rowNum = sheet.getLastRowNum();
					XSSFRow row = sheet.getRow(0);
					//得到总列数
					String[][] objs = new String[rowNum][cols.size() + 5];
					int ecount = 0;
					log = new ImportLog(batchNo, 0, 0, "开始读取数据!", new Date(), importBy);
					importLogService.create(log);
					log = new ImportLog(batchNo, 1, 1, "开始检验列标题!", new Date(), importBy);
					importLogService.create(log);
					int i = 0;
					if(null != code && !("").equals(code))
						i = 1;
					for(; i < cols.size(); i++){
						ImportColumn col = cols.get(i);
						String value = row.getCell(MssqlUtil.getExcelCol(col.getExlCode())).getStringCellValue().toString();
						if(!col.getDescription().equals(value)){
							log = new ImportLog(batchNo, 4, 0, col.getExlCode() + "列列名与预设置的列名不符，请检查后重试!", new Date(), importBy);
							importLogService.create(log);
							return ;
						}
					}
					log = new ImportLog(batchNo, 1, 1, "列标题检验完成!", new Date(), importBy);
					importLogService.create(log);
					FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
					ecount = analysis(evaluator, batchNo, code,null, dateMonth, importBy, sdf, sheet, cols, rowNum, objs, ecount);
					if(0 == ecount){ 
						log = new ImportLog(batchNo, 3, 0, "开始保存数据!", new Date(), importBy);
						importLogService.create(log);
						springJdbcService.saveDataBySql(table.getName(), cols, objs, null == dateMonth ? null : dateMonth + "", batchNo);
						
						log = new ImportLog(batchNo, 3, 0, "已经成功保存了" + objs.length + "条记录!", new Date(), importBy);
						importLogService.create(log);
						springJdbcService.updateDataByProc(table.getName(), batchNo);
						log = new ImportLog(batchNo, 3, 0, "Sheet" + idx + "数据文件处理成功!", new Date(), importBy);
						importLogService.create(log);
					}else{
						log = new ImportLog(batchNo, 3, 0, "当前存在" + ecount + "条数据类型错误，请检查后再上传!", new Date(), importBy);
						importLogService.create(log);
					}
				}catch(IOException e){
					log = new ImportLog(batchNo, 4, 0, "读取Excel数据文件出错，原始文件格式存在问题!", new Date(), importBy);
					try {
						importLogService.create(log);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}catch (Exception e){
					e.printStackTrace();
					log = new ImportLog(batchNo, 4, 0, "数据校验或者转换出错，请检查后再试!", new Date(), importBy);
					try {
						importLogService.create(log);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		if(!isExist){
			log = new ImportLog(batchNo, 4, 0, sheetName+"，不存在 ，请检查后再试!", new Date(), importBy);
		}
		log = new ImportLog(batchNo, 4, 0, "数据文件处理成功，Thank you for waiting !", new Date(), importBy);
		importLogService.create(log);
    } 
	
	
	//解析数据
	private int analysis(FormulaEvaluator evaluator, String batchNo, String code,String version, Integer dateMonth, String importBy, SimpleDateFormat sdf, XSSFSheet sheet,
			List<ImportColumn> cols, int rowNum, String[][] objs, int ecount) throws Exception {
		ImportLog log;
		XSSFRow row;
		int step;
		for(int i = 1; i <= rowNum; i++){
			row = sheet.getRow(i);
			if(5 >= rowNum)
				step = 1;
			else if(6 <= rowNum && 20 >= rowNum)
				step = 2;
			else
				step = rowNum/10;
			
			if(0 == i%step){
				log = new ImportLog(batchNo, 3, (i + 1), "已经成功完成了" + i + "条数据的格式校验;", new Date(), importBy);
				importLogService.create(log);
			}
			String[] array = new String[cols.size() + 5];
			if(null != dateMonth && !("").equals(dateMonth))
				array = new String[cols.size() + 6];
			int j = 1;
			if(null != code && !("").equals(code))
				j = 2;
			if(null != version && !("").equals(version))
				j = 3;
			for(; j <= cols.size(); j++){
				ImportColumn col = cols.get(j - 1);
				String value ="";
				try {
					value = getStringCellValue(evaluator, col, row.getCell(MssqlUtil.getExcelCol(col.getExlCode())), batchNo, importBy).trim();
				} catch (Exception e) {
					log = new ImportLog(batchNo, 4, 0, "读取Excel数据文件出错，第 "+(i+1)+" 行，第‘"+col.getDescription() +"’列 ， 数据无法读取!", new Date(), importBy);
					importLogService.create(log);
					throw e;
				}
				
				if(!col.getExlIsNull()){
					if(null == value || ("").equals(value)){//记录日志，结束	
						log = new ImportLog(batchNo, 2, i + 1, "第" + (i + 1) + "行，" + col.getDescription() + "列数据不能为空!", new Date(), importBy);
						importLogService.create(log);
						ecount++;
					}else{
						boolean state = MssqlUtil.checkFieldType(col.getExlType(), value);
						if(!state){
							log = new ImportLog(batchNo, 2,  i + 1, "第" + (i + 1) + "行，" + col.getDescription() + "列数据类型错误!", new Date(), importBy);
							importLogService.create(log);
							ecount++;
						}else{
							if(null != col.getExlLength() && !("").equals(col.getExlLength()) && 0 < col.getExlLength()){
								if(value.length() > col.getExlLength()){
									log = new ImportLog(batchNo, 2, i + 1, "第" + (i + 1) + "行，" + col.getDescription() + "列数据超出规定长度：" + col.getExlLength() + " 。", new Date(), importBy);
									importLogService.create(log);
									ecount++;
								}else{
									array[j - 1] = value;
								}
							}else{
								array[j - 1] = value;
							}
						}
					}
				}else{
					if(null != value && !("").equals(value)){
						boolean state = MssqlUtil.checkFieldType(col.getExlType(), value);
						if(!state){
							log = new ImportLog(batchNo, 2, i + 1, "第" + (i + 1) + "行，" + col.getDescription() + "列数据类型错误!", new Date(), importBy);
							importLogService.create(log);
							ecount++;
						}else{
							if(null != col.getExlLength() && !("").equals(col.getExlLength()) && 0 < col.getExlLength()){
								if(value.length() > col.getExlLength()){
									log = new ImportLog(batchNo, 2, i + 1, "第" + (i + 1) + "行，" + col.getDescription() + "列数据超出规定长度：" + col.getExlLength() + " 。", new Date(), importBy);
									importLogService.create(log);
									ecount++;
								}else{
									array[j - 1] = value;
								}
							}else{
								array[j - 1] = value;
							}
						}
					}
				}	
			}
			if(null != dateMonth && !("").equals(dateMonth)){
				array[cols.size()] = dateMonth + "";
				array[cols.size() + 1] = null;
				array[cols.size() + 2] = importBy;
				array[cols.size() + 3] = sdf.format(new Date());
				array[cols.size() + 4] = "" + (i + 1);
				array[cols.size() + 5] = batchNo;
			}else{
				if(null != code && !("").equals(code))
					array[0] = code;
				if(null != version && !("").equals(version))
					array[1] = version;
				
				array[cols.size()] = "0";
				array[cols.size() + 1] = importBy;
				array[cols.size() + 2] = sdf.format(new Date());
				array[cols.size() + 3] = "" + (i + 1);
				array[cols.size() + 4] = batchNo;	
			}
			objs[i - 1] = array;
		}
		return ecount;
	}
	
	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cellExcel单元格
	 * @return String 单元格数据内容
	 * @throws Exception 
	 */
	private String getStringCellValue(FormulaEvaluator evaluator, ImportColumn col, Cell cell, String batchNo, String importBy) throws Exception {
		if(null == cell) return "";
		
		String strCell = null;
		switch(cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				int[] dtype = {48, 52, 56, 60, 62, 104, 106, 108, 122, 127};
		        Arrays.sort(dtype); //首先对数组排序
		        int result = Arrays.binarySearch(dtype, col.getExlType()); //在数组中搜索是否含有5
		        if(0 < result){
		        	if(0 == result || 1 == result || 2 == result || 5 == result){
		        		strCell = "" + (int)cell.getNumericCellValue();
		        	}else if(3 == result || 4 == result || 6 == result || 7 == result || 8 == result){
		        		DecimalFormat df = new DecimalFormat("#.###");
		        		strCell = "" + df.format(cell.getNumericCellValue());
		        	}else if(9 == result){
		        		strCell = "" + (long)cell.getNumericCellValue();
		        	}
		        	break;
		        }else{
		        	SimpleDateFormat sdf = null;  
		        	if(40 == col.getExlType()){  //日期  
		        		sdf = new SimpleDateFormat("yyyy-MM-dd");
		        		strCell = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));  
		        	}else if(41 == col.getExlType()){  //时间  
		        		sdf = new SimpleDateFormat("HH:mm");
		        		strCell = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
		        	}else if(61 == col.getExlType() || 189 == col.getExlType()){
		        		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        		strCell = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
		        	}else{  
		        		DecimalFormat df = new DecimalFormat("#");
		        		strCell = "" + df.format(cell.getNumericCellValue());
		        	}
		        	break;
		        }
			case HSSFCell.CELL_TYPE_STRING:
				if(null == cell 
					|| ("NULL").equals(cell.getStringCellValue()) 
					|| ("null").equals(cell.getStringCellValue()) 
					|| ("NA").equals(cell.getStringCellValue()) 
					|| ("N/A").equals(cell.getStringCellValue())){
					return "";
				}
				if(40 == col.getExlType() || 61 == col.getExlType()){
					SimpleDateFormat sdf = null; 
					try{
						if(40 == col.getExlType()){
							sdf = new SimpleDateFormat("yyyy-MM-dd");
			        		strCell = sdf.format(sdf.parse(cell.getStringCellValue())); 
						}else if(61 == col.getExlType()){
							sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        		strCell = sdf.format(sdf.parse(cell.getStringCellValue()));
						}
					}catch(Exception e){
						ImportLog log = new ImportLog(batchNo, 2, cell.getRowIndex() + 1, "第" + (cell.getRowIndex() + 1) + "行，" + col.getDescription() + "列数据转换出错。", new Date(), importBy);
						importLogService.create(log);
						throw new Exception(e);
					}
				}else{
					strCell = cell.getStringCellValue();
				}
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				strCell = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
//				strCell = "" + evaluator.evaluateFormulaCell(cell);
				strCell = String.valueOf(cell.getStringCellValue());
				break;
			case HSSFCell.CELL_TYPE_BLANK:
			default:
				strCell = "";
				break;
		}
		return strCell;
	}
	
	 @RequestMapping("exportExcel")  
	 public void exportExcel(HttpServletRequest request, HttpServletResponse response, 
//			 @RequestParam(value = "name", required = true) String tableName, 
			 @RequestParam(value = "params", required = false) String[] params){  
//	     HttpSession session = request.getSession();  
//	     session.setAttribute("state", null);  
	     // 生成提示信息，  
	     response.setContentType("application/vnd.ms-excel");  
	     ExportExcel expexl = exportExcelService.getByName(params[0]);
	     String codedFileName = expexl.getExcelName() + ".xlsx";  
	     OutputStream out = null;  
	     try{  
	         // 进行转码，使其支持中文文件名  
	    	 //	codedFileName = java.net.URLEncoder.encode("中文", "UTF-8");  
	         final String userAgent = request.getHeader("USER-AGENT");
	             String finalFileName = null;
	             if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器
	                 finalFileName = URLEncoder.encode(codedFileName,"UTF8");
	             }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
	                 finalFileName = new String(codedFileName.getBytes(), "ISO8859-1");
	             }else{
	                 finalFileName = URLEncoder.encode(codedFileName,"UTF8");//其他浏览器
	             }
	         response.setHeader("content-disposition", "attachment;filename=\"" + finalFileName + "\"");  
	         // response.addHeader("Content-Disposition", "attachment;   filename=" + codedFileName + ".xls"); 
	         Workbook wb = exportFileService.exportExcel(params); 
	         out = response.getOutputStream();  
	         wb.write(out);  
	     }catch(Exception e){
	    	 e.printStackTrace();
	     }finally{  
	    	 try{  
	             out.flush();  
	             out.close();  
	         }catch(IOException e){
	        	 e.printStackTrace();
	         }  
//	         session.setAttribute("state", "open");  
	     }  
	 }
	  
}