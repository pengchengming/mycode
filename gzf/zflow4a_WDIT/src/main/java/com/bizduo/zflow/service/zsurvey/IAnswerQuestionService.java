package com.bizduo.zflow.service.zsurvey;

import java.util.List;

import com.bizduo.zflow.domain.zsurvey.AnswerQuestion;
import com.bizduo.zflow.service.base.IBaseService;

/**
 * 答案题干 - Service
 * 
 * @author zs
 *
 */
public interface IAnswerQuestionService extends IBaseService<AnswerQuestion, Integer>{

	/**
	 * 根据问卷记录查询答案题干记录
	 * @param answerSheetId
	 * @return
	 */
	List<AnswerQuestion> getByAnswerQuestionSheetId(Integer answerSheetId);

	List<AnswerQuestion> getByAnswersheetCode(String answersheetCode);
}
