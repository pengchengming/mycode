package com.bizduo.zflow.domain.tableData;

import com.bizduo.zflow.domain.register.Register;
import com.bizduo.zflow.domain.tabulation.MiddleTable;

public class TableData {

	private Long id;
	private String name;
	private Integer age;
	private Long middleTableId;
	private Integer gender;
	private Integer isDelete;//判断列表数据项是否删除 
	private MiddleTable middleTable;
	private Register register;

	/*******************************************************************************************************/
	/********************************* GETTER AND SETTER ***************************************************/
	/*******************************************************************************************************/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public MiddleTable getMiddleTable() {
		return middleTable;
	}

	public void setMiddleTable(MiddleTable middleTable) {
		this.middleTable = middleTable;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Long getMiddleTableId() {
		return middleTableId;
	}

	public void setMiddleTableId(Long middleTableId) {
		this.middleTableId = middleTableId;
	}

	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
	}
	
}
