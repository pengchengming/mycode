<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>审核人员详情</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestUserShow.js" />"></script>

<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var requestUserId='${requestUserId}';
var tableController;
var step=4;

$(function(){
	initPage(); 
	getDataDictionaryValueHtml();
	getDataDictionaryValueHtml1();
	getcompanystatus();
	$("#basis").val($("#accord").val());
});

//判断企业是否审批过，修改button按钮单击事件
function getcompanystatus(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company_request",
			condition: " id="+$("#request_id").val()
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				if(obj.status!=302){
					$("#affirm").attr("onclick","noclick()");
					$("#passapproval").attr("onclick","noclick()");
					$("#nopass").attr("onclick","noclick()");
					$("#reback").attr("onclick","noclick()");
				}else{
				}
			}			
		}
	});
}
function noclick(){
	alert("还未审批公司信息！！！");
	window.location.reload();
}
function getDataDictionaryValueHtml(){
	var   dictionaryValueHtml="";
	   $.ajax({
		    url : rootPath+"/dictionarys/dataDictionaryValueList.do",
			type : "POST",
			async:false,
			data:{
				code :110
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
	  $("#basis").append(dictionaryValueHtml);
}

function getDataDictionaryValueHtml1(){
	   var   dictionaryValueHtml="";
	   $.ajax({
		    url : rootPath+"/dictionarys/dataDictionaryValueList.do",
			type : "POST",
			async:false,
			data:{
				code :120
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

//审批人员
function ispassapproval(state){
	var ispass=$("#ispass").val();
	var materailstate=$("#materailstate").val();
	var basis=$("#basis").val();
	if(!basis){
		alert("请选择审批依据");
		return false;
	}
	
	//获取当前登录人角色
	 var isapproval=false;
	 $.ajax({
			url : rootPath+'/bureauhuman/getuser',
			type : "POST",
	        async: false,
	        data:{
	        	level:4
	        },
			complete : function(xhr, textStatus){
				var data=xhr.responseText
				if(data=="ok"){
				}else {
					alert("当前登录人没有审批权限");
					isapproval=true;
					return false;
				}
			}
	});
	  
	 $.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"wdit_company_request_user",
				condition: " id="+requestUserId
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;
				if(results&&results.length>0){
					var obj=results[0];
					$("#nowdate").val(obj.nowDate);
					var status=obj.status;
					if(status>=4202 && status!=4204){
						isapproval=true;
						alert("此级已审批，请刷新页面");
						return false;
					}
				}			
			}
		});
	if(isapproval){
		return false;
	}
	
	
	var str=$("#nowdate").val();
	
	var array=new Array();
	var obj=new Object();
	var jsonString,jsonString2;
	var jsons = new Array();
	if(materailstate==1){
		if(state==1){
			if(ispass==1201){
				//alert("审核通过");
			}else{
				alert("请选择正确个人资料审核结果");
				return false;
			}
			obj.approvalStep=4;
			obj.status=4202;
			obj.approvalBasis=$("#basis").val();
			obj.approval_Date=str;
			obj.requestUser_id=requestUserId;
			array.push(obj);
			jsonString={
					"formId":62,
					"tableDataId":requestUserId,
					"register":{status:4202}
			}
			jsonString2={
					"formId":67,
					"register":obj
			}
			
		}else{
			if(ispass==1202){
				//alert("审核不通过");
			}else{
				alert("请选择正确个人资料审核结果");
				return false;
			}
			obj.approvalStep=4;
			obj.status=4203;
			obj.approvalBasis=$("#basis").val();
			obj.approval_Date=str;
			obj.requestUser_id=requestUserId;
			array.push(obj);
			jsonString={
					"formId":62,
					"tableDataId":requestUserId,
					"register":{status:4203}
			}
			jsonString2={
					"formId":67,
					"register":obj
			}
			 var username = $("#userName").html();
			var linkman = $("#linkman").val();
			var email = $("#email").val();
			if(linkman && email && username && linkman != "" && email != "" && username != ""){
			 var emailobj = new Object();
           	 var content = linkman + " 您好：贵公司提交的公租房申请表中 "+username+" 在 人设局  的审批中被审批拒绝。";
           	 emailobj["formId"]= 99;
           	 emailobj["register"] = {"email":email,"content":content,"type":1,"flag":0};// 1为发给公司
           	 jsons.push(JSON.stringify(emailobj));
			}	
		}
		jsons.push(JSON.stringify(jsonString));
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
					getcompanyapproval();
					window.opener.openeRequestUserList();
					window.opener.isshowbtreturn();
					window.close();
				}else{
					alert(data.successMsg);
					layer.close(index);
					return false;
				}
			    }
	        });
	}else{
		alert("确认材料，请点击材料确认");
		return false;
	}
	
}

//获取企业，更改企业状态
function getcompanyapproval(){
	$.post(rootPath+'/createSelect/findselectData', { 
		code : "rsjapproval_v1",
		selectConditionSql : " where  t1.id="+$("#request_id").val()
	}, function(data) {
		if (data.code + "" == "1") {
			var results = eval("(" + data.results + ")");
			if(results &&results.length>0){
				var company=results[0];
				var approvalnum=company.approvalnum;
				var id=company.id;
				var passnum=company.passnum;
			}
			if(approvalnum==0){
				if(passnum==0){
					var jsonString={
							"formId":61,
							"tableDataId":id,
							"register":{status:403,approvalNum:approvalnum}
					}
				}else{
					var jsonString={
							"formId":61,
							"tableDataId":id,
							"register":{status:402,approvalNum:approvalnum}
					}
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
							alert(data.successMsg);
						}else{
							alert(data.successMsg);
							layer.close(index);
							return false;
						}
					    }
			        });
			}
			
		}
	});
	
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


function reback(){
	
}
</script>

</head>

<body class="horizontal-menu-fixed">

    <div class="main-wrapper">
		<%-- <%@ include file="/WEB-INF/views/wdit/head.jsp"%>  --%>
		<%-- <%@ include file="/WEB-INF/views/wdit/leftMenu.jsp"%> --%>
        <!-- start: MAIN CONTAINER -->
        <div class="panel-body no-padding-top">
            <!-- start: PAGE -->
            <div class="main-content" style="min-height: 542px;">
                <div class="container">
                    <!-- start: BREADCRUMB -->
                    <%-- <div class="row hidden-sm hidden-xs">
                        <div class="col-md-12">
                            <ol class="breadcrumb">
                                <li>人社局</li>
                                <li class="active"><a  href="<c:url value="/bureauhuman/rsjApprovalList" />">资质审核</a></li>
                                <li class="active">审核人员详情</li>
                            </ol>
                        </div>
                    </div> --%>
                    <!-- end: BREADCRUMB -->
                    <!-- start: PAGE CONTENT -->
                    <div class="row">
                        <div class="col-sm-12">
                            <!-- start: DATE/TIME PICKER PANEL -->
                            <div class="panel panel-white">
                                <div class="panel-body no-padding-top">
                                	<input type="hidden" id="request_id" />
                                	<input type="hidden" id="companyId" />
                                	<input type="hidden" id="materailstate" value="0" />
                                	     
                                		<%@ include file="/wdit/wdit/request/requestUserShow.jsp"%>
                                		<input type="hidden" id="linkman" /><input type="hidden" id="email" />
                                		<div id="approval_div">
                                			<input type="hidden" id="nowdate">
                                			<input type="hidden" id='accord' />
                                			个人资料审核依据： <select id="basis"></select>
                                			<div style="margin-top:5px;">
	                                    	个人资料审核结果：<select id="ispass" onchange="passstate()" style="width:153px"></select>
	                                    	<input type="button" id="affirm" onclick="materailpass()" class="btn btn-info margin-right-10" value="材料通过">&nbsp;&nbsp;
	                                    	<input id="passapproval" type="button" onclick="ispassapproval(1)" value="审核通过" class="btn btn-default" readonly="readonly" />&nbsp;&nbsp;
	                                    	<input id="nopass" type="button" value="审核不通过" onclick="ispassapproval(0)" class="btn btn-default" />&nbsp;&nbsp;
	                                    	<!-- <input type="button" value="退回申请" onclick="reback()" class="btn btn-info margin-right-10" /> -->
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