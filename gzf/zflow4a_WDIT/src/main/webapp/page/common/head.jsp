<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp" %>
<script>
window.onload=function(){
	var  content_width = screen.width ;
	var  scroll_height = screen.height - 265 ;
	var  middle_height = screen.height - 265;
	var  work_height = scroll_height- 27;
	var  scroll_width = content_width- 174;
	var  main_width = scroll_width - 20;
	var  table_container_width = main_width - 8;
	var  form_container_width = main_width - 6;

//	$(".content").css("width",content_width);
	$(".scroll").css("height",scroll_height);
	$(".middle").css("height",middle_height);
	$(".work").css("height",work_height);
//	$(".scroll").css("width",scroll_width);
//	$(".main").css("width",main_width);
	$(".table-container").css("width",table_container_width);
	$(".form-container").css("width",form_container_width);
}

</script>
<div class="head">
	<div class="head_top">
       <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="400" height="48">
          <param name="movie" value="<c:url value="/css/images/logo.swf"/>" />
          <param name="quality" value="high" />
      	  <param name="wmode" value="transparent"><!--必须把FLASH设置为透明--> 
          <embed src="<c:url value="/css/images/logo.swf"/>" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="400" height="48" wmode="transparent"></embed>
       </object>
		<ul id="head_menu">
			<c:forEach var="level1Node" items="${session.TOPROOTMENU}">
			<li><a href="<c:url value="${level1Node.url}"/>?menuId=${level1Node.id}">${level1Node.name}</a>
				<c:if test="${level1Node.children !=null}">
					<ul>
							<c:forEach var="level2Node" items="${level1Node.children}">
							<li><a href="<c:url value="${level2Node.url}"/>?menuId=${level2Node.id}"> ${level2Node.name}</a></li>
<%-- 								<c:if test="${level2Node.children !=null}"> --%>
<!-- 									<ul> -->
<%-- 										<c:forEach var="level3Node" items="${level2Node.children}"> --%>
<%-- 											<li><a href="<c:url value="${level3Node.url}"/>"></a>${level3Node.name}</li>								 --%>
<%-- 										</c:forEach> --%>
<!-- 											<li><div class="ui-menu-bottm"></div></li>			 -->
<!-- 									</ul> -->
<%-- 								</c:if>								 --%>
							</c:forEach>
					</ul>
				</c:if>
			</li>
			</c:forEach>
			</ul>	       	
	</div>
	<div class="nav">
		<div class="nav-right">
			<div class="user"><security:authentication property="principal.realname" /></div>		
				<a href="#" onclick="editPassword();" class="change_password"></a>
				<a href="<c:url value="j_spring_security_logout"/>" class="loginout"></a>
		</div>
	</div>
</div>

	<div id="window_editPassConfigDiv" class="ui-window-container" style="display: none;">
		<div class="ui-window-frame">
			<div class="ui-window-title">
				<a class="ui-window-close-button" href="javascript:closeChangePasswordWindow('window_editPassConfigDiv');"></a>
				<div class="ui-window-title-inner">修改用户密码</div>
			</div>
			<div class="ui-window-content">
				<div class="search-container">
					<table class="search-table">
						<tr>
							<th>原密码：</th>
							<td>
								<input type="password" id="oldpass" maxlength="16" />
							</td>
						</tr>
						<tr>
							<th>新密码：</th>
							<td>
								<input type="password" id="newpass" maxlength="16" />
							</td>
						</tr>
						<tr>
							<th>确认密码：</th>
							<td>
								<input type="password" id="confirmpass" maxlength="16" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="ui-window-foot">
				<div class="ui-window-foot-right"></div>
				<div class="ui-window-foot-inner">
					<input type="button" id="next_id" value="提&nbsp;&nbsp;交" class="btn-short" onclick="savePassword();" />
					<input type="button" value="取&nbsp;&nbsp;消" class="btn-short" onclick="javascript:closeChangePasswordWindow('window_editPassConfigDiv');" />
				</div>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		$(function(){ui.initMenu($("#head_menu"));});
		//修改密码
		function editPassword(){
			$("#oldpass").val("");
			$("#newpass").val("");
			$("#confirmpass").val("");
			openChangePasswordWindow("window_editPassConfigDiv", 300);
		}
		function savePassword(){
			var oldpassword = $("#oldpass").val();
			var newpassword = $("#newpass").val();
			var confirmpass = $("#confirmpass").val();
			if(!checkPassword(oldpassword, newpassword, confirmpass)){
				return false;
			}
			$.ajax({
				url : Securityis.url.eidtPassword,
			   type : "post",
			   data : {oldPassword : oldpassword, password : confirmpass },
		   complete : function(xhr, textStatus)
		   		{
			   		var data = $.evalJSON(xhr.responseText);
			   		if(data.errorMsg){
			   			alert(data.errorMsg);
			   		}else if(data.successMsg){
			   			alert(data.successMsg);
			   			closeChangePasswordWindow('window_editPassConfigDiv');
			   		}
		   		}
			});
		}
		//打开窗口
		 function openChangePasswordWindow(id, width)
		 {
			 if (ui.currentController)
					ui.currentController.confirm();
			openWin(id, {width: width});
		 }
		//关闭窗口
		function closeChangePasswordWindow(id){
			 if (ui.currentController)
					ui.currentController.confirm();
			 closeWin(id); 
		}
		//密码规则检验
		function checkPassword(oldpassword, newpassword, confirmpass){
// 			if(!oldpassword){
// 				alert("原密码不能为空!");
// 				return false;
// 			}
			if(!newpassword){
				alert("新密码不能为空!");
				return false;
			}
			if(!confirmpass){
				alert("确认密码不能为空!");
				return false;
			}
			if(6 > oldpassword.length){
				alert("原密码的长度不能小于6!");
				return false;
			}
			if(6 > newpassword.length){
				alert("新密码的长度不能小于6!");
				return false;
			}
			if(6 > confirmpass.length){
				alert("确认密码的长度不能小于6!");
				return false;
			}
			if(newpassword != confirmpass){
				alert("新密码不一致!");
				return false;
			}
			return true;
		}
	</script>