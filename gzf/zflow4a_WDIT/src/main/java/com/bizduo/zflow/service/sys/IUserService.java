package com.bizduo.zflow.service.sys;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.base.IBaseService;

public interface IUserService extends IBaseService<User, Integer> {
	public User updateUser(User obj,boolean intUpdatepassword) throws Exception;
	public User createUser(User obj,boolean intUpdatepassword) throws Exception;
	////全部
	public List<User> findByAllUser() throws BadCredentialsException ;
	//名称查询
	public List<User> findByUserName(String username) throws BadCredentialsException;
	
	public void updateUserRole(Integer userId, Integer[] ids) throws Exception;
	
	public User findUserAccountsByUsername( String username) throws BadCredentialsException;
	
	public User findUserAccountsByUsernameEqualsAndPasswordEquals(String username, String password) throws BadCredentialsException;
	
	public User findUserAccountsByLDAP( String username, String password) throws BadCredentialsException;

	public Collection<User> findByOrganization(Integer orgId);
	
	public Collection<Role> findRolesByUserId(Integer id);
	
	public Collection<GrantedAuthority> findGrantedAuthorityByUserId(Integer id);
	
	public boolean userExists(String username);
	
	public void changePassowrd(String oldPassword, String newPassword);
	
	public User findByUserId (Integer id);
	//
	public List<User>  findByProcessrole(String Processrole);
	//
	public List<User>  findByProcessroles(String[] Processroles);
	//
	public List<User> findByUserCcmId(String id);
	//根据Ids 查询出User
	public List<User> findByUserIds(Integer[] toUserIds); 
	public User getByName(String name);
	public User getByName(Integer id, String name);
	public boolean matchPassword(User user, String password);
	/**
	 * 记录当前登录人的版本号
	 * @param iMEI
	 * @param mODEL
	 * @param versionName
	 */
	public void updateVersion(String iMEI, String mODEL, String versionName, User user)throws Exception;
	/**
	 * userRole
	 * @param map
	 * @param pageTrace
	 * @return
	 */
	public List<User> listByMap(Map<String, Object> map, PageTrace pageTrace);
	public List<User> getUserCurrentSales(); 
	
	public  Collection<Role> getRolesByUserId(Integer userId);
	
	public List<User> getUserByMap(Map<String, String> paramMap);
	
	public Collection<GrantedAuthority> findPhoneGrantedAuthorityByUserId( Integer id);
	/**
	 * 取得用户全部权限
	 * */
 	public Set<GrantedAuthority> getAllUserPermission(User user,boolean isRoleGroup);
 	/**返回角色对应的权限**/
 	public Map<String,Collection<GrantedAuthority>>  findMapGrantedAuthorityByUserId(Integer id);
 	/**返回角色对应的权限 手机端**/
 	public Map<String,Collection<GrantedAuthority>>  findMapPhoneGrantedAuthorityByUserId(Integer id);
 	/**
 	 * 根据OpenId获取有效的User
 	 * @param openId
 	 * @return
 	 */
	public User findbyOpenId(String openId);
 	/**
 	 * 根据OpenId获取User
 	 * @param openId
 	 * @return
 	 */
	public User findbyOpenIdAll(String openId);
	/**
 	 * 根据tel获取User
 	 * @param openId
 	 * @return
 	 */ 
	public List<User> findbyTelAll(String tel);
	/**
	 * 明文密码校验
	 * @param string
	 */
	public boolean checkPassword(String password,String  userPassword);
}
