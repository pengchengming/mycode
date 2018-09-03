 

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import javax.mail.MessagingException;

import com.bizduo.zflow.util.TimeUitl;


public class MailerMain {
 
	public static void main(String[] args) throws MessagingException, NumberFormatException, ParseException {
		String date= TimeUitl.getTimeLongTime(Long.parseLong(new Date().getTime()+""),"yyyy-MM-dd HH:mm:ss").toString();
		System.out.println(date);
		// TODO Auto-generated method stub ap1-cas-1.rolandberger.net
		//下面的代码在罗兰贝格测试通过
//		new Mailer("ap1-cas-1.rolandberger.net", "true", "rolandberger\\K080091", "CCM.System@rolandberger.com", "change2anew1").send(new String[] { "ITSupport_SHA@rolandberger.com" }, null, null, "Test_title--rolandberger\\K080091", "<h3>test</h3>");
		//下面的代码在罗兰贝格测试未通过
//		new Mailer("ap1-cas-1.rolandberger.net", "true", "rolandberger.com\\CCM.System", "CCM.System@rolandberger.com", "change2anew1").send(new String[] { "kenny.cao@rolandberger.com" }, null, null, "Test_title--CCM.System@rolandberger.com", "<h3>test</h3>");
//		Vector file = new Vector();
//		file.add("d:\\test1.html");
//		file.add("d:\\test2.html");
//		new Mailer("smtp.exmail.qq.com", "true", null, "test@bizduo.com", "Sh201101").send(new String[] { "lixiao@bizduo.com" , "dzt@bizduo.com"}, null, null, "Test_title--rolandberger\\K080091", "<h3>test</h3>",file);
	}

}
