package com.bizduo.zflow.controller.sys;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.domain.sys.Permission;
import com.bizduo.zflow.service.sys.IOrganizationService;
import com.bizduo.zflow.status.OrganType;
import com.bizduo.zflow.util.TreeNode;

@Controller
@RequestMapping(value = "/organization")
public class OrganizationController {
	@Autowired
	private IOrganizationService organizationService;

	@RequestMapping(value = "/template", produces = "text/html")
	public String template(){
		return "organization/template";
	}
	
	@RequestMapping(value = "/getBySelector")
	@ResponseBody
	public Map<String, Object> getBySelector(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<Organization> value = new ArrayList<Organization>();
			for(Organization o : organizationService.getAll())
				value.add(o.shadowCloneBySelector(o));
			map.put("code", "1");
			map.put("massage", "Success !");
			map.put("results", value);
		}catch(Exception e){
			map.put("code", "0");
			map.put("message", "Error, System fault!");
		}
		return map;
	}
	
	@RequestMapping(value = "/getByType", method = RequestMethod.POST)
	@ResponseBody
	public List<Organization> getByType(@RequestParam(value = "type[]", required = true) Integer[] type){
		List<Organization> value = new ArrayList<Organization>();
		if(1 == type.length){
			OrganType state = OrganType.getSelf(type[0]);
			switch(state){
			case TYY : 
				break;
			case TLY : 
			case TJQ :
				for(Organization o : organizationService.getByType(type[0]))
					value.add(o.shadowCloneBySelector(o));
				break;
			case TSJ : 
			case TGR : 
				break;
			} 
		}else{
			for(Integer i : type)
				for(Organization o : organizationService.getByType(i))
					value.add(o.shadowCloneBySelector(o));
		}
		return value;
	}
	
	@RequestMapping(value = "/getBySelectProxy")
	@ResponseBody
	public List<Organization> getBySelectProxy(){
		List<Organization> value = new ArrayList<Organization>();
		value.add(new Organization(0, "请选择"));
		for(Organization o : organizationService.getAll())
			value.add(o.shadowCloneBySelector(o));
		return value;
	}
	
	@RequestMapping(value = "/orgTreeSetting")
	@ResponseBody
	public List<TreeNode> orgTreeSetting(@RequestParam(value = "acronym", required = false) String acronym){
		List<TreeNode> value = new ArrayList<TreeNode>();
		if(null == acronym){
			Organization org = organizationService.getByNlevel(0);
			value.add(new TreeNode(org.getId(), org.getName(), org.getIsParent(), org.getAcronym(), org.getNlevel()));
		}else{
			Organization org = organizationService.getByAcronym(acronym);
			List<Organization> c = organizationService.getByParentId(org.getId());
			for(Organization obj : c)
				value.add(new TreeNode(obj.getId(), obj.getName(), obj.getIsParent(), obj.getAcronym(), obj.getNlevel()));
		}
		return value;
	}
	
	@RequestMapping(value = "/getByNameOrParentId")
	@ResponseBody
	public List<Organization> getByNameOrParentId(@RequestParam(value = "name", required = false) String name, 
				@RequestParam(value = "parentId", required = false) Integer parentId){
		return organizationService.getByNameOrParentId(name, parentId);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> create(@RequestBody Organization organization) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			this.organizationService.create(organization); 
			map.put("successMsg", null == organization.getId() ? "添加成功!" : "修改成功!");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", null == organization.getId() ? "添加失败,请稍后再试!" : "修改失败,请稍后再试!");
			return map;
		}
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Organization());
		return "organization/create";
	}

	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	@ResponseBody
	public Organization getById(@RequestParam(value = "id", required = true) Integer id){
		try {
			return organizationService.findObjByKey(Organization.class, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		uiModel.addAttribute("permission",
				this.organizationService.findObjByKey(Permission.class, id));
		uiModel.addAttribute("itemId", id);
		return "organization/show";
	}

	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "level", required = false) Integer level,
			@RequestParam(value = "isBack", required = true) boolean isBack,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size
			) throws Exception {
		// 1.用来存放查询参数 (id = parentOrganization.id, level = parentOrganization.level)
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Organization org = null;
		// 2.id代表parentOrganization.id 不为空就pramMap.put
		if (null != id && 0L != id) {
			paramMap.put("id", id);
			// 3.如果请求是返回到上级机构则查询出上级机构的parentOrganization(上级机构的上一级机构)
			if (isBack) {
				org = this.organizationService.findObjByKey(Organization.class, id).getParent();
				if (null != org) {
					org = org.shadowClone(org);
					paramMap.put("id", org.getId());
				}
			} else {
				// 4.如果是查询子机构,则当前机构就是子机构的parent机构
				org = this.organizationService.findObjByKey(Organization.class, id);
			}
		}
		// 5.查询一级机构(level = 1 ,表示一级机构)
		if (null == level || 0 == level) {
			level = 1;
			paramMap.put("level", level);
		} else {
			// 6.查询上级机构
			if (isBack) {
				// 7.如果上级机构就是一级机构,则设置查询条件 parentId = null;(一级机构没有上级机构)
				if (1 == level)
					paramMap.put("id", null);
			} else {
				// 8.查询子机构时,level需要加1 (现在的规则是父机构都比它的直属子机构的level值大1)
				level += 1;
			}
			paramMap.put("level", level);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentorg", org);
		map.put("organization", this.organizationService.findByParam(paramMap));
		return map;
}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Organization organization, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, organization);
			return "organization/update";
		}
		uiModel.asMap().clear();
		// /permission.merge();
		organizationService.update(organization);
		return "redirect:/organization/"
				+ encodeUrlPathSegment(organization.getId().toString(),
						httpServletRequest);
	}
	
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) throws Exception {
		populateEditForm(uiModel, organizationService.findObjByKey(Organization.class, id));
		return "organization/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, String> delete(@PathVariable("id") Integer id, Model uiModel){
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "size", required = false) Integer size,
			
//		Permission permission = (Permission)permissionService.findObjByKey(Permission.class, id);
//		organizationService.delete(Permission.class, id);
//		uiModel.asMap().clear();
//		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		Map<String, String> map = new HashMap<String, String>();
		try{
			Organization org = organizationService.findObjByKey(Organization.class, id);
			Collection<Organization> c = organizationService.getByParentId(org.getId());
			if(null != c && 0 < c.size()){
				map.put("errorMsg", org.getName() + "下有" + c.size() + "个子部门，请调整他们之间的关系后再删除!");
				return map;
			}
			this.organizationService.delete(Organization.class, id);
		}catch(Exception e){
			map.put("errorMsg", "删除失败，请稍候重试!");
			return map;
		}
		map.put("successMsg", "删除成功!");
		return map;
	}

	void populateEditForm(Model uiModel, Organization organization) {
		uiModel.addAttribute("organization", organization);
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}
	///jiaju
	@RequestMapping(value = "/getcompany", method = RequestMethod.POST)
	@ResponseBody
	public List<Organization> getcompany(){
		try {
			List<Organization> organizationList=this.organizationService.getcompany();
			List<Organization> neworganizationList=new ArrayList<Organization>(); 
			for (Organization organization : organizationList) {
				Organization neworganization=new Organization(organization.getId(), organization.getName());
				neworganizationList.add(neworganization);
			}
			return neworganizationList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
	@RequestMapping(value = "/getdepartment", method = RequestMethod.POST)
	@ResponseBody
	public List<Organization> getdepartment(@RequestParam(value = "orgId", required = false) Integer orgId){
		try {
			List<Organization> organizationList= this.organizationService.getdepartment(orgId);
			List<Organization> neworganizationList=new ArrayList<Organization>(); 
			for (Organization organization : organizationList) {
				Organization neworganization=new Organization(organization.getId(), organization.getName());
				neworganizationList.add(neworganization);
			}
			return neworganizationList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
