package com.bizduo.zflow.domain.linkage;

import java.util.List;

/**
 * 部门
 * 
 * @author lm
 * @version 创建时间：2012-4-20 下午02:40:28
 */
public class Department {
	private Long id;
	private String name; //部门名字
	private Integer isDelete;// 判断部门是否删除
	private List<Position> positionList; //部门职位相关联

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

	public List<Position> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Position> positionList) {
		this.positionList = positionList;
	}

}
