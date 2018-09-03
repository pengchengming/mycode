package cn.pcm.service;

import java.util.List;

import cn.pcm.domain.User;

public interface UserService {

	public User getUser(String userName,String password);

	public List<User> getAllUser();

	public void addUser(User user);

	public boolean delUser(String id);

	User getUser(String userName);

	boolean updateUserPassword(User user);

	boolean updateUserEmail(User user);

}
