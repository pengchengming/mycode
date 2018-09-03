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
<%-- 		<spring:message code="application_name" var="app_name" htmlEscape="false" /> --%>
<%-- 		<title><spring:message code="welcome_h3" arguments="${app_name}" /></title> --%>
			<c:url value="/j_spring_security_logout" var="logoutUrl"/>
			<link rel="stylesheet" href="<c:url value="/css/index.css" /> " type="text/css">  
			<script type="text/javascript" src="<c:url value="/javascript/formJs/initformPage.js" />"></script>
	</head>
	
	<script type="text/javascript">
		$(function(){
			var code =$("#code").val();
			var url = Zflow.url.tableViewSelect;
			// 参数
			var parameter = {
				"formCode" : code
			};
			jQueryPost(
					url,
					parameter,
					function(data) {
						tableData.tableDataMethod.shwoCondition($("#form_table_guide"), data);

						$("#tableViewselect")
								.dialog(
										{
											width : 680,
											title : "表单向导--" + code,
											position : 'center',
											modal : true,
											buttons : [
													{
														text : "确定",
														className : 'saveButtonClass',
														click : function() {
															var datajson_checkbox =createTable. getcheckBoxData(
																	"conditionField",
																	data);
															if (datajson_checkbox.length != 1) {
																alert("请选择查询条件，并且只能选择一个查询条件");
																return;
															}
															var initform=new initformPage();
															initform.createTableView(code,datajson_checkbox[0].id,'#tableView');
															$(this).dialog("close");
														}
													},
													{
														text : "关闭",
														className : 'closeButtonClass',
														click : function() {
															$(this).dialog("close");
														}
													} ]

										});
						
					});
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
		<div id="jbpmWorkspace"> 
			<input type="hidden" id="code" value="${code}">
			<div id="tableViewselect">
				<div id="form_table_guide"></div>
			</div>
			<div id="tableView"></div>
		</div>
		
		<div class="clear"></div>
	</div>
	<div class="clear"></div>

	</body>
</html>