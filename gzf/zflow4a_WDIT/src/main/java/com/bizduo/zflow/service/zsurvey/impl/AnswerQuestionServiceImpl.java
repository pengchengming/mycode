package com.bizduo.zflow.service.zsurvey.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.zsurvey.AnswerQuestion;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.zsurvey.IAnswerQuestionService;

/**
 * 答案 - Service
 * 
 * @author zs
 *
 */
@Service
public class AnswerQuestionServiceImpl extends BaseService<AnswerQuestion, Integer> implements IAnswerQuestionService{

	@SuppressWarnings("unchecked")
	public List<AnswerQuestion> getByAnswerQuestionSheetId(Integer answerSheetId) {
		// TODO Auto-generated method stub
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(AnswerQuestion.class);
		cri.add(Restrictions.eq("answerSheet.id", answerSheetId));
		cri.addOrder(Order.desc("id"));
		List<AnswerQuestion> answerquestions = super.queryDao.getByDetachedCriteria(cri);
		if(answerquestions.size()<=0){
			return null;
		}
		return answerquestions;	 
	}

	@SuppressWarnings("unchecked")
	public List<AnswerQuestion> getByAnswersheetCode(String answersheetCode) {
		// TODO Auto-generated method stub
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(AnswerQuestion.class);
		cri.createAlias("answerSheet", "answerSheet");
		cri.add(Restrictions.eq("answerSheet.code", answersheetCode));
		cri.addOrder(Order.desc("id"));
		List<AnswerQuestion> answerquestions = super.queryDao.getByDetachedCriteria(cri);
		if(answerquestions.size()<=0){
			return null;
		}
		return answerquestions;	 
	}

 

 
}
