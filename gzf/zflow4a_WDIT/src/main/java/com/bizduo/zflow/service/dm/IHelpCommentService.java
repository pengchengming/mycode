package com.bizduo.zflow.service.dm;

import com.bizduo.zflow.domain.dm.HelpComment;
import com.bizduo.zflow.service.base.IBaseService;

public interface IHelpCommentService extends IBaseService<HelpComment, Long>{
	/**
	 * 根据Id 查询
	 * @param noticeId
	 * @return
	 */
	public HelpComment getHelpCommentById(String id);
}
