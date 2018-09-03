package com.bizduo.zflow.domain.sys;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
@Entity
@Table(name="global_user")
@JsonIgnoreProperties({"roles", "permissions"})
public class User implements Serializable, UserDetails{
	private static final long serialVersionUID = 3087897839541809431L;
	public static final String ISROLEGROUP = "ISROLEGROUP"; //是否角色分拆
	
	public static final String APPID = "wx00f1a2eadc7c69e0"; //微信AppId
	public static final String APPSECRET = "61c6e8ec2bc9351dfbfe214793b4f966"; //微信AppSecret
	public static final String ENCODINGAESKEY = "SOjj1csWgZcMVNX2EXQedVe84CdARvvx2MKQI8f55N7"; //微信EncodingAESKey
	 
	private Integer id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String fullname;
    private String processrole;
    private String tel;
    private String email; 
    private String realname;
	private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private boolean passwordChanged = false;
    private Date birthday;
    private String location;
    private String iosDevic; 
    private String imei;
    private String model;
    private String version;
    private String versionName;
    private String versionUrl;
    private String userType;
    private String indexUrl;
    private Integer entityid;//分公司信息
    private Integer report2;//上级人员
	private Organization organization;
	
	private Integer gender; //性别 
	private Double  height; //身高
	private Double weight; //体重
	private Integer  district; //区县
	private Integer  companyId; //wdit 企业Id
	/*
	 * xd
	 */
	private Integer valid;//是否有效  1为有效  2为无效
	private Integer type;//1为教练  2为微信注册用户

	private String provinceName;//
	private String cityName;
	private String  districtName;
	private String  openId;


	private Collection<Role> roles = new HashSet<Role>();
    private Collection<GrantedAuthority> permissions = new HashSet<GrantedAuthority>();
    private Collection<GrantedAuthority> phonePermissions = new HashSet<GrantedAuthority>();
    
    private  Map<String,Collection<GrantedAuthority>> permissionMap =new HashMap<String, Collection<GrantedAuthority>>();
    private  Map<String,Collection<GrantedAuthority>> phonePermissionMap =new HashMap<String, Collection<GrantedAuthority>>();
    private String roleName;
    
    Boolean isAdmin = false;
    //tour保存人员时，临时用
    private Integer role;
    //酷家乐对应Id
    private Integer appuid;
    public User(){}
    public User(Integer id){
    	this.id = id;
    }
    public User(String username,String realname,String password,String tel, String email){
    	this.username=username;
    	this.password=password; 
    	this.email=email;
    	this.tel=tel;
    	this.realname=realname;
    }
    
    public User(Integer id, String username, String firstname, String lastname,String email, 
    		String password,String fullname, String processrole,String tel,String realname,
    		Integer appuid,Integer entityid,Integer report2,Boolean enabled){
    	this.id=id;
    	this.username=username;
    	this.password=password;
    	this.firstname=firstname;
    	this.email=email;
    	this.lastname=lastname;
    	this.fullname=fullname;
    	this.processrole=processrole;
    	this.tel=tel;
    	this.realname=realname;
    	this.appuid=appuid;
    	this.entityid= report2;
    	this.enabled=enabled;
    }
    public User(Integer id, String username, String firstname, String lastname,  String password,String fullname, String processrole,String tel){
    	this.id=id;
    	this.username=username;
    	this.password=password;
    	this.firstname=firstname; 
    	this.lastname=lastname;
    	this.fullname=fullname;
    	this.processrole=processrole;
    	this.tel=tel; 
    }
	
    
    public User(Integer id, String username, String firstname, String lastname, String password, Organization organization, Collection<Role> roles, String processrole){
    	this.id = id;
    	this.username = username;
    	this.firstname = firstname;
    	this.lastname = lastname;
    	this.password = password;
    	this.organization = organization;
    	this.processrole=processrole;
    	this.roles = roles;
    	this.fullname = firstname.substring(0, 1).toUpperCase() + firstname.substring(1).toLowerCase() + 
    			lastname.substring(0, 1).toUpperCase() + lastname.substring(1).toLowerCase();
    }
    
    @Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="id")    //对应生成策略名称  
    @TableGenerator(name="id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="global_user",            //主键的值  
                    valueColumnName="next_val", //生成的值  列名  
                    table="id_table",            //生成的表名  
                    initialValue=1000,//   [主键初识值，默认为0。]  
                    //catalog schema   指定表所在的目录名或是数据库名。  
                    allocationSize=1)  //主键每次增加的大小,默认为50  
    
	@Column
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	@Column
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	@Column
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	@Column
    public String getProcessrole() {
		return processrole;
	}
	public void setProcessrole(String processrole) {
		this.processrole = processrole;
	}
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Column
	public boolean getPasswordChanged() {
		return passwordChanged;
	}
	public void setPasswordChanged(boolean passwordChanged) {
		this.passwordChanged = passwordChanged;
	}
	@Column
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@Column(name="ios_device")
	public String getIosDevic() {
		return iosDevic;
	}
	
	public void setIosDevic(String iosDevic) {
		this.iosDevic = iosDevic;
	}
	@JoinTable(name="global_user_role", joinColumns={@JoinColumn(name="user_id")},
	inverseJoinColumns={@JoinColumn(name="role_id")})
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@OrderBy("acronym asc")
	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	@Transient
	public Collection<GrantedAuthority> getPermissions() {
		return permissions;
	}
	public void setPermissions(Collection<GrantedAuthority> permissions) {
		this.permissions = permissions;
	}
	@Transient
	public Collection<GrantedAuthority> getPhonePermissions() {
		return phonePermissions;
	}
	public void setPhonePermissions(Collection<GrantedAuthority> phonePermissions) {
		this.phonePermissions = phonePermissions;
	} 
	@Transient
	public Map<String, Collection<GrantedAuthority>> getPermissionMap() {
		return permissionMap;
	}
	public void setPermissionMap(
			Map<String, Collection<GrantedAuthority>> permissionMap) {
		this.permissionMap = permissionMap;
	}
	@Transient
	public Map<String, Collection<GrantedAuthority>> getPhonePermissionMap() {
		return phonePermissionMap;
	}
	public void setPhonePermissionMap(
			Map<String, Collection<GrantedAuthority>> phonePermissionMap) {
		this.phonePermissionMap = phonePermissionMap;
	}
	@Column
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permissions;
	}
	@Column
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}
	@Column
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}
	@Column
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}
	@Column
	public boolean isEnabled() {
		return this.enabled;
	}
	@Column
    public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Column
	public Integer getEntityid() {
		return entityid;
	}
	public void setEntityid(Integer entityid) {
		this.entityid = entityid;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id")
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	@Transient
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	@Column
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	@Column
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	@Column
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Column
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	@Column
	public String getVersionUrl() {
		return versionUrl;
	}
	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	} 
	@Column
	public String getUserType(){
		return userType;
	}	
	public void setUserType(String userType) {
		this.userType = userType;
	} 
	@Column
	public String getIndexUrl() {
		return indexUrl;
	}
	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}
	@Column
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	@Column
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	@Column
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public User clone(){
		User user = new User();
		user.setId(this.id);
		user.setUsername(this.username);
		user.setFullname(this.fullname);
		return user;
	}
	
	public User shadowClone(User o){
		User obj = new User();
		obj.setId(o.getId());
		obj.setUsername(o.getUsername());
		obj.setTel(o.getTel());
		obj.setEmail(o.getEmail());
		obj.setRealname(o.getRealname());
		
		Organization org = o.getOrganization();
		if(org!=null)
			org = org.shadowCloneBySelector(org);
		obj.setOrganization(org);
		return obj;
	}
	@Transient
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	@Column
	public Integer getAppuid() {
		return appuid;
	}
	public void setAppuid(Integer appuid) {
		this.appuid = appuid;
	}
	@Column
	public Integer getReport2() {
		return report2;
	}
	public void setReport2(Integer report2) {
		this.report2 = report2;
	} 
	@Column
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	@Column
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	@Column
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	@Column
	public Integer getDistrict() {
		return district;
	}
	public void setDistrict(Integer district) {
		this.district = district;
	}
	@Transient
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	@Transient
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@Transient
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	} 
	@Column
    public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	/*
	 * xd
	 */
	@Column
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	@Column
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
}
