var telativeSelectHtml="";
var telativeSelectHtml1="";
var telativeSelectHtml2="";
var telativeSelectHtml3="";
var layerindex;
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
function initPage(){
	isUserAddFun();
	//户籍地2
	var  placeOfDomiciles=dictionaryCodeFun(2);
	if(placeOfDomiciles&&placeOfDomiciles.length>0){ 
		$.each(placeOfDomiciles,function(i,datadictionaryValue){
			var id=datadictionaryValue.id;
			var displayValue=datadictionaryValue.displayValue; 
			var  placeOfDomicileHtml='<label class="radio-inline"><input type="radio" value="'+id+'" name="placeOfDomicile">'+displayValue+'</label>'; 
			$("#placeOfDomicile_div").append(placeOfDomicileHtml);
		});
	}
	$("#placeOfDomicile_div").append('<p class="help-block">注：申请人或申请人配偶一方为上海户籍，则视为上海户籍</p>');
	//婚姻状况3
	var  maritalStatus=dictionaryCodeFun(3);
	$.each(maritalStatus,function(i,datadictionaryValue){
		var  maritalStatuHtml='<label class="radio-inline"><input type="radio" value="'+datadictionaryValue.id+'" name="maritalStatus">'+datadictionaryValue.displayValue+'</label>'; 
		$("#maritalStatus_div").append(maritalStatuHtml);
	});
	//婚姻状况4
	var  housingConditionsInTheCitys=dictionaryCodeFun(4);
	$.each(housingConditionsInTheCitys,function(i,datadictionaryValue){
		var  housingConditionsInTheCityHtml='<label class="radio-inline"><input type="radio" value="'+datadictionaryValue.id+'" name="housingConditionsInTheCity" >'+datadictionaryValue.displayValue+'</label>'; 
		$("#housingConditionsInTheCity_div").append(housingConditionsInTheCityHtml);
	});
	//申请户型
	var  applyForFamilys=dictionaryCodeFun(5);
	var pick = $("#pickDwelling_id").val();
	if(applyForFamilys&&applyForFamilys.length>0){
		$.each(applyForFamilys,function(i,datadictionaryValue){
			if(pick+""=="1601"){
				if(datadictionaryValue.id!=503 && datadictionaryValue.id!=504){
					$("#applyForFamily_id").append('<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>');
				}
			}else if(pick+""=="1602"){
				$("#applyForFamily_id").append('<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>');
			}
		});
	}
	//与本人关系
	var  telativeSelects=dictionaryCodeFun(6);
	if(telativeSelects&&telativeSelects.length>0){
		$.each(telativeSelects,function(i,datadictionaryValue){
			telativeSelectHtml+='<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>';
		});
	}
	//产权人类型
	var  telativeSelects1=dictionaryCodeFun(18);
	if(telativeSelects1&&telativeSelects1.length>0){
		$.each(telativeSelects1,function(i,datadictionaryValue){
			telativeSelectHtml1+='<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>';
		});
	}
	//学历
	var  telativeSelects2=dictionaryCodeFun(19);
	if(telativeSelects2&&telativeSelects2.length>0){
		$.each(telativeSelects2,function(i,datadictionaryValue){
			telativeSelectHtml2+='<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>';
		});
		$("#education_id").append(telativeSelectHtml2);
	}
	//符合依据
	var  telativeSelects3=dictionaryCodeFun(11);
	if(telativeSelects3&&telativeSelects3.length>0){
		$.each(telativeSelects3,function(i,datadictionaryValue){
			telativeSelectHtml3+='<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>';
		});
		$("#accordwith").append(telativeSelectHtml3);
	}
	//所选小区
	/*$("#pickDwelling_id").html("");
	var  pickDwellings=dictionaryCodeFun(16);	
	if(pickDwellings&&pickDwellings.length>0){
		$.each(pickDwellings,function(i,datadictionaryValue){
			$("#pickDwelling_id").append('<option value="'+datadictionaryValue.id+'">'+datadictionaryValue.displayValue+'</option>');
		});
	}*/
}

function isUserAddFun(){
	var  byIdconfig2={
			fromConfig:[{
				formCode:"WDIT_Company_Request_User",
				currentField:"request_id",
				parentId:"id",
				aliasesName:"requestUsers",
				fieldName:"id,username" 
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"pickDwelling",
				aliasesName:"pickDwelling",
				fieldName:"id,displayValue"
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
				if(requestCompany.pickDwelling&&requestCompany.pickDwelling.length>0){
					$("#pickDwelling_id").val(requestCompany.pickDwelling[0].id);
					$("#pickDwelling_Name").html(requestCompany.pickDwelling[0].displayValue);
				}
				if(requestUserId)
					return false;
				if(!applicationNum||applicationNum+""!="0"){
					applicationNum=parseInt(applicationNum);
					if(requestCompany.requestUsers){
						if(requestCompany.requestUsers.length>=applicationNum){
							alert("人数达到上限");
							window.location.href=rootPath+"companyRequest/requestUserList?requestId="+requestId;
						}
					} 
				}
			}
		}
	});
	
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

function initForm(){
	 var array=new Array();
	 array.push("applicantIdPhoto");
	 array.push("residencePermit");
	 array.push("householdRegister");
	 array.push("marriageCertificate");
	 array.push("applicantIdTogether");
	 upLoadFormHtmlFun(array);
}
//增加共同申请人
function addUserTelativeFun(){
	var tr="<tr><td><select class='form-control' >"+telativeSelectHtml+"</select></td><td><input></td><td><input></td><td><a href='javascript:void(0)' onclick='removeUserTelative(this)'>删除</a></td></tr>";
	$("#requestUserTelative_table tbody").append(tr)
}
//删除共同申请人
function  removeUserTelative(me){
	$(me).parents("tr").remove()
}
//下一步
function toUserExpandFun(toformId){
			
	//设置隐藏	
	if(toformId=="userExpand_form"){
		var userName=$("#userName_id").val();
		if(!userName){
			alert(" 请填写 人员姓名");
			return false;
		}else if(userName.length>20){
			alert("人员姓名 超出长度规范！");
			$("#userName_id").val("");
			$("#userName_id").focus();
			return false;
		}
		var placeOfDomicile = $('input:radio[name="placeOfDomicile"]:checked').val();
		if (placeOfDomicile == null) {
	        alert("请选择 户籍地");
	        return false;
	    }
		var placeOfDomicileint = $('input:radio[name="placeOfDomicile"]:checked').val();
		if(placeOfDomicileint==202){
			var residencePermitNumber= $("#residencePermitNumber_id").val();
			if(residencePermitNumber==null||residencePermitNumber==""){
				alert("非上海户籍请填写居住证号码");
				return false;
			}
		}
		var maritalStatus = $('input:radio[name="maritalStatus"]:checked').val();
		if(maritalStatus==null){
			alert("请选择 婚姻状况");
			return false;
		}
		var housingConditionsInTheCity = $('input:radio[name="housingConditionsInTheCity"]:checked').val();
		if(housingConditionsInTheCity==null){
			alert("请选择 本市住房情况");
			return false;
		}
		var identityCardNumber=$("#identityCardNumber_id").val();
		if(!identityCardNumber){
			alert(" 请填写 身份证号码");
			return false;
		}else if(identityCardNumber.length>20){
			alert("身份证号码 超出长度规范！");
			$("#identityCardNumber_id").val("");
			$("#identityCardNumber_id").focus();
			return false;
		}
		var applicantPhone=$("#applicantPhone_id").val();
		if(!applicantPhone){
			alert(" 请填写 申请人电话");
			return false;
		}else if(applicantPhone.length>50){
			alert("申请人电话 超出长度规范！");
			$("#applicantPhone_id").val("");
			$("#applicantPhone_id").focus();
			return false;
		}
		var applyForFamily=$("#applyForFamily_id").val();
		if(!applyForFamily){
			alert(" 请选择 申请户型");
			return false;
		}
		var accord=$("#accordwith").val();
		if(!accord){
			alert(" 请选择 符合依据");
			return false;
		}
		/*
		var pickDwelling=$("#pickDwelling_id").val();
		if(!pickDwelling){
			alert("请选择 所选小区  ");
			return false;
		}*/
		var permanentAddress=$("#permanentAddress_id").val();
		if(!permanentAddress){
			alert(" 请填写 户籍地址");
			return false;
		}else if(permanentAddress.length>300){
			alert("户籍地址 超出长度规范！");
			$("#permanentAddress_id").val("");
			$("#permanentAddress_id").focus();
			return false;
		}
		var address_id=$("#address_id").val();
		if(address_id&&address_id.length>200){
			alert("联系地址 超出长度规范！");
			$("#address_id").val("");
			$("#address_id").focus();
			return false;
		}
		
		var residencePermitNumber=$("#residencePermitNumber_id").val();
		if(residencePermitNumber&&residencePermitNumber.length>50){
			alert("居住证号码 超出长度规范！");
			$("#residencePermitNumber_id").val("");
			$("#residencePermitNumber_id").focus();
			return false;
		}
		
		var housingAccumulationFundAccount_id=$("#housingAccumulationFundAccount_id").val();
		if(housingAccumulationFundAccount_id&&housingAccumulationFundAccount_id.length>30){
			alert("住房公积金账户 超出长度规范！");
			$("#housingAccumulationFundAccount_id").val("");
			$("#housingAccumulationFundAccount_id").focus();
			return false;
		}
		var educationCardCode=$("#educationCardCode_id").val();
		if(educationCardCode&&educationCardCode.length>50){
			alert("学历证书编码 超出长度规范！");
			$("#educationCardCode_id").val("");
			$("#educationCardCode_id").focus();
			return false;
		}
		var graduationSchool=$("#graduationSchool_id").val();
		if(graduationSchool&&graduationSchool.length>100){
			alert("毕业院校 超出长度规范！");
			$("#graduationSchool_id").val("");
			$("#graduationSchool_id").focus();
			return false;
		}
		var graduationTime=$("#graduationTime_id").val();
		var specialty=$("#specialty_id").val();
		if(specialty&&specialty.length>50){
			alert("专业 超出长度规范！");
			$("#specialty_id").val("");
			$("#specialty_id").focus();
			return false;
		}
		//住房 
		if(housingConditionsInTheCity+"" =="401"){
			if(!requestUserId){
				$("#user_housing_table").html("");
				adduserHousing();
			}
			$("#housing_tables").show();
		}else 
			$("#housing_tables").hide();
		//户籍地
		if(placeOfDomicile=="201"){
			$("#placeOfDomicile1_photo_tr").show();//户口本
			$("#placeOfDomicile2_photo_tr").hide();//居住证
		}
		if(placeOfDomicile=="202"){
			$("#placeOfDomicile2_photo_tr").show()
			$("#placeOfDomicile1_photo_tr").hide()
		}
		//婚姻状况 
		if(maritalStatus+""=="301"||maritalStatus+""=="303")
			$("#maritalStatus_photo_tr").show();
		else 
			$("#maritalStatus_photo_tr").hide();
		//共同申请人
		 var telativePhotoTrs=$(".telativePhoto");
		 if(!requestUserId){
			 telativePhotoTrs.remove();
		 }else{
			 $.each(telativePhotoTrs,function(i,tr){
				 var isExist=false;
				 var relativeName=$(tr).attr("relativeName");
				 if($("#requestUserTelative_table tbody tr").length>0){
						var relativeArray=new Array();
						$("#requestUserTelative_table tbody tr").each(function(j,tr){
							var tds=$(tr).find("td"); 
							var name= tds.eq(1).find("input").eq(0).val();
							if(relativeName==name+"_name")
								isExist=true;
						});
				 }		
				 if(!isExist)
					 $(tr).remove();
			 });
		 }
		 
		if($("#requestUserTelative_table tbody tr").length>0){
			var istrue=true;
			var relativeNames=new Array();
			var isrelative1=1;
			$("#requestUserTelative_table tbody tr").each(function (i,userTelative){
				var inputs= $(userTelative).find("input");
				var selects= $(userTelative).find("select").eq(0).val();
				var userTelativeName=inputs.eq(0).val();
				var idNumber= inputs.eq(1).val();
				if(selects+""=="601" ){
					if(isrelative1==2){
						alert("配偶不能重复");
						isrelative1=0;
						istrue=false;
						return false;
					}
					if(maritalStatus+""!="301"){
						alert("婚姻状况 不应存在配偶");
						isrelative1=0;
						istrue=false;
						return false;
					}else {
						isrelative1=2;
					}
				}
				
				if(!userTelativeName){
					alert("请填写共同申请人 姓名");
					istrue=false;
					return false;
				}else if(!idNumber){
					alert("请填写共同申请人 身份证号码");
					istrue=false;
					return false;
				}if(userTelativeName.length>20){
					alert("共同申请人姓名超出长度规范");
					istrue=false;
					return false;
				}if(idNumber.length>20){
					alert("共同申请人身份证号码超出长度规范");
					istrue=false;
					return false;
				}
				if(relativeNames.length>0){
					var  isrelative=true;
					$.each(relativeNames,function (index,relativeName){
						var relativeName= relativeNames[index];
						if(relativeName==userTelativeName || relativeName==idNumber){
							alert("请填写共同申请人 姓名  身份证号码 存在重复");
							istrue=false;
							return false;
						}
					});
					if(!isrelative)
						return false;
				}
				relativeNames.push(userTelativeName);
				relativeNames.push(idNumber);
				var isExist=false;
				 var telativePhotoTrs2=$(".telativePhoto");
				 $.each(telativePhotoTrs2,function(i,tr){
					 var relativeName=$(tr).attr("relativeName");
					 if(relativeName==userTelativeName+"_name"){ 
						 isExist=true;
					 } 
				 });
				 if(!isExist){
					 var trHtml='<tr id="requestUserTelative_photo_tr_'+i+'"  relativeName="'+userTelativeName+'_name" class="telativePhoto"><td>共同申请人证件('+userTelativeName+')<br>请分别上传身份证正反面照片、户口本全页（仅限上海户口）</td><td>'
		            	+'<form style="float:left;width:100%;text-align:left;" enctype="multipart/form-data" method="post"  name="housingPropertyCard_form_name"  action="'+rootPath+'/fileUploadDownLoad/uploadImgeFile" >'
						+'<input type="hidden"  name="force" value="1" /><input type="hidden"  name="w" value="128" /><input type="hidden"  name="h" value="96" />'
						+'<input type="file"  name="file"   /> <input type="button" onclick="submitImageOnChange2(this)" value="上传" ></form>'
		            	+'</td><td></td></tr>';
					$("#userPhoto_table tbody").append(trHtml);
					var form= $("#userPhoto_table tbody form").last();
				    formUpload(form);
				 }
			}); 
			if(!istrue)
				return false; 
		}

		initForm();
		$("#userExpand_form").show();
		$("#user_form").hide();
	}else {	
		$("#userExpand_form").hide();
		$("#user_form").show();
	}
}
function areaCheckFun(me){
	var  checkboxs= $(me).parent().parent().parent().find("input[name='areaTyep']");
	$.each(checkboxs,function(i,areaTyep){
		$(areaTyep).prop("checked",false);
	});
	$(me).prop("checked",true);
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
//  增加住房信息 
function  adduserHousing(){
	var table="<table class='table table-bordered table-hover text-center table-horizontal'>"+
				"<tbody><tr>" +
						"<th width='200' class='text-right'>房屋坐落地址</th>" +
						"<td class='text-left'><input name='housingLocatedAddress'></td>" +
						"<th width='100' rowspan='5' class='text-center'><a  href='javascript:void(0)' onclick='removeUserHousing(this);' > 删除 </a></th></tr>"+
					"<tr>" +
						"<th class='text-right'><label class='checkbox-inline'><input type='checkbox' checked='checked' onclick='areaCheckFun(this)' name='areaTyep' value='1'>建筑面积</label><label class='checkbox-inline'><input type='checkbox' onclick='areaCheckFun(this)' name='areaTyep' value='2'>居住面积</label></th>" +
						'<td class="text-left"><input name="area" onblur="areaFun(this)"> </td></tr>' +
					"<tr>" +
						"<th class='text-right'>产权人或承租人</th>" +
						"<td class='text-left'><input name='propertyOwner'> </td></tr>" +
					"<tr>" +
						"<th class='text-right'>产权人类型</th>" +
						"<td class='text-left'><select id='ownerType' class='form-control'>"+telativeSelectHtml1+"</select></td></tr>" +
					"<tr>" +
						"<th class='text-right'>该住房户籍人口总数</th>" +
						'<td class="text-left"><input name="theHousingAllNumpeople"  onkeyup="value=value.replace(/[^\\d]/g,\'\')" ></td></tr>' +
					"<tr rowspan='6'>" +
						"<th class='text-right'>房产证或使用权凭证照片</th>" +
						"<td name='housingPropertyCard_name' class='text-right'>" +
						 	'<form style="float:left;width:100%;text-align:left;" enctype="multipart/form-data" method="post"  name="housingPropertyCard_form_name"  action="'+rootPath+'/fileUploadDownLoad/uploadImgeFile" >'
							+'<input type="hidden"  name="force" value="1" /><input type="hidden"  name="w" value="128" /><input type="hidden"  name="h" value="96" />'
							+'<input type="file"  name="file"   /> <input type="button" onclick="submitImageOnChange2(this,1)" value="上传" ></form>'
						"</td></tr>" +
					"</tbody></table> ";
	$("#user_housing_table").append(table)
	var form= $("#user_housing_table form").last();
	formUpload(form);
}
function formUpload(form){
	form.ajaxForm({
		dataType : 'text',
		success : function(json) {
			if (json.indexOf("pre") > 0|| json.indexOf("PRE") > 0) {
				json = json.substring(5);
				json = json.substring(0, json.length - 6);
			}
			var data = JSON.parse(json);
			if (data.code + "" == "1") {
				var str='<div style="float:left;margin-bottom:10px;margin-right:10px;"><input type="hidden" value="'+data.imageName+'" /><input type="hidden" value="'+data.imagePath+'"/><input type="hidden" value="'+data.smallImagePath+'"/> <a onClick="showPhoto(\''+rootPath+data.imagePath+'\')"><img src="'+rootPath+data.smallImagePath+'"/> </a>&nbsp;<a href="javascript:void(0)" onclick="deletePhoto(this);">删除</a></div>';
				form.before(str);
			} else {
				alert("上传失败");
			}
		}
	});
}
function removeUserHousing(me){
	$(me).parent().parent().parent().parent().remove();
}
//type:1 住房证件 2 共同申请人照片
function submitImageOnChange2(me,type){	
	var fileName=$(me).prev().val();
	 if (fileName != null && fileName.length > 0) {
	     var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase(); 
	     if (fileType == "BMP" || fileType== "WBMP" ||fileType == "JPEG"|| fileType== "PNG"|| 
	     		fileType== "TIF" || fileType== "GIF"||fileType== "JPG") {
	    	 $(me).parent().submit();
	     } else {
	         alert("Please Upload BMP, WBMP, JPEG, PNG, TIF, GIF,JPG ！");
	         $("#largeAdvertImage").val("");
	     }
	 }
}
//上传
function upLoadFormHtmlFun(array){
	$.each(array,function(i,id){
		if($("#"+id+"_td form").length<=0){
			var formhtml = ' <form style="float:left;width:100%;text-align:left;" enctype="multipart/form-data" method="post"  id="'+id+'_form_id"  action="'+rootPath+'/fileUploadDownLoad/uploadImgeFile" >'
			+'<input type="hidden"  name="force" value="1" /><input type="hidden"  name="w" value="128" /><input type="hidden"  name="h" value="96" />'
			+'<input type="file"  name="file" id="'+id+'_file_id" /> <input type="button" onclick="submitImageOnChange(\''+id+'\')" value="上传" ></form>';
		    if(!requestUserId)
		    	$("#"+id+"_td").html("");
		    $("#"+id+"_td").append(formhtml);
		    uploadForm(id);	
		}
	});
} 
function submitImageOnChange(id){
	var isUpload=true;
	if(id=="applicantIdPhoto"){//申请人身份证
		var divs= $("#"+id+"_form_id ~ div");
		if(divs && divs.length>=2){
			alert("申请人身份证  最多上传两张");
			isUpload=false;
			return false;
		}
	}else if(id=="residencePermit"){ //申请人居住证
		var divs= $("#"+id+"_form_id ~ div");
		if(divs && divs.length>=2){
			alert("申请人居住证  最多上传两张");
			isUpload=false;
			return false;
		}
	}else if(id=="marriageCertificate"){//结婚证
		var divs= $("#"+id+"_form_id ~ div");
		if(divs && divs.length>=1){
			alert("申请人婚姻证明  最多上传两张");
			isUpload=false;
			return false;
		}
	}
	if(isUpload){
		 var fileName = $("#"+id+"_file_id").val();
		 if (fileName != null && fileName.length > 0) {
		     var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase();
		     if (fileType == "BMP" || fileType== "WBMP" ||fileType == "JPEG"|| fileType== "PNG"|| 
		     		fileType== "TIF" || fileType== "GIF"||fileType== "JPG") {
		    	 $("#"+id+"_form_id").submit();
		     } else {
		         alert("Please Upload BMP, WBMP, JPEG, PNG, TIF, GIF, JPG ！");
		         $("#largeAdvertImage").val("");
		     }
		 }
	}
	 
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
					var str="<div style='float:left;margin-bottom:10px;margin-right:10px;'><input type='hidden' value='"+data.imageName+"' /><input type='hidden' value='"+data.imagePath+"'/><input type='hidden' value='"+data.smallImagePath+"'/>  <a onClick='showPhoto(\""+rootPath+data.imagePath+"\")'> <img src='"+rootPath+data.smallImagePath+"'></a> &nbsp;<a href='javascript:void(0)' onclick='deletePhoto(this);'>删除</a></div>";
					$("#"+id+"_form_id").before(str);
				} else {
					alert("上传失败");
				}
			}
		});
}
function deletePhoto(me){
	$(me).parent().remove();
}

function saveRequestUser(){
	var maritalStatus = $('input:radio[name="maritalStatus"]:checked').val();//	婚姻状况
	var istrue=true;
	var detail=new Array();
	var userHousingTables =$("#user_housing_table table")
	if(!(userHousingTables.length>0)){
		var housingConditionsInTheCity = $('input:radio[name="housingConditionsInTheCity"]:checked').val();//本市住房情况
		if(housingConditionsInTheCity+""=="401"){
			alert("请填写本市户籍住房信息");
			istrue=false;	
		}		
	}else {
		var  housingArray=new Array();
		userHousingTables.each(function(i,table){			
			var areaType=  $(table).find("input:checkbox[name='areaTyep']:checked").eq(0).val();
			var tds= $(table).find("td");
			var housingLocatedAddress=tds.eq(0).find("input").eq(0).val();
			if(!housingLocatedAddress){
				alert("请填写 房屋坐落地址");
				istrue=false;
				return false;
			}if(housingLocatedAddress.length>300){
				alert("房屋坐落地址 超出长度规范");
				istrue=false;
				return false;
			}
			var area=tds.eq(1).find("input").eq(0).val();
			if(!area){
				alert("请填写 面积");
				istrue=false;return false;
			}if(area.length>10){
				alert(" 面积 超出额度规范");
				istrue=false;
				return false;
			}
			var propertyOwner=tds.eq(2).find("input").eq(0).val();
			if(!propertyOwner){
				alert("请填写 产权人或承租人");
				istrue=false;return false;
			}if(propertyOwner.length>100){
				alert("产权人或承租人 超出长度度规范");
				istrue=false;
				return false;
			}
			var theHousingAllNumpeople=tds.eq(4).find("input").eq(0).val();
			if(!theHousingAllNumpeople){
				alert("请填写 该住房户籍人口总数");
				istrue=false;return false;
			}if(theHousingAllNumpeople.length>8){
				alert(" 该住房户籍人口总数  超出长度度规范");
				istrue=false;
				return false;
			}
			var  divs =tds.eq(5).find("div");
			if(divs.length<=0){
				alert("请填写 房产证或使用权凭证照片");
				istrue=false;return false;
			}
			var ownerType = $('#ownerType').val();
			if(maritalStatus=="301"){
				if(ownerType==null){
					alert("请选择 产权类型");
					istrue=false;return false;
				}
			}else{
				if(ownerType==1802){
					alert("婚姻状况 不应选择配偶");
					istrue=false;return false;
				}
			}
			
			var housingObject= {
					"housingLocatedAddress":housingLocatedAddress,
					"areaType":areaType,
					"area":area,
					"propertyOwner":propertyOwner,
					"theHousingAllNumpeople":theHousingAllNumpeople,
					"ownerType":ownerType
			}
			var inputId=tds.eq(1).find("input").eq(1);
			if(inputId){
				housingObject.id=inputId.val();
			}
			var userPhotoArray=new Array();
			divs.each(function(i,div){
				var inputs= $(div).find("input");
				if(inputs.length>0){
					var photoName=inputs.eq(0).val();
					var photo=inputs.eq(1).val();
					var smallphoto=inputs.eq(2).val();
					userPhotoArray.push({"type":1,"name":photoName,"smallPhoto":smallphoto,"photo":photo});
				}
			});
			var detailphotos=new Array();
			detailphotos.push({"formId":65,"parentId":"user_housing_id","array":userPhotoArray});	
			housingObject.detail=detailphotos;
			housingArray.push(housingObject);
		});
		var subStructure=[{"formId":64,"parentId":"requestUser_id",subStructure:[{"formId":65,"parentId":"user_housing_id"}]}];
		detail.push({"formId":64,"parentId":"requestUser_id","array":housingArray,"subStructure":subStructure});
	}
	if(!istrue) 
		return false;
	var userPhoto2Array=new Array(); 
	var istrue=true;
	 $("#userPhoto_table tbody tr td:nth-child(2) ").each(function(i,td){
		 if(i>3){
			 return false;
		 }
		 var issave=true;
		 if(i==3) {//婚姻状况
			var maritalStatus = $('input:radio[name="maritalStatus"]:checked').val();
			if(maritalStatus+""!="301" && maritalStatus+""!="303"){
				issave=false;		
			}
		 }
		 var placeOfDomicile = $('input:radio[name="placeOfDomicile"]:checked').val();//户籍地
		 if(issave){
			 var divs= $(td).find("div");
			 if(!divs || divs.length <= 0){
				 var ispass =true;
				 if(i==1){
					 if(placeOfDomicile+""=="202"){
						 ispass =false;
					 } 
				 }else if(i==2){
					 if(placeOfDomicile+""=="201")
						 ispass =false;
				 }else 
					 ispass =false;
				 if(!ispass){
					 var photo=userPhotoDescription(i);
					 alert("请 上传 "+photo);
					 istrue=false;return false;
				 }
			 }
			if(i==0&&divs.length!=2){
				alert("申请人身份证 只能上传两张");
				istrue=false;return false;
			}else if(i==1&&divs.length!=2){
				if(placeOfDomicile+""=="202"){
					alert("申请人居住证  只能上传两张");
					istrue=false;return false;
				}
			}else if(i==2&&divs.length<1){
				if(placeOfDomicile+""=="201"){
					alert("申请人户口本  至少上传一张");
					istrue=false;return false;
				}
			}else if(i==3&&divs.length!=1){
				var maritalStatus = $('input:radio[name="maritalStatus"]:checked').val();//	婚姻状况
				if(maritalStatus+""=="301"||maritalStatus+""=="303"){
					alert("申请人婚姻证明  只能上传一张");
					istrue=false;return false;
				}
			}	
			 $.each(divs,function(j,div){
				 var inputs= $(div).find("input");
				 if(inputs.length>0){
					 var photoName=inputs.eq(0).val();
					 var photo=inputs.eq(1).val();
					 var smallphoto=inputs.eq(2).val();
					 if(!photo){
						 var photo=userPhotoDescription(i);
						 alert("请 上传 "+photo);
						 istrue=false;
						 return false;
					 }
					 userPhoto2Array.push({"type":(i+2),"name":photoName,"smallPhoto":smallphoto,"photo":photo});
				 }	 
			 })
			 
		 }
	  });
	 if(!istrue)
		 return false;
	 detail.push({"formId":65,"parentId":"requestUser_id","array":userPhoto2Array});
	
	var userName=$("#userName_id").val();//人员姓名
	var placeOfDomicile = $('input:radio[name="placeOfDomicile"]:checked').val();//户籍地
	var address=$("#address_id").val();//联系地址
	var permanentAddress=$("#permanentAddress_id").val();//户籍地址
	var housingAccumulationFundAccount=$("#housingAccumulationFundAccount_id").val();//住房公积金账户
	var residencePermitNumber=$("#residencePermitNumber_id").val();//居住证号码
	var applyForFamily=$("#applyForFamily_id").val();//申请户型
	var applicantPhone=$("#applicantPhone_id").val();//申请人电话
	var identityCardNumber=$("#identityCardNumber_id").val();//	身份证号码
	var housingConditionsInTheCity = $('input:radio[name="housingConditionsInTheCity"]:checked').val();//本市住房情况
	var education=$("#education_id").val();
	var educationCardCode=$("#educationCardCode_id").val();
	var graduationSchool=$("#graduationSchool_id").val();
	var graduationTime=$("#graduationTime_id").val();
	var specialty=$("#specialty_id").val();
	var accordwith=$("#accordwith").val();
//	var pickDwelling=$("#pickDwelling_id").val();
	var register={
			"request_id":requestId,
			"userName":userName,
			"placeOfDomicile":placeOfDomicile,
			"maritalStatus":maritalStatus,
			"housingConditionsInTheCity":housingConditionsInTheCity,
			"identityCardNumber":identityCardNumber,
			"applicantPhone":applicantPhone,
			"applyForFamily":applyForFamily,
			"permanentAddress":permanentAddress,
			"address":address,
//			"pickDwelling":pickDwelling,
			"status":1201,
			"education":education,
			"educationCardCode":educationCardCode,
			"graduationSchool":graduationSchool,
			"graduationTime":graduationTime,
			"specialty":specialty,
			"accordwith":accordwith
		}
	if(residencePermitNumber) 
		register.residencePermitNumber=residencePermitNumber;
	if(housingAccumulationFundAccount)
		register.housingAccumulationFundAccount=housingAccumulationFundAccount;
	if(permanentAddress)
		register.permanentAddress=permanentAddress;
	var requestUserObj={
			"formId":62,
			"register":register
		};
	if($("#requestUserTelative_table tbody tr").length>0){
		var relativeArray=new Array();
		$("#requestUserTelative_table tbody tr").each(function(i,tr){
			var tds=$(tr).find("td");
			var relative= tds.eq(0).find("select").eq(0).val();
			var name= tds.eq(1).find("input").eq(0).val();
			var identityCardNumber= tds.eq(2).find("input").eq(0).val();
			var arrayTelative=new Array();
			var divs=$("#requestUserTelative_photo_tr_"+i+"  div");
			 if(!divs || divs.length <= 0){
				alert("请 上传  共同申请人 "+name +" 照片");
				istrue=false;return false;
			 }else if(divs.length<2){
				alert("请 上传  共同申请人 "+name +" 照片 ,至少两张 ");
				istrue=false;return false;
			 }
			divs.each(function(j,div){
				 var inputs= $(div).find("input");
				 var photoName=inputs.eq(0).val();
				 var photo=inputs.eq(1).val();
				 var smallphoto=inputs.eq(2).val();
				 if(!photo){
					 var photo=userPhotoDescription(i);
					 alert("请 上传 共同申请人证件"+photo);
					 istrue=false;
					 return false;
				 }
				 arrayTelative.push({"type":6,"name":photoName,"smallPhoto":smallphoto,"photo":photo});	
			 });
			 var detailTelative=new Array();
			detailTelative.push({"formId":65,"parentId":"user_relative_id","array":arrayTelative});
			relativeArray.push({"relative":relative,"name":name,"identityCardNumber":identityCardNumber,"detail":detailTelative});
		});
		var subStructure=[{"formId":63,"parentId":"requestUser_id",subStructure:[{"formId":65,"parentId":"user_relative_id"}]}];
		detail.push({"formId":63,"parentId":"requestUser_id","array":relativeArray,"subStructure":subStructure});
	}
	 if(!istrue)
		 return false;
	requestUserObj.detail=detail;
	if(requestUserId)
		requestUserObj.tableDataId=requestUserId; 
	 
	var index = layer.load(2);
	$.ajax({
		url : rootPath+'/forms/saveFormDataJson',
		type : "POST", 
      	async: false,
      	data : {
      		json : JSON.stringify(requestUserObj)
      	}, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.successMsg){ 
				window.location.href=rootPath+"companyRequest/requestUserList?requestId="+requestId;
		 	}
		   }
      });
	
}

function userPhotoDescription(i){
	if(i==0){
		return "申请人身份证";
	}else if(i==1){
		return "申请人居住证";
	}else if(i==2){
		return "申请人户口本";
	}else if(i==3){
		return "申请人婚姻证明";
	}else if(i==4){
		return "共同申请人证件";
	}
}
