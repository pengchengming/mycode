package com.bizduo.zflow.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.service.customform.IFormPropertyService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.sys.IOrganizationService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.service.table.IZColumnService;
import com.bizduo.zflow.wrapper.OrgAndUserWrapper;

@Controller
@RequestMapping(value = "/controls")
public class ControlController {
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrganizationService organizationService;
	@Autowired
	private IFormService  formService;
	@Autowired
	private IFormPropertyService formPropertyService;
	@Autowired
	private IZColumnService zcolumnService;
	
	@RequestMapping(value = "orgTreeNode", method = RequestMethod.POST)
	@ResponseBody
	public Collection<OrgAndUserWrapper> orgTreeNode(@RequestParam(value = "createUser", required = false) Boolean createUser
		) throws Exception{
		
		List<OrgAndUserWrapper> ouws = new ArrayList<OrgAndUserWrapper>();		
		List<Organization> orgs = (List<Organization>) this.organizationService.findAll(Organization.class);
		if(null != createUser && createUser){
			for(Organization org : orgs){
				if(null == org.getParent())
					ouws.add(new OrgAndUserWrapper(org.getId().toString(), null, org.getName(), false));
				else
					ouws.add(new OrgAndUserWrapper(org.getId().toString(), org.getParent().getId(), org.getName(), false));
			}
		}else{
			for(Organization org : orgs){
				if(null == org.getParent())
					ouws.add(new OrgAndUserWrapper(org.getId().toString(), null, org.getName(), false));
				else{
					List<Organization> children = organizationService.getByParentId(org.getId());
					if(null == children || 1 > children.size())
						ouws.add(new OrgAndUserWrapper(org.getId().toString(), org.getParent().getId(), org.getName(), true));
					else
						ouws.add(new OrgAndUserWrapper(org.getId().toString(), org.getParent().getId(), org.getName(), false));
					
				}
			}
		}		
		return ouws;
	}
	
	@RequestMapping(value = "usersOnOrgTreeNode", method = RequestMethod.POST)
	@ResponseBody
	public Collection<OrgAndUserWrapper> usersOnOrgTreeNode(
			@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "name", required = false) String name
		){
		List<OrgAndUserWrapper> ouws = new ArrayList<OrgAndUserWrapper>();
		List<User> users = (List<User>) this.userService.findByOrganization(id);
		for(User user : users){
			ouws.add(new OrgAndUserWrapper("user_"+user.getId().toString(), user.getOrganization().getId(), user.getFullname(), false));
		}
		return ouws;
	}
	////////////////////////form////////////////////////////
	@RequestMapping(value = "selectTableAllForm", method = RequestMethod.POST)
	@ResponseBody
	public Collection<OrgAndUserWrapper> selectTableAllForm(){
		List<OrgAndUserWrapper> ouws = new ArrayList<OrgAndUserWrapper>(); 
		List<Form>  formAll= formService.getFormPush(true);
		for(Form form : formAll){
			ouws.add(new OrgAndUserWrapper("form_"+form.getId().toString(), null, form.getFormCode(),form.getFormName(), true));
		}
		return ouws;
	} 
	@RequestMapping(value = "selectFormProperty", method = RequestMethod.POST)
	@ResponseBody
	public Collection<OrgAndUserWrapper> selectFormProperty(
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "name", required = false) String name
		) throws NumberFormatException, Exception{
		List<OrgAndUserWrapper> ouws = new ArrayList<OrgAndUserWrapper>();
		String[] ids= id.split("_");
		if(ids!=null&&ids.length>0){
			 List<FormProperty>  formPropertyList= this.formPropertyService.getFormPropertyListByformId(Long.parseLong(ids[1]));
			 for(FormProperty formProperty : formPropertyList){
				ouws.add(new OrgAndUserWrapper("property_"+formProperty.getId().toString(),Integer.parseInt(ids[1]),formProperty.getFieldName(),formProperty.getComment(), false));
			 }
			 String[] colNames={"createDate","createBy","modifyBy","modifyDate"};
			 Form form=(Form)this.formService.findObjByKey(Form.class,Long.parseLong(ids[1]) );
			 List<ZColumn> columList= zcolumnService.getZColumnListByTableId(form.getZtable().getId(), colNames);
			 for(ZColumn column : columList){
				ouws.add(new OrgAndUserWrapper("column_"+column.getId().toString(),Integer.parseInt(ids[1]) ,column.getColName(),column.getComment(), false));
			 }
		}
		return ouws;
	}
}
