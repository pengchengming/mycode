<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@page  language="java" import="com.bizduo.zflow.util.Configure" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
<%-- 	<spring:message code="application_name" var="app_name" htmlEscape="false" /> --%>
<%-- 	<title><spring:message code="welcome_h3" arguments="${app_name}" /></title> --%>
<title>IDP网络调研系统</title>
</head> 
<link  rel="stylesheet" type="text/css" href="<c:url value="/_skins/"/><%=Configure.getConfigure("skin") %>/dcss/login.css.jsp" />
<body>
	<div class="body">
		<div class="top"></div>
		<form action="<c:url value='/j_spring_security_check'/>" method="POST">
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
				</Tr>
				<tr>
					<td></td>
					<td>
						<input class="button" name="submit"type="submit" value="登录">
						<input class="button"  name="reset" type="reset" value="重置">
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td><td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td><td>&nbsp;</td>
				</tr>
				<tr>
						<td>&nbsp;</td>
						<td>
							<span style="font-weight:bold;color:#ff0000;font-size:14px;">请使用IE9.0及以上版本访问本系统</span>
						</td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
function addBookmark(title,url) {
if (window.sidebar) { 
window.sidebar.addPanel(title, url,""); 
} else if( document.all ) {
window.external.AddFavorite( url, title);
} else if( window.opera && window.print ) {
return true;
}
}
</script> 
<div class="bookmark-login">
<a href="#" onclick="javascript:addBookmark('家化云盘','https://pan.jahwa.com.cn/login')"> 
  添加到收藏</a>
  </div>
  
</body>
</html>