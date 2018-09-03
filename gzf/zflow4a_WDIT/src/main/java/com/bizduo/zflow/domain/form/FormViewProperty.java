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
@Table(name="zflow_form_view_property")
public class FormViewProperty {	
	private Long id;	
	private FormView formView;
	private Integer type;  //1非空行  2 空行    
	private FormProperty formProperty;    
	private Integer idx;  //顺序
	private Integer occupyColumn;//占列数  
	private Integer blankColumn;//空列数

	private String extraAttributes;//控件的属性
	private String tdHtml; //单元格手动填写html代码
	private Boolean empty;
	
	public FormViewProperty() {
		super();
	}
	public FormViewProperty(Long id,   Integer type,
			Integer idx, Integer occupyColumn, Integer blankColumn,Boolean empty,String tdHtml,String extraAttributes) {
		super();
		this.id = id; 
		this.type = type;
		this.idx = idx;
		this.occupyColumn = occupyColumn;
		this.blankColumn = blankColumn;
		
		this.empty=empty;
		this.tdHtml=tdHtml;
		this.extraAttributes=extraAttributes;
	}

	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zflow_form_view_property")    //对应生成策略名称  
	@TableGenerator(name="zflow_form_view_property", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zflow_form_view_property",            //主键的值  
		    valueColumnName="next_val", //生成的值  列名  
		    table="id_table",            //生成的表名  
		    initialValue=1000,//   [主键初识值，默认为0。]  
		    //catalog schema   指定表所在的目录名或是数据库名。  
		    allocationSize=1) //主键每次增加的大小,默认为50 
	@Column
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="form_view_id")
	public FormView getFormView() {
		return formView;
	}
	public void setFormView(FormView formView) {
		this.formView = formView;
	}
	@Column
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public String getExtraAttributes() {
		return extraAttributes;
	}
	public void setExtraAttributes(String extraAttributes) {
		this.extraAttributes = extraAttributes;
	} 
}
