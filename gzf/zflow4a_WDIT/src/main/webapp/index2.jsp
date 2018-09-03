<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/page/common/taglibs.jsp"%> 
<%@ include file="/page/common/commonJs.jsp"%>
<%@ include file="/page/common/formJs.jsp" %>
<html> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=8" />	
<%-- 		<spring:message code="application_name" var="app_name" htmlEscape="false" /> --%>
<%-- 		<title><spring:message code="welcome_h3" arguments="${app_name}" /></title> --%>
			<c:url value="/j_spring_security_logout" var="logoutUrl"/>
	</head> 
	
<style type="text/css">
.slidehide {
	background-color:red;width:100px;height:100px;
	display:none;
}
.slideShow {
	background-color:#00FF00;width:100px;height:100px;
	display:block;
}
</style>
      
  	<body id="index_body"> 
  	

  	<script>
  	var time=500;
  	$(function(){
//   		$("#slide_id").slideUp(time, function() {
// 			$("#slide_id").removeClass("slideShow");
// 			$("#slide_id").addClass("slidehide");
// 		});
//   		$("#slide_id").slideDown(time, function() {
// 			$("#slide_id").removeClass("slidehide");
// 			$("#slide_id").addClass("slideShow");
// 		});
  		
  		//$("#slide_id").slideUp(time,callback)
  		$(".test1").hover(function() { 
  			var liid= $(this).attr("liid"); 
			$("#slide_id"+liid).removeClass("slidehide");
			$("#slide_id"+liid).addClass("slideShow");
		}, function() {
			var liid= $(this).attr("liid");
			$("#slide_id"+liid).removeClass("slideShow");
			$("#slide_id"+liid).addClass("slidehide");
		});
  	});
  	</script>
  	
  	<ul> 
  	<ul id="">
  		<div>
	  		<a class='test1' liid=1> test1test1test1test1</a>
	  		<ul ID="slide_id1" style="display:none;">
	  			<li>AAAAA</li>
	  			<li>BBBB</li>
	  			<li>CCCCC</li>
	  		</ul>
  		</div> 
  		
  		<div>
	  		<a class='test1' liid=2> test1test1test1test1</a>
	  		<ul ID="slide_id2" style="display:none;">
	  			<li>AAAAA</li>
	  			<li>BBBB</li>
	  			<li>CCCCC</li>
	  		</ul>
  		</div> 
  		<div>
	  		<a class='test1' liid=3> test1test1test1test1</a>
	  		<ul ID="slide_id3" style="display:none;">
	  			<li>AAAAA</li>
	  			<li>BBBB</li>
	  			<li>CCCCC</li>
	  		</ul>
  		</div> 
  	</ul>
  	
  	
	</body>
</html>