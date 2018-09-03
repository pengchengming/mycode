package com.bizduo.zflow.controller.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.form.SelectConditions;
import com.bizduo.zflow.domain.form.SelectList;
import com.bizduo.zflow.domain.form.SelectTableList;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.domain.tableData.DataTableToPage;
import com.bizduo.zflow.service.customform.IFormPropertyService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.ISelectConditionsService;
import com.bizduo.zflow.service.customform.ISelectListService;
import com.bizduo.zflow.service.customform.ISelectTableListService;
import com.bizduo.zflow.service.formView.IFinderService;
import com.bizduo.zflow.service.table.IZColumnService;
import com.bizduo.zflow.util.TimeUtil;
import com.bizduo.zflow.util.UserUtil;
import com.bizduo.zflow.wrapper.OrgAndUserWrapper;
@Controller
@RequestMapping(value = "/finder")
public class FinderController {
	@Autowired
	private IFormService formService;
	@Autowired
	public IFinderService finderService;
	@Autowired
	private IZColumnService zColumnService; 
	@Autowired
	public ISelectListService selectListService;
	@Autowired
	private IFormPropertyService formPropertyService;
	@Autowired
	public ISelectTableListService selectTableListService;
	@Autowired
	public ISelectConditionsService selectConditionsService; 

	@RequestMapping(value = "/selectFormProperty")
	@ResponseBody
	public Collection<OrgAndUserWrapper> selectFormProperty(
			@RequestParam(value = "id", required = true) String id
		) throws NumberFormatException, Exception{
		List<OrgAndUserWrapper> ouws = new ArrayList<OrgAndUserWrapper>();
		String[] ids = id.split("_");
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
	@RequestMapping(value = "/saveSelectTableList", method = RequestMethod.POST)
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
			if(jsonObj.has("title"))
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
	 
	@RequestMapping(value = "/findTable")
	@ResponseBody
	public  Map<String,Object>  findTable(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String title= request.getParameter("title");
		try {
			if(title!=null&&!title.trim().equals("")){
				SelectTableList selectTableList=selectTableListService.findByTitle(title);
				
				SelectTableList newSelectTableList= new SelectTableList();
				if(selectTableList!=null){
					newSelectTableList.setId(selectTableList.getId());
					newSelectTableList.setCode(selectTableList.getCode());
					newSelectTableList.setDescription(selectTableList.getDescription());
					newSelectTableList.setSelectTableSql(selectTableList.getSelectTableSql());
					newSelectTableList.setOrderBy(selectTableList.getOrderBy());
					//查询列
					List<SelectList> selectList= this.selectListService.findByTableId(selectTableList.getId());
					if(selectList!=null){
						List<SelectList> newselectList=new ArrayList<SelectList>();
						for (SelectList selectListtemp : selectList) {
							SelectList newselect=new SelectList(selectListtemp.getId(), selectListtemp.getColumnName(), selectListtemp.getAliasesName(), selectListtemp.getDescription(), selectListtemp.getIsDisplay(), selectListtemp.getIdx(),selectListtemp.getExportType());
							newselectList.add(newselect);
						}
						newSelectTableList.setSelectLists(newselectList);
					}
					//查询条件
					List<SelectConditions>  selectConditions=this.selectConditionsService.findByTableId(selectTableList.getId()); 
					if(selectConditions!=null){
						List<SelectConditions> newSelectConditions=new ArrayList<SelectConditions>();
						for (SelectConditions selectConditionstemp : selectConditions) {
							SelectConditions newSelectConditions1=new SelectConditions(selectConditionstemp.getId(), selectConditionstemp.getColumnName(), selectConditionstemp.getDescription(), selectConditionstemp.getType(), selectConditionstemp.getIdx(), selectConditionstemp.getOccupyColumn(), selectConditionstemp.getBlankColumn(), selectConditionstemp.getExtraAttributes(), selectConditionstemp.getTdHtml(), selectConditionstemp.getEmpty(),selectConditionstemp.getOperatorType(),selectConditionstemp.getIsSelectData());
							if(selectConditionstemp.getFormProperty()!=null&&selectConditionstemp.getFormProperty().getId()!=null){
								FormProperty formProperty=(FormProperty)this.formPropertyService.findObjByKey(FormProperty.class, selectConditionstemp.getFormProperty().getId());
								FormProperty formProperty2=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(), formProperty.getElementType(), formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(), formProperty.getEmpty(), formProperty.getValidator(), formProperty.getDataType());
								newSelectConditions1.setFormProperty(formProperty2);
							}
							newSelectConditions.add(newSelectConditions1);
						}
					}
				}
				map.put("results", newSelectTableList); 
			}
		} catch (Exception e) {
			map.put("code", "0"); 
			e.printStackTrace();
		}
		map.put("code", "1"); 
		return map;
	}

	@RequestMapping(value = "/formSelectTableList", produces = "text/html")
	public String formSelectTableList( HttpServletRequest request,Model uiModel){
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("title", request.getParameter("title"));
		return "/basicData/formMini/formSelectTableList";
	}
	
	@RequestMapping(value = "/bysql")
	@ResponseBody	
	public DataTableToPage bysql(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String sql = request.getParameter("sql");
		String where = request.getParameter("where");
		String orderby = request.getParameter("orderby");
		String code = request.getParameter("code");
		
		DataTableToPage result = new DataTableToPage();
		if(null == code || ("").equals(code)){
			result.setCode(0);
			return result;
		}
		
		Map<String, Integer> pageMap = new HashMap<String, Integer>();
		pageMap.put("pageIndex", Integer.parseInt(request.getParameter("pageIndex")));
		pageMap.put("pageSize", Integer.parseInt(request.getParameter("pageSize"))); 
			
		Map<String,String> fieldMap = new HashMap<String, String>();
		SelectTableList obj = selectTableListService.findByTitle(code);
		List<SelectList> list = this.selectListService.findByTableId(obj.getId());
		if(null != list && 0 < list.size())
			for(SelectList o : list)
				fieldMap.put(o.getAliasesName(), o.getAliasesName());					
		
		if(null != where && !("").equals(where))
			sql += where;
			
		if(null != orderby && !("").equals(orderby))
			sql += orderby;
		String json = finderService.executionSql(sql, "query", fieldMap, pageMap);
		try{
			JSONObject o = new JSONObject(json);
			if(o.has("code"))
				result.setCode(Integer.parseInt(o.get("code").toString()));
			else
				result.setCode(0);
			result.setResults(o.getJSONArray("results").toString());
			if(o.has("paged"))
				result.setPaged(o.getJSONObject("paged").toString());
		}catch(JSONException e){
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "/showFormDataById")
	@ResponseBody
	public  DataTableToPage  showFormDataById(HttpServletRequest request, HttpServletResponse response) {
		String fromIdStr= request.getParameter("formId");
		String dataId= request.getParameter("dataId");
		
		DataTableToPage dataTableToPage = new DataTableToPage();
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
					String json = finderService.executionSql(sql, "query", fieldMap, null);					
					
			 
						JSONObject jsonObj = new JSONObject(json);
						if (jsonObj.has("code"))
							dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
						else
							dataTableToPage.setCode(0);
						dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
						if (jsonObj.has("paged"))
							dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
					  
					
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}  
		return dataTableToPage;
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
				String json = finderService.executionSql(sql, "query", fieldMap, null);					
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
}
