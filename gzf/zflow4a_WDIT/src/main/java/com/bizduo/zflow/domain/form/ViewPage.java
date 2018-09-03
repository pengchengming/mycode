package com.bizduo.zflow.domain.form;

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
@Table(name="zflow_viewpage")
@JsonIgnoreProperties(value = {"propertyList"})
public class ViewPage {
	private Long id;
	private String code;
	private Integer occupyColumn;//生成Table时的列数
	private String viewSql;
	private List<ViewPageProperty> propertyList;
	
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zflow_viewPage")    //对应生成策略名称  
	@TableGenerator(name="zflow_viewPage", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zflow_viewPage",            //主键的值  
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
	@Column
	public String getViewSql() {
		return viewSql;
	}
	public void setViewSql(String viewSql) {
		this.viewSql = viewSql;
	}
	@OneToMany(fetch=FetchType.EAGER,mappedBy="viewPage")
	@Cascade(value = { CascadeType.ALL })
	public List<ViewPageProperty> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<ViewPageProperty> propertyList) {
		this.propertyList = propertyList;
	}

	
}
