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
@Table(name="ccm_push")  
public class CCMPush  implements Serializable {
	private static final long serialVersionUID = -3195406795755280667L;
	private Long id;
	private Long taskId;
	private CCMNotice notice; //noticeId
	private String officeName;
	private String date; //格式 2014-04-20 11:20:30 
	private String clientName;
	private String  ccName;
	private String ppName;
	private String infoSourceName;
	private String comment;
	private String releatedJsonArray;
	private String unReleatedJsonArray;
	private  String allPPJsonArray;
	private  String  acceptJsonArray;
	private Date createDate;
	private User createBy; 
    private boolean enabled = true; 
    private int pushType;
    private String errorMsg;
    private String feedback;

	

	/**1	TA Leader在 website执行appro时调用的push接口**/
	public static int APPRO=1; 
	/**2	releated P/P 在mobile app 执行 accept时调用的push接口**/
	public static int ACCEPT=2;
	/**3	unReleated P/P 在mobile app 输入 comment时调用的push接口**/
	public static int COMMENT1=3;
	/**4	releated P/P 在mobile app 执行reply 操作时调用的push接口**/
	public static int REPLY=4;
	/**5	定时提醒--accept 调用推送接口**/
	public static int REMINDACCEPT=5;
	/**6	定时提醒--reply 调用推送接口**/
	public static int REMINDREPLY=6; 

	public CCMPush(){}
	public CCMPush(Long taskId ,String officeName,String date,
			String clientName, String  ccName, String ppName,String infoSourceName,
			String comment,String releatedJsonArray,String unReleatedJsonArray,String acceptJsonArray,String allPPJsonArray,int pushType,String feedback) {
		this.taskId = taskId; 
		this.officeName = officeName;
		this.date = date;
		this.clientName = clientName;
		this.ccName=ccName;
		this.ppName=ppName;
		this.infoSourceName=infoSourceName;
		this.comment=comment;
		if(pushType==1||pushType==5){
			this.releatedJsonArray=releatedJsonArray;
			this.unReleatedJsonArray=unReleatedJsonArray;
		}else {
			this.acceptJsonArray=acceptJsonArray;
			this.allPPJsonArray=allPPJsonArray;
		} 
		this.createDate = new Date();
		this.enabled=true;
		this.pushType=pushType;
		this.feedback=feedback;
	}
	@Id
	// 对应数据库中的主键
	@GeneratedValue(strategy = GenerationType.TABLE,// 指定主键生成策略
	generator = "CCMPush_id")
	// 对应生成策略名称
	@TableGenerator(name = "CCMPush_id", // 生成策略名称
	pkColumnName = "sequence_name", // 主键的列名
	pkColumnValue = "CCMPush_id", // 主键的值
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
	@JoinColumn(name = "notice_Id")
	public CCMNotice getNotice() {
		return notice;
	}
	public void setNotice(CCMNotice notice) {
		this.notice = notice;
	}
	@Column
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	@Column
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Column
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	@Column
	public String getCcName() {
		return ccName;
	}
	public void setCcName(String ccName) {
		this.ccName = ccName;
	}
	@Column
	public String getPpName() {
		return ppName;
	}
	public void setPpName(String ppName) {
		this.ppName = ppName;
	}
	@Column
	public String getInfoSourceName() {
		return infoSourceName;
	}
	public void setInfoSourceName(String infoSourceName) {
		this.infoSourceName = infoSourceName;
	}
	@Column
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Column
	public String getReleatedJsonArray() {
		return releatedJsonArray;
	}
	public void setReleatedJsonArray(String releatedJsonArray) {
		this.releatedJsonArray = releatedJsonArray;
	}
	@Column
	public String getUnReleatedJsonArray() {
		return unReleatedJsonArray;
	}
	public void setUnReleatedJsonArray(String unReleatedJsonArray) {
		this.unReleatedJsonArray = unReleatedJsonArray;
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
	@Column	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Column
    public int getPushType() {
		return pushType;
	}
	public void setPushType(int pushType) {
		this.pushType = pushType;
	}
	@Column
	public String getAllPPJsonArray() {
		return allPPJsonArray;
	}
	public void setAllPPJsonArray(String allPPJsonArray) {
		this.allPPJsonArray = allPPJsonArray;
	}
	@Column
	public String getAcceptJsonArray() {
		return acceptJsonArray;
	}
	public void setAcceptJsonArray(String acceptJsonArray) {
		this.acceptJsonArray = acceptJsonArray;
	}

	@Column
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	@Column
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
}
