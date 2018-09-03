package com.bizduo.zflow.service.importdata.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.importdata.ImportTable;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.importdata.IImportTableService;
@Service
public class ImportTableService extends BaseService<ImportTable, Integer> implements IImportTableService {

	@SuppressWarnings("unchecked")
	public ImportTable getByName(String name) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ImportTable.class);
		cri.add(Restrictions.eq("name", name));
		List<ImportTable> objs = super.queryDao.getByDetachedCriteria(cri);
		return 0 < objs.size() ? objs.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public ImportTable getBySheetName(String tempSheetName) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ImportTable.class);
		cri.add(Restrictions.eq("sheetName", tempSheetName));
		List<ImportTable> objs = super.queryDao.getByDetachedCriteria(cri);
		return 0 < objs.size() ? objs.get(0) : null;
	}
}
