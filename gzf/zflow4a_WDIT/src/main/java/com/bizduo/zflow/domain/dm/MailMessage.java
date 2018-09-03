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
@Table(name="MailMessage")  
public class MailMessage implements Serializable{ 
	private static final long serialVersionUID = 8699382018829053100L;
	private Long id;
    private User toUser;
    private String title;
    private String content; 
	private Long dataId;
	private int status; 
	private Long taskId;
	private String formcode;
	private int processStatus; 
	private Date createDate;
	private User createBy;
	private String  processrole;
	private int sendStatus;
	//
	public MailMessage(){}
	public MailMessage(Long id){
		this.id = id;
	}
	public MailMessage(User toUser,String title,String content,Long dataId,Long taskId,
			int status,int processStatus,String formcode,User createBy,String processrole,int sendStatus){ 
		this.toUser=toUser;
		this.title=title;
		this.content=content;
		this.dataId=dataId;
		this.status=status;
		this.createDate=new Date();
		this.createBy=createBy;
		this.taskId=taskId;
		this.processStatus=processStatus;
		this.formcode=formcode;
		this.processrole=processrole; 
		this.sendStatus=sendStatus;
	}
	public MailMessage(Long id,User toUser,String title,String content,Long dataId,Long taskId,
			int status,int processStatus,String formcode){
		this.id = id;
		this.toUser=toUser;
		this.title=title;
		this.content=content;
		this.dataId=dataId;
		this.status=status;
		this.taskId=taskId;
		this.processStatus=processStatus;
		this.formcode=formcode;
	}
    @Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="MailMessage_id")    //对应生成策略名称  
    @TableGenerator(name="MailMessage_id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="MailMessage_id",            //主键的值  
                    valueColumnName="next_val", //生成的值  列名  
                    table="id_table",            //生成的表名  
                    initialValue=1000,//   [主键初识值，默认为0。]  
                    //catalog schema   指定表所在的目录名或是数据库名。  
                    allocationSize=1)  //主键每次增加的大小,默认为50  
    
	@Column
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	} 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toUserId")
	public User getToUser() {
		return toUser;
	}
	public void setToUser(User toUser) {
		this.toUser = toUser;
	} 
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column
	public Long getDataId() {
		return dataId;
	}
	public void setDataId(Long dataId) {
		this.dataId = dataId;
	} 
	@Column
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	} 
	@Column
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	@Column
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}	
	@Column
	public int getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}	
	@Column
	public String getFormcode() {
		return formcode;
	}
	public void setFormcode(String formcode) {
		this.formcode = formcode;
	} 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createBy")
	public User getCreateBy() {
		return createBy;
	}
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	@Column
	public String getProcessrole() {
		return processrole;
	}
	public void setProcessrole(String processrole) {
		this.processrole = processrole;
	}
	@Column
	public int getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	} 
}
