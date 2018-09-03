package com.bizduo.zflow.controller.form.formMini;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.form.ViewPage;
import com.bizduo.zflow.domain.form.ViewPageProperty;
import com.bizduo.zflow.domain.tableData.DataTableToPage;
import com.bizduo.zflow.service.customform.IFormPropertyService;
import com.bizduo.zflow.service.formView.IViewPagePropertyService;
import com.bizduo.zflow.service.formView.IViewPageService;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping(value = "/viewPage")
public class ViewPageController {
	@Autowired
	private IViewPageService viewPageService;
	@Autowired
	private IViewPagePropertyService viewPagePropertyService;
	@Autowired
	private IFormPropertyService formPropertyService;
	

	@RequestMapping(value = "/viewPage", produces = "text/html")
	public String formPropertyshow(HttpServletRequest request, Model uiModel){		 
		if(request.getParameter("dataId")!=null){
			uiModel.addAttribute("dataId", request.getParameter("dataId"));
		}
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/basicData/formMini/viewPage"; 
	}
	
	@RequestMapping(value = "/findViewPagePropertyList")
	@ResponseBody
	public  Map<String,Object>  findViewPagePropertyList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String code= request.getParameter("code");
		try {
			if(code!=null&&!code.trim().equals("")){
				//Long fromId=Long.parseLong(fromIdStr);				
				List<ViewPageProperty> newViewPropertyList=new ArrayList<ViewPageProperty>();
				//List<FormProperty> newformPropertyExpandList=new ArrayList<FormProperty>();
				ViewPage viewPage =this.viewPageService.findViewPageByCode(code);
				map.put("occupyColumn", viewPage.getOccupyColumn());	
				
				List<ViewPageProperty> viewPropertyList= viewPagePropertyService.getViewPagePropertyByCode(code);
				//List<FormProperty> formPropertyList=formPropertyService.getFormPropertyListByformId(fromId);
				if(viewPropertyList!=null&&viewPropertyList.size()>0){
					for (ViewPageProperty viewProperty : viewPropertyList) {
						ViewPageProperty newviewProperty=new ViewPageProperty(viewProperty.getId(), viewProperty.getType(), viewProperty.getIdx(), viewProperty.getOccupyColumn(), viewProperty.getBlankColumn(), viewProperty.getColumnName(), viewProperty.getAliasesName(), viewProperty.getDescription(), viewProperty.getDictionaryCode(), viewProperty.getExtraAttributes(), viewProperty.getTdHtml(), viewProperty.getEmpty(),viewProperty.getFieldType());
						if(viewProperty.getFormProperty()!=null){
							FormProperty formProperty=(FormProperty) formPropertyService.findObjByKey(FormProperty.class, viewProperty.getFormProperty().getId());
							FormProperty newformProperty=new FormProperty(formProperty.getId(), formProperty.getFieldId(), formProperty.getFieldType(), formProperty.getFieldName(), formProperty.getFieldLength(), formProperty.getMinLength(), formProperty.getIsDelete(), formProperty.getComment(),   formProperty.getElementType(),  formProperty.getDictionaryCode(), formProperty.getExtraAttributes(), formProperty.getBindJs(), formProperty.getForeignKey(),  formProperty.getEmpty(), formProperty.getValidator(),formProperty.getDataType()); 
							newviewProperty.setFormProperty(newformProperty);	
						}
						newViewPropertyList.add(newviewProperty);
					}
				}  
				map.put("results", newViewPropertyList);		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("code", "1"); 
		return map;
	}
	
	
	@RequestMapping(value = "/showViewDataByCode")
	@ResponseBody
	public  DataTableToPage  showViewDataByCode(HttpServletRequest request, HttpServletResponse response) {
		String code= request.getParameter("code");
		DataTableToPage dataTableToPage = new DataTableToPage();
		try {
			Map<String,String> fieldMap=new HashMap<String, String>(); 
			if(code!=null&&!code.trim().equals("")){ 
				 
				ViewPage viewPage= this.viewPageService.findViewPageByCode(code);	
				if(viewPage==null){
					dataTableToPage.setCode(0);
					dataTableToPage.setErrorMsg("编号不存在");
					return dataTableToPage;
				}
				String sql=viewPage.getViewSql();
				if(viewPage.getViewSql()==null){
					dataTableToPage.setCode(0);
					dataTableToPage.setErrorMsg("sql不存在");
					return dataTableToPage;
				}
				List<ViewPageProperty> viewPropertyList= viewPagePropertyService.getViewPagePropertyByCode(code);
				for (ViewPageProperty viewPageProperty : viewPropertyList) {
					fieldMap.put(viewPageProperty.getAliasesName(), viewPageProperty.getAliasesName());		
				}  
				if(!sql.trim().equals("")){
					String json =this.viewPageService.findViewPageBySql(sql,fieldMap);
					//SelectToJson.executionsql(dataSource, sql, "query", fieldMap, null);					
					
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
		return dataTableToPage;
	}
	
	
	@RequestMapping(value = "/saveViewPage")
	@ResponseBody
	public  Map<String,Object>  saveViewPage(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map=new HashMap<String, Object>();
		ViewPage viewPage=new ViewPage();
		viewPage.setCode("customerAddress_v1");
		viewPage.setOccupyColumn(6);
		String sql="select b.`name` as customerName,b.age as customerAge,b.`code` as customerCode,b.city  as customerCity "+
					",a.address as address,a.addressCode as addressCode,a.colorStyle as addressStyle,c.`code` as proposalCode,c.`name` as proposalName "+
					" FROM deco_customer_address a left join deco_customer b on  a.custId=b.id left join deco_proposal c on c.addressId=a.id";
		viewPage.setViewSql(sql);		
		try {
			viewPage=this.viewPageService.create(viewPage);
			ViewPageProperty viewPageProperty=new ViewPageProperty(viewPage,1, 1, 2, 0, null, "customerName", "客户名称",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"customerName\",\"fieldid\":\"48\"}", true);
			ViewPageProperty viewPageProperty1=new ViewPageProperty(viewPage,1, 2, 2, 0, null, "customerAge", "客户年龄",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"customerAge\",\"fieldid\":\"48\"}", true);
			ViewPageProperty viewPageProperty2=new ViewPageProperty(viewPage,1, 3, 2, 0, null, "customerCode", "客户编号",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"customerCode\",\"fieldid\":\"48\"}", true);
			ViewPageProperty viewPageProperty3=new ViewPageProperty(viewPage,1, 4, 2, 0, null, "customerCity", "客户城市",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"customerCity\",\"fieldid\":\"48\"}", true);
			ViewPageProperty viewPageProperty4=new ViewPageProperty(viewPage,1, 5, 2, 0, null, "address", "地址",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"address\",\"fieldid\":\"48\"}", true);
			ViewPageProperty viewPageProperty5=new ViewPageProperty(viewPage,1, 6, 2, 0, null, "addressCode", "地址编号",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"addressCode\",\"fieldid\":\"48\"}", true);
			ViewPageProperty viewPageProperty6=new ViewPageProperty(viewPage,1, 7, 2, 0, null, "addressStyle", "样式",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"addressStyle\",\"fieldid\":\"48\"}", true);
			ViewPageProperty viewPageProperty7=new ViewPageProperty(viewPage,1, 8, 2, 0, null, "proposalCode", "方案编号",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"proposalCode\",\"fieldid\":\"48\"}", true);
			ViewPageProperty viewPageProperty8=new ViewPageProperty(viewPage,1, 9, 2, 0, null, "proposalName", "方案名称",  "{\"type\":\"text\",\"size\":\"11\",\"id\":\"proposalName\",\"fieldid\":\"48\"}", true);
			this.viewPagePropertyService.create(viewPageProperty);
			this.viewPagePropertyService.create(viewPageProperty1);
			this.viewPagePropertyService.create(viewPageProperty2);
			this.viewPagePropertyService.create(viewPageProperty3);
			this.viewPagePropertyService.create(viewPageProperty4);
			this.viewPagePropertyService.create(viewPageProperty5);
			this.viewPagePropertyService.create(viewPageProperty6);
			this.viewPagePropertyService.create(viewPageProperty7);
			this.viewPagePropertyService.create(viewPageProperty8); 
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 0);		
			return map;
		}
		map.put("code", 1);
		return map;
	}
}
