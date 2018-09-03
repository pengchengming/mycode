<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>新增修改企业详情</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

 

<script type="text/javascript">
var id='${id}';
var type='${type}';
var roles = "${sessionScope.ROLES}";
$(function(){
	if(roles.indexOf("HOUSEMANAGE_ROLE,")>=0){
		$(".breadcrumb li:eq(0)").html("房管局");
	}
	dictionaryValueHtml=getDataDictionaryValueHtml(150);
	dictionaryValueHtml1=getDataDictionaryValueHtml1(170);
	$("#companyNature").parent().html("<select id='companyNature'  >"+dictionaryValueHtml1+"</select>");
	$("#companyClassification").parent().html("<select id='companytype'  ><option value=''>请选择</option>"+dictionaryValueHtml+"</select>");
	if(id){
		var byIdconfig2={
				fromConfig:[{
					formCode:"wdit_company_photo",
					currentField:"companyId",
					parentId:"id",
					aliasesName:"companyPhoto",
					fieldName:"id,unifiedSocialCreditCode"
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
				$.each(results,function(i,obj){
					if(obj.isApplication==1){
						$("#isApplication").attr("checked",true);	
					}
					$("#code").val(obj.code);
					$("#applicationNum").val(obj.applicationNum);
					$("#applicant").val(obj.applicant);
					$("#registerAddress").val(obj.registerAddress);
					$("#officeAddress").val(obj.officeAddress);
					if(obj.registerMoney){
						var registerMoney=toDecimal2(obj.registerMoney);
					}
					if(obj.oneYearIsTaxAmount){
						var oneYearIsTaxAmount=toDecimal2(obj.oneYearIsTaxAmount);
					}
					
					$("#registerMoney").val(registerMoney);
					$("#oneYearIsTaxAmount").val(oneYearIsTaxAmount);
					$("#staffNum").val(obj.staffNum);
					$("#linkman").val(obj.linkman);
					$("#phone").val(obj.phone);
					$("#email").val(obj.email);
					$("#tel").val(obj.tel);
					$("#applicationCompany").val(obj.applicationCompany);
					$("#companytype").val(obj.companyClassification);
					
					var companyPhoto=obj.companyPhoto[0];
					if(companyPhoto){
						$("#unifiedSocialCreditCode").val(companyPhoto.unifiedSocialCreditCode);
						$("#photoid").val(companyPhoto.id);
					}
					var rents = obj.rent[0];
					if(rents){
						$("#rent").html(rents.displayValue);
					}
					var pickDwellings=obj.pickDwelling[0];
					if(pickDwellings){
						$("#pickDwelling").html(pickDwellings.displayValue);
					}
					$("#companyNature").val(obj.companyNature);
				})
			}
		});
	}
})

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
						dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
	        		});
			   }
			}});
	   return dictionaryValueHtml;
}
function getDataDictionaryValueHtml1(code){
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
						dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
	        		});
			   }
			}});
	   return dictionaryValueHtml;
}

function addCompany(){
	var applicant=$("#applicant").val();
	if(applicant.length>=50){
		alert("申请单位（全称）超出长度规范！ ");
		return false;
	}
	var registerAddress=$("#registerAddress").val();
	if(!registerAddress){
		alert("请填写 注册地址 ");
		return false;
	}else if(registerAddress.length>=200){
		alert("注册地址 超出长度规范！ ");
		return false;
	}	
	
	var officeAddress=$("#officeAddress").val();
	if(!officeAddress){
		alert("请填写 营业或办公地址  ");
		return false;
	}else if(officeAddress.length>=200){
		alert("营业或办公地址 超出长度规范！ ");
		return false;
	}	
	var registerMoney= $("#registerMoney").val();
	var oneYearIsTaxAmount=$("#oneYearIsTaxAmount").val();
	registerMoney=toDecimal2(registerMoney);
	oneYearIsTaxAmount=toDecimal2(oneYearIsTaxAmount);
	if(!registerMoney){
		alert("请填写 注册资本金  ");
		return false;
	}else if(registerMoney.length>11){
		alert("注册资本金 超出金额上限！ ");
		$("#registerMoney").val("");
		$("#registerMoney").focus();
		return false;
	}
	if(!oneYearIsTaxAmount){
		alert("请填写 上一年度纳税金额 ");
		return false;
	}else if(oneYearIsTaxAmount.length>11){
		alert("上一年度纳税金额 超出金额上限！ ");
		$("#oneYearIsTaxAmount").val("");
		$("#oneYearIsTaxAmount").focus();
		return false;
	}
	var applicationNum=$("#applicationNum").val();
	if(applicationNum.length>5){
		alert("可申请人数 超出人数上限！ ");
		return false;
	}
	var staffNum=$("#staffNum").val();
	if(!staffNum){
		alert("请填写 在职员工人数  ");
		return false;
	}else if(staffNum.length>8){
		alert("在职员工人数 超出人数上限！ ");
		return false;
	}
	var rent=$("#rent").val();
	var linkman=$("#linkman").val();
	if(!linkman){
		alert("请填写 专职联系人  ");
		return false;
	}else if(linkman.length>=50){
		alert("专职联系人 超出长度规范！ ");
		return false;
	}	
	var phone=$("#phone").val();
	if(!phone){
		alert("请填写 联系人手机  ");
		return false;
	}else if(phone.length>=50){
		alert("联系人手机 超出长度规范！ ");
		return false;
	}
	var tel=$("#tel").val();
	if(!tel){
		alert("请填写 联系人电话");
		return false;
	}else if(tel.length>=50){
		alert("联系人电话  超出长度规范！ ");
		return false;
	}
	var applicationCompany=$("#applicationCompany").val();
	if(applicationCompany.length>250){
		alert("申请单位（需求说明）  超出长度规范！ ");
		return false;
	}
	var companytype=$("#companytype").val();
	if(companytype==''){
		alert("请选择所属委办");
		return false;
	}
	var email=$("#email").val();
	var reg=/^[\w\-\.]+@[\w\-\.]+(\.\w+)+$/;
	if(!reg.test(email)){
		alert("邮箱格式输入错误！请重新输入")
		return false;
	}else if(email.length>=50){
		alert("电子邮箱 超出长度规范！ ");
		return false;
	}
	var input =$('body :input[type="text"]');
	for(var i=0;i<input.length;i++){
// 		input[i].parentNode.innerHTML = input[i].value;
		if(input[i].value==''){
			alert("带红心的文本框的为必填项，请确认填写！！！");
			return false;
		}
	}
	var isApplication=0;
	if($("#isApplication").is(":checked")==true){
		isApplication=1;
	}
	var obj = new Object();
	obj.isApplication=isApplication;
	obj.applicationNum=$("#applicationNum").val();
	
	obj.applicant=$("#applicant").val();
	obj.registerAddress=$("#registerAddress").val();
	obj.officeAddress=$("#officeAddress").val();
	obj.registerMoney=$("#registerMoney").val();
	obj.oneYearIsTaxAmount=$("#oneYearIsTaxAmount").val();
	obj.staffNum=$("#staffNum").val();
	obj.rent=$("#rent").val();
	obj.companyClassification=$("#companytype").val();
	obj.linkman=$("#linkman").val();
	obj.phone=$("#phone").val();
	obj.email=$("#email").val();
	obj.tel=$("#tel").val();
	obj.applicationCompany=$("#applicationCompany").val();
	obj.companyNature=$("#companyNature").val();
	obj.valid=1;
	
	 var unifiedSocialCreditCode=$("#unifiedSocialCreditCode").val();
 	if(!unifiedSocialCreditCode){
 		alert("请填写 企业信用代码  ");
 		return false;
 	}else if(unifiedSocialCreditCode.length>=100){
 		alert("企业信用代码 超出长度规范！ ");
 		return false;
 	}
	var companytype=$("#companytype").val();
	
	var detail=new Array();
	var array = new Array();
	var json = new Object();
	json.unifiedSocialCreditCode=unifiedSocialCreditCode;
	
	var jsonString={
			"formId":58,
			"register":obj
	}
	if(id){
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"global_user",
				condition: "companyId="+id
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;
				$.each(results,function(i,obj){
					$("#userid").val(obj.id);
				})
			}
		});
		jsonString["tableDataId"]=id;
		var jsonString2={
				"formId":59,
				"tableDataId":$("#photoid").val(),
				"register":json
			};
		var jsonString3={
	    		   "formId":24,
	    		   "tableDataId":$("#userid").val(),
	    		   "register":{"enabled":isApplication}
	       }
		var index = layer.load(2);
		$.ajax({
			url : rootPath+'/forms/saveFormDataJsons',
			type : "POST", 
	        async: false,
	        data : {
	        	jsons : [JSON.stringify(jsonString),JSON.stringify(jsonString2),JSON.stringify(jsonString3)]
	        }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var compayId=data.id;
				if(data.successMsg){
					alert(data.successMsg);
					window.location.href="<c:url value='/talentoffice/companylist' />";
		 		}else{
		 			alert("操作失败");
		 			layer.close(index);
		 			return false;
		 		}
			    }
	        });
	}else{
		var random=parseInt(Math.random()*(99999999-10000000+1)+10000000);
		if(companytype&&companytype.length>0){
			var companyCode="";
			if(companytype==1501){
				companyCode="JR"
			}else if(companytype==1502){
				companyCode="KJ"
			}else if(companytype==1503){
				companyCode="SY"
			}else if(companytype==1504){
				companyCode="RC"
			}
			jsonString.register.code=companyCode+random;
		}
		jsonString["detail"]=detail;
		detail.push({"formId":59,"parentId":"companyId","isSave":1,"array":array});
		array.push(json);
		
		var index = layer.load(2);
		$.ajax({
			url : rootPath+'/forms/saveFormDataJson',
			type : "POST", 
	        async: false,
	        data : {
	        	json : JSON.stringify(jsonString)
	        }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var compayId=data.id;
				if(data.successMsg){
					if(!id){
						addnewuser(compayId);
					}else{
						alert(data.successMsg);
						window.location.href="<c:url value='/talentoffice/companylist' />";
					}
		 		}else{
		 			layer.close(index);
		 			alert("操作失败");
		 			return false;
		 		}
			    }
	        });
	}
	
}
//添加入global_user中
function addnewuser(compayId){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"wdit_company",
			condition: " id="+compayId
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			$.each(results,function(i,obj){
				$("#code").val(obj.code);
			})
		}
	});
	var code=$("#code").val();
	var userobj=new Object();
	userobj.companyId=compayId;
	userobj.username=code;
	userobj.realname=$("#applicant").val();
	userobj.tel=$("#tel").val();
	userobj.accountNonExpired=1;
	userobj.accountNonLocked=1;
	userobj.credentialsNonExpired=1;
	if($("#isApplication").is(":checked")==true){
		userobj.enabled=1;
	}else{
		userobj.enabled=0;
	}
	
	userobj.passwordChanged=0;
	var jsonString={
			"formId":24,
			"register":userobj
	};
	$.ajax({
		url : rootPath+'/talentoffice/saveFormDataJson',
		type : "POST", 
        async: false,
        data : {
        	json : JSON.stringify(jsonString)
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.successMsg){3
				alert("操作成功！！");
				window.location.href="<c:url value='/talentoffice/companylist' />";
	 		}else{
	 			alert("操作失败");
	 			return false;
	 		}
		    }
        });
}
function moneyFun(me) {
	var num = me.value;
	if (num) {
		var exp = /^\d*\.?\d{1,2}$/;
		if (!exp.test(num)) {
			alert("资金(人民币/万元)填写不正确，请填写大于等于零(保留两位小数)");
			me.value = "";
			me.focus();
		}
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
                                <li>人才办</li>
                                <li class="active"><a href="<c:url value="/talentoffice/companylist" />">认证企业管理</a></li>
                                <li class="active"><a href="javascript:void(0)">新增修改企业信息</a></li>
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
                               		<form class="form-horizontal" role="form">
                               		<div class="row">
                               			<div class="col-sm-12" class="over-hidden">
                               				<h5 class="over-hidden" style='color:black'>单位申请配合：</h5>
                               			</div>
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*是否可申请</span></label>
                               					<div class="col-sm-1">
                               						<input id="isApplication" type="checkbox" />
                               					</div>
                               				</div>
                               			</div>
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*可申请人数</span></label>
                               					<div class="col-sm-9">
                               						<input id="applicationNum" class="form-control" type="text" placeholder="必填">
                               					</div>
                               				</div>
                               			</div>
                               		</div>
                               		<div style="border-top:1px solid #000;width:100%;height:1px;"> </div>
                               		<div class="row">
                               			<div class="col-sm-12">
                               				<h5 class="over-hidden" style='color:black'>单位基本信息：</h5>
                               			</div>
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>申请单位（全称）</label>
                               					<div class="col-sm-9">
                               						<input id="applicant" class="form-control" type="text" placeholder="必填"/>
                               					</div>
                               				</div>
                               			</div>
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>企业信用代码</label>
                               					<div class="col-sm-9">
                               						<input id="unifiedSocialCreditCode" class="form-control" type="text" placeholder="必填">
                               						<input id="photoid" type="hidden" />
                               					</div>
                               				</div>
                               			</div>
                               			
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>注册地址</label>
                               					<div class="col-sm-9">
                               						<input id="registerAddress" class="form-control" type="text" placeholder="必填">
                               					</div>
                               				</div>
                               			</div>
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>营业或办公地址</label>
                               					<div class="col-sm-9">
                               						<input id="officeAddress" class="form-control" type="text" placeholder="必填"/>
                               					</div>
                               				</div>
                               			</div>
                               			
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>注册资本金(人民币/万元)</label>
                               					<div class="col-sm-9">
                               						<input id="registerMoney" class="form-control" onblur="moneyFun(this)" type="text" placeholder="必填">
                               					</div>
                               				</div>
                               			</div>
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>上一年度纳税金额(人民币/万元)</label>
                               					<div class="col-sm-9">
                               						<input id="oneYearIsTaxAmount" onblur="moneyFun(this)" class="form-control" type="text" placeholder="必填"/>
                               					</div>
                               				</div>
                               			</div>
                               			
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>在职员工人数</label>
                               					<div class="col-sm-9">
                               						<input id="staffNum" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')" type="text" placeholder="必填" />
                               					</div>
                               				</div>
                               			</div>
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>单位承担租金的百分比</label>
                               					<div class="col-sm-9">
                               						<span id="rent"></span>
                               					</div>
                               				</div>
                               			</div>
                               			
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*所属委办</span></label>
                               					<div class="col-sm-9">
                               						<input id="companyClassification" class="form-control" type="text" />
                               					</div>
                               				</div>
                               			</div>
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>所选小区</label>
                               					<div class="col-sm-9">
                               						<span id="pickDwelling"></span>
                               					</div>
                               				</div>
                               			</div>
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>企业性质</label>
                               					<div class="col-sm-9">
                               						<span id="companyNature"></span>
                               					</div>
                               				</div>
                               			</div>
                               		</div>
                               		<div style="border-top:1px solid #000;width:100%;height:1px;"> </div>
                               		<div class="row">
                               			<div class="col-sm-12">
                               				<h5 class="over-hidden" style='color:black'>单位申请公租房工作专职人员信息：</h5>
                               			</div>
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>专职联系人</label>
                               					<div class="col-sm-9">
                               						<input id="linkman" class="form-control" type="text" placeholder="必填"/>
                               					</div>
                               				</div>
                               			</div>
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>联系人手机</label>
                               					<div class="col-sm-9">
                               						<input id="phone" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')" type="text" placeholder="必填">
                               					</div>
                               				</div>
                               			</div>
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>联系人电子邮箱</label>
                               					<div class="col-sm-9">
                               						<input id="email" class="form-control" type="text" placeholder="必填"/>
                               					</div>
                               				</div>
                               			</div>
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1"><span style='color:red;'>*</span>联系人电话</label>
                               					<div class="col-sm-9">
                               						<input id="tel" class="form-control" type="text" placeholder="必填">
                               					</div>
                               				</div>
                               			</div>
                               		</div>
                               		<div class="row">
                               			<div class="col-sm-6">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1">申请单位（需求说明）</label>
                               					<div class="col-sm-9">
                               						<textarea id="applicationCompany" class="form-control"></textarea>
                               					</div>
                               				</div>
                               			</div>
                               		</div>
                               		<div class="text-center">
                               			<a class="btn btn-info margin-right-10" onclick="addCompany()">保存</a>
                               			<input type="hidden" id="code" />
                               			<input type="hidden" id="userid" />
                               		</div> 
									</form>
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