package com.bizduo.zflow.ZflowThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bizduo.zflow.domain.bizType.DataDictionaryCode;
import com.bizduo.zflow.domain.bizType.DataDictionaryValue;
import com.bizduo.zflow.domain.form.ExcelList;
import com.bizduo.zflow.domain.form.ExcelTableList;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.form.FormView;
import com.bizduo.zflow.domain.form.FormViewProperty;
import com.bizduo.zflow.domain.form.SelectConditions;
import com.bizduo.zflow.domain.form.SelectList;
import com.bizduo.zflow.domain.form.SelectTableList;
import com.bizduo.zflow.service.bizType.IDataDictionaryService;
import com.bizduo.zflow.service.customform.IExcelListService;
import com.bizduo.zflow.service.customform.IExcelTableListService;
import com.bizduo.zflow.service.customform.IFormPropertyService;
import com.bizduo.zflow.service.customform.ISelectConditionsService;
import com.bizduo.zflow.service.customform.ISelectListService;
import com.bizduo.zflow.service.customform.ISelectTableListService;
import com.bizduo.zflow.service.formView.IFormViewPropertyService;
import com.bizduo.zflow.service.formView.IFormViewService;
import com.bizduo.zflow.util.deco.InitZflowUtil;

public class ZflowThread implements Runnable{
 
	private String initType; 
	private IFormViewService formViewService;
	private IFormViewPropertyService formViewPropertyService ;
	private IFormPropertyService formPropertyService;
	private ISelectTableListService selectTableListService;
	private ISelectListService selectListService;
	private ISelectConditionsService selectConditionsService;
	private IDataDictionaryService dataDictionaryService; 
	private  IExcelTableListService  excelTableListService;
	private  IExcelListService  excelListService; 
	
	public void setInitType(String initType) {
		this.initType = initType;
	} 
	public void setFormViewService(IFormViewService formViewService) {
		this.formViewService = formViewService;
	} 
	public void setFormViewPropertyService(
			IFormViewPropertyService formViewPropertyService) {
		this.formViewPropertyService = formViewPropertyService;
	} 
	public void setFormPropertyService(IFormPropertyService formPropertyService) {
		this.formPropertyService = formPropertyService;
	} 
	public void setSelectTableListService(
			ISelectTableListService selectTableListService) {
		this.selectTableListService = selectTableListService;
	} 
	public void setSelectListService(ISelectListService selectListService) {
		this.selectListService = selectListService;
	} 
	public void setSelectConditionsService(
			ISelectConditionsService selectConditionsService) {
		this.selectConditionsService = selectConditionsService;
	}
	public void setDataDictionaryService(
			IDataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	} 
	public IExcelTableListService getExcelTableListService() {
		return excelTableListService;
	}
	public void setExcelTableListService(
			IExcelTableListService excelTableListService) {
		this.excelTableListService = excelTableListService;
	}
	public IExcelListService getExcelListService() {
		return excelListService;
	}
	public void setExcelListService(IExcelListService excelListService) {
		this.excelListService = excelListService;
	}
	
	public void run(){
		if(initType.equals("formView"))
			staticFormViewMapFun(formViewService, formViewPropertyService, formPropertyService);	 
		else if(initType.equals("selectTable"))
			staticSelectTableMapFun(selectTableListService, selectListService, selectConditionsService, formPropertyService);
		else if(initType.equals("dictionary"))
			staticDictionaryMapFun(dataDictionaryService);
		else if(initType.equals("excelTable"))
			staticExcelTableMapFun(excelTableListService, excelListService);
		else if(initType.equals("packageItem"))
			staticPackageItemMapFun(formViewService);
	}
	  
	//formView
	private void staticFormViewMapFun(IFormViewService formViewService,IFormViewPropertyService formViewPropertyService,IFormPropertyService formPropertyService){

		Map<String,FormView> staticFormViewMap =new HashMap<String, FormView>();
		//formView
		List<FormView> formViewList=formViewService.findFormViewByAll();
		if(formViewList!=null&&formViewList.size()>0){
			for (FormView formView : formViewList) {
				try {
					List<FormViewProperty> newformViewPropertyList=new ArrayList<FormViewProperty>();		
					List<FormViewProperty> formViewPropertyList=  formViewPropertyService.getFormViewPropertyByCode(formView.getCode());
					//List<FormProperty> formPropertyList=formPropertyService.getFormPropertyListByformId(fromId);
					if(formViewPropertyList!=null&&formViewPropertyList.size()>0){
						for (FormViewProperty formViewProperty : formViewPropertyList) {
							FormViewProperty newformViewProperty=new FormViewProperty(formViewProperty.getId(), formViewProperty.getType(), formViewProperty.getIdx(), formViewProperty.getOccupyColumn(), formViewProperty.getBlankColumn(),formViewProperty.getEmpty(),formViewProperty.getTdHtml(),formViewProperty.getExtraAttributes());
							if(formViewProperty.getFormProperty()!=null){
								try {
									FormProperty formProperty = (FormProperty) formPropertyService.findObjByKey(FormProperty.class, formViewProperty.getFormProperty().getId());
									FormProperty newformProperty=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(),   formProperty.getElementType(),  formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(),  formProperty.getEmpty(), formProperty.getValidator(),formProperty.getDataType()); 
									newformViewProperty.setFormProperty(newformProperty);	
								} catch (Exception e) {
									System.out.println("formProperty 找不到");
									e.printStackTrace();
								}
								
							}
							newformViewPropertyList.add(newformViewProperty);
						}
					}  
					FormView newFormView =new FormView(formView.getId(),formView.getCode(),formView.getOccupyColumn(),newformViewPropertyList);
					staticFormViewMap.put(formView.getCode(), newFormView);  
				} catch (Exception e) { 
					e.printStackTrace();
				}
			}
		}
		InitZflowUtil.staticFormViewMap=staticFormViewMap;
		
	}
	 
	//SelectTableList
	private void staticSelectTableMapFun(ISelectTableListService selectTableListService,ISelectListService selectListService,
			ISelectConditionsService selectConditionsService,IFormPropertyService formPropertyService) {
		
		Map<String,SelectTableList> staticSelectTableListMap=new HashMap<String, SelectTableList>();
		
		List<SelectTableList> selectTableListcons= selectTableListService.findSelectTableListByAll();
		if(selectTableListcons!=null&&selectTableListcons.size()>0){
			for (SelectTableList selectTableList : selectTableListcons) {
				try {
					SelectTableList newSelectTableList=getSelectTableAndSubList(selectTableList, selectTableListService, selectListService, selectConditionsService, formPropertyService);
					staticSelectTableListMap.put(selectTableList.getCode(), newSelectTableList);
				} catch (Exception e) {
					System.out.println("SelectTableList  error");
					e.printStackTrace();
				} 
			}
		}
		InitZflowUtil.staticSelectTableListMap=staticSelectTableListMap;
	}
	 
	private SelectTableList getSelectTableAndSubList(SelectTableList selectTableList,ISelectTableListService selectTableListService,ISelectListService selectListService,
			ISelectConditionsService selectConditionsService,IFormPropertyService formPropertyService) throws Exception{
		SelectTableList newSelectTableList= new SelectTableList();
		if(selectTableList!=null){
			newSelectTableList.setId(selectTableList.getId());
			newSelectTableList.setCode(selectTableList.getCode());
			newSelectTableList.setDescription(selectTableList.getDescription());
			newSelectTableList.setSelectTableSql(selectTableList.getSelectTableSql());
			newSelectTableList.setOrderBy(selectTableList.getOrderBy());
			newSelectTableList.setSubCondition(selectTableList.getSubCondition());
			newSelectTableList.setOccupyColumn(selectTableList.getOccupyColumn());
			//查询列
			List<SelectList> selectList= selectListService.findByTableId(selectTableList.getId());
			if(selectList!=null){
				List<SelectList> newselectList=new ArrayList<SelectList>();
				for (SelectList selectListtemp : selectList) {
					SelectList newselect=new SelectList(selectListtemp.getId(), selectListtemp.getColumnName(), selectListtemp.getAliasesName(), selectListtemp.getDescription(), selectListtemp.getIsDisplay(), selectListtemp.getIdx(),selectListtemp.getExportType());
					newselectList.add(newselect);
				}
				newSelectTableList.setSelectLists(newselectList);
			}
			//查询条件
			List<SelectConditions>  selectConditions=selectConditionsService.findByTableId(selectTableList.getId()); 
			if(selectConditions!=null){
				List<SelectConditions> newSelectConditions=new ArrayList<SelectConditions>();
				for (SelectConditions selectConditionstemp : selectConditions) {
					SelectConditions newSelectConditions1=new SelectConditions(selectConditionstemp.getId(), selectConditionstemp.getColumnName(), selectConditionstemp.getDescription(), selectConditionstemp.getType(), selectConditionstemp.getIdx(), selectConditionstemp.getOccupyColumn(), selectConditionstemp.getBlankColumn(), selectConditionstemp.getExtraAttributes(), selectConditionstemp.getTdHtml(), selectConditionstemp.getEmpty(),selectConditionstemp.getOperatorType(),selectConditionstemp.getIsSelectData());
					if(selectConditionstemp.getFormProperty()!=null&&selectConditionstemp.getFormProperty().getId()!=null){
						FormProperty formProperty=(FormProperty)formPropertyService.findObjByKey(FormProperty.class, selectConditionstemp.getFormProperty().getId());
						FormProperty formProperty2=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(), formProperty.getElementType(), formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(), formProperty.getEmpty(), formProperty.getValidator(), formProperty.getDataType());
						newSelectConditions1.setFormProperty(formProperty2);
					}
					newSelectConditions.add(newSelectConditions1);
				}
				newSelectTableList.setSelectConditionsList(newSelectConditions);
			}
			if(selectTableList.getSelectTableListSubset()!=null&&selectTableList.getSelectTableListSubset().getId()!=null){
				SelectTableList subSelectTableList= selectTableListService.findObjByKey(SelectTableList.class, selectTableList.getSelectTableListSubset().getId());
				SelectTableList newsubSelectTableList=getSelectTableAndSubList(subSelectTableList, selectTableListService, selectListService, selectConditionsService, formPropertyService);//(subSelectTableList);	
				newSelectTableList.setSelectTableListSubset(newsubSelectTableList);
			}
		}
		return newSelectTableList;
	}
	//dictionary
	private void staticDictionaryMapFun(IDataDictionaryService dataDictionaryService){
		Map<String,String> map=new HashMap<String,String>();
		map.put("code", "1");
		List<DataDictionaryCode>  dataDictionaryCodeList= dataDictionaryService.getDictionaryCodeByTypeCode(map);
		
		Map <String , List<DataDictionaryValue>> staticDictionaryMap=new HashMap<String, List<DataDictionaryValue>>();
		if(dataDictionaryCodeList!=null&&dataDictionaryCodeList.size()>0){
			for (DataDictionaryCode dataDictionaryCode : dataDictionaryCodeList) {
				try {
					List<DataDictionaryValue> dataDictionaryValueAllList= dataDictionaryService.getDataDictionaryValueByCode(dataDictionaryCode.getCode());
					if(dataDictionaryValueAllList!=null){
						for (DataDictionaryValue dataDictionaryValue : dataDictionaryValueAllList) {
							dataDictionaryValue.setDataDictionaryCode(null);
						}
					}
					staticDictionaryMap.put(dataDictionaryCode.getCode(), dataDictionaryValueAllList);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}	
		}
		InitZflowUtil.staticDictionaryMap=staticDictionaryMap;
	}

	//staticExcelTableMapFun
	private void staticExcelTableMapFun(IExcelTableListService excelTableListService,IExcelListService excelListService) { 
		Map<String,ExcelTableList> staticExcelTableListMap=new HashMap<String, ExcelTableList>(); 
		List<ExcelTableList> excelTableListcons= excelTableListService.findExcelTableListByAll();
		if(excelTableListcons!=null&&excelTableListcons.size()>0){
			for (ExcelTableList excelTableList : excelTableListcons) {
				try {
					ExcelTableList newExcelTableList=getExcelTableList(excelTableList, excelListService) ;
					staticExcelTableListMap.put(newExcelTableList.getCode(), newExcelTableList);
				} catch (Exception e) {
					System.out.println("ExcelTableList  error");
					e.printStackTrace();
				} 
			}
		}
		InitZflowUtil.staticExcelTableListMap=staticExcelTableListMap;
	}
	 
	private ExcelTableList getExcelTableList(ExcelTableList excelTableList,IExcelListService excelListService) throws Exception{
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
	//PackageItem  报价的套餐的缓存
	public void staticPackageItemMapFun(IFormViewService formViewService){
		Map<String,List<Map<String, Object>>> staticPackageItemListMap =new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> packageList= formViewService.callShellProcedure("R2010001|79");
		List<Map<String, Object>> packageList80= formViewService.callShellProcedure("R2010001|80");
		packageList.addAll(packageList80);
		for (Map<String, Object> packageMap : packageList) {
			Object id= packageMap.get("id");
			List<Map<String, Object>> packageItemList= formViewService.callShellProcedure("R2009001|"+id);
			staticPackageItemListMap.put(id.toString(), packageItemList);
		} 
		InitZflowUtil.staticPackageItemListMap=staticPackageItemListMap;
	}
}
