package com.bizduo.zflow.service.zsurvey;


import java.util.List;

import com.bizduo.zflow.domain.zsurvey.Option;
import com.bizduo.zflow.service.base.IBaseService;

/**
 * 答案 - Service
 * 
 * @author zs
 *
 */
public interface IOptionService extends IBaseService<Option, Integer>{

	/**
	 * 根据问题获取选项列表
	 * @param parentId
	 * @return
	 */
	List<Option> getByParentId(Integer parentId);
}
