package com.bizduo.zflow.domain.tableData;

public class ZCloumnData {

	private String colname;//字段名称
	private String colType; //字段类型string data int 
	private String value;//字段值
	public ZCloumnData(){}
	public ZCloumnData(String colname,String colType,String value){
		this.colname=colname;
		this.colType=colType;
		this.value=value;
	}
	
	public String getColname() {
		return colname;
	}
	public void setColname(String colname) {
		this.colname = colname;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
