 package com.bizduo.zflow.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import microsoft.exchange.webservices.data.BodyType;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.EmailMessageSchema;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.LogicalOperator;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;

/**
 * @author superman
 * */

public class ExchangeMail {

	private static ExchangeService service;
	
	/**
	 * 初始化连接
	 * */
	private static void init(String host, String mailUser,String mailPassword){
		service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
		ExchangeCredentials credentials = new WebCredentials(mailUser,mailPassword);
		service.setCredentials(credentials);
		try {
			service.setUrl(new URI("https://" + host + "/ews/exchange.asmx"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 使用Exchange协议发送
	 * @param subject 邮件主题
	 * @param to  收件人
	 * @param cc  抄送
	 * @param bobytext  正文
	 * @param realPath  附件
	 * 
	 * @throws Exception
	 */
	public static void doSend(String subject, List<String> to, List<String> cc,
			String bodyText, String realPath) throws Exception {

		EmailMessage msg = new EmailMessage(service);
		msg.setSubject(subject);
		MessageBody body = MessageBody.getMessageBodyFromText(bodyText);
		body.setBodyType(BodyType.HTML);
		msg.setBody(body);
		for (String s : to) {
			msg.getToRecipients().add(s);
		}
		// for (String s : cc) {
		// msg.getCcRecipients().add(s);
		// }
		if (realPath != null && !"".equals(realPath)) {
			msg.getAttachments().addFileAttachment(realPath);
		}
		msg.send();
	}
	
	public static void send(String subject, List<String> to, List<String> cc,
			String bodyText) throws Exception {
		doSend(subject, to, cc, bodyText, null);
	}
	
	/**
	 * 使用Exchange协议接收邮件
	 * 
	 * @throws Exception
	 */
	
	public static void GetUnreadEmails() throws Exception {
		ItemView view = new ItemView(Integer.MAX_VALUE);
		List<String> list = new ArrayList<String>();
		FindItemsResults<Item> unRead = Folder.bind(service,WellKnownFolderName.Inbox).findItems(SetFilter(), view);
		for (Item item : unRead.getItems()) {
			if (item.getSubject() != null) {
				list.add(item.getSubject().toString());
			} else {
				list.add("无标题");
			}
			// list.Add(item.DateTimeSent.ToString());
		}
		// int unRead=Folder.bind(service, WellKnownFolderName.Inbox).getUnreadCount();  //未读邮件数量
		// System.out.println(unRead);
	}

	/**
	 * 设置获取什么类型的邮件
	 * 
	 * @throws Exception
	 */
	
	private static SearchFilter SetFilter() {
		List<SearchFilter> searchFilterCollection = new ArrayList<SearchFilter>();
		searchFilterCollection.add(new SearchFilter.IsEqualTo(
				EmailMessageSchema.IsRead, false));
		SearchFilter s = new SearchFilter.SearchFilterCollection(
				LogicalOperator.And,searchFilterCollection);

		// 如果要获取所有邮件的话代码改成这样：
//		 searchFilterCollection.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
//		 searchFilterCollection.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, true));
//		 SearchFilter s = new SearchFilter.SearchFilterCollection(LogicalOperator.Or,searchFilterCollection);

		return s;
	}

	public static int  sendEmail(String toEmail,String path) {
		
		try {

			ResourceBundle WB = ResourceBundle.getBundle("sysemail", Locale.getDefault());
			 String host= WB.getString("email.hostName");
			 //String fromEmail= WB.getString("email.fromEmail");
			 String formName= WB.getString("email.formName");
			 String mailUser= WB.getString("email.authName"); 
			 String mailPassword = WB.getString("email.authPassword"); 
			 //String subject = WB.getString("email.subject");
	        //String charset = WB.getString("email.charset"); 
	        
	        init(host,mailUser,mailPassword);
			List<String> to = new ArrayList<String>();
			to.add(toEmail);
			  
			 String msg = new StringBuffer()
				.append("<html><head><body role='listitem' tabindex='0'>Email 地址验证<br><br><p>您收到这封邮件，是由于在 公租房官网进行了邮箱绑定或修改才使用了这个邮箱地址。如果您并没有访问过 公租房官网，或没有进行上述操作，请忽略这封邮件。您不需要退订或进行其他进一步的操作。</p>")
				.append("<br>----------------------------------------------------------------------<br>")
				.append("<strong>帐号激活说明</strong><br>----------------------------------------------------------------------<br><br>")
				.append("<p>您只需点击下面的链接即可激活您的帐号：<br><a target='_blank' href='"+path+"'>"+path+"</a><br>(如果上面不是链接形式，请将该地址手工粘贴到浏览器地址栏再访问)</p><p>感谢您的访问，祝您使用愉快！</p>")
				.append("<style type='text/css'>body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}"
						+ "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}"
						+ "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}img{ border:0}header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}blockquote{margin-right:0px}</style>")
				.toString();
			 
			send(formName,to,null,msg);
			GetUnreadEmails(); 
		} catch (Exception e) {
			e.printStackTrace();
			return 0;			
			// TODO: handle exception
		} 
		return 1;
	}
	
	
	//系统定时任务 发给公司 审核人
	public static  boolean sendbystatus(String toEmail, String content) {
		try {
			ResourceBundle WB = ResourceBundle.getBundle("sysemail", Locale.getDefault());
			 String host= WB.getString("email.hostName");
//			 String fromEmail= WB.getString("email.fromEmail");
			 String formName= WB.getString("email.formName");
			 String mailUser= WB.getString("email.authName"); 
			 String mailPassword = WB.getString("email.authPassword"); 
//			 String subject = WB.getString("email.subject");
//	         String charset = WB.getString("email.charset"); 
	      
	         init(host,mailUser,mailPassword);
			 List<String> to = new ArrayList<String>();
			 to.add(toEmail);
			 
			send(formName,to,null,content);
			GetUnreadEmails(); 
		  } catch (Exception e) {
			e.printStackTrace();
			return false;			
			// TODO: handle exception
		} 
		return true;
	}
	
	//系统定时任务 发给公司 审核人 ---发给房管局
		public static  boolean sendtohouse(List<String> emails, String content) {
			try {
				 ResourceBundle WB = ResourceBundle.getBundle("sysemail", Locale.getDefault());
				 String host= WB.getString("email.hostName");
//				 String fromEmail= WB.getString("email.fromEmail");
				 String formName= WB.getString("email.formName");
				 String mailUser= WB.getString("email.authName"); 
				 String mailPassword = WB.getString("email.authPassword"); 
//				 String subject = WB.getString("email.subject");
//		         String charset = WB.getString("email.charset"); 
		      
		         init(host,mailUser,mailPassword);
				 send(formName,emails,null,content);
				GetUnreadEmails(); 
			  } catch (Exception e) {
				e.printStackTrace();
				return false;			
				// TODO: handle exception
			} 
			return true;
		}
	
	
		//系统定时任务 发给公司 审核人 ---发给房管局
	public static  Map<String,Integer> sendbymap(Map<String,String> emaillists) {
		Map<String,Integer> resultmap = new HashMap<String, Integer>();
	    	     ResourceBundle WB = ResourceBundle.getBundle("sysemail", Locale.getDefault());
				 String host= WB.getString("email.hostName");
				 String formName= WB.getString("email.formName");
				 String mailUser= WB.getString("email.authName"); 
				 String mailPassword = WB.getString("email.authPassword"); 
		         for (String email : emaillists.keySet()) {
					 String content = emaillists.get(email);
					 List<String> to = new ArrayList<String>();
					 to.add(email);
					 try {
						 init(host,mailUser,mailPassword);
						 send(formName,to,null,content);
						 GetUnreadEmails(); 
						 resultmap.put(email, 2);	
					} catch (Exception e) {
						resultmap.put(email, 1);	
					} 
				}
	      return resultmap;
	}
		
	
	
}
