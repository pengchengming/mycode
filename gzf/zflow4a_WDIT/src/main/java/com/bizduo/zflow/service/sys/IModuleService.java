package com.bizduo.zflow.service.sys;

import java.util.List;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.sys.Module;
import com.bizduo.zflow.service.base.IBaseService;

public interface IModuleService extends IBaseService<Module, Long> {

	public List<Module> findByPage(PageTrace pageTrace);
}
