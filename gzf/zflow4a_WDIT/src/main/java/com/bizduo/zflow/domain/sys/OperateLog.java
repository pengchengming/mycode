package com.bizduo.zflow.domain.sys;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
* 记录 - Entity
* 
* @author 
*
*/
/**
* @author alex
*
*/
@Entity
@Table(name="global_OperateLog")
public class OperateLog implements java.io.Serializable{

	private static final long serialVersionUID = 1486123433051026566L;


	/** id*/
	private Long id;


	/** 操作代码*/
	private String opcode; 

	/** 操作结果*/
	private String opresult; 
	
	/** 操作描述*/
	private String opdesc; 

	/** 操作预留键1*/
	private String opkey1; 

	/** 操作预留键2*/
	private String opkey2; 

	/** 操作预留键3*/
	private String opkey3; 

	/** 操作预留键4*/
	private String opkey4; 

	/** 操作MAC地址*/
	private String mac; 
	
	/** 操作IP地址*/
	private String ipaddress; 	

	/** 操作开始时间*/
	private Date beginTime;	

	/** 操作结束时间*/
	private Date endTime;		
	
	/** 创建时间*/
	private Date createDate;	
	
	/** 创建人*/
	private String createByName;
	
	@Id //对应数据库中的主键  
   @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                   generator="id")    //对应生成策略名称  
   @TableGenerator(name="id", //生成策略名称  
                   pkColumnName="sequence_name",     //主键的列名  
                   pkColumnValue="OperationLog_id",            //主键的值  
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
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	@Column 
	public String getOpresult() {
		return opresult;
	}
	public void setOpresult(String opresult) {
		this.opresult = opresult;
	}
	@Column 
	public String getOpdesc() {
		return opdesc;
	}
	public void setOpdesc(String opdesc) {
		this.opdesc = opdesc;
	}
	@Column 
	public String getOpkey1() {
		return opkey1;
	}
	public void setOpkey1(String opkey1) {
		this.opkey1 = opkey1;
	}
	@Column 
	public String getOpkey2() {
		return opkey2;
	}
	public void setOpkey2(String opkey2) {
		this.opkey2 = opkey2;
	}
	@Column 
	public String getOpkey3() {
		return opkey3;
	}
	public void setOpkey3(String opkey3) {
		this.opkey3 = opkey3;
	}
	@Column 
	public String getOpkey4() {
		return opkey4;
	}
	public void setOpkey4(String opkey4) {
		this.opkey4 = opkey4;
	}
	@Column 
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	@Column 
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	@Column 
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	@Column 
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Column
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public OperateLog(String opcode, String opresult, String opdesc,
			String opkey1, String opkey2, String opkey3, String opkey4,
			String mac, String ipaddress, Date beginTime, Date endTime,
			Date createDate, String createByName) {
		super();
		this.opcode = opcode;
		this.opresult = opresult;
		this.opdesc = opdesc;
		this.opkey1 = opkey1;
		this.opkey2 = opkey2;
		this.opkey3 = opkey3;
		this.opkey4 = opkey4;
		this.mac = mac;
		this.ipaddress = ipaddress;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.createDate = createDate;
		this.createByName = createByName;
	}
	public OperateLog() {
		super();
		// TODO Auto-generated constructor stub
	}



	
}
