package com.bizduo.zflow.domain.sys;

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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@Entity
@Table(name="global_resource")
@JsonIgnoreProperties({"permissions"})
public class Resource implements Serializable {
	private static final long serialVersionUID = 5468752453288063889L;
	private Integer id;
	private String code;//资源代码
	private String name;//资源名
	private String type;
	private String scope;
	private Module module;//模块
//    private Set<Permission> permissions;
    
    public Resource(){}

    public Resource(Integer id, String code, String name, String scope, String type, Module module){
    	this.id = id;
    	this.name = name;
    	this.code = code;
    	this.type = type;
    	this.scope = scope;
    	this.module = module;
    }
    @Id //对应数据库中的主键  
    @GeneratedValue(strategy = GenerationType.TABLE,//指定主键生成策略  
                    generator = "id")    //对应生成策略名称  
    @TableGenerator(name = "id", //生成策略名称  
                    pkColumnName = "sequence_name",     //主键的列名  
                    pkColumnValue = "resource_id",            //主键的值  
                    valueColumnName = "next_val", //生成的值  列名  
                    table = "id_table",            //生成的表名  
                    initialValue = 1000,//   [主键初识值，默认为0。]  
                    //catalog schema   指定表所在的目录名或是数据库名。  
                    allocationSize = 1)  //主键每次增加的大小,默认为50  
    
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id")
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	//, cascade = CascadeType.ALL
	/*
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="resource_id")
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}*/
}
