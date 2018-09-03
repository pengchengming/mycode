package com.bizduo.zflow.service.dm.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.dm.CCMPush;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.dm.IPushService;
@Service
public class PushService extends BaseService<CCMPush, Long> implements IPushService{
	/**
	 * 获取enabled 的所以数据
	 * @param b
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CCMPush> getPushByEnabled(boolean enabled) { 
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(CCMPush.class);   
			cri.add(Restrictions.eq("enabled", enabled));  
			cri.addOrder(Order.asc("id"));
			cri.add(Restrictions.isNull("errorMsg"));  
			return queryDao.getByDetachedCriteria(cri); 
	}

}
