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
	<script type="text/javascript">
	window.location.replace('<c:url value="/procinstJbpm/ccmhome" />');  
    </script>
     
 	<link rel="stylesheet" type="text/css" href="<c:url value="/css/index.css" />" /> 
  	<body id="index_body">
		<%@ include file="/head.jsp"%>
	
	<div class="index_content" style="display: none;">		
<div class="index_create_table">
			<span class="index_table_title">我的流程</span>
				<table id="create_Table_createDiv" class="jstable" border="0" cellspacing="0" rules="all" width="100%">
					<tr>
						<th colspan="7">
								${sessionScope.USER.username}  待办任务
						</th>
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
					<tr>
							<td>
								 12122122312
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
						</tr>
					<tr>
							<td>
								 12122122312
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
						</tr>				
					<tr>
							<td>
								 12122122312
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
							<td>
								临时数据
							</td>
						</tr>
</table>
		</div>
		<div class="clear"></div>
	</div>
	<div class="clear"></div>

	</body>
</html>