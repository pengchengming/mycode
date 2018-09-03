package com.bizduo.zflow.wrapper;

import java.io.Serializable;

public class OrgAndUserWrapper implements Serializable {

	private static final long serialVersionUID = 6798680485921503328L;
	private String id;
	private Integer pId;
	private String name;
	private String code;
	
	private Long formPropertyId;
	
	private Boolean isParent = false;
	
	public OrgAndUserWrapper(){}
	
	
	public OrgAndUserWrapper(String id, Integer pId,String code, String name,Long formPropertyId, Boolean isParent){
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.isParent = isParent;
		this.code=code;
		this.formPropertyId=formPropertyId;
	}
	
 
	public OrgAndUserWrapper(String id, Integer pId, String name, Boolean isParent){
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.isParent = isParent;
	}
	public OrgAndUserWrapper(String id, Integer pId,String code, String name, Boolean isParent){
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.isParent = isParent;
		this.code=code;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public Long getFormPropertyId() {
		return formPropertyId;
	}


	public void setFormPropertyId(Long formPropertyId) {
		this.formPropertyId = formPropertyId;
	}

 

}
