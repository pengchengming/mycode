package com.bizduo.zflow.controller.sys;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.wrapper.RoleWrapper;

@Controller
@RequestMapping(value = "/role")
public class RoleController {
	@Autowired
	private IRoleService roleService;

	@RequestMapping(value = "template", produces = "text/html")
	public String template(){
		return "role/template";
	}
	
	@RequestMapping(value = "r_u_template", produces = "text/html")
	public String roleusertemplate(){
		return "roleusers/roleuser";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> create(HttpServletResponse response,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		Role role=new Role();
		  if(request.getParameter("id")!=null&&!request.getParameter("id").trim().equals("")){
			role.setId(Integer.parseInt(request.getParameter("id"))) ;
		}
		try {			
			String roleName= request.getParameter("roleName");
			String description= request.getParameter("description");			
			role.setName(roleName);			
			role.setDescription(description);
			this.roleService.create(role);
		} catch (Exception e) {
			e.printStackTrace();
			if(null == role.getId())
				map.put("errorMsg", "添加失败,请稍后再试!");
			else
				map.put("errorMsg", "修改失败,请稍后再试!");	
			return map;
		}
		if(null == role.getId())
			map.put("successMsg", "添加成功!");
		else
			map.put("successMsg", "修改成功!");
		return map;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, String> delete(@PathVariable("id") Integer id, Model uiModel){
		Map<String, String> map = new HashMap<String, String>();
		try{
			this.roleService.delete(Role.class, id);
		}catch(Exception e){
			map.put("errorMsg", "删除失败!");
			return map;
		}
		map.put("successMsg", "删除成功!");
		return map;
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Role());
		return "role/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		uiModel.addAttribute("role",
				roleService.findObjByKey(Role.class, id));
		uiModel.addAttribute("itemId", id);
		return "role/show";
	}

	@RequestMapping(value = "showRoleByUser", method = RequestMethod.POST)
	@ResponseBody
	public Collection<RoleWrapper> showRoleByUser(@RequestParam(value = "userId", required = true) Integer userId){
		try {
			return this.roleService.findByUser(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public List<Role> list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		// if (page != null || size != null) {
		// int sizeNo = size == null ? 10 : size.intValue();
		// final int firstResult = page == null ? 0 : (page.intValue() - 1) *
		// sizeNo;
		// uiModel.addAttribute("role", Role.findDoctorEntries(firstResult,
		// sizeNo));
		// float nrOfPages = (float) Role.countDoctors() / sizeNo;
		// uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages
		// || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		// } else {
		// uiModel.addAttribute("role", Role.findAllDoctors());
		// }
		// return "role/list";
		List<Role> roleList= (List<Role>) roleService.findAll(Role.class);
		List<Role> newRoleList=new ArrayList<Role>();
		if(roleList!=null){
			for (Role role : roleList) {
				Role newrole=new Role(role.getId(), role.getName(), role.getAcronym(), role.getDescription());
				newRoleList.add(newrole);
			}
		} 
		return newRoleList;
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Role role, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, role);
			return "role/update";
		}
		uiModel.asMap().clear();
		// /role.merge();
		roleService.update(role);
		return "redirect:/role/"
				+ encodeUrlPathSegment(role.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		populateEditForm(uiModel,
				(Role) roleService.findObjByKey(Role.class, id));
		return "role/update";
	}

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//	public String delete(@PathVariable("id") Long id,
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "size", required = false) Integer size,
//			Model uiModel) throws Exception {
//		// Role role = (Role)roleService.findObjByKey(Role.class, id);
//		roleService.delete(Role.class, id);
//		uiModel.asMap().clear();
//		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//		return "redirect:/role";
//	}

//	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
//	public String create(@Valid Role role, BindingResult bindingResult,
//			Model uiModel, HttpServletRequest httpServletRequest) {
//		if (bindingResult.hasErrors()) {
//			populateEditForm(uiModel, role);
//			return "role/create";
//		}
//		uiModel.asMap().clear();
//		try {
//			roleService.create(role); // role.persist(); commit
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "redirect:/role/"
//				+ encodeUrlPathSegment(role.getId().toString(),
//						httpServletRequest);
//	}
	
	void populateEditForm(Model uiModel, Role role) {
		uiModel.addAttribute("role", role);
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
