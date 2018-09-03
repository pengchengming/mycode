package com.bizduo.zflow.domain.form;

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
@Table(name="zflow_viewPage_property")
public class ViewPageProperty {
	private Long id;	
	private ViewPage viewPage;
	private Integer type;  //1非空行  2 空行     
	private Integer idx;  //顺序
	private Integer occupyColumn;//占列数  
	private Integer blankColumn;//空列数
	private FormProperty formProperty;  
	/**当formProperty 为空时*****/
	private String columnName;  //字段名称
	private String aliasesName; //别名
	private String description; //字段描述
	private Integer dictionaryCode; 
	/*******/
	private String extraAttributes;//控件的属性
	private String tdHtml; //单元格手动填写html代码
	private Boolean empty;
	private String fieldType;
	
	
	
	public ViewPageProperty() {
		super();
	}
 
	
	public ViewPageProperty(Long id, Integer type, Integer idx,
			Integer occupyColumn, Integer blankColumn, String columnName,
			String aliasesName, String description, Integer dictionaryCode,
			String extraAttributes,String tdHtml,  Boolean empty,String fieldType) {
		super();
		this.id = id;
		this.type = type;
		this.idx = idx;
		this.occupyColumn = occupyColumn;
		this.blankColumn = blankColumn;
		this.columnName = columnName;
		this.aliasesName = aliasesName;
		this.description = description;
		this.dictionaryCode=dictionaryCode;
		this.tdHtml=tdHtml;
		this.extraAttributes = extraAttributes; 
		this.empty = empty;
		this.fieldType=fieldType;
	}

	public ViewPageProperty(ViewPage viewPage, Integer type, Integer idx,
			Integer occupyColumn, Integer blankColumn, String columnName, String aliasesName,
			String description,   String extraAttributes,
			 Boolean empty) {
		super();
		this.viewPage = viewPage;
		this.type = type;
		this.idx = idx;
		this.occupyColumn = occupyColumn;
		this.blankColumn = blankColumn; 
		this.columnName = columnName;
		this.aliasesName = aliasesName;
		this.description = description; 
		this.extraAttributes = extraAttributes; 
		this.empty = empty;
	}

	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zflow_viewPage_property")    //对应生成策略名称  
	@TableGenerator(name="zflow_viewPage_property", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zflow_viewPage_property",            //主键的值  
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
	@JoinColumn(nullable = true,name="view_Page_id")
	public ViewPage getViewPage() {
		return viewPage;
	}
	public void setViewPage(ViewPage viewPage) {
		this.viewPage = viewPage;
	}
	@Column
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	@Column
	public Integer getOccupyColumn() {
		return occupyColumn;
	}
	public void setOccupyColumn(Integer occupyColumn) {
		this.occupyColumn = occupyColumn;
	}
	@Column
	public Integer getBlankColumn() {
		return blankColumn;
	}
	public void setBlankColumn(Integer blankColumn) {
		this.blankColumn = blankColumn;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="form_property_id")
	public FormProperty getFormProperty() {
		return formProperty;
	}
	public void setFormProperty(FormProperty formProperty) {
		this.formProperty = formProperty;
	}
	@Column
	public String getExtraAttributes() {
		return extraAttributes;
	}
	public void setExtraAttributes(String extraAttributes) {
		this.extraAttributes = extraAttributes;
	}
	@Column
	public String getTdHtml() {
		return tdHtml;
	}
	public void setTdHtml(String tdHtml) {
		this.tdHtml = tdHtml;
	}
	@Column
	public Boolean getEmpty() {
		return empty;
	}
	public void setEmpty(Boolean empty) {
		this.empty = empty;
	}
	@Column
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	@Column
	public String getAliasesName() {
		return aliasesName;
	}
	public void setAliasesName(String aliasesName) {
		this.aliasesName = aliasesName;
	}
	@Column
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column
	public Integer getDictionaryCode() {
		return dictionaryCode;
	}
	public void setDictionaryCode(Integer dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}
	@Column
	public String getFieldType() {
		return fieldType;
	} 
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	
}
