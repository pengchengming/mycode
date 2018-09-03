package com.bizduo.zflow.service.zsurvey.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.zsurvey.Questionnaire;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.zsurvey.IQuestionnaireService;

/**
 * 问卷 - Service
 * 
 * @author zs
 *
 */
@Service
public class QuestionnaireServiceImpl extends BaseService<Questionnaire, Integer> implements IQuestionnaireService{
	
	public Integer getMaxId() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Questionnaire> getByIsDelete() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Questionnaire.class);
		cri.add(Restrictions.eq("isDelete", 0));
		cri.addOrder(Order.desc("id"));
		List<Questionnaire> questionnaires = super.queryDao.getByDetachedCriteria(cri);
		if(questionnaires.size()<=0){
			return null;
		}
		return questionnaires;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Questionnaire> getByUserName(String username) {
		List questionareIDList = this.callShellProcedure("180102_"+username);
		List qnIDList = new ArrayList();
		for(int i=0;i<questionareIDList.size();i++)
		{
			Map map =(Map)questionareIDList.get(i);
			qnIDList.add(Integer.valueOf(map.get("id").toString()));
		}
		
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Questionnaire.class);
		cri.add(Restrictions.eq("isDelete", 0));
		cri.add(Restrictions.in("id", qnIDList));
		cri.addOrder(Order.desc("id"));
		List<Questionnaire> questionnaires = super.queryDao.getByDetachedCriteria(cri);
		if(questionnaires.size()<=0){
			return null;
		}
		return questionnaires;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Questionnaire> getByIsOnOff() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Questionnaire.class);
		cri.add(Restrictions.eq("isOnOff", 1));
		cri.addOrder(Order.asc("id"));
		List<Questionnaire> questionnaires = super.queryDao.getByDetachedCriteria(cri);
		if(questionnaires.size()<=0){
			return null;
		}
		return questionnaires;
	}

	@SuppressWarnings("unchecked")
	public Questionnaire getByAnswerSheet(Integer id) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Questionnaire.class);
		cri.add(Restrictions.eq("isOnOff", 1));
		cri.add(Restrictions.eq("id", id));
		List<Questionnaire> questionnaires = super.queryDao.getByDetachedCriteria(cri);
		if(questionnaires.size()<=0){
			return null;
		}
		return questionnaires.get(0);
	}
	
}
