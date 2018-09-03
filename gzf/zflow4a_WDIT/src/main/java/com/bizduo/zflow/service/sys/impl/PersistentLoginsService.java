package com.bizduo.zflow.service.sys.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.PersistentLogins;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IPersistentLoginsService;
@Service
public class PersistentLoginsService extends BaseService<PersistentLogins, Long> implements IPersistentLoginsService {
	
	@SuppressWarnings("unchecked")
	public PersistentLogins getByToken(String token){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(PersistentLogins.class);
		cri.add(Restrictions.eq("token", token));
		cri.add(Restrictions.eq("valid", true));
		List<PersistentLogins> c = queryDao.getByDetachedCriteria(cri);
		if(null != c && 0 < c.size())
			return c.get(0);
		return null;
	}
}
