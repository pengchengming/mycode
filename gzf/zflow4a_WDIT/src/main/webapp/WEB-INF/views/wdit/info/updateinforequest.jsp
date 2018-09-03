<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>接口</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<style type="text/css">
.bgred{ color:red}	
</style>
<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var id = '${inforequestid}';
$(function(){
	getinfo();
});

function getinfo(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_info_request",
			condition: " id="+id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				if(obj.request_id){
					$("#request_id").val(obj.request_id);
				}
				if(obj.isGenerate&&obj.isGenerate==1){
					$("#isGenerate").attr("checked","checked");
				}
				if(obj.type){
					$("#type").val(obj.type);
				}
			}			
		}
	}); 
}

function checksave(){
	 var request_id=$("#request_id").val();
	 if(request_id.length<=0){
		 alert("请填写 对应批次");
		 return;
	 }
	 if(request_id.length>=11){
		 alert("对应批次 超出长度规范");
		 return;
	 }
	 var isGenerate=0;
	 if($("#isGenerate").get(0).checked){
		 isGenerate=1;
	 }
	 var jsonString={
				"formId": 94,
				"tableDataId":id,
				"register":{"request_id":request_id,
							"type":$("#type").val(),
							"isGenerate":isGenerate
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
							location="inforequestlist";
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
                                <li>申请数据同步接口</li>
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
                                           			<td width="200px">申请ID:</td>
                                           			<td width="300px"><input type="text" id="request_id" /></td>
                                           			<td align="right" width="100px">是否生成数据:</td>
                                           			<td ><input type="checkbox" name="valid_1n" id="isGenerate" /></td>
                                           		</tr>
                                           		<tr>
                                           			<td>数据同步:</td>
                                           			<td>
                                           				<select id="type">
                                           					<option value="1">请求数据</option>
                                           					<option value="2">预审审批数据</option>
                                           					<option value="3">资质审批数据</option>
                                           				</select>
                                           			</td>
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