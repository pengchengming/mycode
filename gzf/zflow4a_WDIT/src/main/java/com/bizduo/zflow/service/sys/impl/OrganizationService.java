package com.bizduo.zflow.service.sys.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IOrganizationService;
@Service
public class OrganizationService extends BaseService<Organization, Integer> implements IOrganizationService{

	public Collection<Organization> findOrgsByPage(int firstResult, int sizeNo){
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Organization> findHighestAuthority(){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		cri.add(Restrictions.isNull("parent"));
		return super.queryDao.getByDetachedCriteria(cri);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> findByParam(Map<String, Object> paramMap){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		Long id = (Long)paramMap.get("id");
		Integer level = (Integer)paramMap.get("level");
		if(null != level && 0 != level)
			cri.add(Restrictions.eq("level", level));
		if(null != id && 0L != id)
			cri.add(Restrictions.eq("parent.id", id));
		cri.addOrder(Order.asc("indexNum"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	@Override
	public Organization create(Organization obj) throws Exception{
		Organization parent = obj.getParent();
		if(null != parent && null != parent.getId()){
			parent = super.findObjByKey(Organization.class, parent.getId());
			parent.setIsParent(true);
			
			obj.setIsParent(false);
			Integer level = parent.getLevel();
			obj.setLevel(level.intValue() + 1);
			obj.setParent(parent);
		}else{
			obj.setLevel(1);
			obj.setIsParent(false);
			obj.setParent(null);
			obj.setNlevel(0);
		}
		return super.create(obj);
	}

	@SuppressWarnings("unchecked")
	public Organization getByNlevel(Integer nlevel) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		cri.add(Restrictions.eq("nlevel", nlevel));
//		cri.add(Restrictions.eq("type", Organization.TYPE.ASSISTPRODUCE.ordinal()));
		List<Organization> orgs =  super.queryDao.getByDetachedCriteria(cri);
		if(null != orgs && 0 < orgs.size())
			return orgs.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public Organization getByAcronym(String acronym) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		cri.add(Restrictions.eq("acronym", acronym));
		List<Organization> orgs = super.queryDao.getByDetachedCriteria(cri);
		if(null != orgs && 0 < orgs.size())
			return orgs.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public Organization getByName(String name) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		cri.add(Restrictions.eq("name", name));
		List<Organization> orgs = super.queryDao.getByDetachedCriteria(cri);
		if(null != orgs && 0 < orgs.size())
			return orgs.get(0);
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Organization> getByParentId(Integer id){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		if(null != id && 0L != id)
			cri.add(Restrictions.eq("parent.id", id));
		else
			cri.add(Restrictions.eq("nlevel", 0));
		cri.addOrder(Order.asc("level"));
		return super.queryDao.getByDetachedCriteria(cri);
	}

	@SuppressWarnings("unchecked")
	public List<Organization> getByParentId(Integer parentId, Integer id){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		if(null != parentId && 0L != parentId)
			cri.add(Restrictions.eq("parent.id", parentId));
		if(null != id && 0L != id)
			cri.add(Restrictions.ne("id", id));
		return super.queryDao.getByDetachedCriteria(cri);
	}

	@SuppressWarnings("unchecked")
	public List<Organization> getByNameOrParentId(String name, Integer parentId) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		if(null != name && !("").equals(name)){
			cri.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
		}else{
			if(null != parentId){
				cri.add(Restrictions.eq("parent.id", parentId));
			}else{
				cri.add(Restrictions.eq("nlevel", 0));
			}
		}
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}

	@SuppressWarnings("unchecked")
	public List<Organization> getAll() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		cri.addOrder(Order.asc("nlevel"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	@SuppressWarnings("unchecked")
	public List<Organization> getByType(Integer type){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		cri.add(Restrictions.eq("type", type));
		cri.addOrder(Order.asc("nlevel"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	
	//jiaju
	@SuppressWarnings("unchecked")
	public List<Organization> getcompany() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		cri.add(Restrictions.eq("nlevel", 0));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	/**
	 * 获取部门
	 * @param orgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> getdepartment(Integer orgId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Organization.class);
		cri.add(Restrictions.eq("entityid",orgId));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
}
