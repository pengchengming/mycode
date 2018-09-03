package com.bizduo.zflow.service.bizType;

import java.util.List;
import java.util.Map;

import com.bizduo.zflow.domain.bizType.DataDictionaryCode;
import com.bizduo.zflow.domain.bizType.DataDictionaryType;
import com.bizduo.zflow.domain.bizType.DataDictionaryValue;
import com.bizduo.zflow.service.base.IBaseService;

public interface IDataDictionaryService extends IBaseService<Object, Long>{
	/**
	 * 获取所以类型
	 * @return
	 */
	public List<DataDictionaryType> dataDictionaryTypeAllList();
	/**
	 * 判断Code是否重复
	 * @param dictionaryTypeid
	 * @return
	 */
	public DataDictionaryType getdictionaryTypeByIdisnull(Long dictionaryTypeid,String code);
	/**
	 * 根据类型code  获取代码
	 */
	@SuppressWarnings("rawtypes")
	public List<DataDictionaryCode> getDictionaryCodeByTypeCode(Map map);
	/**
	 * 判断Code是否重复
	 * @param dictionaryTypeid
	 * @return
	 */
	public DataDictionaryCode getdictionaryCodeByIdisnull(Long dictionaryTypeid,Long dataDictionaryCodeid, String code);
	/**
	 * 根据DataDictionaryCodeID查询Value
	 */
	public List<DataDictionaryValue> getDataDictionaryValueByCode(String code);
	/**
	 * 判断Value是否重复
	 * @param dictionaryTypeid
	 * @return
	 */
	public DataDictionaryValue getdictionaryValueByIdisnull(Long dataDictionaryCodeid, Long dictionaryValueid, String code);
	
	/**
	 * 根据Value的Code查询
	 * @param dicTypeCode
	 * @param dicCode
	 * @param dicValCode
	 * @return
	 */
	public DataDictionaryValue getDataDictionaryValueById(String dicTypeCode, String dicCode, String dicValCode);
}
