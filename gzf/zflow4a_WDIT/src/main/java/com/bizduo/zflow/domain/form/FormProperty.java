package com.bizduo.zflow.domain.form;

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

import com.bizduo.zflow.domain.table.ZColumn;

/**
* @author lm
* @version 创建时间：2012-3-28 下午03:57:11
* 表单的属性
*/
@Entity
@Table(name="zflow_form_property")
public class FormProperty implements Serializable{
	private static final long serialVersionUID = 1119672613006640128L;
	private Long id;
	private Long fieldId;//接受从前台传来的id
	private String fieldType;//属性类型   数据库字段类型
	private String fieldName;//属性名称 
	private Integer fieldLength;//数据库 属性长度
	private Integer minLength;//最小输入长度
	private Integer isDelete;//判断表单属性是否删除 
	private String  comment; //描述
	private Form form;
	private ZColumn zcolumn;
	private String elementType;//页面控制元素
	private String dictionaryCode;//数据字典 代码的Code
	private String extraAttributes;//控件的属性
	private String bindJs;
	private String foreignKey;
	
	//private Integer  occupyRow;//占居的列
	
	private Boolean empty;
	private String validator;
	
	private String dataType;
	
	public FormProperty() { 
	}
	public FormProperty(Long id, Long fieldId, String fieldType,
			String fieldName, Integer fieldLength, Integer minLength,
			Integer isDelete, String comment, 
			String elementType, String dictionaryCode,
			String extraAttributes, String bindJs, String foreignKey,
			  Boolean empty, String validator, String dataType) {
		this.id = id;
		this.fieldId = fieldId;
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.fieldLength = fieldLength;
		this.minLength = minLength;
		this.isDelete = isDelete;
		this.comment = comment; 
		this.elementType = elementType;
		this.dictionaryCode = dictionaryCode;
		this.extraAttributes = extraAttributes;
		this.bindJs = bindJs;
		this.foreignKey = foreignKey;
		this.empty = empty;
		this.validator = validator; 
		this.dataType=dataType;
	}
	//=======================get  set=====================================//
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zflow_form_property")    //对应生成策略名称  
	@TableGenerator(name="zflow_form_property", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zflow_form_property",            //主键的值  
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
	public Long getFieldId() {
		return fieldId;
	}
	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}
	@Column
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	@Column
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="form_id")
	public Form getForm() {
		return form;
	}
	public void setForm(Form form) {
		this.form = form;
	}
	@Column
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormProperty other = (FormProperty) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "zcolumn_id")
	public ZColumn getZcolumn() {
		return zcolumn;
	}
	public void setZcolumn(ZColumn zcolumn) {
		this.zcolumn = zcolumn;
	}
	@Column
	public Integer getFieldLength() {
		return fieldLength;
	}
	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}
	@Column
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	@Column(name="form_comment")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	@Column
	public String getExtraAttributes() {
		return extraAttributes;
	}
	public void setExtraAttributes(String extraAttributes) {
		this.extraAttributes = extraAttributes;
	}
	@Column
	public String getDictionaryCode() {
		return dictionaryCode;
	}
	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}
	@Column
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	@Column
	public String getForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}
	@Column
	public String getBindJs() {
		return bindJs;
	}
	public void setBindJs(String bindJs) {
		this.bindJs = bindJs;
	}
	@Column
	public Boolean getEmpty() {
		return empty;
	}
	public void setEmpty(Boolean empty) {
		this.empty = empty;
	}
	@Column
	public String getValidator() {
		return validator;
	}
	public void setValidator(String validator) {
		this.validator = validator;
	}
	@Column
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	 
}
