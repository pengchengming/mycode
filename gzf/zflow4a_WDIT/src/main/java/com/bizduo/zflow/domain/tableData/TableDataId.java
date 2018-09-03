package com.bizduo.zflow.domain.tableData;
/**
 * middel对应的表是否有数据的标识
 */
public class TableDataId {
	private String middleTablename;//middleTable的名称
	private Long middleTableId;//middle的ID
	private Boolean isData;//是否有数据
	private String tabledata;//数据

	public Boolean getIsData() {
		return isData;
	}
	public void setIsData(Boolean isData) {
		this.isData = isData;
	}
	public Long getMiddleTableId() {
		return middleTableId;
	}
	public void setMiddleTableId(Long middleTableId) {
		this.middleTableId = middleTableId;
	}
	public String getTabledata() {
		return tabledata;
	}
	public void setTabledata(String tabledata) {
		this.tabledata = tabledata;
	}
	public String getMiddleTablename() {
		return middleTablename;
	}
	public void setMiddleTablename(String middleTablename) {
		this.middleTablename = middleTablename;
	}
}
