package com.bizduo.zflow.service.sys.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.Global;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IGlobalService;

/**
 * 配置信息 - Service
 * 
 * @author zs
 *
 */
@Service
public class GlobalService extends BaseService<Global, Integer> implements IGlobalService{

	@SuppressWarnings("unchecked")
	public Global getByCode(String code) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Global.class);
		cri.add(Restrictions.eq("code", code));
		List<Global> global = super.queryDao.getByDetachedCriteria(cri);
		if(null != global && 0 < global.size())
			return global.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Global>  getVisibleGlobals() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Global.class);
		cri.add(Restrictions.lt("code","09001" ));
		List<Global> globallist = super.queryDao.getByDetachedCriteria(cri);
		return globallist;
	}
	



}
