package cn.pcm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.pcm.domain.Resource;

@Repository("fileDao")
public class FileDaoImpl implements FileDao{
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public int getResourceCount() {
		String countHql="select count(*) from Resource r where r.status = '1' ";
		 Query query = sessionFactory.getCurrentSession().createQuery(countHql);
        
		    return ((Number)query.uniqueResult()).intValue();
	}

	@Override
	public List<Resource> getResource(int start, int count) {
		String hql = "from Resource  r where r.status = '1' ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		 query.setMaxResults(count);
		 query.setFirstResult(start);
		return query.list();
	}

	@Override
	public Boolean delResource(int id) {
		String hql = "updata Resource r set r.status='0' where r.id = ? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong(0, id);

		return (query.executeUpdate() > 0);
	}

	@Override
	public Resource getResource(int id) {
		String hql = "from Resource r where r.id=? and r.status = '1'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong(0, id);
		return (Resource) query.uniqueResult();
	}

	@Override
	public void addResource(Resource resource) {
		sessionFactory.getCurrentSession().save(resource);
	}

}
