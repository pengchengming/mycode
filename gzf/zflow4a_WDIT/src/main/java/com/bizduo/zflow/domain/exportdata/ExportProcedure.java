package com.bizduo.zflow.domain.exportdata;
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
@Table(name="Sys_Export_Procedure")
public class ExportProcedure implements Serializable{

	private static final long serialVersionUID = -4320149415055020701L;

	private Integer id;
	private String name;
	private String paramIndexColumn;//1,3 多个参数设置
	private String paramIndexValue;//1,3 多个参数设置
	private String description;
	private ExportExcel exportExcel;
	
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="ExportProcedure_id")    //对应生成策略名称  
    @TableGenerator(name="ExportProcedure_id", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="ExportProcedure_id",            //主键的值  
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Excel_ID")
	public ExportExcel getExportExcel() {
		return exportExcel;
	}
	public void setExportExcel(ExportExcel exportExcel) {
		this.exportExcel = exportExcel;
	}
	@Column
	public String getParamIndexColumn() {
		return paramIndexColumn;
	}
	public void setParamIndexColumn(String paramIndexColumn) {
		this.paramIndexColumn = paramIndexColumn;
	}
	@Column
	public String getParamIndexValue() {
		return paramIndexValue;
	}
	public void setParamIndexValue(String paramIndexValue) {
		this.paramIndexValue = paramIndexValue;
	}
}
