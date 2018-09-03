<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>企业申请</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/script/jquery/jquery.form.js" />"></script>
 
<script type="text/javascript">
var companyId = '${sessionScope.USER.companyId}';
var requestId='${requestId}';
var requestPhotoId;
var companyPhotoId;

 $(function(){
	 initPage();
 });
 var isRequest=false;
// 	$.ajax({
// 		url : rootPath+'/forms/getDataByFormId',
// 		type : "POST",
//      async: false,
// 		data:{
// 			formCode:"wdit_company_request",
// 			condition: "companyId="+companyId
// 		},
// 		complete : function(xhr, textStatus){
// 			var data = JSON.parse(xhr.responseText);
// 			var results = data.results;  
// 			if(results&&results.length>0){
// 				var requestCompany=results[0];
// 				var status=requestCompany.status;//编号 
// 				if(!status||status+""!="0"){
// 					alert("不可重复申请");
// 					window.location.href=rootPath+"/companyRequest/requestList";
// 				}
// 			}
// 		}
// 	});
	

 function initPage(){
	 $("input[name='radioTab'][value=1]").prop("checked",true);
	 $(".radio-tab-nav :radio").click(function() {
		 var num = $(this).val();
		 $(".radioTab>div").hide();
		 $(".radioTab>div").eq(num-1).show();
	 });
	 //格式化上传照片
	 var formhtml = upLoadFormHtmlFun("businessLicense");
	 $("#businessLicense_id").html(formhtml);
	 var formhtml = upLoadFormHtmlFun("businessLicense2");
	 $("#businessLicense2_id").html(formhtml);
	 var formhtml = upLoadFormHtmlFun("organizationPhoto");
	 $("#organizationPhoto_id").html(formhtml);
	 var formhtml = upLoadFormHtmlFun("taxationPhoto");
	 $("#taxationPhoto_id").html(formhtml);
	 var array=new Array();
	 array.push("businessLicense");
	 array.push("businessLicense2");
	 array.push("organizationPhoto");
	 array.push("taxationPhoto");
	 uploadFormInit(array);

	 initDictionaryCodeFun();
	 if(requestId)
		 getRequestId(requestId);	 
	 else 
		 getCompany(companyId);
	getCompanyNature();
	 
 };
 var layerindex;
 function showPhoto(photo){
 	var photoHtml='<div><img src="'+photo+'" height="500" alt=""></div>';
 	layerindex= layer.open({
         type: 1,
         title:"原图",
         area: ['820px', '700px'],
         skin: 'layui-layer-rim', //加上边框
         content: photoHtml
     });
 }
 function initDictionaryCodeFun(){
	//单位承担租金的百分比 
	$("#rent_id").html("");
	var  rents=dictionaryCodeFun(1);	
	if(rents&&rents.length>0){
		$.each(rents,function(i,datadictionaryValue){
			$("#rent_id").append('<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>');
		});
	}
	//所选小区
	$("#pickDwelling_id").html("");
	var  pickDwellings=dictionaryCodeFun(16);	
	if(pickDwellings&&pickDwellings.length>0){
		$.each(pickDwellings,function(i,datadictionaryValue){
			$("#pickDwelling_id").append('<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>');
		});
	}
	//企业性质
	$("#companyNature_id").html("");
	var  companyNatures=dictionaryCodeFun(17);	
	if(companyNatures&&companyNatures.length>0){
		$.each(companyNatures,function(i,datadictionaryValue){
			$("#companyNature_id").append('<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>');
		});
	}
 }
 function dictionaryCodeFun(codeId){
 	var results ;
 	$.ajax({
 		url : rootPath+'/forms/getDataByFormId',
 		type : "POST",
         async: false,
 		data:{
 			formCode:"sys_datadictionary_value",
 			condition: " dataDictionaryCode_id="+codeId
 		},
 		complete : function(xhr, textStatus){
 			var data = JSON.parse(xhr.responseText);
 			if(data.code + "" == "1"){
 				results = data.results;
 			} 
 		}
 	}); 
 	return results ;
 }
 
 function  getRequestId(requestId){
	//获取申请
	 var  byIdconfig2={
				fromConfig:[{
					formCode:"wdit_company_request_photo",
					currentField:"request_id",
					parentId:"id",
					aliasesName:"companyPhoto",
					fieldName:"id,organizationSmallPhoto,organizationPhoto,taxRegistersCardNumber,taxationPhotoName,taxationSmallPhoto,taxationPhoto,authorizationName,authorizationSmallPhoto,authorization,organizationPhotoName,organization,businessLicense,registrationNumber,businessLicenseSmallPhoto,businessLicenseName,unifiedSocialCreditCode,licenseNumber,photoType"
				}]
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
					requestId=requestCompany.id;//编号
					requestPhotoId=requestCompany.companyPhoto[0].id;
					getCompanyPhoto();
					setCompany(requestCompany);
				}
			}
		}); 
 }
 function getCompanyPhoto(){
	 $.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"WDIT_Company_Photo",
				condition: "companyId="+companyId
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;  
				if(results&&results.length>0){						
					var companyPhoto=results[0];
					companyPhotoId=companyPhoto.id;
				}
			}
		}); 
 }
 
 function uploadFormInit(array){
	 $.each(array,function (i,id){
		 uploadForm(id);
	 }) 
	}
 function uploadForm(id){
	 $("#"+id+"_form_id").ajaxForm({
			dataType : 'text',
			success : function(json) {
				if (json.indexOf("pre") > 0|| json.indexOf("PRE") > 0) {
					json = json.substring(5);
					json = json.substring(0, json.length - 6);
				}
				var data = JSON.parse(json);
				if (data.code + "" == "1") {
					var str="<input type='hidden' value='"+data.imageName+"' /><input type='hidden' value='"+data.imagePath+"'/><input type='hidden' value='"+data.smallImagePath+"'/><a onClick='showPhoto(\""+rootPath+data.imagePath+"\")'><img src='"+rootPath+data.smallImagePath+"'></a>&nbsp;<a href='javascript:void(0)' onclick='deletePhoto(\""+id+"\");'>删除</a>";
		        	$("#"+id+"_id").html(str);
				} else {
					alert("上传失败");
				}
			}
		});
 }
 
 function getCompany(companyId){
		var  byIdconfig={
				fromConfig:[{
					formCode:"WDIT_Company_Photo",
					currentField:"companyId",
					parentId:"id",
					aliasesName:"companyPhoto",
					fieldName:"id,organizationSmallPhoto,organizationPhoto,taxRegistersCardNumber,taxationPhotoName,taxationSmallPhoto,taxationPhoto,authorizationName,authorizationSmallPhoto,authorization,organizationPhotoName,organization,businessLicense,registrationNumber,businessLicenseSmallPhoto,businessLicenseName,unifiedSocialCreditCode,licenseNumber,photoType"
				}]
		}
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"WDIT_Company",
				condition: "id="+companyId,
				byIdconfig:JSON.stringify(byIdconfig)
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;  
				if(results&&results.length>0){
					var company=results[0];
					setCompany(company);
					var companyPhotos=company.companyPhoto;
					if(companyPhotos&&companyPhotos.length>0){
						companyPhoto=companyPhotos[0];
						companyPhotoId=companyPhoto.id;
					}
				}
			}
		});
 }
 
 function  setCompany(company){
	 var  code=company.code;//编号
		var  isApplication=company.isApplication;//是否可申请
		var  applicationNum=company.applicationNum;//可申请人数
		var  applicant=company.applicant;//申请单位（全称）
		var  registerAddress=company.registerAddress;//注册地址
		var  officeAddress=company.officeAddress;//营业或办公地址
		var  registerMoney=company.registerMoney;//注册资本金
		var  oneYearIsTaxAmount=company.oneYearIsTaxAmount;//上一年度纳税金额
		var  staffNum=company.staffNum;//在职员工人数
		var  rent=company.rent;//单位承担租金的百分比
		var  companyClassification=company.companyClassification;//企业分类
		var  linkman=company.linkman;//专职联系人
		var  phone=company.phone;//联系人手机
		var  email=company.email;//电子邮箱
		var  tel=company.tel;//联系人电话
		var  applicationCompany=company.applicationCompany;//申请单位（需求说明）
		var  pickDwelling=company.pickDwelling;//所选小区;
		var  companyNature=company.companyNature;
		$("#applicationNum_id").val(applicationNum);
		$("#applicant_id").val(applicant);
		$("#registerAddress_id").val(registerAddress);
		$("#officeAddress_id").val(officeAddress);
		if(registerMoney){
			registerMoney=toDecimal2(registerMoney);
		}
		if(oneYearIsTaxAmount){
			oneYearIsTaxAmount=toDecimal2(oneYearIsTaxAmount);
		}
		
		$("#registerMoney_id").val(registerMoney);
		$("#oneYearIsTaxAmount_id").val(oneYearIsTaxAmount);
		$("#staffNum_id").val(staffNum);
		if(!rent){
			$("#rent_id").val(101);
		}else{
			$("#rent_id").val(rent);
		}
// 		return false;
		$("#linkman_id").val(linkman);
		$("#phone_id").val(phone);
		$("#email_id").val(email);
		$("#tel_id").val(tel);
		$("#applicationCompany_id").val(applicationCompany);
		$("#companyClassification_id").val(companyClassification);
		$("#pickDwelling_id").val(pickDwelling);
		$("#companyNature_id").val(companyNature);
		var companyPhotos=company.companyPhoto;
		if(companyPhotos&&companyPhotos.length>0){
			$.each(companyPhotos,function(i,companyPhoto){
				var  photoType=companyPhoto.photoType;//照片类型
				var  unifiedSocialCreditCode=companyPhoto.unifiedSocialCreditCode;//企业信用代码
				$("#unifiedSocialCreditCode_id").val(unifiedSocialCreditCode);
				if(photoType){
					if(photoType+""=="1"){
						var  licenseNumber=companyPhoto.licenseNumber;//证照编号
						var  businessLicense=companyPhoto.businessLicense;//营业执照
						var businessLicenseName=companyPhoto.businessLicenseName;
						var businessLicenseSmallPhoto=companyPhoto.businessLicenseSmallPhoto;
						$("#licenseNumber_id").val(licenseNumber);
						if(businessLicense)
							$("#businessLicense_id").html('<input type="hidden" value="'+businessLicenseName+'" /><input type="hidden" value="'+businessLicense+'"/><input type="hidden" value="'+businessLicenseSmallPhoto+'"/> <a onClick="showPhoto(\''+rootPath+businessLicense+'\')"><img src="'+rootPath+businessLicenseSmallPhoto+'" height="180" alt=""><a href="javascript:void(0)" onclick="deletePhoto(\'businessLicense\');">删除</a>');									 
					}else {
						var  registrationNumber=companyPhoto.registrationNumber;//注册号
						var  businessLicense=companyPhoto.businessLicense;//营业执照
						var businessLicenseName=companyPhoto.businessLicenseName;
						var businessLicenseSmallPhoto=companyPhoto.businessLicenseSmallPhoto;
						//var  organization=companyPhoto.organization;//组织机构代码
						var  organization=companyPhoto.unifiedSocialCreditCode;//企业信用代码
						var  organizationPhoto=companyPhoto.organizationPhoto;//组织机构照片
						var organizationPhotoName=companyPhoto.organizationPhotoName;
						var organizationSmallPhoto=companyPhoto.organizationSmallPhoto;
						var  taxRegistersCardNumber=companyPhoto.taxRegistersCardNumber;//税务登记证号码
						var  taxationPhoto=companyPhoto.taxationPhoto;//税务登记证
						var taxationPhotoName=companyPhoto.taxationPhotoName;
						var taxationSmallPhoto=companyPhoto.taxationSmallPhoto;
						$("#registrationNumber_id").val(registrationNumber);
						$("#organization_id").val(organization);
						$("#taxRegistersCardNumber_id").val(taxRegistersCardNumber);
						if(organizationPhoto)
							$("#organizationPhoto_id").html('<input type="hidden" value="'+organizationPhotoName+'" /><input type="hidden" value="'+organizationPhoto+'"/><input type="hidden" value="'+organizationSmallPhoto+'"/><a onClick="showPhoto(\''+rootPath+organizationPhoto+'\')"> <img src="'+rootPath+organizationSmallPhoto+'" height="180" alt=""></a><a href="javascript:void(0)" onclick="deletePhoto(\'organizationPhoto\');">删除</a>');
						if(companyNature==1702){
							if(businessLicense)
								$("#businessLicense2_id").html('<input type="hidden" value="'+businessLicenseName+'" /><input type="hidden" value="'+businessLicense+'"/><input type="hidden" value="'+businessLicenseSmallPhoto+'"/> <a onClick="showPhoto(\''+rootPath+businessLicense+'\')"><img src="'+rootPath+businessLicenseSmallPhoto+'" height="180" alt=""></a><a href="javascript:void(0)" onclick="deletePhoto(\'businessLicense2\');">删除</a>');
						}else if(companyNature==1703){
						}else if(companyNature==1701){
							if(businessLicense)
								$("#businessLicense2_id").html('<input type="hidden" value="'+businessLicenseName+'" /><input type="hidden" value="'+businessLicense+'"/><input type="hidden" value="'+businessLicenseSmallPhoto+'"/> <a onClick="showPhoto(\''+rootPath+businessLicense+'\')"><img src="'+rootPath+businessLicenseSmallPhoto+'" height="180" alt=""></a><a href="javascript:void(0)" onclick="deletePhoto(\'businessLicense2\');">删除</a>');						
							if(taxationPhoto) 
								$("#taxationPhoto_id").html('<input type="hidden" value="'+taxationPhotoName+'" /><input type="hidden" value="'+taxationPhoto+'"/><input type="hidden" value="'+taxationSmallPhoto+'"/><a onClick="showPhoto(\''+rootPath+taxationPhoto+'\')"> <img src="'+rootPath+taxationSmallPhoto+'" height="180" alt=""></a><a  href="javascript:void(0)" onclick="deletePhoto(\'taxationPhoto\');">删除</a>');
						}
						
					}
				
				var photoType1=parseInt(photoType)-1;
				 $(".radioTab>div").hide();
				 $(".radioTab>div").eq(photoType1).show();
				$("input[name='radioTab'][value="+photoType+"]").prop("checked",true);
				$("#unifiedSocialCreditCode_id").val(companyPhoto.unifiedSocialCreditCode);
				}
				
			});
		}
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
 
 function deletePhoto(id){
	 $("#"+id+"_id").html("");
	 $("#"+id+"_id").append(upLoadFormHtmlFun(id)); 
	 uploadForm(id);
 }
 function upLoadFormHtmlFun(id){	 
     var formhtml = ' <form enctype="multipart/form-data" method="post"  id="'+id+'_form_id"  action="<c:url value="/fileUploadDownLoad/uploadImgeFile"/>" >'
		+'<input type="hidden"  name="force" value="1" /><input type="hidden"  name="w" value="128" /><input type="hidden"  name="h" value="96" />'
		+'<input type="file"  name="file" id="'+id+'_file_id" /> <input type="button" onclick="submitImageOnChange(\''+id+'\')" value="上传" ></form>';
     return  formhtml; 
 }
function submitImageOnChange(id){
	 var fileName = $("#"+id+"_file_id").val();
	 if(!fileName){
		 alert("请选择上传图片");
		 return false;
	 }

	 if (fileName != null && fileName.length > 0) {
	     var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase();
	     if (fileType == "BMP" || fileType== "WBMP" ||fileType == "JPEG"|| fileType== "PNG"|| 
	     		fileType== "TIF" || fileType== "GIF" ||fileType== "JPG" ) {
	    	 $("#"+id+"_form_id").submit();
	     } else {
	         alert("Please Upload BMP, WBMP, JPEG, PNG, TIF, GIF, JPG ！");
	         $("#largeAdvertImage").val("");
	     }
	 }
	 
}



function saveCompanyFun(){
	var applicant=$("#applicant_id").val();
	if(!applicant){
		alert("请填写 申请单位（全称） ");
		return false;
	}else if(applicant.length>=50){
		alert("申请单位（全称）超出长度规范！ ");
		return false;
	}	
	var registerAddress=$("#registerAddress_id").val();
	if(!registerAddress){
		alert("请填写 注册地址 ");
		return false;
	}else if(registerAddress.length>=200){
		alert("注册地址 超出长度规范！ ");
		return false;
	}	
	var officeAddress=$("#officeAddress_id").val();
	if(!officeAddress){
		alert("请填写 营业或办公地址  ");
		return false;
	}else if(officeAddress.length>=200){
		alert("营业或办公地址 超出长度规范！ ");
		return false;
	}	
	var registerMoney= $("#registerMoney_id").val();
	var oneYearIsTaxAmount=$("#oneYearIsTaxAmount_id").val();
	registerMoney=toDecimal2(registerMoney);
	oneYearIsTaxAmount=toDecimal2(oneYearIsTaxAmount);
	if(!registerMoney){
		alert("请填写 注册资本金  ");
		return false;
	}else if(registerMoney.length>11){
		alert("注册资本金 超出金额上限！ ");
		$("#registerMoney_id").val("");
		$("#registerMoney_id").focus();
		return false;
	}
	if(!oneYearIsTaxAmount){
		alert("请填写 上一年度纳税金额 ");
		return false;
	}else if(oneYearIsTaxAmount.length>11){
		alert("上一年度纳税金额 超出金额上限！ ");
		$("#oneYearIsTaxAmount_id").val("");
		$("#oneYearIsTaxAmount_id").focus();
		return false;
	}
	var applicationNum=$("#applicationNum_id").val();
	if(applicationNum.length>5){
		alert("可申请人数 超出人数上限！ ");
		return false;
	}
	var staffNum=$("#staffNum_id").val();
	if(!staffNum){
		alert("请填写 在职员工人数  ");
		return false;
	}else if(staffNum.length>8){
		alert("在职员工人数 超出人数上限！ ");
		return false;
	}
	var rent=$("#rent_id").val();
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
		alert("请填写 联系人电话");
		return false;
	}else if(tel.length>=50){
		alert("联系人电话  超出长度规范！ ");
		return false;
	}
	var pickDwelling=$("#pickDwelling_id").val();
	if(!pickDwelling){
		alert("请选择 所选小区  ");
		return false;
	}
	var companyNature=$("#companyNature_id").val();
	if(!companyNature){
		alert("请选择 企业性质  ");
		return false;
	}
	var applicationCompany=$("#applicationCompany_id").val();
	if(applicationCompany.length>250){
		alert("申请单位（需求说明）  超出长度规范！ ");
		return false;
	}
	var photoType = $('input:radio[name="radioTab"]:checked').val();
    if (photoType == null) {
        alert("没有选中项");
        return false;
    }
    if(photoType+""=="1"){
        var unifiedSocialCreditCode=$("#unifiedSocialCreditCode_id").val();
    	if(!unifiedSocialCreditCode){
    		alert("请填写 企业信用代码  ");
    		return false;
    	}else if(unifiedSocialCreditCode.length>=100){
    		alert("企业信用代码 超出长度规范！ ");
    		return false;
    	}
    	var licenseNumber=$("#licenseNumber_id").val();
    	if(!licenseNumber){
    		alert("请填写 证照编号  ");
    		return false;
    	}else if(licenseNumber.length>=100){
    		alert("证照编号 超出长度规范！ ");
    		return false;
    	}
    	var inputs=$("#businessLicense_id > input");
    	if(!inputs||inputs.length<=0){
    		alert("请选择 营业执照")
    		return false;
    	}
    }else if(photoType+""=="2"){
    	
    	/*
		var  organization=$("#organization_id").val();
		if(!organization){
    		alert("请填写 组织机构代码  ");
    		return false;
    	} */
    	var companyNature=$("#companyNature_id").val();
    	if(companyNature==1701){
    		var  registrationNumber =$("#registrationNumber_id").val();
        	if(!registrationNumber){
        		alert("请填写 注册号  ");
        		return false;
        	} 
    		var taxRegistersCardNumber=$("#taxRegistersCardNumber_id").val();
    		if(!taxRegistersCardNumber){
        		alert("请填写 税务登记证号码  ");
        		return false;
        	}
    		var taxationPhotoInputs=$("#organizationPhoto_id").find(">  input");
        	if(!taxationPhotoInputs||taxationPhotoInputs.length<=0){
        		alert("请选择 税务登记证")
        		return false;
        	} 
        	var businessLicenseInputs=$("#businessLicense2_id").find(" > input");
        	if(!businessLicenseInputs||businessLicenseInputs.length<=0){
        		alert("请选择 营业执照")
        		return false;
        	}
    	}else if(companyNature==1702){
    		var businessLicenseInputs=$("#businessLicense2_id").find(" > input");
        	if(!businessLicenseInputs||businessLicenseInputs.length<=0){
        		alert("请选择 营业执照")
        		return false;
        	}
    	}
		
		
    	var organizationPhotoInputs=$("#organizationPhoto_id").find(" > input");
    	if(!organizationPhotoInputs||organizationPhotoInputs.length<=0){
    		alert("请选择 组织机构代码证")
    		return false;
    	}
    	
    }
    var companyClassification=$("#companyClassification_id").val();
	var saveJson=new Object();
	var register={
		"applicant":applicant,
		"registerAddress":registerAddress,
		"officeAddress":officeAddress,
		"registerMoney":registerMoney,
		"oneYearIsTaxAmount":oneYearIsTaxAmount,
		"staffNum":staffNum,
		"linkman":linkman,
		"phone":phone,
		"email":email,
		"companyClassification":companyClassification,
		"tel":tel,
		"pickDwelling":pickDwelling,
		"companyNature":companyNature
	}	
	if(rent)
		register["rent"]=rent;
	if(applicationCompany)
		register["applicationCompany"]=applicationCompany;
	var jsonString={
			"formId":58,
			"tableDataId":companyId,
			"register":register
		};
	var photoArray=new Array();
	var unifiedSocialCreditCode=$("#unifiedSocialCreditCode_id").val();
	if(photoType+""=="1"){ 
    	var licenseNumber=$("#licenseNumber_id").val();
    	var inputs=$("#businessLicense_id").find("input");
    	var businessLicenseName=inputs.eq(0).val();
    	var businessLicense=inputs.eq(1).val();
    	var businessLicenseSmallPhoto=inputs.eq(2).val();
    	var photo={"photoType":1,"unifiedSocialCreditCode":unifiedSocialCreditCode,"licenseNumber":licenseNumber,"businessLicenseName":businessLicenseName,"businessLicense":businessLicense,"businessLicenseSmallPhoto":businessLicenseSmallPhoto};
    	if(companyPhotoId)
    		photo.id=companyPhotoId
    	photoArray.push(photo);    	
	 }else if(photoType+""=="2"){
		var  registrationNumber =$("#registrationNumber_id").val();	    	
		var  organization=unifiedSocialCreditCode;
		var taxRegistersCardNumber=$("#taxRegistersCardNumber_id").val();
		var businessLicenseInputs=$("#businessLicense2_id").find("input");
    	var organizationPhotoInputs=$("#organizationPhoto_id").find("input");
    	var taxationPhotoInputs=$("#taxationPhoto_id").find("input");
    	var businessLicenseName=businessLicenseInputs.eq(0).val();
    	var businessLicense=businessLicenseInputs.eq(1).val();
    	var businessLicenseSmallPhoto=businessLicenseInputs.eq(2).val();
    	
    	var organizationPhotoName=organizationPhotoInputs.eq(0).val();
    	var organizationPhoto=organizationPhotoInputs.eq(1).val();
    	var organizationSmallPhoto=organizationPhotoInputs.eq(2).val();
    	var taxationPhotoName=taxationPhotoInputs.eq(0).val();
    	var taxationPhoto =taxationPhotoInputs.eq(1).val();
    	var taxationSmallPhoto =taxationPhotoInputs.eq(2).val();
    	var photo={"photoType":2,"unifiedSocialCreditCode":unifiedSocialCreditCode,"registrationNumber":registrationNumber,"organization":organization,"taxRegistersCardNumber":taxRegistersCardNumber,
        		"businessLicenseName":businessLicenseName,"businessLicense":businessLicense,"businessLicenseSmallPhoto":businessLicenseSmallPhoto,
        		"organizationPhotoName":organizationPhotoName,"organizationPhoto":organizationPhoto,"organizationSmallPhoto":organizationSmallPhoto,
        		"taxationPhotoName":taxationPhotoName,"taxationPhoto":taxationPhoto,"taxationSmallPhoto":taxationSmallPhoto
        		};
    	if(companyPhotoId)
    		photo.id=companyPhotoId
    	photoArray.push(photo);
	 }
	var detail=new Array();
	detail.push({"formId":59,"parentId":"companyId","array":photoArray});
	jsonString.detail=detail;
	//申请
	var register2= jQuery.extend({}, register); 
	register2.status=0
	register2.applicationNum=$("#applicationNum_id").val();//申请人数	
	register2.canApplicationNum=$("#applicationNum_id").val();//申请人数
	register2.companyId=companyId;
 
	var detail2=new Array();
	var photoObj2= jQuery.extend({}, photoArray[0]);
	delete photoObj2["id"];
	if(requestPhotoId)
		photoObj2["id"]=requestPhotoId;
	var photoArray2=new Array();
	photoArray2.push(photoObj2);
	detail2.push({"formId":68,"parentId":"request_id","array":photoArray2})
	var jsonRequestString={"formId":61,"register":register2, "detail":detail2};
	if(requestId)
		jsonRequestString["tableDataId"]=requestId;
 
	var index = layer.load(2);
	$.ajax({
		url : rootPath+'/forms/saveFormDataJsons',
		type : "POST", 
      	async: false,
      	data : {
      		jsons : [JSON.stringify(jsonString),JSON.stringify(jsonRequestString)]
      	}, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.code+""=="1"){
				window.location.href=rootPath+"companyRequest/requestUserList?requestId="+data["WDIT_Company_Request"];
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
function getCompanyNature(){
	var companyNature=$("#companyNature_id").val();
	if(companyNature==1702){
		var formhtml = upLoadFormHtmlFun("taxationPhoto");
		$("#taxationPhoto_id").html(formhtml);
		
		var array=new Array();
		array.push("taxationPhoto");
		uploadFormInit(array);
		
		$("input[name='radioTab'][value=2]").prop("checked",true);
		$("input[name='radioTab'][value=1]").removeAttr("checked");
		$("input[name='radioTab'][value=1]").attr("disabled","disabled");
		var num=2;
		$(".radioTab>div").hide();
		$(".radioTab>div").eq(num-1).show();
		$("#registrationNumber_id").removeAttr("disabled");
		$("#businessLicense2_file_id").removeAttr("disabled");
		$("#taxRegistersCardNumber_id").val("");
		
		$("#taxRegistersCardNumber_id").attr("disabled","disabled");
		$("#taxationPhoto_file_id").attr("disabled","disabled");
	}else if(companyNature==1703){
		var formhtml = upLoadFormHtmlFun("businessLicense2");
		$("#businessLicense2_id").html(formhtml);
		var formhtml = upLoadFormHtmlFun("taxationPhoto");
		$("#taxationPhoto_id").html(formhtml);
		
		var array=new Array();
		array.push("businessLicense2");
		array.push("taxationPhoto");
		uploadFormInit(array);
		$("input[name='radioTab'][value=2]").prop("checked",true);
		$("input[name='radioTab'][value=1]").removeAttr("checked");
		$("input[name='radioTab'][value=1]").attr("disabled","disabled");
		var num=2;
		$(".radioTab>div").hide();
		$(".radioTab>div").eq(num-1).show();
		$("#taxRegistersCardNumber_id").val("");
		$("#registrationNumber_id").val("");
		
		$("#registrationNumber_id").attr("disabled","disabled");
		$("#businessLicense2_file_id").attr("disabled","disabled");
		$("#taxRegistersCardNumber_id").attr("disabled","disabled");
		$("#taxationPhoto_file_id").attr("disabled","disabled");
	}else if(companyNature==1701){
		$("input[name='radioTab'][value=1]").removeAttr("disabled");
		$("#taxRegistersCardNumber_id").removeAttr("disabled");
		$("#taxationPhoto_file_id").removeAttr("disabled");
		$("#registrationNumber_id").removeAttr("disabled");
		$("#businessLicense2_file_id").removeAttr("disabled");
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
                                <li>提交申请</li>
                                <li class="active">申请企业</li>                           
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
                                     	<div role="form" class="form-horizontal">
                                     			<input id="applicationNum_id" type="hidden">
                                     			<input id="companyClassification_id" type="hidden">
		                                        <h3 class="text-center padding-vertical-20">公租房单位申请意向登记单表</h3>
		                                        <div class="row">
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="applicant_id">
		                                                    	<span style="color:#FF0000;">*</span> 申请单位（全称）
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="applicant_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>                 
		                                             <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="unifiedSocialCreditCode_id">
		                                                    	<span style="color:#FF0000;">*</span> 企业信用代码
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="unifiedSocialCreditCode_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div>
		                                        <div class="row">
		                                         <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="registerAddress_id">
		                                                    	<span style="color:#FF0000;">*</span> 注册地址
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="registerAddress_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="officeAddress_id">
		                                                    	<span style="color:#FF0000;">*</span>营业或办公地址
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="officeAddress_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            
		                                        </div>
		                                        <div class="row">
		                                        	<div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="registerMoney_id">
		                                                    	<span style="color:#FF0000;">*</span>注册资本金(人民币/万元)
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input onblur="moneyFun(this)" type="text" placeholder="必填" id="registerMoney_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="oneYearIsTaxAmount_id">
		                                                    	<span style="color:#FF0000;">*</span>上一年度纳税金额(人民币/万元)
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input onblur="moneyFun(this)" type="text" placeholder="必填" id="oneYearIsTaxAmount_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div>
		                                        <div class="row">
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="staffNum_id">
		                                                    	<span style="color:#FF0000;">*</span>在职员工人数
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="staffNum_id" onkeyup="value=value.replace(/[^\d]/g,'')" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="rent_id">
		                                                        	单位承担租金的百分比
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <select style="height: 32px" class="form-control"  id="rent_id">
		                                                        </select>
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div>
		                                        <div class="row">
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="staffNum_id">
		                                                    	<span style="color:#FF0000;">*</span>所选小区
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <select style="height: 32px" class="form-control"  id="pickDwelling_id">
		                                                        </select>
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="staffNum_id">
		                                                    	<span style="color:#FF0000;">*</span>企业性质
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <select style="height: 32px" class="form-control"  id="companyNature_id" onchange="getCompanyNature()">
		                                                        </select>
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div>
		                                        <hr class="hr-sm">
		                                        <h4>单位申请公租房工作专职人员信息：</h4>
		                                        <div class="row">
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="linkman_id">
		                                                    	<span style="color:#FF0000;">*</span>专职联系人
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="linkman_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="phone_id">
		                                                    	<span style="color:#FF0000;">*</span>联系人手机
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="phone_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div>
		                                        <div class="row">
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="email_id">
			                                                    <span style="color:#FF0000;">*</span>电子邮箱
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="email_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="tel_id">
		                                                    	<span style="color:#FF0000;">*</span>联系人电话
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="tel_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div>
		                                        <div class="row">
		                                            <div class="col-sm-12">
		                                                <div class="form-group">
		                                                    <label class="col-sm-1 control-label" for="applicationCompany_id">
		                                                        	申请单位（需求说明）
		                                                    </label>
		                                                    <div class="col-sm-10">
		                                                        <textarea name="txtarea" id="applicationCompany_id" class="form-control" rows="5"> </textarea>
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div>
		                                        <hr class="hr-sm">
		                                        <div class="row">
		                                            <div class="col-sm-12">
		                                                <div class="form-group">
		                                                    <label class="col-sm-1 control-label" for="form-field-13">
		                                                        	上传材料照片：
		                                                    </label>
		                                                    <div class="col-sm-10 radio-tab-nav">
		                                                        <label class="radio-inline"><input type="radio" value="1" name="radioTab" checked="checked">已办理“三证合一”</label>
		                                                        <label class="radio-inline"><input type="radio" value="2" name="radioTab">未办理“三证合一”</label>
		                                                    </div>
		                                                </div>
		                                                <div class="radioTab">
		                                                    <div class="table-responsive">
		                                                        <table class="table table-bordered table-hover text-center table-striped">
		                                                            <thead>
		                                                                <tr>
		                                                                    <td>材料内容</td>
		                                                                    <td>编号</td>
		                                                                    <td>上传照片</td>
		                                                                    <td>样例图片</td>
		                                                                </tr>
		                                                            </thead>
		                                                            <tbody>
		                                                                <tr>
		                                                                  
		                                                                    <td>营业执照</td>
		                                                                    <td>
		                                                                        <p><span style="color:#FF0000;">*</span>证照编号：</p>
		                                                                        <p><input type="text" id="licenseNumber_id"></p>
		                                                                    </td>
		                                                                    <td id="businessLicense_id">
		                                                                   		<a class="btn btn-default margin-right-10" href="#">上传</a><a class="btn btn-default" href="#">删除</a></td>
		                                                                    <td><img src="<c:url value="/wdit/assets/images/yyzz.jpg"/> " height="180" alt=""></td>
		                                                                </tr>
		                                                            </tbody>
		                                                        </table>
		                                                    </div>
		                                                    <div class="table-responsive">
		                                                        <table class="table table-bordered table-hover text-center table-striped">
		                                                            <thead>
		                                                                <tr>
		                                                                    <td>材料内容</td>
		                                                                    <td>编号</td>
		                                                                    <td>上传照片</td>
		                                                                    <td>样例图片</td>
		                                                                </tr>
		                                                            </thead>
		                                                            <tbody>
		                                                                <tr>
		                                                                    <td>营业执照或企业法人证</td>
		                                                                    <td>
		                                                                        <p><span style="color:#FF0000;">*证件编号</span>：</p>
		                                                                        <p><input type="text" id="registrationNumber_id"></p>
		                                                                    </td>
		                                                                    <td id="businessLicense2_id">
		                                                                    	<a class="btn btn-default margin-right-10" href="#">上传</a><a class="btn btn-default" href="#">删除</a>
		                                                                    </td>
		                                                                    <td><img src="<c:url value="/wdit/assets/images/yyzz.jpg"/>" height="180" alt=""></td>
		                                                                </tr>
		                                                                <tr>
		                                                                    <td>组织机构代码证</td>
		                                                                    <td> 
		                                                                    </td>
		                                                                    <td id="organizationPhoto_id">
		                                                                    	<a class="btn btn-default margin-right-10" href="#">上传</a><a class="btn btn-default" href="#">删除</a>
		                                                                    </td>
		                                                                    <td><img src="<c:url value="/wdit/assets/images/u254.jpg"/>" height="180" alt=""></td>
		                                                                </tr>
		                                                                <tr>
		                                                                    <td>税务登记证</td>
		                                                                    <td>
		                                                                        <p><span style="color:#FF0000;">*</span>税务登记证号码：</p>
		                                                                        <p><input type="text" id="taxRegistersCardNumber_id"></p>
		                                                                    </td>
		                                                                    <td id="taxationPhoto_id">
		                                                                    	<a class="btn btn-default margin-right-10" href="#">上传</a><a class="btn btn-default" href="#">删除</a>
		                                                                    </td>
		                                                                    <td><img src="<c:url value="/wdit/assets/images/u254.jpg"/>" height="180" alt=""></td>
		                                                                </tr>
		                                                            </tbody>
		                                                        </table>
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div>
		                                        <div class="text-center">
		                                            <a href='javascript:void(0)' class="btn btn-info margin-right-10" onclick="saveCompanyFun()">确定</a>
		                                            <a  class="btn btn-default" href="<c:url value="/companyRequest/requestList"/>">关闭</a>
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