package com.bizduo.zflow.service.table.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.table.IZTableService;
@Service
public class ZTableService extends BaseService<Object, Long> implements IZTableService {

	/**
	 * 根据tablename查询
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ZTable getZTableByName(String tableName){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ZTable.class);
 		cri.add(Restrictions.eq("tablename", tableName));
 		List<ZTable> ztableList= this.queryDao.getByDetachedCriteria(cri);
 		if(ztableList!=null&&ztableList.size()>0){
 			return ztableList.get(0); 
 		}
 		return null;
	}
	@SuppressWarnings("unchecked")
	public List<ZTable> getTableAll() {
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(ZTable.class);
		return this.queryDao.getByDetachedCriteria(cri);
	}
}
