<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>申请人员</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

<script type="text/javascript">
var requestflag=false;
var id = '${sessionScope.USER.companyId}';
function saveUser(){
		window.location.href=rootPath+"companyRequest/requestUserList";
}
$(function(){
	$("#upcompany").attr("href",""+rootPath+"company/showmycompany?id="+id+"");
	getRequestCompanyList();
	getcompanyrequest();
	getRequestSettime();
	
	$("#previewbtn").click(function(){
		if(requestflag){
			alert("当前申请不在申请时间内或未开启申请!");
			return false;
		}
	});
	$("#menu_ul_53 li:eq(1) a").click(function(){
		if(requestflag){
			alert("当前申请不在申请时间内或未开启申请!");
			return false;
		}
	});
}) 

function getRequestSettime(){
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
				var nowtime=obj.nowDate;
				nowtime=nowtime.substr(0,10);
				var overtime=obj.applicationOverTime
				var starttime=obj.applicationStartTime
					if(status==0){
						requestflag=true;
					}
					if(nowtime<starttime){
						requestflag=true;
					}
					if(nowtime>overtime){
						requestflag=true;
					}
			})
		}
	});
}
function getcompanyrequest(){
	var  byIdconfig2={
			fromConfig:[{
				formCode:"wdit_company_photo",
				currentField:"companyid",
				parentId:"id",
				aliasesName:"companyPhoto",
				fieldName:"id,organizationSmallPhoto,organizationPhoto,taxRegistersCardNumber,taxationPhotoName,taxationSmallPhoto,taxationPhoto,authorizationName,authorizationSmallPhoto,authorization,organizationPhotoName,organization,businessLicense,registrationNumber,businessLicenseSmallPhoto,businessLicenseName,unifiedSocialCreditCode,licenseNumber,photoType"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"companyClassification",
				aliasesName:"companyClassification",
				fieldName:"id,displayValue"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"rent",
				aliasesName:"rent",
				fieldName:"displayValue"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"pickDwelling",
				aliasesName:"pickDwelling",
				fieldName:"displayValue"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"companyNature",
				aliasesName:"companyNatures",
				fieldName:"displayValue"
			}]
	}
	
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company",
			condition: " id="+id,
			byIdconfig:JSON.stringify(byIdconfig2)
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				
				$("#applicantnums").html(obj.applicationNum);
				$("#modifyDate").html(obj.modifyDate);
				
				$("#applicationNum").html(obj.applicationNum);
				var canApplicationNum=obj.canApplicationNum;
				var nums=obj.applicationNum
				$("#nums").html(nums);
				$("#applicant").html(obj.applicant);
				$("#registerAddress").html(obj.registerAddress);
				$("#officeAddress").html(obj.officeAddress);
				var registerMoney=toDecimal2(obj.registerMoney)
				var oneYearIsTaxAmount=toDecimal2(obj.oneYearIsTaxAmount)
				$("#registerMoney").html(registerMoney);
				$("#oneYearIsTaxAmount").html(oneYearIsTaxAmount);
				$("#staffNum").html(obj.staffNum);
				
				var rent=obj.rent[0];
				if(rent){
					$("#rent").html(rent.displayValue);	
				}
				var pickDwellings=obj.pickDwelling[0];
				if(pickDwellings){
					$("#pickDwelling").html(pickDwellings.displayValue);
				}
				var companyNature_id=obj.companyNature;
				var companyNature=obj.companyNatures[0];
				if(companyNature){
					$("#companyNature").html(companyNature.displayValue);
				}
				$("#linkman").html(obj.linkman);
				$("#phone").html(obj.phone);
				$("#bt_change").before(obj.email);
// 				$("#email").html(obj.email);
				$("#tel").html(obj.tel);
				$("#applicationCompany").html(obj.applicationCompany);
				var displayValue= obj.companyClassification[0];
				if(displayValue)
					$("#companytype").html(displayValue.displayValue);
				var companyId=obj.companyId;				  
				if(obj.companyPhoto&&obj.companyPhoto.length>0){
					var companyPhoto=obj.companyPhoto[0];
					$("#unifiedSocialCreditCode").html(companyPhoto.unifiedSocialCreditCode);
					if(companyPhoto.photoType+""=="1"){
						$("#registrationNumber_tr").hide();
						$("#taxRegistersCardNumber_tr").hide();
						$("#photoType1_tr").show();
						$("#photoType2_tr").hide();
						
						$("#licenseNumber").html(companyPhoto.licenseNumber);
						if(companyPhoto.businessLicense){
							$("#photoType1_td").html('<a onClick="showPhoto(\''+rootPath+companyPhoto.businessLicense+'\')"><img src="'+rootPath+companyPhoto.businessLicenseSmallPhoto+'" height="180" alt=""></a>');	
						}
					}else {
						$("#registrationNumber").html(companyPhoto.registrationNumber);
						$("#taxRegistersCardNumber").html(companyPhoto.taxRegistersCardNumber);
						if(companyPhoto.businessLicense){
							var photoHtml='<a onClick="showPhoto(\''+rootPath+companyPhoto.organizationPhoto+'\')"><img src="'+rootPath+companyPhoto.organizationSmallPhoto+'" height="180" alt=""></a>';
							if(companyNature_id==1702){//事业单位
								photoHtml+='<a onClick="showPhoto(\''+rootPath+companyPhoto.businessLicense+'\')"><img src="'+rootPath+companyPhoto.businessLicenseSmallPhoto+'" height="180" alt=""></a>';
							}else if(companyNature_id==1703){//机关
							}else if(companyNature_id==1701){//非事业单位
								photoHtml+='<a onClick="showPhoto(\''+rootPath+companyPhoto.businessLicense+'\')"><img src="'+rootPath+companyPhoto.businessLicenseSmallPhoto+'" height="180" alt=""></a>';
								photoHtml+='<a onClick="showPhoto(\''+rootPath+companyPhoto.taxationPhoto+'\')"><img src="'+rootPath+companyPhoto.taxationSmallPhoto+'" height="180" alt=""></a>';					
							}					
							$("#photoType2_td").html(photoHtml);
						}
						
						$("#licenseNumber_tr").hide();
						$("#photoType1_tr").hide();
						$("#photoType2_tr").show();
					}
				}
			}			
		}
	});
}

//制保留2位小数
function toDecimal2(x) {    
    var f = parseFloat(x);    
    if (isNaN(f)) {    
        return false;    
    }    
    var f = Math.round(x*100)/100;    
    var s = f.toString();    
    var rs = s.indexOf('.');    
    if (rs < 0) {    
        rs = s.length;    
        s += '.';    
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
}   

var emaillayer;
function changeEmail(){
	var emailHtml = '<div role="form" class="form-horizontal">'+
					'<div><div width=450><div class="form-group">'+
					      '<label class="col-sm-2 control-label" for="email_id"><span style="color:#FF0000;">*</span>输入邮箱：</label>'+
				          '<div class="col-sm-9"><input type="text" placeholder="必填" id="email_id" class="form-control"></div>'+
				    '</div></div></div>'+
				    '<div ><div  width=450 ><div class="form-group">'+
				          '<label class="col-sm-2 control-label" for="comemail_id"><span style="color:#FF0000;">*</span> 再次输入邮箱:</label>'+
				          '<div class="col-sm-9"><input type="text" placeholder="必填" id="comemail_id" class="form-control"></div>'+
				    '</div></div></div>'+
				    '<div><div  width=450 style="text-align: center;" >'+
					 	'<a href="javascript:void(0)" class="btn btn-info margin-right-10" onclick="saveEmail()">确定</a>'+
					 	'<a href="javascript:void(0)" class="btn btn-info margin-right-10" onclick="returnlogin()">取消</a>'+	
					 '</div></div>'+
					 '</div>';
	emaillayer= layer.open({
        type: 1,
        area: ['520px', '200px'],
        skin: 'layui-layer-rim', //加上边框
        content: emailHtml
    });
}
//取消
function returnlogin(){
	layer.close(emaillayer);
}
//发送邮件
function saveEmail(){
		var email= $("#email_id").val();
		var comEmail=$("#comemail_id").val();
		if(!email){
			alert("请填写邮箱");
			return false;
		}else if(!comEmail){
			alert("请填写确认邮箱");
			return false;
		}else if(email!=comEmail){
			alert("邮箱和确认邮箱不一致");
			return false;
		}
		if(!isEmail(email)){
			 alert("邮件格式不正确!");
			 return false;
		} 
		
		$.ajax({
			url : rootPath+'/user/bindsendEmail',
			type : "POST", 
	      	async: false,
	      	data : {
	      		email : email
	      	}, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.successMsg){
					alert("发送成功");
					layer.close(emaillayer);
			 	}
			}
	      });
	}


function isEmail(str){
    var reg = /^((([A-Z|a-z|0-9_\\.-]+)@([0-9|A-Z|a-z\.-]+)\.([A-Z|a-z\.]{2,6}\;))*(([A-Z|a-z|0-9_\\.-]+)@([0-9|A-Z|a-z\.-]+)\.([A-Z|a-z\.]{2,6})))$/;
    if(!reg.test(str)){
         return false;
     }
     return true;
}
	

var layerindex;
function showPhoto(photo){
	var photoHtml='<div><img src="'+photo+'" height="500" alt=""></div>';
	layerindex= layer.open({
        type: 1,
        area: ['820px', '670px'],
        skin: 'layui-layer-rim', //加上边框
        content: photoHtml
    });
}

//申请列表
function getRequestCompanyList(){
	$.post(rootPath+'/createSelect/findselectData', { 
		code : "approvallist_v1",
		selectConditionSql : " where  t1.companyId="+id
	}, function(data) {
		if (data.code + "" == "1") {
			var results = eval("(" + data.results + ")");
			if(results &&results.length>0){
				var index=0;
				$.each(results,function(i,obj){
					index+=1;
					var status=obj.status;
					var approvalhtml='';
					var statusName='';
					
					if(obj.modifyDate){
						try{
							modifyDate=new Date(parseInt(obj.modifyDate)*1000).format('yyyy-MM-dd hh:mm')
						}catch (e) {}
					}
					if(obj.statusStep==1){
						approvalhtml='<tr><td>'+index+'</td><td>'+obj.acceptanceNumber+'</td>';
						if(obj.status==103){
							statusName="受理不通过";
							approvalhtml+='<td><span class="label label-danger label-sm">'+statusName+'</span></td>';
						}else if(status==101){
							statusName="待受理"; 
							approvalhtml+='<td><span class="label label-info label-sm">'+statusName+'</span></td>';
						}else if(status==102){
							statusName="已受理"; 
							approvalhtml+='<td><span class="label label-info label-sm">'+statusName+'</span></td>';
						}else if(status==104){
							statusName="待受理"; 
							approvalhtml+='<td><span class="label label-info label-sm">'+statusName+'</span></td>';
						}else if(status==105){
							statusName="待受理"; 
							approvalhtml+='<td><span class="label label-info label-sm">'+statusName+'</span></td>';
						}
						
						approvalhtml+='<td>'+obj.shenqingnum+'</td>'+
						'<td>'+obj.passnum+'</td><td>'+modifyDate+'</td>'+
						'<td><a href="'+rootPath+'/company/requestshowcompany?id='+obj.id+'">查看申请</a></td></tr>';
					}else if(obj.statusStep==2||obj.statusStep==3||obj.statusStep==4){
						
						approvalhtml='<tr><td>'+index+'</td><td>'+obj.acceptanceNumber+'</td>';
						if(obj.status==203||obj.status==303||obj.status==403){
							var statusName="审核不通过";
							approvalhtml+='<td><span class="label label-danger label-sm">'+statusName+'</span></td>';
						}else{
							approvalhtml+='<td><span class="label label-default label-sm">'+obj.statusVal+'</span></td>';
						}
						
						approvalhtml+='<td>'+obj.shenqingnum+'</td>'+
						'<td>'+obj.passnum+'</td><td>'+modifyDate+'</td>'+
						'<td><a href="'+rootPath+'/company/requestshowcompany?id='+obj.id+'">查看申请</a></td></tr>';
					}else if(obj.statusStep==5){
						
						approvalhtml='<tr><td>'+index+'</td><td>'+obj.acceptanceNumber+'</td>';
						if(obj.status==503){
							var statusName="审核不通过";
							approvalhtml+='<td><span class="label label-danger label-sm">'+statusName+'</span></td>';
						}else if(obj.status==504){
							var statusName="审核中";
							approvalhtml+='<td><span class="label label-default label-sm">'+statusName+'</span></td>';
						}else{
							approvalhtml+='<td><span class="label label-default label-sm">'+obj.statusVal+'</span></td>';
						}
						
						approvalhtml+='<td>'+obj.shenqingnum+'</td>'+
						'<td>'+obj.passnum+'</td><td>'+modifyDate+'</td>'+
						'<td><a href="'+rootPath+'/company/requestshowcompany?id='+obj.id+'">查看申请</a></td></tr>';
					}else if(obj.statusStep==0){
						approvalhtml='<tr><td>'+index+'</td><td>'+obj.acceptanceNumber+'</td>'+
						'<td><span class="label label-info label-sm">'+obj.statusVal+'</span></td>'+
						'<td>'+obj.shenqingnum+'</td>'+
						'<td>'+obj.passnum+'</td><td>'+modifyDate+'</td>'+
						'<td><a class="previewbtn" href="'+rootPath+'/companyRequest/requestRead?requestId='+obj.id+'">修改提交</a>&nbsp;&nbsp;&nbsp<a href="javascript:void(0)" onclick="deleteRequest('+obj.id+')">删除 </a></td></tr>';
					}else if(obj.statusStep==6){
						approvalhtml='<tr><td>'+index+'</td><td>'+obj.acceptanceNumber+'</td>';
							var statusName="审核完";
							approvalhtml+='<td><span class="label label-default label-sm">'+statusName+'</span></td>';
						
						approvalhtml+='<td>'+obj.shenqingnum+'</td>'+
						'<td>'+obj.passnum+'</td><td>'+modifyDate+'</td>'+
						'<td><a href="'+rootPath+'/company/requestshowcompany?id='+obj.id+'">查看申请</a></td></tr>';
					}
					$("#approvallist").append(approvalhtml);
				})
			}
		}
	}); 
}

function  deleteRequest(id){
	var deleteJson={
			"formId":61,
			"tableDataId":id,
			"subStructure":[{
				"formId":68,
				"parentId":"request_id"
			},{
				"formId":62,
				"parentId":"request_id",
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
				 window.location.reload();
		 	}
		   }
      });
}

function check(){
	if(requestflag)
		return false;
	$.ajax({
		url : rootPath+'/createSelect/findselectData',
		type : "POST", 
      	async: false,
      	data : {
    		code : "approvalnum_v1",
    		selectConditionSql : " where a.companyId="+id
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
						window.location.href=rootPath+"/companyRequest/requestRead";
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
                                        <div class="col-sm-12" >
                                            <h3>单位信息<a id="previewbtn" onclick="check()"  class="btn btn-xs btn-info pull-right">  开始申请 <i class="iconfont icon-arrowright"></i></a></h3>
                                            <div class="table-responsive">
		                               		<table class="table table-bordered table-hover text-center table-horizontal">
														<tbody>
														<tr>
		                               					<th class="text-right" width="200">单位名称</th>
		                                                <td class="text-left" width="300" id="applicant"></td>
		                                                <th class="text-right" width="200">申请名额</th>
		                                                <td class="text-left" width="300" id="nums"></td>
		                                                </tr>
														<tr>
															<th class="text-right" width="200">企业信用代码</th>
													        <td class="text-left" colspan="3" id="unifiedSocialCreditCode" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">注册地址</th>
													        <td class="text-left" id="registerAddress" colspan="3"  ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">营业或办公地址</th>
													        <td class="text-left" id="officeAddress" colspan="3" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">注册资本金(人民币/万元)</th>
													        <td class="text-left" id="registerMoney" colspan="3" ></td>
														</tr>
														<tr id="licenseNumber_tr">
															<th class="text-right" width="200">证照编号</th>
													        <td class="text-left" id="licenseNumber" colspan="3" ></td>
														</tr>
														<tr  id="registrationNumber_tr" >
															<th class="text-right" width="200">工商登记号</th>
													        <td class="text-left" id="registrationNumber" colspan="3" ></td>
														</tr>
														<tr id="taxRegistersCardNumber_tr">
															<th class="text-right" width="200">税务登记号</th>
													        <td class="text-left" id="taxRegistersCardNumber" colspan="3" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">上一年度纳税金额(人民币/万元)</th>
													        <td class="text-left" id="oneYearIsTaxAmount" colspan="3" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">所属委办</th>
													        <td class="text-left" id="companytype" colspan="3"></td>
														</tr>
														<tr>
															<th class="text-right" width="200">在职员工人数</th>
													        <td class="text-left" id="staffNum" colspan="3" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">单位承担租金的百分比</th>
													        <td class="text-left" id="rent" colspan="3" ></td>
														</tr>
														<tr>
			                               					<th class="text-right" width="200">所选小区</th>
			                                                <td class="text-left" id="pickDwelling" colspan="3"></td>
			                               				</tr>
			                               				<tr>
			                              					<th class="text-right" width="200">企业性质</th>
			                                                <td class="text-left" id="companyNature" colspan="3"></td>
			                              				</tr>
														<tr>
															<th class="text-right" width="200">申请公租房工作专职联系人</th>
													        <td class="text-left" id="linkman" colspan="3" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">联系人(手机)</th>
													        <td class="text-left" id="phone" colspan="3" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">联系人(电子邮箱)</th>
													        <td class="text-left" id="email" colspan="3" >
													        <button id = "bt_change" class="btn btn-info margin-right-10" onclick="changeEmail()">我要更新绑定邮箱</button>
													        </td>
														</tr>
														<tr>
															<th class="text-right" width="200">联系人(电话)</th>
													        <td class="text-left"  id="tel" colspan="3" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">申请单位(需求说明)</th>
													        <td class="text-left" id="applicationCompany" colspan="3" ></td>
														</tr>
														<tr id="photoType1_tr">
															<th class="text-right" width="200">营业执照(三证合一)</th>
													        <td class="text-left" id="photoType1_td" colspan="3" ></td>
														</tr>
														<tr id="photoType2_tr">
															<th class="text-right" width="200">营业执照(未三证合一)</th>
													        <td class="text-left" id="photoType2_td" colspan="3" ></td>
														</tr>
														</tbody>
													</table>
                                            <a id="upcompany"  class="btn btn-sm btn-info pull-right">我要更新单位信息</a>
                                            </div>
                                        </div>
                                    </div>
                                    <hr class="hr-sm">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <h3>申请列表</h3>
                                            <div class="table-responsive">
                                            <table id="approvallist" class="table table-bordered table-hover text-center table-striped">
                                            <tbody>
                                            	<tr>
                                            		<th>序号</th>
                                            		<th>受理编号</th>
                                            		<th>申请状态</th>
                                            		<th>申请人数</th>
                                            		<th>审核通过人数</th>
                                            		<th>最后操作时间</th>
                                            		<th>操作</th>
                                            	</tr>
                                            </tbody>
                                            </table>
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