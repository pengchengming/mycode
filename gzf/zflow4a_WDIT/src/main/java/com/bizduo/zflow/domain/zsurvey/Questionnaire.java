package com.bizduo.zflow.domain.zsurvey;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

/**
 * 问卷 - Entity
 * 
 * @author zs
 *
 */
//@Entity
//@Table(name="survey_questionnaire")
public class Questionnaire implements Serializable{

	private static final long serialVersionUID = 2215727944609144574L;

	/** id*/
	private Integer id;
	
	/** 编号*/
	private String code;
	
	/** 问卷名称*/
	private String cnname;
	
	private String enname;
	
	/** 标题说明*/
	private String description;
	
	private String cntitle;
	
	/** 末尾说明*/
	private String entitle;
	
	/** 发布时间*/
	private String publishDate;
	
	private Date cutoffDate;
	
	/** 创建时间*/
	private Date createDate;
	
	/** 课程*/
	private String course;
	
	/** 教师*/
	private String teacher;
	
	/** 地址*/
	private String address;
	
	/** 状态1以答*/
	private Integer status;
	
	/** 是否删除*/
	private Integer isDelete;
	
	/** 公司*/
	private Business business;
	
	/** 1上线0下线*/
	private Integer isOnOff;
	
	
	private Integer isMakeupExamination;

	private Integer  examinationTime;
	
	private Date startDate;

	private Date endDate;

	private Date makeupStartDate;

	private Date makeupEndDate;
	
	private Integer valid;
	
	private Integer questionNum;
	
	private Integer qualifiedNum;
	
	
	
	

	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="questionnaire_ID")    //对应生成策略名称  
    @TableGenerator(name="questionnaire_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="questionnaire_ID",            //主键的值  
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
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column
	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
	}

	@Column
	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	@Column
	public String getCntitle() {
		return cntitle;
	}

	public void setCntitle(String cntitle) {
		this.cntitle = cntitle;
	}

	@Column
	public String getEntitle() {
		return entitle;
	}

	public void setEntitle(String entitle) {
		this.entitle = entitle;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	@Column
	public Date getCutoffDate() {
		return cutoffDate;
	}

	public void setCutoffDate(Date cutoffDate) {
		this.cutoffDate = cutoffDate;
	}

	@Column
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column
	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	@Column
	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	@Column
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	@Column
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="business_id")
	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	@Column
	public Integer getIsOnOff() {
		return isOnOff;
	}

	public void setIsOnOff(Integer isOnOff) {
		this.isOnOff = isOnOff;
	}

	@Column
	public Integer getIsMakeupExamination() {
		return isMakeupExamination;
	}

	public void setIsMakeupExamination(Integer isMakeupExamination) {
		this.isMakeupExamination = isMakeupExamination;
	}

	@Column
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column
	public Date getMakeupStartDate() {
		return makeupStartDate;
	}

	public void setMakeupStartDate(Date makeupStartDate) {
		this.makeupStartDate = makeupStartDate;
	}

	@Column
	public Date getMakeupEndDate() {
		return makeupEndDate;
	}

	public void setMakeupEndDate(Date makeupEndDate) {
		this.makeupEndDate = makeupEndDate;
	}

	@Column
	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	@Column
	public Integer getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(Integer questionNum) {
		this.questionNum = questionNum;
	}

	@Column
	public Integer getQualifiedNum() {
		return qualifiedNum;
	}

	public void setQualifiedNum(Integer qualifiedNum) {
		this.qualifiedNum = qualifiedNum;
	}

	@Column
	public Integer getExaminationTime() {
		return examinationTime;
	}

	public void setExaminationTime(Integer examinationTime) {
		this.examinationTime = examinationTime;
	}
	
	
	
}
