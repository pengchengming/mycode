package com.bizduo.zflow.domain.form;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
@Entity
@Table(name="sys_excel_table_list") 
@JsonIgnoreProperties({"excelList"})
public class ExcelTableList  implements Serializable{ //table 集合
	/** *  */
	private static final long serialVersionUID = 1L;
	private Long id;// 表单的id 
	private String code; //标题
	private String description;//描述
	private String excelTableSql; //select 语句
	private String excelFromSql;// 用于查询计数
	private String orderBy; //排序
	private List<ExcelList> excelList;
	 
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="excelTableList")    //对应生成策略名称  
	@TableGenerator(name="excelTableList", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="excelTableList",            //主键的值  
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column
	public String getExcelTableSql() {
		return excelTableSql;
	}
	public void setExcelTableSql(String excelTableSql) {
		this.excelTableSql = excelTableSql;
	}
	@Column
	public String getExcelFromSql() {
		return excelFromSql;
	}
	public void setExcelFromSql(String excelFromSql) {
		this.excelFromSql = excelFromSql;
	}
	@Column
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	@OneToMany(fetch=FetchType.LAZY,mappedBy="excelTableList")
	@Cascade(value = { CascadeType.ALL })
	public List<ExcelList> getExcelList() {
		return excelList;
	}
	public void setExcelList(List<ExcelList> excelList) {
		this.excelList = excelList;
	} 
	
 
}
