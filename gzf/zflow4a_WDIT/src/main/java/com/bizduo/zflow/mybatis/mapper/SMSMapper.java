package com.bizduo.zflow.mybatis.mapper;

import java.util.List;

import com.bizduo.zflow.mybatis.domain.SMS;
 

public interface SMSMapper {
	
	public List<SMS> findAll();
	
	public List<SMS> findSMS2Send();
	
	public int updateSMSByID(SMS sms);
}
