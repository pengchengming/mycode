package com.bizduo.zflow.service.zsurvey;

import java.util.List;

import com.bizduo.zflow.domain.zsurvey.AnswerSheet;
import com.bizduo.zflow.service.base.IBaseService;

/**
 * 答卷记录 - Entity
 * 
 * @author zs
 *
 */
public interface IAnswerSheetService extends IBaseService<AnswerSheet, Integer>{

	/**
	 * 根据时间获取记录
	 * @param date
	 * @return
	 */
	AnswerSheet getByDate(String date);
	
	/**
	 * 根据试卷states与condition获取记录
	 * @param QuesnareId
	 * @return
	 */
	List<AnswerSheet> getByQuesnareIds();
	
	/**
	 * 根据试卷id获取记录
	 * @return
	 */
	AnswerSheet getByQuesnareId(Integer id);
	
	/**
	 * 获取state为1(作答过)的记录
	 * @param states
	 * @return
	 */
	List<AnswerSheet> getByStates();

	AnswerSheet findByCode(Class<AnswerSheet> class1, String answersheetCode);
	
	int getAnswerSheetScore(int answersheetid);
	
	@SuppressWarnings("rawtypes")
	List  getAnswerSheetList() ;
	
	
	
}
