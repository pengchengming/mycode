<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>P&nbsp;C&nbsp;M</title>
<%
request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path;
%>
<link rel="stylesheet" href="<%=basePath%>/css/top.css">
<style type="text/css">
.Login{
position: absolute;
margin-left:10%;
top:11%;
font-family: "楷体";
font-size: 20px;
}
#isLogin{
text-decoration: none;
}
.top {
	width: 80%;
	height: 10%;
	margin-left: 10%;
	margin-right: 10%;
}
</style>
<script charset="utf-8" src="<%=basePath%>/js/jquery-1.7.1.js"></script>
 <script type="text/javascript">
$(document).ready(function(){
		 if(<%= session.getAttribute("user")!=null%>){
			var islogin=document.getElementById("Login");
			islogin.innerHTML="<a id='isLogin'>${user.userName},欢迎您</a> ";
		 }
		 
			
	});
</script>
</head>

<body>
  <header>
    <nav>
      <ul>
        
        <li class="dropdown">
          <a href="#">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;文章浏览&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
          <ul >
           <li><a href="<%=basePath%>/article/getArticles?page=1" id="getArticles">所有文章</a></li>
      
             <li><a href="<%=basePath%>/kindEditor">写文章</a></li>
                   
          </ul>
        </li>
        <li class="dropdown">
          <a href="#/">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;文件操作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
          <ul>
            <li><a href="<%=basePath%>/upload">文件上传</a></li>
            <li><a href="<%=basePath%>/file/getResources?page=1">文件下载</a></li>
          </ul>
        </li>
        <li ><a href="<%=basePath%>/info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;个人信息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
        <li><a href="<%=basePath%>/face.jsp">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人脸&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
      <li> <div id="Login" class="Login" ><a href="/PcmBlog/login.jsp">登录</a>
        <a href="/PcmBlog/register.jsp">注册</a></div></li> 
        
      </ul>
    </nav>
  </header>

</body>
</html>