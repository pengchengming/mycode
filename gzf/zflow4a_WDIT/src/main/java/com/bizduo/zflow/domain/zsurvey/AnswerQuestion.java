package com.bizduo.zflow.domain.zsurvey;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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

/**
 * 答题题目记录 - Entity
 * 存放针对整个题目的回答
 * 
 * 以后需要修正为 QuestionAnswer
 * 
 * @author zs
 *
 */
//@Entity
//@Table(name="survey_answerquestion")
public class AnswerQuestion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2227488500964089295L;

	/** id*/
	private Integer id;	
	
	/** 答卷记录*/
	private AnswerSheet answerSheet;
	
	/** 问题*/
	private Question question;
	
	/** 创建时间*/
	private Date create_time;
	
	/** 答案内容*/
	private String textvalue;
	
	/** 答案选项（选中的）*/
	private Set<Answer> answerOptions;
	

	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="answerquestion_ID")    //对应生成策略名称  
    @TableGenerator(name="answerquestion_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="answerquestion_ID",            //主键的值  
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
	@JoinColumn(nullable = true,name="answersheet_id")
//	@JsonIgnore
	public AnswerSheet getAnswerSheet() {
		return answerSheet;
	}

	public void setAnswerSheet(AnswerSheet answerSheet) {
		this.answerSheet = answerSheet;
	}
 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="question_id")
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
	
	@Column
	public String getTextvalue() {
		return textvalue;
	}

	public void setTextvalue(String textvalue) {
		this.textvalue = textvalue;
	}
 
	@OneToMany(fetch=FetchType.EAGER,mappedBy="answerQuestion")
	@Cascade(value = { CascadeType.ALL })
	public Set<Answer> getAnswerOptions() {
		return answerOptions;
	}

	public void setAnswerOptions(Set<Answer> answerOptions) {
		this.answerOptions = answerOptions;
	}	
	
}
