package com.bizduo.zflow.service.table.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.table.IZColumnService;

@Service
public class ZColumnService extends BaseService<ZColumn, Long> implements IZColumnService {
	@SuppressWarnings("unchecked")
	public List<ZColumn> getZColumnList(String tableName) {
		try {
			PageDetachedCriteria cri=PageDetachedCriteria.forClass(ZColumn.class);
			if(tableName!=null&&!tableName.trim().equals("")){
				cri.createAlias("ztable", "ztable");
				cri.add(Restrictions.eq("ztable.tablename", tableName));
				return this.queryDao.getByDetachedCriteria(cri);
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<ZColumn> getZColumnByTableIdList(Long tableId){
		try {
			PageDetachedCriteria cri=PageDetachedCriteria.forClass(ZColumn.class);
			if(tableId!=null){				
				cri.add(Restrictions.eq("ztable.id", tableId));
				return this.queryDao.getByDetachedCriteria(cri);
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
//
	@SuppressWarnings("unchecked")
	public List<ZColumn> getZColumnListByTableId(Long tableId, String[] colNames){
		try {
			PageDetachedCriteria cri=PageDetachedCriteria.forClass(ZColumn.class);
			if(colNames!=null&&colNames.length>0){ 
				if(tableId!=null){
					cri.add(Restrictions.eq("ztable.id", tableId)); 
				}
				cri.add(Restrictions.in("colName", colNames));
				cri.addOrder(Order.asc("id"));
				return this.queryDao.getByDetachedCriteria(cri);
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
