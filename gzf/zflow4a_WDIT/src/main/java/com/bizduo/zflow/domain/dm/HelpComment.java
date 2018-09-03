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
@Table(name="ccm_helpComment")  
public class HelpComment implements Serializable{
	private static final long serialVersionUID = 8248008088011443737L;
	private Long id;
	private CCMNotice notice;
	private Long taskId;
	private String comment;
	private Date createDate;
	private User createBy;
	public HelpComment(){}
	public HelpComment(CCMNotice notice,Long taskId, String comment, User createBy) {
		this.notice = notice; 
		this.taskId = taskId;
		this.comment = comment;
		this.createBy = createBy;
		this.createDate = new Date();
	}
	@Id
	// 对应数据库中的主键
	@GeneratedValue(strategy = GenerationType.TABLE,// 指定主键生成策略
	generator = "HelpComment_id")
	// 对应生成策略名称
	@TableGenerator(name = "HelpComment_id", // 生成策略名称
	pkColumnName = "sequence_name", // 主键的列名
	pkColumnValue = "HelpComment_id", // 主键的值
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notice_Id")
	public CCMNotice getNotice() {
		return notice;
	}
	public void setNotice(CCMNotice notice) {
		this.notice = notice;
	}
	@Column
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	@Column
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	
}
