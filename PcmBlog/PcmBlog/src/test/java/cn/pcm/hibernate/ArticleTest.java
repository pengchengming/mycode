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
public class ArticleTest {
	@Autowired
	@Qualifier("articleService")
	private ArticleService articleService;

	@Test
	public void delArticle(){
		articleService.delArticle(2);
	}
  
}
