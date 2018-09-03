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
<style type="text/css">
#inputcode {
	margin-left: -93px;
	width: 45%;
}

#checkCode {
	font-family: Arial;
	font-style: italic;
	margin-right: 30px;
	margin-left: -100px;
	margin-bottom: 20px;
	color: blue;
	font-size: 30px;
	border: 0;
	padding: 2px 3px;
	letter-spacing: 3px;
	font-weight: bolder;
	float: right;
	cursor: pointer;
	width: 100px;
	height: 60px;
	line-height: 60px;
	text-align: center;
	vertical-align: middle;
}
</style>
<script type="text/javascript">
var code;

	function createCode() {
		
		code = "";
		var codeLength = 4; //验证码的长度
		var checkCode = document.getElementById("checkCode");
		var codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'a', 'b', 'c',
				'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
				'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
				'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'); //所有候选组成验证码的字符，当然也可以用中文的
		for ( var i = 0; i < codeLength; i++) {
			var charNum = Math.floor(Math.random() * 52);
			code += codeChars[charNum];
		}
		if (checkCode) {
			checkCode.className = "code";
			checkCode.innerHTML = code;
		}
		
	}

	
	function getUser() {
		var inputCode = document.getElementById("inputcode").value;
		if (inputCode.length <= 0) {
			alert("请输入验证码！");
		} else if (inputCode.toUpperCase() != code.toUpperCase()) {
			alert("验证码输入有误！");
			createCode();
		} else {
			var userName = document.getElementById("userName").value;
			var password = document.getElementById("password").value;
			var form = document.forms[0];
			form.action = "/PcmBlog/getUser";
			form.method = "post";
			form.submit();
		}
	}
</script>
</head>
<body onload="createCode()">
<h1>pcmBlog Login Form</h1>
<div class="log" >
	<div class="content2" >
		<h2>登录页面</h2>
		
		<h3 style="color: red;">${msg}</h3>
		<form>
			<input id="userName" type="text" name="userName" value="USERNAME" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'USERNAME';}">
			<input  id="password" type="password" name="password" value="PASSWORD" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'PASSWORD';}">
			<input  id="inputcode" type="text" name="code" value="请输入验证码" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '请输入验证码';}">
			<div class="code" id="checkCode" onclick="createCode()" ></div>
			<div class="button-row">
				<input type="button" class="sign-in" value="登录" onclick="getUser()">
				<input type="button" class="reset" value="注册" onclick ="location.href='<%=basePath%>/register.jsp'">
				<div class="clear"></div>
			</div>
		</form>
	</div>
<!--	<div class="content2">-->
<!--		<h2>Sign Up Form</h2>-->
<!--		<form>-->
<!--			<input type="text" name="userid" value="USERNAME" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'NAME AND SURNAME';}">-->
<!--			<input type="email" name="email" value="EMAIL ADDRESS" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'EMAIL ADDRESS';}">-->
<!--			<input type="password" name="psw" value="PASSWORD" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'PASSWORD';}">-->
<!--			<input type="submit" class="register" value="Register">-->
<!--		</form>-->
<!--	</div>-->
	<div class="clear"></div>
</div>
<div class="footer">
	<p>Copyright &copy; 2017.PCM</p>
</div>

</body>


</html>