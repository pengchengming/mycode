package com.bizduo.zflow.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
 
/**
 * 将资源code放到pageContext
 * 
 * */
public class ResourceTag extends TagSupport {

	private String code = null;

	
	public void setCode(String code) {
		this.code = code;
	}


	public int doStartTag() throws JspException{
		try {
			//判断code是否在用户的
			this.pageContext.setAttribute("JSP_Resource", code.trim());
			
			return EVAL_BODY_INCLUDE;
			
		} catch (Exception e) {
			throw new JspException(e);
		}
	}
	
	
}
