package com.bizduo.zflow.service.bizType;

import java.util.List;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.bizType.BizType;
import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.service.base.IBaseService;

public interface IBizTypeService extends IBaseService<Object, Long>{

	/**
	 * 根据Code查询
	 * @param category
	 * @return
	 */
	BizType getBizTypeByCode(String code);
	/**
	 * 根据Code查询
	 * @param category
	 * @return
	 */
	List<BizValue> getBizValuesByCode(String code);
	/**
	 * 根据bizValue Name查询
	 * @param name
	 * @return
	 */
	List<BizValue> getBizValuesByName(Long id, String name);
	
	/**
	 * 查询所有的常量类型
	 * @return List<BizType> 常量类型集合
	 */
	public List<BizType> getAllBizType();
	/**
	 * 查询所有常量值(分页)
	 * @param btId 常量类型ID
	 * @param pageTrace 分页添加
	 * @return List<BizValue> 常量值集合
	 */
	public List<BizValue> getAllBizValueByPageAndBizType(Long btId, PageTrace pageTrace, Boolean disable);
	/**
	 * 禁用常量
	 */
	public BizValue disabledBizValue(Long id);

	/**
	 * 查询同一类型下的常量值是否重复
	 */
	public List<BizValue> getBizValueByBizType(Long btid, Long bvid, String displayValue);
}
