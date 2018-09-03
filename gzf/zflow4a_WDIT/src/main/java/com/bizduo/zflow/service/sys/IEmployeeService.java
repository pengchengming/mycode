package com.bizduo.zflow.service.sys;

import java.util.List;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.sys.Employee;
import com.bizduo.zflow.service.base.IBaseService;

public interface IEmployeeService extends IBaseService<Employee, Integer> {
	public List<Employee> getByParam(Long orgid, String realname, Boolean isLeave);
	
	public List<Employee> getByParamPage(Long orgid, String realname,String username,Integer rankId, String userType,PageTrace pageTrace);
	
	public Employee getByUserame(String username);
	
	/**
	 * 根据openId获取employee
	 * @param openId
	 * @return
	 */
	public Employee getByOpenId(String openId);
	
	/** 根据名字查询*/
	public List<Employee> getByRealName(String realname);
	
	public List<Employee> getByRealNameAndUserName(String realname,String username);
	
	public boolean checkEmployeeIDAndUserName(Integer id,String username);
}
