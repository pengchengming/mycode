<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
<%-- 	<spring:message code="application_name" var="app_name" htmlEscape="false" /> --%>
<%-- 	<title><spring:message code="welcome_h3" arguments="${app_name}" /></title> --%>
<title><spring:message code="application_name"  /></title>
</head> 
<link rel="stylesheet" type="text/css" href="<c:url value="/css/login.css" />" /> 
<body>
	<div class="body">
	<div><img src="<c:url value="/images/logo.jpg"/> "></div>
		<form action="<c:url value='/pan/logon'/>" method="POST">
			<table class="login-table">
				<tr>
					<td width="30">&nbsp;</td>
					<td><input class="input01" type='text' name='j_username'/>
						<input class="input02"  type='password' name='j_password'>
					</td>
				</tr>
			    <tr>
					<td>&nbsp;</td>
					<td style="color:#ff0000;padding:2px 0;font-size:13px;">
 						${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message} 
 						${sessionScope.loginType} 
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input class="button" name="submit"type="submit" value="Login">
						<!-- <input class="button"  name="reset" type="reset" value="Reset"> -->
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>