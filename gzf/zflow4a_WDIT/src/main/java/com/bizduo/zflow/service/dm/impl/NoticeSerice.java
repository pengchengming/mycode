package com.bizduo.zflow.service.dm.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.dm.CCMNotice;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.dm.INoticeService;
@Service
public class NoticeSerice extends BaseService<CCMNotice, Long> implements INoticeService {
	/**
	 * 根据人员和状态查找
	 * @param userId
	 * @param types
	 * @param pageTrace
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CCMNotice> getByUserType(Integer userId, Integer[] types, PageTrace pageTrace){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(CCMNotice.class);
		if(null != userId) 
			cri.add(Restrictions.eq("readUser.id", userId));
		if(null != types && 0 < types.length)
			cri.add(Restrictions.in("userType", types));

		cri.add(Restrictions.eq("enabled", true));
		cri.addOrder(Order.desc("id"));
		if(null != pageTrace)
			return queryDao.getByDetachedCriteria(cri, pageTrace, false);
		return queryDao.getByDetachedCriteria(cri);
	}
	/**
	 * 根据人员和状态查找
	 * @param userId
	 * @param types
	 * @param pageTrace
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CCMNotice> getByUserType(Integer userId, Integer[] types, PageTrace pageTrace,Integer remindCount){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(CCMNotice.class);
		if(null != userId) 
			cri.add(Restrictions.eq("readUser.id", userId));
		if(null != types && 0 < types.length)
			cri.add(Restrictions.in("userType", types));

		//cri.add(Restrictions.eq("enabled", true));
		if(null != remindCount)
			cri.add(Restrictions.eq("remindCount", remindCount));
		cri.addOrder(Order.desc("id"));
		if(null != pageTrace)
			return queryDao.getByDetachedCriteria(cri, pageTrace, false);
		return queryDao.getByDetachedCriteria(cri);
	}


	
	@SuppressWarnings("unchecked")
	public List<CCMNotice> getByParam(Integer userId, String taskId, String type){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(CCMNotice.class);
		if(null != userId && null != taskId && !("").equals(taskId) && null != type && !("").equals(type)){ 
			cri.add(Restrictions.eq("readUser.id", userId));
			cri.add(Restrictions.eq("taskId", userId));
			cri.add(Restrictions.eq("type", type));
			cri.add(Restrictions.eq("enabled", true)); 
			return queryDao.getByDetachedCriteria(cri);
		}
		return null;
	}
	/**
	 * 根据Id 查询
	 * @param noticeId
	 * @return
	 */
	public CCMNotice getNoticeById(Long noticeId){
		CCMNotice notice = (CCMNotice)super.queryDao.get(CCMNotice.class, noticeId);
		return notice;
	} 
	/**
	 * 根据dataId和type
	 * @param dataId
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CCMNotice> getNoticeByDataIdandType(Long dataId, int type){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(CCMNotice.class);
		if(null != dataId   && !("").equals(dataId) &&  type!=0){ 
			cri.add(Restrictions.eq("dataId", dataId));
			cri.add(Restrictions.eq("userType", type)); 
			cri.add(Restrictions.eq("enabled", true));
			return queryDao.getByDetachedCriteria(cri);
		}
		return null;
	}
	/**
	 * 根据Type 和  remindCount
	 * @param pPGROUPSELECT
	 * @param remindCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CCMNotice> getNoticeByTypeRemind(int type,
			int remindCount){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(CCMNotice.class);  
			cri.add(Restrictions.eq("userType", type)); 
			cri.add(Restrictions.eq("enabled", true)); 
			cri.add(Restrictions.lt("remindCount", remindCount));
			cri.addOrder(Order.desc("taskId"));
			return queryDao.getByDetachedCriteria(cri); 
	}
	
	/**
	 * 根据人员和remindCount查找
	 * @param userId
	 * @param types
	 * @param pageTrace
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CCMNotice> getByRemindCount(Integer userId, Integer[] types,  PageTrace pageTrace ){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(CCMNotice.class);
		if(null != userId){
			cri.add(Restrictions.eq("readUser.id", userId)); 
			cri.add(Restrictions.gt("remindCount", 0));
			//cri.add(Restrictions.eq("enabled", true)); 
			if(null != types && 0 < types.length)
				cri.add(Restrictions.in("userType", types));
			cri.addOrder(Order.desc("id"));
			if(null != pageTrace)
				return queryDao.getByDetachedCriteria(cri, pageTrace, false);
			return queryDao.getByDetachedCriteria(cri);
		}
			return null;
	}
}
