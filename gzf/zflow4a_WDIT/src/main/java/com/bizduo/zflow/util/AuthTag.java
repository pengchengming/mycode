package com.bizduo.zflow.util;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
 
 
 
/**
 * 权限验证的标签
 * 
 * */
public class AuthTag extends BodyTagSupport {

	private String code = null;

	
	public void setCode(String code) {
		this.code = code;
	}


	public int doStartTag() throws JspException{ 
		try {
			
			boolean hasPermission=false;
			//判断code是否在用户的
			String res_code=(String)this.pageContext.getAttribute("JSP_Resource");
			//if(res_code==null)throw new JspException("请先定义JSP所属Module");
			
			String permissionCode=res_code==null?code.trim():res_code+"_"+code.trim();
			HttpSession session = this.pageContext.getSession();
			String[] permissions = (String[])session.getAttribute("PERMISSIONS");
			if(permissions!=null){
				for(String pcode:permissions){
					if(permissionCode.equals(pcode)){
						hasPermission=true;
						break;
					}
				}
			}
			
			
			if(hasPermission){
				return EVAL_BODY_INCLUDE;
			}
			return SKIP_BODY;
			
		} catch (Exception e) {
			throw new JspException(e);
		}
	}
	
}
