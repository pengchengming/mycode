<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<script type="text/javascript">
	
	$(function(){ 
		initMenu($("#head_menu"));
		
		
		/*
		$.ajaxSetup({
			timeout: 3000, 
		　　　success: function (data) { alert(0)},  
		　　　error: function (xhr, status, e) { alert(1) }, 
		　　　complete: function (xhr, status) { alert(2)} 
		　　}); 
		  */
	});
	function initMenu(obj) {
		var menuMoved = false;
		$(obj).addClass("ui-menu");
		$("li", obj).addClass("ui-menu-item");
		$("ul", obj).addClass("ui-menu-sub");
		$("ul", obj).each(function() {
			var div = document.createElement("div");
			$(div).addClass("ui-menu-sub-div");
			$(this).wrap(div);
		});
		$("li", obj)
				.each(
						function() {
							var ul = $("ul", this);
							if (ul.length > 0 && $(">a", this).length > 0) {
								var text = $(">a", this).text();
								if (!text)
									text = "";
								$(">a", this).replaceWith(
										"<div class='ui-menu-text'>" + text
												+ "</div>");
							} else if ($(">a", this).length > 0) {
								var a = $(">a", this);
								$(this).click(function() {
									window.location.href = $(a).attr('href');
								});
								//
								// a.replaceWith("<div class='ui-menu-text'>"
								// +text+ "&nbsp;" + "</div>");
							}
						});
		$(">li", obj).addClass("ui-menu-item-first");
		$(">li", obj).each(function(index) {
			var pagename = this.value;
			$(this).addClass("ui-menu-main-item-" + pagename);

			$(this).hover(function() {
				$(this).addClass("ui-menu-main-item-hover-" + pagename);
			}, function() {
				$(this).removeClass("ui-menu-main-item-hover-" + pagename);
			});

		});

		var time = 500;
		$("li", obj).hover(function() {
			var ul = $("ul", this);
			if (ul.length > 0 && !$(this).hasClass("ui-menu-item-hover")) {
				$(this).addClass("ui-menu-item-hover");
				if (ui.animate) {
					$(ul[0]).slideDown(time, function() {
					});
				} else {
					$(ul[0]).show();
				}
			} else {
				$(this).addClass("ui-menu-item-hover");
			}
		}, function() {
			var ul = $("ul", this);
			var ele = this;
			if (ul.length > 0) {
				if (ui.animate) {
					$(ul[0]).slideUp(time, function() {
						$(ele).removeClass("ui-menu-item-hover");
					});
				} else {
					$(ul[0]).hide();
					$(ele).removeClass("ui-menu-item-hover");
				}
			} else {
				$(ele).removeClass("ui-menu-item-hover");
			}
		});

	};
	</script>
	  <div class="index_banner">
		<img style="float:left" src="<c:url value="/images/logo.jpg"/>">
	 	<div class="index_top">
  		<ul>
  			<li><a href="${logoutUrl}">Logout</a></li>
  			<li><a href="#">Welcome： ${sessionScope.USER.username}</a></li>
  			<li><a href="#"  onclick="showChangePasswordDiv()" >ChangePassword</a></li>
  		</ul>
  		</div>
  		
 	</div>
	<div class="clear"></div>
	<div class="index_menu_bg"></div>
	<div class="index_menu">
		<a href="<c:url value="/"/>"  class="ui-menu" style="float:left;padding:0 50px;margin-left:20px;line-height:40px;"><div class="ui-menu-text">首页</div></a>
		<ul id="head_menu" >
			<c:forEach var="mi" items="${sessionScope.MENUITEMS}">
				<li style="float: left;clear: none;padding:0 30px"><a href="<c:url value="${mi.url}"/>?mid=${mi.id}"  class="">${mi.name}</a>
				<ul style="">
				<c:forEach var="tmi" items="${mi.subMenuItemList}"> 
						<li><a href="<c:url value="${tmi.url}"/>"  class="" >${tmi.name}</a></li>
				</c:forEach>
				<!-- <li> <div class="ui-menu-bottm"></div> </li> -->
				</ul>
				</li>
			</c:forEach>
		</ul> 
	</div>
	
	
	<div id="window_ChangePassword" class="ui-window-container" style="display: none;">
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
					</table>
	</div> 
	
	<script>
	function showChangePasswordDiv(){

		$("#oldpass").val("");
		$("#newpass").val("");
		$("#confirmpass").val("");
		
		$("#window_ChangePassword").show(); 
		$("#window_ChangePassword").dialog({
			width:550,
			title:"ChangePassword",
			position:'center', 
				buttons : [
						{
							text : "submit",
							'class': 'btn-short',
							click : function() {
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
											$(me).dialog("close");
								   		}
							   		}
								});
							}
						},
						{
							text : "Close",
							'class' : 'btn-short',
							click : function() {
								$(this).dialog("close");
							}
						} ]
		});
		//openWin("window_uploadFileDiv",{ width : 750 } );
		//$("#window_uploadFileDiv").show(); 
	}
	function cleanChangepasswordDiv(){
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
