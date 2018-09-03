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

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.base.PageNewTrace;
import com.bizduo.zflow.domain.sys.Employee;
import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.domain.sys.Rank;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.sys.IEmployeeService;
import com.bizduo.zflow.service.sys.IRankService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {
	@Autowired
	private IEmployeeService employeeService;
	/*@Autowired
	private IOrganizationService organizationService;
	*/
	@Autowired
	private IRankService rankService;

	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IUserService userService;
	 
	@RequestMapping(value = "/template", produces = "text/html")
	public String template(Model model){
		model.addAttribute("ranks", rankService.findAll(Rank.class));
		return "employee/template";
	}
	
	@RequestMapping(value = "/precreate", produces = "text/html")
	public String precreate(){
		return "employee/create";
	}
	
	/** 编辑
	 * @throws Exception */
	@RequestMapping(value = "/edit", produces = "text/html")
	public String edit(Integer id,Model model) throws Exception{
		Employee employee = employeeService.findObjByKey(Employee.class, id);
		model.addAttribute("employee", employee);
		return "employee/edit";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> create(@RequestBody Employee employee){
		Map<String, String> map = new HashMap<String, String>();
		try {
			//检查用户名是否和系统中已有数据冲突
			if( employeeService.checkEmployeeIDAndUserName(employee.getId(),employee.getUsername()))
			{
			
			Collection<Role> roles = new ArrayList<Role>();
		 	Role adminrole = roleService.findObjByKey(Role.class, 13);
		 	Role loginrole = roleService.findObjByKey(Role.class, 2);
		 	Employee oldemployee=null;
			if(null != employee.getUserType() && !("").equals(employee.getUserType())){
				if(employee.getId()!=null){
					oldemployee = employeeService.findObjByKey(Employee.class, employee.getId());
					User user = userService.getByName(employee.getUsername());

				if(employee.getRoleType().equalsIgnoreCase("1")){
					user.setProcessrole("admin");
					roles.add(adminrole);
				}else{
					user.setProcessrole("outjh");
					roles.add(loginrole);
//					roles.add(new Role(2L,"有登陆系统权限的角色","ROLE_LOGIN"));
				}
					user.setRoles(roles);	
					userService.update(user);
				}else{
					oldemployee=new Employee();
					
					oldemployee.setUsedDiskSize(0.0);;
					
					User user = new User();
					user.setPassword("3d4f2bf07dc1be38b20cd6e46949a1071f9d0e3d");
					user.setUsername(employee.getUsername());
					user.setEnabled(true);
					 
					if("1"== employee.getRoleType())
					{
						user.setProcessrole("admin");
						roles.add(adminrole);
//						roles.add(new Role(2L,"有登陆系统权限的角色","ROLE_LOGIN"));
					}
					else{
						user.setProcessrole("outjh");
						roles.add(loginrole);
//						roles.add(new Role(2L,"有登陆系统权限的角色","ROLE_LOGIN"));
					}
					user.setRoles(roles);
					userService.create(user);
				}
			}
			oldemployee.setUsername(employee.getUsername());
			oldemployee.setRealname(employee.getRealname());
			oldemployee.setEmail(employee.getEmail());
			oldemployee.setDiskSize(employee.getDiskSize());
			oldemployee.setUserType(employee.getUserType());
			oldemployee.setRoleType(employee.getRoleType());
			
//			if(null != employee.getRank() && null != employee.getRank().getId()){
//				oldemployee.setRank(rankService.findObjByKey(Rank.class, employee.getRank().getId()));
//				if(oldemployee.getDiskSize()==null || oldemployee.getDiskSize()==0)
//					oldemployee.setDiskSize(Long.parseLong(employee.getRank().getRoomSize().toString()));
//				
//			}
//			if(null == employee.getCreatDate() || ("").equals(employee.getCreatDate())){
//				Date date = new Date();
//				employee.setCreatDate(date);
//			}
//			if(null != employee.getLeaveDate() && ("").equals(employee.getLeaveDate()))
//				employee.setIsLeave(true);
			employeeService.create(oldemployee);
			map.put("code", "1");
			map.put("message", null == oldemployee.getId() ? "添加成功!" : "修改成功!");
			}
			else
			{
				map.put("code", "0");
				map.put("message", "用户名与系统中的现有用户名冲突!");	
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
			map.put("message", null == employee.getId() ? "添加失败,请稍后再试!" : "修改失败,请稍后再试!");
			return map;
		}
	}
	
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "orgid", required = false) Long orgid, 
			@RequestParam(value = "realname", required = false) String realname, 
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "rankId", required = false) Integer rankId,
			@RequestParam(value = "userType", required = true) String userType,
			@RequestParam(value = "pageIndex", required = true) Integer pageIndex,
			@RequestParam(value = "recordPerPage", required = true) Integer recordPerPage
			){
		Map<String, Object> results = new HashMap<String, Object>();
		try{
			//int pageIndex=1;//参数 当前页数
			//int recordPerPage= 10;//参数 当前页数
			if(pageIndex==null||pageIndex==0)pageIndex=1;
			if(recordPerPage==null||recordPerPage==0)recordPerPage=10;
					
			PageTrace pageTrace = new PageTrace(recordPerPage);
			pageTrace.setPageIndex(pageIndex);
			List<Employee> employeeList=employeeService.getByParamPage(orgid, realname,username,rankId,userType,pageTrace);
			
			if(employeeList.size()>0){
				PageNewTrace pageNewTrace=new PageNewTrace();
				pageNewTrace.setPageIndex(pageIndex);
				pageNewTrace.setLcsCount(recordPerPage);				 
				pageNewTrace.setCordCnt(pageTrace.getTotal());
				pageNewTrace.setPageCnt(pageTrace.getLastPageIndex());
				
				String pagem = "";
				for(int i=1;i<=pageTrace.getLastPageIndex();i++){
	                pagem = pagem + i + "_";
	            }
				pageNewTrace.setPagem(pagem);
				results.put("paged", pageNewTrace);	
			}
			results.put("results", employeeList);
			results.put("code", "1");
			results.put("message", "Success !");
		}catch(Exception e){
			e.printStackTrace();
			results.put("code", "0");
			results.put("message", "System Fault !");
		}
		return results;
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Organization());
		return "employee/create";
	}

	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	@ResponseBody
	public Employee getById(@RequestParam(value = "id", required = true) Integer id){
		try {
			return employeeService.findObjByKey(Employee.class, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel)
			throws Exception {
//		uiModel.addAttribute("permission",
//				this.organizationService.findObjByKey(Permission.class, id));
		uiModel.addAttribute("itemId", id);
		return "employee/show";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Organization organization, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, organization);
			return "employee/update";
		}
		uiModel.asMap().clear();
		// /permission.merge();
//		organizationService.update(organization);
		return "redirect:/employee/"
				+ encodeUrlPathSegment(organization.getId().toString(),
						httpServletRequest);
	}
	
//	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
//	public String updateForm(@PathVariable("id") Long id, Model uiModel) throws Exception {
//		populateEditForm(uiModel, organizationService.findObjByKey(Organization.class, id));
//		return "employee/update";
//	}

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//	@ResponseBody
//	public Map<String, String> delete(@PathVariable("id") Long id, Model uiModel){
////			@RequestParam(value = "page", required = false) Integer page,
////			@RequestParam(value = "size", required = false) Integer size,
//			
////		Permission permission = (Permission)permissionService.findObjByKey(Permission.class, id);
////		organizationService.delete(Permission.class, id);
////		uiModel.asMap().clear();
////		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
////		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//		Map<String, String> map = new HashMap<String, String>();
//		try{
//			Organization org = organizationService.findObjByKey(Organization.class, id);
//			Collection<Organization> c = org.getOrganizations();
//			if(null != c && 0 < c.size()){
//				map.put("errorMsg", org.getName() + "下有" + c.size() + "个子部门，请调整他们之间的关系后再删除!");
//				return map;
//			}
//			this.organizationService.delete(Organization.class, id);
//		}catch(Exception e){
//			map.put("errorMsg", "删除失败，请稍候重试!");
//			return map;
//		}
//		map.put("successMsg", "删除成功!");
//		return map;
//	}

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
	
	
	
}
