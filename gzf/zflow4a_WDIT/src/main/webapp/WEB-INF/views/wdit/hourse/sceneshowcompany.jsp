<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>房管局/现场审核/申请批次明细</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestCompanyShow.js" />"></script>

<script type="text/javascript">
var id='${id}';
var type='${type}'; 
var step=6;


var createTable = new createTable();

$(function(){
	initPage();
	getDataDictionaryValueHtml2();
	approvalstep();
})

function openeRequestUserList(){
	getRequestUserList(id,step); //人员列表
}

function reload(){
	window.location.reload();
}

function approvalstep(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company_request",
			condition: " id="+id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				if(obj.status ==502){
					$("#approvalstep").show();
				}else{
					$("#approvalstep").hide();
				}
			}			
		}
	});
}

//审核提交
function passapproval(judge){
	var Date;
	var flag=false;
	//获取审核人判断
	 $.ajax({
			url : rootPath+'/bureauhuman/getuser',
			type : "POST",
	        async: false,
	        data:{
	        	level:1
	        },
			complete : function(xhr, textStatus){
				var data=xhr.responseText
				if(data=="ok"){
				}else {
					alert("当前登录人没有审批权限");
					flag=true;
				}
			}
	});
	//获取状态判断
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company_request",
			condition: " id="+id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				var status=obj.status;
				Date = obj.nowDate;
				if(status&&status.length>0){
					if(status+""!="502"){
						flag = true;
						alert("当前申请已审批过！");
					}
					
				}
			}			
		}
	}); 
	if(flag){
		window.location.reload();
		return false;
	}
	var state = $("#bt_pass").val();//通过点击
	var ispass=$("#ispass").val();//下拉选择
	var nopass=$("#bt_not").val();//不通过点击
	var materailstate=$("#materailstate").val();
	if(materailstate==0){
		alert("请先确认材料"); 
		return false;
	}
	var jsons = new Array();
	var obj=new Object();
	obj.approvalStep=6;
	obj.approval_Date=Date;
	obj.companyId=$("#companyId").val();	
	obj.request_Id=id;
	if(judge==1){//审批通过
		if(ispass==1401){
		}else{
			alert("请选择正确企业资料审核结果");
			return false;
		}
		obj.status=6102;
		var jsonString={
				"formId":61,
				"tableDataId":id,
				"register":{status:604}
		}
		jsons.push(JSON.stringify(jsonString));
	}else if(judge==0){//不通过
		if(ispass==1402 || ispass==1403){
		}else{
			alert("请选择正确企业资料审核结果");
			return false;
		}
		obj.status=6103;
		var jsonString={
				"formId":61,
				"tableDataId":id,
				"register":{status:603}
		}
		jsons.push(JSON.stringify(jsonString));
		//往邮件表插一条记录
		var linkman = $("#linkman").html();
        var email = $("#email").html();	
		if(linkman&&email && linkman != "" && email != ""){
			 var emailobj = new Object();
          	 var content = linkman + " 您好：贵公司提交的公租房申请表在  现场审核  的审批中被审批拒绝。";
          	 emailobj["formId"]= 99;
          	 emailobj["register"] = {"email":email,"content":content,"type":1,"flag":0};// 1为发给公司
          	 jsons.push(JSON.stringify(emailobj));
		}	
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"wdit_company_request_user",
				condition: " status =5202 and  request_id="+id
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;
				if(results&&results.length>0){
					$.each(results,function(i,obj){ 
						var userrequsetobj=new Object();
						userrequsetobj["formId"]= 62;
						userrequsetobj["tableDataId"] = obj.id;
						userrequsetobj["register"] = {"status":6203};
						jsons.push(JSON.stringify(userrequsetobj));
						var adduserapprovalobj=new Object();
						adduserapprovalobj["formId"]= 67;
						adduserapprovalobj["register"] = 
							{"status":6203,"approvalStep":6,
							"requestUser_id":obj.id,"approval_Date":Date};
						jsons.push(JSON.stringify(adduserapprovalobj));
					});
				}			
			}
		});
	}
	var jsonString2={
			"formId":66,
			"register":obj
	}
	jsons.push(JSON.stringify(jsonString2));
	var index = layer.load(2);
	$.ajax({
		url : rootPath+'/forms/saveFormDataJsons',
		type : "POST", 
        async: false,
        data : {
        	jsons : jsons
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.successMsg){
				alert("审核成功");
				window.location.reload();
			}else{
				layer.close(index);
				return false;
			}
		    }
        });
}

function updateUserUnpass(Date){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company_request_user",
			condition: " status=5202 and  request_id="+id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var jsons = new Array();
				$.each(results,function(i,obj){ 
					var userrequsetobj=new Object();
					userrequsetobj["formId"]= 62;
					userrequsetobj["tableDataId"] = obj.id;
					userrequsetobj["register"] = {"status":6203};
					jsons.push(JSON.stringify(userrequsetobj));
					var adduserapprovalobj=new Object();
					adduserapprovalobj["formId"]= 67;
					adduserapprovalobj["register"] = 
						{"status":6203,"approvalStep":6,
						"requestUser_id":obj.id,"approval_Date":Date};
					jsons.push(JSON.stringify(adduserapprovalobj));
				});
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
									alert("审核成功");
							 	}else {
									alert(data.errorMsg);
							}	
						}});
				}else{
					alert("jsons太短");
				}
			}			
		}
	}); 
}

function getDataDictionaryValueHtml2(){
	   var   dictionaryValueHtml="";
	   $.ajax({
		    url : rootPath+"/dictionarys/dataDictionaryValueList.do",
			type : "POST",
			async:false,
			data:{
				code :140
			},
			complete : function(xhr, textStatus){
			var dictionaryCodeData = eval("("+xhr.responseText+")");
			if(dictionaryCodeData &&dictionaryCodeData .length>0){
				$.each(dictionaryCodeData,function(i,n){ 
					dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
				});
		   		}
			}
			});
	  $("#ispass").append(dictionaryValueHtml);
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

function materailpass(){
	$("#affirm").removeAttr("class");
	$("#affirm").attr("class","btn btn-default");
	
	$("#passapproval").removeAttr("class");
	$("#passapproval").attr("class","btn btn-info margin-right-10");
	$("#nopass").removeAttr("class");
	$("#nopass").attr("class","btn btn-info margin-right-10");
	
	$("#materailstate").val("1");
}
function passstate(){
	$("#affirm").removeAttr("class");
	$("#affirm").attr("class","btn btn-info margin-right-10");
	
	$("#passapproval").removeAttr("class");
	$("#passapproval").attr("class","btn btn-default");
	$("#nopass").removeAttr("class");
	$("#nopass").attr("class","btn btn-default");
	
	$("#materailstate").val("0");
}

function print_atta(param,me){
	  if(param == 1){
		   $("#printatta").attr("class","");
		   $("#printcompany").attr("class","active");
	       window.open(rootPath + "/hmcheck/printcompany?requreid="+id+"&step=6");
	  }else if(param == 2){
		  $("#printcompany").attr("class","");
		  $("#printatta").attr("class","active");
		  window.open(rootPath + "/hmcheck/printatta?requreid="+id+"&step=6");
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
                                <li>房管局</li>
                                <li class="active">现场审核</li>
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
	                                <input type="hidden" id="basis" />
	                                    <div class="col-sm-12">
	                                           <div class="form-group">
	                                            <div class="title padding-20">
			                                         <ul class="nav nav-tabs" role="tablist">
			                                            <li role="presentation" class="active"><a class="f18" href="#" onclick="show(1)" aria-controls="tab-1" role="tab" data-toggle="tab">公司信息</a>
			                                            <li role="presentation"><a class="f18" href="#" onclick="show(2)" aria-controls="tab-2" role="tab" data-toggle="tab">员工</a> 
			                                            <li style="margin-left: 550px;" id="printcompany"><a class="f18" onclick="print_atta(1)">打印申请表</a></li>
                                                        <li style="margin-left: 25px;" id="printatta"><a class="f18" onclick="print_atta(2)">打印附件</a></li>
			                                        </ul>
												 </div>
	                                        </div>
	                                    </div>
	                                </div> 
	                                <div id="showcompany" class="table-responsive" style="overflow: hidden;margin-top:10px;" tabindex="2">
                                	<input type="hidden" id="materailstate" value="0" /> 
                                	 <%@ include file="/wdit/wdit/request/requestCompanyShow.jsp"%>
                               		<table id="approvalstep">
                               			<tr>
                               				<td class="text-right" style="font-size:14px">现场终审结果：</td>
                               				<td class="text-left" colspan="3">
                               					<select id="ispass" onchange="passstate()" style="width:153px">
                               					</select>
                               				</td>
	                               			<td colspan="3" >
	                               				<input type="button" id="affirm" onclick="materailpass()" class="btn btn-info margin-right-10" value="材料通过" id="bt_sure">&nbsp;&nbsp;
	                   							<input type="button" id="passapproval" onclick="passapproval(1)" class="btn btn-default" value="现场审核通过" id="bt_pass">&nbsp;&nbsp;
	                   							<input type="button" id="nopass" onclick="passapproval(0)" class="btn btn-default" value="现场审核不通过" id="bt_not">&nbsp;&nbsp;
	                   							<input type="hidden" id="basis" />
	                               			</td>
                               			</tr>
                               		</table>
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