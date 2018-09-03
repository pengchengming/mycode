package com.bizduo.zflow.domain.table;

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

/**字段的信息**/
@Entity
@Table(name="zsql_column")
public class ZColumn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2966322896489477814L;
	public enum  ColType{
		TINYINT,SMALLINT,MEDIUMINT,
		INT,INTEGER,BIGINT,REAL,DOUBLE,FLOAT,DECIMAL,NUMERIC,DATE,TIME,TIMESTAMP,DATETIME,CHAR,
		VARCHAR,TINYBLOB,BLOB,MEDIUMBLOB,LONGBLOB,TINYTEXT,TEXT,MEDIUMTEXT,LONGTEXT
	};
	private Long id;
	private ZTable ztable;
	/**字段名称**/
	@Column(name="col_name")
	private String colName;
	/**类型**/
	@Column(name="col_Type")
	private ColType colType;
	/**是否必填**/
	private Boolean mandatory=false;
	/**默认值**/
	private String  defaultValue;
	/**自增**/
	private Boolean autoIncrement=false;
	/**长度**/
	private Long length;
	private Long decimals;
	/**是否为主键**/
	private Boolean  primaryKey=false;
	/****/
	private String unique; 
	private String primary; 
	/**序列**/
	private Integer  serialValue;
	/**外键的key**/
	private String foreignKey;
	/**外键字段**/
	private String foreignColumn;
	/**外键对象**/
	private String foreignObj;
	/**描述**/
	private String comment;
	
	private Boolean unsigned=false;
	private Boolean zerofill=false;
	private Boolean binary=false; 
	private Boolean ascii=false;
	private Boolean unicode=false;
	//数据库取出字段
	private String jsonData;
	public ZColumn(){}
	public ZColumn(String colName,ColType colType,Long length,Boolean primaryKey,String comment,ZTable ztable){
		this.colName=colName;
		this.colType=colType;
		this.length=length;
		this.primaryKey=primaryKey;
		this.comment=comment;
		this.ztable=ztable;
	}
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zsql_column")    //对应生成策略名称  
	@TableGenerator(name="zsql_column", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zsql_column",            //主键的值  
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
	@JoinColumn(name = "zql_table_id")
	public ZTable getZtable() {
		return ztable;
	}
	public void setZtable(ZTable ztable) {
		this.ztable = ztable;
	}
	@Column(name="col_name")
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	@Column(name="col_Type")
	public ColType getColType() {
		return colType;
	}
	public void setColType(ColType colType) {
		this.colType = colType;
	}
	@Column(name="zsql_mandatory")
	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	@Column(name="default_Value")
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	@Column(name="auto_Increment")
	public Boolean getAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(Boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	@Column
	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}
	@Column(name="zsql_decimals")
	public Long getDecimals() {
		return decimals;
	}
	public void setDecimals(Long decimals) {
		this.decimals = decimals;
	}
	@Column(name="primary_Key")
	public Boolean getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	@Column(name="zsql_unique")
	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}
	@Column(name="zsql_primary")
	public String getPrimary() {
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
	}
	@Column(name="zsql_serialValue")
	public Integer getSerialValue() {
		return serialValue;
	}
	public void setSerialValue(Integer serialValue) {
		this.serialValue = serialValue;
	}
	@Column(name="zsql_foreignKey")
	public String getForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}
	@Column(name="zsql_foreignColumn")
	public String getForeignColumn() {
		return foreignColumn;
	}
	public void setForeignColumn(String foreignColumn) {
		this.foreignColumn = foreignColumn;
	}
	@Column(name="zsql_foreignObj")
	public String getForeignObj() {
		return foreignObj;
	}
	public void setForeignObj(String foreignObj) {
		this.foreignObj = foreignObj;
	}
	@Column(name="zsql_comment")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Column(name="zsql_unsigned")
	public Boolean getUnsigned() {
		return unsigned;
	}
	public void setUnsigned(Boolean unsigned) {
		this.unsigned = unsigned;
	}
	@Column(name="zsql_zerofill")
	public Boolean getZerofill() {
		return zerofill;
	}
	public void setZerofill(Boolean zerofill) {
		this.zerofill = zerofill;
	}
	@Column(name="zsql_binary")
	public Boolean getBinary() {
		return binary;
	}
	public void setBinary(Boolean binary) {
		this.binary = binary;
	}
	@Column(name="zsql_ascii")
	public Boolean getAscii() {
		return ascii;
	}
	public void setAscii(Boolean ascii) {
		this.ascii = ascii;
	}
	@Column(name="zsql_unicode")
	public Boolean getUnicode() {
		return unicode;
	}
	public void setUnicode(Boolean unicode) {
		this.unicode = unicode;
	}
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}


}
