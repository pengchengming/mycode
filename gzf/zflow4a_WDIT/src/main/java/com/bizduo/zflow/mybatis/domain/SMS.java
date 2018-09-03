package com.bizduo.zflow.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SMS implements Serializable {
	
	public long id;
	
	public String mobile; 
	
	public String content;
	
	public int status;
	
	public String msg;
	
	public Date submittime;
	
	public int verifytype;
	
	public String verifycode;
	
	public int verifystatus;
	
	public int device_id;
	
	//ID,mobile,content,status,verifytype,verifycode,verifystatus,device_id

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getSubmittime() {
		return submittime;
	}

	public void setSubmittime(Date submittime) {
		this.submittime = submittime;
	}

	public int getVerifytype() {
		return verifytype;
	}

	public void setVerifytype(int verifytype) {
		this.verifytype = verifytype;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public int getVerifystatus() {
		return verifystatus;
	}

	public void setVerifystatus(int verifystatus) {
		this.verifystatus = verifystatus;
	}

	public int getDevice_id() {
		return device_id;
	}

	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	
	

}
