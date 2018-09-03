package com.bizduo.zflow.service.sys;

import java.util.Collection;
import java.util.List;

import com.bizduo.zflow.domain.sys.Permission;
import com.bizduo.zflow.service.base.IBaseService;
import com.bizduo.zflow.wrapper.PermissionWrapper;

public interface IPermissionService extends IBaseService<Permission, Integer> {

	public List<Permission> updatePermissionSetting(String[] codes);
	
	public List<PermissionWrapper> findPermissionDescart();
	
	public List<Permission> findByGroupFromResourceAndModule(Integer resourceId);
	
	public List<PermissionWrapper> findByC(Collection<? extends Permission> c);
//	
//	public List<PermissionWrapper> findByMenuItem(MenuItem mi);
//	
//	public List<PermissionWrapper> findByRole(Role role);
//	
//	public List<PermissionWrapper> findByUser(User user);
	
	public void updateMenuPermission(Integer menuItemId, Integer[] ids) throws Exception;

	public void updateRolePermission(Integer roleId, Integer[] ids) throws Exception;
	
	public void updateUserPermission(Integer userId, Integer[] ids) throws Exception;
	
	public Collection<Permission> findByCode(String code);
}
