package com.bizduo.zflow.service.zsurvey.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.zsurvey.Option;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.zsurvey.IOptionService;

/**
 * 答案 - Service
 * 
 * @author zs
 *
 */
@Service
public class OptionServiceImpl extends BaseService<Option, Integer> implements IOptionService{

	@SuppressWarnings("unchecked")
	public List<Option> getByParentId(Integer parentId) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Option.class);
		cri.add(Restrictions.eq("question.id", parentId));
		cri.addOrder(Order.asc("id"));
		List<Option> options = super.queryDao.getByDetachedCriteria(cri);
		if(options.size()<=0){
			return null;
		}
		return options;
	}


	
}
