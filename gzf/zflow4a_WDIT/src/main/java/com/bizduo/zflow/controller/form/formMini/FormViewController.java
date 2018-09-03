package com.bizduo.zflow.controller.form.formMini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.form.FormView;
import com.bizduo.zflow.domain.form.FormViewProperty;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.customform.IFormPropertyService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.formView.IFormViewPropertyService;
import com.bizduo.zflow.service.formView.IFormViewService;
import com.bizduo.zflow.util.UserUtil;
import com.bizduo.zflow.util.ccm.UploadFileStatus;
import com.bizduo.zflow.util.deco.InitZflowUtil;

@Controller
@RequestMapping(value = "/formView")
public class FormViewController {
	@Autowired
	private IFormService formService;
	@Autowired
	private IFormPropertyService formPropertyService;
	@Autowired
	private IFormViewService formViewService;	
	@Autowired
	private IFormViewPropertyService formViewPropertyService;	
	@Autowired
	private IBizTypeService  bizTypeService;
	
	//展示表单页面
	@RequestMapping(value = "/formPropertyView", produces = "text/html")
	public String formPropertyshow(HttpServletRequest request, Model uiModel){
		if(request.getParameter("id")!=null){
			uiModel.addAttribute("formId", request.getParameter("id"));
		}else if(request.getParameter("formCode")!=null){
			Form from= this.formService.findByFormName(request.getParameter("formCode"));
			if(from!=null)uiModel.addAttribute("formId", from.getId());
		}
		if(request.getParameter("dataId")!=null){
			uiModel.addAttribute("dataId", request.getParameter("dataId"));
		}
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/basicData/formMini/formPropertyView"; 
	}
	
	
	@RequestMapping(value = "/findFormPropertyList")
	@ResponseBody
	public  Map<String,Object>  findFormPropertyList(HttpServletRequest request, HttpServletResponse response) {
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
				if(ischeck){
					Map<String,FormView> staticFormViewMap= InitZflowUtil.staticFormViewMap;
					if(staticFormViewMap!=null&& staticFormViewMap.get(code)!=null){
						FormView formView= staticFormViewMap.get(code);
						map.put("occupyColumn", formView.getOccupyColumn());
						map.put("results", formView.getPropertyList());
					}	
				}else { 
					//Long fromId=Long.parseLong(fromIdStr);				
					List<FormViewProperty> newformViewPropertyList=new ArrayList<FormViewProperty>();
					//List<FormProperty> newformPropertyExpandList=new ArrayList<FormProperty>();
					FormView formView =this.formViewService.findFormViewByCode(code);
					map.put("occupyColumn", formView.getOccupyColumn());	
					
					List<FormViewProperty> formViewPropertyList=  formViewPropertyService.getFormViewPropertyByCode(code);
					//List<FormProperty> formPropertyList=formPropertyService.getFormPropertyListByformId(fromId);
					if(formViewPropertyList!=null&&formViewPropertyList.size()>0){
						for (FormViewProperty formViewProperty : formViewPropertyList) {
							FormViewProperty newformViewProperty=new FormViewProperty(formViewProperty.getId(), formViewProperty.getType(), formViewProperty.getIdx(), formViewProperty.getOccupyColumn(), formViewProperty.getBlankColumn(),formViewProperty.getEmpty(),formViewProperty.getTdHtml(),formViewProperty.getExtraAttributes());
							if(formViewProperty.getFormProperty()!=null){
								FormProperty formProperty=(FormProperty) formPropertyService.findObjByKey(FormProperty.class, formViewProperty.getFormProperty().getId());
								FormProperty newformProperty=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(),   formProperty.getElementType(),  formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(),  formProperty.getEmpty(), formProperty.getValidator(),formProperty.getDataType()); 
								newformViewProperty.setFormProperty(newformProperty);	
							}
							newformViewPropertyList.add(newformViewProperty);
						}
					}  
					map.put("results", newformViewPropertyList);	
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("code", "1"); 
		return map;
	}
	 
}
