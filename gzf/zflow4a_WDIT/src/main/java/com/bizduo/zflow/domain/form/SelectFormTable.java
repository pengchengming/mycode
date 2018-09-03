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

import com.bizduo.zflow.domain.tabulation.MiddleTable;
@Entity
@Table(name="Select_Form_Table")
public class SelectFormTable {
	private Long id;// 表单的id
	private Boolean isFrom;
	private Form form;
	private MiddleTable middleTable;
	private SelectTableList selectTableList;
	
	private String formName;  //formName
	private Integer isPrimaryTable;  //是否是关联表
	private String aliasesName; //别名
	private String associateType;//关联方式   left join  inner join  
	private String  onCondition;// on 后面的条件
	
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="SelectFormTable")    //对应生成策略名称  
	@TableGenerator(name="SelectFormTable", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="SelectFormTable",            //主键的值  
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
	public Boolean getIsFrom() {
		return isFrom;
	}
	public void setIsFrom(Boolean isFrom) {
		this.isFrom = isFrom;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_id")
	public Form getForm() {
		return form;
	}
	public void setForm(Form form) {
		this.form = form;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "middle_Table_id")
	public MiddleTable getMiddleTable() {
		return middleTable;
	}
	public void setMiddleTable(MiddleTable middleTable) {
		this.middleTable = middleTable;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "select_Table_id")
	public SelectTableList getSelectTableList() {
		return selectTableList;
	}
	public void setSelectTableList(SelectTableList selectTableList) {
		this.selectTableList = selectTableList;
	}
	@Column
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	@Column
	public Integer getIsPrimaryTable() {
		return isPrimaryTable;
	}
	public void setIsPrimaryTable(Integer isPrimaryTable) {
		this.isPrimaryTable = isPrimaryTable;
	}
	@Column
	public String getAliasesName() {
		return aliasesName;
	}
	public void setAliasesName(String aliasesName) {
		this.aliasesName = aliasesName;
	}
	@Column
	public String getAssociateType() {
		return associateType;
	}
	public void setAssociateType(String associateType) {
		this.associateType = associateType;
	}
	@Column
	public String getOnCondition() {
		return onCondition;
	}
	public void setOnCondition(String onCondition) {
		this.onCondition = onCondition;
	} 
	
  
}
