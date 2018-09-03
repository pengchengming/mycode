package com.bizduo.zflow.service.table;

import java.util.List;

import com.bizduo.zflow.domain.table.ConditionSelect;
import com.bizduo.zflow.service.base.IBaseService;

public interface IConditionSelectService extends IBaseService<Object, Long> {
/**
 * 保存数据
 * @param actions 
 */
	public  Boolean saveConDition(String tablename, String selectFields,String columnFields, String actions);

	/**
	 * 根据ztable查询
	 * @param id
	 * @return
	 */
	public List<ConditionSelect> getConditionSelectByZtableId(Long ztableid);

	/**
	 * 根据Long conditionId查询操作
	 */
	//public List<ConditionAction> getConditionActionByConditionId(Long conditionId);
}
