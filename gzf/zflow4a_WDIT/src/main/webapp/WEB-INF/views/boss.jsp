<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
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

		<title>jbpm请假流程---老板处理</title>
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
		<fieldset>
			<legend>
				老板审核
			</legend>
			<form action='<c:url value="flows/submitBoss"/>' method="post">
				<input type="hidden" name="id" value="${id}">
				申请人：
				${map.owner }
				<br />
				请假时间：
				${map.day }
				<br />
				请假原因：
				${map.reason }
				<br />
				姓名：
				${map.name }
				<br />
				性别：
				${map.sex }
				<br />
				年龄：
				${map.address }
				<br />
				<input type="submit" />
			</form>
		</fieldset>
	</body>
</html>
