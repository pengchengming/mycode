package com.bizduo.zflow.service.dm;

import java.util.List;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.dm.MailMessage;

public interface IMailMessageService {
	//根据Id查找
	public MailMessage findMailMessageById(Long Id);
	//根据状态和类型查找邮件
	public List<MailMessage>  getMailMessageBy(String processrole,int status);
	//保存
	public  MailMessage saveMailMessage(MailMessage mailMessage);
	//保存
	public  MailMessage updateMailMessage(MailMessage mailMessage) throws Exception;
	//发送邮件
	public void updateResendMail(MailMessage mailMessage)throws Exception;	 
	//认领的人
	 public  String[]  findByClaimUser( MailMessage mailMessage);
	 /**
	  * 根据DataId查询
	  * @param dataid
	  * @return
	  */
	public List<MailMessage> findMailMessageByDataId(Long dataid);
	
	public void getMailMessageByAll(PageTrace pageTrace);
}
