package com.bizduo.zflow.domain.sys;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@Entity
@Table(name="global_organization")
@JsonIgnoreProperties({"organizations"})
public class Organization implements Serializable {
	private static final long serialVersionUID = -5803290762774839209L;
	
	private Integer id;
	private String name; //组织架构名称
	private Integer type;
	private String acronym;
	private String description; //描述
	private Organization parent; //上级组织架构
	private Integer indexNum; //同一个parent下的序号
	private Integer level; //层次从1开始	
	private Integer nlevel;
	private Boolean isParent;
	private String address;
	private Date createDate;
	private String createBy;
	private Date modifyDate;
	private String modifyBy;
    private Integer entityid;//分公司信息
    private Integer report2;//上级人员
	private boolean display = false; //是否显示，不保存到数据库	
	
	/****xd	 **/
	private String contactname; //联系人名字
	private String contactphone; //联系人电话
	private Integer valid;//是否有效   1为有效  2为无效
	
	
	public Organization(){
		super();
	}
	
	public Organization (Integer id){ 
		super();
		this.id = id; 
	}
	public Organization(Integer id, Integer level){
		super();
		this.id = id;
		this.level = level;
	}
	public Organization(Integer id, String name){
		super();
		this.id = id;
		this.name = name;
	}
	public Organization(Integer id, String name, String acronym, Integer type){
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.acronym = acronym;
	}
	public Organization(Integer id, String name, String description, Integer indexNum, Organization parent){
		this.id = id;
		this.name = name;
		this.description = description;
		this.indexNum = indexNum;
		this.parent = parent;
	}
    
    @Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="id")    //对应生成策略名称  
    @TableGenerator(name="id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="organization_id",            //主键的值  
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	public Organization getParent() {
		return parent;
	}
	public void setParent(Organization parent) {
		this.parent = parent;
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
	@Column
	public Integer getNlevel() {
		return nlevel;
	}
	public void setNlevel(Integer nlevel) {
		this.nlevel = nlevel;
	}
	@Transient
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	} 
	
	public Organization shadowClone(Organization o){
		Organization org = new Organization();
		org.setId(o.getId());
		org.setName(o.getName());
		org.setLevel(o.getLevel());
		org.setDisplay(display);
		org.setParent(o.getParent());
		return org;
	}
	
	public Organization shadowCloneBySelector(Organization o){
		return new Organization(o.getId(), o.getName(), o.getAcronym(), o.getType());
	}
	@Column
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	@Column
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column
	public String getModifyBy() {
		return modifyBy;
	}
	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	@Column
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	@Column
	public Integer getEntityid() {
		return entityid;
	}
	public void setEntityid(Integer entityid) {
		this.entityid = entityid;
	}
	@Column
	public Integer getReport2() {
		return report2;
	}
	public void setReport2(Integer report2) {
		this.report2 = report2;
	}
	
	/****xd	 **/
	@Column
	public String getContactname() {
		return contactname;
	}
	public void setContactname(String contactname) {
		this.contactname = contactname;
	}
	@Column
	public String getContactphone() {
		return contactphone;
	}
	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}
	@Column
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	
	
}
