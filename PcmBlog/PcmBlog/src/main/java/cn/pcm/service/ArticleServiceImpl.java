package cn.pcm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.pcm.dao.ArticleDao;

import cn.pcm.domain.ArticleContent;
import cn.pcm.domain.ArticleDetail;
import cn.pcm.domain.User;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService{

	@Resource(name="articleDao")
	private ArticleDao articleDao;

	
	public void setUserDao(ArticleDao articleDao) {
		this.articleDao =  articleDao;
	}
	
	@Override
	public void addArticle(ArticleDetail detail,ArticleContent content) {
		 articleDao.addArticle(detail,content);
	}

	@Override
	public int getArticleCount() {
		// TODO Auto-generated method stub
		return articleDao.getArticleCount();
	}

	@Override
	public Boolean delArticle(int id) {
		// TODO Auto-generated method stub
		return articleDao.delArticle(id);
	}

	

	@Override
	public List<ArticleDetail> getArticles(int start, int count) {
		// TODO Auto-generated method stub
		return articleDao.getArticles(start, count);
	}

	@Override
	public ArticleContent getContent(int id) {
		// TODO Auto-generated method stub
		return articleDao.getContent(id);
	}

	@Override
	public ArticleDetail getArticle(int id) {
		// TODO Auto-generated method stub
		return articleDao.getArticle(id);
	}

	@Override
	public List<ArticleDetail> getArticleByUSer(User user) {
		return articleDao.getArticleByUSer(user);
	}

}
