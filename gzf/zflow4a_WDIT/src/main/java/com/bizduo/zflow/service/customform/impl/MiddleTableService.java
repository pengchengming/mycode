package com.bizduo.zflow.service.customform.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.tabulation.MiddleTable;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.customform.IMiddleTableService;
@Service
public class MiddleTableService extends BaseService<MiddleTable, Long> implements IMiddleTableService {

	@SuppressWarnings("unchecked")
	public MiddleTable findByFormIdAndMiddleTableId(Long formId, Long middleTableId) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MiddleTable.class);
		if(null != formId)
			cri.add(Restrictions.eq("form.id", formId));
		if(null != middleTableId)
			cri.add(Restrictions.eq("middleTableId", middleTableId));
		List<MiddleTable>  mtbs = super.queryDao.getByDetachedCriteria(cri);
		if(null != mtbs && 1 == mtbs.size())
			return mtbs.get(0);
		else 
			return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<MiddleTable> findByFormId(Long formId) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MiddleTable.class);
		if(null != formId)
			cri.add(Restrictions.eq("form.id", formId));
		return super.queryDao.getByDetachedCriteria(cri);
	}

	/**
	 * 保存  对象集合
	 * @param middleTables
	 */
	public void saveMiddleTables(List<MiddleTable> middleTables){
		for (MiddleTable middleTable : middleTables) {
			try {
				this.create(middleTable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
	
	/**
	 * 根据主表的名称，查询出mddleTable 
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MiddleTable> findByFormTableName(Long formId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MiddleTable.class);
		if(null != formId){
			cri.add(Restrictions.eq("form.id", formId));
			return super.queryDao.getByDetachedCriteria(cri);
		}
		else return null;
	}
}
