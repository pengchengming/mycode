package com.bizduo.zflow.domain.table;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

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

/**表的信息**/
@Entity
@Table(name="zsql_table")
public class ZTable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6108864282349177710L;
	private Long id;
	/**数据库名称**/
	private String tablename;
	/**ZDataBase*/
	private ZDataBase zdataBase;
	/**字段的集合**/
    private Collection<ZColumn> zcolumns = new HashSet<ZColumn>();
	//insert
	private Boolean lowPrinority =false;
	private Boolean highPriority =false;
	private Boolean delayed =false;
	private Boolean ignore =false;
	//delete
	private Boolean quick  =false; //[QUICK] [IGNORE]
	private String orderBy  ; 
	private  Integer rowCount ;
	private String description;
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zsql_table")    //对应生成策略名称  
	@TableGenerator(name="zsql_table", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zsql_table",            //主键的值  
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
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ZDataBase")
	public ZDataBase getZdataBase() {
		return zdataBase;
	}
	public void setZdataBase(ZDataBase zdataBase) {
		this.zdataBase = zdataBase;
	}
	@Column(name="sql_lowPrinority")
	public Boolean getLowPrinority() {
		return lowPrinority;
	}
	public void setLowPrinority(Boolean lowPrinority) {
		this.lowPrinority = lowPrinority;
	}
	@Column(name="sql_highPriority")
	public Boolean getHighPriority() {
		return highPriority;
	}
	public void setHighPriority(Boolean highPriority) {
		this.highPriority = highPriority;
	}
	@Column(name="sql_ignore")
	public Boolean getIgnore() {
		return ignore;
	}
	public void setIgnore(Boolean ignore) {
		this.ignore = ignore;
	}
	@Column(name="sql_delayed")
	public Boolean getDelayed() {
		return delayed;
	}
	public void setDelayed(Boolean delayed) {
		this.delayed = delayed;
	}
	@Column(name="sql_quick")
	public Boolean getQuick() {
		return quick;
	}
	public void setQuick(Boolean quick) {
		this.quick = quick;
	}
	@Column(name="sql_orderBy")
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	@Column(name="sql_rowCount")
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	@OneToMany(fetch=FetchType.EAGER,mappedBy="ztable")
	@Cascade(value = { CascadeType.ALL })
	public Collection<ZColumn> getZcolumns() {
		return zcolumns;
	}
	public void setZcolumns(Collection<ZColumn> zcolumns) {
		this.zcolumns = zcolumns;
	}
	@Column(name="sql_description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ZTable selectClone(){
		ZTable me = new ZTable();
		me.setId(this.id);
		me.setTablename(this.tablename);
		me.setDescription(this.description);
		return me;
	}

}
