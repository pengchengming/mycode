package cn.pcm.dao;

import java.util.List;

import javax.annotation.Resource;


import cn.pcm.domain.User;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public User getUser(String userName,String password) {

		String hql = "from User u where u.userName=? and u.password=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userName);
		query.setString(1, password);
		return (User) query.uniqueResult();
	}

	@Override
	public User getUser(String userName) {

		String hql = "from User u where u.userName=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userName);
		return (User) query.uniqueResult();
	}
	
	@Override
	public List<User> getAllUser() {

		String hql = "from User";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);

		return query.list();
	}

	@Override
	public void addUser(User user) {
		sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public boolean delUser(String id) {
		String hql = "delete User u where u.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);

		return (query.executeUpdate() > 0);
	}

	@Override
	public boolean updateUserEmail(User user) {

		String hql = "update User u set u.email = ? where u.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getEmail());
		query.setString(1, user.getId());
		return (query.executeUpdate() > 0);
	}

	@Override
	public boolean updateUserPassword(User user) {
		String hql = "update User u set u.password = ? where u.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getPassword());
		query.setString(1, user.getId());
		return (query.executeUpdate() > 0);
	}
	
	
}
