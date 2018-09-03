package cn.pcm.hibernate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.pcm.domain.User;
import cn.pcm.service.ArticleService;
import cn.pcm.service.UserService;
import cn.pcm.utils.MD5Util;

@ContextConfiguration(locations = { "classpath*:spring/applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTest {
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Test
	public void getUserTest() {
		User user = userService.getUser("zhangsan", "123");
		System.out.println(user == null);
		System.out.println(user!=null);
		if (user != null) {
			
			System.out.println(user.getId());
			System.out.println(user.getUserName());
		} else {
			System.out.println("用户不存在！-");
		}

	}

	
	public void addUser() {
		User user = new User();
		user.setEmail("11");

		user.setPassword(MD5Util.string2MD5("123"));
		System.out.println(user.getPassword());
		System.out.println(MD5Util.convertMD5(MD5Util.convertMD5(user
				.getPassword())));

		user.setUserName("zhangsan");
		userService.addUser(user);
	}
}
