<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>申请人员列表</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/script/jquery/jquery.form.js" />"></script>
 

<script type="text/javascript">
	var requestId='${requestId}';
	var companyId = '${sessionScope.USER.companyId}';
	var createTable = new createTable();	
	var pickDwelling;
	function submitImageOnChange(id){
		 var fileName = $("#authorization_id").val();
		 if (fileName != null && fileName.length > 0) {
		     var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase();
		     if (fileType == "BMP" || fileType== "WBMP" ||fileType == "JPEG"|| fileType== "PNG"|| 
		     		fileType== "TIF" || fileType== "GIF"||fileType== "PSD"|| fileType== "ICO"||fileType== "JPG"||
		     		fileType== "PCX"|| fileType== "TGA"||fileType== "JP2"|| fileType== "PDF") {
		    	 $("#authorization_form_id").submit();
		     } else {
		         alert("Please Upload BMP, WBMP, JPEG, PNG, TIF, GIF, PSD, ICO, PCX, TGA, JP2, PDF！");
		         $("#largeAdvertImage").val("");
		     }
		 }
		 
	}
	function uploadForm(){
		 $("#authorization_form_id").ajaxForm({
				dataType : 'text',
				success : function(json) {
					if (json.indexOf("pre") > 0|| json.indexOf("PRE") > 0) {
						json = json.substring(5);
						json = json.substring(0, json.length - 6);
					}
					var data = JSON.parse(json);
					if (data.code + "" == "1") {
						var str="<div><input type='hidden' value='"+data.imageName+"' /><input type='hidden' value='"+data.imagePath+"'/><input type='hidden' value='"+data.smallImagePath+"'/><a onClick='showPhoto(\""+rootPath+data.imagePath+"\")'><img src='"+rootPath+data.smallImagePath+"'></a>&nbsp;<a href='javascript:void(0)' onclick='deletePhoto(this);'>删除</a></div>";
						 $("#authorization_form_id").before(str);
					} else {
						alert("上传失败");
					}
				}
			});
	}
	function showPhoto(photo){
		var photoHtml='<div><img src="'+photo+'" height="500" alt=""></div>';
		layerindex= layer.open({
	        type: 1,
	        title:"原图",
	        area: ['820px', '670px'],
	        skin: 'layui-layer-rim', //加上边框
	        content: photoHtml
	    });
	}
	
	function deletePhoto(me){
		$(me).parent().remove();
	}
	$(function(){
		uploadForm(); //上次照片
		getUploadFile();
		userListFun();
		isUserAddFun();//
	});
	function isUserAddFun(){
		var  byIdconfig2={
				fromConfig:[{
					formCode:"WDIT_Company_Request_User",
					currentField:"request_id",
					parentId:"id",
					aliasesName:"requestUsers",
					fieldName:"id,username" 
				} ]
		}
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	     async: false,
			data:{
				formCode:"wdit_company_request",
				condition: "id="+requestId,
				byIdconfig:JSON.stringify(byIdconfig2)
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;  
				if(results&&results.length>0){
					var requestCompany=results[0];
					var applicationNum=requestCompany.applicationNum;//编号
					pickDwelling = requestCompany.pickDwelling;
					$("#applicationNum_id").html(applicationNum);
					if(!applicationNum||applicationNum+""!="0"){
						applicationNum=parseInt(applicationNum);
						if(requestCompany.requestUsers){
							if(requestCompany.requestUsers.length>=applicationNum){
								$("#requestUserAdd_id").hide();
							}
						} 
					}else{
						$("#requestUserAdd_id").hide();
					}
				}
			}
		});
		
	}
	
	
	function userListFun(){
		var tableConfig=userListConfig();//tableConfig 
		var  byIdconfig2={
				fromConfig:[{
					formCode:"WDIT_Company_Request_User_relative",
					currentField:"requestUser_id",
					parentId:"id",
					aliasesName:"userRelative",
					fieldName:"name,relative",
					fromConfig:[{
						formCode:"sys_datadictionary_value",
						currentField:"id",
						parentId:"relative",
						aliasesName:"relatives",
						fieldName:"displayValue"	
					}]
				},{
					formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"housingConditionsInTheCity",
					aliasesName:"housingConditionsInTheCity",
					fieldName:"displayValue"	
				}]
		}
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"WDIT_Company_Request_User",
				condition: "request_id="+requestId,
				byIdconfig:JSON.stringify(byIdconfig2)
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.code + "" == "1"){
					createTable.registTable($('#form_table'), tableConfig, data, "queryClick"); 
				}else{
					$("#form_table").html("<div style='text-align: center'>没有记录</div>");
				}
			}
		}); 
	}
	
	function userListConfig(){
		var tableConfig = {
				fields : [{
					title:"序号",
					type:"line"
				},{
					title : '申请人姓名',
					data : 'userName'
				}, {
					title : '身份证号码',
					data : 'identityCardNumber'
				}, {
					title : '联系电话',
					data : 'applicantPhone'
				}, {
					title : '共同申请人',
					data : '',
					type : 'cutsomerRender',
					doRender : function(data, container,config, rowIndex){
						var userRelatives=data.userRelative;
						var relativeHtml="";
						$.each(userRelatives,function(i,userRelative){
							var relative="";
							var displayValues=userRelative.relatives[0];
							if(displayValues){
								relative=displayValues.displayValue
							}
							relativeHtml+=relative+":"+userRelative.name+";";
						});
						$(container).append(relativeHtml);
					}
				},{
					title : '本市户籍住房信息',
					data : '',
					type : 'cutsomerRender',
					doRender : function(data, container,config, rowIndex){
						var housingConditionsInTheCity=data.housingConditionsInTheCity[0];
						if(housingConditionsInTheCity)
							$(container).append(housingConditionsInTheCity.displayValue);
					}
				},{
					title : '操作',
					data : '',
					type : 'cutsomerRender',
					doRender : function(data, container,config, rowIndex){
						var updateHtml="<a   href='"+rootPath+"/companyRequest/requestUserUpdate?requestId="+requestId+"&requestUserId="+data.id+"'>更改</a> ";
						$(container).append(updateHtml);
						var deleteHtml='<a href="javascript:void(0)"  onclick="deleteRequestUser(\''+data.id+'\')">删除</a> ';
						$(container).append(deleteHtml);
					}
				},{
					title :"",
					type:"none",
					data:"applyForFamily"
				}]
			}; 
		return tableConfig;
	}

	function deleteRequestUser(id){
		var deleteJson={
				"formId":62,
				"tableDataId":id,
				"subStructure":[{
					"formId":63,
		            "parentId":"requestUser_id",
		            "subStructure":[{"formId":65,"parentId":"user_relative_id"}]				
				},{
					"formId":64,
		            "parentId":"requestUser_id",
		            "subStructure":[{"formId":65,"parentId":"user_housing_id"}]				
				},{
					"formId":65,
		            "parentId":"requestUser_id"				
				}]
		}
		$.ajax({
			url : rootPath+'/forms/delteFormDataJson',
			type : "POST", 
	      	async: false,
	      	data : {
	      		json : JSON.stringify(deleteJson)
	      	}, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.code+""=="1"){
					 alert("删除成功")
					 userListFun();
					 $("#requestUserAdd_id").show();
			 	}
			   }
	      });
		
	}
	function requestConfirm(type){
		var authorizationDivs= $("#authorization_div").find("div");
		if(authorizationDivs.length<=0){
			alert("请上传授权书");	
			return  false;
		};
		var issubmit=false;
		 var tds= $("#form_table_createDiv tbody").find("td");
		 for (var i = 0; i < 10; i++) {
			 var applyForFamily=$("#form_table_createDiv tbody tr:eq("+i+") td:eq(7)").html();
			 var applyForName=$("#form_table_createDiv tbody tr:eq("+i+") td:eq(1)").html();
			 if(pickDwelling==1601){
				 if(applyForFamily==503 || applyForFamily==504){
					 var line=i+1;
					 alert("请先检查姓名为"+applyForName+"的申请人户型");
					 return false;
				 }
			 }
		}
		 var applicationNum=$("#applicationNum_id").html();
		 if(!applicationNum){
			 alert("请检查申请人数量");
			 return false;
		 }
		 if(tds.length>1){
// 			 var trs= $("#form_table_createDiv tbody").find("tr");
// 			 if(applicationNum){
// 				 applicationNum=parseInt(applicationNum);
// 				 if(applicationNum!=trs.length){
// 					 if(confirm("您所提交的申请人数不满额度，是否继续提交？")){
// 						 issubmit=true; 
// 					 }		 
// 				 }else {
// 					 issubmit=true;
// 				 }
// 			 }
			 $.ajax({
				url : rootPath+'/createSelect/findselectData',
				type : "POST", 
		      	async: false,
		      	data : {
		      		code : "approvalnum_v1",
					selectConditionSql : " where  a.companyId="+companyId
		      	}, complete : function(xhr, textStatus){
					var data = JSON.parse(xhr.responseText);
					if(data.code+""=="1"){
						var results = eval("(" + data.results + ")");
						if(results &&results.length>0){
							var approval=results[0];
							var approvalnum=parseInt(approval.approvalnum);
							var applicationNum=parseInt(approval.applicationNum);
							if(approvalnum<applicationNum){
								if(type==1){
									if(confirm("您所提交的申请人数不满额度，是否继续提交？")){
										 issubmit=true; 
									 }									
								}else{
									if(confirm("是否保存?")){
										 issubmit=true; 
									 }
								}
							}else{
								issubmit=true;
							}
						}
					}
		      	}
				});
// 			 var trs= $("#form_table_createDiv tbody").find("tr");
// 			 if(applicationNum){
// 				 applicationNum=parseInt(applicationNum);
// 				 if(applicationNum!=trs.length){
// 					 if(confirm("您所提交的申请人数不满额度，提交后本次申请不可再补充人员 ，是否继续提交？")){
// 						 issubmit=true; 
// 					 }		 
// 				 }else {
// 					 issubmit=true;
// 				 }
// 			 }
		 }else{
			 alert("请填写申请人信息");
			 return false;
		 }
		 if(issubmit){
				
				var  userPhoto2Array=new Array();
				authorizationDivs.each(function(i,authorization){
					var inputs=$(authorization).find("input");
					var photoName=inputs.eq(0).val();
					 var photo=inputs.eq(1).val();
					 var smallphoto=inputs.eq(2).val();
					 userPhoto2Array.push({"type":7,"name":photoName,"smallPhoto":smallphoto,"photo":photo});
				});
				var requestUserObj={
						"formId":62,
						"tableDataId":requestId, 
						register:{a:1},
						"isSave":0
					};
				var detail=new Array();
				detail.push({"formId":65,"parentId":"request_id","array":userPhoto2Array});
				requestUserObj.detail=detail;
				$.ajax({
					url : rootPath+'/forms/saveFormDataJson',
					type : "POST", 
			      	async: false,
			      	data : {
			      		json : JSON.stringify(requestUserObj)
			      	}, complete : function(xhr, textStatus){
						var data = JSON.parse(xhr.responseText);
						if(data.successMsg){
							if(type==1){
								window.location.href=rootPath+"companyRequest/requestConfirm?requestId="+requestId;
							}else if(type==0){
								window.location.href=rootPath+"companyRequest/requestList";
							}
					 	}
					   }
			      });
		 }
	}
	function getUploadFile(){
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"wdit_company_request_user_photo",
				condition: " request_id="+requestId
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.code + "" == "1"){
					var results = data.results;  
					if(results&&results.length>0){
						$.each(results,function(i,requestPhoto){
							var str="<div><input type='hidden' value='"+requestPhoto.name+"' /><input type='hidden' value='"+requestPhoto.photo+"'/><input type='hidden' value='"+requestPhoto.smallPhoto+"'/><a onClick='showPhoto(\""+rootPath+requestPhoto.photo+"\")'><img src='"+rootPath+requestPhoto.smallPhoto+"'></a>&nbsp;<a href='javascript:void(0)' onclick='deletePhoto(this);'>删除</a></div>";
							 $("#authorization_form_id").before(str);
						}) ;
						 
					}
				}
			}
		}); 
	}

	function previousFun(){
		window.location.href=rootPath+"companyRequest/requestCompany?requestId="+requestId;
	}
	function ischeck(){
		$.ajax({
			url : rootPath+'/createSelect/findselectData',
			type : "POST", 
	      	async: false,
	      	data : {
	      		code : "approvalnum_v1",
				selectConditionSql : " where  a.companyId="+companyId
	      	}, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.code+""=="1"){
					var results = eval("(" + data.results + ")");
					if(results &&results.length>0){
						var approval=results[0];
						var approvalnum=parseInt(approval.approvalnum);
						var applicationNum=parseInt(approval.applicationNum);
						if(approvalnum>=applicationNum){
							alert("申请人数已足够请不要重复申请");
							return false;
						}else{
							window.location.href=rootPath+"/companyRequest/requestUser?requestId="+requestId;
						}
					}
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
                                <li>单位申请</li>
                                <li class="active">人才公寓申请</li>
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
                                    <div class="row">
                                        <div class="col-sm-12">
                                        	<h3>区筹公租房人员信息表</h3>
                                            <p style="line-height: 35px;">
                                            	本单位共可申请<span id="applicationNum_id"></span>人
                                            	<a class="btn btn-link pull-right"  id="requestUserAdd_id" onclick="ischeck()" ><i class="iconfont icon-plus"></i>增加人员</a>
                                            </p>
                                        	<div class="table-responsive" id="form_table">
                                        		
                                        	</div>
                                            <p>单位盖章的查询个人住房信息授权书
                                            	<div id="authorization_div">
                                            		<form enctype="multipart/form-data" method="post"  id="authorization_form_id"  action="<c:url value="/fileUploadDownLoad/uploadImgeFile"/>" >
												 	<input type="hidden"  name="force" value="1" /><input type="hidden"  name="w" value="128" /><input type="hidden"  name="h" value="96" />
												 	<input type="file"  name="file"  id="authorization_id"  /> <input type="button" onclick="submitImageOnChange(this)" value="上传" ></form>
                                            	</div>
                                            	<a class="pull-right"  target="_blank"  href="<c:url value="/wdit/assets/wts.docx"/>">查询个人住房信息授权书模板下载</a>
                                            </p>
                                            <div class="text-center">
		                                		<a href='javascript:void(0)' class="btn btn-info margin-right-10" onclick="previousFun()">上一步</a>
                                                <a href="javascript:void(0)" class="btn btn-info margin-right-30"  onclick="requestConfirm(1)">确认</a>
                                                <a href="javascript:void(0)" class="btn btn-default" onclick="requestConfirm(0)">保存</a>
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

            </div> 
        </div> 
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>
    </div>
</body>
</html>