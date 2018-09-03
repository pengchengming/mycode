package com.bizduo.zflow.domain.sys;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
@Entity
@Table(name="global_role")
@JsonIgnoreProperties({"permissions", "users"})
public class Role implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 8247602793345868042L;
	
	private Integer id;
    private String name;
    private String acronym;
    private String description;
	private Collection<Permission> permissions;
	private Collection<Permission> phonePermissions;
	
	public Role(){}
	public Role(Integer id){
		this.id = id;
	}
	public Role(Integer id, String name,String acronym, String description){
		this.id = id;
		this.name = name;
		this.acronym=acronym;
		this.description = description;
	}
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="id")    //对应生成策略名称  
    @TableGenerator(name="id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="role_id",            //主键的值  
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
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	@Column
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="global_permission_role", joinColumns={@JoinColumn(name="role_id")},
	inverseJoinColumns={@JoinColumn(name="permission_id")})
	public Collection<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Collection<Permission> permissions) {
		this.permissions = permissions;
	}
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permissions;
	}
	@Transient
	public String getAuthority() {
		return this.name;
	}
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="global_phonePermission_role", joinColumns={@JoinColumn(name="role_id")},
	inverseJoinColumns={@JoinColumn(name="permission_id")})
	public Collection<Permission> getPhonePermissions() {
		return phonePermissions;
	}
	public void setPhonePermissions(Collection<Permission> phonePermissions) {
		this.phonePermissions = phonePermissions;
	}
}
