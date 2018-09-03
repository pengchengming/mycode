package com.bizduo.zflow.service.customform.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.SelectConditions;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.customform.ISelectConditionsService;
@Service
public class SelectConditionsService extends BaseService<SelectConditions, Long>  implements ISelectConditionsService {
	
	@SuppressWarnings("unchecked")
	public List<SelectConditions> findByTableId(Long id){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(SelectConditions.class);
		if(null != id)
			cri.add(Restrictions.eq("selectTableList.id", id)); 
		cri.addOrder(Order.asc("idx"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
}
