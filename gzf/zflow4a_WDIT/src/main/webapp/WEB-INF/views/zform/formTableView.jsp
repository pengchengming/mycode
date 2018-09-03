<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/commonCss.jsp"%>
<%@ include file="/page/common/commonJs.jsp"%>
<%@ include file="/page/common/formJs.jsp" %>
<html> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=8" />	
			<c:url value="/j_spring_security_logout" var="logoutUrl"/>
			<link rel="stylesheet" href="<c:url value="/css/index.css" /> " type="text/css">  
			<script type="text/javascript" src="<c:url value="/javascript/formJs/initformPage.js" />"></script>
	</head>
	
	<script type="text/javascript">
		$(function(){
			var code =$("#code").val();
			var tableDataId=$("#tableDataId").val();
			var formView=$("#formView");
			var initform=new initformPage();
			initform.initFormTablePage(code,"#formView",tableDataId);
		});
		
	</script>

    
  	<body id="index_body">
  	<div class="index_top">
  		<ul>
  			<li><a href="${logoutUrl}">安全退出</a></li>
  			<li><a href="#">欢迎您： ${sessionScope.USER.username}</a></li>
  		</ul>
  	</div>
	<div class="index_banner">
		Application_Name
	</div>
	<div class="index_menu">
		<ul>
<!-- 			<li>首页</li> -->
<!-- 			<li id="li_form_id" onClick="index.form()">表单管理</li> -->
<!-- 			<li>流程管理</li> -->
<!-- 			<li>系统管理</li> -->
<!-- 			<li>我的任务</li> -->
<!-- 			<li>关于</li> -->
				<c:forEach var="mi" items="${sessionScope.MENUITEMS}">
					<li><a href="#">${mi.name}+${mi.level}</a></li>
				</c:forEach>
			<div class="nav" style="color: #fff; font-size:14px; font-family: 'Arial'">

				<c:forEach var="mi" items="${sessionScope.MENUITEMS}">
					<ul style="float: left;width:70px;padding-top:8px;margin-left:20px;">
						<li style="float: left;"><a href="<c:url value="${mi.url}"/>?mid=${mi.id}"  class="">${mi.name}+${mi.level}</a>
						<c:forEach var="tmi" items="${mi.subMenuItemList}">
							<ul style="background:#6794D9;z-index:100;position:absolute;">
								<li><a href="<c:url value="${tmi.url}"/>?mid=${tmi.id}"  class="" style="font-size:12px;">${tmi.name}+${tmi.level}</a>
							
							<c:forEach var="thmi" items="${tmi.subMenuItemList}" >
								<ul style="color:red;">
									<li><a href="<c:url value="${thmi.url}"/>?mid=${thmi.id}"  class="">${thmi.name}+${thmi.level}</a></li>
								</ul>	
							</c:forEach>
							</ul>
						</c:forEach>
						</li>
					</ul>	
				</c:forEach>
			</div>
		</div>
				<li id="li_form_id" onClick="index.form()"><a href="<c:url value='/page/jsp/form/formindex.jsp'/>" >表单管理</a></li>
			<div class="clear"></div>
		</ul>
	</div>
	
	<div class="index_left_menu">
		<span>菜单项</span>
	</div>
	
	<div class="index_content">
		<input type="hidden" id="code" value="${code}" />
		<input type="hidden" id="tableDataId" value="${tableDataId}" />
		
		<div id="jbpmWorkspace"> 
			<div id="formView"> </div>
			<div class="clear"></div>
			<div id="tableView"></div>
			aaaaaaaaaaaaa
		</div>
		<div class="clear"></div>
	</div>
	<div class="clear"></div>

	</body>
</html>