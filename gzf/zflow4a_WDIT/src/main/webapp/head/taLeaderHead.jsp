<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<a href="<c:url value="/procinstJbpm/ccmhome"/>"  class="ui-menu" style="float:left;padding:0 50px;margin-left:20px;line-height:40px;"><div class="ui-menu-text">To Do List</div></a>
		<a href="<c:url value="/clientCallAdminController/toExlList"/>"  class="ui-menu" style="float:left;padding:0 50px;margin-left:20px;line-height:40px;"><div class="ui-menu-text">Data Inquery</div></a>
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