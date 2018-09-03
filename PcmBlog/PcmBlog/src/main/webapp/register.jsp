<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="<%=basePath%>/css/style.css" type="text/css">
<script charset="utf-8" src="<%=basePath%>/js/jquery-1.7.1.js"></script>
<script type="text/javascript">

var register="";
	function check() {
		if (this.value == "") {
			this.value = "USERNAME";
		}else {
			var userName = document.getElementById("userName").value;
			 var show1 =  document.getElementById("show1");
			 var show2 =  document.getElementById("show2");
			$.ajax({
				type : "post",
				url : "<%=basePath%>/check",
				data : {
					"userName" : userName
				},
				success : function(data) {
					register=data;
					if(data=="yes"){
						 show1.innerHTML=" ";
						 show2.innerHTML="可以使用这个用户名 ";
					 }else{
						 alert(register);
						 show1.innerHTML="该用户名已注册  ";
						 show2.innerHTML=" ";
					 }
				},error : function() {
					alert("发送失败");
				}
			});	
		}
	}
	function addUser(){
		var userName=document.getElementById("userName").value;
		var email=document.getElementById("email").value;
		var password=document.getElementById("password").value;
		if(register=="no"){
			 alert("换个用户名把 ");
		 }
		else{
			if(userName!="" ||email!="" || password !=""){
				
				var form = document.forms[0];
				form.action = "<%=basePath%>/addUser";
				form.method="post";
				form.submit();
			}
			else{
				alert("填写完整信息把 ");
			}
		}
		
	}
</script>
</head>
<body>
<h1>注册页面</h1>
<div class="log" >
<!--	<div class="content2" >-->
<!--		<h2>Sign In Form</h2>-->
<!--		<form>-->
<!--			<input type="text" name="userid" value="USERNAME" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'USERNAME';}">-->
<!--			<input type="password" name="psw" value="PASSWORD" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'PASSWORD';}">-->
<!--			-->
<!--			<div class="button-row">-->
<!--				<input type="submit" class="sign-in" value="Sign In">-->
<!--				<input type="reset" class="reset" value="Reset">-->
<!--				<div class="clear"></div>-->
<!--			</div>-->
<!--		</form>-->
<!--	</div>-->
	<div class="content2">
		<h2>注册</h2>
		<h3 id="show1" style="color: red;"></h3>
		<h3 id="show2" style="color: blue;"></h3>
		<form action="" > 
			<input type="text" id="userName" name="userName" value="USERNAME" onfocus="this.value = '';" onblur="check()">
			<input type="email" id="email" name="email" value="EMAIL ADDRESS" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'EMAIL ADDRESS';}">
			<input type="password" id="password" name="password" value="PASSWORD" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'PASSWORD';}">
			<input type="button" class="register" value="Register" onclick="addUser()">
		</form>
	</div>
	<div class="clear"></div>
</div>
<div class="footer">
	<p>Copyright &copy; 2017.PCM</p>
</div>

</body>

</html>