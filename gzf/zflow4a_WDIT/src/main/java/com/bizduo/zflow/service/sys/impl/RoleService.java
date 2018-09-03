package com.bizduo.zflow.service.sys.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.wrapper.RoleWrapper;
@Service
public class RoleService extends BaseService<Role, Integer> implements IRoleService {

	@Autowired
	private IUserService userService;

	public Collection<RoleWrapper> findByUser(Integer userId) throws Exception{
		Collection<Role> roles = (this.userService.findObjByKey(User.class, userId)).getRoles();
		
		Collection<Role> res = (List<Role>) super.findAll(Role.class);
		
		Collection<RoleWrapper> rws = new ArrayList<RoleWrapper>();
		
		for(Role re : res){
			RoleWrapper rw = new RoleWrapper();
			Role newre=new Role(re.getId(), re.getName(),re.getAcronym(), re.getDescription());
			rw.setRole(newre);
			for(Role r : roles){
				if(r.getId().equals(re.getId()))
					rw.setChecked(true);
			}
			rws.add(rw);
		}
		return rws;
	}
	@SuppressWarnings("unchecked")
	public List<Role> findByMap(Map<String, Object> map, PageTrace pageTrace){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Role.class); 
		if(map.get("roleName")!=null&&!map.get("roleName").equals("") )
			cri.add(Restrictions.or(Restrictions.ilike("description", map.get("username").toString(), MatchMode.ANYWHERE), Restrictions.ilike("name", map.get("username").toString(), MatchMode.ANYWHERE)));
		cri.addOrder(Order.desc("id"));
		return super.queryDao.getByDetachedCriteria(cri, pageTrace, false);
	}
}
