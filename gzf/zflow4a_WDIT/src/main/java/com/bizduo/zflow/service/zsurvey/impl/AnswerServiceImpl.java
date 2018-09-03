package com.bizduo.zflow.service.zsurvey.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.zsurvey.Answer;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.zsurvey.IAnswerService;

/**
 * 答案 - Service
 * 
 * @author zs
 *
 */
@Service
public class AnswerServiceImpl extends BaseService<Answer, Integer> implements IAnswerService{

	@SuppressWarnings("unchecked")
	public List<Answer> getByAnswerSheetId(Integer answerSheetId) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Answer.class);
		cri.add(Restrictions.eq("answerSheet.id", answerSheetId));
		cri.addOrder(Order.desc("id"));
		List<Answer> answers = super.queryDao.getByDetachedCriteria(cri);
		if(answers.size()<=0){
			return null;
		}
		return answers;
	}

}
