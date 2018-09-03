<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.bizduo.zflow.domain.sys.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/commonCss.jsp"%>
<%@ include file="/page/common/commonJs.jsp"%>
<%@ include file="/page/common/formJs.jsp" %>
<%@ include file="/page/common/zTree.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:url value="/j_spring_security_logout" var="logoutUrl"/>
<style>
#selectable .ui-selecting {
	background: #FECA40;
} 
</style>
<title>Zflow——工作流系统</title>
</head>
<body>
	<div class="content">
		<div class="head">
			<div class="logo_here">
				<font style="color: #999; font-size: 20px; font-family: 'Arial'">
					&nbsp;&nbsp;&nbsp;&nbsp;ZFLOW业务流程管理软件
				</font>
				<ul>
					<li style="float:right;">&nbsp;&nbsp;&nbsp;</li>
	  				<li style="float:right;"><a href="${logoutUrl}" style="color:white;">安全退出</a></li>
	  				<li style="float:right;">&nbsp;&nbsp;&nbsp;</li>
	  				<li style="float:right;"><a href="#" style="color:white;">欢迎您： ${sessionScope.USER.username}</a></li>
	  			</ul>
			</div>
			<div class="nav" style="color: #fff; font-size:14px; font-family: 'Arial'">
	 
	
			</div>
		</div>
		<div class="main">
			<!--左菜单-->
			<div class="left_menu">
				<%@ include file="/page/jsp/form/leftForm.jsp"%>
			</div>
			<!--拖拽杆-->
			<div class="draggable"></div>
			<!--工作区-->
			<div class="workspace">
				<div class="workspace_title">
					<font style="color: #fff; font-size: 14px; font-family: 'Arial'">workspace </font>
				</div>
				<div class="toolbar">
					<div id="tool">
					
					</div>
						<div id="template">
						</div>
				</div>
			<div class="workspace_inner">
				<div style="height: 780px;">
					<div id="workspace"></div>
				</div>
			</div>
		</div>
			<%@ include file="/page/jsp/form/toolForm.jsp"%>
		<!--拖拽杆-->
		<div class="draggable"></div>
		<!--工具栏-->
		<div class="components">
			<div class="thin_title">
				<font style="color: #fff; font-size: 14px; font-family: 'Arial'">
					Components    ${sessionScope.USER.username}</font>
					<font style="color: #fff; font-size: 14px; font-family: 'Arial'">
					
					</font>
			</div>
			<%@ include file="/page/jsp/form/rightForm.jsp"%>
		</div>
	<div class="foot">
		<font style="color: #fff; font-size: 12px; font-family: 'Arial'">
			power by bizduo </font>
	</div>
	</div>
	<%@ include file="/page/jsp/form/template.jsp"%>
</body>
</html>