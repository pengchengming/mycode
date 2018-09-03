package com.bizduo.zflow.json;

import java.util.List;

/**
 *  表单向导  封装json   查询字段，列表字段，操作
 * @author dzt
 *
 */
public class PropertyJson {
	private Long id;
	private String fieldName;//字段名称   、  middleTable
	private String comment;//字段描述     
	private String code;  //操作code
	//middleTable   属性的集合 
	private List<PropertyJson> middlePropertyJsonList;
	public PropertyJson() {
		super();
		// TODO Auto-generated constructor stub
	}
	//form 和 TableProperty属性的
	public PropertyJson(Long id, String fieldName, String comment) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.comment = comment;
	}
	//middle的
	public PropertyJson(String fieldName,Long id,
			List<PropertyJson> middlePropertyJsonList) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.middlePropertyJsonList = middlePropertyJsonList;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<PropertyJson> getMiddlePropertyJsonList() {
		return middlePropertyJsonList;
	}
	public void setMiddlePropertyJsonList(List<PropertyJson> middlePropertyJsonList) {
		this.middlePropertyJsonList = middlePropertyJsonList;
	}

}
