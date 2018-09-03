package com.bizduo.zflow.service.dm;

import java.util.List;

import com.bizduo.zflow.domain.dm.CCMPush;
import com.bizduo.zflow.service.base.IBaseService;

public interface IPushService extends IBaseService<CCMPush, Long>{

	/**
	 * 获取enabled 的所以数据
	 * @param b
	 * @return
	 */
	List<CCMPush> getPushByEnabled(boolean enabled);

}
