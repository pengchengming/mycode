package com.bizduo.zflow.service.exportdata.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.exportdata.ExportExcel;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.exportdata.IExportExcelService;
@Service
public class ExportExcelService extends BaseService<ExportExcel, Integer> implements IExportExcelService {
	
	@SuppressWarnings("unchecked")
	public ExportExcel getByName(String name){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ExportExcel.class);
		cri.add(Restrictions.eq("name", name));
		List<ExportExcel> c = super.queryDao.getByDetachedCriteria(cri);
		if(null != c && 0 < c.size())
			return c.get(0);
		return null;
	}
}
