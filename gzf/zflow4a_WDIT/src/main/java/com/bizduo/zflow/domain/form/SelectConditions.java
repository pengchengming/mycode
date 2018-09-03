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
@Table(name="select_Conditions") 
public class SelectConditions  implements Serializable{  //条件
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ISESCAPE= "ISESCAPE"; //是否jsp sql转译
	public static final String KEYWORDS= "keywords";
	private Long id;// 表单的id
	private String columnName;
	private String description;
	private FormProperty formProperty;
	private TableProperty tableProperty;
	private SelectTableList selectTableList;
	
	private Integer type;  //1非空行  2 空行      
	private Integer idx;
	private Integer occupyColumn;//占列数  
	private Integer blankColumn;//空列数
	private String extraAttributes;//控件的属性
	private String tdHtml; //单元格手动填写html代码
	private Boolean empty; 
	private String operatorType; //默认等于（=）like in  isnull  
	private Integer isSelectData;//是否获取查询值  1 不用获取值，
	
	public SelectConditions() {
		super();
	}
	public SelectConditions(Long id, String columnName, String description,
			  Integer type, Integer idx,
			Integer occupyColumn, Integer blankColumn, String extraAttributes,
			String tdHtml, Boolean empty,String operatorType,Integer isSelectData) {
		super();
		this.id = id;
		this.columnName = columnName;
		this.description = description;
		this.type = type;
		this.idx = idx;
		this.occupyColumn = occupyColumn;
		this.blankColumn = blankColumn;
		this.extraAttributes = extraAttributes;
		this.tdHtml = tdHtml;
		this.empty = empty;
		this.operatorType=operatorType;
		this.isSelectData=isSelectData;
	}
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="selectConditions")    //对应生成策略名称  
	@TableGenerator(name="selectConditions", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="selectConditions",            //主键的值  
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
	@JoinColumn(nullable = true,name="selectTableList_id") 
	public SelectTableList getSelectTableList() {
		return selectTableList;
	}
	public void setSelectTableList(SelectTableList selectTableList) {
		this.selectTableList = selectTableList;
	}
	@Column
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
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
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	@Column
	public Integer getIsSelectData() {
		return isSelectData;
	}
	public void setIsSelectData(Integer isSelectData) {
		this.isSelectData = isSelectData;
	}
 
}
