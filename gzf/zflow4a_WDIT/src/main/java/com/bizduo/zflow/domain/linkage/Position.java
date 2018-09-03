package com.bizduo.zflow.domain.linkage;

/**
 * 职位
* @author lm
* @version 创建时间：2012-4-20 下午02:42:44
*/
public class Position {
	private Long id;
	private String name; //职位名
	private Integer isDelete;// 判断职位是否删除
	private Department department; //职位和部门相关联

	// =======================GETTER AND SETTER=============================//

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
	
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
    
	
}
