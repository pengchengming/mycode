package cn.pcm.hibernate;

import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.pcm.domain.ArticleContent;
import cn.pcm.domain.ArticleDetail;
import cn.pcm.service.ArticleService;

@ContextConfiguration(locations = { "classpath*:spring/applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class Dome1 {

	@Autowired
	@Qualifier("articleService")
	private ArticleService articleService;

	public void test1() {
		ArticleDetail detail = new ArticleDetail();
		ArticleContent content = new ArticleContent();
		detail.setAuthor("佚名");
		detail.setDatetime(new Timestamp(System.currentTimeMillis()));
		detail.setTitle("咋好难过三");
		detail.setClassify("科技类");
		content.setContent(Hibernate.createBlob("sssss".getBytes()));
		for (int i = 0; i < 100; i++)
			articleService.addArticle(detail, content);
	}

	@Test
	public void test2() {
		int count = articleService.getArticleCount();
		System.out.println("\n---------------------------\n" + count
				+ "\n---------------------------\n");
	}

	@Test
	public void test3() {
		List<ArticleDetail> list = articleService.getArticles(0, 10);
		for (ArticleDetail detail : list) {
			System.out.println(detail.getId());
		}
	}

	@Test
	public void test4() {
		ArticleContent content = articleService.getContent(105);
		Blob blob = content.getContent();
		try {
			String str = new String(
					blob.getBytes((long) 1, (int) blob.length()));
			System.out.println("\n---------------------------\n" + str
					+ "\n---------------------------\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test5(){
		ArticleDetail detail=articleService.getArticle(200);
		System.out.println(detail.getAuthor());
		System.out.println(detail.getId());
		System.out.println(detail.getTitle());
		System.out.println(detail.getDatetime());
		System.out.println(detail.getClassify());
	}
	
}
