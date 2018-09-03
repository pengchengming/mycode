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
var  requestId='${requestId}';
var createTable = new createTable();	
$(function(){
	getRequest();
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
			},   {
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
			}]
		}; 
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
});

function getRequest(){ 
		var  byIdconfig2={
				fromConfig:[{
					formCode:"wdit_company_request_photo",
					currentField:"request_id",
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
					parentId:"pickDwelling",
					aliasesName:"pickDwelling",
					fieldName:"displayValue"
				},{
					formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"companyNature",
					aliasesName:"companyNatures",
					fieldName:"displayValue"
				},{
					formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"rent",
					aliasesName:"rent",
					fieldName:"displayValue"
				}]
		}
		
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"wdit_company_request",
				condition: " id="+requestId,
				byIdconfig:JSON.stringify(byIdconfig2)
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;
				if(results&&results.length>0){
					var obj=results[0];
					$("#applicationNum").html(obj.applicationNum);
					$("#applicant").html(obj.applicant);
					$("#registerAddress").html(obj.registerAddress);
					$("#officeAddress").html(obj.officeAddress);
					var registerMoney=toDecimal2(obj.registerMoney)
					var oneYearIsTaxAmount=toDecimal2(obj.oneYearIsTaxAmount)
					$("#registerMoney").html(registerMoney);
					$("#oneYearIsTaxAmount").html(oneYearIsTaxAmount);
					$("#staffNum").html(obj.staffNum);
					$("#linkman").html(obj.linkman);
					$("#phone").html(obj.phone);
					$("#email").html(obj.email);
					$("#tel").html(obj.tel);
					$("#applicationCompany").html(obj.applicationCompany);
					var displayValue= obj.companyClassification[0];
					if(displayValue)
						$("#companytype").html(displayValue.displayValue);
					var companyId=obj.companyId;

					var pickDwellings=obj.pickDwelling[0];
					if(pickDwellings){
						$("#pickDwelling").html(pickDwellings.displayValue);
					}
					var rent=obj.rent[0];
					if(rent){
						$("#rent").html(rent.displayValue);	
					}
					var companyNature_id=obj.companyNature;
					var companyNature=obj.companyNatures[0];
					if(companyNature){
						$("#companyNature").html(companyNature.displayValue);
					}
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
function colseRequest(){
	window.location.href=rootPath+"companyRequest/requestList";
}
var i;
var layerindex;
var index=1;
function confirm(){
	index=0;
	var conHtml="<div>根据上海市公租房的相关政策，表格中的内容填写需真实有效，如有虚假，将会影响公共租赁住房的申请</div>"+
	"<div class='text-center'>"+
	"<a id='confirm_id' href='javascript:void(0)' class='btn btn-info margin-right-30'>确认提交（10秒）</a>"+
	"<a onclick='closeDiv()' href='javascript:void(0)' class='btn btn-default'>关闭</a></div>";
 	layerindex= layer.open({
         type: 1,
         title:"提交信息真实性承诺",
         area: ['600px', '250px'],
         skin: 'layui-layer-rim', //加上边框
         content: conHtml
     });
	i=setInterval("showConfirmDiv()",1000); 
}
function closeDiv(){
	layer.close(layerindex);
}
function showConfirmDiv(){
	if(index==0){
		$("#confirm_id").html("确认提交");
		$("#confirm_id").click(function(){
			confirmRequest();
		});
		clearInterval(i); 
	}else {
		index--;
		$("#confirm_id").html("确认提交（"+index+"秒）");
	}
}

function confirmRequest(){
	var requestObj={
			"formId":61,
			"tableDataId":requestId,
			"register":{"status":101},
			"detail":[{"formId":94,"parentId":"request_id","array":[{"isGenerate":0,"type":1}]}] 
	}
	$.ajax({
		url : rootPath+'/forms/saveFormDataJson',
		type : "POST", 
      	async: false,
      	data : {
      		json : JSON.stringify(requestObj)
      	}, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.successMsg){
				alert("确认成功")
				window.location.href=rootPath+"companyRequest/requestList";
		 	}
		   }
      });
}

function closepage(){
	window.location.href="<c:url value='/companyRequest/requestList' />";
}
function previousFun(){
	window.location.href=rootPath+"companyRequest/requestUserList?requestId="+requestId;
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
                                <li>企业用户</li>
                                <li class="active">提交申请</li>
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
                                            <h3>公司信息</h3>
                                            <div class="table-responsive">
                                               <table class="table table-bordered table-hover text-center table-horizontal">
													<tbody>
														<tr>
															<th class="text-right" width="200">单位名称</th>
													        <td class="text-left"  id="applicant"></td>
														</tr>
														<tr>
															<th class="text-right" width="200">企业信用代码</th>
													        <td class="text-left" id="unifiedSocialCreditCode" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">注册地址</th>
													        <td class="text-left" id="registerAddress"  ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">营业或办公地址</th>
													        <td class="text-left" id="officeAddress" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">注册资本金(人民币/万元)</th>
													        <td class="text-left" id="registerMoney" ></td>
														</tr>
														<tr id="licenseNumber_tr">
															<th class="text-right" width="200">证照编号</th>
													        <td class="text-left" id="licenseNumber" ></td>
														</tr>
														<tr  id="registrationNumber_tr" >
															<th class="text-right" width="200">工商登记号</th>
													        <td class="text-left" id="registrationNumber" ></td>
														</tr>
														<tr id="taxRegistersCardNumber_tr">
															<th class="text-right" width="200">税务登记号</th>
													        <td class="text-left" id="taxRegistersCardNumber" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">上一年度纳税金额(人民币/万元)</th>
													        <td class="text-left" id="oneYearIsTaxAmount" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">所属委办</th>
													        <td class="text-left" id="companytype"></td>
														</tr>
														<tr>
															<th class="text-right" width="200">在职员工人数</th>
													        <td class="text-left" id="staffNum"  ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">单位承担租金的百分比</th>
													        <td class="text-left" id="rent"  ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">所选小区</th>
													        <td class="text-left" id="pickDwelling"></td>
														</tr>
														<tr>
															<th class="text-right" width="200">企业性质</th>
													        <td class="text-left" id="companyNature"></td>
														</tr>
														<tr>
															<th class="text-right" width="200">申请公租房工作专职联系人</th>
													        <td class="text-left" id="linkman" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">联系人(手机)</th>
													        <td class="text-left" id="phone" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">联系人(电子邮箱)</th>
													        <td class="text-left" id="email" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">联系人(电话)</th>
													        <td class="text-left"  id="tel" ></td>
														</tr>
														<tr>
															<th class="text-right" width="200">申请单位(需求说明)</th>
													        <td class="text-left" id="applicationCompany" ></td>
														</tr>
														<tr id="photoType1_tr">
															<th class="text-right" width="200">营业执照(三证合一)</th>
													        <td class="text-left" id="photoType1_td"></td>
														</tr>
														<tr id="photoType2_tr">
															<th class="text-right" width="200">营业执照(未三证合一)</th>
													        <td class="text-left" id="photoType2_td"></td>
														</tr>
														</tbody>
													</table>
                                            </div>
                                        </div>
                                    </div>
                                    <hr class="hr-sm">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <h3>人员信息列表</h3>
                                            <div class="table-responsive" id="form_table">
                                                 
                                            </div>
                                        </div>
                                    </div>
                                    <p class="text-center">
		                                <a href='javascript:void(0)' class="btn btn-info margin-right-10" onclick="previousFun()">上一步</a>
                                        <a class="btn btn-info margin-right-30"  data-toggle="modal" onclick="confirm()" >确认提交</a>
                                        <a class="btn btn-default" href='javascript:void(0)' onclick="closepage()" >关闭</a>
                                    </p>
                                    
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