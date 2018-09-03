package com.bizduo.zflow.domain.importdata;

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
@Entity
@Table(name="Sys_Import_Column")
public class ImportColumn implements Serializable{

	private static final long serialVersionUID = 7085402739701051885L;

	private Integer id;
	private String name;
	private String description;
	private String exlCode;
	private Integer exlType;
	private Integer exlLength;
	private Boolean exlIsNull;
	private Integer sheet;
	private ImportTable importTable;
	
    @Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="ImportColumn_id")    //对应生成策略名称  
    @TableGenerator(name="ImportColumn_id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="ImportColumn_id",            //主键的值  
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column
	public String getExlCode() {
		return exlCode;
	}
	public void setExlCode(String exlCode) {
		this.exlCode = exlCode;
	}
	@Column
	public Integer getExlType() {
		return exlType;
	}
	public void setExlType(Integer exlType) {
		this.exlType = exlType;
	}
	@Column
	public Integer getExlLength() {
		return exlLength;
	}
	public void setExlLength(Integer exlLength) {
		this.exlLength = exlLength;
	}
	@Column
	public Boolean getExlIsNull() {
		return exlIsNull;
	}
	public void setExlIsNull(Boolean exlIsNull) {
		this.exlIsNull = exlIsNull;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Table_ID")
	public ImportTable getImportTable() {
		return importTable;
	}
	public void setImportTable(ImportTable importTable) {
		this.importTable = importTable;
	}
	@Column
	public Integer getSheet() {
		return sheet;
	}
	public void setSheet(Integer sheet) {
		this.sheet = sheet;
	}
}
