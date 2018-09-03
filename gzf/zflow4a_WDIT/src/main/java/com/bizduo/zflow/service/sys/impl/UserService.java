package com.bizduo.zflow.service.sys.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.Permission;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.ExecutionSql;
@Service
public class UserService extends BaseService<User, Integer> implements IUserService, UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private IRoleService roleService;
	/*@Autowired
	private IOrganizationService organizationService;
	*/
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	@Autowired
	private IBizTypeService  bizTypeService;

	public User findByUserId (Integer id){
		User user = (User)super.queryDao.get(User.class, id);
		return user;
	}
	
	public void updateUserRole(Integer userId, Integer[] ids)
			throws Exception {
		User u = (User)super.findObjByKey(User.class, userId);
		Collection<Role> roles = new ArrayList<Role>();
		for(int i = 0; i < ids.length; i++){
			roles.add(this.roleService.findObjByKey(Role.class, ids[i]));
		}
		u.getRoles().clear();
		u.getRoles().addAll(roles);
		super.update(u);
	}

	@SuppressWarnings("unchecked")
	public List<User> findByUserName(String username) throws BadCredentialsException {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		cri.add(Restrictions.like("username", username, MatchMode.ANYWHERE));
		List<User> list = super.queryDao.getByDetachedCriteria(cri);
		if(null == list || 0 == list.size()) return null;
		else return list;
	}
	@SuppressWarnings("unchecked")
	public List<User> findByAllUser() throws BadCredentialsException {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class); 
		List<User> list = super.queryDao.getByDetachedCriteria(cri);
		if(null == list || 0 == list.size()) return null;
		else return list;
	}
	
	public User findUserAccountsByUsername(
			String username) throws BadCredentialsException {
		User u = this.getByName(username);
		if(null == u) {
//			throw new BadCredentialsException("The username doesn't exist, Please check it!");
			u = this.findbyOpenId(username);
			if(null == u){
				throw new BadCredentialsException("用户名或密码不正确，请重新输入！");	
			}
		}
		return u;
	} 
	public User findUserAccountsByUsernameEqualsAndPasswordEquals(
			String username, String password) throws BadCredentialsException {
		User u = this.getByName(username);
		boolean isWeiXin=false;
			if(null == u){
//				throw new BadCredentialsException("The username doesn't exist, Please check it!");
				u = this.findbyOpenId(username);
				if(null == u){
					throw new BadCredentialsException("用户名或密码不正确，请重新输入！");	
				}else {
					isWeiXin=true;	
				}
			}
			//if(!passwordEncoder.encode(password.trim()).equals(u.getPassword()))
			if(!isWeiXin){
				if(!passwordEncoder.matches(password.trim(),u.getPassword()))
//					throw new BadCredentialsException("Username and password do not match, Please enter password again!");
					throw new BadCredentialsException("密码错误!");	
			}
 
		return u;
	}
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public User findUserAccountsByLDAP( String username, String password) throws BadCredentialsException {
		Boolean ldapresult=false;
		User u = this.getByName(username);
		if(null == u) 
			throw new BadCredentialsException("The username doesn't exist, Please check it!");
		
		DirContext ctx =null;
		Hashtable env =new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://172.16.10.22:389/");//连接LDAP的URL和端口


		env.put(Context.SECURITY_AUTHENTICATION, "simple");//以simple方式发送
		//env.put(Context.SECURITY_PRINCIPAL, "cn=xinyi,OU=Jahwa User,DC=jahwa,DC=com,DC=cn");//用户名
		env.put(Context.SECURITY_PRINCIPAL, username+"@jahwa.com.cn");//用户名
		env.put(Context.SECURITY_CREDENTIALS, password);//密码
		String baseDN="OU=Jahwa User,DC=jahwa,DC=com,DC=cn";//查询区域
		String filter="(&(objectClass=person))";//条件查询

		try{
		ctx =new InitialDirContext(env);//连接LDAP服务器
		System.out.println("Success");
		ldapresult=true;
		}catch(Exception ex1)
		{
			//throw new BadCredentialsException("Unexpected Exception!["+ex1.getMessage()+"]");
			throw new BadCredentialsException("Username and password do not match, Please enter password again!");
		}
		if(!ldapresult) 
			throw new BadCredentialsException("Username and password do not match, Please enter password again!");
		return u;
	}
		
 
	public User createUser(User obj, boolean intUpdatepassword) throws Exception {
//		obj.setFullname(obj.getFirstname().substring(0, 1).toUpperCase() + obj.getFirstname().substring(1).toLowerCase() + ","+
//    			obj.getLastname().substring(0, 1).toUpperCase() + obj.getLastname().substring(1).toLowerCase());
		if(null != obj.getFirstname() && null != obj.getLastname())
			obj.setFullname(obj.getFirstname() + "," + obj.getLastname());
		 if(intUpdatepassword) 
			 obj.setPassword(passwordEncoder.encode(obj.getPassword()));
		return super.create(obj);
	} 
	public User updateUser(User obj, boolean intUpdatepassword) throws Exception{
//		obj.setFullname(obj.getFirstname().substring(0, 1).toUpperCase() + obj.getFirstname().substring(1).toLowerCase() + ","+
//    			obj.getLastname().substring(0, 1).toUpperCase() + obj.getLastname().substring(1).toLowerCase());
		if(null != obj.getFirstname() && null != obj.getLastname())
			obj.setFullname(obj.getFirstname() + "," + obj.getLastname());
		 if(intUpdatepassword) 
			 obj.setPassword(passwordEncoder.encode(obj.getPassword()));
		return super.update(obj);
	}

	@SuppressWarnings("unchecked")
	public Collection<User> findByOrganization(Integer orgId) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		cri.add(Restrictions.eq("organization.id", orgId));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	public Collection<GrantedAuthority> findGrantedAuthorityByUserId(Integer id){
		Collection<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		try{
			Collection<Role> roles = (super.findObjByKey(User.class, id)).getRoles();
			if(null != roles && 0 < roles.size()){
				for(Role r : roles){
					if(null != r.getPermissions()){
						for(Permission p : r.getPermissions()){
							list.add(new SimpleGrantedAuthority(p.getCode()));
						}
					}
				}
			}
		}catch(Exception e){
			 e.printStackTrace();
			 return null;
		}
        return list;
	}
	
	public Collection<GrantedAuthority> findPhoneGrantedAuthorityByUserId(Integer id){
		Collection<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		try{
			Collection<Role> roles = (super.findObjByKey(User.class, id)).getRoles();
			if(null != roles && 0 < roles.size()){
				for(Role r : roles){
					if(null != r.getPhonePermissions()){
						for(Permission p : r.getPhonePermissions()){
							list.add(new SimpleGrantedAuthority(p.getCode()));
						}
					}
				}
			}
		}catch(Exception e){
			 e.printStackTrace();
			 return null;
		}
        return list;
	}
	
	//返回角色对应的权限
	public Map<String,Collection<GrantedAuthority>>  findMapGrantedAuthorityByUserId(Integer id){
		Map<String,Collection<GrantedAuthority>>  map =new HashMap<String, Collection<GrantedAuthority>>();
		try{
			Collection<Role> roles = (super.findObjByKey(User.class, id)).getRoles();
			if(null != roles && 0 < roles.size()){
				for(Role r : roles){
					if(map.get(r.getName())!=null){
						Collection<GrantedAuthority> list =map.get(r.getName());
						for(Permission p : r.getPermissions()){
							list.add(new SimpleGrantedAuthority(p.getCode()));
						}
					}else {
						if(null != r.getPermissions()){
							Collection<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
							for(Permission p : r.getPermissions()){
								list.add(new SimpleGrantedAuthority(p.getCode()));
							}
							map.put(r.getName(), list);
						}
					}
				}
			}
		}catch(Exception e){
			 e.printStackTrace();
			 return null;
		}
        return map;
	}
	//返回角色对应的权限 手机端
	public Map<String,Collection<GrantedAuthority>>  findMapPhoneGrantedAuthorityByUserId(Integer id){
		Map<String,Collection<GrantedAuthority>>  map =new HashMap<String, Collection<GrantedAuthority>>();
		//Collection<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		try{
			Collection<Role> roles = (super.findObjByKey(User.class, id)).getRoles();
			if(null != roles && 0 < roles.size()){
				for(Role r : roles){
					if(map.get(r.getName())!=null){
						Collection<GrantedAuthority> list =map.get(r.getName());
						for(Permission p : r.getPhonePermissions()){
							list.add(new SimpleGrantedAuthority(p.getCode()));
						}
					}else {
						if(null != r.getPhonePermissions()){
							Collection<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
							for(Permission p : r.getPhonePermissions()){
								list.add(new SimpleGrantedAuthority(p.getCode()));
							}
							map.put(r.getName(), list);
						}
					}
				}
			}
		}catch(Exception e){
			 e.printStackTrace();
			 return null;
		}
        return map;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public User getByName(String name){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		cri.add(Restrictions.eq("username", name));
		cri.add(Restrictions.eq("enabled", true));
		List<User> users = super.queryDao.getByDetachedCriteria(cri);
		if(null != users && 0 < users.size())
			return users.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public User getByName(Integer id, String name){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		cri.add(Restrictions.ne("id", id));
		cri.add(Restrictions.eq("username", name));
		List<User> users = super.queryDao.getByDetachedCriteria(cri);
		if(null != users && 0 < users.size())
			return users.get(0);
		return null;
	}

	public boolean userExists(String username) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		cri.add(Restrictions.eq("username", username));
		return (super.queryDao.getByDetachedCriteria(cri)).size() == 0 ? false : true;
	}
	public boolean matchPassword(User user, String password){
		if(passwordEncoder.encode(password.trim()).equals(user.getPassword())) 
			return true;
		return false;
	} 
	public void changePassowrd(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	public Collection<Role> findRolesByUserId(Integer id) {
		Collection<Role> roles = null;
		try {
			Collection<Role> roles1=super.findObjByKey(User.class, id).getRoles();
			if(null !=roles1){
				roles = new ArrayList<Role>();
				for(Role r : roles1)
					roles.add(r);
			}else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return roles;
	}

	@SuppressWarnings("unchecked")
	public List<User> findByProcessrole(String Processrole){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		cri.add(Restrictions.eq("processrole", Processrole));
		cri.add(Restrictions.eq("enabled", true));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	@SuppressWarnings("unchecked")
	public List<User> findByProcessroles(String[] Processroles){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		cri.add(Restrictions.in("processrole", Processroles));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	//根据ccmId查询出toPPgroupID
	public List<User> findByUserCcmId(String id){
		String tasksql="select  toPPGroupIds  from clientcall where id ='"+id +"' ";  //DBID_=
		List<String> list1=new ArrayList<String>(); 
		list1.add("EXECUTION_");  
		JSONArray jsonArray= ExecutionSql.querysqlJSONArray(jdbcTemplate, tasksql, list1);
		if(jsonArray.length() > 0) { 
			try { 
				List<User> userList=new ArrayList<User>();
				JSONObject	tempObj = jsonArray.getJSONObject(0);
				String toPPGroupIds  = (String)tempObj.get("toPPGroupIds"); 
				if(toPPGroupIds!=null&&!toPPGroupIds.equals("")){
					String[] toppgroupIdStrs=toPPGroupIds.split(",");
					for (String toppgroupId : toppgroupIdStrs) {
						if(toppgroupId!=null&&!toppgroupId.equals("")){
							User user=this.findByUserId(Integer.parseInt(toppgroupId));
							userList.add(user);
						} 
					}
				}
				return userList;
			} catch (JSONException e) { 
				e.printStackTrace();
			} 
		}
		return null;
	}
	

	//根据Ids 查询出User
	@SuppressWarnings("unchecked")
	public List<User> findByUserIds(Integer[] toUserIds){ 
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		cri.add(Restrictions.in("id", toUserIds));
		return super.queryDao.getByDetachedCriteria(cri);
	}

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return getByName(username);
	}
	/**
	 * 记录当前登录人的版本号
	 * @param iMEI
	 * @param mODEL
	 * @param versionName
	 * @throws Exception 
	 */
	public void updateVersion(String iMEI, String mODEL, String versionName, User user) throws Exception{
		String updateSql="update global_user set  versionName='"+versionName+"'";
		 if(user.getImei()==null||user.getImei().equals("")){
			 if(iMEI!=null&&!iMEI.equals("")) updateSql+=" , iMEI='"+iMEI+"' ";
		 }
		 if(user.getModel()==null||user.getModel().equals("")){
			 if(mODEL!=null&&!mODEL.equals("")) updateSql+=" ,MODEL='"+mODEL+"'  ";
		 }
		 updateSql+=" where id='"+user.getId()+"' ";
		ExecutionSql.executionsql(null, "execution", jdbcTemplate, updateSql, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> listByMap(Map<String, Object> map, PageTrace pageTrace){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class); 
		if(map.get("userName")!=null&&!map.get("userName").equals("") ){
			Criterion usernamec= Restrictions.ilike("username", map.get("userName").toString().trim(), MatchMode.ANYWHERE); 
			Criterion realnamec= Restrictions.ilike("realname", map.get("userName").toString().trim(), MatchMode.ANYWHERE);
			cri.add(Restrictions.or(usernamec, realnamec));
		}
		cri.addOrder(Order.desc("id"));
		return super.queryDao.getByDetachedCriteria(cri, pageTrace, false);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUserCurrentSales(){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);  
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUserByMap(Map<String, String> paramMap){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class);
		if(paramMap!=null){
			if(paramMap.get("roleCode")!=null && !paramMap.get("roleCode").toString().trim().equals("")){
				cri.createAlias("roles", "roles");
				cri.add(Restrictions.eq("roles.acronym", paramMap.get("roleCode")));
			}
			if(paramMap.get("entityid")!=null && !paramMap.get("entityid").toString().trim().equals("")){
				cri.add(Restrictions.eq("entityid", Integer.parseInt(paramMap.get("entityid")) ));
			}
			
		}
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	@SuppressWarnings("unchecked")
	public  List<Role> getRolesByUserId(Integer userId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class); 
		cri.add(Restrictions.eq("id", userId)); 
		List<User> userList= super.queryDao.getByDetachedCriteria(cri);
		if(userList!=null&&userList.size()>0){
			Collection<Role> roles= userList.get(0).getRoles();
			List<Role>  newRoleList=new ArrayList<Role>();
			for (Role role : roles) {
				Role role1=new Role(role.getId(), role.getName(), role.getAcronym(), role.getDescription());
				newRoleList.add(role1);
			}
			return newRoleList;
		}
		return null; 
	}
	
	/**
	 * 取得用户全部权限
	 * */
 	public Set<GrantedAuthority> getAllUserPermission(User user,boolean isRoleGroup) {
		if(isRoleGroup){ //是否角色分拆
			Map<String,Collection<GrantedAuthority>> grantedAuthorityMap = user.getPermissionMap();
			Map<String,Collection<GrantedAuthority>> phoneGrantedAuthorityMap =user.getPhonePermissionMap();
			
			Set<GrantedAuthority> sp=new HashSet<GrantedAuthority>();
			if(grantedAuthorityMap!=null){
				Collection<GrantedAuthority> grantedAuthority= grantedAuthorityMap.get(user.getRealname());
				if(grantedAuthority!=null)
					sp.addAll(grantedAuthority);
			}
			if(phoneGrantedAuthorityMap!=null){
				Collection<GrantedAuthority> grantedAuthority= phoneGrantedAuthorityMap.get(user.getRealname());
				if(grantedAuthority!=null)
					sp.addAll(grantedAuthority);
			}
			return sp;
		}else {

			if(user==null|| (user.getPermissions()==null && user.getPhonePermissions()==null))
				return null;
			Set<GrantedAuthority> sp=new HashSet<GrantedAuthority>();
			//先取得User的Permission 
			sp.addAll(user.getPermissions());
			sp.addAll(user.getPhonePermissions()); 
			return sp;
		}
	}
 	/**
 	 * 根据OpenId获取User
 	 * @param openId
 	 * @return
 	 */
	@SuppressWarnings("unchecked")
	public User findbyOpenId(String openId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class); 
		cri.add(Restrictions.eq("openId", openId)); 
		cri.add(Restrictions.eq("enabled", true));
		List<User> userList= super.queryDao.getByDetachedCriteria(cri);
		if(userList!=null&&userList.size()==1){
			User user= userList.get(0);
			return user;
		}
		return null; 
	}
	
	/**
 	 * 根据OpenId获取User
 	 * @param openId
 	 * @return
 	 */
	@SuppressWarnings("unchecked")
	public User findbyOpenIdAll(String openId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class); 
		cri.add(Restrictions.eq("openId", openId)); 
		List<User> userList= super.queryDao.getByDetachedCriteria(cri);
		if(userList!=null&&userList.size()==1){
			User user= userList.get(0);
			return user;
		}
		return null; 
	}
	/**
 	 * 根据tel获取User
 	 * @param openId
 	 * @return
 	 */
	@SuppressWarnings("unchecked")
	public List<User> findbyTelAll(String tel){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(User.class); 
		cri.add(Restrictions.eq("tel", tel));
		cri.add(Restrictions.eq("enabled", true));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	public boolean checkPassword(String password,String  userPassword){
		if(passwordEncoder.matches(password.trim(),userPassword)){
			return true;
		}
		return false;
	}

}
