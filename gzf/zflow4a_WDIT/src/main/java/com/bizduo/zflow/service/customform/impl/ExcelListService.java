package com.bizduo.zflow.service.customform.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.ExcelList;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.customform.IExcelListService;

@Service
public class ExcelListService extends BaseService<ExcelList, Long> implements IExcelListService {

	@SuppressWarnings("unchecked")
	public  List<ExcelList> findByTableId(Long id){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ExcelList.class);
		if(null != id)
			cri.add(Restrictions.eq("excelTableList.id", id));
		cri.addOrder(Order.asc("idx"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
}
