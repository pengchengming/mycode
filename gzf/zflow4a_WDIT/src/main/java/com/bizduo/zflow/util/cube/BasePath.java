package com.bizduo.zflow.util.cube;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
public class BasePath {
	protected static String contextPath = null;
	protected static String basePath = null; 
	protected static String realPath = null;

	public static String getBasePath(HttpServletRequest request) {
		contextPath = request.getContextPath();
		basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+contextPath+"/";
		return basePath;
	}

	public static String getRealPath(HttpServletRequest request, String path) {
		ServletContext context = request.getSession().getServletContext();
		realPath = context.getRealPath(path);
		realPath = context.getRealPath(path)+"\\";
		return realPath;
	}

	public static String getMyRealPath(HttpServletRequest request, String path) {
		ServletContext context = request.getSession().getServletContext();
		realPath = context.getRealPath(path);
		realPath = context.getRealPath(path);
		return realPath;
	}

/**
* 获取完整的URL地址，包括参数
* */
	public static String getFullPath(HttpServletRequest request) {
		//getQueryString()得到的是url后面的参数串，和前者相加就是带参数的请求路径了 
		String queryString = request.getQueryString(); 
		//获取URL地址
		StringBuffer url = request.getRequestURL();
		String fullPath=null;
		//URL地址+参数串
		if(queryString!=null){
		fullPath = url +"?"+ queryString;
		}else {
		fullPath= url.toString();
		}
		return fullPath;
	}
}