package com.bizduo.zflow.service.sys;

import java.util.List;

import com.bizduo.zflow.domain.sys.Resource;
import com.bizduo.zflow.service.base.IBaseService;

public interface IResourceService extends IBaseService<Resource, Integer> {
	public List<Resource> findByGroupFromModule();
}
