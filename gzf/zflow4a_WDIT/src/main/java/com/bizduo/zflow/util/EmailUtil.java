package com.bizduo.zflow.util;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.HtmlEmail;

/*
 * 发送 验证邮箱
 */	
public class EmailUtil { 
	 
	public static int sendEmail(String toEmail,String path){
		try {
			ResourceBundle WB = ResourceBundle.getBundle("sysemail", Locale.getDefault());
			 String host= WB.getString("email.hostName");
			 String fromEmail= WB.getString("email.fromEmail");
			 String formName= WB.getString("email.formName");
			 String mailUser= WB.getString("email.authName"); 
			 String mailPassword = WB.getString("email.authPassword"); 
			 String subject = WB.getString("email.subject");
	        String charset = WB.getString("email.charset"); 
	        
	        Properties props = new Properties();  
	        props.put("mail.smtp.host", host);  
	        props.put("mail.smtp.auth", "true");  
	        props.put("mail.transport.protocol", "smtp");  
	        
	        Session session = Session.getInstance(props, null);  
	        session.setDebug(true);  
	        System.setProperty("mail.mime.charset", charset);  
	        MimeMessage message = new MimeMessage(session);  
	        message.setSentDate(new Date());  
	        message.setRecipients(Message.RecipientType.TO, new InternetAddress[] {new InternetAddress(toEmail)});  
	          

	        message.setFrom(new InternetAddress(fromEmail,formName));  
	        message.setSubject(subject); 
	        MimeMultipart multipart = new MimeMultipart("related");  
	        MimeBodyPart html_body = new MimeBodyPart();  
	        String msg = new StringBuffer()
			.append("<html><head><body role='listitem' tabindex='0'>Email 地址验证<br><br><p>您收到这封邮件，是由于在 公租房官网进行了邮箱绑定或修改才使用了这个邮箱地址。如果您并没有访问过 公租房官网，或没有进行上述操作，请忽略这封邮件。您不需要退订或进行其他进一步的操作。</p>")
			.append("<br>----------------------------------------------------------------------<br>")
			.append("<strong>帐号激活说明</strong><br>----------------------------------------------------------------------<br><br>")
			.append("<p>您只需点击下面的链接即可激活您的帐号：<br><a target='_blank' href='"+path+"'>"+path+"</a><br>(如果上面不是链接形式，请将该地址手工粘贴到浏览器地址栏再访问)</p><p>感谢您的访问，祝您使用愉快！</p>")
			.append("<style type='text/css'>body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}"
					+ "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}"
					+ "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}img{ border:0}header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}blockquote{margin-right:0px}</style>")
			.toString();
	        
	        html_body.setContent(msg,"text/html; charset=utf-8");  
	        multipart.addBodyPart(html_body); 
	        message.setContent(multipart);
	        Transport transport = session.getTransport();   
	        transport.connect(host, 25, mailUser, mailPassword);

	        transport.sendMessage(message, message.getAllRecipients()); 
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
        return 1;
	}
	
 //发邮件
	public static int send(String toEmail,String path){
		HtmlEmail sysemail = new HtmlEmail();
		int result;
		 ResourceBundle WB = ResourceBundle.getBundle("sysemail", Locale.getDefault());
		 String hostName= WB.getString("email.hostName");
		 String fromEmail= WB.getString("email.fromEmail");
		 String formName= WB.getString("email.formName");
		 String authName= WB.getString("email.authName"); 
		 String authPassword = WB.getString("email.authPassword"); 
		 String subject = WB.getString("email.subject");
         String charset = WB.getString("email.charset");
 
		try {
			sysemail.setHostName(hostName);
			sysemail.setCharset(charset);
			sysemail.addTo(toEmail);
			sysemail.setFrom(fromEmail, formName);
			sysemail.setAuthentication(authName, authPassword);
			sysemail.setSubject(subject);
			String msg = new StringBuffer().append("Test123456")
//					.append("<html><head><body role='listitem' tabindex='0'>Email 地址验证<br><br><p>您收到这封邮件，是由于在 公租房官网进行了邮箱绑定或修改才使用了这个邮箱地址。如果您并没有访问过 公租房官网，或没有进行上述操作，请忽略这封邮件。您不需要退订或进行其他进一步的操作。</p>")
//					.append("<br>----------------------------------------------------------------------<br>")
//					.append("<strong>帐号激活说明</strong><br>----------------------------------------------------------------------<br><br>")
//					.append("<p>您只需点击下面的链接即可激活您的帐号：<br><a target='_blank' href='"+path+"'>"+path+"</a><br>(如果上面不是链接形式，请将该地址手工粘贴到浏览器地址栏再访问)</p><p>感谢您的访问，祝您使用愉快！</p>")
//					.append("<style type='text/css'>body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}"
//							+ "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}"
//							+ "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}img{ border:0}header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}blockquote{margin-right:0px}</style>")
					.toString();
			sysemail.setMsg(msg);
			String sendresult = sysemail.send();
			result = 1;
			System.out.println("=======sendresult:"+sendresult);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("=======Mail Send Exception:"+e.getMessage());
			result = 0;
		}  
		 return result;
	}
	//系统定时任务 发给公司 审核人
	public static boolean sendbystatus(String email, String content){
		boolean result = false;
		HtmlEmail sysemail = new HtmlEmail();
		 ResourceBundle WB = ResourceBundle.getBundle("sysemail", Locale.getDefault());
		 String hostName= WB.getString("email.hostName");
		 String fromEmail= WB.getString("email.fromEmail");
		 String formName= WB.getString("email.formName");
		 String authName= WB.getString("email.authName"); 
		 String authPassword = WB.getString("email.authPassword"); 
		 String subject = "审核通知";
         String charset = WB.getString("email.charset");
         
         try {
 			sysemail.setHostName(hostName);
 			sysemail.setCharset(charset);
 			sysemail.addTo(email);
 			sysemail.setFrom(fromEmail, formName);
 			sysemail.setAuthentication(authName, authPassword);
 			sysemail.setSubject(subject);
 			String msg = new StringBuffer()
 					.append("<html><head><body role='listitem' tabindex='0'><br><br><p>"+content+"</p>")
 					.append("<br>----------------------------------------------------------------------<br>")
 					.append("<style type='text/css'>body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}"
							+ "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}"
							+ "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}img{ border:0}header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}blockquote{margin-right:0px}</style>")
 					.toString();
 			sysemail.setMsg(msg);
 			sysemail.send();
 			result = true;
 		} catch (Exception e) {
 			e.printStackTrace();
 		}  
 		 return result;
	}
	
	
}
