<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=8" />	
			
		<spring:message code="application_name" var="app_name" htmlEscape="false" />
		<title><spring:message code="welcome_h3" arguments="${app_name}" /></title>
	</head>
	<script type="text/javascript">

    </script>
  	<body class="tundra spring">
<!--    		<div id="wrapper"> -->
<%-- 		    <tiles:insertAttribute name="header" ignore="true" /> --%>
<%-- 		    <tiles:insertAttribute name="menu" ignore="true" />    --%>
<!-- 		    <div id="main"> -->
<%-- 	    		<tiles:insertAttribute name="body"/>  --%>
<%-- 		    	<tiles:insertAttribute name="footer" ignore="true"/> --%>
<!-- 		    </div> -->
<!-- 		</div> -->
	</body>
</html>
