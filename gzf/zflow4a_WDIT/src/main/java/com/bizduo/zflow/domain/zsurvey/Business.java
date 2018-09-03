package com.bizduo.zflow.domain.zsurvey;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
/**
 * 公司 - Entity
 * 
 * @author zs
 *
 */
//@Entity
//@Table(name="survey_business")
public class Business implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -525484923831439471L;

	/** id*/
	private Integer id;
	
	/** 公司名称*/
	private String name;

	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="business_ID")    //对应生成策略名称  
    @TableGenerator(name="business_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="business_ID",            //主键的值  
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
