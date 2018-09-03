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
		
		//操作 ：批准，驳回
		function operating(me,id){
			var resultValue= $(me).val();
			$.ajax({
				url : rootPath+"/procinstJbpm/managerOperating.do",
				type : "POST",
				data : {
					id:id,
					resultValue:resultValue
				},
				complete : function(xhr, textStatus) {
					var data = $.evalJSON(xhr.responseText);
					if(data&&data.code&&data.code=="0"){
						alert(data.msg);
						window.location.replace('<c:url value="/procinstJbpm/indexjbpmhome" />');  
						
					}
					
				}
			});
		}
	</script>

    
  	<body id="index_body">
  		<%@ include file="/head.jsp"%>
	<%@ include file="/leftMenu.jsp"%>
	
	<div class="index_content">
		<input type="hidden" id="id" value="${id}" />
		<input type="hidden" id="code" value="${map.tableName }" />
		<input type="hidden" id="tableDataId" value="${map.dataId}" />
		
		<div id="jbpmWorkspace"> 
			<div id="formView"> </div>
			<div class="clear"></div>
			<div id="tableView"></div>
			<div>
				<input type="button" value="批准" onclick="operating(this,${id})"/>
				<input type="button" value="驳回" onclick="operating(this,${id})"/>
			</div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="clear"></div>

	</body>
</html>