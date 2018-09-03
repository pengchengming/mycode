package com.bizduo.zflow.service.zsurvey;

import com.bizduo.zflow.domain.zsurvey.Outside;
import com.bizduo.zflow.service.base.IBaseService;

/**
 * 外部调查 - Service
 * 
 * @author zs
 *
 */
public interface IOutsideService extends IBaseService<Outside, Integer>{

	/**
	 * 根据问卷id获取外部调查
	 * @param parentId
	 * @return
	 */
	Outside getByParentId(Integer parentId);
	
	/**
	 * 根据手机号和姓名获取外部调查
	 * @param tel
	 * @param name
	 * @return
	 */
	Outside getByTelName(String tel,String name);
}
