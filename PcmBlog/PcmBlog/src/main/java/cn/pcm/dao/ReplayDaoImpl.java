package cn.pcm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.pcm.domain.Replay;

@Repository("replayDao")
public class ReplayDaoImpl implements ReplayDao{

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public void addReplay(Replay replay) {
		sessionFactory.getCurrentSession().save(replay);
	}

	@Override
	public List<Replay> getReplay(int replayid) {
		String hql = "from Replay r where r.replayid=? and r.status= '1'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong(0, replayid);
		return query.list();
	}

	@Override
	public Boolean delReplay(int id) {
		String hql = "updata Replay r  set r.status = '0' where r.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong(0, id);

		return (query.executeUpdate() > 0);
	}

}
