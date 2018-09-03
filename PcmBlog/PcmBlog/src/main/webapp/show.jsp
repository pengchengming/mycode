<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>
<%
	request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<link rel="stylesheet" href="<%=basePath%>/css/pubu.css">
<script charset="utf-8" src="<%=basePath%>/js/jquery-1.7.1.js"></script>

<style type="text/css">
.top {
	width: 80%;
	height: 10%;
	margin-left: 10%;
	margin-right: 10%;
}

.bottom {
	margin-top: 4%;
	margin-bottom: 4%;
}
</style>

</head>

<div class="top"><jsp:include page="top.jsp" flush="true" /></div>
<div style="padding:20px" >
<!--效果开始-->
<div class="qx_list" id="container">
<ul>
<c:forEach items="${showArticle}" var="node">

<li class="box">
<c:if test="${node.classify == '科技类'}">
			<div class="qx_picBox"><img src="<%=basePath%>/images/keji.png"
				width="210" height="71" /></div>
		</c:if>
  <c:if test="${node.classify == '文学类'}">
			<div class="qx_picBox"><img src="<%=basePath%>/images/wenxue.png"
				width="210" height="71" /></div>
		</c:if>
  <div class="qx_txt">
   <p>
   <c:out value="${node.id}"></c:out>
   <a href='/PcmBlog/article/${node.id}'><h2><c:out value="${node.title}"></c:out></h2></a><br>
      作者：<c:out value="${node.author}"></c:out><br/>
       时间：<c:out value="${node.datetime}"></c:out>
   </p>
  </div>
 </li>
 </c:forEach> 
</ul>
</div>

</div>


 
   <div class="bottom"><jsp:include page="bottom.jsp" flush="true" /></div>
  
</body>
</html>