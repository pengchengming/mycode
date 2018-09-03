<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>人才办-绿色通道审核</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestCompanyShow.js" />"></script>

 

<script type="text/javascript">
var id='${id}';
var type='${type}';
var createTable = new createTable();
var step=7;

$(function(){
	initPage();
	getDataDictionaryValueHtml2();
	approvalstep();
})

function openeRequestUserList(){
	getRequestUserList(id,step); //人员列表
}

function reload(){//刷新当前页面
	window.location.reload();
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

function approvalstep(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company_request",
			condition: " id = " + id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				if(obj.status != 202){
					$("#approvalstep").hide();
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
function passapproval(judge){
	var flag = false;
	var Date;
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
					if(status+""!="202" && status+""!="505"){
						flag = true;
						alert("当前申请已审批过");
					}
					
				}
			}			
		}
	}); 
	if(flag){
		window.location.reload();
		return false;
	}
	var state = $("#passapproval").val();
	var ispass=$("#ispass").val();
	var nopass=$("#nopass").val();
	var materailstate=$("#materailstate").val();
	var jsons = new Array();
	var obj=new Object();
	if(materailstate==1){
		if(judge==1){//审批通过
			if(ispass==1201){
				alert("审核通过");
			}else{
				alert("请选择正确终审结果");
				return false;
			}
			obj.approvalStep=5;
			obj.status=5102;
			obj.approvalBasis=$("#basis").val();
			obj.approval_Date=Date;
			obj.request_Id=id;
			obj.companyId=$("#companyId").val();
			
			var jsonString={
					"formId":61,
					"tableDataId":id,
					"register":{status:504}
			}
			var jsonString2={
					"formId":66,
					"register":obj
			}
			jsons.push(JSON.stringify(jsonString));
			jsons.push(JSON.stringify(jsonString2));
		}else if(judge==0){//不通过
			if(ispass==1202){
			}else{
				alert("请选择正确终审结果");
				return false;
			}
			obj.approvalStep=5;
			obj.status=5103;
			obj.approvalBasis=$("#basis").val();
			obj.approval_Date=Date;
			obj.request_Id=id;
			obj.companyId=$("#companyId").val();
			var jsonString={
					"formId":61,
					"tableDataId":id,
					"register":{status:503}
			}
			var jsonString2={
					"formId":66,
					"register":obj
			}
			jsons.push(JSON.stringify(jsonString));
			jsons.push(JSON.stringify(jsonString2));
			$.ajax({
				url : rootPath+'/forms/getDataByFormId',
				type : "POST",
		        async: false,
				data:{
					formCode:"wdit_company_request_user",
					condition: " status in(2202,5204) and  request_id="+id
				},
				complete : function(xhr, textStatus){
					var data = JSON.parse(xhr.responseText);
					var results = data.results;
					if(results&&results.length>0){
						$.each(results,function(i,obj){ 
							var userrequsetobj=new Object();
							userrequsetobj["formId"]= 62;
							userrequsetobj["tableDataId"] = obj.id;
							userrequsetobj["register"] = {"status":5203};
							jsons.push(JSON.stringify(userrequsetobj));
							var adduserapprovalobj=new Object();
							adduserapprovalobj["formId"]= 67;
							adduserapprovalobj["register"] = 
								{"status":5203,"approvalStep":5,
								"requestUser_id":obj.id,"approval_Date":Date};
							jsons.push(JSON.stringify(adduserapprovalobj));
						});
					}			
				}
			});
			//往邮件表插一条记录
			var linkman = $("#linkman").html();
            var email = $("#email").html();	
			if(linkman&&email && linkman != "" && email != ""){
				 var emailobj = new Object();
	          	 var content = linkman + " 您好：贵公司提交的公租房申请表在  人才办资质审核 的审批中被审批拒绝。";
	          	 emailobj["formId"]= 99;
	          	 emailobj["register"] = {"email":email,"content":content,"type":1,"flag":0};// 1为发给公司
	          	 jsons.push(JSON.stringify(emailobj));
			}	
			
			
			
		}
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
	}else{
		alert("请先材料确认！");
		return false;
	}
	
}

function updateUserUnpass(Date){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company_request_user",
			condition: " status=2202 and  request_id="+id
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
					userrequsetobj["register"] = {"status":5203};
					jsons.push(JSON.stringify(userrequsetobj));
					var adduserapprovalobj=new Object();
					adduserapprovalobj["formId"]= 67;
					adduserapprovalobj["register"] = 
						{"status":5203,"approvalStep":5,
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


//退回
function openlayer(){
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
				formCode:"WDIT_Company_Request_User",
				currentField:"request_id",
				parentId:"id",
				aliasesName:"requestUsers",
				fieldName:"id",
				fromConfig:[{
					formCode:"WDIT_Company_User_Approval",
					currentField:"requestUser_id",
					parentId:"id",
					aliasesName:"userApprovalIds",
					fieldName:"id,approvalStep,status"	
				}]
			},{
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
				comrequestobj["register"] = {"status":205,"isReturn":1};
				jsons.push(JSON.stringify(comrequestobj));
				var requestinfo = data.results[0];
				var adddataobj = new Object();
				
				adddataobj["formId"]= 66;
				adddataobj["register"] = {"status":51042,"returnRemark":returnRemark,"isReturn":1,"approval_Date":requestinfo.nowDate,"approvalStep":5,"request_Id":id,"companyId":requestinfo.companyId};
				jsons.push(JSON.stringify(adddataobj));
				
				var companyapproves = requestinfo.companyApprovalIds;//公司审核数组
				$.each(companyapproves,function(i,companyapprove){
					 if(companyapprove.approvalStep + ""== "2" && companyapprove.status + ""== "2102" ){
						 var comapprovrobj = new Object();
						     comapprovrobj["formId"]= 66;
						     comapprovrobj["tableDataId"] = companyapprove.id;
						     comapprovrobj["register"] = {"status":2104};
							 jsons.push(JSON.stringify(comapprovrobj));
					 }
				})
				var requestUsers =  requestinfo.requestUsers;
				if(requestUsers&&requestUsers.length > 0){
					$.each(requestUsers,function(i,requestUser){
						var jsonobj = new Object();
						jsonobj["formId"]= 62;
						jsonobj["tableDataId"] = requestUser.id;
						jsonobj["register"] = {"status":2204};
						jsons.push(JSON.stringify(jsonobj));
						var userApprovalIds = requestUser.userApprovalIds;
						if(userApprovalIds&&userApprovalIds.length>0){
							$.each(userApprovalIds,function(i,userApprovalId){
								if(userApprovalId.approvalStep + "" == "2" && (userApprovalId.status + ""== "2202" ||userApprovalId.status + ""== "2203" )){
									var obj = new Object();
									obj["formId"]= 67;
									obj["tableDataId"] = userApprovalId.id;
									obj["register"] = {"status":2204};
									jsons.push(JSON.stringify(obj));
								}
							})
						}
					})
				}
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
									window.location.href=rootPath+"/talentoffice/rcbQuickList";
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
                                <li>人才办</li>
                                <li class="active"><a href="<c:url value="/talentoffice/rcbQuickList" />">绿色通道审核</a></li>
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
	                                    <div class="col-sm-12">
	                                    			<input type="hidden" id="companyId" />
	                                                <input type="hidden" id="basis" />
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
                                	<input type="hidden" id="materailstate" value="0" />                       		
                               		<%@ include file="/wdit/wdit/request/requestCompanyShow.jsp"%>
                               		<table id="approvalstep">
                               			<tr><td class="text-right">绿色通道审核结果:</td>
                               			<td class="text-left" colspan="3"><select id="ispass" onchange="passstate()" style="width:153px"></select></td>
                               			<td colspan="3">
                               				<input type="button" id="affirm" onclick="materailpass()" class="btn btn-info margin-right-10" value="材料通过">&nbsp;&nbsp;
                   							<input type="button" id="passapproval" onclick="passapproval(1)" class="btn btn-default" value="审核通过">&nbsp;&nbsp;
                   							<input type="button" id="nopass" onclick="passapproval(0)" class="btn btn-default" value="审核不通过">&nbsp;&nbsp;
                   							<input type="button"  class="btn btn-info margin-right-10" value="退回申请" onclick="openlayer()">&nbsp;&nbsp;
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