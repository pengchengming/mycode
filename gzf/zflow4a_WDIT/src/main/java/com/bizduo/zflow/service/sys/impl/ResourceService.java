package com.bizduo.zflow.service.sys.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.Resource;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IResourceService;
@Service
public class ResourceService extends BaseService<Resource, Integer> implements IResourceService {

	@SuppressWarnings("unchecked")
	public List<Resource> findByGroupFromModule() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Resource.class);
		cri.createAlias("module", "m");
		cri.addOrder(Order.asc("m.code"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Resource> findAll(Class<?> clazz) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(clazz);
		cri.createAlias("module", "m");
		cri.addOrder(Order.asc("m.code"));
		try {

			return super.queryDao.getByDetachedCriteria(cri);	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
