package com.bizduo.zflow.domain.sys;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@Entity
@Table(name="global_menu_item")
@JsonIgnoreProperties({"permissions", "subMenuItemList"})
public class MenuItem implements Serializable {
	private static final long serialVersionUID = 7990691429379749695L;
	private Integer id;
	private String projectCode;	//项目Id
	private String url; //菜单上的URL地址 
	private String clsName; //菜单对应的图片名称
	private String name; //菜单名称
	private String pageDisplayName; //页面名称
	private String description; //菜单描述
	private String shortCutLink; //快捷方式的图片地址
	private Boolean isLeafNode; //最终叶子节点
	private MenuItem parentMenuItem; //上级菜单
	private Integer indexNum; //同一个parent下的序号
	private Integer level; //层次从1开始	
	private List<MenuItem> subMenuItemList; //子菜单列表
	
	private String  phoneUrl;//手机菜单
	private Integer isPhone;//是否是手机菜单
	private Integer isPc;//是否是菜单
    private Collection<Permission> permissions = new HashSet<Permission>(); //菜单对应的权限	
    private boolean display=false; //是否显示，不保存到数据库	
    
    public MenuItem(){}
    public MenuItem(Integer id){
    	this.id = id;
    }
    public MenuItem(Integer id, Integer level){
    	this.id = id;
    	this.level = level;
    }
    public MenuItem(Integer id, String clsName, String description, Integer indexNum, 
    		Boolean isLeafNode, Integer level, String name, String pageDisplayName, String projectCode,
    		String shortCutLink, String url, MenuItem parentMenuItem,Integer isPhone,String  phoneUrl){
    	this.id = id;
    	this.clsName = clsName;
    	this.description = description;
    	this.indexNum = indexNum;
    	this.isLeafNode = isLeafNode;
    	this.level = level;
    	this.name = name;
    	this.pageDisplayName = pageDisplayName;
    	this.projectCode = projectCode;
    	this.shortCutLink = shortCutLink;
    	this.url = url;
    	this.parentMenuItem = parentMenuItem;
    	this.isPhone=isPhone;
    	this.phoneUrl=phoneUrl;
    }
    
    @Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="id")    //对应生成策略名称  
    @TableGenerator(name="id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="menu_item_id",            //主键的值  
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
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	@Column
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column
	public String getClsName() {
		return clsName;
	}
	public void setClsName(String clsName) {
		this.clsName = clsName;
	}
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getPageDisplayName() {
		return pageDisplayName;
	}
	public void setPageDisplayName(String pageDisplayName) {
		this.pageDisplayName = pageDisplayName;
	}
	@Column
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column
	public String getShortCutLink() {
		return shortCutLink;
	}
	public void setShortCutLink(String shortCutLink) {
		this.shortCutLink = shortCutLink;
	}
	@Column
	public Boolean getIsLeafNode() {
		return isLeafNode;
	}
	public void setIsLeafNode(Boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_menu_item_id")
	public MenuItem getParentMenuItem() {
		return parentMenuItem;
	}
	public void setParentMenuItem(MenuItem parentMenuItem) {
		this.parentMenuItem = parentMenuItem;
	}
	@Column
	public Integer getIndexNum() {
		return indexNum;
	}
	public void setIndexNum(Integer indexNum) {
		this.indexNum = indexNum;
	}
	@Column
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentMenuItem", targetEntity = MenuItem.class)
    @OrderBy("level")
	public List<MenuItem> getSubMenuItemList() {
		return subMenuItemList;
	}
	public void setSubMenuItemList(List<MenuItem> subMenuItemList) {
		this.subMenuItemList = subMenuItemList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="global_menu_item_permission", joinColumns={@JoinColumn(name="menu_item_id")},
	inverseJoinColumns={@JoinColumn(name="permission_id")})
	public Collection<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Collection<Permission> permissions) {
		this.permissions = permissions;
	}
	@Transient
	public boolean getDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	
	public MenuItem shadowClone(){
		MenuItem mi = new MenuItem();
		mi.setId(this.id);
		mi.setUrl(this.url);
		mi.setName(this.name);
		mi.setLevel(this.level);
		mi.setClsName(this.clsName);
		mi.setDisplay(this.display);
		mi.setParentMenuItem(this.parentMenuItem);
		return mi;
	}
	@Column
	public Integer getIsPhone() {
		return isPhone;
	}
	public void setIsPhone(Integer isPhone) {
		this.isPhone = isPhone;
	}
	@Column
	public String getPhoneUrl() {
		return phoneUrl;
	}
	public void setPhoneUrl(String phoneUrl) {
		this.phoneUrl = phoneUrl;
	}
	@Column
	public Integer getIsPc() {
		return isPc;
	}
	public void setIsPc(Integer isPc) {
		this.isPc = isPc;
	}
}
