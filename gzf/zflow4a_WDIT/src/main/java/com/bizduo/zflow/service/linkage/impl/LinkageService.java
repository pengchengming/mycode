package com.bizduo.zflow.service.linkage.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.linkage.Department;
import com.bizduo.zflow.domain.linkage.Position;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.linkage.ILinkageService;
import com.bizduo.zflow.status.ZFlowStatus;
@Service
public class LinkageService extends BaseService<Object, Long> implements ILinkageService {

	/**
	 * 根据id删除department
	 * @param department
	 * @return 2012-4-23
	 * @author lm
	 */
	public void deleteDepartment(Long departmentId) {
		if (departmentId != null) {
			Department department = (Department) super.queryDao.get(Department.class, departmentId);
			department.setIsDelete(ZFlowStatus.ISDELETE_YES);
			// 删除department的同时，同时删除department的register
			List<Position> poistionList = department.getPositionList();
			if (poistionList != null && poistionList.size() > 0) {
				for (int i = 0; i < poistionList.size(); i++) {
					poistionList.get(i).setIsDelete(ZFlowStatus.ISDELETE_YES);
				}
			}
			super.queryDao.save(department);
		}
	}

	/**
	 * 获得所有department
	 * @param department
	 * @return 2012-4-23
	 * @author lm
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getAllDepartment() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Department.class);
		cri.add(Restrictions.eq("isDelete", ZFlowStatus.ISDELETE_NO));
		return super.queryDao.getByDetachedCriteria(cri);
	}
    
	/**
	 * 根据departmentId得到职位列表
	 * @param department
	 * @return 2012-4-23
	 * @author lm
	 */
	public List<Position> getPositionListByDepartmentId(Long departmentId){
		if (departmentId != null) {
			Department department = (Department) super.queryDao.get(Department.class, departmentId);
			List<Position> positionList = department.getPositionList();
			if (positionList != null && positionList.size() > 0) {
				return positionList;
				}
			}
		return null;
		}
}
