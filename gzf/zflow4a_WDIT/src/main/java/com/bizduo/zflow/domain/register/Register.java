package com.bizduo.zflow.domain.register;

import java.util.Set;

import com.bizduo.zflow.domain.base.BaseEntity;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.tableData.TableData;

public class Register extends BaseEntity{
	
	private static final long serialVersionUID = 6032065667471087089L;
	
	private String username;
	private String password;
	private Integer gender;
	private Integer isDelete;
	private Form form;
	private Set<TableData> tableDataList;//用户表和录入信息表关联

	/*******************************************************************************************************/
	/********************************* GETTER AND SETTER ***************************************************/
	/*******************************************************************************************************/

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Set<TableData> getTableDataList() {
		return tableDataList;
	}

	public void setTableDataList(Set<TableData> tableDataList) {
		this.tableDataList = tableDataList;
	}


}
