package com.bizduo.zflow.service.importdata.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.importdata.ImportColumn;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.importdata.IImportColumnService;
@Service
public class ImportColumnService extends BaseService<ImportColumn, Integer> implements IImportColumnService {

	@SuppressWarnings("unchecked")
	public List<ImportColumn> getByTableID(Integer id, Integer sheet) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ImportColumn.class);
		cri.add(Restrictions.eq("importTable.id", id));
		if(null != sheet)
			cri.add(Restrictions.eq("sheet", sheet));
		return super.queryDao.getByDetachedCriteria(cri);
	}
 
}
