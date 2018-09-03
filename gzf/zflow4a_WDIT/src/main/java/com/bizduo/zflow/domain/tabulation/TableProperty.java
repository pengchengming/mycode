package com.bizduo.zflow.domain.tabulation;

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
@Entity
@Table(name="zflow_table_property")
public class TableProperty implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6394329044144319845L;
	private Long id;
	private MiddleTable middleTable;
	private Long tablePropertyId;//标识
	/**
	 * 字段类型
	 */
	private String tablePropertyType;
	/**
	 * 中文名称
	 */
	private String tablePropertyLabel;
	/**
	 * 字段名称
	 */
	private String tablePropertyName;
	/**
	 * 字段长度
	 */
	private Long tablePropertyLength;

	private ZColumn zcolumn;

	private String elementType;//页面控制元素
	private String dictionaryCode;//数据字典 代码的Code
	private String extraAttributes;//控件的属性
	private String foreignKey;//外键表
	/*******************************************************************************************************/
	/********************************* GETTER AND SETTER ***************************************************/
	/*******************************************************************************************************/
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zflow_table_property")    //对应生成策略名称  
	@TableGenerator(name="zflow_table_property", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zflow_table_property",            //主键的值  
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "middle_table_id")
	public MiddleTable getMiddleTable() {
		return middleTable;
	}
	public void setMiddleTable(MiddleTable middleTable) {
		this.middleTable = middleTable;
	}
	@Column
	public Long getTablePropertyId() {
		return tablePropertyId;
	}
	public void setTablePropertyId(Long tablePropertyId) {
		this.tablePropertyId = tablePropertyId;
	}
	@Column
	public String getTablePropertyType() {
		return tablePropertyType;
	}
	public void setTablePropertyType(String tablePropertyType) {
		this.tablePropertyType = tablePropertyType;
	}
	@Column
	public String getTablePropertyLabel() {
		return tablePropertyLabel;
	}
	public void setTablePropertyLabel(String tablePropertyLabel) {
		this.tablePropertyLabel = tablePropertyLabel;
	}
	@Column
	public String getTablePropertyName() {
		return tablePropertyName;
	}
	public void setTablePropertyName(String tablePropertyName) {
		this.tablePropertyName = tablePropertyName;
	}
	@Column
	public Long getTablePropertyLength() {
		return tablePropertyLength;
	}
	public void setTablePropertyLength(Long tablePropertyLength) {
		this.tablePropertyLength = tablePropertyLength;
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
	public String getDictionaryCode() {
		return dictionaryCode;
	}
	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}
	@Column
	public String getExtraAttributes() {
		return extraAttributes;
	}
	public void setExtraAttributes(String extraAttributes) {
		this.extraAttributes = extraAttributes;
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
}
