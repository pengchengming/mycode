package cn.pcm.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.pcm.domain.ArticleContent;
import cn.pcm.domain.ArticleDetail;
import cn.pcm.domain.User;

@Repository("articleDao")
public class ArticleDaoImpl implements ArticleDao {

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public void addArticle(ArticleDetail detail, ArticleContent content) {
		sessionFactory.getCurrentSession().save(detail);
		sessionFactory.getCurrentSession().save(content);

	}

	@Override
	public int getArticleCount() {
		String countHql = "select count(*) from ArticleDetail a where a.status='1'";
		Query query = sessionFactory.getCurrentSession().createQuery(countHql);

		return ((Number) query.uniqueResult()).intValue();

	}
	
	@Override
	public Boolean delArticle(int id) {
		String hql = "update ArticleDetail a set a.status= '0' where id=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong(0, id);
		return (query.executeUpdate() > 0);
	}

	@Override
	public List<ArticleDetail> getArticles(int start, int count) {
		String hql = "from ArticleDetail a where a.status='1'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setMaxResults(count);
		query.setFirstResult(start);
		return query.list();
	}

	@Override
	public ArticleContent getContent(int id) {
		if (getArticle(id) != null) {
			String hql = "from ArticleContent a where a.id=? ";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setLong(0, id);
			return (ArticleContent) query.uniqueResult();
		} else
			return null;
	}

	@Override
	public ArticleDetail getArticle(int id) {
		String hql = "from ArticleDetail a where a.id=? and a.status='1'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong(0, id);
		return (ArticleDetail) query.uniqueResult();
	}

	@Override
	public List<ArticleDetail> getArticleByUSer(User user) {
		String hql = "from ArticleDetail a where a.status='1' and a.author=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getUserName());
		return query.list();
	}

}
