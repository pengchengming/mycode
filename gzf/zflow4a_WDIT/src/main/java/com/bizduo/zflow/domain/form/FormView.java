package com.bizduo.zflow.domain.form;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="zflow_form_view")
@JsonIgnoreProperties(value = {"propertyList"})
public class FormView {
	private Long id;
	private String code;
	private Form form;
	private Integer occupyColumn;//生成Table时的列数
	private List<FormViewProperty> propertyList;
	
	public FormView() {
		super();
	}
	public FormView(Long id, String code,  Integer occupyColumn,
			List<FormViewProperty> propertyList) {
		super();
		this.id = id;
		this.code = code;
		this.occupyColumn = occupyColumn;
		this.propertyList = propertyList;
	}
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zflow_form_view")    //对应生成策略名称  
	@TableGenerator(name="zflow_form_view", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zflow_form_view",            //主键的值  
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
	@Column
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column
	public Integer getOccupyColumn() {
		return occupyColumn;
	}
	public void setOccupyColumn(Integer occupyColumn) {
		this.occupyColumn = occupyColumn;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true,name="form_id")
	public Form getForm() {
		return form;
	}
	public void setForm(Form form) {
		this.form = form;
	}

	@OneToMany(fetch=FetchType.EAGER,mappedBy="formView")
	@Cascade(value = { CascadeType.ALL })
	public List<FormViewProperty> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<FormViewProperty> propertyList) {
		this.propertyList = propertyList;
	}
}
