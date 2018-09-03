package com.bizduo.zflow.service.zsurvey;

import java.util.List;

import com.bizduo.zflow.domain.zsurvey.Answer;
import com.bizduo.zflow.service.base.IBaseService;

/**
 * 答案 - Service
 * 
 * @author zs
 *
 */
public interface IAnswerService extends IBaseService<Answer, Integer>{

	/**
	 * 根据问卷记录查询问题记录
	 * @param answerSheetId
	 * @return
	 */
	List<Answer> getByAnswerSheetId(Integer answerSheetId);
}
