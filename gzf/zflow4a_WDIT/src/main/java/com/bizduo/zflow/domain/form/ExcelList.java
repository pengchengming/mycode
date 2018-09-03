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

import com.bizduo.zflow.domain.tabulation.TableProperty;

@Entity
@Table(name="sys_excel_list") 
public class ExcelList implements Serializable{  //列
	/***/
	private static final long serialVersionUID = 1L;
	private Long id;// 表单的id
	private String columnName;  //字段名称
	private String aliasesName; //别名
	private String description; //字段描述
	private Integer  isDisplay; //是否显示   1显示 0 不显示  
	private Integer idx; //排序
	private FormProperty formProperty;
	private TableProperty tableProperty;
	private ExcelTableList excelTableList;
	private String exportType;
	
	public ExcelList() {
		super();
	} 
	public ExcelList(Long id, String columnName, String aliasesName,
			String description, Integer isDisplay, Integer idx,String exportType) {
		super();
		this.id = id;
		this.columnName = columnName;
		this.aliasesName = aliasesName;
		this.description = description;
		this.isDisplay = isDisplay;
		this.idx = idx;
		this.exportType=exportType;
	}

	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="excelList")    //对应生成策略名称  
	@TableGenerator(name="excelList", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="excelList",            //主键的值  
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
	@JoinColumn(name = "form_property_id")
	public FormProperty getFormProperty() {
		return formProperty;
	}
	public void setFormProperty(FormProperty formProperty) {
		this.formProperty = formProperty;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_property_id")
	public TableProperty getTableProperty() {
		return tableProperty;
	}
	public void setTableProperty(TableProperty tableProperty) {
		this.tableProperty = tableProperty;
	}
	@Column(length=50)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="exclTableList_id")
	public ExcelTableList getExcelTableList() {
		return excelTableList;
	}
	public void setExcelTableList(ExcelTableList excelTableList) {
		this.excelTableList = excelTableList;
	}
	@Column
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	@Column
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	@Column
	public String getAliasesName() {
		return aliasesName;
	}
	public void setAliasesName(String aliasesName) {
		this.aliasesName = aliasesName;
	}
	@Column
	public Integer getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}
	@Column
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

}
