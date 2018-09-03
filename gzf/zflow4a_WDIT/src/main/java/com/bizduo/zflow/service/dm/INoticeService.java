package com.bizduo.zflow.service.dm;

import java.util.List;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.dm.CCMNotice;
import com.bizduo.zflow.service.base.IBaseService;


public interface INoticeService extends IBaseService<CCMNotice, Long>{
	/**
	 * 根据人员和状态查找
	 * @param userId
	 * @param types
	 * @param pageTrace
	 * @return
	 */
	public List<CCMNotice> getByUserType(Integer userId, Integer[] types, PageTrace pageTrace);
	/**
	 * 根据人员和状态查找
	 * @param userId
	 * @param types
	 * @param pageTrace
	 * @return
	 */
	public List<CCMNotice> getByUserType(Integer userId, Integer[] types, PageTrace pageTrace,Integer remindCount);
	/**
	 * 根据人员和remindCount查找
	 * @param userId
	 * @param types
	 * @param pageTrace
	 * @return
	 */
	public List<CCMNotice> getByRemindCount(Integer userId, Integer[] types,  PageTrace pageTrace);
	
	public List<CCMNotice> getByParam(Integer userId, String taskID, String type);
	/**
	 * 根据Id 查询
	 * @param noticeId
	 * @return
	 */
	public CCMNotice getNoticeById(Long noticeId);

	/**
	 * 根据dataId和type
	 * @param dataId
	 * @param i
	 * @return
	 */
	public List<CCMNotice> getNoticeByDataIdandType(Long dataId, int type);
	/**
	 * 根据Type 和  remindCount
	 * @param pPGROUPSELECT
	 * @param remindCount
	 * @return
	 */
	public List<CCMNotice> getNoticeByTypeRemind(int type,
			int remindCount);
}
