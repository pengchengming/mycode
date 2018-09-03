package com.bizduo.zflow.domain.zsurvey;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 答案 - Entity
 * 
 * @author zs
 *
 */
/**
 * @author Administrator
 *
 */
//@Entity
//@Table(name="survey_option")
public class Option implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2815358362207894270L;

	/** id*/
	private Integer id;
	
	/** 编号*/
	private String code;
	
	/** 内容*/
	private String cntxt;
	
	/** 分数*/
	private Integer score;
	
	/** 问题*/
	private Question question;
	
	/** 含有填空项*/
	private Integer hastextvalue;
	
	/**填空项备注*/
	private String textvaluenote;

	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="option_ID")    //对应生成策略名称  
    @TableGenerator(name="option_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="option_ID",            //主键的值  
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
	
	public String getCntxt() {
		return cntxt;
	}

	public void setCntxt(String cntxt) {
		this.cntxt = cntxt;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	
	
	public Integer getHastextvalue() {
		return hastextvalue;
	}

	public void setHastextvalue(Integer hastextvalue) {
		this.hastextvalue = hastextvalue;
	}

 

	public String getTextvaluenote() {
		return textvaluenote;
	}

	public void setTextvaluenote(String textvaluenote) {
		this.textvaluenote = textvaluenote;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="question_id")
	@JsonIgnore
	public Question getQuestion() {
		return question;
	}


	
	
}
