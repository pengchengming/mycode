<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>审核员工详情</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestUserShow.js" />"></script>

<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var requestUserId='${requestUserId}';
var tableController;
var step=7;

$(function(){
	initPage();
	getDataDictionaryValueHtml1();
	getDataDictionaryValueHtml();
	getcompanystatus();//查看企业状态
	approvalstep();
});

function approvalstep(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company_request_user",
			condition: " id = " + requestUserId
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				if(obj.status+"" != "2202" && obj.status+"" != "5204" ){
					$("#approvalstep").hide();
				}
			}		
		}
	});
}

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
				if(obj.status==503){
					$("#approvalstep").hide();
				}
				if(obj.status!=504 && obj.status!=505){
					$("#affirm").attr("onclick","noclick()");
					$("#passapproval").attr("onclick","noclick()");
					$("#nopass").attr("onclick","noclick()");
					$("#reback").attr("onclick","noclick()");
				}
			}			
		}
	});
}

function noclick(){
	alert("请先审批公司信息！！！");
	window.opener.reload();
	window.close();
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
function ispassapproval(status){
	var Date;
	var flag=false;
	//获取审核人判断
	 $.ajax({
			url : rootPath+'/bureauhuman/getuser',
			type : "POST",
	        async: false,
	        data:{
	        	level:5
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
			formCode:"wdit_company_request_user",
			condition: " id="+requestUserId
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				var status=obj.status;
				Date = obj.nowDate;
				if(status&&status.length>0){
					if(status+""!="2202" && status+""!="5204"){
						flag = true;
						alert("当前申请已审核过！");
					}
					
				}
			}			
		}
	}); 
	if(flag){
		window.location.reload();
		return false;
	}
	var ispass=$("#ispass").val();//下拉选择
	if($("#affirm").attr("class")=="btn btn-info margin-right-10"){
		alert("清先材料确认");
		return false;
	}
	if(ispass == 1201){
		if(status == 5203){
			alert("当前预审结果通过，请点击预审通过！");
			return false;
		}
	}else if(ispass == 1202){
		if( status == 5202){
			alert("当前预审结果不通过，请点击预审不通过！");
			return false;
		}
	}
	var jsons = new Array();
	 var requestUserObj = {
			"formId":62,
			"tableDataId":requestUserId,
			"register":{"status":status}
	};
	 jsons.push(JSON.stringify(requestUserObj));
	 var approvalUserObj = {
				"formId":67,
				"register":{"status":status,"approvalStep":5,
					        "requestUser_id":requestUserId,"approval_Date":Date}
			};
	 jsons.push(JSON.stringify(approvalUserObj));
	 
	    var username = $("#userName").html();
		var linkman = $("#linkman").val();
		var email = $("#email").val();
		if(linkman && email && username && linkman != "" && email != "" && username != "" && status && (status == 5202 || status == 5203)){
		 var emailobj = new Object();
    	 emailobj["formId"]= 99;
    	 var content = linkman ;
    	 if(status == 5203){
    		 content += " 您好：贵公司提交的公租房申请表中 "+username+" 在 人才办资质审核  的审批中被审批拒绝。"; 
    	 }else if(status == 5202){
    		 content += " 您好：贵公司提交的公租房申请表中"+username+" 已通过终审";}
    	 emailobj["register"] = {"email":email,"content":content,"type":1,"flag":0};// 1为发给公司
    	 jsons.push(JSON.stringify(emailobj));
		}	
	 
	 
	 var index = layer.load(2);
	 $.ajax({
			url : rootPath+'/forms/saveFormDataJsons',
			type : "POST", 
			async: false,
			data : {
				jsons : jsons,
				requestUserCall : 'U2051001|'+requestUserId
			}, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
					if(data.code+""=="1"){
						checkstatus();
						alert("审核成功");
						window.opener.openeRequestUserList();
						window.close();
					}else {
						layer.close(index);
						alert(data.errorMsg);
					}	
			}});	 
}

function checkstatus(){
	$.ajax({
		url : '<c:url value="/data/procedure"/>',
		type : "POST", 
	    async: false,
	    data : {
	    	p : 'U2014005|'+ requestUserId 
	    }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
	   }
	})
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

function onclick(){
	
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
                                <li>人才办</li>
                                <li class="active"><a href="<c:url value="/talentoffice/rcbApprovalList" />">绿色通道审核</a></li>
                                <li class="active">审核员工详情</li>
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
                                	<input type="hidden" id="materailstate" value="0" /> 
                                	<input type="hidden" id="request_id" />
                                	<input type="hidden" id="basis" />
                                	<%@ include file="/wdit/wdit/request/requestUserShow.jsp"%>
                                	<input type="hidden" id="linkman" /><input type="hidden" id="email" />
                                 	<div id="approvalstep" class="table-responsive" style="overflow: hidden;margin-top:10px;">
                           			个人资料审核结果：<select id="ispass" onchange="passstate()" style="width:153px"></select>
                           			<input type="button" id="affirm" onclick="materailpass()" class="btn btn-info margin-right-10" value="材料通过">&nbsp;&nbsp;
                           			<input id="passapproval" type="button" onclick="ispassapproval(5202)" value="审核通过" class="btn btn-default" />&nbsp;&nbsp;
                           			<input id="nopass" type="button" value="审核不通过" onclick="ispassapproval(5203)" class="btn btn-default" />&nbsp;&nbsp;
                           			<!-- <input id="reback" type="button" value="退回申请" class="btn btn-info margin-right-10" /> -->
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