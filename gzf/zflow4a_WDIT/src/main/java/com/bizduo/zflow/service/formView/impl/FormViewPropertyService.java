package com.bizduo.zflow.service.formView.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.FormViewProperty;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.formView.IFormViewPropertyService;
@Service
public class FormViewPropertyService extends BaseService<FormViewProperty, Long>  implements IFormViewPropertyService {
	
	@SuppressWarnings("unchecked")
	public List<FormViewProperty> getFormViewPropertyByCode(String code){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(FormViewProperty.class);
		if(null != code&&!code.trim().equals("")){
			cri.createAlias("formView", "formView");
			cri.add(Restrictions.eq("formView.code", code));
			cri.addOrder(Order.asc("idx"));
			return super.queryDao.getByDetachedCriteria(cri);
		}else 
			return null;
		 
	}
}
