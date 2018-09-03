<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/commonCss.jsp"%>
<%@ include file="/page/common/commonJs.jsp"%>
<%@ include file="/page/common/formJs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
#selectable .ui-selecting {
	background: #FECA40;
} 
</style>
<title>Zflow——工作流系统</title>
<script type="text/javascript">
function savedata(){
	var jsonString= $("#testData_id").val();
	var parameter={
			"jsonString":jsonString
	};
	var url=Zflow.url.saveRegister;
	jQueryPost(url,parameter, function(data) {
		var data=$.evalJSON(data);
	});
}
function getData(){
	url =rootPath+'/forms/getDataByFormId';
	var param= $("#testData_id").val();
	url +="?"+param
	$.ajax({
			url : url,
			type : "POST",
			complete : function(xhr, textStatus){
				$("#returnData_id").html(xhr.responseText);
				var data = JSON.parse(xhr.responseText);
				alert(data.code);
			}
		});
}
</script>
</head>
<body>
数据: <input id="testData_id" />
</br>
<input type="button" onclick="savedata()" value="保存">
<input type="button" onclick="getData()" value="查询根据formcode和DataId">
<span id="returnData_id"></span>

</body>
</html>