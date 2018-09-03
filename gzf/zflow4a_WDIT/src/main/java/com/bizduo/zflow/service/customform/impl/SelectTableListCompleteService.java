package com.bizduo.zflow.service.customform.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.IQueryDao;
import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.SelectConditionComplete;
import com.bizduo.zflow.domain.form.SelectListComplete;
import com.bizduo.zflow.domain.form.SelectTableListComplete;
import com.bizduo.zflow.service.customform.ISelectTableListCompleteService;
import com.bizduo.zflow.util.SelectToJson;

@Service
public class SelectTableListCompleteService implements ISelectTableListCompleteService {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	public IQueryDao queryDao;
	
	public SelectTableListComplete createSelectTableListComplete(
			SelectTableListComplete selectTableListComplete) throws Exception { 
		return ((SelectTableListComplete)queryDao.save(selectTableListComplete)); 
	}
	public SelectListComplete createSelectListComplete( SelectListComplete selectListComplete) throws Exception {
		return ((SelectListComplete)queryDao.save(selectListComplete));
	}
	public SelectConditionComplete createSelectConditionComplete(SelectConditionComplete selectConditionComplete) throws Exception {
		return ((SelectConditionComplete)queryDao.save(selectConditionComplete));
	}

	
	@SuppressWarnings("unchecked")
	public SelectTableListComplete findByCode(String code){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(SelectTableListComplete.class);
		if(null != code&&!code.trim().equals(""))
			cri.add(Restrictions.eq("code", code));
		List<SelectTableListComplete>  selectTableListCompleteList=queryDao.getByDetachedCriteria(cri);
		if(selectTableListCompleteList!=null&&selectTableListCompleteList.size()>0)
			return selectTableListCompleteList.get(0);
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public  List<SelectListComplete> findByTableId(Long id){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(SelectListComplete.class);
		if(null != id)
			cri.add(Restrictions.eq("selectTableListComplete.id", id));
		cri.addOrder(Order.asc("idx"));
		return queryDao.getByDetachedCriteria(cri);
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectConditionComplete> findSelectConditionCompleteByTableId(Long id){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(SelectConditionComplete.class);
		if(null != id)
			cri.add(Restrictions.eq("selectTableListComplete.id", id)); 
		cri.addOrder(Order.asc("idx"));
		return queryDao.getByDetachedCriteria(cri);
	}
	
	public SelectTableListComplete findbyId(Class<SelectTableListComplete> class1,Long id){
		return  (SelectTableListComplete) queryDao.get(class1, id);
	}
	
	public  String selectBySqlPage(SelectTableListComplete selectTableListComplete, String selectConditionSql,PageTrace pageTrace){

		String selectTableSql=selectTableListComplete.getSelectTableSql();
		String selectFromSql= selectTableListComplete.getSelectFromSql();
		String orderBy= selectTableListComplete.getOrderBy();
		String groupBy= selectTableListComplete.getGroupBy();
		 
		Map<String,String> sqlMap=new HashMap<String, String>(); 
		if(StringUtils.isNotBlank(selectConditionSql)) {
			selectTableSql += selectConditionSql ; 
		}
		if(StringUtils.isNotBlank(selectFromSql) ) {
			if(StringUtils.isNotBlank(selectConditionSql))
				selectFromSql += selectConditionSql ;
			if(StringUtils.isNotBlank(groupBy))
				selectFromSql +=" "+groupBy;
			sqlMap.put("sqlForm",selectFromSql); 
		}
		if(StringUtils.isNotBlank(groupBy) ) {
			sqlMap.put("groupBy",groupBy);
			selectTableSql +=" "+groupBy;
		}
		if(orderBy!=null&&!orderBy.trim().equals(""))
			selectTableSql +=" "+ orderBy  ;
		List<SelectListComplete> selectListComplete=  findByTableId(selectTableListComplete.getId());

		Map<String,String> fieldMap=new HashMap<String, String>(); 
		if(selectListComplete!=null){
			for (SelectListComplete selectListtemp : selectListComplete) {
				fieldMap.put(selectListtemp.getAliasesName(), selectListtemp.getAliasesName()); 
			}
		}		
		sqlMap.put("sql",selectTableSql);
		try {
			return  SelectToJson.jdbcTemplateQuerysql(jdbcTemplate, sqlMap, fieldMap, pageTrace).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
