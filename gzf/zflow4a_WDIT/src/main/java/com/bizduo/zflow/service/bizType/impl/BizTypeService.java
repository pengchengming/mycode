package com.bizduo.zflow.service.bizType.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.bizType.BizType;
import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.bizType.IBizTypeService;
@Service
public class BizTypeService extends BaseService<Object, Long> implements IBizTypeService {

	@SuppressWarnings("unchecked")
	public BizType getBizTypeByCode(String code){
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(BizType.class);
		if(null != code && !code.trim().equals("")){
			cri.add(Restrictions.eq("code", code));
		}
		List<BizType> list = super.queryDao.getByDetachedCriteria(cri);
		if(list!=null&&list.size()>0)
			return list.get(0);
		return null;
	}
	/**
	 * 根据Code查询
	 * @param super.queryDao
	 */
	@SuppressWarnings("unchecked")
	public List<BizValue> getBizValuesByCode(String code){
		List<BizValue> list = null;
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(BizValue.class);
		cri.createAlias("bizType", "bizType");
		cri.add(Restrictions.eq("disable", false));
		if(null != code && !code.trim().equals("")){
			cri.add(Restrictions.eq("bizType.code", code));
		}
		list = super.queryDao.getByDetachedCriteria(cri);
		return list;
	}
	/**'
	 * 根据bizValue Name查询
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BizValue> getBizValuesByName(Long id, String name){
		List<BizValue> list = null;
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(BizValue.class);
		if(null != id && 0L != id){
			cri.add(Restrictions.eq("id", id));
		}
		if(null != name && !("".equals(name))){
			cri.add(Restrictions.eq("displayValue", name));
			list = super.queryDao.getByDetachedCriteria(cri);
		}
		return list;
	}
	/**
	 * 查询所有的常量类型
	 */
	@SuppressWarnings("unchecked")
	public List<BizType> getAllBizType(){
		return super.queryDao.getAll(BizType.class);
	}
	
	
	/**
	 * 查询所有常量值(分页)
	 */
	@SuppressWarnings("unchecked")
	public List<BizValue> getAllBizValueByPageAndBizType(Long btId, PageTrace pageTrace, Boolean disable){
		List<BizValue> list = null;
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(BizValue.class);
		if(null != btId && 0L != btId){
			cri.add(Restrictions.eq("bizType.id", btId));
		}
		if(null != disable){
			cri.add(Restrictions.eq("disable", disable));
		}
		if(null != pageTrace){
			cri.createAlias("bizType", "bizType");
			cri.addOrder(Order.desc("bizType.code"));
			list = super.queryDao.getByDetachedCriteria(cri, pageTrace, false);
		}else{
			cri.createAlias("bizType", "bizType");
			cri.addOrder(Order.desc("bizType.code"));
			list = super.queryDao.getByDetachedCriteria(cri);
		}
		return list;
	}
	
	/**
	 * 禁用常量
	 */
	public BizValue disabledBizValue(Long id){
		BizValue bizvalue = (BizValue)super.queryDao.get(BizValue.class, id);
		bizvalue.setDisable(true);
		return (BizValue)super.queryDao.save(bizvalue);
		
	}
	
	/**
	 * 查询同一类型下的常量值是否重复
	 */
	@SuppressWarnings("unchecked")
	public List<BizValue> getBizValueByBizType(Long btid, Long bvid, String displayValue){
		List<BizValue> list = null;
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(BizValue.class);
		if(null != btid && 0L != btid){
			cri.add(Restrictions.eq("bizType.id", btid));
		}
		if(null != bvid && 0L != bvid){
			cri.add(Restrictions.ne("id", bvid));
		}
		if(null != displayValue && !("".equals(displayValue))){
			cri.add(Restrictions.eq("displayValue", displayValue));
			list = super.queryDao.getByDetachedCriteria(cri);
		}
		return list;
	}
}
