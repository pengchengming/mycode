package com.bizduo.zflow.domain.sys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="global_employee")
public class Employee implements Serializable  {

	private static final long serialVersionUID = 8186041164189968208L;
	private Integer id;
	/**
	 * 身份证号
	 */
	private String identitycard;
	/**
	 * 账号
	 */
	private String username;

	/** 人员类型
	 * 0:外部用户 1:内部用户
	 * */
	private String userType;

	/** 角色类型
	 * 0:普通用户 1:管理员
	 * */
	private String roleType;
		
	/**
	 * 姓名
	 */
	private String realname;
	
	/**
	 * 工号
	 */
	private String empNo;
	/**
	 * 性别 
	 */
	private boolean sex;
	/**
	 * 出生日期
	 */
	private Date birthdate;
	/**
	 * 学历
	 */
	private String education;
	
	/** 邮箱*/
	private String email;
	
	/**
	 * 电话
	 */
	private String telephone;
	/**
	 * 手机
	 */
	private String phone;
	/**
	 * 家庭地址
	 */
	private String homeAddress;
	/**
	 * 是否离职
	 */
	private boolean isLeave;
	/**
	 * 入职日期
	 */
	private Date entryDate;
	
	/** 创建时间*/
	private Date creatDate;
	
	/**
	 * 离职日期
	 */
	private Date leaveDate;
	/**
	 * 禁用
	 */
	private Boolean enabled;
	/**
	 * 职位
	 */
	private String position;
	/**
	 * 所属机构 
	 */
	private Organization organization;
	
	/** 容量*/
	private Long diskSize;

	/** 已用容量*/
	private Double usedDiskSize;
	
	/** 目录名*/
	private String openId;
	
	/** 等级*/
	private Rank rank;
	
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="id")    //对应生成策略名称  
    @TableGenerator(name="id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="employee_id",            //主键的值  
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
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	@Column
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	@Column
	public boolean getSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	@Column
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	@Column
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	@Column
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@Column
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	@Column
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	@Column
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id")
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	@Column
	public boolean getIsLeave() {
		return isLeave;
	}
	public void setIsLeave(boolean isLeave) {
		this.isLeave = isLeave;
	}
	@Column
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	@Column
	public String getIdentitycard() {
		return identitycard;
	}
	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}
	@Column
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	@Column
	public Long getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(Long diskSize) {
		this.diskSize = diskSize;
	}
	@Column
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rank_id")
	public Rank getRank() {
		return rank;
	}
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	@Column
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Column
	public Date getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}
	@Column
	public Double getUsedDiskSize() {
		return usedDiskSize;
	}
	public void setUsedDiskSize(Double usedDiskSize) {
		this.usedDiskSize = usedDiskSize;
	}
	@Column
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
}
