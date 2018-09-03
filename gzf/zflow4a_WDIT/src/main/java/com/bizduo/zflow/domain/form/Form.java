package com.bizduo.zflow.domain.form;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bizduo.zflow.domain.table.ZTable;

/**
 * @author lm
 * @version 创建时间：2012-3-28 下午02:10:51 表单--实体类
 */
@Entity
@Table(name="zflow_form")
@JsonIgnoreProperties(value = {"propertyList"})
public class Form implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4054604347049164070L;
	private Long id;// 表单的id
	private String formName;//数据库表中name
	private String formCode;//  表单的name
	private String formHtml;// 表单的html
	private Integer isDelete;// 判断表单是否删除
	private List<FormProperty> propertyList;// 表单的表和表单属性的表相关联
	private ZTable ztable;
	private Boolean isPublish=false; 
	private Integer isBaseFrom ;  //0 否，1 是，2 已经发布 
	private Long baseFormId;
	private Integer ishistoryTable; //
	
	// =======================GETTER AND SETTER=============================//
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zflow_form")    //对应生成策略名称  
	@TableGenerator(name="zflow_form", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zflow_form",            //主键的值  
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
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}
	@Column
	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	@Column(length=400)
	public String getFormHtml() {
		return formHtml;
	}

	public void setFormHtml(String formHtml) {
		this.formHtml = formHtml;
	}

	public Integer getIsDelete() {
		return isDelete;
	}
	@Column
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	@OneToMany(fetch=FetchType.EAGER,mappedBy="form")
	@Cascade(value = { CascadeType.ALL })
	public List<FormProperty> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<FormProperty> propertyList) {
		this.propertyList = propertyList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ztable_id")
	public ZTable getZtable() {
		return ztable;
	}
	public void setZtable(ZTable ztable) {
		this.ztable = ztable;
	}
	@Column
	public Boolean getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}
	
	public Form shadowClone(){
		Form form = new Form();
		form.setId(this.id);
		form.setFormCode(formCode);
		form.setFormName(this.formName);
		return form;
	}
	@Column
	public Integer getIsBaseFrom() {
		return isBaseFrom;
	}
	public void setIsBaseFrom(Integer isBaseFrom) {
		this.isBaseFrom = isBaseFrom;
	}
	@Column
	public Long getBaseFormId() {
		return baseFormId;
	}
	public void setBaseFormId(Long baseFormId) {
		this.baseFormId = baseFormId;
	}
	@Column
	public Integer getIshistoryTable() {
		return ishistoryTable;
	}

	public void setIshistoryTable(Integer ishistoryTable) {
		this.ishistoryTable = ishistoryTable;
	} 
 
 
}
