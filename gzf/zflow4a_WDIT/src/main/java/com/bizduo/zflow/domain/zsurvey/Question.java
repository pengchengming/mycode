package com.bizduo.zflow.domain.zsurvey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 题目 - Entity
 * 
 * @author zs
 *
 */
//@Entity
//@Table(name="survey_question")
public class Question implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2773273576638620270L;

	/** id*/
	private Integer id;
	
	/** 编号*/
	private String code;
	
	/** 试卷*/
	private Questionnaire questionnaire;
	
	/** 内容*/
	private String context;
	
	/** 题型*/
	private QuestionType questionType;
	
	/** 答案*/
	private List<Option> options;
	
	/** 1上线0下线*/
	private Integer status;
	
	/** 创建时间*/
	private Date creatTime;
	
	/** 含有填空项*/
	private Integer hastextvalue;
	
	/**填空项备注*/
	private String textvaluenote;	
	
	/** 是否为补考用题目*/
	private Integer isMakeup;

	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="question_ID")    //对应生成策略名称  
    @TableGenerator(name="question_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="question_ID",            //主键的值  
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
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

 
	@Column
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column
	public Date getCreatTime() {
		return creatTime;
	}
	 
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	@Column
	public Integer getHastextvalue() {
		return hastextvalue;
	}
	
	public void setHastextvalue(Integer hastextvalue) {
		this.hastextvalue = hastextvalue;
	}
	@Column
	public String getTextvaluenote() {
		return textvaluenote;
	}
	
	public void setTextvaluenote(String textvaluenote) {
		this.textvaluenote = textvaluenote;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="qustnr_id")
	@JsonIgnore
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="type")
	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}


	@OneToMany(fetch=FetchType.EAGER,mappedBy="question")
	@Cascade(value = { CascadeType.ALL })
	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	@Column
	public Integer getIsMakeup() {
		return isMakeup;
	}

	public void setIsMakeup(Integer isMakeup) {
		this.isMakeup = isMakeup;
	}

	
	

	
}
