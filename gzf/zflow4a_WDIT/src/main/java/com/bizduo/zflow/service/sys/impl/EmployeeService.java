package com.bizduo.zflow.service.sys.impl;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.Employee;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IEmployeeService;
@Service
public class EmployeeService extends BaseService<Employee, Integer> implements IEmployeeService{

	@SuppressWarnings("unchecked")
	public List<Employee> getByParam(Long orgid, String realname,
			Boolean isLeave) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Employee.class);
		if(null != orgid && !("").equals(orgid))
			cri.add(Restrictions.eq("organization.id", orgid));
		if(null != realname && !("").equals(realname))
			cri.add(Restrictions.like("realname", realname, MatchMode.ANYWHERE));
		if(null != isLeave)
			cri.add(Restrictions.eq("isLeave", isLeave));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getByParamPage(Long orgid, String realname,String username,Integer rankId,
			String userType,PageTrace pageTrace) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Employee.class);
		if(null != orgid && !("").equals(orgid))
			cri.add(Restrictions.eq("organization.id", orgid));
		if(null != realname && !("").equals(realname))
			cri.add(Restrictions.like("realname", realname, MatchMode.ANYWHERE));
		if(null != username && !("").equals(username))
			cri.add(Restrictions.like("username", username, MatchMode.ANYWHERE));
		if(null != rankId && !("").equals(rankId))
			cri.add(Restrictions.eq("rank.id", rankId));
		if(null != userType)
			cri.add(Restrictions.eq("userType", userType));
		cri.addOrder(Order.asc("id")); 
		return super.queryDao.getByDetachedCriteria(cri, pageTrace, false);
	}
	@SuppressWarnings("unchecked")
	public Employee getByUserame(String username){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Employee.class);
		cri.add(Restrictions.eq("username", username));
		List<Employee> employees = super.queryDao.getByDetachedCriteria(cri);
		if(null != employees && 0 < employees.size())
			return employees.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Employee getByOpenId(String openId) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Employee.class);
		cri.add(Restrictions.eq("openId", openId));
		List<Employee> employees = super.queryDao.getByDetachedCriteria(cri);
		if(null != employees && 0 < employees.size())
			return employees.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getByRealName(String realname) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Employee.class);
		if(null != realname && !("").equals(realname))
			cri.add(Restrictions.like("realname", realname, MatchMode.ANYWHERE));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}	
	
	@SuppressWarnings("unchecked")
	public List<Employee> getByRealNameAndUserName(String realname,String username) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Employee.class);
		if(null != realname && !("").equals(realname))
			cri.add(Restrictions.like("realname", realname, MatchMode.ANYWHERE));
		if(null != username && !("").equals(username))
			cri.add(Restrictions.like("username", realname, MatchMode.ANYWHERE));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}	
	 
	@SuppressWarnings({ "unchecked", "unused" })
	public boolean checkEmployeeIDAndUserName(Integer id,String username){
		boolean checkresult=false;
		List<Employee> employeelist;
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Employee.class);
		if(null != id)
			cri.add(Restrictions.ne("id", id));
		if(null != username && !("").equals(username))
			cri.add(Restrictions.eq("username", username));
		employeelist = super.queryDao.getByDetachedCriteria(cri);
		if(employeelist.size()>0)
			return false;
		else
			return true;
	}
}
