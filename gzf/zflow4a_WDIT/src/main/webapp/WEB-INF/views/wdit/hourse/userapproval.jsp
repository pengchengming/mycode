<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>审核人员信息</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/script/jquery/jquery.form.js" />"></script>
<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var requestUserId='${requestUserId}';
var guserid=${sessionScope.USER.id};
var tableController;
var createTable = new createTable();
var relative_length;
var selstatus;
var requreid = ${requreid};
var restatus = 0;
var roles = "${sessionScope.ROLES}";
var linkman;
var email;

$(function(){
	$("#faimily_p").hide();
	$("#select_status").append(getDataDictionaryValueHtml(70));
	$("#select_ownerType").append(getDataDictionaryValueHtml(180));
	$("#education").append(getDataDictionaryValueHtml(190));
	getuser();
	var familyid=$("#applyForFamily_id").val();
	if(familyid+""=="503" || familyid+""=="504"){
		$("#faimily_p").show();
	}
// 	$("#pickDwelling").append(getDataDictionaryValueHtml(160));
	$("#select_status").change(function(){
		if(selstatus != $("#select_status").val()){
			$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);
		}else{
			$("#bt_sure").attr("class","btn btn-default").attr("disabled", true);
		}	
	})
	
   $("#applyForFamily").change(function(){
    	restatus = 1;
    	var familyid2=$("#applyForFamily").val();
    	if(familyid2+""=="503" || familyid2+""=="504"){
    		$("#faimily_p").show();
    	}else{
    		$("#faimily_p").hide();
    	}
    	$("#bt_save").attr("class","btn btn-info margin-right-10").attr("disabled", false);
    	$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);
    	
	})
	$("input").change(function(){
		restatus = 1;
		$("#bt_save").attr("class","btn btn-info margin-right-10").attr("disabled", false);
		$("#bt_sure").attr("class","btn btn-info margin-right-10").attr("disabled", false);
	})
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



function getuser(){
	var  byIdconfig={
			fromConfig:[{
				formCode:"WDIT_Company_Request_User_relative",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"userRelative",
				fieldName:"id,name,relative,identityCardNumber",
				fromConfig:[{
					formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"relative",
					aliasesName:"relativename",
					fieldName:"displayValue"	
				},{
					formCode:"WDIT_Company_Request_User_photo",
					currentField:"user_relative_id",
					parentId:"id",
					aliasesName:"userRelativePhotos",
					fieldName:"type,smallPhoto,photo"
				}]
			},{
				formCode:"WDIT_Company_Request_User_photo",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"userPhoto",
				fieldName:"smallPhoto,type,user_relative_id,photo"
			},{
				formCode:"WDIT_Company_Request",
				currentField:"id",
				parentId:"request_id",
				aliasesName:"requestpickDwelling",
				fieldName:"pickDwelling,linkman,email",
				fromConfig:[{
					formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"pickDwelling",
					aliasesName:"pickDwellingname",
					fieldName:"displayValue"	
				}]
			},{
				formCode:"WDIT_Company_Request_User_housing",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"userHousing",
				fieldName:"id,housingLocatedAddress,area,propertyOwner,theHousingAllNumpeople,ownerType",
				fromConfig:[{
					formCode:"WDIT_Company_Request_User_photo",
					currentField:"user_housing_id",
					parentId:"id",
					aliasesName:"userHousingPhotos",
					fieldName:"type,smallPhoto,photo"
				}]
			}]
	}
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request_User",
			condition: " id="+requestUserId,
			byIdconfig:JSON.stringify(byIdconfig)
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results&&results.length>0){
				var requreUser=results[0];
				var userName = requreUser.userName;
				var placeOfDomicile = requreUser.placeOfDomicile;
				var maritalStatus = requreUser.maritalStatus;
				var housingConditionsInTheCity = requreUser.housingConditionsInTheCity;
				var applyForFamily = requreUser.applyForFamily;
			    var applicantPhone = requreUser.applicantPhone; 
			    var education = requreUser.education;
			    var educationCardCode = requreUser.educationCardCode;
			    var graduationSchool = requreUser.graduationSchool;
			    var graduationTime = requreUser.graduationTime;
			    var specialty = requreUser.specialty;
// 			    var pickDwelling = requreUser.pickDwelling[0].displayValue;
// 			    var pickDwelling_id = requreUser.pickDwelling[0].id;
                var pickDwelling =  requreUser.requestpickDwelling[0].pickDwellingname[0].displayValue;
                linkman = requreUser.requestpickDwelling[0].linkman;
                email = requreUser.requestpickDwelling[0].email;
                var pickDwelling_id = requreUser.requestpickDwelling[0].pickDwelling;
                var applyForFamilys=dictionaryCodeFun(5);
            	var pick = pickDwelling_id;
            	if(applyForFamilys&&applyForFamilys.length>0){
            		$.each(applyForFamilys,function(i,datadictionaryValue){
            			if(pick+""=="1601"){
            				if(datadictionaryValue.id!=503 && datadictionaryValue.id!=504){
            					$("#applyForFamily").append('<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>');
            				}
            			}else if(pick+""=="1602"){
            				$("#applyForFamily").append('<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>');
            			}
            		});
            	}
			    var identityCardNumber = requreUser.identityCardNumber;
			    var residencePermitNumber = requreUser.residencePermitNumber;
			    var housingAccumulationFundAccount = requreUser.housingAccumulationFundAccount;
			    var permanentAddress = requreUser.permanentAddress;
			    var address = requreUser.address;
			    $("#userName").val(userName);
			 	$("input[name='placeOfDomicile'][value='"+placeOfDomicile+"']").prop("checked","checked");
			    $("input[name='maritalStatus'][value='"+maritalStatus+"']").prop("checked","checked");
                $("input[name='housingConditionsInTheCity'][value='"+housingConditionsInTheCity+"']").prop("checked","checked");
                $("input[name='placeOfDomicile']").attr("disabled",true);
                $("input[name='maritalStatus']").attr("disabled",true);
                $("input[name='housingConditionsInTheCity']").attr("disabled",true);
                $("#pickDwelling").html(pickDwelling);
                $("#pickDwelling_id").val(pickDwelling_id);
                $("#pickDwelling").attr("readonly","readonly");
			    $("#applyForFamily").val(applyForFamily);
			    $("#applicantPhone").val(applicantPhone);
			    $("#identityCardNumber").val(identityCardNumber);
			    $("#residencePermitNumber").val(residencePermitNumber);
			    $("#housingAccumulationFundAccount").val(housingAccumulationFundAccount);
			    $("#permanentAddress").val(permanentAddress);
			    $("#address").val(address);
			    $("#education").val(education);
			    $("#educationCardCode").val(educationCardCode);
			    $("#graduationSchool").val(graduationSchool);
			    $("#graduationTime").val(graduationTime);
			    $("#specialty").val(specialty);
			    
				var tableConfig = {
						fields : [{
							title : 'id',
							data : 'id',
							type:'none'
						},{
							title : '与申请人关系',
							data : '',
							type : 'cutsomerRender',
							doRender : function(data, container,config, rowIndex){
								 $(container).append(data.relativename[0].displayValue);
							}
						},{
							title : '姓名',
							data : '',
							type : 'cutsomerRender',
							doRender : function(data, container,config, rowIndex){
								$(container).append('<input type="text" value="'+data.name+'">');
							}
						}, {
							title : '身份证号码',
							data : '',
							type : 'cutsomerRender',
							doRender : function(data, container,config, rowIndex){
								$(container).append('<input type="text" value="'+data.identityCardNumber+'">');
							}
						},{
							title : '身份证照片',
							data : '',
							type : 'cutsomerRender',
							doRender : function(data, container,config, rowIndex){
								var userRelativePhotos = data.userRelativePhotos;
								var imagehtml='';
								$.each(userRelativePhotos,function(i,userRelativePhoto){
									$(container).append('<a onClick="showPhoto(\''+rootPath+userRelativePhoto.photo+'\')"><img src="'+rootPath+userRelativePhoto.smallPhoto+'" style="margin-left: 20px;height: 180px;" alt=""></a>');
								})
							}
						}]
				}; 
				if(data.code + "" == "1"){
					createTable.registTable($('#relative_table'), tableConfig, {code:1,results:requreUser.userRelative}, "queryClick");
				}else{
					$("#relative_table").html("<div style='text-align: center'>没有记录</div>");
				}
				
				if(housingConditionsInTheCity == 401){
					$("#houseinfo_div").css('display' ,'');
					var userHousinfo = requreUser.userHousing[0];
					if(userHousinfo){
						$("#houseinfo_id").text(userHousinfo.id);
						$("#houseaddress").val(userHousinfo.housingLocatedAddress);
						$("#area").val(userHousinfo.area);
						$("#propertyOwner").val(userHousinfo.propertyOwner);
						$("#theHousingAllNumpeople").val(userHousinfo.theHousingAllNumpeople);
						$("#select_ownerType").val(userHousinfo.ownerType);
						var userHousingPhotos = userHousinfo.userHousingPhotos;
						$.each(userHousingPhotos,function(i,userHousingPhoto){
							$("#userHousingPhoto").append('<a onClick="showPhoto(\''+rootPath+userHousingPhoto.photo+'\')"><img src="'+rootPath+userHousingPhoto.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt=""></a>');		
						})	
					}
				}
				 relative_length = $("#relative_table table tbody").find('tr').length;
				
				var userphotos = requreUser.userPhoto;
				 var tbody = $('#photo_table tbody');
	                var namehtml2 = '申请人身份证';
	                var amagehtml2 = '';
	                var namehtml3 = '申请人居住证';
	                var amagehtml3 = '';
	                var namehtml4 = '申请人户口本';
	                var amagehtml4 = '';
	                var namehtml5 = '申请人婚姻情况证明';
	                var amagehtml5 = '';
	                $.each(userphotos,function(i,obj){
						if(obj.type + ""== "2"){
							amagehtml2 += '<a onClick="showPhoto(\''+rootPath+obj.photo+'\')"><img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt=""></a>';
						}else if(obj.type + "" == "3"){
							amagehtml3 += '<a onClick="showPhoto(\''+rootPath+obj.photo+'\')"><img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt=""></a>';
						}else if(obj.type + "" == "4"){
							amagehtml4 += '<a onClick="showPhoto(\''+rootPath+obj.photo+'\')"><img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt=""></a>';
						}else if(obj.type + "" == "5"){
							amagehtml5 += '<a onClick="showPhoto(\''+rootPath+obj.photo+'\')"><img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt=""></a>';
						}
					})
					if(amagehtml2 != '')
						tbody.append('<tr><td>'+namehtml2+'</td><td>'+amagehtml2+'</td></tr>');
					if(amagehtml3 != '')
						tbody.append('<tr><td>'+namehtml3+'</td><td>'+amagehtml3+'</td></tr>');
					if(amagehtml4 != '')
						tbody.append('<tr><td>'+namehtml4+'</td><td>'+amagehtml4+'</td></tr>');
					if(amagehtml5 != '')
						tbody.append('<tr><td>'+namehtml5+'</td><td>'+amagehtml5+'</td></tr>');
// 				1房产证或使用权凭证照片 2 申请人身份证 3 申请人居住证 4 申请人户口本 5申请人结婚证 6共同申请人身份证7授权书
// 				var td1 = $('#houseinfo_div table tbody').find('tr:eq(2)').find('td:eq(0)');
// 				var td2 = $('#photo_table tbody').find('tr:eq(1)').find('td:eq(1)');
// 				var td3 = $('#photo_table tbody').find('tr:eq(2)').find('td:eq(1)');
// 				var td4 = $('#photo_table tbody').find('tr:eq(3)').find('td:eq(1)');
// 				var td5 = $('#photo_table tbody').find('tr:eq(4)').find('td:eq(1)');
// 				$.each(userphotos,function(i,obj){
// 					if(obj.type + ""== "1"){
// 						$(td1).append('<img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt="">');
// 					}else 
// 					if(obj.type + ""== "2"){
// 						$(td2).append('<img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt="">');
// 					}else if(obj.type + "" == "3"){
// 						$(td3).append('<img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt="">');
// 					}else if(obj.type + "" == "4"){
// 						$(td4).append('<img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt="">');
// 					}else if(obj.type + "" == "5"){
// 						$(td5).append('<img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt="">');
// 					}
// 					else if(obj.type + "" == "6"){
// 						for(var i=0;i<relative_length;i++){
// 							var tr = $("#relative_table table tbody").find('tr:eq('+i+')');
// 							var relative_id = $(tr).find('td:eq(0)').text();
// 							if (relative_id == obj.user_relative_id+""){
// 								$(tr).find('td:eq(4)').append('<img src="'+rootPath+obj.smallPhoto+'"  style="margin-left: 20px;height: 180px;" alt="">');
// 							} 
// 						}
// 					}
// 				})
			}
		}
	});
}


function saveRequestUser(){
	var resubarray = new Array();
	
	var userName=$("#userName").val();
	if(!userName){
		alert("请填写人员姓名 ");
		return false;
	}else if(userName.length>20){
		alert("人员姓名 超出长度规范！");
		$("#userName").val("");
		$("#userName").focus();
		return false;
	}
	var placeOfDomicile=$("input[name='placeOfDomicile']:checked").val();
	if(!placeOfDomicile){
		alert("请选择户籍地 ");
		return false;
	}
	var maritalStatus=$("input[name='maritalStatus']:checked").val();
	if(!maritalStatus){
		alert("请选择 婚姻状况");
		return false;
	}
	var housingConditionsInTheCity=$("input[name='housingConditionsInTheCity']:checked").val();
	if(!housingConditionsInTheCity){
		alert("请选择本市住房情况");
		return false;
	}
	var applyForFamily=$("#applyForFamily").val();
	if(!applyForFamily){
		alert("请申请户型");
		return false;
	}
	var applicantPhone=$("#applicantPhone").val();
	if(!applicantPhone){
		alert("请填写申请电话");
		return false;
	}else if(applicantPhone.length>50){
		alert("申请人电话 超出长度规范！");
		$("#applicantPhone").val("");
		$("#applicantPhone").focus();
		return false;
	}
	var identityCardNumber=$("#identityCardNumber").val();
	if(!identityCardNumber){
		alert("请填写身份证号码");
		return false;
	}else if(identityCardNumber.length>20){
		alert("身份证号码 超出长度规范！");
		$("#identityCardNumber").val("");
		$("#identityCardNumber").focus();
		return false;
	}
	var residencePermitNumber=$("#residencePermitNumber").val();
	if(!residencePermitNumber && placeOfDomicile == 202){
		alert("请填写居住证号码");
		return false;
	}if(residencePermitNumber&&residencePermitNumber.length>50){
		alert("居住证号码 超出长度规范！");
		$("#residencePermitNumber").val("");
		$("#residencePermitNumber").focus();
		return false;
	}
	var housingAccumulationFundAccount=$("#housingAccumulationFundAccount").val();
	if(housingAccumulationFundAccount&&housingAccumulationFundAccount.length>30){
		alert("住房公积金账户 超出长度规范！");
		$("#housingAccumulationFundAccount").val("");
		$("#housingAccumulationFundAccount").focus();
		return false;
	}
// 	if(!housingAccumulationFundAccount){
// 		alert("请填写住房公积金账号");
// 		return false;
// 	}
	var permanentAddress=$("#permanentAddress").val();
	if(!permanentAddress){
		alert("请填写户籍地址");
		return false;
	}else if(permanentAddress.length>300){
		alert("户籍地址 超出长度规范！");
		$("#permanentAddress").val("");
		$("#permanentAddress").focus();
		return false;
	}
	var address=$("#address").val();
	if(address&&address.length>200){
		alert("联系地址 超出长度规范！");
		$("#address").val("");
		$("#address").focus();
		return false;
	}
// 	if(!address){
// 		alert("请填写联系地址");
// 		return false;
// 	}
	
	var education=$("#education").val();
	var educationCardCode = $("#educationCardCode").val();
    if(educationCardCode&&educationCardCode.length>50){
			alert("学位证书编号 超出长度规范！");
			$("#educationCardCode").val("");
			$("#educationCardCode").focus();
			return false;
		}
	var graduationSchool = $("#graduationSchool").val();
	if(graduationSchool&&graduationSchool.length>100){
		alert("毕业院校 超出长度规范！");
		$("#graduationSchool").val("");
		$("#graduationSchool").focus();
		return false;
	}
	var graduationTime=$("#graduationTime").val();
	var specialty = $("#specialty").val();
	if(specialty&&specialty.length>50){
		alert("专业 超出长度规范！");
		$("#specialty").val("");
		$("#specialty").focus();
		return false;
	}

	var register={
			"userName":userName,
			"placeOfDomicile":placeOfDomicile,
			"maritalStatus":maritalStatus,
			"housingConditionsInTheCity":housingConditionsInTheCity,
			"applyForFamily":applyForFamily,
			"applicantPhone":applicantPhone,
			"identityCardNumber":identityCardNumber,
	        "permanentAddress":permanentAddress,
	        "education":education,
	        "educationCardCode":educationCardCode,
	        "graduationSchool":graduationSchool,
	        "graduationTime":graduationTime,
	        "specialty":specialty
	}
	
	if(address)
		register['address'] = address;
	if(residencePermitNumber)
		register['residencePermitNumber'] = residencePermitNumber;
	if(housingAccumulationFundAccount)
		register['housingAccumulationFundAccount'] = housingAccumulationFundAccount;
	 var jsonString={
			"formId":62,
			"tableDataId":requestUserId,
			"register":register
		};
	resubarray.push(JSON.stringify(jsonString));
	
	var tdlength = $("#relative_table table tbody").find('tr:eq(0)').find('td').length;
	if(tdlength >1){
		for(var i=0;i<relative_length;i++){
			var tr = $("#relative_table table tbody").find('tr:eq('+i+')');
			var relative_id = $(tr).find('td:eq(0)').text();
			var name = $(tr).find('input:eq(0)').val();
			var identityCardNumber =  $(tr).find('input:eq(1)').val();
			var relative_sub = new Object();
			relative_sub['formId'] = 63;
			relative_sub['tableDataId'] = relative_id;
			relative_sub['register'] = {"name":name,"identityCardNumber":identityCardNumber};
			resubarray.push(JSON.stringify(relative_sub));
		}	
	}
	if(housingConditionsInTheCity == 401  && $("#houseinfo_div").css('display') != 'none'){
		var houseaddress = $("#houseaddress").val();
		var area = $("#area").val();
		var propertyOwner = $("#propertyOwner").val();
		var theHousingAllNumpeople = $("#theHousingAllNumpeople").val();
		var houseinfoId = $("#houseinfo_id").text();
		var house_sub = new Object();
		house_sub['formId'] = 64;
		house_sub['tableDataId'] = houseinfoId;
		house_sub['register'] = {"houseaddress":houseaddress,"area":area,"propertyOwner":propertyOwner,"theHousingAllNumpeople":theHousingAllNumpeople};
		resubarray.push(JSON.stringify(house_sub));
	}
	var url = '';
	var datajson = new Object();
	if (resubarray.length > 1){
		url = rootPath+'/forms/saveFormDataJsons';
		datajson = {jsons : resubarray};
	}else{
		url = rootPath+'/forms/saveFormDataJson';
		datajson = {json : JSON.stringify(jsonString)};
	}
	
	 $.ajax({
			url : url,
			type : "POST", 
			async: false,
			data : datajson, 
			complete : function(xhr, textStatus){
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


function sureRequestUser(){
	if(restatus == 1){
		alert("请先保存修改");
		return false;
	}
	$("#bt_sure").attr("class","btn btn-default").attr("disabled", true);;
	selstatus = $("#select_status").val();
}

function iscontinue(){
	var isapprovalCompany=true;
	$.ajax({
		url : '<c:url value="/data/procedure"/>',
		type : "POST", 
	    async: false,
	    data : {
	    	p : 'R2014002|1|'+requestUserId 
	    }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data&&data.length>0){
				var obj = data[0];
				if(obj.result == 1)
					isapprovalCompany=false;
			}
	   }
	});
	return  isapprovalCompany;
}

 
function approveRequestUser(status){
	if(roles.indexOf("HOUSEMANAGE_ROLE,")>=0){
		//公司还没审 人员还不能审批
		if(!iscontinue()){
			alert("请先审核公司,再审核员工!");
			return false;
		}
		if($("#bt_sure").attr("class")=="btn btn-info margin-right-10"){
			alert("清先材料确认");
			return false;
		}
		selstatus = $("#select_status").val();
		if(selstatus == 701 && status == 1203){
			alert("当前预审结果通过，请点击预审通过！");
			return false;
		}
		if(selstatus != 701 && status == 1202){
			alert("当前预审结果不通过，请点击预审不通过！");
			return false;
		}
		$.ajax({
			url : '<c:url value="/data/procedure"/>',
			type : "POST", 
		    async: false,
		    data : {
		    	p : 'R2014006|'+requestUserId + '|12'
		    }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data&&data.length>0){
					var obj = data[0];
					if(obj.checkcode && obj.checkcode == 12){
						var index = layer.load(2);
						var jsons = new Array();
						 var approval_Date = obj.currenttime;
						 $("#bt_div").css('display' ,'none');
						 var requestUserObj = {
								"formId":62,
								"tableDataId":requestUserId,
								"register":{"status":status}
							};
						 jsons.push(JSON.stringify(requestUserObj));	
						 var approvalUserObj = {
									"formId":67,
									"register":{"status":status,"approvalStep":1,
										        "requestUser_id":requestUserId,"approval_Date":approval_Date}
								};
						 jsons.push(JSON.stringify(approvalUserObj));
						 
						 var username = $("#userName").val();
						 if(linkman && email && username &&status == 1203){
							 var emailobj = new Object();
			            	 var content = linkman + " 您好：贵公司提交的公租房申请表中 "+username+" 在 资料预审  的审批中被审批拒绝。";
			            	 emailobj["formId"]= 99;
			            	 emailobj["register"] = {"email":email,"content":content,"type":1,"flag":0};// 1为发给公司
			            	 jsons.push(JSON.stringify(emailobj));
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
											checkstatus(index);
										}else {
											alert(data.errorMsg);
										}	
								}});	 
					}else{
						alert("无需重复审批");
					}
				}}
		})
	}else{
		alert("请切换房管局角色");
	}
	
}

function checkstatus(index){
	$.ajax({
		url : '<c:url value="/data/procedure"/>',
		type : "POST", 
	    async: false,
	    data : {
// 	    	p : 'U2014001|'+ requestUserId 
	    	p : 'U2014004|'+ requestUserId + '|' + guserid
	    }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data[0].resultcode == 1 || data[0].resultcode == 0 ){
				alert("审核成功");
                layer.close(index);
                window.opener.getRequestUserList(requreid,1);
				window.close();
			}
	   }
	})
}


function areaFun(me) {
	var num = me.value;
	if (num) {
		var exp = /^\d*\.?\d{1,2}$/;
		if (!exp.test(num)) {
			alert("居住面积填写不正确，请填写大于等于零");
			me.value = "";
			me.focus();
		}
	}
} 





</script>

</head>

<body class="horizontal-menu-fixed">

    <div class="main-wrapper">
		<%-- <%@ include file="/WEB-INF/views/wdit/head.jsp"%>  --%>
	<%-- 	 <%@ include file="/WEB-INF/views/wdit/leftMenu.jsp"%> --%>
        <!-- start: MAIN CONTAINER -->
        <div class="panel-body no-padding-top">
            <!-- start: PAGE -->
            <div class="main-content" style="min-height: 542px;">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-12">
                            <!-- start: DATE/TIME PICKER PANEL -->
                            <div class="panel panel-white">
                                <div class="panel-body no-padding-top">
                                    <div class="row">
                                    	<div>
                                    	<h4>申请人基本信息</h4>
                                    	<table class="table table-bordered table-hover text-center table-horizontal">
                                    		<tbody>
                                    		 <tr>
	                                    		<th class="text-right" width="100">人员姓名</th>
	                                            <td class="text-left"><input type="text"  id = "userName" class="form-control"></td>
                                    		    <th class="text-right" width="100">户籍地</th>
	                                            <td class="text-left">
	                                               <label><input type="radio" name="placeOfDomicile"  value="201">上海市户籍</label>
	                                               <label><input type="radio" name="placeOfDomicile"  value="202">非上海市户籍</label>
	                                            </td>
	                                            <th class="text-right" width="100">婚姻状况</th>
	                                            <td>
	                                               <label><input type="radio" name="maritalStatus"  value="301">已婚</label>
	                                               <label><input type="radio" name="maritalStatus"  value="302">未婚</label>
	                                               <label><input type="radio" name="maritalStatus"  value="303">离异</label>
	                                               <label><input type="radio" name="maritalStatus"  value="304">丧偶</label>
	                                            </td>
	                                         </tr>
                                    		<tr>
                                    		    <th class="text-right" width="100">本市住房情况</th>
	                                            <td class="text-left">
	                                                <label><input type="radio" name="housingConditionsInTheCity"  value="401">本市有住房</label>
	                                                <label><input type="radio" name="housingConditionsInTheCity"  value="402">本市无住房</label>
	                                            </td>
                                    		   	<th class="text-right" width="100">申请电话</th>
	                                            <td class="text-left"><input type="text"  id = "applicantPhone" class="form-control"></td>
                                    		 </tr>
                                    		<tr><th class="text-right" width="100">身份证号码</th>
	                                            <td class="text-left"><input type="text"  id = "identityCardNumber" class="form-control"></td>
                                    		    <th class="text-right" width="100">居住证号码</th>
	                                            <td class="text-left"><input type="text"  id = "residencePermitNumber" class="form-control"></td>
                                    		    <th class="text-right" width="100">住房公积金账号</th>
	                                            <td class="text-left"><input type="text"  id = "housingAccumulationFundAccount" class="form-control"></td>
                                    		</tr>
                                    		 <tr>
                                    		 	<th class="text-right" width="100">所选小区<input type="hidden" id="pickDwelling_id"/></th>
                                    		    <td class="text-left"  style="width: 200px;" id="pickDwelling"></td>
                                    		    <th class="text-right" width="100">申请户型</th>
	                                            <td class="text-left">
	                                                 <select class="form-control" style="height: 32px" id="applyForFamily" ></select>
	                                            </td>
	                                             <td align="left" colspan="2" >
                                    			 	<p style="margin-top:10px;height: 15px" id="faimily_p">注：该户型仅适用单人使用！</p>
                                    			 </td>
                                    		 </tr>
                                    		<tr>
                                    		    <th class="text-right" width="100">户籍地址</th>
                                    		    <td colspan="5"><input type="text"  id="permanentAddress" class="form-control"></td>
                                    		</tr>
                                    		<tr>
                                    		    <th class="text-right" width="100">联系地址</th>
                                    		    <td colspan="5"><input type="text"  id="address" class="form-control"></td>
                                    		</tr>
                                    		 <tr>
                                    		 	<th class="text-right" width="100">学历</th>
                                    		 	<td class="text-left">
	                                                 <select class="form-control" style="height: 32px" id="education" ></select>
	                                            </td>
                                    		    <th class="text-right" width="100">学位证书编码</th>
	                                            <td class="text-left">
	                                                 <input type="text"  id="educationCardCode" class="form-control">
	                                            </td>
	                                            <th class="text-right" width="100">毕业学校</th>
	                                            <td class="text-left">
	                                                 <input type="text"  id="graduationSchool" class="form-control">
	                                            </td>
                                    		 </tr>
                                    		  <tr>
                                    		 	<th class="text-right" width="100">毕业时间</th>
                                    		 	<td class="text-left">
	                                                 <input type="text"  id="graduationTime" class="form-control" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
	                                            </td>
                                    		    <th class="text-right" width="100">专业</th>
	                                            <td class="text-left">
	                                                 <input type="text"  id="specialty" class="form-control">
	                                            </td>
	                                            <td colspan="2"></td>
                                    		 </tr>
                                    	</tbody>
                                    	</table>
                                    	</div>
                                    	<h4>共同申请人</h4>
                                    	<div class="table-responsive" id="relative_table">
                                    	</div>
                                    	<div id="houseinfo_div" style="display: none">
                                    	<h4>本市户籍住房信息</h4>
                                    	<span id="houseinfo_id" style="display: none;"></span>
                                    	<table class="table table-bordered table-hover text-center table-horizontal">
                                    		<tbody>
                                    			<tr>
                                    			    <th class="text-right" width="200">房屋坐落地址</th>
                                    			    <td class="text-left"><input type="text"  id = "houseaddress" class="form-control"></td>
                                    			    <th class="text-right" width="200">使用面积</th>
                                    			    <td class="text-left"><input  class="text-left" type="text"  id = "area" class="form-control" onblur="areaFun(this)"></td>
                                    			 </tr>
                                    			<tr>
                                    			    <th class="text-right" width="200">产权人类型</th>
                                    			    <td class="text-left">
                                    			       <select class="small-select2 form-control" style="height: 40px;" id = "select_ownerType"></select>
                                    			    </td>	
                                    			    <th class="text-right" width="200">产权承租人</th>
                                    			    <td class="text-left"><input type="text"  id = "propertyOwner" class="form-control"></td>
                                    			</tr>
                                    			<tr>
                                    			    <th class="text-right" width="200">该住房人口总数</th>
                                    			    <td class="text-left"><input type="text"  id = "theHousingAllNumpeople" class="form-control"  onkeyup="value=value.replace(/[^\\d]/g,'')"></td>
                                    			    <th colspan="2"></th>
                                    			</tr>
                                    			<tr>
                                    			    <th class="text-right" width="200">房产证或使用权凭证照片</th>
                                    			    <td id="userHousingPhoto" colspan="3"></td>
                                    			</tr>
                                    		</tbody>
                                    	</table>
                                    	</div>
                                    	
                                    	<h4>上传人</h4>
                                    	<div class="table-responsive">
                                           <table id="photo_table" class="table table-bordered table-hover text-center table-horizontal">
                                    		<tbody>
                                    			<tr><th class="text-center" width="200">材料内容</th><th class="text-center">照片</th></tr>
                                    		</tbody>
                                    	</table>
                                    	</div>
                                    	<div class="row">
                                                <div class="col-sm-5">
                                                    <div class="col-sm-5 text-right padding-5">公租房材料预审结果：</div>
                                                    <div class="col-sm-7">
                                                        <select class="small-select2 form-control" style="height: 40px;" id = "select_status"></select>
                                                    </div>
                                                </div>
                                                <div class="col-sm-7" id="bt_div">
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="saveRequestUser()" id="bt_save">保存修改</a>
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="sureRequestUser()" id="bt_sure">材料确认</a>
                                                    <a class="btn btn-info margin-right-10" href="javascript:void(0)" onclick="approveRequestUser(1202)">预审通过</a>
                                                    <a class="btn btn-default" href="javascript:void(0)" onclick="approveRequestUser(1203)">预审不通过</a>
                                                </div>
                                            </div>
                                    </div>  
                                 </div>
                             </div>
                         </div>
                     </div>
                    </div>
                </div>

            </div> 
        </div> 
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>

</body>
</html>