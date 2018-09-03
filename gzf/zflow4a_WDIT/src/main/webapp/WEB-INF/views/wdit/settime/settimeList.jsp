<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>申请参数设置</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">

var nowtime;
$(function(){
	$("#startbtn").hide();
	$("#endbtn").hide();
	getsettime();
})


function getsettime(){
	for(var j=1000;j<1006;j++){
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"wdit_request_settime",
				condition: " id="+j
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;
				$.each(results,function(i,obj){
					$("#approvalLimit"+j).val(obj.approvalLimit);
					$("#warning"+j).val(obj.warningLimit);
				})
			}
		});
	}
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_request_settime",
			condition: " id="+1006
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			$.each(results,function(i,obj){
				var status=obj.status;
				nowtime=obj.nowDate;
				nowtime=nowtime.substr(0,10);
				if(status==1){
					$("#overtime").attr("readonly","true");
					$("#overtime").removeAttr("onfocus");
					$("#starttime").attr("readonly","true");
					$("#starttime").removeAttr("onfocus");
					$("#endbtn").show();
				}else if(status==0){
					$("#overtime").removeAttr("readonly");
					$("#starttime").removeAttr("readonly");
					$("#overtime").attr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd'})");
					$("#starttime").attr("onfocus","WdatePicker({dateFmt:'yyyy-MM-dd'})");
					$("#startbtn").show();
				}
				$("#overtime").val(obj.applicationOverTime);
				$("#starttime").val(obj.applicationStartTime);
			})
		}
	});
}

function time(id){
	var overtime=$("#overtime").val();
	var starttime=$("#starttime").val();
	if(id==1){
		if(starttime<nowtime){
			alert("起始时间不能小于当前时间！！");
			return false;
		}
		if(starttime>=overtime){
			alert("结束时间不能小于起始时间！！");
			return false;
		}
		if($("#overtime").val()==''||$("#starttime").val()==''){
			alert("填写日期！！！");
			return false;
		}	
	}
	var obj=new Object();
	obj.applicationOverTime=$("#overtime").val();
	obj.applicationStartTime=$("#starttime").val();
	if(id==1){
		obj.status=1;
	}else if(id==0){
		obj.status=0;
	}
	var jsonString={
			"formId":60,
			"tableDataId":1006,
			"register":obj
	}
	$.ajax({
		url : rootPath+'/forms/saveFormDataJson',
		type : "POST", 
        async: false,
        data : {
        	json : JSON.stringify(jsonString)
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.successMsg){
				alert("操作成功！");
				window.location.reload();
	 		}else{
	 			alert("操作失败");
	 			return false;
	 		}
		    }
        });
}

function addsettime(){
	var ispass=true;
	for(i=1000;i<1006;i++){
		var approvalLimit=$("#approvalLimit"+i).val();
		var warningLimit=$("#warning"+i).val();
		if(approvalLimit==''||warningLimit==''){
			alert("请填写完整！！");
			ispass=false;
			return false;
		}
		if(parseInt(warningLimit)>parseInt(approvalLimit)){
			alert("预警期限不能大于审批期限！");
			ispass=false;
			return false;
		}
	}
	if(!ispass){
		return false;
	}
	for(i=1000;i<1006;i++){
		var approvalLimit=$("#approvalLimit"+i).val();
		var warningLimit=$("#warning"+i).val();
		var obj=new Object();
		obj.approvalLimit=approvalLimit;
		obj.warningLimit=warningLimit;
		var jsonString={
				"formId":60,
				"tableDataId":i,
				"register":obj
		}
		$.ajax({
			url : rootPath+'/forms/saveFormDataJson',
			type : "POST", 
	        async: false,
	        data : {
	        	json : JSON.stringify(jsonString)
	        }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.successMsg){
					if(i==1005){
						alert("操作成功！！");
						window.location.reload();
					}
		 		}else{
		 			alert("操作失败");
		 			return false;
		 		}
			    }
	        });
	}
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
                                <li>申请参数设置</li>
                            </ol>
                        </div>
                    </div>
                    <!-- end: BREADCRUMB -->
                    <!-- start: PAGE CONTENT -->
                    <div class="row">
                        <div class="col-sm-12">
                            <!-- start: DATE/TIME PICKER PANEL -->
                            <div class="panel panel-white">
                                <div class="panel-body no-padding-top">
                                	<form class="form-horizontal" role="form">
                                	<div class="row">
                               			<div class="col-sm-12" class="over-hidden">
                               				<h5 class="over-hidden" style='color:black'> </h5>
                               			</div>
                               		</div>
                                    <div class="row">
                                    	<div class="col-sm-9">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1">*申请起始时间</label>
                               					<div class="col-sm-9">
                               						<input id="starttime" class="form-control" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="xxxxxxxx"/>
                               					</div>
                               				</div>
                               			</div>
                                    </div>
                                    <div class="row">
                                    	<div class="col-sm-9">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1">*申请结算时间</label>
                               					<div class="col-sm-9">
                               						<input id="overtime" class="form-control" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text" placeholder="xxxxxxxx">
                               					</div>
                               				</div>
                               			</div>
                                    </div> 
                                    <div class="row">
                                    	<div class="col-sm-3" id="startbtn">
                               				<div class="form-group">
                               					<label class="col-sm-3 control-label" for="form-field-1"><a class="btn btn-default" onclick="time(1)">启动申请</a></label>
                               				</div>
                               			</div>
                               			<div class="col-sm-3" id="endbtn">
                               				<div class="form-group">
                               					<label class="col-sm-3 control-label" for="form-field-1"><a class="btn btn-default" onclick="time(0)">停止申请</a></label>
                               				</div>
                               			</div>
                                    </div>
                                    <div style="border-top:1px solid #000;width:100%;height:1px;"> </div>
                                    <div class="row" style="margin-top:10px;">
                                    	<div class="col-sm-6">
	                                    <table id="type2" class="table table-bordered table-hover table-horizontal">
	                               			<tbody>
	                               				<tr>
	                               					<th class="text-center" width="200">审批步骤</th>
	                               					<th class="text-center" width="200">审批期限</th>
	                               					<th class="text-center" width="200">预警期限</th>
	                               				</tr>
	                               				<tr>
	                                                <td class="text-center">房管局-预审</td>
	                                                <td><input id='approvalLimit1000' type='text' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                                                <td><input id='warning1000' type='text' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                               				</tr>
	                               				<tr>
	                                                <td class="text-center">房管局-资质审核</td>
	                                                <td><input id='approvalLimit1001' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')"  /></td>
	                                                <td><input id='warning1001' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                               				</tr>
	                               				<tr>
	                                                <td class="text-center">科/经/商委-资质审核</td>
	                                                <td><input id='approvalLimit1002' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                                                <td><input id='warning1002' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                               				</tr>
	                               				<tr>
	                                                <td class="text-center">人社局-资质审核</td>
	                                                <td><input id='approvalLimit1003' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                                                <td><input id='warning1003' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                               				</tr>
	                               				<tr>
	                                                <td class="text-center">人才办</td>
	                                                <td><input id='approvalLimit1004' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                                                <td><input id='warning1004' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                               				</tr>
	                               				<tr>
	                                                <td class="text-center">房管局-现场审核</td>
	                                                <td><input id='approvalLimit1005' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                                                <td><input id='warning1005' type='text' class='text-left' onkeyup="value=value.replace(/[^\d]/g,'')" /></td>
	                               				</tr>
	                               			</tbody>
	                               		</table>
	                               		<div class="col-sm-2">
	                               			<a class="btn btn-info margin-right-10" onclick="addsettime()">保存</a>
	                               		</div>
	                               		</div>
	                               	</div>
                                    </form> 
                                 </div>
                             </div>
                         </div>
                            <!-- end: DATE/TIME PICKER PANEL -->
                     </div>
                    </div>
                    <!-- end: PAGE CONTENT-->
                </div>

            </div> 
        </div> 
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>

</body>
</html>