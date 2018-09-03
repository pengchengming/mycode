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
<title>Insert title here</title>
<script type="text/javascript">
	$(document).ready(function(){
		var url = Zflow.url.formConfigFields;
		var data = {
			formId : 1
		};
		jQueryPost(url,data, function(data) {
			var data = $.evalJSON(data);
			var configField = data.configField;
			var propertyList = configField.formfields;
			var List = configField.tablefields;
			var formHtml = configField.formHtml;
			$("#formHtml").html(formHtml);	
			if(propertyList){
				$.each(propertyList,function(i,n){
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
		 	$(".datepicker").icalendar({showMonths:1, selected:true});
			$(".findTable").click(function(){
				var table = $(this).parent().attr("table");
				var val = $(this).parent().attr("val");
				$.ajax({
					url : Zflow.url.getObjectsCollection,
					type : "POST",
					data : {
						tableName:table
					},
					complete : function(xhr, textStatus) {
						var data = $.evalJSON(xhr.responseText);
						var tabledataPlugin= Zplugin.columnConfig(data.tablePropertys,data.tableDatas);
						Zplugin.createTableList(tabledataPlugin,val);
					}
				});
			});
			$(".findTree").click(function(){
				var val = $(this).parent().attr("val");
				var treeval = $(this).parent().attr("treeval");
				$.ajax({
					url : Zflow.url.orgTreeNode,
					type : "POST",
					complete : function(xhr, textStatus) {
						if(treeval == "radio"){
							orgTreeNode.selectType="radio";
							orgTreeNode.isLevelRadio=true;
						}
						if(treeval == "checkbox"){
							orgTreeNode.selectType="checkbox";
							orgTreeNode.isLevelRadio=true;
						}
						if(treeval == "true"){
							orgTreeNode.selectType="radio";
							orgTreeNode.isLevelRadio=false;
						}
						if(treeval == "radio"){
							orgTreeNode.selectType="radio";
							orgTreeNode.isLevelRadio=true;
						}
						var zNodes = $.evalJSON(xhr.responseText);
						if(zNodes)
							orgTreeNode.zNodes=zNodes;
						$.fn.zTree.init($("#treeDemo"),orgTreeNode.setting, zNodes);
						$("#show_tree_dialog").dialog({
							title:"组织架构",
							position:'center',
							buttons: {
								"提交": function() {
									var foreignkeyId = $(this).find(":radio").filter(":checked").attr("id").substring(11);
									var id = $(this).find(":radio").filter(":checked").next().attr("id");
									var spanId = id.substring(0,id.lastIndexOf("a")) + "span";
									$("#"+ val).val($("#"+spanId).text());
									$("#"+ val).attr("foreignkey",foreignkeyId);
									$(this).dialog("close");
								},
								"关闭": function() {
									$(this).dialog("close");
								}
							},
							modal: true
						});
					}
				});

			});
			$("#formHtml").find("table").attr("border","0");
			$(".pShow").show();
			$("#selectable .drag").removeClass("drag");
			$(".createList").click(function(){
				var middleId = $(this).parent().next('table').attr("tableid");
				var tdLength = $(this).parent().next('table').find('tr').first().children().length;
				$(this).parent().next('table').append("<tr class='addRow' height='25px'><td width='80' ><input type='checkbox' name='deleteList' /></td></tr>")
				for ( var int = 0; int < tdLength - 1; int++) {
					$(this).parent().next('table').find('tr').last().append("<td><input class='keyVal' type='text'size='20'></td>")
				}
			});
			$(".listAll").click(function(){
				if($(this).attr('checked')){
					$(this).parent().parent().parent().parent().find(":checkbox[name='deleteList']").attr("checked",true);
				}else{
					$(this).parent().parent().parent().parent().find(":checkbox[name='deleteList']").attr("checked",false);
				}
			});
			
			$(".deleteListRow").click(function(){
				var deleteList = $(this).parent().next('table').find(":checkbox[name='deleteList']");
				$.each(deleteList,function(i,n){
					if($(deleteList[i]).attr("checked")){
						$(deleteList[i]).parent().parent().remove();
					}
				});
			}); 
 			$(".buttonUpload").click(function(){
				var $this = $(this).parent();
				uploadFile($this);
			}); 
 			
			var tableDataList = new Array();
			var middleDatabase = {};
			middleDatabase.middleTableId = middleId;
			var dataBase = {};
			$.each(propertyList, function(i,n) {
				var id = n.fieldName;
				if(n.validator){
					$("#"+id).attr("onblur","checkInfo('"+id+"',"+n.empty+",'"+n.validator+"','"+n.minLength+"','"+n.fieldLength+"')");
				}
				if(n.extraAttributes.form_to){
					$("#"+id).attr("onblur","checkInfo('"+id+"',"+n.empty+",'"+n.validator+"','"+n.minLength+"','"+n.fieldLength+"','"+n.extraAttributes.form_to+"')");
				}
				if(n.foreignkey){
					dataBase[id] = $("#"+id).attr("foreignkey");
				}else{
					dataBase[id] = $("#"+id).val();
				}
				
			});
			tableData.tableDataMethod.enteringDialog(formId,dataBase,List);
		});
	});
</script>
</head>
<body>
	<div id="formHtml"></div>
		<!-- radio 模板  -->
	<p style="display: none">
		<textarea id="template_radio" rows="0" cols="0">
				<!--
				<div class='drag' type='options'>
				{#foreach $T.option as info}
					<input name='name_{$T.name}' type='radio' value='{$T.info.id}'/><span>{$T.info.name}</span>
				{#/for}
				</div>
				-->
			</textarea>
	</p>

	<!-- 下拉模板 -->
	<p style="display: none">
			<textarea id="template_select" rows="0" cols="0">
				<!--
				{#foreach $T.option as info bengin=0}
				<option value="{$T.info.code}" id="{$T.info.id}">{$T.info.name}</option>
				{#/for}
				-->
			</textarea>
	</p>

	<!-- 下拉模板 -->
	<p style="display: none">
			<textarea id="template_select1" rows="0" cols="0">
				<!--
				{#foreach $T.option as info bengin=0}
				<option value="{$T.info.id}">{$T.info.name}</option>
				{#/for}
				-->
			</textarea>
	</p>
	
	<!-- 多选模板 -->
	<p style="display: none">
		<textarea id="template_check" rows="0" cols="0">
				<!--
				<div class='drag' type='options'>
				{#foreach $T.option as info}
				<input name='name_{$T.name}' type='checkbox' value='{$T.info.id}'/><span>{$T.info.name}</span>
				{#/for}
				</div>
				-->
			</textarea>
	</p>
	
</body>
</html>