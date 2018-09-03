package com.bizduo.zflow.util;

import java.util.Map;

/**
 * 树
 * @author Administrator
 *
 */
public class TreeNode {
	
	private Integer id;
	
	private String name;
	
	private Boolean isParent;
	
	private String attach;
	
	private String path;
	
	private int nlevel;
	
	private Long parentId;
	
	private String code;
	
	private String acronym;
	
	private Map<String,Object> map;

	public TreeNode(Integer id, String name,Boolean isParent,int nlevel) {
		super();
		this.id = id;
		this.name = name;
		this.isParent = isParent;
		this.nlevel = nlevel;
	}
	
	public TreeNode(Integer id, String name,Boolean isParent,int nlevel, String code) {
		super();
		this.id = id;
		this.name = name;
		this.isParent = isParent;
		this.nlevel = nlevel;
		this.code = code;
	}
	
	public TreeNode(Integer id, String name, Boolean isParent, String acronym, int nlevel) {
		super();
		this.id = id;
		this.name = name;
		this.isParent = isParent;
		this.acronym = acronym;
		this.nlevel = nlevel;
	}
	
	public TreeNode(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getNlevel() {
		return nlevel;
	}

	public void setNlevel(int nlevel) {
		this.nlevel = nlevel;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	
	
}
