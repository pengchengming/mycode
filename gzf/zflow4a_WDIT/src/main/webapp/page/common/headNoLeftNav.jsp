<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp" %>
<script>
$(function(){
	$(".loginout").attr("href", '<c:url value="/j_spring_security_logout"/>');
});
function caculateMiddleSize(){
	var  content_width = screen.width ;
	var  scroll_height = screen.height - 300;
	var  middle_height = screen.height - 300;
	var  scroll_width = content_width ;//- 174;
	var  main_width = scroll_width - 20;
	var  table_container_width = main_width - 8;
	var  form_container_width = main_width - 6;
	var  main_index_width = (content_width - 20)/2;
	
	$(".main_index").css("width",main_index_width);
	$(".content").css("width",content_width);
	$(".scroll").css("height",scroll_height);
	$(".middle").css("height",middle_height);
	$(".scroll").css("width",scroll_width);
	$(".main").css("width",main_width);
	$(".table-container").css("width",table_container_width);
	$(".form-container").css("width",form_container_width);
};
/*
$.ajaxSetup({
	cache : false,
	global : true,
    contentType:"application/x-www-form-urlencoded;charset=utf-8",
	error: function(XMLHttpRequest, textStatus,errorThrown) {
		console.info("error");
	 	if(textStatus=="error" && XMLHttpRequest.status == 401){
		   alert("超时请重新登陆！");
		   window.location.replace('<c:url value="/login.jsp" />');  
	     throw ("unLogin");
	 	}
	}
  });
*/
</script>
<div class="head">
	<div class="nav">
		<ul id="head_menu" class="sf-menu" style="z-index:20">
			<li style="z-index:20"><a href="#">首页</a>
				<ul></ul>
			</li>
			<c:forEach var="mi" items="${sessionScope.MENUITEMS}">
				<li style="z-index:20"><a href="#"  class="">${mi.name}</a><!-- <a href="<c:url value="${mi.url}"/>?mid=${mi.id}"  class="">${mi.name}</a> -->
					<ul class="sf-menu1">
						<c:forEach var="tmi" items="${mi.subMenuItemList}">
 							<li><a href="<c:url value="${tmi.url}"/>"  class="child_a">${tmi.name}</a></li>
 						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>
<!-- 		<div class="nav-right"> -->
<%-- 			<div class="user"><a href="#">Welcome： ${sessionScope.USER.username}</a></div>		 --%>
<!-- 				<a href="#"  onclick="showChangePasswordDiv()" >ChangePassword</a> -->
<%-- 				<a href="${logoutUrl}">Logout</a> --%>
<!-- 		</div> -->
		
		<div class="nav-right">
			<div class="user"><a href="#">${sessionScope.USER.username}</a></div>		
<!-- 				<a href="#" onclick="showChangePasswordDiv();" class="change_password">修改密码</a> -->
				<a class="loginout"></a>
		</div>
	</div>
</div>