package com.bizduo.zflow.service.dm.impl;

import org.springframework.stereotype.Service;

import com.bizduo.zflow.domain.dm.HelpComment;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.dm.IHelpCommentService;
@Service
public class HelpCommentService extends BaseService<HelpComment, Long> implements IHelpCommentService {
	/**
	 * 根据Id 查询
	 * @param noticeId
	 * @return
	 */
	public HelpComment getHelpCommentById(String id){
		HelpComment helpComment = (HelpComment)super.queryDao.get(HelpComment.class, id);
		return helpComment;
	} 
}
