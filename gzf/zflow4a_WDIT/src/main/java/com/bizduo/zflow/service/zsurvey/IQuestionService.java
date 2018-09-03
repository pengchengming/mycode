package com.bizduo.zflow.service.zsurvey;

import java.util.List;

import com.bizduo.zflow.domain.zsurvey.Question;
import com.bizduo.zflow.service.base.IBaseService;

/**
 * 题目 - Service
 * 
 * @author zs
 *
 */
public interface IQuestionService extends IBaseService<Question, Integer>{

	/**
	 * 根据问卷获取上线的题目列表
	 * @param parentId
	 * @return
	 */
	public List<Question> getByParentId(Integer parentId);
	
	/**
	 * 根据问卷获取题目列表
	 * @return
	 */
	public List<Question> getByStates(Integer parentId);
}
