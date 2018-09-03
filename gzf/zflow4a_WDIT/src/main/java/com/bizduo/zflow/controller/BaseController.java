package com.bizduo.zflow.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.bizduo.toolbox.propertyEditor.DatePropertyEditor;

public class BaseController {
    /**JSP页面-新增**/
	protected final static String VIEW_NEW = "new"; 
	
	/**JSP页面-主页**/
	protected final static String VIEW_INDEX = "index";
	
	/**JSP页面-编辑**/
	protected final static String VIEW_EDIT = "edit";
	
	/** JSP页面-详细**/
	protected final static String VIEW_SHOW = "show";
	
	/** 重定向**/
	protected final static String REQUEST_REDIRECT = "redirect:";
	
	/** 成功页面**/
	protected final static String SUCCESS = "success";
	
	/** 失败页面**/
	protected final static String FAILED = "failed";
	
	@Autowired  
	protected  HttpServletRequest request;
	
	protected static final Log LOG = LogFactory.getLog(BaseController.class);
	
	// AJAX输出，返回null
    public String ajax(String content, String type, HttpServletResponse response) {
        try {
            response.setContentType(type + ";charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            LOG.error("ajax", e);
        }
        return null;
    }
	
	/**AJAX输出HTML，返回null**/
    public String ajaxHtml(String html, HttpServletResponse response) {
        return ajax(html, "text/html", response);
    }
    
    /**AJAX输出json，返回null**/
    public String ajaxJson(String json, HttpServletResponse response) {
        return ajax(json, "application/json", response);
    }
	
	@InitBinder  
    protected void initBinder(WebDataBinder binder) {   
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   
        dateFormat.setLenient(false);   
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
        
        String dateTime = "yyyy-MM-dd HH:mm:ss";
        binder.registerCustomEditor(Timestamp.class, new DatePropertyEditor(dateTime));  
    }
}
