<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="cn.pcm.domain.ArticleContent"%>
<%@page import="java.sql.Blob"%>
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
<%
	ArticleContent content = (ArticleContent) request
			.getAttribute("content");
	Blob blob = content.getContent();
	int id = content.getId();
	String str = new String(
			blob.getBytes((long) 1, (int) blob.length()));
	String url = basePath + "/article/" + id;
%>

<script charset="utf-8" src="<%=basePath%>/js/jquery-1.7.1.js"></script>
<script charset="utf-8" src="<%=basePath%>/js/qrcode.js"></script>


<style type="text/css">
body {
	background-color: #f8f8f8;
}
.top {
	width: 80%;
	height: 10%;
	margin-left: 10%;
	margin-right: 10%;
	margin-bottom: 5%;
}
#main {
	margin-left: 10%;
	margin-right: 10%;
	margin-top: 1%;
}

#qrCodeIco {
	position: absolute;
	width: 30px;
	height: 30px;
}

#qrCode {
	width: 128px;
	height: 128px;
	
}
#title{
font-family:"宋体";
font-size: 30px;
}
#discuss{
margin-top: 5%;
margin-left: 10%;
margin-bottom: 10%;
}
#showComment{
margin-top:2%;
margin-left: 8%;
font-family:"楷体";
margin-right: 10%;

}
#each_comment{
border:2px solid #DFDFDF;
border-color: gray;
margin: 1%;
}
</style>

<script type="text/javascript">

window.onload = function () {
	var qrcode = new QRCode("qrcode", {
			text : "<%=url%>",
			width : 128,
			height : 128,
			background : "#ffffff", //背景颜色
			foreground : "red", //前景颜色
			colorDark : "#000000",
			colorLight : "#ffffff",
			correctLevel : QRCode.CorrectLevel.H
		});
		var margin = ($("#qrcode").height() - $("#qrCodeIco").height())/2;
		$("#qrCodeIco").css("margin", margin);	
}
$(document).ready(function(){
	  $("#qrcode").hide();
	  $("#share").click(function(){
		  if( $("#share").text() =="收起 "){
			  $("#share").text("分享文章");
			  $("#qrcode").hide(400);
		  }else{
			  $("#share").text("收起 ");
			  $("#qrcode").show(200);
		  }
		 
	  });
	});
function commit(){
	var comment=document.getElementById("comment").value;
	
	if(comment==""){
		alert("您的评论为空 ");
	}else{
		if (<%= session.getAttribute("user")==null%>){
			alert("请先登录  ");
		}
		else{
			var replayId=${detail.id};
			$.ajax({
				type : "post",
				url : "/PcmBlog/comment",
				data : {
					"comment" : comment,
					"replayId": replayId
				},
				success : function(data) {
					alert("添加评论成功 ");
					history.go(0);					
				},
				error : function(data) {
					alert(data+"发送失败");
				}
			});	
		}
	}
	
}
</script>
</head>
<body>
<div class="top"><jsp:include page="top.jsp" flush="true" /></div>
<div align="center" id="title">${detail.title} </div><br>
<div style="margin-left:55% ">
作者：${detail.author} &nbsp; &nbsp;
时间:${detail.datetime} &nbsp; &nbsp;
分类：${detail.classify} &nbsp; &nbsp;
<a id="share"> 分享文章</a>

</div>
<div style="margin-left:70%;margin-top: 1%;">
<div id="qrcode" >
<img id="qrCodeIco" src="<%=basePath%>/images/logo.png" />
</div>
</div>
<div id="main"><%=str%></div>
<div id="showComment">
<P style="font-size: 19px;">大家这么说：</p>
<c:forEach items="${replay}" var="node">
<div id="each_comment">
用户：<c:out value="${node.userName}"></c:out>&nbsp;&nbsp;
时间:<c:out value="${node.datetime}"></c:out>
<br/>
&nbsp;&nbsp;&nbsp;<c:out value="${node.words}"></c:out><br/>
</div>
</c:forEach>
</div>
<div id="discuss">
<p>评论：*需要登录</p>
<textarea id="comment" cols="100" rows="3"  ></textarea> &nbsp; &nbsp;
<input type="button" value="留言"  onclick="commit()"/>
</div>
</body>
</html>