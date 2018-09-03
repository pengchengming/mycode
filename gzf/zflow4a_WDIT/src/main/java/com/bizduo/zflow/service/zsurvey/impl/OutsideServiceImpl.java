package com.bizduo.zflow.service.zsurvey.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.zsurvey.Outside;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.zsurvey.IOutsideService;

/**
 * 外部调查 - Service
 * 
 * @author zs
 *
 */
@Service
public class OutsideServiceImpl extends BaseService<Outside, Integer> implements IOutsideService{

	@SuppressWarnings("unchecked")
	public Outside getByParentId(Integer parentId) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Outside.class);
		cri.add(Restrictions.eq("questionnaire.id", parentId));
		cri.addOrder(Order.asc("id"));
		List<Outside> outsides = super.queryDao.getByDetachedCriteria(cri);
		if(outsides.size()<=0){
			return null;
		}
		return outsides.get(0);
	}

	@SuppressWarnings("unchecked")
	public Outside getByTelName(String tel, String name) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Outside.class);
		cri.add(Restrictions.eq("tel", tel));
		cri.add(Restrictions.eq("stuName", name));
		List<Outside> outsides = super.queryDao.getByDetachedCriteria(cri);
		if(outsides.size()<=0){
			return null;
		}
		return outsides.get(0);
	}

}
