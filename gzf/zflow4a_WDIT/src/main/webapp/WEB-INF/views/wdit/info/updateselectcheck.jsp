<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>查询检查</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<style type="text/css">
.bgred{ color:red}	
</style>
<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var id = '${infoid}';
$(function(){
	getinfo();
});

function getinfo(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_info",
			condition: " id="+id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				if(obj.code){
					$("#code").val(obj.code);
				}
				if(obj.type){
					$("#type").val(obj.type);
				}
				if(obj.selectConditionSql){
					$("#selectConditionSql").val(obj.selectConditionSql);
				}
				if(obj.remark){
					$("#remark").val(obj.remark);
				}
				if(obj.fromSql){
					$("#fromSql").val(obj.fromSql);
				}
			}			
		}
	}); 
}

function checksave(){
	 var code=$("#code").val();
	 if(code.length>=50){
		 alert("对应批次 超出长度规范");
		 return;
	 }
	 var selectConditionSql=$("#selectConditionSql").val();
	 if(selectConditionSql.length<=0){
		 alert("请填写查询条件");
		 return;
	 }
	 if(selectConditionSql.length>=255){
		 alert("查询条件 超出长度规范");
		 return;
	 }
	 var fromSql=$("#fromSql").val();
	 if(fromSql.length<=0){
		 alert("请填写查询语句");
		 return;
	 }
	 if(fromSql.length>=255){
		 alert("查询语句 超出长度规范");
		 return;
	 }
	 var remark=$("#remark").val();
	 if(remark.length>=255){
		 alert("描述 超出长度规范");
		 return;
	 }
	 var jsonString={
				"formId": 84,
				"tableDataId":id,
				"register":{"code":$("#code").val(),"type":$("#type").val()
							,"selectConditionSql":$("#selectConditionSql").val(),"remark":$("#remark").val(),
							"fromSql":$("#fromSql").val()
							}
		};
			url =rootPath+'/forms/saveFormDataJson';		
			$.ajax({
				url : url,
				type : "POST", 
		        async: false,
		        data : {
		        	json : JSON.stringify(jsonString)
		        }, complete : function(xhr, textStatus){
					var data = JSON.parse(xhr.responseText);
						if(data.successMsg){
							alert("修改"+data.successMsg);
							location="infolist";
					 	}
				    }
		     });
}

</script>

</head>

<body class="horizontal-menu-fixed">

    <div class="main-wrapper">
		<%@ include file="/WEB-INF/views/wdit/head.jsp"%> 
		<%@ include file="/WEB-INF/views/wdit/leftMenu.jsp"%>
        <!-- start: MAIN CONTAINER -->
        <div class="main-container inner">
            <!-- start: PAGE -->
            <div class="main-content" style="min-height: 542px;">
                <div class="container">
                    <!-- start: BREADCRUMB -->
                    <div class="row hidden-sm hidden-xs">
                        <div class="col-md-12">
                            <ol class="breadcrumb">
                                <li>查询检查</li>
                                <li class="active">修改</li>
                            </ol>
                        </div>
                    </div>
                    <!-- end: BREADCRUMB -->
                    <!-- start: PAGE CONTENT -->
                    <div class="row">
                        <div class="col-sm-12">
                           
                            <div class="panel panel-white">
                                <div class="panel-body no-padding-top">
                                    <div class="row">
                                        <div class="col-sm-12">
                                           	<table class="table">
                                           		<tr>
                                           			<td>接口批次:</td>
                                           			<td><input type="text" id="code" /></td>
                                           			<td>接口类型:</td>
                                           			<td>
                                           				<select id="type">
                                           					<option value="1">申请导出</option>
                                           					<option value="2">申请导入</option>
                                           					<option value="3">审批导出</option>
                                           					<option value="4">审批导入</option>
                                           				</select>
                                           			</td>
                                           		</tr>
                                           		<tr>
                                           			<td>查询条件:</td>
                                           			<td><input type="text" id="selectConditionSql" /></td>
                                           		</tr>
                                           		<tr>
                                           			<td>描述:</td>
                                           			<td><input type="text" id="remark" /></td>
                                           		</tr>
                                           		<tr>
                                           			<td>查询语句：</td>
                                           			<td><input type="text" id="fromSql" /></td>
                                           		</tr>
                                           		<tr border="1px" >
                                           			<td  colspan="4" align="center">
                                           				<button class="btn btn-primary" type="button" onclick="checksave()">保存</button>
                                           			</td>
                                           			
                                           		</tr>
                                           	
                                           	</table>
                                        </div>
                                    </div>  
                                 </div>
                             </div>
                         </div>
                     </div>
                    </div>
                    <!-- end: PAGE CONTENT-->
                </div>

            </div> 
        </div> 
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>

</body>
</html>