package com.bizduo.zflow.controller.form.formMini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.service.customform.IFormPropertyService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.util.UserUtil;
 
@Controller
@RequestMapping(value = "/formExpand")
public class FormExpandController {
	@Autowired
	private IFormService formService;
	@Autowired
	private IFormPropertyService formPropertyService;
	
	@RequestMapping(value = "/formExpandList", produces = "text/html")
	public String template( Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/basicData/formMini/formExpandList";
	}
	
	@RequestMapping(value = "/findFormList")
	@ResponseBody
	public Map<String,Object> findAllForm(HttpServletRequest request, HttpServletResponse response) {

		Integer pageIndex=null;
		if(request.getParameter("pageIndex")!=null){
			pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
		} 
		Integer pageSize=null;
		if(request.getParameter("pageSize")!=null){
			pageSize=Integer.parseInt(request.getParameter("pageSize"));
		}
		PageTrace pageTrace = new PageTrace(pageSize);
		pageTrace.setPageIndex(pageIndex); 
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pamMap=new HashMap<String, Object>(); 
		//pamMap.put("isPublish", true);
		pamMap.put("isDelete", 0);
		pamMap.put("formName", request.getParameter("formName"));
		
		List<Form> formList = formService.getAllForm();// getFormList(pamMap,pageTrace);
		if(formList!=null&&formList.size()>0){
			for(Form form : formList){
				if(null != form.getZtable()) form.setZtable(null);
				if(null != form.getPropertyList()) form.getPropertyList().clear();
			}
			//分页
			/*PageNewTrace pageNewTrace=new PageNewTrace();
			pageNewTrace.setPageIndex(pageIndex);
			pageNewTrace.setLcsCount(pageSize);				 
			pageNewTrace.setCordCnt(pageTrace.getTotal());
			pageNewTrace.setPageCnt(pageTrace.getLastPageIndex());
			
			String pagem = "";
			for(int i=1;i<=pageTrace.getLastPageIndex();i++){
                pagem = pagem + i + "_";
            }
			pageNewTrace.setPagem(pagem);
			map.put("paged", pageNewTrace);*/
		}
		map.put("code", "1");
		map.put("results", formList);
		return map;
	}
	
	@RequestMapping(value = "/saveForm")
	@ResponseBody
	public  Map<String,Object>  saveForm(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();		
		try{
			String fromIdStr= request.getParameter("formId");
			String formName= request.getParameter("formName");
			String formCode= request.getParameter("formCode");			
			String isBaseFrom= request.getParameter("isBaseFrom");
			String baseFormId= request.getParameter("baseFormId");
			String isHistoryTable= request.getParameter("isHistoryTable");
			
			Form formold =this.formService.findByFormName(formName);
			if(formold!=null){
				map.put("code", "0");
				map.put("errorMsg", "提示：\n   保存失败!\n"+formName+"已经存在");	
				return map;
			}
			Form form=new Form();
			if(fromIdStr!=null&&!fromIdStr.trim().equals("")){
				Long fromId=Long.parseLong(fromIdStr);
				form=(Form) this.formService.findObjByKey(Form.class, fromId);
			} 
			form.setFormCode(formCode);
			form.setFormName(formName);
			form.setIshistoryTable(Integer.parseInt(isHistoryTable));
			form.setIsDelete(0);
			form.setIsBaseFrom(Integer.parseInt(isBaseFrom));
			if(baseFormId!=null&&!baseFormId.equals("")){
				Form baseForm=new Form();
				form.setBaseFormId(Long.parseLong(baseFormId));
				baseForm=(Form)this.formService.findObjByKey(Form.class, Long.parseLong(baseFormId));
				baseForm.setIsBaseFrom(Integer.parseInt(isBaseFrom)); 
				this.formService.create(baseForm);
				form.setIsBaseFrom(0);
			}
			this.formService.create(form);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
			map.put("errorMsg", "提示：\n   保存失败!");	
			return map;
		}
		map.put("code", "1");
		map.put("successMsg", "提示：\n   保存成功!");	
		return map;
	}
	
	@RequestMapping(value = "/findFormPropertyList")
	@ResponseBody
	public  Map<String,Object>  findFormPropertyList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String fromIdStr= request.getParameter("id");
		try {
			if(fromIdStr!=null&&!fromIdStr.trim().equals("")){
				Long fromId=Long.parseLong(fromIdStr);
				
				List<FormProperty> newformPropertyList=new ArrayList<FormProperty>();
				List<FormProperty> newformPropertyExpandList=new ArrayList<FormProperty>();
				
				List<FormProperty> formPropertyList=formPropertyService.getFormPropertyListByformId(fromId);
				if(formPropertyList!=null&&formPropertyList.size()>0){
					for (FormProperty formProperty : formPropertyList) {
						FormProperty newformProperty=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(),   formProperty.getElementType(),   formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(),   formProperty.getEmpty(), formProperty.getValidator(), formProperty.getDataType()); 
						newformPropertyList.add(newformProperty);
					}
				}  
				map.put("results", newformPropertyList);
				 List<Form>  formList=this.formService.findSubsetForm(fromId);
				if(formList!=null&&formList.size()>0){
					for (Form form2 : formList) {
						List<FormProperty> formPropertyExpandList=formPropertyService.getFormPropertyListByformId(form2.getId());
						if(formPropertyExpandList!=null&&formPropertyExpandList.size()>0){
							for (FormProperty formProperty : formPropertyExpandList) {
								FormProperty newformProperty=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(), formProperty.getElementType(),   formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(),  formProperty.getEmpty(), formProperty.getValidator(),formProperty.getDataType()); 
								newformPropertyExpandList.add(newformProperty);
							}
						}
					}
					
					map.put("resultExpands", newformPropertyExpandList);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("code", "1"); 
		return map;
	}
	
	@RequestMapping(value = "/formPropertyModify", produces = "text/html")
	public String formPropertyModify( HttpServletRequest request,Model uiModel){
		if(request.getParameter("id")!=null){
			uiModel.addAttribute("formId", request.getParameter("id"));
		} 
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/basicData/formMini/formPropertyModify";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/saveFormProperty")
	@ResponseBody
	public  Map<String,Object>  saveFormProperty(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try{
			String fromIdStr= request.getParameter("formId");
			String module= request.getParameter("module");
			String dictionaryType= request.getParameter("database_dactionary");
			String dictionaryCode= request.getParameter("database_children");
			 
			String propertyIdStr= request.getParameter("propertyId");
			String fieldType= request.getParameter("fieldType");
			String comment= request.getParameter("comment");
			String fieldName= request.getParameter("fieldName");
			String fieldLength= request.getParameter("fieldLength");
			String fieldId= request.getParameter("fieldId"); 
			String tdHtml= request.getParameter("tdHtml");
			
			if(fromIdStr!=null&&!fromIdStr.trim().equals("")){
				FormProperty formProperty=new FormProperty();
				if(propertyIdStr!=null&&!propertyIdStr.trim().equals("")){
					Long propertyId=Long.parseLong(propertyIdStr);	
					formProperty=(FormProperty)this.formPropertyService.findObjByKey(FormProperty.class, propertyId);
				}else {
						Long fromId=Long.parseLong(fromIdStr);
						Form form=(Form) this.formService.findObjByKey(Form.class, fromId);
						formProperty.setForm(form);
				}
				String extraAttributes="";
				String elementType="";
				if(module!=null){
					if(module.trim().equals("1")){
						elementType="input";
						extraAttributes="{\"type\":\"text\",\"size\":\"30\",\"id\":\""+formProperty.getForm().getFormName()+"_"+fieldName+"\",\"fieldid\":\"1\"}";
					} else  if(module.trim().equals("2")){
						elementType="select";
						fieldType="attrString"; 
						formProperty.setDictionaryCode(dictionaryCode);		
						extraAttributes="{\"id\":\""+formProperty.getForm().getFormName()+"_"+fieldName+"\",\"fieldid\":\"1\",\"type\":\"select\"}";
					}else if(module.trim().equals("3")){
						elementType="span";
						fieldType="attrString"; 
						formProperty.setDictionaryCode(dictionaryCode);		
						extraAttributes="{\"id\":\""+formProperty.getForm().getFormName()+"_"+fieldName+"\",\"fieldid\":\"2\",\"type\":\"radio\",\"style\":\"float:left\"}";
					}else if(module.trim().equals("4")){
						elementType="span";
						fieldType="attrString"; 
						formProperty.setDictionaryCode(dictionaryCode);		
						extraAttributes="{\"id\":\""+formProperty.getForm().getFormName()+"_"+fieldName+"\",\"fieldid\":\"4\",\"type\":\"checkbox\",\"style\":\"float:left\"}";
					}
				}	
				formProperty.setMinLength(0);
				formProperty.setIsDelete(0);
				formProperty.setIsDelete(0);
				formProperty.setElementType(elementType);
				formProperty.setExtraAttributes(extraAttributes);
				formProperty.setEmpty(true);
				formProperty.setValidator("0"); 
				
				formProperty.setFieldType(fieldType);
				formProperty.setComment(comment);
				formProperty.setFieldName(fieldName);
				formProperty.setFieldLength(Integer.parseInt(fieldLength));
				formProperty.setFieldId(Long.parseLong(fieldId));   
				this.formPropertyService.create(formProperty); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
			map.put("errorMsg", "提示：\n   保存失败!");	
			return map;
		}
		map.put("code", "1");
		map.put("successMsg", "提示：\n   保存成功!");	
		return map;
	}
	
	
	//发布表单
	@RequestMapping(value = "/createTable")
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
	
	///保存数据
	@RequestMapping(value = "/saveFormData")
	@ResponseBody
	public Map<String, Object> saveFormData(
			@RequestParam(value = "jsonString", required = true) String jsonString){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			Long formId = jsonObj.getLong("formId");
			if(null == formId || 0L == formId){
				map.put("errorMsg", "表单标识未传入");
				return map;
			}else{
				this.formService.saveFormData(formId, jsonObj);
				
				
			}
				
		} catch (Exception e) {
			map.put("errorMsg", "保存错误");
			return map;
		}
		map.put("successMsg", "保存成功");
		return map;
	}
}
