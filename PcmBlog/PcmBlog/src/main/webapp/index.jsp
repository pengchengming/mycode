<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PcmBlog</title>
<%
request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path;
%>
<script charset="utf-8" src="<%=basePath%>/js/jquery-1.7.1.js"></script>
<style type="text/css">
.top {
	width: 80%;
	height: 10%;
	margin-left: 10%;
	margin-right: 10%;
	
}
</style>


</head>
<body>
<div class="top"><jsp:include page="top.jsp" flush="true" /></div>
<div class="top" style="margin-top: 13%;"><jsp:include page="index1.jsp" flush="true" /></div>
</body>
</html>