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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
@Entity
@Table(name="global_permission")
@JsonIgnoreProperties({"roles", "action", "resource"})
public class Permission implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 8340389237005223796L;
	private Integer id;
    private String code;
    private Action action; //操作
    private Resource resource;//资源
    private Collection<Role> roles;
//  private Collection<User> users;
    
    private String name; //对应的操作的名称
    
    boolean checked;
    
    public Permission() {
		super();
	}
	public Permission(Action action, Resource resource) {
		super();
		this.action = action;
		this.resource = resource;
		this.code = action.getCode() + "_" + resource.getCode();
	}
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="id")    //对应生成策略名称  
    @TableGenerator(name="id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="permission_id",            //主键的值  
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_id")
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "resource_id")
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="global_permission_role", joinColumns={@JoinColumn(name="permission_id")},
	inverseJoinColumns={@JoinColumn(name="role_id")})
	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
//	@ManyToMany(fetch=FetchType.LAZY)
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
//	@JoinTable(name="global_permission_user", joinColumns={@JoinColumn(name="permission_id")},
//	inverseJoinColumns={@JoinColumn(name="user_id")})
//	public Collection<User> getUsers() {
//		return users;
//	}
//	public void setUsers(Collection<User> users) {
//		this.users = users;
//	}
	@Transient
	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	@Transient
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Transient
	public String getAuthority() {
		return code;
	}
}
