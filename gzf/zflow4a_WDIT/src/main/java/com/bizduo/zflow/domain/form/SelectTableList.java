package com.bizduo.zflow.domain.form;

import java.io.Serializable;
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
@Table(name="select_Table_List") 
@JsonIgnoreProperties({"selectformTables","selectConditionsList","selectLists"})
public class SelectTableList  implements Serializable{ //table 集合
	/** *  */
	private static final long serialVersionUID = 1L;
	private Long id;// 表单的id 
	private String code; //标题
	private String description;//描述
	private String selectTableSql; //select 语句
	private String selectFromSql;// 用于查询计数
	private String orderBy; //排序
	private String groupBy;//分组
//	private List<SelectFormTable> selectformTables; //表的机会，用字段区分 from和middleTable

	private Integer occupyColumn;//生成Table时的列数
	private List<SelectConditions> selectConditionsList;
	private List<SelectList> selectLists;
	
	private SelectTableList selectTableListSubset;
	private String subCondition;//子集对应的查询Id
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="selectTableList")    //对应生成策略名称  
	@TableGenerator(name="selectTableList", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="selectTableList",            //主键的值  
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
	@OneToMany(fetch=FetchType.LAZY,mappedBy="selectTableList")
	@Cascade(value = { CascadeType.ALL })
	public List<SelectConditions> getSelectConditionsList() {
		return selectConditionsList;
	}
	public void setSelectConditionsList(List<SelectConditions> selectConditionsList) {
		this.selectConditionsList = selectConditionsList;
	}
	@OneToMany(fetch=FetchType.LAZY,mappedBy="selectTableList")
	@Cascade(value = { CascadeType.ALL })
	public List<SelectList> getSelectLists() {
		return selectLists;
	}
	public void setSelectLists(List<SelectList> selectLists) {
		this.selectLists = selectLists;
	}
//	@OneToMany(fetch=FetchType.EAGER,mappedBy="selectTableList")
//	@Cascade(value = { CascadeType.ALL })
//	public List<SelectFormTable> getSelectformTables() {
//		return selectformTables;
//	}
//	public void setSelectformTables(List<SelectFormTable> selectformTables) {
//		this.selectformTables = selectformTables;
//	} 
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
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	@Column
	public Integer getOccupyColumn() {
		return occupyColumn;
	}
	public void setOccupyColumn(Integer occupyColumn) {
		this.occupyColumn = occupyColumn;
	}
	@Column
	public String getSelectTableSql() {
		return selectTableSql;
	}
	public void setSelectTableSql(String selectTableSql) {
		this.selectTableSql = selectTableSql;
	}
	@Column 
	public String getSelectFromSql() {
		return selectFromSql;
	}
	public void setSelectFromSql(String selectFromSql) {
		this.selectFromSql = selectFromSql;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "select_Table_sub_id")
	public SelectTableList getSelectTableListSubset() {
		return selectTableListSubset;
	}
	public void setSelectTableListSubset(SelectTableList selectTableListSubset) {
		this.selectTableListSubset = selectTableListSubset;
	}
	@Column 
	public String getSubCondition() {
		return subCondition;
	}
	public void setSubCondition(String subCondition) {
		this.subCondition = subCondition;
	}
	@Column 
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
 
 
}
