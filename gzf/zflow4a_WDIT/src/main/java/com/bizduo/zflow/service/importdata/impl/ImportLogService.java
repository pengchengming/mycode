package com.bizduo.zflow.service.importdata.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.importdata.ImportLog;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.importdata.IImportLogService;
@Service
public class ImportLogService extends BaseService<ImportLog, Integer> implements IImportLogService {

	@SuppressWarnings("unchecked")
	public List<ImportLog> getByParam(Integer id, String batchNo) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ImportLog.class);
		cri.add(Restrictions.eq("batchNo", batchNo));
		cri.add(Restrictions.gt("id", id));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
}
