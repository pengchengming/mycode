package com.bizduo.zflow.controller.form.formMini;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.form.SelectConditionComplete;
import com.bizduo.zflow.domain.form.SelectConditions;
import com.bizduo.zflow.domain.form.SelectList;
import com.bizduo.zflow.domain.form.SelectListComplete;
import com.bizduo.zflow.domain.form.SelectTableList;
import com.bizduo.zflow.domain.form.SelectTableListComplete;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.domain.tableData.DataTableToPage;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.customform.IFormPropertyService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.ISelectConditionsService;
import com.bizduo.zflow.service.customform.ISelectListService;
import com.bizduo.zflow.service.customform.ISelectTableListCompleteService;
import com.bizduo.zflow.service.customform.ISelectTableListService;
import com.bizduo.zflow.service.table.IZColumnService;
import com.bizduo.zflow.util.SelectToJson;
import com.bizduo.zflow.util.TimeUtil;
import com.bizduo.zflow.util.UserUtil;
import com.bizduo.zflow.util.ccm.UploadFileStatus;
import com.bizduo.zflow.util.deco.InitZflowUtil;
import com.bizduo.zflow.wrapper.OrgAndUserWrapper;
@Controller
@RequestMapping(value = "/createSelect")
public class CreateSelectController {
	@Autowired
	private IFormService formService;
	@Autowired
	private IZColumnService zColumnService; 
	@Autowired
	public ISelectTableListService selectTableListService;
	@Autowired
	public ISelectListService selectListService;
	@Autowired
	public ISelectConditionsService selectConditionsService;  
	@Autowired
	private IFormPropertyService formPropertyService;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public  ISelectTableListCompleteService selectTableListCompleteService;

	@Autowired
	private IBizTypeService  bizTypeService;
	
	
	@RequestMapping(value = "selectFormProperty")
	@ResponseBody
	public Collection<OrgAndUserWrapper> selectFormProperty(
			@RequestParam(value = "id", required = true) String id
		) throws NumberFormatException, Exception{
		List<OrgAndUserWrapper> ouws = new ArrayList<OrgAndUserWrapper>();
		String[] ids= id.split("_");
		if(ids!=null&&ids.length>0){
			Form form=(Form) this.formService.findObjByKey(Form.class, Long.parseLong(ids[1]));
			if(form!=null&&form.getZtable()!=null){
				List<ZColumn> zcolumnList= this.zColumnService.getZColumnByTableIdList(form.getZtable().getId());
				 for(ZColumn column : zcolumnList){
					ouws.add(new OrgAndUserWrapper("property_"+column.getId().toString(),Integer.parseInt(ids[1]),column.getColName(),column.getComment(), false));
				 } 
			} 
		}
		return ouws;
	}
	
	@RequestMapping(value = "selectFormProperty2")
	@ResponseBody
	public Collection<OrgAndUserWrapper> selectFormProperty2(
			@RequestParam(value = "id", required = true) String id
		) throws NumberFormatException, Exception{
		List<OrgAndUserWrapper> ouws = new ArrayList<OrgAndUserWrapper>();
		String[] ids= id.split("_");
		if(ids!=null&&ids.length>0){
			Form form=(Form) this.formService.findObjByKey(Form.class, Long.parseLong(ids[1]));
			if(form!=null&&form.getZtable()!=null){
				List<ZColumn> zcolumnList= this.zColumnService.getZColumnByTableIdList(form.getZtable().getId());
				 for(ZColumn column : zcolumnList){ 
					FormProperty formProperty= formPropertyService.getFormPropertyByZcolumnId(column.getId());
					Long formPropertyId=null;
					if(formProperty!=null)
						formPropertyId=formProperty.getId();
					ouws.add(new OrgAndUserWrapper("property_"+column.getId().toString(),Integer.parseInt(ids[1]),column.getColName(),column.getComment(),formPropertyId, false));
				 } 
			} 
		}
		return ouws;
	}
	
	@RequestMapping(value = "saveSelectTableList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveSelectTableList(@RequestParam(value = "jsonString", required = true) String jsonString){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObj = new JSONObject(jsonString); 
			Boolean isExist=false;
			if(jsonObj.has("selectLists")){
				org.json.JSONArray jsonArray = jsonObj.getJSONArray("selectLists");
				if(jsonArray!=null&&jsonArray.length()>0){
					isExist=true;
				}
			}
			if(!isExist){
				map.put("code", 0);
				map.put("errorMsg", "查询列不能为空");
				return map;
			} 
			/**保存 selectTableList**/
			SelectTableList selectTableList=new SelectTableList();
			if(jsonObj.has("code"))
				selectTableList.setCode(jsonObj.getString("code"));
			selectTableList.setDescription(TimeUtil.date2Str(new Date(), null)); 
			if(jsonObj.has("selectTableSql")) 
				selectTableList.setSelectTableSql(jsonObj.getString("selectTableSql"));
			selectTableList=this.selectTableListService.create(selectTableList);
			//显示列
			List<SelectList> selectLists=new ArrayList<SelectList>();
			org.json.JSONArray selectListsArray = jsonObj.getJSONArray("selectLists");
			for(int i = 0; i < selectListsArray.length(); i++) {
				JSONObject tempObj = selectListsArray.getJSONObject(i);
				SelectList  selectList =new SelectList();
				if(tempObj.has("columnName")) selectList.setColumnName(tempObj.getString("columnName"));
				if(tempObj.has("description"))selectList.setDescription(tempObj.getString("description"));
				selectList.setSelectTableList(selectTableList); 
				selectList= this.selectListService.create(selectList);
				selectLists.add(selectList);
			}
			//查询列
			List<SelectConditions> selectConditionsList=new ArrayList<SelectConditions>();
			if(jsonObj.has("selectConditions")){
				org.json.JSONArray selectConditionArray = jsonObj.getJSONArray("selectConditions");
				if(selectConditionArray!=null&&selectConditionArray.length()>0){
					for(int i = 0; i < selectConditionArray.length(); i++) {
						JSONObject tempObj = selectConditionArray.getJSONObject(i);
						SelectConditions selectConditions=new SelectConditions();
						if(tempObj.has("columnName")) selectConditions.setColumnName(tempObj.getString("columnName"));
						if(tempObj.has("description"))selectConditions.setDescription(tempObj.getString("description"));
						selectConditions.setSelectTableList(selectTableList); 
						selectConditions=this.selectConditionsService.create(selectConditions); 
						selectConditionsList.add(selectConditions);
					}
				}
			} 
		} catch (Exception e) {
			map.put("errorMsg", "保存错误");
			return map;
		}
		map.put("successMsg", "保存成功");
		return map; 
	}
	 
	@RequestMapping(value = "/findSelectTableList")
	@ResponseBody
	public  Map<String,Object>  findSelectTableList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String code= request.getParameter("code");
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
				SelectTableList newSelectTableList=new SelectTableList();
				if(ischeck){
					Map<String,SelectTableList> staticSelectTableListMap=InitZflowUtil.staticSelectTableListMap;
					newSelectTableList=staticSelectTableListMap.get(code);	
				}else{
					SelectTableList selectTableList=selectTableListService.findByTitle(code);
					newSelectTableList=getSelectTableAndSubList(selectTableList);
				}
				//子集
				map.put("results", newSelectTableList); 
			}
		} catch (Exception e) {
			map.put("code", "0"); 
			e.printStackTrace();
		}
		map.put("code", "1"); 
		return map;
	}

	private SelectTableList getSelectTableAndSubList(SelectTableList selectTableList) throws Exception{
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
				SelectTableList newsubSelectTableList=getSelectTableAndSubList(subSelectTableList);	
				newSelectTableList.setSelectTableListSubset(newsubSelectTableList);
			}
		}
		return newSelectTableList;
	}
	
	@RequestMapping(value = "/formSelectTableList", produces = "text/html")
	public String formSelectTableList( HttpServletRequest request,Model uiModel){
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("code", request.getParameter("code"));
		return "/basicData/formMini/formSelectTableList";
	}
	
	
	
	
	@RequestMapping(value = "/findselectData")
	@ResponseBody	
	public  DataTableToPage  findselectData(HttpServletRequest request, HttpServletResponse response) throws IOException{
		 
		
		String selectConditionSql=request.getParameter("selectConditionSql");
		//String selectTableSql= request.getParameter("selectTableSql");
		//String OrderBy= request.getParameter("OrderBy");
		String code= request.getParameter("code");
		
		DataTableToPage dataTableToPage = new DataTableToPage();
		if(!(code!=null&&!code.trim().equals(""))){
			dataTableToPage.setCode(0);
			return dataTableToPage;
		}
		   
			SelectTableList selectTableList=new SelectTableList();
			try {
				  selectTableList=selectTableListService.findByTitle(code);
			} catch (Exception e) {
				// TODO: handle exception
			} 
			if(selectTableList==null||selectTableList.getId()==null){
				dataTableToPage.setCode(0);
				dataTableToPage.setErrorMsg("传入的code编号出错");
			}
			PageTrace pageTrace = null;
			if(request.getParameter("pageIndex")!=null){
				Integer pageIndex= Integer.parseInt(request.getParameter("pageIndex"));
				Integer pageSize= Integer.parseInt(request.getParameter("pageSize"));
				pageTrace = new PageTrace(pageSize);
				pageTrace.setPageIndex(pageIndex);
			}
			String json = selectTableListService.selectBySqlPage(selectTableList,selectConditionSql,pageTrace,1);
			try {
				JSONObject jsonObj = new JSONObject(json);
				if (jsonObj.has("code"))
					dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
				else
					dataTableToPage.setCode(0);
				dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
				if (jsonObj.has("paged"))
					dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		return dataTableToPage;
	}
	
	@RequestMapping(value = "/findsubSelectData")
	@ResponseBody	
	public  DataTableToPage  findsubSelectData(HttpServletRequest request, HttpServletResponse response) throws IOException{
		 
		//String selectConditionSql=request.getParameter("selectConditionSql");
		//String selectTableSql= request.getParameter("selectTableSql");
		//String OrderBy= request.getParameter("OrderBy");
		String code= request.getParameter("code");
		String dataId= request.getParameter("id");
		String subCondition= request.getParameter("subCondition");
		String queryConditions= request.getParameter("queryConditions");
		
		
		DataTableToPage dataTableToPage = new DataTableToPage();
		if(!(code!=null&&!code.trim().equals(""))){
			dataTableToPage.setCode(0);
			return dataTableToPage;
		}
		PageTrace pageTrace =new PageTrace();
		  if(request.getParameter("pageIndex")!=null&&request.getParameter("pageSize")!=null){
				Integer pageIndex= Integer.parseInt(request.getParameter("pageIndex"));
				Integer pageSize= Integer.parseInt(request.getParameter("pageSize"));
				pageTrace = new PageTrace(pageSize);
				pageTrace.setPageIndex(pageIndex);  
		  }else 
			  pageTrace=null;
		  
			SelectTableList selectTableList=new SelectTableList();
			try {
				  selectTableList=selectTableListService.findByTitle(code);
			} catch (Exception e) {
			} 
			if(selectTableList==null||selectTableList.getId()==null){
				dataTableToPage.setCode(0);
				dataTableToPage.setErrorMsg("传入的code编号出错");
			}  
			String selectConditionSql=" where  1=1 ";
			if(StringUtils.isNotBlank(subCondition)){
				selectConditionSql += subCondition+" = "+dataId;
			}
			if(queryConditions!=null&&!queryConditions.trim().equals(""))
				selectConditionSql+=" "+queryConditions;
			String json = selectTableListService.selectBySqlPage(selectTableList,selectConditionSql,pageTrace,1);
			
			try {
				JSONObject jsonObj = new JSONObject(json);
				if (jsonObj.has("code"))
					dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
				else
					dataTableToPage.setCode(0);
				dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
				if (jsonObj.has("paged"))
					dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		return dataTableToPage;
	}

	@RequestMapping(value = "/showFormDataById")
	@ResponseBody
	public  DataTableToPage  showFormDataById(HttpServletRequest request, HttpServletResponse response) {
		String fromIdStr= request.getParameter("formId");
		String dataId= request.getParameter("dataId");
		try {
			Map<String,String> fieldMap=new HashMap<String, String>(); 
			if(fromIdStr!=null&&!fromIdStr.trim().equals("")){
				Long fromId=Long.parseLong(fromIdStr); 
				String sql="";
				String tableSql=""; 
				Form frombase=(Form)this.formService.findObjByKey(Form.class, fromId);
				List<FormProperty> formPropertyList=formPropertyService.getFormPropertyListByformId(fromId);
				tableSql+=" from "+frombase.getFormName();
				if(formPropertyList!=null&&formPropertyList.size()>0){					
					for (FormProperty formProperty : formPropertyList) {
						//FormProperty newformProperty=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(),   formProperty.getElementType(), formProperty.getDictionaryType(), formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(), formProperty.getOccupyRow(), formProperty.getEmpty(), formProperty.getValidator()); 
						sql+=frombase.getFormName()+"."+formProperty.getFieldName()+",";
						fieldMap.put(formProperty.getFieldName(), formProperty.getFieldName());						
					} 
				}
				//map.put("results", newformPropertyList);
				List<Form>  formList=this.formService.findSubsetForm(fromId);
				if(formList!=null&&formList.size()>0){
					for (Form form2 : formList) {
						List<FormProperty> formPropertyExpandList=formPropertyService.getFormPropertyListByformId(form2.getId());
						if(formPropertyExpandList!=null&&formPropertyExpandList.size()>0){
							tableSql +=" left join "+form2.getFormName()+" on "+form2.getFormName()+".expandId="+frombase.getFormName()+".id";
							for (FormProperty formProperty : formPropertyExpandList) {
								//FormProperty newformProperty=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(), formProperty.getElementType(), formProperty.getDictionaryType(), formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(), formProperty.getOccupyRow(), formProperty.getEmpty(), formProperty.getValidator()); 
								sql+=form2.getFormName()+"."+formProperty.getFieldName()+",";
								fieldMap.put(formProperty.getFieldName(), formProperty.getFieldName());								
							}
						}
					} 
				}
				if(!sql.trim().equals("")){
					sql="select "+sql.substring(0, sql.length()-1)+tableSql +" where "+frombase.getFormName()+".id="+dataId;
					Map<String,String> sqlMap=new HashMap<String, String>(); 
					sqlMap.put("sql",sql);
					String json =SelectToJson.jdbcTemplateQuerysql(jdbcTemplate, sqlMap, fieldMap, null).toString(); 
					//SelectToJson.executionsql(dataSource, sql, "query", fieldMap, null);					
					DataTableToPage dataTableToPage = new DataTableToPage();
					try {
						JSONObject jsonObj = new JSONObject(json);
						if (jsonObj.has("code"))
							dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
						else
							dataTableToPage.setCode(0);
						dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
						if (jsonObj.has("paged"))
							dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
					} catch (JSONException e) {
						e.printStackTrace();
					} 
					return dataTableToPage;
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}  
		return null;
	}
	
	@RequestMapping(value = "/showFormInternalDataById")
	@ResponseBody
	public  DataTableToPage  showFormInternalDataById(HttpServletRequest request, HttpServletResponse response) {
		String fromIdStr= request.getParameter("formId");
		String dataId= request.getParameter("dataId"); 
		String propertyCodeStr=request.getParameter("propertyCodes");
		DataTableToPage dataTableToPage = new DataTableToPage();
		try {
			Long fromId=Long.parseLong(fromIdStr); 
			Form frombase=(Form)this.formService.findObjByKey(Form.class, fromId);
			String sql="";
			String tableSql=" from "+frombase.getFormName();
			Map<String,String> fieldMap=new HashMap<String, String>(); 
			if(propertyCodeStr!=null&&!propertyCodeStr.trim().equals("")){
				String[] propertyCodes= propertyCodeStr.split(",");
				for (String propertyCode : propertyCodes) {
					if(!propertyCode.trim().equals("")){
						sql+=frombase.getFormName()+"."+propertyCode+",";
						fieldMap.put(propertyCode, propertyCode);
					} 
				}
			}
			sql+=frombase.getFormName()+".createDate,"+frombase.getFormName()+".modifyDate,"+frombase.getFormName()+".createBy,"+frombase.getFormName()+".modifyBy,"+
			" createByUser.realname as createByName,createByUser.username as createByUsername, modifyByUser.realname as modifyByName,modifyByUser.username as modifyByUsername,";
			tableSql+=" left  join global_user as createByUser on "+frombase.getFormName()+".createBy=createByUser.id" +
			 " left  join global_user as modifyByUser on "+frombase.getFormName()+".modifyBy=modifyByUser.id ";
			 
			fieldMap.put("createDate", "createDate");
			fieldMap.put("modifyDate", "modifyDate");
			fieldMap.put("createBy", "createBy");
			fieldMap.put("modifyBy", "modifyBy");
			fieldMap.put("createByName", "createByName");
			fieldMap.put("createByUsername", "createByUsername");
			fieldMap.put("modifyByName", "modifyByName");
			fieldMap.put("modifyByUsername", "modifyByUsername");
			  
			if(!sql.trim().equals("")){
				sql="select "+sql.substring(0, sql.length()-1)+tableSql +" where "+frombase.getFormName()+".id="+dataId;	
				//String json = SelectToJson.executionsql(dataSource, sql, "query", fieldMap, null);
				Map<String,String> sqlMap=new HashMap<String, String>(); 
				sqlMap.put("sql",sql);
				String json =SelectToJson.jdbcTemplateQuerysql(jdbcTemplate, sqlMap, fieldMap, null).toString(); 
				
				
				try {
					JSONObject jsonObj = new JSONObject(json);
					if (jsonObj.has("code"))
						dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
					else
						dataTableToPage.setCode(0);
					dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
					if (jsonObj.has("paged"))
						dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				} 
				return dataTableToPage;
			}
		} catch (Exception e) { 
			dataTableToPage.setCode(0);
			e.printStackTrace();
		}
		return dataTableToPage;
	}
	
	
	@RequestMapping(value = "/createSelectList", produces = "text/html")
	public String createSelectList(@RequestParam(value = "type", required = false) Integer type, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/basicData/formMini/createSelectCompleteList";
	}
	
	@RequestMapping(value = "saveSelectTableListComplete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveSelectTableListComplete(@RequestParam(value = "jsonString", required = true) String jsonString){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObj = new JSONObject(jsonString); 
			Boolean isExist=false;
			if(jsonObj.has("selectLists")){
				org.json.JSONArray jsonArray = jsonObj.getJSONArray("selectLists");
				if(jsonArray!=null&&jsonArray.length()>0){
					isExist=true;
				}
			}
			if(!isExist){
				map.put("code", 0);
				map.put("errorMsg", "查询列不能为空");
				return map;
			} 
			/**保存 selectTableList**/
			SelectTableListComplete selectTableListComplete=new SelectTableListComplete();
			if(jsonObj.has("code"))
				selectTableListComplete.setCode(jsonObj.getString("code"));
			selectTableListComplete.setDescription(TimeUtil.date2Str(new Date(), null)); 
			if(jsonObj.has("selectTableSql")) 
				selectTableListComplete.setSelectTableSql(jsonObj.getString("selectTableSql"));
			if(jsonObj.has("selectFromSql")) 
				selectTableListComplete.setSelectFromSql(jsonObj.getString("selectFromSql"));
			if(jsonObj.has("occupyColumn")) 
				selectTableListComplete.setOccupyColumn(jsonObj.getInt("occupyColumn"));
			
			selectTableListComplete=this.selectTableListCompleteService.createSelectTableListComplete(selectTableListComplete);
			//显示列
			List<SelectListComplete> selectListCompletes=new ArrayList<SelectListComplete>();
			org.json.JSONArray selectListsArray = jsonObj.getJSONArray("selectLists");
			for(int i = 0; i < selectListsArray.length(); i++) {
				JSONObject tempObj = selectListsArray.getJSONObject(i);
				SelectListComplete  selectListComplete =new SelectListComplete();
				if(tempObj.has("columnName")) selectListComplete.setColumnName(tempObj.getString("columnName"));
				if(tempObj.has("description"))selectListComplete.setDescription(tempObj.getString("description"));
				if(tempObj.has("aliasesName"))selectListComplete.setAliasesName(tempObj.getString("aliasesName"));
				if(tempObj.has("idx")) selectListComplete.setIdx(tempObj.getInt("idx"));
				if(tempObj.has("isDisplay")) selectListComplete.setIsDisplay(tempObj.getInt("isDisplay"));
				
				selectListComplete.setSelectTableListComplete(selectTableListComplete); 
				selectListComplete= this.selectTableListCompleteService.createSelectListComplete(selectListComplete);
				selectListCompletes.add(selectListComplete);
			}
			//查询列
			List<SelectConditionComplete> selectConditionCompleteList=new ArrayList<SelectConditionComplete>();
			if(jsonObj.has("selectConditions")){
				org.json.JSONArray selectConditionArray = jsonObj.getJSONArray("selectConditions");
				if(selectConditionArray!=null&&selectConditionArray.length()>0){
					for(int i = 0; i < selectConditionArray.length(); i++) {
						JSONObject tempObj = selectConditionArray.getJSONObject(i);
						SelectConditionComplete selectConditionComplete=new SelectConditionComplete();
						if(tempObj.has("columnName")) selectConditionComplete.setColumnName(tempObj.getString("columnName"));
						if(tempObj.has("description"))selectConditionComplete.setDescription(tempObj.getString("description"));
						selectConditionComplete.setSelectTableListComplete(selectTableListComplete); 
 
						if(tempObj.has("formPropertyId")){
							FormProperty formProperty= (FormProperty) formPropertyService.findObjByKey(FormProperty.class, tempObj.getLong("formPropertyId"));
							//FormProperty formProperty= formPropertyService.getFormPropertyByZcolumnId(tempObj.getLong("zcolumnId"));
							//FormProperty formProperty=(FormProperty)this.formPropertyService.findObjByKey(FormProperty.class, );
							if(formProperty!=null){
								selectConditionComplete.setFormProperty(formProperty);
								selectConditionComplete.setExtraAttributes(formProperty.getExtraAttributes());	
							}
						}else {
							String idstr= selectConditionComplete.getColumnName().replace(".", "_");
							String extraAttributes="{'type':'text','size':'20','id':'"+idstr+"','style':'width:100px' ";
							if(selectConditionComplete.getColumnName().indexOf(".createDate")>0 || 
									selectConditionComplete.getColumnName().indexOf(".modifyDate") >0 ){
								extraAttributes += " , 'fieldType':'attrDate'"; 
							}
							if(selectConditionComplete.getColumnName().indexOf(".createBy")>0 || 
									selectConditionComplete.getColumnName().indexOf(".modifyBy") >0 ){
								extraAttributes += " , 'fieldType':'attrUser','roleCode':'allUsers' "; 
							} 
							extraAttributes +=" }";
							selectConditionComplete.setExtraAttributes(extraAttributes);
						}
						selectConditionComplete.setType(tempObj.getInt("type"));
						selectConditionComplete.setIdx(tempObj.getInt("idx"));
						selectConditionComplete.setOccupyColumn(tempObj.getInt("occupyColumn"));
						selectConditionComplete.setBlankColumn(tempObj.getInt("blankColumn"));
						selectConditionComplete.setEmpty(true);
 
						selectConditionComplete=this.selectTableListCompleteService.createSelectConditionComplete(selectConditionComplete); 
						selectConditionCompleteList.add(selectConditionComplete);
					}
				}
			} 
		} catch (Exception e) {
			map.put("errorMsg", "保存错误");
			return map;
		}
		map.put("successMsg", "保存成功");
		return map; 
	}
	
	
	@RequestMapping(value = "/findSelectTableListComplete")
	@ResponseBody
	public  Map<String,Object>  findSelectTableListComplete(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String code= request.getParameter("code");
		try {
			if(code!=null&&!code.trim().equals("")){
				SelectTableListComplete selectTableListComplete=selectTableListCompleteService.findByCode(code);
				SelectTableListComplete newSelectTableList=getSelectTableAndSubListComplete(selectTableListComplete);
				//子集
				map.put("results", newSelectTableList); 
			}
		} catch (Exception e) {
			map.put("code", "0"); 
			e.printStackTrace();
		}
		map.put("code", "1"); 
		return map;
	}
	
	private SelectTableListComplete getSelectTableAndSubListComplete(SelectTableListComplete selectTableListComplete) throws Exception{
		SelectTableListComplete newSelectTableListComplete= new SelectTableListComplete();
		if(selectTableListComplete!=null){
			newSelectTableListComplete.setId(selectTableListComplete.getId());
			newSelectTableListComplete.setCode(selectTableListComplete.getCode());
			newSelectTableListComplete.setDescription(selectTableListComplete.getDescription());
			newSelectTableListComplete.setSelectTableSql(selectTableListComplete.getSelectTableSql());
			newSelectTableListComplete.setOrderBy(selectTableListComplete.getOrderBy());
			newSelectTableListComplete.setSubCondition(selectTableListComplete.getSubCondition());
			newSelectTableListComplete.setOccupyColumn(selectTableListComplete.getOccupyColumn());
			//查询列
			List<SelectListComplete> SelectListCompleteList= this.selectTableListCompleteService.findByTableId(selectTableListComplete.getId());			
			if(SelectListCompleteList!=null){
				List<SelectListComplete> newSelectListCompleteList=new ArrayList<SelectListComplete>();
				for (SelectListComplete selectListCompletetemp : SelectListCompleteList) {
					SelectListComplete selectListComplete=new SelectListComplete(selectListCompletetemp.getId(), selectListCompletetemp.getColumnName(), selectListCompletetemp.getAliasesName(), selectListCompletetemp.getDescription(), selectListCompletetemp.getIsDisplay(), selectListCompletetemp.getIdx());
					newSelectListCompleteList.add(selectListComplete);
				}
				newSelectTableListComplete.setSelectListComplete(newSelectListCompleteList);
			}
			//查询条件
			List<SelectConditionComplete>  selectConditionCompleteList=selectTableListCompleteService.findSelectConditionCompleteByTableId(selectTableListComplete.getId()); 
			if(selectConditionCompleteList!=null){
				List<SelectConditionComplete> newSelectConditionCompleteList=new ArrayList<SelectConditionComplete>();
				for (SelectConditionComplete selectConditionstemp : selectConditionCompleteList) {
					SelectConditionComplete newSelectConditions1=new SelectConditionComplete(selectConditionstemp.getId(), selectConditionstemp.getColumnName(), selectConditionstemp.getDescription(), selectConditionstemp.getType(), selectConditionstemp.getIdx(), selectConditionstemp.getOccupyColumn(), selectConditionstemp.getBlankColumn(), selectConditionstemp.getExtraAttributes(), selectConditionstemp.getTdHtml(), selectConditionstemp.getEmpty(),selectConditionstemp.getOperatorType());
					if(selectConditionstemp.getFormProperty()!=null&&selectConditionstemp.getFormProperty().getId()!=null){
						FormProperty formProperty=(FormProperty)this.formPropertyService.findObjByKey(FormProperty.class, selectConditionstemp.getFormProperty().getId());
						FormProperty formProperty2=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(), formProperty.getElementType(), formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(), formProperty.getEmpty(), formProperty.getValidator(), formProperty.getDataType());
						newSelectConditions1.setFormProperty(formProperty2);
					}
					newSelectConditionCompleteList.add(newSelectConditions1);
				}
				newSelectTableListComplete.setSelectConditionComplete(newSelectConditionCompleteList);
			}
			if(selectTableListComplete.getSelectTableListSubset()!=null&&selectTableListComplete.getSelectTableListSubset().getId()!=null){
				SelectTableListComplete subSelectTableListComplete= selectTableListCompleteService.findbyId(SelectTableListComplete.class, selectTableListComplete.getSelectTableListSubset().getId());
				
				SelectTableListComplete newsubSelectTableListComplete=getSelectTableAndSubListComplete(subSelectTableListComplete);	
				newSelectTableListComplete.setSelectTableListSubset(newsubSelectTableListComplete);
			}
		}
		return newSelectTableListComplete;
	}
	
	@RequestMapping(value = "/findselectDataComplete")
	@ResponseBody	
	public  DataTableToPage  findselectDataComplete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String selectConditionSql=request.getParameter("selectConditionSql");
		String code= request.getParameter("code");
		DataTableToPage dataTableToPage = new DataTableToPage();
		if(!(code!=null&&!code.trim().equals(""))){
			dataTableToPage.setCode(0);
			return dataTableToPage;
		}  
		SelectTableListComplete selectTableListComplete=new SelectTableListComplete();
			try {
				selectTableListComplete=selectTableListCompleteService.findByCode(code);
			} catch (Exception e) {
				// TODO: handle exception
			} 
			if(selectTableListComplete==null||selectTableListComplete.getId()==null){
				dataTableToPage.setCode(0);
				dataTableToPage.setErrorMsg("传入的code编号出错");
			}
			PageTrace pageTrace = null;
			if(request.getParameter("pageIndex")!=null){
				Integer pageIndex= Integer.parseInt(request.getParameter("pageIndex"));
				Integer pageSize= Integer.parseInt(request.getParameter("pageSize"));
				pageTrace = new PageTrace(pageSize);
				pageTrace.setPageIndex(pageIndex);
			}
			String json = selectTableListCompleteService.selectBySqlPage(selectTableListComplete,selectConditionSql,pageTrace);
			try {
				JSONObject jsonObj = new JSONObject(json);
				if (jsonObj.has("code"))
					dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
				else
					dataTableToPage.setCode(0);
				dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
				if (jsonObj.has("paged"))
					dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		return dataTableToPage;
	}
	
	@RequestMapping(value = "/findsubSelectDataComplete")
	@ResponseBody	
	public  DataTableToPage  findsubSelectDataComplete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		  
		String code= request.getParameter("code");
		String dataId= request.getParameter("id");
		String subCondition= request.getParameter("subCondition");
		String queryConditions= request.getParameter("queryConditions");
		
		
		DataTableToPage dataTableToPage = new DataTableToPage();
		if(!(code!=null&&!code.trim().equals(""))){
			dataTableToPage.setCode(0);
			return dataTableToPage;
		}
		PageTrace pageTrace =new PageTrace();
		  if(request.getParameter("pageIndex")!=null&&request.getParameter("pageSize")!=null){
				Integer pageIndex= Integer.parseInt(request.getParameter("pageIndex"));
				Integer pageSize= Integer.parseInt(request.getParameter("pageSize"));
				pageTrace = new PageTrace(pageSize);
				pageTrace.setPageIndex(pageIndex);  
		  }else 
			  pageTrace=null;
		  
		  SelectTableListComplete selectTableListComplete=new SelectTableListComplete();
			try {
				selectTableListComplete=selectTableListCompleteService.findByCode(code);
			} catch (Exception e) {
			} 
			if(selectTableListComplete==null||selectTableListComplete.getId()==null){
				dataTableToPage.setCode(0);
				dataTableToPage.setErrorMsg("传入的code编号出错");
			}  
			String selectConditionSql=" where " +subCondition+" = "+dataId;
			if(queryConditions!=null&&!queryConditions.trim().equals(""))
				selectConditionSql+=" "+queryConditions;
			String json = selectTableListCompleteService.selectBySqlPage(selectTableListComplete,selectConditionSql,pageTrace);
			
			try {
				JSONObject jsonObj = new JSONObject(json);
				if (jsonObj.has("code"))
					dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
				else
					dataTableToPage.setCode(0);
				dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
				if (jsonObj.has("paged"))
					dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		return dataTableToPage;
	}
	
	
}
