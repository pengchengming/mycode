package com.bizduo.zflow.domain.tabulation;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.table.ZTable;

/**
 * 这个表建立了form和table之间的关系 form 和 middleTable 是一对多的关系 middletable 和 table 是一对多的关系
 * 
 * @author lm
 * @version 创建时间：2012-4-26 上午10:29:42
 */
@Entity
@Table(name="zflow_middle_table")
public class MiddleTable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4053447375884167033L;
	private Long id;
	private String middleTableName;
	private Long middleTableId;
	private Form form;
	private Integer isDelete;// 判断列表是否删除
	private List<TableProperty> tablePropertyList;
	private ZTable ztable;
	// =======================GETTER AND SETTER=============================//
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zflow_middle_table")    //对应生成策略名称  
	@TableGenerator(name="zflow_middle_table", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zflow_middle_table",            //主键的值  
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
	public String getMiddleTableName() {
		return middleTableName;
	}
	public void setMiddleTableName(String middleTableName) {
		this.middleTableName = middleTableName;
	}
	@Column
	public Long getMiddleTableId() {
		return middleTableId;
	}
	public void setMiddleTableId(Long middleTableId) {
		this.middleTableId = middleTableId;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_id")
	public Form getForm() {
		return form;
	}
	public void setForm(Form form) {
		this.form = form;
	}
	@Column
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	@OneToMany(fetch=FetchType.EAGER,mappedBy="middleTable")
	@Cascade(value = { CascadeType.ALL })
	public List<TableProperty> getTablePropertyList() {
		return tablePropertyList;
	}
	public void setTablePropertyList(List<TableProperty> tablePropertyList) {
		this.tablePropertyList = tablePropertyList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ztable_id")
	public ZTable getZtable() {
		return ztable;
	}
	public void setZtable(ZTable ztable) {
		this.ztable = ztable;
	}
}
