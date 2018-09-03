package cn.pcm.utils;


import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;
  
public class SendMail {

	public static Boolean sendEmail(String email, String subject, String body) {
		Boolean flag = true;
		Properties prop = new Properties();
		// 开启debug调试，以便在控制台查看
		// prop.setProperty("mail.debug", "true");
		// 设置邮件服务器主机名
		prop.setProperty("mail.host", "smtp.qq.com");
		// 发送服务器需要身份验证
		prop.setProperty("mail.smtp.auth", "true");
		// 发送邮件协议名称
		prop.setProperty("mail.transport.protocol", "smtp");
		try {
			// 开启SSL加密，否则会失败
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			prop.put("mail.smtp.ssl.enable", "true");
			prop.put("mail.smtp.ssl.socketFactory", sf);

			// 创建session
			Session session = Session.getInstance(prop);
			// 通过session得到transport对象
			Transport ts = session.getTransport();
			// 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）
			ts.connect("smtp.qq.com", "1056214084@qq.com", "kzatyedwclwlbdgh");// 后面的字符是授权码，用qq密码反正我是失败了（用自己的，别用我的，这个号是我瞎编的，为了。。。。）
			// 创建邮件
			MimeMessage message = new MimeMessage(session);
			// 指明邮件的发件人
			message.setFrom(new InternetAddress("1056214084@qq.com"));
			// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(
					email));
			// 邮件的标题
			message.setSubject(subject);
			// 邮件的文本内容
			message.setContent(body, "text/html;charset=UTF-8");
			// 返回创建好的邮件对象
			// 发送邮件
			ts.sendMessage(message, message.getAllRecipients());
			ts.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			flag = false;
		} catch (MessagingException e) {
			e.printStackTrace();
			flag = false;
		} catch (GeneralSecurityException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;

	}
 
}