<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>jbpm请假流程---填写申请表</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>
	<body>
		<fieldset style="width: 80%;">
			<legend>
				申请
			</legend>
			<!-- 此是普通用户，也就是变量owner的处理页面，不管当前登陆什么角色，统一提交完成任务的请求即可，
   	                                 在submit.jsp里调用完成任务的API，至于会如何完成,完全由流程定义图决定，我们编程只管调用完成即可 
            -->
			<form:form action="flows/submit" method="post" modelAttribute="processDto">
				<input type="hidden" name="taskId" value="${id}">
				<!-- 此处的owner与day两个name值要与流程图中定义的一致,owner是在xml中定义的变量，不属于具体登陆者 -->
				申请人：
				<input type="text" name="owner" value="${sessionScope.USER.username}" />
				<br />
				请假时间：
				<input type="text" name="day" value="" />
				<br />
				请假原因：
				<textarea name="reason"></textarea>
				<br />
				<input type="submit" />
			</form:form>
		</fieldset>
	</body>
</html>
