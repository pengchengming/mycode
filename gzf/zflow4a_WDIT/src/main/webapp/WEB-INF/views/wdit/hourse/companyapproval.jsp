<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>房管局-材料预审列表</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>


<script type="text/javascript">
var requreid = ${requreid};
var companyPhotoId;
var photoType;
var selstatus;
var restatus = 0;
var companyId;
var companyNature;
var createTable = new createTable();
var roles = "${sessionScope.ROLES}";


$(function(){
	$("#companyClassification_id").append(getDataDictionaryValueHtml(150));
	$("#rent_id").append(getDataDictionaryValueHtml(10));
	$("#select_status").append(getDataDictionaryValueHtml(70));
	getCompany(requreid);
	getpeople(requreid);
	$("#select_status").change(function(){
		if(selstatus != $("#select_status").val()){
			$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);
		}else{
			$("#bt_sure").attr("class","btn btn-default").attr("disabled", true);
		}
			
	})
	$("#tab_company input").change(function(){
		restatus = 1;
		$("#bt_save").attr("class","btn btn-info margin-right-10").attr("disabled", false);
		$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);
	})
	$("#tab_company select").change(function(){
		restatus = 1;
		$("#bt_save").attr("class","btn btn-info margin-right-10").attr("disabled", false);
		$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);
	})
	$("#tab_company textarea").change(function(){
		restatus = 1;
		$("#bt_save").attr("class","btn btn-info margin-right-10").attr("disabled", false);
		$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);
	})
	ReturnRemarkList();
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
						dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
	        		});
			   }
			}});
	   return dictionaryValueHtml;
}



var layerindex;
function showPhoto(photo){
	var photoHtml='<div><img src="'+photo+'" height="500" alt=""></div>';
	layerindex= layer.open({
        type: 1,
        area: ['720px', '600px'],
        skin: 'layui-layer-rim', //加上边框
        content: photoHtml
    });
}


var subarray = new Array();
function getCompany(requreid){
	var  byIdconfig={
			fromConfig:[{
				formCode:"WDIT_Company_Request_Photo",
				currentField:"request_id",
				parentId:"id",
				aliasesName:"companyPhoto",
				fieldName:"id,taxRegistersCardNumber,businessLicense,registrationNumber,unifiedSocialCreditCode,photoType,businessLicenseSmallPhoto,organizationPhoto,organizationSmallPhoto,taxationPhoto,taxationSmallPhoto,licenseNumber"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"companyNature",
				aliasesName:"companyNaturename",
				fieldName:"id,displayValue"
			}]
	}
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request",
			condition: "id="+requreid,
			byIdconfig:JSON.stringify(byIdconfig)
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;  
			if(results&&results.length>0){
				var company=results[0];
				var  isApplication=company.isApplication;//是否可申请
				var  applicant=company.applicant;//申请单位（全称）
				var  registerAddress=company.registerAddress;//注册地址
				var  officeAddress=company.officeAddress;//营业或办公地址
				var  oneYearIsTaxAmount=toDecimal2(company.oneYearIsTaxAmount);//上一年度纳税金额
				var  staffNum=company.staffNum;//在职员工人数
				var companyClassification = company.companyClassification;
                companyNature = company.companyNature;//企业性质
                var companyNaturename
                if(companyNature && company.companyNaturename[0].displayValue)
                  companyNaturename = company.companyNaturename[0].displayValue;
				var  rent=company.rent;//单位承担租金的百分比
				var  linkman=company.linkman;//专职联系人
				var  phone=company.phone;//联系人手机
				var  email=company.email;//电子邮箱
				var  tel=company.tel;//联系人电话
				companyId = company.companyId;//企业id
				$("#companyId").val(companyId);
				var  applicationCompany=company.applicationCompany;//申请单位（需求说明）

				$("#applicant_id").val(applicant);
				$("#registerAddress_id").val(registerAddress);
				$("#officeAddress_id").val(officeAddress);
				$("#oneYearIsTaxAmount_id").val(oneYearIsTaxAmount);
				$("#staffNum_id").val(staffNum);
				$("#companyClassification_id").val(companyClassification);
				$("#companyClassification_id").attr("disabled", true);
				if(companyNature)
						$("#td_companyNature").html(companyNaturename);
				$("#rent_id").val(rent);
				$("#rent_id").attr("disabled", true);
				$("#linkman_id").val(linkman);
				$("#phone_id").val(phone);
				$("#email_id").val(email);
				$("#tel_id").val(tel);
				$("#applicationCompany_id").val(applicationCompany);
				
				var companyPhotos=company.companyPhoto;
				if(companyPhotos&&companyPhotos.length>0){
					$.each(companyPhotos,function(i,companyPhoto){
						companyPhotoId=companyPhoto.id;
						photoType=companyPhoto.photoType;//照片类型
						$("#unifiedSocialCreditCode_id").val(companyPhoto.unifiedSocialCreditCode);//企业信用代码
						if(photoType){
							if(companyNature + ""== "1701"||companyNature + ""== "1702"){
								$("#businessLicense_id").parent().css('display' ,'');
								var businessLicense=companyPhoto.businessLicense;//营业执照
								var businessLicenseSmallPhoto=companyPhoto.businessLicenseSmallPhoto;
								if(businessLicense){
									$("#businessLicense_id").html('<a onClick="showPhoto(\''+rootPath+businessLicense+'\')"><img src="'+rootPath+businessLicenseSmallPhoto+'"  style="margin-left: 30px;height: 180px;" alt=""></a>');
								}
							}
							var subobj = new Object();
							subobj.formId = 68;
							subobj.tableDataId = companyPhoto.id;
							subarray.push(subobj);
							
							if(photoType+""=="1"){
								var  licenseNumber=companyPhoto.licenseNumber;//证照编号
								$("#licenseNumber_id").parent().parent().css('display' ,'');
								$("#licenseNumber_id").val(licenseNumber);
								$("#businessLicense_id").prev().html('营业照（三证合一）');
							}else{
								var  registrationNumber=companyPhoto.registrationNumber;//注册号
								$("#registrationNumber_id").parent().parent().css('display' ,'');
								$("#registrationNumber_id").val(registrationNumber);
								var  taxRegistersCardNumber=companyPhoto.taxRegistersCardNumber;//税务登记证号码
								
								if(companyNature + ""== "1701" ){
									$("#taxRegistersCardNumber_id").parent().parent().css('display' ,'');
									$("#taxRegistersCardNumber_id").val(taxRegistersCardNumber);
								}
								var organizationPhoto=companyPhoto.organizationPhoto;//组织机构照片
								var organizationSmallPhoto=companyPhoto.organizationSmallPhoto;
								var taxationPhoto=companyPhoto.taxationPhoto;//税务登记证
								var taxationSmallPhoto=companyPhoto.taxationSmallPhoto;
								if(organizationPhoto)
									$("#businessLicense_id").append('<a onClick="showPhoto(\''+rootPath+organizationPhoto+'\')"><img src="'+rootPath+organizationSmallPhoto+'"  style="margin-left: 30px;height:180px;"   alt=""></a>');
								if(taxationPhoto && companyNature + ""== "1701") 
									$("#businessLicense_id").append('<a onClick="showPhoto(\''+rootPath+taxationPhoto+'\')"><img src="'+rootPath+taxationSmallPhoto+'" style="margin-left: 30px;height:180px;"  alt=""></a>');
							}
					}
					});
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

function getRequestUserList(){
	getpeople(requreid);
}
function getpeople(requreid){
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
				title : '共同申请人',
				data : '',
				type : 'cutsomerRender',
				doRender : function(data, container,config, rowIndex){
					var userRelatives=data.userRelatives;
					var relativeHtml="";
					$.each(userRelatives,function(i,userRelative){
						var relative="";
						var displayValues=userRelative.relativename[0];
						if(displayValues){
							relative=displayValues.displayValue;
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
					var housingConditionsInTheCity=data.housingConditionsInTheCityname[0];
					if(housingConditionsInTheCity)
						$(container).append(housingConditionsInTheCity.displayValue);
				}
			},{
				title:'申请状态',
				data : '',
				type : 'cutsomerRender',
				doRender : function(data, container,config, rowIndex){
					var status = data.status; 
					statusname = "";
					if(status == 1201||status ==1204 || status == "")
						statusname = "待审核";
					else if((status >1203 && status !=1204) || status == 1202)
						statusname = "已通过";
					else if(status == 1203)
						statusname = "不通过";
					$(container).append(statusname);
				}
			},{
				title : '操作',
				data : '',
				type : 'cutsomerRender',
				doRender : function(data, container,config, rowIndex){
					var status = data.status; 
					var a = document.createElement('a');
					$(a).html('&nbsp;查看&nbsp;');
					if(status == 1201||status == 1204 || status == ""){
						$(a).html('&nbsp;审核&nbsp;');
					}
					$(a).attr('href', rootPath + '/hmcheck/userapproval?requestUserId='+data.id+'&requreid='+requreid+'&status='+status).attr("target","_blank");
//                     $(a).attr('href', rootPath + '/hmcheck/userapproval?requestUserId='+data.id);
					$(container).append(a);
				}
			}]
		}; 
	var  byIdconfig2={
			fromConfig:[{
				formCode:"WDIT_Company_Request_User_relative",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"userRelatives",
				fieldName:"name,relative",
				fromConfig:[{
					formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"relative",
					aliasesName:"relativename",
					fieldName:"displayValue"	
				}]
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"housingConditionsInTheCity",
				aliasesName:"housingConditionsInTheCityname",
				fieldName:"displayValue"	
			}]
	}
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request_User",
			condition: "request_id="+requreid,
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


function saveCompanyFun(){
	var applicant=$("#applicant_id").val();
	if(!applicant){
		alert("请填写公司名称 ");
		return false;
	}else if(applicant.length>=50){
		alert("公司名称  超出长度规范！ ");
		return false;
	}	 
	
	if(!companyNature){
		alert("请选择企业性质");
		return false;
	}
	
	var unifiedSocialCreditCode=$("#unifiedSocialCreditCode_id").val();
	if(!unifiedSocialCreditCode){
		alert("请填写企业信用代码 ");
		return false;
	}else if(unifiedSocialCreditCode.length>=100){
		alert("企业信用代码  超出长度规范！ ");
		return false;
	}
	var registerAddress=$("#registerAddress_id").val();
	if(!registerAddress){
		alert("请填写 注册地址 ");
		return false;
	}else if(registerAddress.length>=200){
		alert("注册地址  超出长度规范！ ");
		return false;
	}
	var officeAddress=$("#officeAddress_id").val();
	if(!officeAddress){
		alert("请填写 营业或办公地址  ");
		return false;
	}else if(officeAddress.length>=200){
		alert("营业或办公地址  超出长度规范！ ");
		return false;
	}
	var oneYearIsTaxAmount=$("#oneYearIsTaxAmount_id").val();
	oneYearIsTaxAmount=toDecimal2(oneYearIsTaxAmount);
	if(!oneYearIsTaxAmount){
		alert("请填写 上一年度纳税金额  ");
		return false;
	}else if(oneYearIsTaxAmount.length>11){
		alert("上一年度纳税金额 超出金额上限！ ");
		$("#oneYearIsTaxAmount_id").val("");
		return false;
	}
	var companyClassification=$("#companyClassification_id").val();
	if(!companyClassification){
		alert("请选择所属委办类型");
		return false;
	}
	var staffNum=$("#staffNum_id").val();
	if(!staffNum){
		alert("请填写 在职员工人数  ");
		return false;
	}else if(staffNum.length>8){
		alert("在职员工人数 超出金额上限！ ");
		return false;
	}
 	var rent=$("#rent_id").val(); //单位承担租金的百分比
 	var linkman=$("#linkman_id").val();
	if(!linkman){
		alert("请填写 专职联系人  ");
		return false;
	}else if(linkman.length>=50){
		alert("专职联系人 超出长度规范！ ");
		return false;
	}	
	var phone=$("#phone_id").val();
	if(!phone){
		alert("请填写 联系人手机  ");
		return false;
	}else if(phone.length>=50){
		alert("联系人手机 超出长度规范！ ");
		return false;
	}
	var email=$("#email_id").val();
	if(!email){
		alert("请填写 电子邮箱  ");
		return false;
	}else if(email.length>=50){
		alert("电子邮箱 超出长度规范！ ");
		return false;
	}
	var tel=$("#tel_id").val();
	if(!tel){
		alert("请填写 联系人电话  ");
		return false;
	}else if(tel.length>=50){
		alert("联系人电话  超出长度规范！ ");
		return false;
	}
	var applicationCompany=$("#applicationCompany_id").val();
	 if(applicationCompany.length>250){
		alert("需求说明  超出长度规范！ ");
		return false;
	}
	var registrationNumber;
	var taxRegistersCardNumber;
	var licenseNumber;
	var resubarray = new Array();
	
	if(photoType+""=="1"){
		licenseNumber = $("#licenseNumber_id").val();
		if(!licenseNumber){
				alert("请填写 证书编号 ");
				return false;
			} 
		if(!licenseNumber && licenseNumber.length>=100){
				alert("证书编号  超出长度规范！ ");
				return false;
		 } 
		$.each(subarray,function(i,subobj){
			subobj.register = {"licenseNumber":licenseNumber,"unifiedSocialCreditCode":unifiedSocialCreditCode};
			resubarray.push(JSON.stringify(subobj));
		})
	}else if(photoType+""=="2"){
		    registrationNumber = $("#registrationNumber_id").val();
		    if( companyNature+"" != "1703"){
			    if(!registrationNumber){
					alert("请填写 工商登记号  ");
					return false;
				}
			    if(!registrationNumber && registrationNumber.length>=100){
					alert("工商登记号  超出长度规范！ ");
					return false;
				} 
		    }
		    if(companyNature+"" != "1702" && companyNature+"" != "1703"){
		    	 taxRegistersCardNumber = $("#taxRegistersCardNumber_id").val();
				    if(!taxRegistersCardNumber){
						alert("请填写 税务登记号");
						return false;
					}
				    if(!taxRegistersCardNumber && taxRegistersCardNumber.length>=100){
						alert("税务登记号  超出长度规范！ ");
						return false;
					}
		    }
		   
		    $.each(subarray,function(i,subobj){
		    	if(companyNature+"" != "1702" && companyNature+"" != "1703")
		    	    subobj.register = {"registrationNumber":registrationNumber,"taxRegistersCardNumber":taxRegistersCardNumber,"unifiedSocialCreditCode":unifiedSocialCreditCode};
		    	else if(companyNature+"" == "1702")
		    		subobj.register = {"registrationNumber":registrationNumber,"unifiedSocialCreditCode":unifiedSocialCreditCode};
		    	else if(companyNature+"" == "1703")
		    		subobj.register = {"registrationNumber":registrationNumber};	
		    	resubarray.push(JSON.stringify(subobj));
			})
		    
	 }
	var register={
		"applicant":applicant,
		"registerAddress":registerAddress,
		"officeAddress":officeAddress,
		"oneYearIsTaxAmount":oneYearIsTaxAmount,
		"staffNum":staffNum,
		"linkman":linkman,
		"phone":phone,
		"email":email,
		"tel":tel,
		"rent":rent,
		"applicationCompany":applicationCompany,
		"companyClassification":companyClassification
	}	
	
	var jsonString={
			"formId":61,
			"tableDataId":requreid,
			"register":register
		};
	resubarray.push(JSON.stringify(jsonString));
	 $.ajax({
			url : rootPath+'/forms/saveFormDataJsons',
			type : "POST", 
			async: false,
			data : {
				jsons : resubarray  
			}, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
					if(data.code+""=="1"){
						alert("修改成功");
						restatus = 0;
						$("#bt_save").attr("class","btn btn-default").attr("disabled", true);
						if($("#bt_sure").attr("class")=="btn btn-default"){
							$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);	
						}	
					}else {
						alert(data.errorMsg);
					}	
			}});
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

function sureCompanyFun(){
	if(restatus == 1){
		alert("请先保存修改");
		return;
	}
	$("#bt_sure").attr("class","btn btn-default").attr("disabled", true);;
	selstatus = $("#select_status").val();
}

function approveCompanyFun(status){
	if(roles.indexOf("HOUSEMANAGE_ROLE,")>=0){
		if($("#bt_sure").attr("class")=="btn btn-info margin-right-10"){
			alert("清先材料确认");
			return;
		}
		selstatus = $("#select_status").val();
		if(selstatus == 701 && status == 103){
			alert("当前预审结果通过，请点击预审通过！")
			return false;
		}
		if(selstatus != 701 && status == 104){
			alert("当前预审结果不通过，请点击预审不通过！")
			return false;
		}

		$.ajax({
			url : '<c:url value="/data/procedure"/>',
			type : "POST", 
		    async: false,
		    data : {
		    	p : 'R2014006|'+requreid + '|11'
		    }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data&&data.length>0){
					var obj = data[0];
					if(obj.checkcode && obj.checkcode == 1){
						var index = layer.load(2);
						var jsons = new Array();
						var approval_Date = obj.currenttime;
						var approvstatus;
						if(status==103)  //不通过
							approvstatus = 1103;//不通过
						else if(status==104)  //审核中
							approvstatus = 1102;//已通过
						
						var requestjson={
								"formId":61,
								"tableDataId":requreid,
								"register":{"status":status}
							};
						jsons.push(JSON.stringify(requestjson));	
						
						var approvaljson ={
								"formId":66,
								"register":{"status":approvstatus,"approvalStep":1,"companyId":companyId,"approval_Date":approval_Date,"request_Id":requreid}
						}
						jsons.push(JSON.stringify(approvaljson));
						if(status==103){
							$.ajax({
								url : rootPath+'/forms/getDataByFormId',
								type : "POST",
						        async: false,
								data:{
									formCode:"wdit_company_request_user",
									condition: " status in(1201,1204) and  request_id="+requreid
								},
								complete : function(xhr, textStatus){
									var data = JSON.parse(xhr.responseText);
									var results = data.results;
									if(results&&results.length>0){
										$.each(results,function(i,obj){ 
											var userrequsetobj=new Object();
											userrequsetobj["formId"]= 62;
											userrequsetobj["tableDataId"] = obj.id;
											userrequsetobj["register"] = {"status":1203};
											jsons.push(JSON.stringify(userrequsetobj));
											var adduserapprovalobj=new Object();
											adduserapprovalobj["formId"]= 67;
											adduserapprovalobj["register"] = 
												{"status":1203,"approvalStep":1,
												"requestUser_id":obj.id,"approval_Date":approval_Date};
											jsons.push(JSON.stringify(adduserapprovalobj));
										});
									}}})
							  var linkman = $("#linkman_id").val();
				              var email = $("#email_id").val();	
							  if(linkman&&email){
								 var emailobj = new Object();
				            	 var content = linkman + " 您好：贵公司提交的公租房申请表在  资料预审  的审批中被审批拒绝。";
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
									alert("审核成功");
									layer.close(index);
									if(status==103){
// 										updateUserUnpass(obj.currenttime);
										window.location.href=rootPath+"/hmcheck/prelist";
									}else if(status==104){
										window.location.href=rootPath+"/hmcheck/companyapproval?requreid="+requreid+"&companystutus=104";
									}
								}else {
									alert(data.errorMsg);
								}	
						}});
					}else{
						alert("无需重复审批");
					}   
				}
		   }
		});
	 }
	else{
		alert("请切换房管局角色");
	}
}

// function updateUserUnpass(Date){
// 	$.ajax({
// 		url : rootPath+'/forms/getDataByFormId',
// 		type : "POST",
//         async: false,
// 		data:{
// 			formCode:"wdit_company_request_user",
// 			condition: " status in(1201,1204) and  request_id="+requreid
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
// 					userrequsetobj["register"] = {"status":1203};
// 					jsons.push(JSON.stringify(userrequsetobj));
// 					var adduserapprovalobj=new Object();
// 					adduserapprovalobj["formId"]= 67;
// 					adduserapprovalobj["register"] = 
// 						{"status":1203,"approvalStep":1,
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

function ReturnRemarkList(){
	$.post(rootPath+'/createSelect/findselectData', { 
		code : "returnremark_v1",
		selectConditionSql : " where status in(11041,21041) and request_id="+requreid
	},function(data){
			var results =eval("(" + data.results + ")");
			if(results &&results.length>0){
				$.each(results,function(i,json){
					var returnRemark=json.returnRemark;
					if(!returnRemark || returnRemark.length<0){
						returnRemark='退回';
					}
					var approval_Date="";
					if(json.modifyDate){
						try{
							approval_Date=new Date(parseInt(json.modifyDate)*1000).format('yyyy-MM-dd hh:mm')
						}catch (e) {}
					}
					var  trhtml="<tr><td class='text-right' style='color:red' width='300'>"+json.approvalStep2+"-"+json.realname+"-退回"+json.approvalStep+" 备注：</td>" +
					"<td class='text-left' style='color:green' >"+returnRemark+"</td>"+
					"<td class='text-right' style='color:green' width='210px'>退回时间："+approval_Date+"</td></tr>"
					$("#showreturnremark").append(trhtml);
				})
			}
	}); 
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
		return false;
	}
	 $.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"WDIT_Company_Request",
				condition: "id="+requreid
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.code + "" == "1"){
					var requestinfo = data.results[0];
					 var jsons = new Array();
					 var adddataobj = new Object();
	                 adddataobj["formId"]= 66;
	                 adddataobj["register"] = {"status":11041,"returnRemark":returnRemark,"isReturn":1,"approval_Date":requestinfo.nowDate,"approvalStep":1,"request_Id":requreid,"companyId":requestinfo.companyId};
	                 jsons.push(JSON.stringify(adddataobj));
	            	 var comrequestobj = new Object();
	            	 comrequestobj["formId"]= 61;
	            	 comrequestobj["tableDataId"] = requreid;
	            	 comrequestobj["register"] = {"status":0,"isReturn":1};
	            	 jsons.push(JSON.stringify(comrequestobj));
	            	 var infoobj = new Object();
	            	 infoobj["formId"]= 94;
	            	 infoobj["register"] = {"request_id":requreid,"isGenerate":0};
	            	 jsons.push(JSON.stringify(infoobj));
	            	 var emailobj = new Object();
	            	 emailobj["formId"]= 99;
	            	 var content = requestinfo.linkman + " 您好：贵公司提交的公租房申请表因  "+ returnRemark+" 原因被退回，请再次编辑本条申请信息提交";
	            	 emailobj["register"] = {"email":requestinfo.email,"content":content,"type":1,"flag":0};// 1为发给公司
	            	 jsons.push(JSON.stringify(emailobj));
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
	             						window.location.href=rootPath+"/hmcheck/prelist";
	             				 	}else {
	             						alert(data.errorMsg);
	             				}	
	             			}});	 
	                 }else{
	            			alert("jsons太短");
	            		}
	            }else{
	            	alert("查不到公司信息");
	            }
				}})
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
                                <li>房管局/材料预审</li>
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
                                                <table class="table table-bordered table-hover text-center table-horizontal" id="tab_company">
                                                    <tbody>
                                                        <tr>
                                                            <th class="text-right" width="200"><span style="color:#FF0000;">*</span>公司名称</th>
                                                            <td class="text-left">
                                                                 <input type="text" placeholder="必填" id = "applicant_id" class="form-control">
                                                                 <input type="hidden" id="companyId"/>
                                                            </td>
                                                            
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>企业信用代码</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id=  "unifiedSocialCreditCode_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>注册地址</th>
                                                            <td>
                                                                <input type="text" placeholder="必填" id="registerAddress_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>营业或办公地址</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id="officeAddress_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr style="display: none">
                                                            <th class="text-right" ><span style="color:#FF0000;">*</span>证书编号</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id="licenseNumber_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr style="display: none">
                                                            <th class="text-right" ><span style="color:#FF0000;">*</span>工商登记号</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id="registrationNumber_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr style="display: none">
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>税务登记号</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id="taxRegistersCardNumber_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>上一年度纳税金额(人民币/万元)</th>
                                                            <td colspan="3" class="text-left">
                                                               <input onblur="moneyFun(this)" type="text" placeholder="必填" id="oneYearIsTaxAmount_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>所属委办</th>
                                                            <td colspan="3" class="text-left">
                                                               <select class="form-control"  id="companyClassification_id" style="height: 40px;width: 400px;"></select>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>在职员工人数</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id="staffNum_id" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right">单位承担租金的百分比</th>
                                                            <td colspan="3" class="text-left">
                                                                <select class="form-control"  id="rent_id" style="height: 40px;width: 400px;">
		                                                        </select>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>企业性质</th>
                                                            <td colspan="3" class="text-left" id = "td_companyNature">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>申请公租房工作专职联系人</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id="linkman_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>联系人手机</th>
                                                            <td class="text-left">
                                                               <input type="text" placeholder="必填" id="phone_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>联系人电子邮箱</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id="email_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right"><span style="color:#FF0000;">*</span>联系人电话</th>
                                                            <td colspan="3" class="text-left">
                                                               <input type="text" placeholder="必填" id="tel_id" class="form-control">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="text-right">需求说明</th>
                                                            <td colspan="3" class="text-left">
                                                                <textarea name="txtarea" id="applicationCompany_id" class="form-control" rows="5"> </textarea>
                                                            </td>
                                                        </tr> 
                                                        <tr style="display: none">
                                                            <th class="text-right" >营业照（非三证合一）</th>
                                                            <td colspan="3" class="text-left" id="businessLicense_id">
                                                            <img src="<c:url value="/wdit/assets/images/yyzz.jpg"/> " style="margin-left: 30px;height: 180px;" alt=""></td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <table id="showreturnremark">
											</table>
									        <div class="row">
                                                <div class="col-sm-5">
                                                    <div class="col-sm-5 text-right padding-5">预审结果：</div>
                                                    <div class="col-sm-7">
                                                        <select class="small-select2 form-control" style="height: 40px;" id = "select_status">
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-sm-7">
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="saveCompanyFun()" id="bt_save">保存修改</a>
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="sureCompanyFun()" id="bt_sure">材料确认</a>
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="approveCompanyFun(104)">预审通过</a>
                                                    <a class="btn btn-default" href="javascript:void(0)" onclick="approveCompanyFun(103)">预审不通过</a>
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="openlayer()">退回申请</a>
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