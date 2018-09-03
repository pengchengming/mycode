package com.bizduo.zflow.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.form.ExcelList;
import com.bizduo.zflow.domain.form.ExcelTableList;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.customform.IExcelListService;
import com.bizduo.zflow.service.customform.IExcelTableListService;
import com.bizduo.zflow.service.report.IExcelService;
import com.bizduo.zflow.util.ccm.UploadFileStatus;
import com.bizduo.zflow.util.deco.InitZflowUtil;
import com.bizduo.zflow.view.ViewExcel;

@Controller
@RequestMapping(value = "/excel")
public class ExcelController {
	@Autowired
	private IBizTypeService  bizTypeService;
	@Autowired
	private IExcelTableListService  excelTableListService;
	@Autowired
	private IExcelListService  excelListService;
	@Autowired
	private IExcelService  excelService;
	
	
	private ExcelTableList getExcelTableList(ExcelTableList excelTableList) throws Exception{
		ExcelTableList newExcelTableList= new ExcelTableList();
		if(excelTableList!=null){
			newExcelTableList.setId(excelTableList.getId());
			newExcelTableList.setCode(excelTableList.getCode());
			newExcelTableList.setDescription(excelTableList.getDescription());
			newExcelTableList.setExcelTableSql(excelTableList.getExcelTableSql());
			newExcelTableList.setOrderBy(excelTableList.getOrderBy());  
			//查询列
			List<ExcelList> excelList= excelListService.findByTableId(excelTableList.getId());
			if(excelList!=null){
				List<ExcelList> newExcelList=new ArrayList<ExcelList>();
				for (ExcelList excelListtemp : excelList) {
					ExcelList newExcel=new ExcelList(excelListtemp.getId(), excelListtemp.getColumnName(), excelListtemp.getAliasesName(), excelListtemp.getDescription(), excelListtemp.getIsDisplay(), excelListtemp.getIdx(),excelListtemp.getExportType());
					newExcelList.add(newExcel);
				}
				newExcelTableList.setExcelList(newExcelList);
			} 
		}
		return newExcelTableList;
	}
	
	 /**  
	* @Title: exportExcel  
	* @Description: 导出用户数据生成的excel文件 
	* @param  model 
	* @param  request 
	* @param  response 
	* @param  设定文件  
	* @return ModelAndView    返回类型  
	* @throws  
	*/  
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportExcel", method = RequestMethod.POST)    
	public ModelAndView exportExcel(ModelMap model,HttpServletRequest request, HttpServletResponse response) {
		String code= request.getParameter("code");
		String selectConditionSql=request.getParameter("selectConditionSql");
		
		ExcelTableList newExcelTableList=new ExcelTableList();
		try {
			if(code!=null&&!code.trim().equals("")){
				List<BizValue> list = bizTypeService.getBizValuesByCode(UploadFileStatus.CACHING);
				boolean ischeck=false;//true 从缓存中取值
				if (list != null && list.size() > 0) {
					String value = list.get(0).getDisplayValue();
					if(StringUtils.isNotBlank(value)&& value.equals("1") ){
						ischeck=true;
					}
				} 
				if(ischeck){
					Map<String,ExcelTableList> staticExcelTableListMap=InitZflowUtil.staticExcelTableListMap;
					newExcelTableList=staticExcelTableListMap.get(code);	
				}else{
					ExcelTableList excelTableList=excelTableListService.findByCode(code);
					newExcelTableList=getExcelTableList(excelTableList);
				} 
			}
			/*
			ExcelTableList excelTableList=excelTableListService.findByCode(code);
			newExcelTableList=getExcelTableList(excelTableList);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	      ViewExcel viewExcel = new ViewExcel(); 
			Map<String,Object> resultsMap = excelTableListService.selectBySqlPage(newExcelTableList,selectConditionSql);
			String results="";
			Map<String,String> fieldValMap=new HashMap<String, String>();
			List<String> fieldList=new ArrayList<String>();
			if(resultsMap.get("fieldValMap")!=null)
				fieldValMap=(Map<String, String>) resultsMap.get("fieldValMap");
			if(resultsMap.get("fieldList")!=null)
				fieldList=  (List<String>) resultsMap.get("fieldList");
			if(resultsMap.get("results")!=null)
				results=resultsMap.get("results").toString();
			
			List<Map<String, String>> c = new ArrayList<Map<String, String>>();
			if(results!=null&&!results.trim().equals("")){
				try {
					//DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
					JSONArray objArry=new JSONArray(results);
					for(int i=0;i<objArry.length();i++){
						Map<String,String> map = new HashMap<String, String>();
						JSONObject objJson=objArry.getJSONObject(i);
						for (Map.Entry<String, String> entry : fieldValMap.entrySet()) {
			        		String valueData= objJson.getString(entry.getKey()); 
			        		map.put(entry.getKey(), valueData);
			        	}
						c.add(map);
					} 
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			Workbook workbook = excelService.exportForm(c,fieldList);  
	      try {  
	       viewExcel.buildExcelDocument(null, (HSSFWorkbook)workbook, request, response);  
	      } catch (Exception e) {  
	    // TODO Auto-generated catch block  
	    e.printStackTrace();  
	      }  
	      return new ModelAndView(viewExcel, model);     
	  } 
}
