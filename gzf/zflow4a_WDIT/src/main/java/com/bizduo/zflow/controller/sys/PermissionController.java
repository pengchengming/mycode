package com.bizduo.zflow.controller.sys;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.bizduo.zflow.domain.sys.MenuItem;
import com.bizduo.zflow.domain.sys.Permission;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.service.sys.IMenuItemService;
import com.bizduo.zflow.service.sys.IPermissionService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.wrapper.PermissionWrapper;

@Controller
@RequestMapping(value = "/permission")
public class PermissionController {
	@Autowired
	private IPermissionService permissionService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IMenuItemService menuItemService;

	@RequestMapping(value = "/template", produces = "text/html")
	public String template(){
		return "permission/template";
	}
	
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> save(@RequestParam(value = "codes[]", required = true) String[] codes){
		Map<String, String> map = new HashMap<String, String>();
		try{
			this.permissionService.updatePermissionSetting(codes);
		}catch(Exception e){
			e.printStackTrace();
			map.put("errorMsg", "保存失败,请稍后再试!");
		}
		map.put("successMsg", "权限设置保存成功!");
		return map;
	}
	
//	@RequestMapping(value="/list", method=RequestMethod.POST)
//	@ResponseBody
//	public List<PermissionWrapper> list(){
//		try{
//		List<PermissionWrapper> permissionWrappers = permissionService.getPermissionDescart();
//		return permissionWrappers;
//		}catch(Exception e){
//			e.printStackTrace();
//			return null;
//		}
//	}
	/**
	 * 权限设置界面的数据由这里返回
	 * @return json
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	public List<PermissionWrapper> list(){
		return this.permissionService.findPermissionDescart();
	}
	
	/**
	 * 菜单的权限设置的数据展示界面请求的数据由这里返回
	 * @param menuItemId
	 * @return json
	 */
//	@RequestMapping(value="/menuPermisList", method=RequestMethod.POST)
//	@ResponseBody
//	public List<PermissionWrapper> menuPermisList(@RequestParam(value = "menuItemId", required = true) Long menuItemId){
//		try{
//			MenuItem mi = (MenuItem)permissionService.findObjByKey(MenuItem.class, menuItemId);
//			if(null == mi) throw new Exception("不存在ID为" + menuItemId + "的菜单!");
//		  //List<PermissionWrapper> pwList = permissionService.findByMenuItem(mi);
//			List<PermissionWrapper> pwList = this.permissionService.findByC(mi.getPermissions());
//			return pwList;
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * 角色的权限设置的数据展示界面请求的数据由这里返回
	 * @param menuItemId
	 * @return json
	 */
//	@RequestMapping(value = "rolePermisList", method = RequestMethod.POST)
//	@ResponseBody
//	public List<PermissionWrapper> rolePermisList(@RequestParam(value = "roleId", required = true) Long roleId){
//		try{
//			Role role = (Role)permissionService.findObjByKey(Role.class, roleId);
//			if(null == role) throw new Exception("不存在ID为" + roleId + "的菜单!");
//			//List<PermissionWrapper> reList = permissionService.findByRole(role);
//			List<PermissionWrapper> reList = this.permissionService.findByC(role.getPermissions());
//			return reList;
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * 用户的权限设置的数据展示界面请求的数据由这里返回
	 * @param menuItemId
	 * @return json
	 */
//	@RequestMapping(value = "userPermisList", method = RequestMethod.POST)
//	@ResponseBody
//	public List<PermissionWrapper> userPermisList(@RequestParam(value = "userId", required = true) Long userId){
//		try{
//			User ur = (User)permissionService.findObjByKey(User.class, userId);
//			if(null == ur) throw new Exception("不存在ID为" + userId + "的菜单!");
//		  //List<PermissionWrapper> urList = permissionService.findByUser(ur);
//			List<PermissionWrapper> urList = this.permissionService.findByC(ur.getPermissions());
//			return urList;
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	@RequestMapping(value = "listByT", method = RequestMethod.POST)
	@ResponseBody
	public List<PermissionWrapper> listByT( @RequestParam(value = "menuItemId", required = false) Integer menuItemId,
											@RequestParam(value = "userId", required = false) Integer userId,
											@RequestParam(value = "roleId", required = false) Integer roleId){
		try{
			if(null != menuItemId && 0L != menuItemId){
				MenuItem mi = this.menuItemService.findObjByKey(MenuItem.class, menuItemId);
				List<PermissionWrapper> permissionWrapperList= this.permissionService.findByC(mi.getPermissions());
				return permissionWrapperList;
			}else if(null != userId && 0L != userId){
//				User u = (User)this.permissionService.findObjByKey(User.class, userId);
//				return this.permissionService.findByC(u.getPermissions());
				return null;
			}else if(null != roleId && 0L != roleId){
				Role r = this.roleService.findObjByKey(Role.class, roleId);
				return this.permissionService.findByC(r.getPermissions());
			}else{
				throw new Exception("请求失败,请稍后再试!");
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Permission permission, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, permission);
			return "permission/create";
		}
		uiModel.asMap().clear();
		try {
			this.permissionService.create(permission); // permission.persist(); commit
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/permission/"
				+ encodeUrlPathSegment(permission.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Permission());
		return "permission/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		uiModel.addAttribute("permission",
				this.permissionService.findObjByKey(Permission.class, id));
		uiModel.addAttribute("itemId", id);
		return "permission/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		// if (page != null || size != null) {
		// int sizeNo = size == null ? 10 : size.intValue();
		// final int firstResult = page == null ? 0 : (page.intValue() - 1) *
		// sizeNo;
		// uiModel.addAttribute("permission", Permission.findDoctorEntries(firstResult,
		// sizeNo));PageTrace . count
		// float nrOfPages = (float) Permission.countDoctors() / sizeNo;
		// uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages
		// || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		// } else {
		// uiModel.addAttribute("permission", Permission.findAllDoctors());
		// }
		// return "permission/list";
		return null;
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Permission permission, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, permission);
			return "permission/update";
		}
		uiModel.asMap().clear();
		// /permission.merge();
		permissionService.update(permission);
		return "redirect:/permission/"
				+ encodeUrlPathSegment(permission.getId().toString(),
						httpServletRequest);
	}
	
	@RequestMapping(value = "updateByT", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateByT(@RequestParam(value = "menuItemId", required = false) Integer menuItemId,
										 @RequestParam(value = "userId", required = false) Integer userId,
										 @RequestParam(value = "roleId", required = false) Integer roleId,
										 @RequestParam(value = "ids[]", required = true) Integer[] ids){
		Map<String, String> map = new HashMap<String, String>();
		try{
			if(null != menuItemId && 0L != menuItemId){
				this.permissionService.updateMenuPermission(menuItemId, ids);
				map.put("successMsg", "菜单权限保存成功!");
				return map;
			}else if(null != userId && 0L != userId){
				this.permissionService.updateUserPermission(userId, ids);
				map.put("successMsg", "用户权限保存成功!");
				return map;
			}else if(null != roleId && 0L != roleId){
				this.permissionService.updateRolePermission(roleId, ids);
				map.put("successMsg", "角色权限保存成功!");
				return map;
			}else{
				map.put("errorMsg", "保存失败,请稍后再试!");
				return map;
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("errorMsg", "保存失败,请稍后再试!");
			return map;
		}
	}
	
//	@RequestMapping(value = "updateMenuPermission", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String, String> updateMenuPermission(@RequestParam(value = "menuItemId", required = true) Long menuItemId,
//													@RequestParam(value = "ids[]", required = true) Long[] ids){
//		Map<String, String> map = new HashMap<String, String>();
//		//MenuItem mi = (MenuItem)this.permissionService.findObjByKey(MenuItem.class, menuItemId);
//		try {
//			this.permissionService.updateMenuPermission(menuItemId, ids);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			map.put("errorMsg", "保存失败,请稍后再试!");
//		}
//		map.put("successMsg", "菜单权限保存成功!");
//		return map;
//	}
//	
//	@RequestMapping(value = "updateRolePermission", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String, String> updateRolePermission(@RequestParam(value = "roleId", required = true) Long roleId,
//													@RequestParam(value = "ids[]", required = true) Long[] ids){
//		Map<String, String> map = new HashMap<String, String>();
//		try{
//			this.permissionService.updateRolePermission(roleId, ids);
//		}catch(Exception e){
//			e.printStackTrace();
//			map.put("errorMsg", "保存失败,请稍后再试!");
//		}
//		map.put("successMsg", "角色权限保存成功!");
//		return map;
//	}
	
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) throws Exception {
		populateEditForm(uiModel, (Permission) permissionService.findObjByKey(Permission.class, id));
		return "permission/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) throws Exception {
		// Permission permission = (Permission)permissionService.findObjByKey(Permission.class, id);
		permissionService.delete(Permission.class, id);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/permission";
	}

	void populateEditForm(Model uiModel, Permission permission) {
		uiModel.addAttribute("permission", permission);
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
