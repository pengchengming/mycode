package com.bizduo.zflow.domain.zsurvey;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 答卷记录 - Entity
 * 
 * @author zs
 *
 */
//@Entity
//@Table(name="survey_answersheet")
public class AnswerSheet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8958076257651759346L;

	/** id*/
	private Integer id;
	
	/** 编号*/
	private String code;
	
	/** 问卷*/
	private Questionnaire questionnaire;
	
	/** 答卷时间*/
	private Date create_time;
	
	/** 客户*/
	private Member member;
	
	/** 答卷人*/
	private String memberName;
	
	/** 合同*/
	private Outside outside;
	
	/** 1为答过*/
	private Integer states;
	
	/** 是否发送1发送*/
	private Integer condition;
	
	/** */
	private String userName;
	
	/** 是否是补考*/
	private Integer isMakeup;
	
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="answersheet_ID")    //对应生成策略名称  
    @TableGenerator(name="answersheet_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="answersheet_ID",            //主键的值  
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="qustnr_id")
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="member_id")
	@JsonIgnore
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="outside_id")
	@JsonIgnore
	public Outside getOutside() {
		return outside;
	}

	public void setOutside(Outside outside) {
		this.outside = outside;
	}

	@Column
	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	@Column
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Column
	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	@Column
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column
	public Integer getIsMakeup() {
		return isMakeup;
	}

	public void setIsMakeup(Integer isMakeup) {
		this.isMakeup = isMakeup;
	}
	
	
	
}
