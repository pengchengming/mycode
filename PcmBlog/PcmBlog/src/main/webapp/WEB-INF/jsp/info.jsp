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
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<style type="text/css">
.top {
	width: 80%;
	height: 10%;
	margin-left: 10%;
	margin-right: 10%;
	margin-bottom: 5%;
}
</style>
<script charset="utf-8" src="<%=basePath%>/js/jquery-1.7.1.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	  $("#user_email").hide();
	  $("#user_password").hide();
	  $("#u_eamil").click(function(){
		  if( $("#u_eamil").text() =="收起 "){
			  $("#u_eamil").text("修改邮箱");
			  $("#user_email").hide(400);
		  }else{
			  $("#user_password").hide();
			  $("#u_password").text("修改密码  ");
			  $("#u_eamil").text("收起 ");
			  $("#user_email").show(200);
		  } 
	  });
	  $("#u_password").click(function(){
		  if( $("#u_password").text() =="收起 "){
			  $("#u_password").text("修改密码 ");
			  $("#user_password").hide(400);
		  }else{
			  $("#user_email").hide();
			  $("#u_eamil").text("修改邮箱");
			  $("#u_password").text("收起 ");
			  $("#user_password").show(200);
		  } 
	  });
	  $("#u_article").click(function(){
		  $.ajax({
				type : "get",
				url : "<%=basePath%>/article/userArticles",
				success : function() {
				var str="<c:forEach items='${userArticle}' var='node'>"+
			             "<c:out value='${node.title}'></c:out>&nbsp;&nbsp;"+
			             "<c:out value='${node.classify}'></c:out>&nbsp;&nbsp;"+
			             "<c:out value='${node.author}'></c:out>&nbsp;&nbsp;"+
			             "<c:out value='${node.datetime}'></c:out> &nbsp"+
			             "<a onclick='delArticle(<c:out value='${node.id}'></c:out>)' href='#'>删除</a><br/>"+
			             "</c:forEach> ";
			      $("#user_Article").html(str);
				},
				error : function(data) {
					alert(data+"发送失败");
				}
			});	
	  });
});		
	function updata_mail(){
		var email=document.getElementById("email").value;
		if(email==""){
			alert("邮箱不能为空 ");
		}else{
			$.ajax({
				type : "post",
				url : "<%=basePath%>/updataEmail",
				data : {
					"email" : email
				},
				success : function(data) {
					alert("修改邮箱 成功 ");
				},
				error : function(data) {
					alert(data+"发送失败");
				}
			});	
		}
	}
		function updata_passsword(){
			var password=document.getElementById("password").value;
			if(password==""){
				alert("密码不能为空 ");
			}else{
				$.ajax({
					type : "post",
					url : "<%=basePath%>/updataPassword",
				data : {
					"password" : password
				},
				success : function(data) {
					alert("修改密码成功 ");
				},
				error : function(data) {
					alert(data + "发送失败");
				}
			});
		}
	}
	function delArticle(id){
		alert(id);
		 $.ajax({
				type : "post",
				url : "<%=basePath%>/article/delArticle",
				data : {
					"id" : id
				},
				success : function(data) {
					alert("删除成功 ");
				var str="<c:forEach items='${userArticle}' var='node'>"+
			             "<c:out value='${node.title}'></c:out>&nbsp;&nbsp;"+
			             "<c:out value='${node.classify}'></c:out>&nbsp;&nbsp;"+
			             "<c:out value='${node.author}'></c:out>&nbsp;&nbsp;"+
			             "<c:out value='${node.datetime}'></c:out> &nbsp"+
			             "<a id='delarticle' >删除<a><br/>"+
			             "</c:forEach> ";
			             $("#user_Article").html("");
			      $("#user_Article").html(str);
				},
				error : function() {
					alert("发送失败");
				}
			});	
	}
</script>
</head>
<body onload="load()">
<div class="top"><jsp:include page="top.jsp" flush="true" /></div>
<div align="center">
${user.userName} 欢迎你来到这里！ 你想要：<br>
<a id="u_eamil" href="#">修改邮箱</a> 
<a id="u_password"href="#"> 修改密码</a>
<a id="u_article" href="#" >我的文章</a>
 <div id="user_email">
 <input type="text" id="email"  onfocus="this.value = '';"/>
 <input type="button" value="修改邮箱" onclick="updata_mail()">
 </div>
 <br>

<div id="user_password">
 <input type="password"" id="password" />
 <input type="button" value="修改密码" onclick = "updata_passsword()" />
 </div>
</div>

<div id="user_Article" style="margin-left: 39%; margin-top: 5%;">
   
</div>

</body>
</html>