package com.bizduo.zflow.service.formView.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.ViewPageProperty;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.formView.IViewPagePropertyService;

@Service
public class ViewPagePropertyService extends BaseService<ViewPageProperty, Long>  implements IViewPagePropertyService {

	@SuppressWarnings("unchecked")
	public List<ViewPageProperty> getViewPagePropertyByCode(String code){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ViewPageProperty.class);
		if(null != code&&!code.trim().equals("")){
			cri.createAlias("viewPage", "viewPage");
			cri.add(Restrictions.eq("viewPage.code", code));
		}
		List<ViewPageProperty>  viewPropertyList=super.queryDao.getByDetachedCriteria(cri);
		return viewPropertyList;
	}
}
