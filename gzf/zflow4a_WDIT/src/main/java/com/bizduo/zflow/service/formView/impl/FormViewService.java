package com.bizduo.zflow.service.formView.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.FormView;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.formView.IFormViewService;
@Service
public class FormViewService extends BaseService<FormView, Long>  implements IFormViewService {
	
	@SuppressWarnings("unchecked")
	public FormView findFormViewByCode(String code){ 
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(FormView.class);
		if(null != code&&!code.trim().equals(""))
			cri.add(Restrictions.eq("code", code));
		List<FormView>  formViewList=super.queryDao.getByDetachedCriteria(cri);
		if(formViewList!=null&&formViewList.size()>0)
			return formViewList.get(0);
		return null;
	}
	 
	@SuppressWarnings("unchecked")
	public List<FormView> findFormViewByAll(){ 
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(FormView.class);
		List<FormView>  formViewList=super.queryDao.getByDetachedCriteria(cri);
		return formViewList;
	}
	
}
