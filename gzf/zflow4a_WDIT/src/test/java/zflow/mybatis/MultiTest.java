package zflow.mybatis;
 

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

 


import com.bizduo.zflow.mybatis.domain.SMS;
import com.bizduo.zflow.mybatis.service.SMSService; 
 
 
/**
 * @author zhuc
 * @create 2013-3-11 下午1:47:03
 */
public class MultiTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
//		UserService userService = (UserService) ac.getBean("userService");
//		System.out.println(userService.get(1));
//		System.out.println(userService.get2(1));
		SMSService smsService =   (SMSService) ac.getBean("smsService");
		List<SMS> smslist=smsService.findAll();
		System.out.println(((SMS)smslist.get(0)).getMobile());
//		
//		BarcodeFactory.encode("http://blog.csdn.net/caofeilong20941/article/details/16962147", 300, 300, "D:\\Temp\\a.png", "D:\\Temp\\b.jpg");
//		
//		String url="http://115.29.171.5:9001/sms.aspx?action=send&userid=2107&account=goodin&password=123456&mobile=13916337676&content=你好，早上&sendTime=&taskName=旅游云&checkcontent=1&mobilenumber=10&countnumber=12&telephonenumber=1";
//		String responsecontent  = CommonUtils.httpGet(url);

//		FlowService flowService = (FlowService) ac.getBean("flowService");
//		System.out.println(flowService.get("94003d29-a7b0-42f0-839c-fa609b209ff1"));

		//		User user = new User();
		//		user.setId(100);
		//		user.setUserName("admin100");
		//		user.setPassword("password100");
		//		user.setTrueName("小李4");
		//		user.setCreateTime(new Date());
		//
		//		userService.insert(user);	//受事务管理,抛出Exception时将回滚  (rollbackFor)
	}

}
