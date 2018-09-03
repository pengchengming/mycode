<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script type="text/javascript">
$(function(){
	$(".loginout").attr("href", '<c:url value="/j_spring_security_logout"/>');
});
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
</script>
<div class="head">
	<div class="nav">
		<ul id="head_menu" class="sf-menu" style="z-index:20">
<!-- 			<li style="z-index:20"><a href="../procinstJbpm/ccmhome">首页</a> -->
<!-- 				<ul></ul> -->
<!-- 			</li> -->
<!-- 			<li style="z-index:20"><a href="../procinstJbpm/ccmhome">文件传输</a> -->
<!-- 				<ul> -->
<!-- 				<li><a href="../pan/index"  class="child_a">文件传输</a></li> -->
<!-- 				</ul> -->
<!-- 			</li> -->
			
<!-- 			<li style="z-index:20"><a href="../procinstJbpm/ccmhome">出卷管理</a> -->
<!-- 				<ul> -->
<!-- 				<li><a href="../questionnaire/template"  class="child_a">待调查列表</a></li> -->
<!-- 				<li><a href="../questionnaire/list"  class="child_a">问卷列表</a></li> -->
<!-- 				<li><a href="../questionnaire/result"  class="child_a">问卷结果</a></li> -->
<!-- 				<li><a href="../outside/list"  class="child_a">任务列表</a></li> -->
				
<!-- 				</ul> -->
<!-- 			</li> -->
			
			<c:forEach var="mi" items="${sessionScope.MENUITEMS}">
				<li style="z-index:20"><a href="#"  class="">${mi.name}</a><!-- <a href="<c:url value="${mi.url}"/>?mid=${mi.id}"  class="">${mi.name}</a> -->
					<ul>
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
			<div class="user">
			<a href="#">${sessionScope.USER.username}/${sessionScope.currentemployee.realname} 
			<c:if test="${sessionScope.currentemployee.userType==1}" >内部用户 </c:if>
			<c:if test="${sessionScope.currentemployee.userType==0}" >外部用户 </c:if>
			</a>
			
			
			</div>	
			<a class="bookmark_head" href="#" onclick="javascript:addBookmark('家化云盘','https://pan.jahwa.com.cn/')"> 
  添加到收藏</a>	
  			<c:if test="${sessionScope.currentemployee.userType==0}" ><a href="#" onclick="showChangePasswordDiv();" class="change_password">修改密码</a> </c:if>
				

				<a class="loginout" href="../j_spring_security_logout"></a>
		</div>
	</div>
</div>			
	<div  id="window_ChangePassword" class="changepasswordbox" style="display: none;">
		<h2>
			<a href="#" class="fr" onclick="closeChangePasswordDiv();">关闭</a>密码修改
		</h2>
<!-- 		<div class="pd15"> -->
<!-- 	<div id="window_ChangePassword" class="ui-window-container" style="display: none;"> -->
	 	<table class="search-table">
			<tr>
				<th>Old Password/原密码：</th>
				<td>
					<input type="password" id="oldpass" maxlength="12" />
				</td>
			</tr>
			<tr>
				<th>New Password/新密码：</th>
				<td>
					<input type="password" id="newpass" maxlength="12" />
				</td>
			</tr>
			<tr>
				<th>Confirm password/确认密码：</th>
				<td>
					<input type="password" id="confirmpass" maxlength="12" />
				</td>
			</tr>
			
			<tr><td colspan="2">
			<input type=button class="button1" value="提交" onclick="javascript:changepassword();">
			</td></tr>
			
		</table>
	</div> 
<script type="text/javascript">
function changepassword()
{
	var oldpassword = $("#oldpass").val();
	var newpassword = $("#newpass").val();
	var confirmpass = $("#confirmpass").val();
	if(!checkPassword(oldpassword, newpassword, confirmpass)){
		return false;
	}
	var me=this;
	$.ajax({
	 url : rootPath+"clientCallAdminController/eidtPassword.do",
	   type : "post",
	   data : {oldpassword : oldpassword, password : confirmpass },
   		complete : function(xhr, textStatus)
   		{
	   		var data = $.evalJSON(xhr.responseText);
	   		if(data.errorMsg){
	   			alert(data.errorMsg);
	   		}else if(data.successMsg){
	   			alert(data.successMsg);
// 				$(me).dialog("close");
	   		}
   		}
	});
	
	}

function addBookmark(title,url) {
if (window.sidebar) { 
window.sidebar.addPanel(title, url,""); 
} else if( document.all ) {
window.external.AddFavorite( url, title);
} else if( window.opera && window.print ) {
return true;
}
}
 
	
	
	function showChangePasswordDiv(){

		$(".changepasswordbox").css(
				{
					left : ($("body").width() - $(".changepasswordbox").width()) / 2
							- 20 + "px",
					top : ($(window).height() - $(".changepasswordbox").height()) / 2
							+ $(window).scrollTop() + "px",
					display : "block"
				});
		$("#oldpass").val("");
		$("#newpass").val("");
		$("#confirmpass").val("");
		
		$("#window_ChangePassword").show(); 
	 
	}
	function closeChangePasswordDiv(){
		$("#window_ChangePassword").hide();
	}
	
	//密码规则检验
	function checkPassword(oldpassword, newpassword, confirmpass){
//			if(!oldpassword){
//				alert("原密码不能为空!");
//				return false;
//			}
		if(!newpassword){
			alert("New password is required!");//新密码不能为空
			return false;
		}
		if(!confirmpass){
			alert("Confirmed password is required!"); //确认密码不能为空
			return false;
		}
		if(6 > oldpassword.length){
			alert("The length of old password must be great than 6!");//原密码的长度不能小于6
			return false;
		}
		if(6 > newpassword.length){
			alert("The length of new password must be great than 6!");//新密码的长度不能小于6
			return false;
		}
		if(6 > confirmpass.length){
			alert("The length of confirm password must be great than 6!");//确认密码的长度不能小于6
			return false;
		}
		if(newpassword != confirmpass){
			alert("The confirm password isn't same as new password!");//新密码不一致
			return false;
		}
		return true;
	}
	</script>
