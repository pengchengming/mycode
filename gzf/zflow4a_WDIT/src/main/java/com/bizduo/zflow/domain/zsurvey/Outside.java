package com.bizduo.zflow.domain.zsurvey;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

/**
 * 外部调查 - Entity
 * 
 * @author zs
 *
 */
//@Entity
//@Table(name="survey_outside")
public class Outside implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5466842920621730424L;

	/** id*/
	private Integer id;
	
	/** 编号*/
	private String oscarId;
	
	/** 分公司*/
	private Business business;

	/** 国别*/
	private String nation;
	
	/** 学生姓名*/
	private String stuName;
	
	/** 生日*/
	private Date birthday;
	
	/** 联系方式*/
	private String tel;
	
	/** 签约日期*/
	private Date signsUpDate;
	
	/** 签证通过日期*/
	private Date visaThroughDate;
	
	/** 调查日期*/
	private Date surveyDate;
	
	/** 顾问1*/
	private String consultantOne;
	
	/** 顾问2*/
	private String consultantTwo;
	
	/** 助理1*/
	private String assistantOne;
	
	/** 助理2*/
	private String assistantTwo;
	
	/** 申请1*/
	private String applyOne;
	
	/** 申请2*/
	private String applyTwo;
	
	/** 签证1*/
	private String visaOne;
	
	/** 签证2*/
	private String visaTwo;
	
	/** 状态*/
	private Integer states;
	
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="outside_ID")    //对应生成策略名称  
    @TableGenerator(name="outside_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="outside_ID",            //主键的值  
                    valueColumnName="next_val", //生成的值  列名  
                    table="id_table",            //生成的表名  
                    initialValue=1000,//   [主键初识值，默认为0。]  
                    //catalog schema   指定表所在的目录名或是数据库名。  
                    allocationSize=1)  //主键每次增加的大小,默认为50  
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOscarId() {
		return oscarId;
	}

	public void setOscarId(String oscarId) {
		this.oscarId = oscarId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="business_id")
	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getConsultantOne() {
		return consultantOne;
	}

	public void setConsultantOne(String consultantOne) {
		this.consultantOne = consultantOne;
	}

	public String getConsultantTwo() {
		return consultantTwo;
	}

	public void setConsultantTwo(String consultantTwo) {
		this.consultantTwo = consultantTwo;
	}

	public String getAssistantOne() {
		return assistantOne;
	}

	public void setAssistantOne(String assistantOne) {
		this.assistantOne = assistantOne;
	}

	public String getAssistantTwo() {
		return assistantTwo;
	}

	public void setAssistantTwo(String assistantTwo) {
		this.assistantTwo = assistantTwo;
	}

	public String getApplyOne() {
		return applyOne;
	}

	public void setApplyOne(String applyOne) {
		this.applyOne = applyOne;
	}

	public String getApplyTwo() {
		return applyTwo;
	}

	public void setApplyTwo(String applyTwo) {
		this.applyTwo = applyTwo;
	}

	public String getVisaOne() {
		return visaOne;
	}

	public void setVisaOne(String visaOne) {
		this.visaOne = visaOne;
	}

	public String getVisaTwo() {
		return visaTwo;
	}

	public void setVisaTwo(String visaTwo) {
		this.visaTwo = visaTwo;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getSignsUpDate() {
		return signsUpDate;
	}

	public void setSignsUpDate(Date signsUpDate) {
		this.signsUpDate = signsUpDate;
	}

	public Date getVisaThroughDate() {
		return visaThroughDate;
	}

	public void setVisaThroughDate(Date visaThroughDate) {
		this.visaThroughDate = visaThroughDate;
	}

	public Date getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}
	
	
}
