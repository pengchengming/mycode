<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>人社局-资质审核</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestCompanyShow.js" />"></script>
 

<script type="text/javascript">
var id='${id}';
var type='${type}';
var createTable = new createTable();
var step=4;

$(function(){
	initPage();
	if(!isshowbtreturn()){
		$("#bt_div").hide();
	}
});


function isshowbtreturn(){
	var flag = true;
	$.ajax({
		url : '<c:url value="/data/procedure"/>',
		type : "POST", 
	    async: false,
	    data : {
	    	p : 'R2014005|'+ id + '|4'
	    }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data&&data.length>0){
				var obj = data[0];
				if(obj.result == 1){//当有一个人审核了  退回按钮不再显示
					flag = false;
				}
			}
	   }
	})
	return flag;
}

function openeRequestUserList(){
	getRequestUserList(id,step); //人员列表
}

function show(id){
	if(id==1){
		$("#showcompany").show();
		$("#showuser").hide();
	}else if(id==2){
		$("#showcompany").hide();
		$("#showuser").show();
	}
}


//退回
function openlayer(){
	if(!isshowbtreturn()){
	     alert("当前已经有人审批了，不能再退回");
	     return false;
	}
	var divhtml = '<table  style="width: 400px;margin: auto;">'+
	              '<tr><th class="text-right"><span style="color:#FF0000;">*</span>备注</th>'+
	              '<td colspan="3" class="text-left"> <input type="text" placeholder="必填" id="returnRemark" class="form-control"></td></tr></table>'+
	              '<br><br><input type="button" id="" onclick="reback()" class="btn btn-info" style="margin-left:200px" value="确定">';
	var layerindex= layer.open({
	    type: 1,
	    area: ['500px', '250px'],
	    skin: 'layui-layer-rim', //加上边框 
	    title :'退回备注',
	    content: divhtml
	});
}



function reback(){
	var returnRemark = $("#returnRemark").val();
	if(!returnRemark){
		alert("退回备注必填！");
		return false;
	}else if(returnRemark.length>200){
		alert("退回备注超出长度！");
		return false;
	}
	var  byIdconfig2={
			fromConfig:[{
				formCode:"WDIT_Company_Approval",
				currentField:"request_Id",
				parentId:"id",
				aliasesName:"companyApprovalIds",
				fieldName:"id,approvalStep,status"	
			}]
	}
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request",
			condition: "id="+id,
			byIdconfig:JSON.stringify(byIdconfig2)
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.code + "" == "1"){
				var jsons = new Array();
				var comrequestobj = new Object();
				comrequestobj["formId"]= 61;
				comrequestobj["tableDataId"] = id;
				comrequestobj["register"] = {"status":305,"isReturn":1};
				jsons.push(JSON.stringify(comrequestobj));
				var requestinfo = data.results[0];
				var adddataobj = new Object();
				adddataobj["formId"]= 66;
				adddataobj["register"] = {"status":41041,"returnRemark":returnRemark,"isReturn":1,"approval_Date":requestinfo.nowDate,"approvalStep":4,"request_Id":id,"companyId":requestinfo.companyId};
				jsons.push(JSON.stringify(adddataobj));
				
				var companyapproves = requestinfo.companyApprovalIds;//公司审核数组
				$.each(companyapproves,function(i,companyapprove){
					 if(companyapprove.approvalStep + ""== "3" && companyapprove.status + ""== "3102" ){
						 var comapprovrobj = new Object();
						     comapprovrobj["formId"]= 66;
						     comapprovrobj["tableDataId"] = companyapprove.id;
						     comapprovrobj["register"] = {"status":3104};
							 jsons.push(JSON.stringify(comapprovrobj));
					 }
				})
				
				if(jsons&&jsons.length>1){
					$.ajax({
						url : '<c:url value="/forms/saveFormDataJsons"/>',
						type : "POST", 
						async: false,
						data : { 
							jsons : jsons  
						}, complete : function(xhr, textStatus){
							var data = JSON.parse(xhr.responseText);
								if(data.code+""=="1"){
									alert("退回成功");
									window.location.href=rootPath+"/bureauhuman/rsjApprovalList";
							 	}else {
									alert(data.errorMsg);
							}	
						}});
				}else{
					alert("jsons太短");
				}
			}else{
				alert("查不到数据");
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
                                <li>人社局</li>
                                <li class="active"><a  href="<c:url value="/bureauhuman/rsjApprovalList" />">资质审核</a></li>
                                <li class="active">申请批次明细</li>
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
                                	<div class="row" style="margin-bottom:10px;margin-top:10px;">
                                		<input type="hidden" id="companyId" />
	                                    <div class="col-sm-12">
	                                        <div class="form-group">
	                                            <div class="title padding-20">
			                                         <ul class="nav nav-tabs" role="tablist">
			                                            <li role="presentation" class="active"><a class="f18" href="#" onclick="show(1)" aria-controls="tab-1" role="tab" data-toggle="tab">公司信息</a></li>
			                                            <li role="presentation"><a class="f18" href="#" onclick="show(2)" aria-controls="tab-2" role="tab" data-toggle="tab">员工</a></li>
			                                        </ul>
												 </div>
	                                        </div>
	                                    </div>
	                                </div> 
                                	<div id="showcompany" class="table-responsive" style="overflow: hidden;margin-top:10px;" tabindex="2">
										<%@ include file="/wdit/wdit/request/requestCompanyShow.jsp"%> 
										 <div class="col-sm-7" id="bt_div">
                                                    <a class="btn btn-default" href="javascript:void(0)" onclick="openlayer()">退回申请</a>
                                         </div>
                               		</div>
                               		<div id="showuser" style="display:none">
                               			<h4>人员信息列表</h4>
                               			<div class="table-responsive" id="form_table">
                                        </div>
                               		</div>
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