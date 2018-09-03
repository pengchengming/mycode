package com.bizduo.zflow.service.linkage;

import java.util.List;

import com.bizduo.zflow.domain.linkage.Department;
import com.bizduo.zflow.domain.linkage.Position;
import com.bizduo.zflow.service.base.IBaseService;
public interface ILinkageService extends IBaseService<Object, Long>{

	/**
	 * 获得所有department
	 * @return
	 * 2012-4-23
	 * @author lm
	 */
	public List<Department> getAllDepartment();

	/**
	 * 根据departmentId得到职位列表
	 * @param departmentId
	 * @return
	 * 2012-4-23
	 * @author lm
	 */
	public List<Position> getPositionListByDepartmentId(Long departmentId);

	/**
	 * 根据id删除department
	 * @param department
	 * @return 2012-4-23
	 * @author lm
	 */
	public void deleteDepartment(Long departmentId); 
}
