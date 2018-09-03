<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>查看企业详情</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

 

<script type="text/javascript">
var id='${id}';

$(function(){
	getcompany();
// 	getcompanyphoto();
})

function getcompany(){
	var  byIdconfig2={
			fromConfig:[{
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
				parentId:"companyClassification",
				aliasesName:"companyClassification",
				fieldName:"displayValue"
			},{
				formCode:"wdit_company_photo",
				currentField:"companyId",
				parentId:"id",
				aliasesName:"companyPhoto",
				fieldName:"id,organizationSmallPhoto,organizationPhoto,taxRegistersCardNumber,taxationPhotoName,taxationSmallPhoto,taxationPhoto,authorizationName,authorizationSmallPhoto,authorization,organizationPhotoName,organization,businessLicense,registrationNumber,businessLicenseSmallPhoto,businessLicenseName,unifiedSocialCreditCode,licenseNumber,photoType"
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
				if(obj.isApplication==1){
					$("#isApplication").html("是");	
				}else if(obj.isApplication==0){
					$("#isApplication").html("否");
				}
				$("#code").html(obj.code);
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
				
				var rents = obj.rent[0];
				if(rents){
					$("#rent").html(rents.displayValue);
				}
				var companyClassifications=obj.companyClassification[0];
				if(companyClassifications){
					$("#companyClassification").html(companyClassifications.displayValue);
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
				var companyPhoto=obj.companyPhoto[0];
				if(companyPhoto){
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

// function getcompanyphoto(){
// 	$.ajax({
// 		url : rootPath+'/forms/getDataByFormId',
// 		type : "POST",
//         async: false,
// 		data:{
// 			formCode:"wdit_company_photo",
// 			condition: " companyId="+id
// 		},
// 		complete : function(xhr, textStatus){
// 			var data = JSON.parse(xhr.responseText);
// 			var results = data.results;
// 			$.each(results,function(i,obj){
// 				$("#registrationNumber").html(obj.registrationNumber);
// 				$("#taxRegistersCardNumber").html(obj.taxRegistersCardNumber);
// 				$("#unifiedSocialCreditCode").html(obj.unifiedSocialCreditCode);
// 				$("#businessLicense").html('<img src="'+rootPath+obj.businessLicenseSmallPhoto+'" height="180" alt="">');
// 			})
// 		}
// 	});
// }
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
                                <li class="active"><a href="<c:url value="/talentoffice/companylist" />">企业查看</a></li>
                                <li class="active"><a href="">查看企业信息</a></li>
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
                                	<div class="table-responsive" style="overflow: hidden;margin-top:10px;" tabindex="2">
                               		<table class="table table-bordered table-hover text-center table-horizontal">
                               			<tbody>
                               				<tr>
                               					<th class="text-right" width="200">是否可申请</th>
                                                <td class="text-left" id="isApplication"></td>
                                                <th class="text-right" width="200">账户名</th>
                                                <td class="text-left" id="code"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">可申请人数</th>
                                                <td class="text-left" colspan="3" id="applicationNum"></td>
                               				</tr>
                               			</tbody>
                               		</table>
                               		<table class="table table-bordered table-hover text-center table-horizontal">
                               			<tbody>
                               				<tr>
                               					<th class="text-right" width="200">单位名称</th>
                                                <td class="text-left" id="applicant"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">企业信用代码</th>
                                                <td class="text-left" id="unifiedSocialCreditCode"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">注册地址</th>
                                                <td class="text-left" id="registerAddress"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">营业或办公地址</th>
                                                <td class="text-left" id="officeAddress"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">注册资本金(人民币/万元)</th>
                                                <td class="text-left" id="registerMoney"></td>
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
                                                <td class="text-left" id="oneYearIsTaxAmount"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">所属委办</th>
                                                <td class="text-left" id="companyClassification"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">在职员工人数</th>
                                                <td class="text-left" id="staffNum"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">单位承担租金的百分比</th>
                                                <td class="text-left" id="rent"></td>
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
                                                <td class="text-left" id="linkman"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">联系人(手机)</th>
                                                <td class="text-left" id="phone" ></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">联系人(电子邮箱)</th>
                                                <td class="text-left" id="email"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">联系人(电话)</th>
                                                <td class="text-left" id="tel"></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">申请单位(需求说明)</th>
                                                <td class="text-left" id="applicationCompany"></td>
                               				</tr>
                               				<tr id="photoType1_tr">
												<th class="text-right" width="200">营业执照(三证合一)</th>
										        <td class="text-left" id="photoType1_td"></td>
											</tr>
											<tr id="photoType2_tr">
												<th class="text-right" width="200">营业执照(未三证合一)</th>
										        <td class="text-left"  id="photoType2_td"></td>
											</tr>
                               			</tbody>
                               		</table>
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