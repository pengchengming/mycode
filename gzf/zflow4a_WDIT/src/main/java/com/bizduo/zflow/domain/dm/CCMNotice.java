package com.bizduo.zflow.domain.dm;

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

import com.bizduo.zflow.domain.sys.User;

@Entity
@Table(name="ccm_notice")  
public class CCMNotice implements Serializable {
	private static final long serialVersionUID = -3195406795755280667L;
	private Long id;
	private Long taskId;
	private User readUser;
	private int userType;
	private Long dataId;
	private String formcode;
	private int readStatus;
	private Date createDate;
	private User createBy;
	private HelpComment helpComment; 
    private boolean enabled = true; 
    private Integer remindCount; 
    

	public CCMNotice() {
	} 
	public CCMNotice(Long id) {
		this.id = id;
	}

	public CCMNotice(Long taskId, User readUser, int userType, Long dataId,
			String formcode, int readStatus, User createBy,HelpComment helpComment) {
		this.taskId = taskId;
		this.readUser = readUser;
		this.userType = userType;
		this.dataId = dataId;
		this.formcode = formcode;
		this.readStatus = readStatus;
		this.createBy = createBy;
		this.createDate = new Date();
		this.helpComment=helpComment;
		this.enabled=true; 
		this.remindCount=0;
	}

	public CCMNotice(Long taskId, User readUser, int userType, Long dataId,
			String formcode, int readStatus, User createBy) {
		this.taskId = taskId;
		this.readUser = readUser;
		this.userType = userType;
		this.dataId = dataId;
		this.formcode = formcode;
		this.readStatus = readStatus;
		this.createBy = createBy;
		this.createDate = new Date();
		this.remindCount=0;
	}

	@Id
	// 对应数据库中的主键
	@GeneratedValue(strategy = GenerationType.TABLE,// 指定主键生成策略
	generator = "CCMNotice_id")
	// 对应生成策略名称
	@TableGenerator(name = "CCMNotice_id", // 生成策略名称
	pkColumnName = "sequence_name", // 主键的列名
	pkColumnValue = "CCMNotice_id", // 主键的值
	valueColumnName = "next_val", // 生成的值 列名
	table = "id_table", // 生成的表名
	initialValue = 1000,// [主键初识值，默认为0。]
	// catalog schema 指定表所在的目录名或是数据库名。
	allocationSize = 1)
	// 主键每次增加的大小,默认为50
	@Column
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "readUser_Id")
	public User getReadUser() {
		return readUser;
	}
	public void setReadUser(User readUser) {
		this.readUser = readUser;
	}
	@Column
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	@Column
	public Long getDataId() {
		return dataId;
	}
	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}
	@Column
	public String getFormcode() {
		return formcode;
	}
	public void setFormcode(String formcode) {
		this.formcode = formcode;
	}
	@Column
	public int getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}
	@Column
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createBy")
	public User getCreateBy() {
		return createBy;
	}
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	} 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "helpComment_id")
	public HelpComment getHelpComment() {
		return helpComment;
	}
	public void setHelpComment(HelpComment helpComment) {
		this.helpComment = helpComment;
	}	
	@Column
	public boolean isEnabled() {
		return enabled;
	} 
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
	@Column
	public Integer getRemindCount() {
		return remindCount;
	}
	public void setRemindCount(Integer remindCount) {
		this.remindCount = remindCount;
	}
}
