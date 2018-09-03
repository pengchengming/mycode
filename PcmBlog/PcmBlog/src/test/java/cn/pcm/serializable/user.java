package cn.pcm.serializable;

import java.io.Serializable;

public class user implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private int age;
	private String password;

	public user() {
		super();
	}

	public user(String username, int age, String password) {
		super();
		this.username = username;
		this.age = age;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
