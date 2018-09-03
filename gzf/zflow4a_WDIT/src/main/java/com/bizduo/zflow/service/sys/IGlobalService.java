package com.bizduo.zflow.service.sys;

import java.util.List;

import com.bizduo.zflow.domain.sys.Global;
import com.bizduo.zflow.service.base.IBaseService;
/**
 * 配置信息 - Service
 * 
 * @author zs
 *
 */
public interface IGlobalService extends IBaseService<Global, Integer>{

	public Global getByCode(String code);
	public List<Global>  getVisibleGlobals();


}
