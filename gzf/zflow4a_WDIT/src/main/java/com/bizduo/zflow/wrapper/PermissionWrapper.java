package com.bizduo.zflow.wrapper;

import java.io.Serializable;
import java.util.List;

import com.bizduo.zflow.domain.sys.Permission;
import com.bizduo.zflow.domain.sys.Resource;
/**
 * 系统初始化时,为权限定义封装数据的POJO
 * @author Administrator
 *
 */
public class PermissionWrapper implements Serializable{
	private static final long serialVersionUID = 3430985419500569295L;
	private Resource resource;
	private List<Permission> permissionList;
	private String moduleName;
	private int rowspan;
	
	public PermissionWrapper(){super();}
	public PermissionWrapper(Resource resource, List<Permission> permissionList) {
		super();
		this.resource = resource;
		this.permissionList = permissionList;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public List<Permission> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
}
