package com.bizduo.zflow.service.sys;

import com.bizduo.zflow.domain.sys.PersistentLogins;
import com.bizduo.zflow.service.base.IBaseService;

public interface IPersistentLoginsService extends IBaseService<PersistentLogins, Long>{

	public PersistentLogins getByToken(String token);
}
