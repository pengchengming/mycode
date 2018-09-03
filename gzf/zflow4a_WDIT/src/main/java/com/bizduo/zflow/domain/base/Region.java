package com.bizduo.zflow.domain.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="db_yhm_city")
public class Region implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6962862009862916804L;
	private Integer id;
	private String code;
	private Region parentRegion;  
	private String name;
	private String level;
	
	public Region() {
		super();
	}
	
	public Region(Integer id,String code, String name, String level) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
		this.code=code;
	}
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="db_yhm_city")    //对应生成策略名称  
    @TableGenerator(name="db_yhm_city", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="db_yhm_city",            //主键的值  
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="parent_id")
	public Region getParentRegion() {
		return parentRegion;
	}

	public void setParentRegion(Region parentRegion) {
		this.parentRegion = parentRegion;
	}
	@Column
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
