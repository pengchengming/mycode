package com.bizduo.zflow.service.zsurvey.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.zsurvey.AnswerSheet;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.zsurvey.IAnswerSheetService;

/**
 * 答卷记录 - Service
 * 
 * @author zs
 *
 */
@Service
public class AnswerSheetServiceImpl extends BaseService<AnswerSheet, Integer> implements IAnswerSheetService{

	@SuppressWarnings("unchecked")
	public AnswerSheet getByDate(String date) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(AnswerSheet.class);
		cri.add(Restrictions.eq("create_time", date));
		List<AnswerSheet> answerSheets = super.queryDao.getByDetachedCriteria(cri);
		if(answerSheets.size()<=0){
			return null;
		}
		return answerSheets.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<AnswerSheet> getByQuesnareIds() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(AnswerSheet.class);
		cri.add(Restrictions.eq("states", 0));
		cri.add(Restrictions.eq("condition", 1));
		List<AnswerSheet> answerSheets = super.queryDao.getByDetachedCriteria(cri);
		if(answerSheets.size()<=0){
			return null;
		}
		return answerSheets;
	}

	@SuppressWarnings("unchecked")
	public List<AnswerSheet> getByStates() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(AnswerSheet.class);
		cri.add(Restrictions.eq("states", 1));
		cri.addOrder(Order.desc("id"));
		List<AnswerSheet> answerSheets = super.queryDao.getByDetachedCriteria(cri);
		if(answerSheets.size()<=0){
			return null;
		}
		return answerSheets;
	}

	@SuppressWarnings("unchecked")
	public AnswerSheet getByQuesnareId(Integer id) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(AnswerSheet.class);
		cri.add(Restrictions.eq("questionnaire.id", id));
		List<AnswerSheet> answerSheets = super.queryDao.getByDetachedCriteria(cri);
		if(answerSheets.size()<=0){
			return null;
		}
		return answerSheets.get(0);
	}

	@SuppressWarnings("unchecked")
	public AnswerSheet findByCode(Class<AnswerSheet> class1,
			String answersheetCode) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(AnswerSheet.class);
		cri.add(Restrictions.eq("code", answersheetCode));
		List<AnswerSheet> answerSheets = super.queryDao.getByDetachedCriteria(cri);
		if(answerSheets.size()<=0){
			return null;
		}
		return answerSheets.get(0);	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getAnswerSheetScore(int answersheetid) {
		// TODO Auto-generated method stub
		
		Integer param2Value = (Integer) jdbcTemplate.execute(   
				     new CallableStatementCreator() {   
							public CallableStatement createCallableStatement(
									Connection con) throws SQLException { 
				           String storedProc = "{call usp_prg_GetAnswerSheetScore(?,?)}";// 调用的sql   
				           CallableStatement cs = con.prepareCall(storedProc);   
				           cs.setInt(1, 1);// 设置输入参数的值   
				           cs.registerOutParameter(2,Types.INTEGER);// 注册输出参数的类型   
				           return cs;   
				        }
				     }, new CallableStatementCallback() {   
				         public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
				           cs.execute(); 
				           return cs.getInt(2);// 获取输出参数的值   
				     }   
				  });
		
		return param2Value.intValue();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List  getAnswerSheetList() {
		// TODO Auto-generated method stub
		
		List resultlist = (List) jdbcTemplate.execute(   
				     new CallableStatementCreator() {   
							public CallableStatement createCallableStatement(
									Connection con) throws SQLException { 
				           String storedProc = "{call usp_prg_GetAnswerSheetList(?)}";// 调用的sql   
				           CallableStatement cs = con.prepareCall(storedProc);   
				           cs.setInt(1, 1);// 设置输入参数的值   
				           return cs;   
				        }
				     }, new CallableStatementCallback() {   
				         public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
				           cs.execute(); 
				           ResultSet resultset =cs.getResultSet();
				           List resultlist = getResultSet(resultset);
				           return resultlist ;// 获取输出参数的值   
				     }   
				  });
		return resultlist;
	}
	
	
  
	
}
