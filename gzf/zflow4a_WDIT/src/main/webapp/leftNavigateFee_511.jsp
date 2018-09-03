<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
	function caculateMiddleSize(){
	var winWidth=0;
	var winHeight=0;
	if (window.innerWidth) 
		winWidth = window.innerWidth; 
	else if ((document.body) && (document.body.clientWidth)) 
		winWidth = document.body.clientWidth; 
		//获取窗口高度 
	if (window.innerHeight) 
		winHeight = window.innerHeight; 
	else if ((document.body) && (document.body.clientHeight)) 
		winHeight = document.body.clientHeight; 
		//通过深入Document内部对body进行检测，获取窗口大小 
	if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) 
	{ 
		winHeight = document.documentElement.clientHeight; 
		winWidth = document.documentElement.clientWidth; 
	} 
		
	var  scroll_height = winHeight - 100;
	var  middle_height = winHeight - 100;
	
	var  content_width = screen.width ;
	var  scroll_width = content_width- 174;
	var  main_width = scroll_width - 20;
	var  table_container_width = main_width - 8;
	var  form_container_width = main_width - 6;
	var  main_index_width = (content_width - 20)/2;
	
	$(".main_index").css("width",main_index_width);
	$(".content").css("width",content_width);
	$(".scroll").css("height",scroll_height);
	$(".middle").css("height",middle_height);
	$(".left-menu").css("height",middle_height);
	$(".ztree").css("height",middle_height-29);
	$(".left-menu-bar").css("height",middle_height);
	$(".scroll").css("width",scroll_width);
	$(".main").css("width",main_width);
	$(".table-container").css("width",table_container_width);
	$(".form-container").css("width",form_container_width);
};
$.ajaxSetup({
	cache : false,
	global : true,
    contentType:"application/x-www-form-urlencoded;charset=utf-8",
	error: function(XMLHttpRequest, textStatus,errorThrown) {
		console.info("error");
	 	if(textStatus=="error" && XMLHttpRequest.status == 401){
		   alert("超时请重新登陆!");
		   window.location.replace('<c:url value="/login.jsp" />');  
	     throw ("unLogin");
	 	}
	}
  });
</script>
<div class="left-menu">
	<div class="left-menu-title">
		组织架构
	</div>
	<div class="left-menu-main">
		<ul id="org_tree" class="ztree" style="width:164px;overflow:scroll"></ul>
	</div>				
</div>
						