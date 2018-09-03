<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>单位主页-我的申请</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
 

<script type="text/javascript">
var id = '${sessionScope.USER.companyId}';

$(function(){
	getcompanyrequest();
	getcompanyphoto();
})

function getcompanyrequest(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company",
			condition: " id="+id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			$.each(results,function(i,obj){
				if(obj.isApplication==1){
					$("#isApplication").val("是");	
				}else if(obj.isApplication==0){
					$("#isApplication").val("否");
				}
				var num=parseInt(obj.applicationNum);
				$("#nums").val(num);
				$("#applicationNum").val(obj.applicationNum);
				$("#applicant").val(obj.applicant);
				$("#registerAddress").val(obj.registerAddress);
				$("#officeAddress").val(obj.officeAddress);
				var registerMoney=toDecimal2(obj.registerMoney)
				var oneYearIsTaxAmount=toDecimal2(obj.oneYearIsTaxAmount)
				$("#registerMoney").html(registerMoney);
				$("#oneYearIsTaxAmount").html(oneYearIsTaxAmount);
				$("#staffNum").val(obj.staffNum);
				$("#rent").val(obj.rent);
				$("#companyClassification").val(obj.companyClassification);
				$("#linkman").val(obj.linkman);
				$("#phone").val(obj.phone);
				$("#email").val(obj.email);
				$("#tel").val(obj.tel);
				$("#applicationCompany").val(obj.applicationCompany);
			})
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

function getcompanyphoto(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company_photo",
			condition: " companyId="+id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			$.each(results,function(i,obj){
				$("#registrationNumber").val(obj.registrationNumber);
				$("#taxRegistersCardNumber").val(obj.taxRegistersCardNumber);
				$("#unifiedSocialCreditCode").val(obj.unifiedSocialCreditCode);
				$("#businessLicense").html('<img src="'+rootPath+obj.businessLicenseSmallPhoto+'" height="180" alt="">');
			})
		}
	});
	
	var input=$('input[type="text"]');
	for(var i=0;i<input.length;i++){
		input[i].parentNode.innerHTML = input[i].value;
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
                                <li>企业用户</li>
                                <li class="active">我的申请</li>
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
	                                        <div class="form-group">
	                                            <div class="col-sm-4" style="float:right">
	                                                <input type="button" value="开始申请" onclick='location.href=("<c:url value="/companyRequest/requestRead" />")' class="form-control" />
	                                            </div>
	                                        </div>
	                                    </div>
	                                </div> 
                                	<div class="table-responsive" style="overflow: hidden;margin-top:10px;" tabindex="2">                       		
                               		<table class="table table-bordered table-hover text-center table-horizontal">
                               			<tbody>
                               				<tr>
                               					<th class="text-right" width="200">单位名称</th>
                                                <td class="text-left" width="300"><input type="text" id="applicant" /></td>
                                                <th class="text-right" width="200">剩余申请名额</th>
                                                <td class="text-left" width="300"><input type="text" id="nums" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">企业信用代码</th>
                                                <td class="text-left" colspan="3"><input type="text" id="unifiedSocialCreditCode" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">注册地址</th>
                                                <td class="text-left" colspan="3"><input type="text" id="registerAddress" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">营业或办公地址</th>
                                                <td class="text-left" colspan="3"><input type="text" id="officeAddress" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">注册资本金(人民币/万元)</th>
                                                <td class="text-left" colspan="3"><input type="text" id="registerMoney" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">工商登记号</th>
                                                <td class="text-left" colspan="3"><input type="text" id="registrationNumber" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">税务登记号</th>
                                                <td class="text-left" colspan="3"><input type="text" id="taxRegistersCardNumber" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">上一年度纳税金额(人民币/万元)</th>
                                                <td class="text-left" colspan="3"><input type="text" id="oneYearIsTaxAmount" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">在职员工人数</th>
                                                <td class="text-left" colspan="3"><input type="text" id="staffNum" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">单位承担租金的百分比</th>
                                                <td class="text-left" colspan="3"><input type="text" id="rent" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">申请公租房工作专职联系人</th>
                                                <td class="text-left" colspan="3"><input type="text" id="linkman" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">联系人(手机)</th>
                                                <td class="text-left" colspan="3"><input type="text" id="phone" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">联系人(电子邮箱)</th>
                                                <td class="text-left" colspan="3"><input type="text" id="email" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">联系人(电话)</th>
                                                <td class="text-left" colspan="3"><input type="text" id="tel" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">申请单位(需求说明)</th>
                                                <td class="text-left" colspan="3"><input type="text" id="applicationCompany" /></td>
                               				</tr>
                               				<tr>
                               					<th class="text-right" width="200">营业执照(未三证合一)</th>
                                                <td class="text-left" colspan="3"><span id="businessLicense"></span></td>
                               				</tr>
                               				<tr>
                               					<td colspan="4"><a href="<c:url value="/company/showmycompany?id=1001" />" class="btn btn-info">我要更新单位信息</a></td>
                               				</tr>
                               			</tbody>
                               		</table>
                               		<div class="row">
                                        <div class="col-sm-12">
                                            <h3>申请列表</h3>
                                            <div class="table-responsive">
                                            <table class="table table-bordered table-hover text-center table-striped">
                                                <thead>
                                                    <tr>
                                                        <td >序号</td>
                                                        <td>受理编号</td>
                                                        <td>申请状态</td>
                                                        <td>申请人数</td>
                                                        <td>审核通过人数</td>
                                                        <td>最后操作时间</td>
                                                        <td>操作</td>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <td>1</td>
                                                        <td>XHKW201700001010</td>
                                                        <td><span class="label label-info label-sm">待提交</span></td>
                                                        <td>10</td>
                                                        <td>0</td>
                                                        <td>2017-03-02 16:30</td>
                                                        <td><a class="margin-right-5" href="#">修改提交</a><a href="#">删除</a></td>
                                                    </tr>
                                                    <tr>
                                                        <td>2</td>
                                                        <td>XHKW201700001010</td>
                                                        <td><span class="label label-info label-sm">待受理</span></td>
                                                        <td>10</td>
                                                        <td>0</td>
                                                        <td>2017-03-02 16:30</td>
                                                        <td><a class="margin-right-5" href="#">查看申请</a></td>
                                                    </tr>
                                                    <tr>
                                                        <td>3</td>
                                                        <td>XHKW201700001010</td>
                                                        <td><span class="label label-danger label-sm">受理不通过</span></td>
                                                        <td>10</td>
                                                        <td>0</td>
                                                        <td>2017-03-02 16:30</td>
                                                        <td><a class="margin-right-5" href="#">查看申请</a></td>
                                                    </tr>
                                                    <tr>
                                                        <td>4</td>
                                                        <td>XHKW201700001010</td>
                                                        <td><span class="label label-warning label-sm">审核中</span></td>
                                                        <td>10</td>
                                                        <td>0</td>
                                                        <td>2017-03-02 16:30</td>
                                                        <td><a class="margin-right-5" href="#">查看申请</a></td>
                                                    </tr>
                                                    <tr>
                                                        <td>5</td>
                                                        <td>XHKW201700001010</td>
                                                        <td><span class="label label-default label-sm">审核完</span></td>
                                                        <td>10</td>
                                                        <td>0</td>
                                                        <td>2017-03-02 16:30</td>
                                                        <td><a class="margin-right-5" href="#">查看申请</a></td>
                                                    </tr>
                                                </tbody>
                                            </table>
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

            </div> 
        </div> 
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>

</body>
</html>