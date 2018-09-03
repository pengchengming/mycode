<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 
<link rel="stylesheet" href="<c:url value="/wdit/assets/plugins/bootstrap/css/bootstrap.css" />">
<link rel="stylesheet" href="http://at.alicdn.com/t/font_1459913019_6172068.css">
<link rel="stylesheet" href="<c:url value="/wdit/assets/css/AdminETUI.css" />">
<link rel="stylesheet" href="<c:url value="/wdit/assets/css/animate.min.css" />">
<link rel="stylesheet" href="<c:url value="/wdit/assets/css/style.css" />">
<link rel="stylesheet" href="<c:url value="/wdit/assets/css/styles-responsive.css" />">
<link rel="shortcut icon" href="<c:url value="/wdit/assets/images/favicon.ico" />" />

 <script src="<c:url value="/wdit/assets/plugins/jQuery/jquery-1.11.1.min.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/jquery-ui/jquery-ui.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/bootstrap/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/jquery.scrollTo/jquery.scrollTo.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/nicescroll/jquery.nicescroll.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/jquery-validation/jquery.validate.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/bgswitcher/jquery.bcat.bgswitcher.js" />"></script> 
<script src="<c:url value="/wdit/assets/js/login.js" />"></script>
<script src="<c:url value="/wdit/assets/js/main.js" />"></script>
<style>
	#bg-body {background: none repeat scroll 0 0 #000;height: 200%;left: -50%;position: fixed;top: -50%;width: 200%;}
	#bg-body img {bottom: 0;left: 0;margin: auto;min-height: 50%;min-width: 50%;position: absolute;right: 0;top: 0;}
</style>
<script type="text/javascript">
var rootPath = "<c:url value="/" />";
jQuery(document).ready(function() {
	
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	 var isOpera = userAgent.indexOf("Opera") > -1;
	 if (userAgent.indexOf("Firefox") > -1 ||  userAgent.indexOf("Edge") > -1) {
		 
	 }else if (!(userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera)) {
		 if (!(!!window.ActiveXObject || "ActiveXObject" in window)){
			//alert("请使用IE,Firefox");
			//window.location.href=rootPath+"j_spring_security_logout";
			//return false;
		} 
  }
	 
	Main.init();
	Login.init();
	
	var  message='${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}';
	if(message){
		$("#password_error").show();
	}
	
});
</script>
<title>公租房并联审批系统</title>
</head> 
<body class="login">
	<div class="main-login col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-4">
		<div style="margin-bottom: 30%"></div>
		<div class="box-login">
			<h3>登录</h3>
			<p>请输入用户名和密码登录</p>
			<form class="form-login" action="<c:url  value='/j_spring_security_check' />" method="POST">
				<div class="errorHandler alert alert-danger no-display padding-5 no-radius" id="password_error">
					<i class="iconfont icon-exclamationsign"></i> 
					${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message} 
				</div>
				<fieldset>
					<div class="form-group">
						<span class="input-icon">
							<input type="text" class="form-control" name="j_username" placeholder="用户名">
							<i class="iconfont icon-lock"></i> </span>
					</div>
					<div class="form-group form-actions">
						<span class="input-icon">
							<input type="password" class="form-control password" name="j_password" placeholder="密码">
							<i class="iconfont icon-vpnkey"></i>
						</span>
					</div>
					<div class="form-actions">
						<button type="submit" class="btn btn-info">
							登录 <i class="iconfont icon-lock"></i>
						</button> 	
					</div>
					<div class="new-account"></div>
				</fieldset>
			</form>
			<div class="copyright">
			</div>
			<!-- end: COPYRIGHT -->
		</div>
		<!-- end: LOGIN BOX -->
	</div>

	<div id="bg-body"></div>
</body>
</html>