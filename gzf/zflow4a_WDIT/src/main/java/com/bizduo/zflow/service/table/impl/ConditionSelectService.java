package com.bizduo.zflow.service.table.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.table.ConditionSelect;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.table.IConditionSelectService;
import com.bizduo.zflow.service.table.IZTableService;
@Service
public class ConditionSelectService extends BaseService<Object, Long> implements IConditionSelectService {
	@Autowired
	private IZTableService  ztableService;
	/**
	 * 保存数据
	 */
	public  Boolean saveConDition(String tablename, String selectFields,String columnFields, String actions) {
		//查询表名
		try {
			ConditionSelect conditionSelect=new ConditionSelect();
			conditionSelect.setName(tablename);
			ZTable ztable= ztableService.getZTableByName(tablename);
			conditionSelect.setZtable(ztable);
			conditionSelect.setDescription(ztable.getDescription());
			conditionSelect.setCreateDate(new java.util.Date());
			conditionSelect.setSelectFieldList(selectFields);
			conditionSelect.setColumnFieldList(columnFields);
			conditionSelect.setActionChecks(actions);
			conditionSelect=(ConditionSelect)this.queryDao.save(conditionSelect);
			return true; 
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 根据ztable查询
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConditionSelect> getConditionSelectByZtableId(Long ztableid){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ConditionSelect.class);
 		cri.add(Restrictions.eq("ztable.id", ztableid));
 		return this.queryDao.getByDetachedCriteria(cri);
	}

	/**
	 * 根据Long conditionId查询操作
	 */
/*	public List<ConditionAction> getConditionActionByConditionId(Long conditionId){
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(ConditionAction.class);
		if(conditionId!=null){
			cri.add(Restrictions.eq("conditionSelect.id", conditionId));
			return this.queryDao.getByDetachedCriteria(cri);
		}else
			return null;
	}
	*/
}
