package com.bizduo.zflow.domain.image;

/**
 * @author lm
 * @version 创建时间：2012-3-28 下午02:10:51 表单--实体类
 */
public class Image {
	private Long id; // 图片id
	private String name; //图片名字
	private String url; //图片url
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
