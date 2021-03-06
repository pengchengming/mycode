package com.bizduo.zflow.domain.sys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@Entity
@Table(name="global_action")
public class Action implements Serializable {
	private static final long serialVersionUID = 2697792736300169790L;
	private Long id;
	private String name;
	private String code;
	
	public Action(){}
	public Action(Long id, String code, String name){
		this.id = id;
		this.name = name;
		this.code = code;
	}
	
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="id")    //对应生成策略名称  
    @TableGenerator(name="id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="action_id",            //主键的值  
                    valueColumnName="next_val", //生成的值  列名  
                    table="id_table",            //生成的表名  
                    initialValue=1000,//   [主键初识值，默认为0。]  
                    //catalog schema   指定表所在的目录名或是数据库名。  
                    allocationSize=1)  //主键每次增加的大小,默认为50  
    
	@Column 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
