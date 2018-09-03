package com.bizduo.zflow.domain.tableData;

public class DataTableToPage {
	private int code;
	private String results;
	private String paged;
	private String successMsg;
	private String errorMsg;
	
	
	
	public String getSuccessMsg() {
		return successMsg;
	}
	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getPaged() {
		return paged;
	}
	public void setPaged(String paged) {
		this.paged = paged;
	}
} 