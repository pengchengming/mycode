<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/commonCss.jsp"%>
<%@ include file="/page/common/commonJs.jsp"%>
<%@ include file="/page/common/formJs.jsp" %>
<html> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=8" />	
<%-- 		<spring:message code="application_name" var="app_name" htmlEscape="false" /> --%>
<%-- 		<title><spring:message code="welcome_h3" arguments="${app_name}" /></title> --%>
			<c:url value="/j_spring_security_logout" var="logoutUrl"/>
	</head>
<%-- 	<script type="text/javascript" src="<c:url value="/javascript/index.js" />"></script> --%>
	<script type="text/javascript">
    </script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/index.css" />" /> 
	
  	<body id="index_body">
	<%@ include file="/head.jsp"%>
	<%@ include file="/leftMenu.jsp"%>
	
	<div class="index_content">
		<div class="index_content_01">
			<span>我的计划</span>
			<span>
				<a href='<c:url value="/flows/init"/>'>index1</a>
				<br/><br/>
				<a href='<c:url value="/procinstJbpm/deployStart"/>'>发布启动请假新流程</a> 
				<!-- 
				<br/>
				<a href='<c:url value="/procinstJbpm/loanDeployStart"/>'>发布启动外借新流程</a>
				 -->
			</span>
			<span>	
				<table align="center">
					<tr>
						<td align="center" colspan="7">
							<h1>
								流程发布表单
							</h1>
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
						</tr>
					</c:forEach>
				</table>
			
			</span>
		</div>
		<div class="index_content_01">
			<span>我的流程</span>
			<span>
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
								<a href='<c:url value="/procinstJbpm/${item.formResourceName }?id=${item.id}"/>'>办理</a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</span>
		</div>
		<div class="index_content_01">
			<span>
				<div>
					<table >
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
									<fmt:formatDate value="${item.startTime }" pattern="yyyy-MM-dd"/>
								</td>
								<td>
									<fmt:formatDate value="${item.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
									
								</td>
								<td>
								<c:out value="${item.endActivityName }" />
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</span>
		</div>
		<div class="index_content_01">
			<span>所有流程实例表单表单</span>
			
					<table align="center">
		

			<tr>
				<td>
					id
				</td>
				<td>
					key
				</td>
				<td>
					name
				</td>
				<td>
					状态
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
<%-- 						${findActiveActivityNames() } 
						<c:out value="findActiveActivityNames()"></c:out>--%>
					</td>			
					<td><%-- 
						<a href='<c:url value="/flows/view?id=${item.id }" target=”_blank"/>'>查看流程图</a>--%>
					</td>
				</tr>
		</c:forEach>
		</table>
		
		</div>
		<div class="index_content_01">
			<span>我的报销</span>
		</div>
		<div class="index_content_01">
			<span>个人信息</span>
		</div>
<!-- 		<div class="index_content_01"></div> -->
<!-- 		<div class="index_content_01"></div> -->
		<div class="clear"></div>
	</div>
	<div class="clear"></div>

	</body>
</html>