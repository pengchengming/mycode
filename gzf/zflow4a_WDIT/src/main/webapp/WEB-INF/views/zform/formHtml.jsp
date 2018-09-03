<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.bizduo.zflow.domain.sys.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/commonCss.jsp"%>
<%@ include file="/page/common/commonJs.jsp"%>
<%@ include file="/page/common/formJs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:url value="/j_spring_security_logout" var="logoutUrl"/>
<style>
#selectable .ui-selecting {
	background: #FECA40;
} 
</style>
<script type="text/javascript">
var List=null;
var formId=null;
var dataBase = {};
$(function(){
	formId = $("#zformId").val();
	//获取表单的html 控件信息
	var url = Zflow.url.formConfigFields;
	var data = {
		formId : formId
	};
	jQueryPost(url,data, function(data) {
		var data = $.evalJSON(data);
		var configField = data.configField;
		var propertyList = configField.formfields;
		List = configField.tablefields;
		var formHtml = configField.formHtml;
		$("#jbpmWorkspace").html(formHtml);	
		$("#jbpmWorkspace").find("table").attr("border","0");
		dataBase = {};
		if(propertyList){
			$.each(propertyList,function(i,n){
				//验证
				var id = n.fieldName;
				if(n.validator){
					$("#"+id).attr("onblur","checkInfo('"+id+"',"+n.empty+",'"+n.validator+"','"+n.minLength+"','"+n.fieldLength+"')");
				}
				if(n.extraAttributes.form_to){
					$("#"+id).attr("onblur","checkInfo('"+id+"',"+n.empty+",'"+n.validator+"','"+n.minLength+"','"+n.fieldLength+"','"+n.extraAttributes.form_to+"')");
				}
				if(n.foreignkey){
					dataBase[id] =  $("#"+id).attr("foreignkey");
				}else{
					dataBase[id] = $("#"+id).val();
				}
				
				var elementType = document.createElement(n.elementType);
				$(elementType).attr(n.extraAttributes);
				$("#input_"+n.extraAttributes["id"]).append(elementType);
				if(n.option){
					n.name = n.fieldId; 
					switch(n.extraAttributes.type){
					case "select":
						$(elementType).setTemplateElement("template_select1");
						$(elementType).processTemplate(n);
						break;
					case "radio":
						$(elementType).setTemplateElement("template_radio");
						$(elementType).processTemplate(n);
						break;
					case "checkbox":
						$(elementType).setTemplateElement("template_check");
						$(elementType).processTemplate(n);
						break;	
					}
				}
			});
		}
		//时间控件
		$(".datepicker").icalendar({showMonths:1, selected:true});
		//字表集的操作显示
		$(".pShow").show();
		//移除拖拽
		$("#selectable .drag").removeClass("drag");
		
		$(".createList").click(function(){
			var middleId = $(this).parent().next('table').attr("tableid");
			var tdLength = $(this).parent().next('table').find('tr').first().children().length;
			$(this).parent().next('table').append("<tr class='addRow' height='25px'><td width='80' ><input type='checkbox' name='deleteList' /></td></tr>")
			for ( var int = 0; int < tdLength - 1; int++) {
				$(this).parent().next('table').find('tr').last().append("<td><input class='keyVal' type='text'size='20'></td>")
			}
		});
// 		alert(formId+"___"+dataBase+"___"+List)
// 		var jsonString=tableData.tableDataMethod.getFormDataBase(formId,dataBase,List);
// 		//tableData.tableDataMethod.enteringDialog(formId,dataBase,List);
//  		var jsonString= JSON.stringify(jsonString);
	});
});
//保存数据
function saveFormData(){
	var pass = false;
	$.each(dataBase,function(i,n){
		$("#"+i).blur();
		if($("#input_"+i).parent().find("label").length != 0){
			pass = false;
			return pass;
		}else{
			 pass = true;
		}
	});
	if(pass){
		saveformDatajson(formId,dataBase,List);
		//$(this).dialog("close");
	}else{
		alert("信息不完整或格式有误!");
	}
}


function saveformDatajson(Id,register,List){
	var jsonString=tableData.tableDataMethod.getFormDataBase(Id,register,List);
	var taskId=$("#taskId").val();
	if(taskId)
		jsonString.register.taskId=taskId;
	var participationId=$("#participationId").val();
	if(participationId)
		jsonString.register.participationId=participationId;
	
	var json =JSON.stringify(jsonString);
// 	alert(json);
	$.ajax({
		url : Zflow.url.saveRegister,
		type : "POST",
		data : {
			jsonString : JSON.stringify(jsonString)
		},
		complete : function(xhr, textStatus) {
			var data = $.evalJSON(xhr.responseText);
			alert("保存成功！");
		}
	});
}

</script>
<title>Zflow——工作流系统</title>
</head>
<body>
<input  type="hidden"  id="taskId" value="${taskId}">
<input  type="hidden"  id="participationId" value="${participationId}">
<input  type="hidden" id="zformId" value="${zformId}">
	<div class="content">
		<div class="head">
			<div class="logo_here">
				<font style="color: #999; font-size: 20px; font-family: 'Arial'">
					logo here </font>
				<ul>
					<li style="float:right;">&nbsp;&nbsp;&nbsp;</li>
	  				<li style="float:right;"><a href="${logoutUrl}" style="color:white;">安全退出</a></li>
	  				<li style="float:right;">&nbsp;&nbsp;&nbsp;</li>
	  				<li style="float:right;"><a href="#" style="color:white;">欢迎您： ${sessionScope.USER.username}</a></li>
	  			</ul>
			</div>
			<div class="nav" style="color: #fff; font-size:14px; font-family: 'Arial'">
			</div>
		</div>
		<div class="main">
			<!--拖拽杆-->
<!-- 			<div class="draggable" style="display: none"></div> -->
			<!--工作区-->
			<div class="jbpmWorkspace" id="jbpmWorkspace_id">
				<div class="workspace_title">
					<font style="color: #fff; font-size: 14px; font-family: 'Arial'">workspace </font>
				</div>
				<div class="toolbar">
					<div id="tool">
					</div>
					<div id="template">
					</div>
				</div>
			<div class="workspace_inner">
				<div style="height: 780px;">
					<div id="jbpmWorkspace">  </div>
					<div style=" float: right;">
						<button type="button" onclick="saveFormData()">保存</button>
					</div>
				</div>
			</div>
		</div>
			
		
		
<!-- 	<div class="foot"> -->
<!-- 		<font style="color: #fff; font-size: 12px; font-family: 'Arial'"> -->
<!-- 			power by bizduo </font> -->
<!-- 	</div> -->
	</div>
	<%@ include file="/page/jsp/form/template.jsp"%>
</body>
</html>