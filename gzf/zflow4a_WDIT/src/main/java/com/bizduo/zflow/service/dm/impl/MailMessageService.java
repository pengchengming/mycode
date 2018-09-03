package com.bizduo.zflow.service.dm.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.dm.MailMessage;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.dm.IMailMessageService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.service.table.IZTableService;
import com.bizduo.zflow.util.ExecutionSql;
import com.bizduo.zflow.util.MySQLZDialect;
import com.bizduo.zflow.util.ccm.CreateTamp;
import com.bizduo.zflow.util.ccm.MailStatus;
import com.bizduo.zflow.util.mail.ByteArrayDataSource;
@Service
public class MailMessageService extends BaseService<Object, Long> implements IMailMessageService { 
	@Autowired
	private IFormService formService;
	@Autowired
	private IZTableService ztableService; 
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	@Autowired
	private IUserService userService;
	
	public MailMessage findMailMessageById(Long Id){
		MailMessage mailMessage = (MailMessage)super.queryDao.get(MailMessage.class, Id);
		return mailMessage;
	}
	
	public void getMailMessageByAll(PageTrace pageTrace ){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MailMessage.class);  
		
		 super.queryDao.getByDetachedCriteria(cri,pageTrace,false); 
	}
	
	@SuppressWarnings("unchecked")
	public List<MailMessage> getMailMessageBy(String processrole, int status) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MailMessage.class);
		if(null != processrole){
			cri.createAlias("toUser", "toUser");
			cri.add(Restrictions.eq("toUser.processrole", processrole));
		}
		cri.add(Restrictions.eq("status", status));
		List<MailMessage> mailMessageList= super.queryDao.getByDetachedCriteria(cri); 
		return mailMessageList;
	} 
	@SuppressWarnings("unchecked")
	public List<MailMessage> findMailMessageByDataId(Long dataid){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MailMessage.class);
		if(null != dataid){ 
			cri.add(Restrictions.eq("dataId", dataid));
		} 
		List<MailMessage> mailMessageList= super.queryDao.getByDetachedCriteria(cri); 
		return mailMessageList;
	}
	public MailMessage saveMailMessage(MailMessage mailMessage) {
		// TODO Auto-generated method stub
		return (MailMessage)super.queryDao.save(mailMessage);
	}

	public MailMessage updateMailMessage(MailMessage mailMessage)  throws Exception{
		// TODO Auto-generated method stub
		return (MailMessage)super.update(mailMessage);
	}
	
	
	 private static boolean IsUTF8(byte[] buf)
	    {
	    int i;
	    byte cOctets; // octets to go in this UTF-8 encoded character
	    boolean bAllAscii = true;
	    long iLen = buf.length;
	    cOctets = 0;
	    for (i = 0; i < iLen; i++)
	    {
	    if ((buf[i] & 0x80) != 0) bAllAscii = false;
	    if (cOctets == 0)
	    {
	    if (buf[i] >= 0x80)
	    {
	    do
	    {
	    buf[i] <<= 1;
	    cOctets++;
	    }
	    while ((buf[i] & 0x80) != 0);
	    cOctets--;
	    if (cOctets == 0)
	    return false;
	    }
	    }
	    else
	    {
	    if ((buf[i] & 0xC0) != 0x80)
	    return false;
	    cOctets--;
	    }
	    }
	    if (cOctets > 0)
	    return false;
	    if (bAllAscii)
	    return false;
	    return true;
	    }
	 //认领的人
	 public  String[]  findByClaimUser( MailMessage mailMessage){ 
			String formCode= mailMessage.getFormcode();
			ZTable ztable=this.ztableService.getZTableByName(formCode);
			//查询form对应表的数据//这里要加条件
			String formTablesql= MySQLZDialect.selectData(formCode, " where id="+mailMessage.getDataId());
			System.out.println(formTablesql); 
			Map<String,String> filedValue=new HashMap<String, String>();
			filedValue.put("toPPGroupIds", "toPPGroupIds"); 
			JSONObject  jsonObj = ExecutionSql.querysqlOrCal(ztable,  jdbcTemplate,formTablesql, null,filedValue);

			try {
				if(jsonObj.getJSONArray(formCode)!=null&&jsonObj.getJSONArray(formCode).length()>0){
					for (int obji = 0; obji < jsonObj.getJSONArray(formCode).length(); obji++) { 
						JSONObject   jsonObjData = jsonObj.getJSONArray(formCode).getJSONObject(obji); 
						String toPPGroupIds= jsonObjData.getString("toPPGroupIds");
						  
						//邮件
						if(toPPGroupIds!=null&&!toPPGroupIds.trim().equals("")){
							//选择的PPGroup
							String[] userStrs=toPPGroupIds.split(","); 
							return userStrs;
						} 
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	 }

	protected static ResourceBundle MAIL_PARAMETER = ResourceBundle.getBundle("mailParameter", Locale.getDefault()); 
	//	
	//发送邮件   mailCount 所以定时任务过来的都为1，其他都为0， pp 认领，2天一提醒，一共提醒2次
	@SuppressWarnings("unused")
	public  void  updateResendMail(MailMessage mailMessage)throws Exception{  
		 String host =MAIL_PARAMETER.getString("host").trim();
		 String from =MAIL_PARAMETER.getString("from").trim();
		 String mailUser =MAIL_PARAMETER.getString("mailUser").trim();
		 String mailPassword =MAIL_PARAMETER.getString("mailPassword").trim();
				 
//      String host = "smtp.exmail.qq.com"; 
//      String from = "test@bizduo.com";
//		String mailUser="test@bizduo.com";
//	      String mailPassword="Sh201101"; 
		 
//      String host = "ap1-cas-1.rolandberger.net";  
//      String from = "CCM.System@rolandberger.com";   
//      String mailUser="rolandberger\\K080091";
//      String mailPassword="change2anew1";
      
      
        String to = mailMessage.getToUser().getEmail();  
  
        Properties props = new Properties();  
        props.put("mail.smtp.host", host);  
        props.put("mail.smtp.auth", "true");  
        props.put("mail.transport.protocol", "smtp");  
  
        Session session = Session.getInstance(props, null);  
        session.setDebug(true);  
        System.setProperty("mail.mime.charset", "utf-8");  
        MimeMessage message = new MimeMessage(session);  
        message.setSentDate(new Date());  
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[] {new InternetAddress(to)});  
           

			String  processrole=mailMessage.getProcessrole(); //mailMessage.getToUser().getProcessrole();
             
    		Map<String,String> valueMap=new HashMap<String, String>();
			String formCode= mailMessage.getFormcode();
			Form form =(Form)this.formService.getFormByCode(formCode);
			//taskid
			valueMap.put("id",mailMessage.getTaskId().toString()); 

			ZTable ztable=this.ztableService.getZTableByName(formCode);
			//查询form对应表的数据//这里要加条件
			String formTablesql= MySQLZDialect.selectData(formCode, " where id="+mailMessage.getDataId());
			System.out.println(formTablesql);
			Map<String,String> filedValue=new HashMap<String, String>();
			filedValue.put("status", "statusName");
			filedValue.put("note", "note");  
			JSONObject  jsonObj = ExecutionSql.querysqlOrCal(ztable,  jdbcTemplate,formTablesql, null,filedValue);
			//JSONObject jsonObj = new JSONObject(jsonStr);
			if(jsonObj.getJSONArray(formCode)!=null&&jsonObj.getJSONArray(formCode).length()>0){
				for (int obji = 0; obji < jsonObj.getJSONArray(formCode).length(); obji++) { 
					JSONObject   jsonObjData = jsonObj.getJSONArray(formCode).getJSONObject(obji); 
					 valueMap.put("createDate",jsonObjData.getString("createDate")); 
					 valueMap.put("clientCallId",jsonObjData.getString("id")); 
					 valueMap.put("status",jsonObjData.getString("status")); 
					 valueMap.put("userId",mailMessage.getToUser().getId().toString()); 
					//存放Map中
					for (int i = 0; i < form.getPropertyList().size(); i++) {
						FormProperty prop =  form.getPropertyList().get(i);
						String fieldName= prop.getFieldName();
						String dataVal= jsonObjData.getString(fieldName);  
						 valueMap.put(fieldName,dataVal);    
					}      
					String  infosource=valueMap.get("infosource");
					if(infosource!=null&&!infosource.trim().equals("")&&infosource.trim().equals("Others, please specify (free text)")){
						infosource=valueMap.get("infosourceOther");
						valueMap.put("infosource",infosource);
					} 
					//String title="";
					//String content="";
					 String filecontent="";
					 String fileName="";
					 //
					 Map<String,String>  titleConMap= getTiltleContent(mailMessage, valueMap);
					 String title= titleConMap.get("title");
					 String content =titleConMap.get("content");
						 if(processrole.equals("taLeader")){  
						        message.setFrom(new InternetAddress(from,title));  
						        message.setSubject(title); 
						        MimeMultipart multipart = new MimeMultipart("related");  
						        MimeBodyPart html_body = new MimeBodyPart();  
						        html_body.setContent(content,"text/html; charset=utf-8");  
						        multipart.addBodyPart(html_body); 
						        message.setContent(multipart);
						        Transport transport = session.getTransport();   
						        try {  
//						            transport.connect(host, 25, "test@bizduo.com", "Sh201101");  
						            transport.connect(host, 25, mailUser, mailPassword);

						            transport.sendMessage(message, message.getAllRecipients());  
						        } finally {  
						            transport.close();  
						        }   
						 }else { 
							 boolean isUnPPGroup=true; // false只作为提醒，不发邮件
							 //3,
							 if(mailMessage.getSendStatus()==MailStatus.UNCLAIMSTATUS){
								 isUnPPGroup=false;
								 message.setFrom(new InternetAddress(from,title));  
							        message.setSubject(title);   
							        MimeMultipart multipart = new MimeMultipart("related");  
							        MimeBodyPart html_body = new MimeBodyPart();  
							        html_body.setContent(content,"text/html; charset=utf-8");  
							        multipart.addBodyPart(html_body); 
							        message.setContent(multipart);
							        Transport transport = session.getTransport();   
							        try {  
//							            transport.connect(host, 25, "test@bizduo.com", "Sh201101");  
							            transport.connect(host, 25, mailUser, mailPassword);
							            transport.sendMessage(message, message.getAllRecipients());  
							        } finally {  
							            transport.close();  
							        } 
							 }
							 else if(mailMessage.getSendStatus()==MailStatus.PPUNSELECTSTATUS){
								 filecontent= CreateTamp.readFile("unPPGroup", valueMap);  
									 fileName ="unppGroup.html"; 
							 }
							 else if(processrole.equals("ppGroup")){  
								 filecontent= CreateTamp.readFile("ppGroup", valueMap);  
								 fileName ="ppGroup.html";   
							}else if(processrole.equals("ppFeedback")){ 
								filecontent= CreateTamp.readFile("ppFeedback", valueMap);   
								 fileName ="ppFeedback.html";  
							}
							 if(isUnPPGroup){
								 
							 
						        message.setFrom(new InternetAddress(from,title)); //标题 
						        message.setSubject(title);    
						        MimeMultipart multipart = new MimeMultipart("related");  
						        MimeBodyPart html_body = new MimeBodyPart();  
						        html_body.setContent(content,"text/html; charset=utf-8");  //正文
						        multipart.addBodyPart(html_body);  
						        
						        MimeBodyPart file = new MimeBodyPart(); 
						        DataHandler dh=null;
						        String contentType="";
								 if(IsUTF8(filecontent.getBytes())){  
						            contentType = "application/octet-stream; charset=utf-8";  
						        } else {  
						            contentType = "application/octet-stream";  
						        }  
						        dh = new DataHandler(new ByteArrayDataSource(filecontent,"text/html; charset=utf-8"));
						        file.setDataHandler(dh);
						        file.setFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));          
						        multipart.addBodyPart(file);  
						        message.setContent(multipart);
						        
						        Transport transport = session.getTransport();   
						        try {  
//						            transport.connect(host, 25, "test@bizduo.com", "Sh201101");  
						            transport.connect(host, 25, mailUser, mailPassword);
						            transport.sendMessage(message, message.getAllRecipients());  
						        } finally {  
						            transport.close();  
						        }  
							 }
						 } 				        
				}
			}  
	        
		mailMessage.setStatus(1);
		this.updateMailMessage(mailMessage); 
    
	}
	

	private   Map<String,String> getTiltleContent(MailMessage mailMessage,Map<String,String> valueMap){
		Map<String,String> titleContentMap=new HashMap<String, String>();
		String title="";
		String content="";  
		String clientName="";
		switch (mailMessage.getSendStatus()) {
		case 1: 
			clientName =valueMap.get("company");//valueMap.get("name")
			title="Incoming Call from "+valueMap.get("office")+" office - "+clientName; 
			 String Link ="";//MAIL_PARAMETER.getString("Link");
			 content="Please see information attached, and take action accordingly. <a  href='"+Link+"'>Link </a>";
			break;
		case 2:
			String userNames="";
			String[] toUserIds= this.findByClaimUser(mailMessage);
			if(toUserIds!=null&&toUserIds.length>0){
				Integer[] toUserIdLongs=new Integer[toUserIds.length];
				for (int i=0;i<toUserIds.length;i++) {
					String userId=toUserIds[i];
					toUserIdLongs[i]=Integer.parseInt(userId);
				}
				List<User> userList= this.userService.findByUserIds(toUserIdLongs);
				if(userList!=null&&userList.size()>0){
					for (User user : userList) {
						userNames=userNames+user.getFullname()+";";
					} 
				}
			}
			userNames=userNames.substring(0, userNames.length()-1);

			clientName =valueMap.get("company");//valueMap.get("name")
			title="Incoming Call from "+valueMap.get("office")+" office - "+clientName;
			String  infosource=valueMap.get("infosource");
			if(infosource!=null&&!infosource.trim().equals("")&&infosource.trim().equals("Others, please specify (free text)")){
				infosource=valueMap.get("infosourceOther");
			}
			content= "Please be noted the lead is related to "+valueMap.get("CompetenceCenter")+",  and may be led by "+userNames+". " +
					"Client received our information through "+infosource+".\r\n\r\n " +
					"Please click the Taking Lead button in the attachment, if you are interested in the case. " +
					"\r\n\r\n Thanks. "; 
			break;
		case 3: 
			String userNames1="";
			String[] toUserIds1= this.findByClaimUser(mailMessage);
			if(toUserIds1!=null&&toUserIds1.length>0){
				Integer[] toUserIdLongs=new Integer[toUserIds1.length];
				for (int i=0;i<toUserIds1.length;i++) {
					String userId=toUserIds1[i];
					toUserIdLongs[i]=Integer.parseInt(userId);
				}
				List<User> userList= this.userService.findByUserIds(toUserIdLongs);
				if(userList!=null&&userList.size()>0){
					for (User user : userList) {
						userNames1=userNames1+user.getFullname()+";";
					} 
				}
			}
			userNames1=userNames1.substring(0, userNames1.length()-1);

			clientName =valueMap.get("company");//valueMap.get("name")
			title=" Incoming Call from "+valueMap.get("office")+" office - "+clientName;
			 infosource=valueMap.get("infosource");
			if(infosource!=null&&!infosource.trim().equals("")&&infosource.trim().equals("Others, please specify (free text)")){
				infosource=valueMap.get("infosourceOther");
			}
			content= "Please be noted the lead is related to "+valueMap.get("CompetenceCenter")+",  and may be led by "+userNames1+". " +
					"Client received our information through "+infosource+"." +
					"\r\n\r\n Thanks. "; 
			break;
		case 4: 
			clientName =valueMap.get("company");//valueMap.get("name")
			title="Reminder- "+clientName+"- Acceptance needed ";
			content= "This is a reminder. " +
					"Please click the Taking Lead button in the attachment, if you are interested in the case.";
			break;
		case 5:
			clientName =valueMap.get("company");//valueMap.get("name")
			title="Reminder-"+clientName+"- Acceptance needed";
			content= clientName+" is not yet accepted after server reminders. Please take action";
			break;
		case 6:
			String userName="";
			List<MailMessage> mailMessageList=  findByDataIdAndsendStatus(mailMessage.getDataId(),7);
			if(mailMessageList!=null&&mailMessageList.size()>0){
				userName=mailMessageList.get(0).getToUser().getFullname();
			} 
			clientName =valueMap.get("company");//valueMap.get("name")
			title=" Notice- "+clientName+"- Accepted by "+userName;
			content= "The "+clientName+" is accepted by "+userName+", please contact hem/her if you are interested in this case";

			break;
		case 7:
			clientName =valueMap.get("company");//valueMap.get("name")
			title=clientName+"- Feedback needed";
			content= "Please give feedback in the attachment after you make contact with the client."; 
			break;
		case 8: 
			clientName =valueMap.get("company");//valueMap.get("name")
			title="Reminder-"+clientName+"- Feedback needed ";
			content= "This is a reminder. " +
					"Please make contact with the client, and input in the attachment.";
			break;
		}
		titleContentMap.put("title", title);
		titleContentMap.put("content", "<html><head></head>\r\n\r\n <body>"+content+"</body></html>");
		return titleContentMap;
	}

	@SuppressWarnings("unchecked")
	public List<MailMessage> findByDataIdAndsendStatus(Long dataid,int sendStatus){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MailMessage.class);
		if(null != dataid){ 
			cri.add(Restrictions.eq("dataId", dataid));
			cri.add(Restrictions.eq("sendStatus", sendStatus));

			List<MailMessage> mailMessageList= super.queryDao.getByDetachedCriteria(cri); 
			return mailMessageList;
		} 
		return null;
	}  

}
