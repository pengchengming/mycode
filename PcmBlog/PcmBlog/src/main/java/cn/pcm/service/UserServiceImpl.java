package cn.pcm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import cn.pcm.dao.UserDao;
import cn.pcm.dao.UserDaoImpl;
import cn.pcm.domain.User;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource(name="userDao")
	private UserDao userDao;

	
	public void setUserDao(UserDao userDao) {
		this.userDao =  userDao;
	}

	@Override
	public User getUser(String userName,String password) {
		return userDao.getUser( userName, password);
	}
	
	@Override
	public User getUser(String userName) {
		return userDao.getUser( userName);
	}

	@Override
	public List<User> getAllUser() {
		return userDao.getAllUser();
	}

	@Override
	public void addUser(User user) {
		userDao.addUser(user);
	}

	@Override
	public boolean delUser(String id) {

		return userDao.delUser(id);
	}

	@Override
	public boolean updateUserPassword(User user) {
		// TODO Auto-generated method stub
		return userDao.updateUserPassword(user );
	}

	@Override
	public boolean updateUserEmail(User user) {
		// TODO Auto-generated method stub
		return userDao.updateUserEmail(user);
	}

	
}
