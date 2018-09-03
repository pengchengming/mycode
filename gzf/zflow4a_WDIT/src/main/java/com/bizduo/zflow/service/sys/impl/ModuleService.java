package com.bizduo.zflow.service.sys.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.Module;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IModuleService;
@Service
public class ModuleService extends BaseService<Module, Long> implements IModuleService {

	@SuppressWarnings("unchecked")
	public List<Module> findByPage(PageTrace pageTrace) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Module.class);
		if(null != pageTrace)
			return super.queryDao.getByDetachedCriteria(cri, pageTrace, false);
		return super.queryDao.getAll(Module.class);
	}

}
