package com.bizduo.zflow.service.exportdata.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.exportdata.ExportProcedure;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.exportdata.IExportProcedureService;
@Service
public class ExportProcedureService extends BaseService<ExportProcedure, Integer> implements IExportProcedureService {
	
	@SuppressWarnings("unchecked")
	public List<ExportProcedure> getByExcelId(Integer excelId) {
		// TODO Auto-generated method stub
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ExportProcedure.class);
		cri.add(Restrictions.eq("exportExcel.id", excelId));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
}
