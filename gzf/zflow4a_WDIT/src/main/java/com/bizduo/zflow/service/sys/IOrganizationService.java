package com.bizduo.zflow.service.sys;

import java.util.Collection;
import java.util.List;

import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.service.base.IBaseService;

public interface IOrganizationService extends IBaseService<Organization, Integer> {

	public Collection<Organization> findOrgsByPage(int firstResult, int sizeNo);
	
	public Collection<Organization> findHighestAuthority();

	public Organization getByNlevel(Integer nlevel);

	public Organization getByAcronym(String acronym);
	
	public Organization getByName(String name);

	public List<Organization> getByParentId(Integer id);

	public List<Organization> getByNameOrParentId(String name, Integer parentId);

	public List<Organization> getAll();
	
	public List<Organization> getByType(Integer type);
	//jiaju
	/**
	 * 获取公司
	 */
	public List<Organization> getcompany();
	/**
	 * 获取部门
	 * @param orgId
	 * @return
	 */
	public List<Organization> getdepartment(Integer orgId);
}
