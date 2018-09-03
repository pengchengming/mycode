<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

		<title>jbpm请假流程设计示例</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<style type="text/css">
table {
	width: 80%;
	border: 1px solid blue;
	border-collapse: collapse;
}

table tr td {
	border: 1px solid blue;
	border-collapse: collapse;
}
</style>
	</head>
	<body>

		<table align="center">
			<tr>
				<td>
					登录用户名：
				</td>
				<td>
					${sessionScope.USER.username}
				</td>
				<td>
					<a href="${logoutUrl}">安全退出</a>
				</td>
				<td>
					<a href="login.html">登录</a>
				</td>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td align="center" colspan="7">
					<h1>
						流程发布表单
					</h1>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					如果想发布流程请点击右边的链接
				</td>
				<td colspan="4">
					<a href='<c:url value="/flows/deploy"/>'>点击发布新流程</a>
				</td>
			</tr>
			<tr>
				<td>
					流程id
				</td>
				<td>
					发布id
				</td>
				<td>
					流程key
				</td>
				<td>
					流程name
				</td>
				<td>
					版本号
				</td>
				<td>
					移除
				</td>
				<td>
					启动流程
				</td>
			</tr>
			<c:forEach items="${pds }" var="item" varStatus="s">
				<tr>
					<td>
						${item.id }
					</td>
					<td>
						${item.deploymentId }
					</td>
					<td>
						${item.key }
					</td>
					<td>
						${item.name }
					</td>
					<td>
						${item.version }
					</td>
					<td>
						<a href='<c:url value="/flows/undeploy?id=${item.deploymentId }"/>'>删除流程</a>
					</td>
					<td>
						<a href='<c:url value="/flows/start?id=${item.id }"/>'>启动流程</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<br>
		<table align="center">
			<tr>
				<td align="center" colspan="7">
					<h1>
						所有流程实例表单表单
					</h1>
				</td>
			</tr>

			<tr>
				<td>
					实例id
				</td>
				<td>
					实例key
				</td>
				<td>
					实例name
				</td>
				<td>
					实例状态
				</td>
				<td>
					activityName
				</td>				
				<td>
					查看流程图
				</td>
			</tr>
			<c:forEach items="${pis }" var="item" >
				<tr>
					<td>
						${item.id }
					</td>
					<td>
						${item.key }
					</td>
					<td>
						${item.name }
					</td>
					<td>
						${item.state }
					</td>
					<td>
<%-- 						${findActiveActivityNames() } --%>
						<c:out value="findActiveActivityNames()"></c:out>
					</td>			
					<td>
						<a href='<c:url value="/flows/view?id=${item.id }"/>'>查看流程图</a>
					</td>
				</tr>
		</c:forEach>
		</table>
		<br>
		<table align="center">
			<tr>
				<td align="center" colspan="7">
					<h1>
						${sessionScope.USER.username}  待办任务
					</h1>
				</td>
			</tr>

			<tr>
				<td>
					任务id
				</td>
				<td>
					任务key
				</td>
				<td>
					任务name
				</td>
				<td>
					activityName
				</td>
				<td>
					指派人
				</td>
				<td>
					办理
				</td>

			</tr>
			<c:forEach items="${tasks }" var="item">
				<tr>
					<td>
						<c:out value="${item.id }" />
					</td>
					<td>
						<c:out value="${key }" />
					</td>
					<td>
						<c:out value="${item.name }" />
					</td>
					<td>
						<c:out value="${item.activityName }" />
					</td>
					<td>
					<c:out value="${item.assignee }" />
					</td>
					<td>
						<a href='<c:url value="/flows/${item.formResourceName }?id=${item.id}"/>'>办理</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<br>
				<table align="center">
			<tr>
				<td align="center" colspan="7">
					<h1>
						历史流程实例
					</h1>
				</td>
			</tr>

			<tr>
				<td>
					流程实例id
				</td>
				<td>
					历史流程实例key
				</td>
				<td>
					开始时间
				</td>
				<td>
					结束时间
				</td>
				<td>
					结束节点名称
				</td>

			</tr>
			<c:forEach items="${hpis }" var="item">
				<tr>
					<td>
						<c:out value="${item.processDefinitionId }" />
					</td>
					<td>
						<c:out value="${item.key }" />
					</td>
					<td>
						<c:out value="${item.startTime }" />
					</td>
					<td>
						<c:out value="${item.endTime }" />
					</td>
					<td>
					<c:out value="${item.endActivityName }" />
					</td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>
