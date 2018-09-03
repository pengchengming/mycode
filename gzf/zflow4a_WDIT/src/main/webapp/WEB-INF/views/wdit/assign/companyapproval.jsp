<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>委办——资质审核</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestCompanyShow.js" />"></script>


<script type="text/javascript">
var selstatus;
var createTable = new createTable();
var step=3;
var roles="${sessionScope.ROLES}";
var id='${requreid}';
var type='${type}';
var showdiv = ${showdiv};

$(function(){
	
	initPage();
	if(showdiv == 1){  //查看
		$("#isshowdiv").show();
		$("#select_basic").append(getDataDictionaryValueHtml(90));
		$("#select_status").append(getDataDictionaryValueHtml(100));
		$("#select_status").change(function(){
			if(selstatus != $("#select_status").val()){
				$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);
			}else{
				$("#bt_sure").attr("class","btn btn-default").attr("disabled", true);
			}
		})
	}
	
});  



function getDataDictionaryValueHtml(code){
	   var   dictionaryValueHtml="";
	   $.ajax({
		    url : rootPath+"/dictionarys/dataDictionaryValueList.do",
			type : "POST",
			async:false,
			data:{
				code :code
			},
			complete : function(xhr, textStatus){
				var dictionaryCodeData = eval("("+xhr.responseText+")");
				if(dictionaryCodeData &&dictionaryCodeData .length>0){
					$.each(dictionaryCodeData,function(i,n){ 
						if(code+""=="90"){
							if(roles.indexOf("ASSIGN_ROLE") == 0){ //商委
								if(n.id>910 && n.id<920){
									dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
								}
							}else if(roles.indexOf("SCIASSIGN_ROLE") == 0){  //科委
								if(n.id>920 && n.id<930){
									dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
								}
							}else if(roles.indexOf("BUSASSIGN_ROLE") == 0){  //经委
								if(n.id>900 && n.id<910){
									dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
								}
							}
						}else{
							dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
						}
	        		});
			   }
			}});
	   return dictionaryValueHtml;
}



//材料确认
function sureCompanyFun(){	
	$("#bt_sure").attr("class","btn btn-default").attr("disabled", true);
	selstatus = $("#select_status").val();
}

function approveCompanyFun(status){
	
	if($("#bt_sure").attr("class")=="btn btn-info margin-right-10"){
		alert("清先材料确认");
		return false;
	}
	selstatus = $("#select_status").val();
	if(selstatus == 1001 && status == 303){
		alert("当前预审结果通过，请点击预审通过！")
		return false;
	}
	if(selstatus != 1001 && status == 302){
		alert("当前预审结果不通过，请点击预审不通过！")
		return false;
	}
	
	$.ajax({
		url : '<c:url value="/data/procedure"/>',
		type : "POST", 
	    async: false,
	    data : {
	    	p : 'R2014006|'+id + '|31'
	    }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data&&data.length>0){
				var obj = data[0];
				if(obj.checkcode && obj.checkcode == 31){
					var index = layer.load(2);
					var jsons = new Array();
					$("#bt_div").css('display' ,'none');
					var approvstatus;
					if(status==303)  //不通过
						approvstatus = 3103;//不通过
					else if(status==302)  //通过
						approvstatus = 3102;//已通过
					
					var requestjson={
							"formId":61,
							"tableDataId":id,
							"register":{"status":status}
						};
					jsons.push(JSON.stringify(requestjson));	
					var selectBasic = $("#select_basic").val();
					
					var approval_Date = obj.currenttime;
					var approvaljson ={
							"formId":66,
							"register":{"approvalBasis":selectBasic,"status":approvstatus,"approvalStep":3,"companyId":$("#companyId").val(),"approval_Date":approval_Date,"request_Id":id}
					}
					jsons.push(JSON.stringify(approvaljson));
					
					if(status == 303){
						$.ajax({
							url : rootPath+'/forms/getDataByFormId',
							type : "POST",
					        async: false,
							data:{
								formCode:"wdit_company_request_user",
								condition: " status in(2202,2204,4204) and  request_id="+id
							},
							complete : function(xhr, textStatus){
								var data = JSON.parse(xhr.responseText);
								var results = data.results;
								if(results&&results.length>0){
									$.each(results,function(i,obj){ 
										var Date=obj.nowDate;
										var userrequsetobj=new Object();
										userrequsetobj["formId"]= 62;
										userrequsetobj["tableDataId"] = obj.id;
										userrequsetobj["register"] = {"status":3203};
										jsons.push(JSON.stringify(userrequsetobj));
										var adduserapprovalobj=new Object();
										adduserapprovalobj["formId"]= 67;
										adduserapprovalobj["register"] = 
											{"status":3203,"approvalStep":3,
											"requestUser_id":obj.id,"approval_Date":Date};
										jsons.push(JSON.stringify(adduserapprovalobj));
									});
								}}});
						  var linkman = $("#linkman").html();
			              var email = $("#email").html();
						  if(linkman && email && linkman != "" && email != ""){
							 var emailobj = new Object();
			            	 var content = linkman + " 您好：贵公司提交的公租房申请表在  委办资质审核结果  的审批中被审批拒绝。";
			            	 emailobj["formId"]= 99;
			            	 emailobj["register"] = {"email":email,"content":content,"type":1,"flag":0};// 1为发给公司
			            	 jsons.push(JSON.stringify(emailobj));
						}	
					}
					 
				 $.ajax({
					url : rootPath+'/forms/saveFormDataJsons',
					type : "POST", 
					async: false,
					data : {
						jsons : jsons
					}, complete : function(xhr, textStatus){
						var data = JSON.parse(xhr.responseText);
							if(data.code+""=="1"){
// 								if(status == 303){
// 									updateUserUnpass(obj.currenttime);
// 								}
								alert("审核成功");
								layer.close(index);
//				 				window.location.href('<c:url value="/assign/approvelist"/>');
								window.location.href=rootPath+"/assign/approvelist";
							}else {
								alert(data.errorMsg);
							}	
					}});
				}
				else{
					alert("无需重复审批");		
				}
			}
	    }
	})
}

// function updateUserUnpass(Date){
// 	$.ajax({
// 		url : rootPath+'/forms/getDataByFormId',
// 		type : "POST",
//         async: false,
// 		data:{
// 			formCode:"wdit_company_request_user",
// 			condition: " status in(2202,2204,4204) and  request_id="+id
// 		},
// 		complete : function(xhr, textStatus){
// 			var data = JSON.parse(xhr.responseText);
// 			var results = data.results;
// 			if(results&&results.length>0){
// 				var jsons = new Array();
// 				$.each(results,function(i,obj){ 
// 					var userrequsetobj=new Object();
// 					userrequsetobj["formId"]= 62;
// 					userrequsetobj["tableDataId"] = obj.id;
// 					userrequsetobj["register"] = {"status":3203};
// 					jsons.push(JSON.stringify(userrequsetobj));
// 					var adduserapprovalobj=new Object();
// 					adduserapprovalobj["formId"]= 67;
// 					adduserapprovalobj["register"] = 
// 						{"status":3203,"approvalStep":3,
// 						"requestUser_id":obj.id,"approval_Date":Date};
// 					jsons.push(JSON.stringify(adduserapprovalobj));
// 				});
// 				if(jsons&&jsons.length>1){
// 					$.ajax({
// 						url : '<c:url value="/forms/saveFormDataJsons"/>',
// 						type : "POST",
// 						async: false,
// 						data : {
// 							jsons : jsons  
// 						}, complete : function(xhr, textStatus){
// 							var data = JSON.parse(xhr.responseText);
// 								if(data.code+""=="1"){
// 							 	}else {
// 									alert(data.errorMsg);
// 							}	
// 						}});
// 				}else{
// 					alert("jsons太短");
// 				}
// 			}			
// 		}
// 	}); 
// }

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
				adddataobj["register"] = {"status":31041,"returnRemark":returnRemark,"isReturn":1,"approval_Date":requestinfo.nowDate,"approvalStep":3,"request_Id":id,"companyId":requestinfo.companyId};
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
									window.location.href=rootPath+"/assign/approvelist";
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
                                <li>委办/资质审核</li>
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
                                    <div class="title padding-20">
                                         <ul class="nav nav-tabs" role="tablist">
                                            <li role="presentation" class="active"><a class="f18" href="#tab-1" aria-controls="tab-1" role="tab" data-toggle="tab">公司信息</a></li>
                                            <li role="presentation"><a class="f18" href="#tab-2" aria-controls="tab-2" role="tab" data-toggle="tab">员工</a></li>
                                        </ul>
									 </div>
									 <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane active" id="tab-1">
                                            <div class="table-responsive">
                                                <input type="hidden" id="companyId" />
                                                <%@ include file="/wdit/wdit/request/requestCompanyShow.jsp"%>
                                            </div>
                                            <div id="isshowdiv" style="display: none;">
                                            <div class="row">
                                               <div class="col-sm-5">
                                                    <div class="col-sm-5 text-right padding-5">单位资格审核依据:</div>
                                                    <div class="col-sm-7">
                                                        <select class="small-select2 form-control" style="height: 40px; width: 500px;" id = "select_basic">
                                                        </select>
                                                    </div>
                                                </div>
                                            </div><br>
									        <div class="row">
                                                <div class="col-sm-5">
                                                    <div class="col-sm-5 text-right padding-5">单位资格审核结果：</div>
                                                    <div class="col-sm-7">
                                                        <select class="small-select2 form-control" style="height: 40px;" id = "select_status">
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-sm-7" id="bt_div">
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="sureCompanyFun()" id="bt_sure">材料确认</a>
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="approveCompanyFun(302)">预审通过</a>
                                                    <a class="btn btn-default" href="javascript:void(0)" onclick="approveCompanyFun(303)">预审不通过</a>
                                                    <a class="btn btn-default" href="javascript:void(0)" onclick="openlayer()">退回申请</a>
                                                </div>
                                            </div>
                                            </div>
                                        </div>
									     <div role="tabpanel" class="tab-pane" id="tab-2">
									         <h4>人员信息列表</h4>
                                             <div class="table-responsive" id="form_table"></div>
                                        </div>
                                    </div>
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
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>

</body>
</html>