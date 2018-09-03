package com.bizduo.zflow.service.bizType.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.bizType.DataDictionaryCode;
import com.bizduo.zflow.domain.bizType.DataDictionaryType;
import com.bizduo.zflow.domain.bizType.DataDictionaryValue;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.bizType.IDataDictionaryService;
@Service
public class DataDictionaryService extends BaseService<Object, Long> implements IDataDictionaryService {
	/**
	 * 获取所以类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataDictionaryType> dataDictionaryTypeAllList() {
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(DataDictionaryType.class);
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	/**
	 * 判断Code是否重复
	 * @param dictionaryTypeid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataDictionaryType getdictionaryTypeByIdisnull(Long dictionaryTypeid,String code){
		//查询
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(DataDictionaryType.class);
		if(dictionaryTypeid!=null&&!dictionaryTypeid.equals("")){
			cri.add(Restrictions.ne("id",dictionaryTypeid));
		}
		if(code!=null&&!code.trim().equals("")){
			cri.add(Restrictions.eq("code",code));
		}
		List<DataDictionaryType>  dataDictionaryTypeList= super.queryDao.getByDetachedCriteria(cri);
		if(dataDictionaryTypeList!=null&&dataDictionaryTypeList.size()>0){
			return dataDictionaryTypeList.get(0);
		}
		return null;
	}
	
	/**
	 * 根据 获取代码
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<DataDictionaryCode> getDictionaryCodeByTypeCode(Map map){
		//参数
		String dictionaryTypeCode=map.get("code").toString();
		 
		
		//查询
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(DataDictionaryCode.class);
		if(dictionaryTypeCode!=null&&!dictionaryTypeCode.equals("")){
			cri.createAlias("dataDictionaryType", "dataDictionaryType");
			cri.add(Restrictions.eq("dataDictionaryType.code", dictionaryTypeCode));
		}
		if(map.get("dictionaryCode")!=null&&!map.get("dictionaryCode").toString().trim().equals(""))
			cri.add(Restrictions.ilike("code", map.get("dictionaryCode").toString(), MatchMode.ANYWHERE));
		if( map.get("dictionaryValue")!=null&&! map.get("dictionaryValue").toString().trim().equals(""))
			cri.add(Restrictions.ilike("display",  map.get("dictionaryValue").toString(), MatchMode.ANYWHERE));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	/**
	 * 判断Code是否重复
	 * @param dictionaryTypeid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataDictionaryCode getdictionaryCodeByIdisnull(Long dictionaryTypeid,Long dataDictionaryCodeid, String code){
		//查询
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(DataDictionaryCode.class);
		if(dictionaryTypeid!=null&&!dictionaryTypeid.equals("")){
			cri.add(Restrictions.eq("dataDictionaryType.id",dictionaryTypeid));
		}
		if(dataDictionaryCodeid!=null&&!dataDictionaryCodeid.equals("")){
			cri.add(Restrictions.ne("id",dataDictionaryCodeid));
		}
		if(code!=null&&!code.trim().equals("")){
			cri.add(Restrictions.eq("code",code));
		}
		List<DataDictionaryCode>  dataDictionaryCodeList= super.queryDao.getByDetachedCriteria(cri);
		if(dataDictionaryCodeList!=null&&dataDictionaryCodeList.size()>0){
			return dataDictionaryCodeList.get(0);
		}
		return null;
	}
	/**
	 * 根据DataDictionaryCodeID查询Value
	 */
	@SuppressWarnings("unchecked")
	public List<DataDictionaryValue> getDataDictionaryValueByCode(String code) {
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(DataDictionaryValue.class);
		if(code!=null&&!code.equals("")){
			cri.createAlias("dataDictionaryCode", "dataDictionaryCode");
			cri.add(Restrictions.eq("dataDictionaryCode.code", code));
		}
		cri.addOrder(Order.asc("ordinal"));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	/**
	 * 判断Value是否重复
	 * @param dictionaryTypeid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataDictionaryValue getdictionaryValueByIdisnull(Long dataDictionaryCodeid, Long dictionaryValueid, String code){
		//查询
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(DataDictionaryValue.class);
		if(dataDictionaryCodeid!=null&&!dataDictionaryCodeid.equals("")){
			cri.add(Restrictions.eq("dataDictionaryCode.id",dataDictionaryCodeid));
		}
		if(dictionaryValueid!=null&&!dictionaryValueid.equals("")){
			cri.add(Restrictions.ne("id",dictionaryValueid));
		}
		if(code!=null&&!code.trim().equals("")){
			cri.add(Restrictions.eq("value",code));
		}
		List<DataDictionaryValue>  dataDictionaryValueList= super.queryDao.getByDetachedCriteria(cri);
		if(dataDictionaryValueList!=null&&dataDictionaryValueList.size()>0){
			return dataDictionaryValueList.get(0);
		}
		return null;
	}
	

	/**
	 * 根据Value的Code查询
	 * @param dicTypeCode
	 * @param dicCode
	 * @param dicValCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataDictionaryValue getDataDictionaryValueById(String dicTypeCode, String dicCode, String dicValCode){
		//查询
		try {
			PageDetachedCriteria cri=PageDetachedCriteria.forClass(DataDictionaryValue.class);
			cri.createAlias("dataDictionaryCode", "dataDictionaryCode");
			cri.add(Restrictions.eq("dataDictionaryCode.code", dicCode)); 
			cri.createAlias("dataDictionaryCode.dataDictionaryType", "dataDictionaryType");
			cri.add(Restrictions.eq("dataDictionaryType.code", dicTypeCode)); 
			cri.add(Restrictions.eq("value",dicValCode)); 
			List<DataDictionaryValue>  dataDictionaryValueList= super.queryDao.getByDetachedCriteria(cri);
			if(dataDictionaryValueList!=null&&dataDictionaryValueList.size()>0){
				return dataDictionaryValueList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return null;
	}
}
