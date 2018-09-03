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
 * 答题记录 - Entity
 * 以后需要修正为 AnswerOption
 * @author zs
 *
 */
//@Entity
//@Table(name="survey_answer")
public class Answer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2227488500964089295L;

	/** id*/
	private Integer id;
	
	/** 客户*/
	private Member member;
	
	/** 答卷记录*/
	private AnswerSheet answerSheet;
	
	/** 问题*/
	private Question question;
	
	/** 问题的答案*/
	private AnswerQuestion answerQuestion;
	
	
	/** 分数*/
	private Integer score;
	
	/** 创建时间*/
	private Date create_time;
	
	/** 答案*/
	private Option option;
	
	/** 答案内容，未来可以扩充为多个*/
	private String text_value;

	/** 答案内容*/
	private String textvalue;

	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="answer_ID")    //对应生成策略名称  
    @TableGenerator(name="answer_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="answer_ID",            //主键的值  
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="answersheet_ids")
	@JsonIgnore
	public AnswerSheet getAnswerSheet() {
		return answerSheet;
	}

	public void setAnswerSheet(AnswerSheet answerSheet) {
		this.answerSheet = answerSheet;
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
	@JoinColumn(nullable = true,name="question_id")
	@JsonIgnore
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	@Column
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="option_id")
	public Option getOption() {
		return option;
	}

	public void setOption(Option option) {
		this.option = option;
	}
	@Column
	public String getText_value() {
		return text_value;
	}

	public void setText_value(String text_value) {
		this.text_value = text_value;
	}
	@Column
	public Integer getScore() {
		return score;
	}
	
	public void setScore(Integer score) {
		this.score = score;
	}
	@Column
	public String getTextvalue() {
		return textvalue;
	}

	public void setTextvalue(String textvalue) {
		this.textvalue = textvalue;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="answerquestion_id")
	@JsonIgnore
	public AnswerQuestion getAnswerQuestion() {
		return answerQuestion;
	}

	public void setAnswerQuestion(AnswerQuestion answerQuestion) {
		this.answerQuestion = answerQuestion;
	}
	
	
}
