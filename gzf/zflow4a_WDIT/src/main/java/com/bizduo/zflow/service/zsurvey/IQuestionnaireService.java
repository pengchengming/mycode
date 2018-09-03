package com.bizduo.zflow.service.zsurvey;


import java.util.List;

import com.bizduo.zflow.domain.zsurvey.Questionnaire;
import com.bizduo.zflow.service.base.IBaseService;


/**
 * 问卷 - Service
 * 
 * @author zs
 *
 */
public interface IQuestionnaireService extends IBaseService<Questionnaire, Integer>{

	/**
	 * 获取最大id
	 * @return
	 */
	Integer getMaxId();
	
	/**
	 * 查询isDelete为0的试卷
	 * @return
	 */
	List<Questionnaire> getByIsDelete();
	
	
    List<Questionnaire> getByUserName(String username);
	
	/**
	 * 查询isOnOff为1的试卷
	 * @return
	 */
	List<Questionnaire> getByIsOnOff();
	
	/**
	 * 根据答卷记录获取问卷
	 * @param answerSheetId
	 */
	Questionnaire getByAnswerSheet(Integer id);
	
}
