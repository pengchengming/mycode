package com.bizduo.zflow.service.zsurvey.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.zsurvey.Question;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.zsurvey.IQuestionService;

/**
 * 问题 - Service
 * 
 * @author zs
 *
 */
@Service
public class QuestionServiceImpl extends BaseService<Question, Integer> implements IQuestionService{

		@SuppressWarnings("unchecked")
		public List<Question> getByParentId(Integer parentId) {
			PageDetachedCriteria cri = PageDetachedCriteria.forClass(Question.class);
			cri.add(Restrictions.eq("questionnaire.id", parentId));
			cri.add(Restrictions.eq("status", 1));
			cri.addOrder(Order.asc("id"));
			List<Question> questions = super.queryDao.getByDetachedCriteria(cri);
			if(questions.size()<=0){
				return null;
			}
			return questions;
	}

		@SuppressWarnings("unchecked")
		public List<Question> getByStates(Integer parentId) {
			PageDetachedCriteria cri = PageDetachedCriteria.forClass(Question.class);
			cri.add(Restrictions.eq("questionnaire.id", parentId));
			cri.addOrder(Order.asc("id"));
			List<Question> questions = super.queryDao.getByDetachedCriteria(cri);
			if(questions.size()<=0){
				return null;
			}
			return questions;
		}

}
