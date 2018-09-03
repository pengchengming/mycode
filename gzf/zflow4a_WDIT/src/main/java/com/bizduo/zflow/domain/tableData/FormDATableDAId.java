package com.bizduo.zflow.domain.tableData;

import java.util.List;


//form的数据
public class FormDATableDAId {
	private Long formId;//form的iD
	private String register;//form对应的表的数据
	private List<TableDataId> tableDateList;//middleTable对应的表的集合
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
	public String getRegister() {
		return register;
	}
	public void setRegister(String register) {
		this.register = register;
	}
	public List<TableDataId> getTableDateList() {
		return tableDateList;
	}
	public void setTableDateList(List<TableDataId> tableDateList) {
		this.tableDateList = tableDateList;
	} 
}
