package com.bizduo.zflow.service.customform;

import java.util.Collection;
import java.util.List;

import com.bizduo.zflow.domain.tabulation.MiddleTable;
import com.bizduo.zflow.service.base.IBaseService;

public interface IMiddleTableService extends IBaseService<MiddleTable, Long> {

	/**
	 * 根据formId 和 当前MiddleTable的middleTableId查询
	 * @param formId
	 * @param middleTableId 该ID为前台生成用,在对象保存前就已经存在
	 * @return
	 */
	public MiddleTable findByFormIdAndMiddleTableId(Long formId,Long middleTableId);
	

	/**
	 * 根据formId 查询
	 * @param formId
	 * @return
	 */
	public Collection<MiddleTable> findByFormId(Long formId);

	/**
	 * 保存  对象集合
	 * @param middleTables
	 */
	public void saveMiddleTables(List<MiddleTable> middleTables);

	/**
	 * 根据主表的名称，查询出mddleTable 
	 * @param formId
	 * @return
	 */
	public List<MiddleTable> findByFormTableName(Long formId);
}
