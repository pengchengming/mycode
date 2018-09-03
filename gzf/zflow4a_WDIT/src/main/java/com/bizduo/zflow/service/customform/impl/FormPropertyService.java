package com.bizduo.zflow.service.customform.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.customform.IFormPropertyService;
@Service
public class FormPropertyService extends BaseService<Object, Long> implements IFormPropertyService {

	//根据formId 查询出属性值
	@SuppressWarnings("unchecked")
	public  List<FormProperty> getFormPropertyListByformId(Long fromId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(FormProperty.class);
		if(null != fromId)
			cri.add(Restrictions.eq("form.id", fromId));
		
		cri.addOrder(Order.asc("fieldId"));
		cri.addOrder(Order.asc("id"));
		List<FormProperty> formProperty= super.queryDao.getByDetachedCriteria(cri); 
		return formProperty;
	}
	
	
	@SuppressWarnings("unchecked")
	public FormProperty getFormPropertyByZcolumnId(Long zcolumnId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(FormProperty.class);
		if(null != zcolumnId)
			cri.add(Restrictions.eq("zcolumn.id", zcolumnId)); 
		List<FormProperty> formPropertys= super.queryDao.getByDetachedCriteria(cri);
		if(formPropertys.size()>0)
			return formPropertys.get(0);
		else 
			return null;
	}
	
	
}
