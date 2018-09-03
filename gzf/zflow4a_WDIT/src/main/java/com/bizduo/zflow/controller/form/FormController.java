package com.bizduo.zflow.controller.form;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.form.FormRegister;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.domain.tabulation.MiddleTable;
import com.bizduo.zflow.domain.tabulation.TableProperty;
import com.bizduo.zflow.json.PropertyJson;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.IMiddleTableService;
import com.bizduo.zflow.service.table.IConditionSelectService;
import com.bizduo.zflow.service.table.IZTableService;
import com.bizduo.zflow.status.ZFlowStatus;
import com.bizduo.zflow.util.JsonToObjectUtil;

/**
* @author lm
* @version 创建时间：2012-4-12 上午09:59:28
*/
@Controller
@RequestMapping("/forms")
public class FormController {
	@Autowired
	private IFormService formService;
//	@Autowired
//	private IFormPropertyService formPropertyService;
	@Autowired
	private IZTableService zTableService;
	@Autowired
    private IMiddleTableService middleTableService;
	@Autowired
	private IConditionSelectService conditionSelectService;
	
	/**
	 * 保存表单
	 * @return
	 * 2012-4-9
	 * @author lm
	 * @throws Exception 
	 */
	@RequestMapping(value = "createOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createOrUpdate(@RequestBody Form form) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		form.setIsDelete(ZFlowStatus.ISDELETE_NO);
		Form f =new Form();
		if(null == form.getId()){
			map.put("status", false);
			f = (Form) this.formService.create(form);
		}
		else{
			map.put("status", true);
			Form  form1=(Form) this.formService.findObjByKey(Form.class, form.getId());
			form1.setFormCode(form.getFormCode());
			form1.setFormName(form.getFormName());
			f = (Form) this.formService.update(form1);
		}
		map.put("id", f.getId());
		map.put("formName", f.getFormName());
		map.put("formCode", f.getFormCode());
		return map;
	}
	
	@RequestMapping(value = "selectTableAllForm", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectTableAllForm() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		 
//		map.put("id", f.getId());
//		map.put("formName", f.getFormName());
//		map.put("formCode", f.getFormCode());
		return map;
	}
	/**
	 * 保存，更新，删除表单
	 * @return
	 * 2012-4-12
	 * @author lm
	 * @throws Exception 
	 */
	@RequestMapping(value = "/saveFormProperty.do", method = RequestMethod.POST)
	@ResponseBody
	public  Map<String,Object> saveFormProperty(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "prototype", required = false) String prototype){
		Map<String, Object> map = new HashMap<String, Object>();
		 
		if(null == prototype ||("").equals(prototype.trim())){
			map.put("errorMsg", "参数错误");
			return map;
		}
		try {
			JSONObject jsonObj = new JSONObject(prototype);
			JSONObject tempJsonObj = jsonObj.getJSONObject("attrData");
			//解析json，得到formId
			Long formId = tempJsonObj.getLong("formId");
			//判断formId
			if(null == formId || 0L == formId){
				map.put("errorMsg", "表单标识未传入");
				return map;
			}else
				this.formService.saveFormProperty(tempJsonObj, formId,request);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "保存错误");
			return map;
		}
		map.put("successMsg", "保存成功");
		return map;
	}
	/**
	 * 获得所有Form
	 * @return
	 * 2012-4-9
	 * @author lm
	 */
	@RequestMapping(value = "/findAllForm.do")
	@ResponseBody
	public Map<String,Object> findAllForm() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Form> formList = formService.getAllForm();
		for(Form form : formList){
			if(null != form.getZtable()) form.setZtable(null);
			if(null != form.getPropertyList()) form.getPropertyList().clear();
		}
		map.put("formList", formList);
		return map;
	}
	
	/**
	 * 获得所有Form
	 * @return
	 * 2012-4-9
	 * @author lm
	 * @throws Exception 
	 */
	@RequestMapping(value = "/showFormbyId.do")
	@ResponseBody
	public Map<String,Object> showFormbyId(@RequestParam(value = "formId", required = true) Long formId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if(formId!=null) {
			Form form =(Form)formService.findObjByKey(Form.class, formId);
			map.put("formCode", form.getFormCode());
			map.put("formName", form.getFormName());
		}
		return map;
	}
	
	/**
	 * 表单显示  :返回json
	 * @return
	 */
	@RequestMapping(value = "/formConfigFields.do")
	@ResponseBody
	public void formConfigFields(HttpServletResponse response,
			@RequestParam(value = "formId", required = true) Long formId){
		String formjson="";
		try {
			Form form =(Form)this.formService.findObjByKey(Form.class, formId);
			if(form!=null)
				formjson = this.formService.formPropertyAndTableJson(form);
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(formjson);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 表单显示  :返回json
	 * @return
	 */
	@RequestMapping(value = "/formConfigFieldsByCode.do")
	@ResponseBody
	public void formConfigFieldsByCode(HttpServletResponse response,
			@RequestParam(value = "code", required = true) String code){
		String formjson="";
		try {
			Form form =(Form)this.formService.getFormByCode(code);
			if(form!=null)
				formjson = this.formService.formPropertyAndTableJson(form);
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(formjson);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 删除表单
	 * @return
	 * 2012-4-9
	 * @author lm
	 */
	@RequestMapping(value = "/deleteForm.do")
	@ResponseBody
	public Map<String, Object> deleteForm(@RequestParam(value = "formId", required = true) Long formId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(formId!=null) {
			formService.deleteForm(formId);
			map.put("successMsg", "删除成功");
			return map;
		}else {
			map.put("errorMsg", "删除错误");
			return map;
		}
	}
	//点击发布创建表
	@RequestMapping(value = "/createTable.do")
	@ResponseBody
	public Map<String, Object> createTable(@RequestParam(value = "formId", required = true) Long formId){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Form form = (Form)formService.findObjByKey(Form.class, formId);
			if(form !=null){
				//if(!form.getIsPublish())
					this.formService.updateTabelConfig(form);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "发布失败");
			return map;
		}
		map.put("successMsg", "发布成功");
		return map;
	}
	/**
	 * 表单向导
	 * @throws Exception 
	 */
	@RequestMapping(value = "/formZCloumnGuide.do")
	@ResponseBody
	public Map<String ,List<PropertyJson>>  formZCloumnGuide(
			@RequestParam(value = "formId", required = true) Long formId
		) throws Exception{
		 Map<String ,List<PropertyJson>>  mapJson=new HashMap<String, List<PropertyJson>>();
		 
		List<PropertyJson> zcolumnlist=new ArrayList<PropertyJson>();
		//返回json
		Form form =(Form)formService.findObjByKey(Form.class, formId);
		ZTable ztable= (ZTable)zTableService.findObjByKey(ZTable.class, form.getZtable().getId());
		//form 的属性
		if(ztable!=null){
			for(ZColumn zcolumn: ztable.getZcolumns()){
				if (zcolumn.getColName().trim().equals("id")) {
					continue;
				}
				PropertyJson propertyRegister=new PropertyJson(zcolumn.getId(),zcolumn.getColName(),zcolumn.getComment());
				zcolumnlist.add(propertyRegister);
			}
		}
		//middleTable的属性
		Collection<MiddleTable> middleTableList=this.middleTableService.findByFormId(formId) ;
		List<PropertyJson> middleTablelist=new ArrayList<PropertyJson>();
		if(middleTableList!=null){
			for (MiddleTable middleTable : middleTableList) {
				List<PropertyJson> tableColumnlist=new ArrayList<PropertyJson>();
				for (TableProperty tableProperty : middleTable.getTablePropertyList()) {
					PropertyJson propertyRegister=new PropertyJson(tableProperty.getId(),tableProperty.getTablePropertyName(),tableProperty.getTablePropertyLabel());
					tableColumnlist.add(propertyRegister);
				}
				PropertyJson midPropertyJson=new PropertyJson(middleTable.getMiddleTableName(),middleTable.getId(), tableColumnlist);
				middleTablelist.add(midPropertyJson);
			}
		}
		//{zcolumnlist:[{"id":1000,fileName:"",comment:""},{"id":1000,fileName:"",comment:""}],middlecolumnlist:[id:1000,middlename:"",midPropertyJson:[{},{}]]}
		mapJson.put("zcolumnlist", zcolumnlist);
		mapJson.put("middleTablelist", middleTablelist);
		return mapJson;
	}
	
	/**
	 * 保存查询的查询字段和列表字段
	 */
	@RequestMapping(value = "/saveSelectField.do")
	@ResponseBody
	public Map<String ,Object> saveSelectField(
			@RequestParam(value = "formName", required = false) String formName,
			@RequestParam(value = "selectFields", required = false) String selectFields,
			@RequestParam(value = "columnFields", required = false) String columnFields,
			@RequestParam(value="actions" ,required=false) String actions
		){
		//返回json
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.conditionSelectService.saveConDition(formName, selectFields, columnFields,actions);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "保存错误");
			return map;
		}
		map.put("successMsg", "保存成功");
		return map;
	}
	//获取form中的数据
	@RequestMapping(value = "/formAllData.do")
	@ResponseBody
	public Map<String ,Object> formAllData(){
		//返回json
		Map<String, Object> map = new HashMap<String, Object>();
		List<Form> formList =this.formService.getAllForm();
		List<FormRegister> formRegisterList=new ArrayList<FormRegister>();
		for(Form form :formList){
			FormRegister formRegister=new FormRegister();
			formRegister.setId(form.getId());
			formRegister.setFormName(form.getFormName());
			formRegister.setFormCode(form.getFormCode());
			formRegister.setIsDelete(form.getIsDelete());
			formRegisterList.add(formRegister);
		}
		map.put("formList", formRegisterList);
		return map;
	}
	/**
	 * 获得一个指定的form
	 * @return
	 * 2012-4-9
	 * @author lm
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/findFormById.do")
	@ResponseBody
	public Map<String, Object> findFormById(
			@RequestParam(value = "formId", required = true) Long formId
			) throws Exception {
		//返回json
		Map<String, Object> map = new HashMap<String, Object>();
		if(formId!=null) {
			Form form =(Form)formService.findObjByKey(Form.class, formId);
			//属性字段
			JSONArray selectFieldListJson= JsonToObjectUtil.toJsonArray(form.getPropertyList());
			List propertyList= JsonToObjectUtil.toList(selectFieldListJson, PropertyJson.class);
			form.setPropertyList(new ArrayList<FormProperty>());
			List<MiddleTable>  tableList=(List<MiddleTable>) this.middleTableService.findByFormId(formId);
			map.put("status", false);
			map.put("id",form.getId());
			map.put("formName", form.getFormName());
			map.put("formCode", form.getFormCode());
			map.put("formHtml", form.getFormHtml());
			map.put("propertyList", propertyList);
			map.put("tableList", tableList);
			return map;
		}else{
			map.put("errorMsg", "错误");
			return map;
		}
	}
	
	//修改数据
	@RequestMapping(value = "/updateFormData.do")
	@ResponseBody
	public Map<String, Object> updateFormData( @RequestParam(value = "jsonString", required = true) String jsonString){
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObj = new JSONObject(jsonString);  
			if(!jsonObj.has("formId")){
				map.put("code", 0);
				map.put("errorMsg", "表单标识未传入");
				return map;
			}
			Long formId = jsonObj.getLong("formId");
			if(null == formId || 0L == formId){
				map.put("code", 0);
				map.put("errorMsg", "表单标识未传入");
				return map;
			}else{
				if(!jsonObj.has("tableDataId")){
					map.put("code", 0);
					map.put("errorMsg", "修改标识未传入");
					return map;
				}
				Long tableDataId= jsonObj.getLong("tableDataId");;
				if(null == tableDataId|| 0L == tableDataId){
					map.put("code", 0);
					map.put("errorMsg", "修改标识未传入");
					return map;
				}
				this.formService.updateFormData(formId, jsonObj);
			}
				
		} catch (Exception e) {
			map.put("code", 0);
			map.put("errorMsg", "保存错误");
			return map;
		}
		map.put("code", 1);
		map.put("successMsg", "保存成功");
		return map;
	}
	
	///保存数据
	@RequestMapping(value = "/saveFormData.do")
	@ResponseBody
	public Map<String, Object> saveFormData(
			@RequestParam(value = "jsonString", required = true) String jsonString){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			Long formId = jsonObj.getLong("formId");
			if(null == formId || 0L == formId){
				map.put("code", 0);
				map.put("errorMsg", "表单标识未传入");
				return map;
			}else{
				Long id= this.formService.saveFormData(formId, jsonObj);
				map.put("id", id);
			}
				
		} catch (Exception e) {
			map.put("code", 0);
			map.put("errorMsg", "保存错误");
			return map;
		}
		map.put("code", 1);
		map.put("successMsg", "保存成功");
		return map;
	} 
	
	@RequestMapping(value = "/saveTestDataJson", method = RequestMethod.GET)
	@ResponseBody  
	public Map<String, Object> saveTestDataJson(
			@RequestParam(value = "param", required = true) String param, HttpServletRequest request){
			Map<String, Object> map = new HashMap<String, Object>();
			try{
				formService.saveTestDataJson(param);
				map.put("code", "1");
				map.put("successMsg",  "提示：\n    保存成功!");
			}catch(Exception e){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
			}
			return map;
	}
	
	@RequestMapping(value = "/saveFormDataJson", method = RequestMethod.POST)
	@ResponseBody   	
	public Map<String, Object> saveFormDataJson( @RequestParam(value="addressId",required = false)String addressId,
			                                     @RequestParam(value = "vOPCode", required = false) String vOPCode,
			                                     @RequestParam(value="dOPCode",required=false)String dOPCode,
			                                     @RequestParam(value="tOPCode",required=false)String tOPCode,
			                                     @RequestParam(value = "vCreateBy", required = false) String vCreateBy,
			@RequestParam(value = "json", required = true) String json, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			Long id= formService.saveFormDataJson(json);
			
			if(id==null||id.longValue()==0l){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
				return map;
			}
			
			
			map.put("id", id);
			map.put("code", "1");
			map.put("successMsg",  "提示：\n    保存成功!");
		}catch(Exception e){
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveFormDataJsons", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveFormDataJsons(@RequestParam(value = "jsons[]", required = true) String[] jsons, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try{ 
			JSONObject returnObj= formService.saveFormDataJson(jsons);
			Boolean  isCorrect =returnObj.getBoolean("isCorrect");
			if(!isCorrect){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
				return map;
			}
			if(returnObj.length()>0){ 
					Iterator<String> sIterator = returnObj.keys();  
					while(sIterator.hasNext()){   
					    String key = sIterator.next();   
					    if(!key.equals("isCorrect")){
					    	Long value = returnObj.getLong(key);  
						    map.put(key, value);
					    }
					    	
					}
			}
			map.put("code", "1");
			map.put("successMsg",  "提示：\n    保存成功!");
		}catch(Exception e){
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
		}
		return map;
	}
	/**
	 * @param json { "formId":187, "dataIds":22 };
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delteFormDataJson", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delteFormDataJson(@RequestParam(value = "json", required = true) String json, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			formService.deleteFormDataJson(json);			
			map.put("code", "1");
			map.put("successMsg",  "提示：\n    保存成功!");
		}catch(Exception e){
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
		}
		return map;
	}
	
	/**
	 * 
	 * @param jsons  [{formId:,dataIds:'1000,10001'},{}]
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteFormDataJsons", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteFormDataJsons(@RequestParam(value = "jsons[]", required = true) String[] jsons, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			JSONObject returnObj = formService.deleteFormDataJsons(jsons);
			Boolean  isCorrect =returnObj.getBoolean("isCorrect");
			if(!isCorrect){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
				return map;
			}
			map.put("code", "1");
			map.put("successMsg",  "提示：\n    保存成功!");
		}catch(Exception e){
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
		}
		return map;
	}
	
	@RequestMapping(value = "/saveformHtml.do")
	@ResponseBody
	public Map<String, Object> saveformHtml(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "formHtml", required = true) String formHtml,
			@RequestParam(value = "formName", required = true) String formName){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(formHtml!=null&&!formHtml.trim().equals("")){
				String contextPath= request.getSession().getServletContext().getRealPath("");//request.getRealPath("/");//request.getContextPath();
				System.out.println(contextPath);
				String fileName="/"+formName+".jsp";
				saveFile(contextPath+"/saveFormHtml/",fileName,formHtml);
			}
		} catch (Exception e) {
			map.put("errorMsg", "保存错误");
			return map;
		}
		map.put("successMsg", "保存成功");
		return map;
	}
	
	private static void saveFile(String path,String fileName,String center){
		try {
			File srcFolder=new File(path);
			if(!srcFolder.exists()) srcFolder.mkdirs();
			
			File controlFile = new File(path+fileName);
			if (!controlFile.exists()) {
				controlFile.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(controlFile, "rw");
			FileWriter fw = new FileWriter(controlFile);
			fw.write(center);
				fw.flush();
			fw.close();
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@RequestMapping(value = "/getDataByFormId", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody   	
	public String getDataByFormId( @RequestParam(value="formCode",required = true)String formCode,
			                                     @RequestParam(value = "dataId", required = false) Integer dataId,
			                                     @RequestParam(value = "orderBy", required = false) String orderBy,
			                                     @RequestParam(value = "uuId", required = false) String uuId,
			                                     @RequestParam(value = "modifyDate", required = false) String modifyDate,
			                                     @RequestParam(value = "condition", required = false) String condition,
			                                     @RequestParam(value = "byIdconfig", required = false) String byIdconfig,
			                                     HttpServletRequest request, HttpServletResponse response){
		
		try{
			Map<String,String> map =new HashMap<String, String>();
			if(StringUtils.isNotBlank(formCode))
				map.put("formCode", formCode.trim());
			else {
				JSONObject json=new JSONObject();
				json.put("code", 0);
				json.put("errorMsg", "formCode 不存在");
				return json.toString();
			}
			if(dataId!=null)
				map.put("dataId", dataId.toString().trim());
			if(StringUtils.isNotBlank(orderBy))
				map.put("orderBy", orderBy.toString());
			if(StringUtils.isNotBlank(uuId))
				map.put("uuId", uuId.toString().trim());
			if(StringUtils.isNotBlank(modifyDate))
				map.put("modifyDate", modifyDate.toString().trim());
			PageTrace pageTrace = null;
			if(request.getParameter("pageIndex")!=null){
				Integer pageIndex= Integer.parseInt(request.getParameter("pageIndex"));
				Integer pageSize= Integer.parseInt(request.getParameter("pageSize"));
				pageTrace = new PageTrace(pageSize);
				pageTrace.setPageIndex(pageIndex);
			}
			JSONObject json= this.formService. getDataByFormId(map, condition, pageTrace,byIdconfig,1);
			 return json.toString();
		}catch(Exception e){
			System.out.println(e);
			JSONObject json=new JSONObject();
			json.put("code", 0);
			json.put("errorMsg", "system buys");
			return json.toString();			
		}
	} 
}
