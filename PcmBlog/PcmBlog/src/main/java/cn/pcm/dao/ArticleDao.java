package cn.pcm.dao;

import java.util.List;

import cn.pcm.domain.ArticleContent;
import cn.pcm.domain.ArticleDetail;
import cn.pcm.domain.User;

public interface ArticleDao {
	public void addArticle(ArticleDetail detail, ArticleContent content);

	public int getArticleCount();

	public Boolean delArticle(int id);


	public ArticleDetail getArticle(int id);

	public List<ArticleDetail> getArticles(int start, int count);

	public ArticleContent getContent(int id);

	public List<ArticleDetail> getArticleByUSer(User user);

}
