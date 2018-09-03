package com.bizduo.zflow.controller.tableData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.table.ConditionSelect;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.domain.tableData.FormDATableDAId;
import com.bizduo.zflow.domain.tableData.TableData;
import com.bizduo.zflow.domain.tabulation.MiddleTable;
import com.bizduo.zflow.json.ConditionselectJson;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.IMiddleTableService;
import com.bizduo.zflow.service.table.IConditionSelectService;
import com.bizduo.zflow.service.table.IZTableService;
import com.bizduo.zflow.service.tableData.ITableDataService;

@Controller
@RequestMapping("/tableDatas")
public class TableDataController extends BaseController {

	@Autowired
	private ITableDataService tableDataService;
//	@Autowired
//	private IZColumnService zColumnService;
	@Autowired
	private IFormService formService;
	@Autowired
	private IConditionSelectService conditionSelectService;
	@Autowired
	private IZTableService zTableService;
	
	private String tableDataJson;
	private Form form;
	private MiddleTable middleTable;
	private IMiddleTableService middleTableService;
	private List<TableData> tableDataList;
	private Long id;
	private String name;
	private Integer age;
	private Long middleTableId;
	private String parameter;
	private String tableName;
	private FormDATableDAId formDATableDAId;
	private Long formId;

	private List<ConditionSelect> conditionSelectFieldList;
	private Long conditionId;
	
	/**
	 * 查询数据
	 * @return
	 * dzt
	 */
	@RequestMapping(value = "/formTableData.do")
	@ResponseBody
	public FormDATableDAId formTableData(
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "parameter", required = true) String parameter
			){
		FormDATableDAId formDATableDAId=new FormDATableDAId();
		if(tableName!=null&&!tableName.trim().equals("")){
			formDATableDAId= this.tableDataService.selectTableNameData(tableName,parameter);
			System.out.println(formDATableDAId.getRegister());
		}
		return formDATableDAId;
	}
	/**
	 * 显示查询
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/showConditionSelect.do")
	@ResponseBody
	public List<ConditionselectJson> showConditionSelect(
			@RequestParam(value = "formId", required = true) Long formId
			) throws Exception{
		Form form= (Form)this.formService.findObjByKey(Form.class, formId);
		List<ConditionSelect> conditionSelectFieldList= this.conditionSelectService.getConditionSelectByZtableId(form.getZtable().getId());
		List<ConditionselectJson>  conditionselectJsonList=new ArrayList<ConditionselectJson>();
		for(ConditionSelect conditionSelect: conditionSelectFieldList){
			ConditionselectJson conditionselectJson=new ConditionselectJson();
			conditionselectJson.setId(conditionSelect.getId());
			conditionselectJson.setName(conditionSelect.getName());
			conditionselectJson.setDescription(conditionSelect.getDescription());
			if(conditionSelect.getCreateDate()!=null){
				 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				 String data=dataFm.format(conditionSelect.getCreateDate());
				 conditionselectJson.setCreateDate(data);
			}
			conditionselectJsonList.add(conditionselectJson);
		}
		return conditionselectJsonList;
	}
	
	/**
	 * 查询页面
	 * dzt	
	 * @throws Exception 
	 */
	@RequestMapping(value = "/selectColumnList.do")
	@ResponseBody
	public Map<String, Object> selectColumnList(
			@RequestParam(value = "conditionId", required = true) Long conditionId
			) throws Exception{
		//返回json
		Map<String, Object> map = new HashMap<String, Object>();
		ConditionSelect conditionSelect=(ConditionSelect)this.conditionSelectService.findObjByKey(ConditionSelect.class, conditionId);
		if(conditionSelect!=null){
			map.put("selectFieldList", conditionSelect.getSelectFieldList());
			map.put("columnFieldList", conditionSelect.getColumnFieldList());
			map.put("conditionActionList", conditionSelect.getActionChecks());
		}
		return map;
	}
	
	@RequestMapping(value="/getObjectsCollection.do")
	@ResponseBody
	public void getObjectsCollection(
			HttpServletResponse response,
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "parameter", required = false) String parameter
			){
		//List<ZColumn> zcolumnList=this.zColumnService.getZColumnList(tableName);
		ZTable ztable = zTableService.getZTableByName(tableName);
		JSONArray tableDatajsonArray=this.tableDataService.getTableDataByTableName(ztable,parameter);
		String tableDatajson=this.getTabledataAndColumnJson(ztable,tableDatajsonArray);
		try {
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(tableDatajson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getTabledataAndColumnJson(ZTable ztable,JSONArray tableDatajsonArray) {
		JSONObject tabledataAndColumnJson=new JSONObject();
		try {
			JSONArray tablePropertys=new JSONArray();
			if(ztable.getZcolumns()!=null&&ztable.getZcolumns().size()>0){
				int i=0;
				for(ZColumn zcolumn: ztable.getZcolumns() ){
					JSONObject tablePropertyObj=new JSONObject();
					if( zcolumn.getColName()==null||zcolumn.getColName().trim().equals("id")
							||zcolumn.getColName().trim().equals("createDate")
							)
						continue;
					tablePropertyObj.put("name", zcolumn.getColName());
					tablePropertyObj.put("description", zcolumn.getComment());
					tablePropertys.put(i, tablePropertyObj);
					i++;
				}
			}
			tabledataAndColumnJson.put("tablePropertys", tablePropertys);
			tabledataAndColumnJson.put("tableDatas", tableDatajsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tabledataAndColumnJson.toString();
	}
	
	/**
	 * 删除表中数据
	 * @param tableName
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/deleteTableData.do")
	@ResponseBody
	public Map<String, Object>  deleteTableData( 
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "id", required = true) Long  id
			) throws Exception{
		//返回json
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(tableName!=null&&!tableName.trim().equals("")){
			ZTable ztable = zTableService.getZTableByName(tableName);
			Boolean isDelete= this.tableDataService.deleteTableData(ztable,id);
			if(isDelete)
				map.put("successMsg", "删除成功");
			else 
				map.put("errorMsg", "删除失败");
		}else
			map.put("errorMsg", "删除失败");
		return map;
	}
	
	//列表的更新，修改，保存
//	public String saveTableData() throws JSONException {
//		JSONObject jsonObj = new JSONObject(tableDataJson);
//		Long formId = jsonObj.getLong("formId");
//		//Long middleTableId = jsonObj.getLong("middleTableId");
//		if (formId != null) {
//			form = formService.getFormById(formId);
//		}
//		/*if (middleTableId != null) {
//			//middleTable = middleTableService.getMiddleTableById(middleTableId);
//			//tableDataList = middleTable.getTableDataList();
//		}*/
//		org.json.JSONArray  tableRowjsonObj = jsonObj.getJSONArray("tableRowData");
//		for(int i=0 ; i<tableRowjsonObj.length() ; i++ ){
//			JSONObject tempObj = tableRowjsonObj.getJSONObject(i);
////		if(!tableRowjsonObj.isNull("id")){
////		 Long id =  (Long) tempObj.get("id");
////		}
//			String name = (String)tempObj.get("name");
//			Integer age =  (Integer)tempObj.get("age");
//			
//			try {
//				if(!tempObj.isNull("id")&&!((String)tempObj.get("id")).equals("")){
//					Long id = Long.parseLong(tempObj.get("id").toString());
//					TableData tableData = tableDataService.getTableDataById(id);
//					tableData.setName(name);
//					tableData.setAge(age);
//					tableData.setMiddleTable(middleTable);
//					tableDataList.add(tableData);
//					//middleTable.setTableDataList(tableDataList);
//					this.middleTableService.updateMiddleTable(middleTable);
//				}else{
//					TableData tableData = new TableData();
//					tableData.setName(name);
//					tableData.setAge(age);
//					tableData.setMiddleTable(middleTable);
//					tableDataList.add(tableData);
//					//middleTable.setTableDataList(tableDataList);
//					this.middleTableService.updateMiddleTable(middleTable);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return SUCCESS;
//	}
//	
//	//获得某一列表
//	public String findOneTableData() throws JSONException{
//		//JSONObject jsonObj = new JSONObject(tableDataJson);
//		//Long formId = jsonObj.getLong("formId");
//		//Long middleTableId = jsonObj.getLong("middleTableId");
//		middleTable = middleTableService.getMiddleTableById(middleTableId);
//		//tableDataList = middleTable.getTableDataList();
//		return SUCCESS;
//	}
//	
//	//获得全部列表
//	public String findAllTableData(){
//		tableDataList = tableDataService.getAllTableData();
//		return SUCCESS;
//	}
	
	// =======================GETTER AND SETTER=============================//


	public String getTableDataJson() {
		return tableDataJson;
	}

	public void setTableDataJson(String tableDataJson) {
		this.tableDataJson = tableDataJson;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public IFormService getFormService() {
		return formService;
	}

	public void setFormService(IFormService formService) {
		this.formService = formService;
	}

	public MiddleTable getMiddleTable() {
		return middleTable;
	}

	public void setMiddleTable(MiddleTable middleTable) {
		this.middleTable = middleTable;
	}

	public IMiddleTableService getMiddleTableService() {
		return middleTableService;
	}

	public void setMiddleTableService(IMiddleTableService middleTableService) {
		this.middleTableService = middleTableService;
	}

	public List<TableData> getTableDataList() {
		return tableDataList;
	}

	public void setTableDataList(List<TableData> tableDataList) {
		this.tableDataList = tableDataList;
	}

	public ITableDataService getTableDataService() {
		return tableDataService;
	}

	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Long getMiddleTableId() {
		return middleTableId;
	}

	public void setMiddleTableId(Long middleTableId) {
		this.middleTableId = middleTableId;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public FormDATableDAId getFormDATableDAId() {
		return formDATableDAId;
	}

	public void setFormDATableDAId(FormDATableDAId formDATableDAId) {
		this.formDATableDAId = formDATableDAId;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public IConditionSelectService getConditionSelectService() {
		return conditionSelectService;
	}

	public void setConditionSelectService(
			IConditionSelectService conditionSelectService) {
		this.conditionSelectService = conditionSelectService;
	}

	public List<ConditionSelect> getConditionSelectFieldList() {
		return conditionSelectFieldList;
	}

	public void setConditionSelectFieldList(
			List<ConditionSelect> conditionSelectFieldList) {
		this.conditionSelectFieldList = conditionSelectFieldList;
	}

	public Long getConditionId() {
		return conditionId;
	}

	public void setConditionId(Long conditionId) {
		this.conditionId = conditionId;
	}

}
