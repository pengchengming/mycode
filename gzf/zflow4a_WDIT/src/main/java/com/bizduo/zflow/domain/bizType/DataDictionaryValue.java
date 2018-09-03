package com.bizduo.zflow.domain.bizType;

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

/*** 值*/
@Entity
@Table(name="sys_DataDictionary_Value")
public class DataDictionaryValue  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6247039733793615473L;
	private  Long id ; 
	private DataDictionaryCode dataDictionaryCode ;
	private String value;//值
	private String displayValue;//描述
	private Integer ordinal ;//序号; 
	private String organization;// 所属机构 
	
	public DataDictionaryValue() {
		super();
	}
	public DataDictionaryValue(Long id, String value, String displayValue) {
		super();
		this.id = id;
		this.value = value;
		this.displayValue = displayValue;
	}
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="dm_DataDictionary_Value")    //对应生成策略名称  
    @TableGenerator(name="dm_DataDictionary_Value", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="dm_DataDictionary_Value",            //主键的值  
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
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dataDictionaryCode_id")
	public DataDictionaryCode getDataDictionaryCode() {
		return dataDictionaryCode;
	}
	public void setDataDictionaryCode(DataDictionaryCode dataDictionaryCode) {
		this.dataDictionaryCode = dataDictionaryCode;
	}
	@Column
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	@Column
	public String getDisplayValue() {
		return displayValue;
	}
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	@Column
	public Integer getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	@Column
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
}
