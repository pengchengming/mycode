<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%
request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path;
%>
<link rel="stylesheet" href="<%=basePath%>/css/pubu.css">
<style type="text/css">
.top {
	width: 80%;
	height: 10%;
	margin-left: 10%;
	margin-right: 10%;
	
}
.qx_picBox{
   border-style: none;
   font: 20px "微软雅黑";
   height: 30px;
   margin-left: 8px;
   margin-top: 5px;
}
.qx_list{
  margin-top: 20px;
  margin-bottom: 40px;

}
</style>
</head>

<body>
<div class="top"><jsp:include page="top.jsp" flush="true" /></div>


  <div class="qx_list" id="container">
<ul>
<c:forEach items="${showResource}" var="node">
<li class="box">
  <div class="qx_picBox"> <a href="<%=basePath%>/file/download?id=${node.id}">
      <c:out value="${node.realName}"></c:out></a> </div>
  <div class="qx_txt">
   <p>
   上传者：<c:out value="${node.author}"></c:out><br/>
   上传时间：<c:out value="${node.uploadTime}"></c:out><br>
  描述：<c:out value="${node.description}"></c:out>
   </p>
  </div>
 </li>
 </c:forEach> 
</ul>
</div>
   <div class="bottom"><jsp:include page="bottom2.jsp" flush="true" /></div>
  
</body>
</html>