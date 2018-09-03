package cn.pcm.dao;

import java.util.List;

import cn.pcm.domain.User;

public interface UserDao {

	public User getUser(String userName,String password);
	
	public User getUser(String userName);
	
	public List<User> getAllUser();
	
	public void addUser(User user);
	
	public boolean delUser(String id);

	boolean updateUserPassword(User user);

	boolean updateUserEmail(User user);

	
}
