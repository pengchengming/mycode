package com.bizduo.zflow.service.sys;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.service.base.IBaseService;
import com.bizduo.zflow.wrapper.RoleWrapper;

public interface IRoleService extends IBaseService<Role, Integer> {

	public Collection<RoleWrapper> findByUser(Integer userId) throws Exception;

	public List<Role> findByMap(Map<String, Object> map, PageTrace pageTrace);
	
}
