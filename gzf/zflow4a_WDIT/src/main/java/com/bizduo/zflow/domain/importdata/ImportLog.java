package com.bizduo.zflow.domain.importdata;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@Entity
@Table(name="Sys_Import_Log")
public class ImportLog implements Serializable{

	private static final long serialVersionUID = 6214784223273055502L;
	private Integer id;
	private String batchNo; //数据的批次code
	private Integer type;//0 读取配置文件(系统错误提示)，1 标题（开始校验，结束校验） 2 错误显示  3 正确行号显示  4结束
	private Integer line;//当前行号
	private String description;
	private Date importDate;
	private String importBy;
	
	public ImportLog(){}
	public ImportLog(String batchNo, Integer type, Integer line, String description, Date importDate, String importBy){
		super();
		this.batchNo = batchNo;
		this.type = type;
		this.line = line;
		this.description = description;
		this.importDate = importDate;
		this.importBy = importBy;
	}
	
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="ImportLog_ID")    //对应生成策略名称  
    @TableGenerator(name="ImportLog_ID", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="ImportLog_ID",            //主键的值  
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
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	@Column
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column
	public Integer getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
	}
	@Column
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	@Column
	public String getImportBy() {
		return importBy;
	}
	public void setImportBy(String importBy) {
		this.importBy = importBy;
	}
}
