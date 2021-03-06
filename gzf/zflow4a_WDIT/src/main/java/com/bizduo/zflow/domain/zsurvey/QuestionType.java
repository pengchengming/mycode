package com.bizduo.zflow.domain.zsurvey;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;



/**
 * 题目类型 - Entity
 * 
 * @author zs
 *
 */
//@Entity
//@Table(name="survey_question_type")
public class QuestionType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9186139621492230356L;

	/** id*/
	private Integer id;
	
	/** 题型*/
	private String description;

	
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="question_category_ID")    //对应生成策略名称  
    @TableGenerator(name="question_category_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="question_category_ID",            //主键的值  
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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	 
}
