package com.bizduo.zflow.domain.table;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="select_conditionSelect")
public class ConditionSelect implements Serializable{
	/**
	 * 表单向导
	 */
	private static final long serialVersionUID = 1863064303315314333L;
	private Long id ;
	private String name; //表名
	private String code;//
	private String description; //描述
	private  ZTable ztable;
	//private  Collection<SelectColumn> selectColumns = new HashSet<SelectColumn>();
	//private Collection<ConditionAction> conditionActions=new HashSet<ConditionAction>();
	private Boolean status=false; //(true完成 前面可根据条件查看)；
	private Date createDate;
	private String selectFieldList;//查询条件
	private String columnFieldList;	//查询列
	private String actionChecks;	//操作
	
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="select_conditionSelect")    //对应生成策略名称  
	@TableGenerator(name="select_conditionSelect", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="select_conditionSelect",            //主键的值  
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ztable")
	public ZTable getZtable() {
		return ztable;
	}
	public void setZtable(ZTable ztable) {
		this.ztable = ztable;
	}
	@Column(length=400)
	public String getSelectFieldList() {
		return selectFieldList;
	}
	public void setSelectFieldList(String selectFieldList) {
		this.selectFieldList = selectFieldList;
	}
	@Column(length=400)
	public String getColumnFieldList() {
		return columnFieldList;
	}
	public void setColumnFieldList(String columnFieldList) {
		this.columnFieldList = columnFieldList;
	}
	@Column
	public String getActionChecks() {
		return actionChecks;
	}
	public void setActionChecks(String actionChecks) {
		this.actionChecks = actionChecks;
	}
	@Column
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	@Column
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
