function getRequestUserFun(requestUserId){
	var  byIdconfig2={
			fromConfig:[{
				formCode:"WDIT_Company_Request_User_relative",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"userRelative",
				fieldName:"id,identityCardNumber,name,relative",
				fromConfig:[{
					formCode:"WDIT_Company_Request_User_photo",
					currentField:"user_relative_id",
					parentId:"id",
					aliasesName:"userRelativePhoto",
					fieldName:"id,type,name,smallPhoto,photo",
				}]
			},{
				formCode:"WDIT_Company_Request_User_housing",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"userHousing",
				fieldName:"id,housingLocatedAddress,areaType,area,propertyOwner,theHousingAllNumpeople,ownerType"
			},{
				formCode:"WDIT_Company_Request_User_photo",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"userPhoto",
				fieldName:"id,type,name,smallPhoto,photo"
			}]
	}
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request_User",
			condition: " id="+requestUserId,
			byIdconfig:JSON.stringify(byIdconfig2)
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.code + "" == "1"){
				var results = data.results;  
				if(results&&results.length>0){
					var requestUser=results[0];
					var userRelatives=requestUser.userRelative;
					var userHousings=requestUser.userHousing;
					var userPhotos=requestUser.userPhoto;
					
					var address=requestUser.address;//联系地址
					var permanentAddress=requestUser.permanentAddress;//户籍址
					var housingAccumulationFundAccount=requestUser.housingAccumulationFundAccount;//住房公积金账户
					var residencePermitNumber=requestUser.residencePermitNumber;//居住证号码
					var applyForFamily=requestUser.applyForFamily;//申请户型
					var applicantPhone=requestUser.applicantPhone;//申请人电话
					var identityCardNumber=requestUser.identityCardNumber;//身份证号码
					var housingConditionsInTheCity=requestUser.housingConditionsInTheCity;//本市住房情况
					var maritalStatus=requestUser.maritalStatus;//婚姻状况
					var placeOfDomicile=requestUser.placeOfDomicile;//户籍地
					var userName=requestUser.userName;//人员姓名
					var pickDwelling=requestUser.pickDwelling;//所选小区
					var education=requestUser.education;
					var educationCardCode=requestUser.educationCardCode;
					var graduationSchool=requestUser.graduationSchool;
					var graduationTime=requestUser.graduationTime;
					var specialty=requestUser.specialty;
					var accordwith=requestUser.accordwith;
					
					$('#userName_id').val(userName);
					$('#address_id').val(address);
					$('#permanentAddress_id').val(permanentAddress);
					$('#housingAccumulationFundAccount_id').val(housingAccumulationFundAccount);
					$('#residencePermitNumber_id').val(residencePermitNumber);
					$('#applyForFamily_id').val(applyForFamily);
					$('#applicantPhone_id').val(applicantPhone);
					$('#identityCardNumber_id').val(identityCardNumber);	
					$('#pickDwelling_id').val(pickDwelling);
					$("#education_id").val(education);
					$("#educationCardCode_id").val(educationCardCode);
					$("#graduationSchool_id").val(graduationSchool);
					$("#graduationTime_id").val(graduationTime);
					$("#specialty_id").val(specialty);
					$("#accordwith").val(accordwith);
					 $("input[name='placeOfDomicile'][value="+placeOfDomicile+"]").prop("checked",true);
					 $("input[name='maritalStatus'][value="+maritalStatus+"]").prop("checked",true);
					 $("input[name='housingConditionsInTheCity'][value="+housingConditionsInTheCity+"]").prop("checked",true);
					 if(userRelatives&&userRelatives.length>0){
						 $.each(userRelatives,function(i,userRelative){
							 var tr="<tr><td id='"+userRelative.id+"'><select class='form-control' id='"+userRelative.id+"_RelativeId'>"+telativeSelectHtml+"</select></td><td><input value='"+userRelative.name+"'></td><td><input  value='"+userRelative.identityCardNumber+"'></td><td><a href='javascript:void(0)' onclick='removeUserTelative(this)'>删除</a></td></tr>";
							 $("#requestUserTelative_table tbody").append(tr);
							 $("#"+userRelative.id+"_RelativeId").val(userRelative.relative);
							 
							 var trHtml='<tr id="requestUserTelative_photo_tr_'+i+'" relativeName="'+userRelative.name+'_name" class="telativePhoto"><td>共同申请人证件('+userRelative.name+')<br>请分别上传身份证正反面照片、户口本全页（仅限上海户口）</td><td>'
				            	+'<form  id="requestUserTelative_photo_from_'+i+'"  enctype="multipart/form-data" method="post"  name="housingPropertyCard_form_name"  action="'+rootPath+'/fileUploadDownLoad/uploadImgeFile" >'
								+'<input type="hidden"  name="force" value="1" /><input type="hidden"  name="w" value="128" /><input type="hidden"  name="h" value="96" />'
								+'<input type="file"  name="file"   /> <input type="button" onclick="submitImageOnChange2(this)" value="上传" ></form>'
				            	+'</td><td></td></tr>';
							$("#userPhoto_table tbody").append(trHtml);
							var form= $("#userPhoto_table tbody form").last();
						    formUpload(form);
						    if(userRelative.userRelativePhoto){
							    $.each(userRelative.userRelativePhoto,function(i,userRelativePhoto){
									var str="<div><input type='hidden' value='"+userRelativePhoto.name+"' /><input type='hidden' value='"+userRelativePhoto.photo+"'/><input type='hidden' value='"+userRelativePhoto.smallPhoto+"'/><a onClick='showPhoto(\""+rootPath+userRelativePhoto.photo+"\")'> <img src='"+rootPath+userRelativePhoto.smallPhoto+"'></a>&nbsp;<a href='javascript:void(0)' onclick='deletePhoto(this);'>删除</a></div>";
									form.before(str);
								});
						    }
						 });
					 }
					 if(userHousings&&userHousings.length){
						 updateUserHousingFun(userHousings);
					 }
					 if(userPhotos&&userPhotos.length){
						 userPhotosFun(userPhotos);
					 }
				}
			}else{
				$("#form_table").html("<div style='text-align: center'>没有记录</div>");
			}
		}
	}); 
}
function areaCheckFun(me){
	var  checkboxs= $(me).parent().parent().parent().find("input[name='areaTyep']");
	$.each(checkboxs,function(i,areaTyep){
		$(areaTyep).prop("checked",false);
	});
	$(me).prop("checked",true);
}
function  updateUserHousingFun(userHousings){
	$("#user_housing_table").html("");
	$.each(userHousings,function(i,userHousing){
		var housingPhotos=gethousingPhoto(userHousing.id);
		
		var table="<table class='table table-bordered table-hover text-center table-horizontal'>"+
		"<tbody><tr>" +
			"<th width='200' class='text-right'>房屋坐落地址</th>" +
			"<td class='text-left'><input name='housingLocatedAddress' value='"+userHousing.housingLocatedAddress+"' ><input type='hidden' value='"+userHousing.id+"'></td>" +
			"<th width='100' rowspan='5' class='text-center'><a  href='javascript:void(0)' onclick='removeUserHousing(this);' > 删除 </a>  </th></tr>"+
		"<tr>"+
			"<th class='text-right'>" ;
			if(userHousing.areaType+""==1){
				table +="<label class='checkbox-inline'><input type='checkbox' checked='checked' onclick='areaCheckFun(this)' name='areaTyep' value='1'>建筑面积</label>" +
						"<label class='checkbox-inline'><input type='checkbox' onclick='areaCheckFun(this)' name='areaTyep' value='2'>居住面积</label></th>" ;
			}else if(userHousing.areaType+""==2){
				table +="<label class='checkbox-inline'><input type='checkbox' onclick='areaCheckFun(this)' name='areaTyep' value='1'>建筑面积</label>" +
				"<label class='checkbox-inline'><input type='checkbox' checked='checked' onclick='areaCheckFun(this)' name='areaTyep' value='2'>居住面积</label></th>" ;
			}
			table +='<td class="text-left"><input name="area" value="'+userHousing.area+'"   onblur="areaFun(this)"> </td></tr>' +
		"<tr>" +
			"<th class='text-right'>产权人或承租人</th>" +
			"<td class='text-left'><input name='propertyOwner' value='"+userHousing.propertyOwner+"'> </td></tr>" +
		"<tr>" +
			"<th class='text-right'>产权人类型</th>" +
			"<td class='text-left'><select id='ownerType'>"+telativeSelectHtml1+" </select></td></tr>" +
		"<tr>" +
			"<th class='text-right'>该住房户籍人口总数</th>" +
			'<td class="text-left"><input name="theHousingAllNumpeople" value="'+userHousing.theHousingAllNumpeople+'"  onkeyup="value=value.replace(/[^\\d]/g,\'\')" ></td></tr>' +
		"<tr rowspan='6'>" +
			"<th class='text-right'>房产证或使用权凭证照片</th>" +
			"<td name='housingPropertyCard_name' class='text-right'>" +
			 	'<form enctype="multipart/form-data" method="post"  name="housingPropertyCard_form_name"  action="'+rootPath+'/fileUploadDownLoad/uploadImgeFile" >'
				+'<input type="hidden"  name="force" value="1" /><input type="hidden"  name="w" value="128" /><input type="hidden"  name="h" value="96" />'
				+'<input type="file"  name="file"   /> <input type="button" onclick="submitImageOnChange2(this)" value="上传" ></form>'
			"</td></tr>" +
		"</tbody></table> ";
		$("#user_housing_table").append(table); 
		$("#ownerType").val(userHousing.ownerType);
		var form= $("#user_housing_table form").last();
		if(housingPhotos&&housingPhotos.length>0){
			$.each(housingPhotos,function(i,housingPhoto){
				var str="<div><input type='hidden' value='"+housingPhoto.name+"' /><input type='hidden' value='"+housingPhoto.photo+"'/><input type='hidden' value='"+housingPhoto.smallPhoto+"'/> <a onClick='showPhoto(\""+rootPath+housingPhoto.photo+"\")'> <img src='"+rootPath+housingPhoto.smallPhoto+"'></a>&nbsp;<a href='javascript:void(0)' onclick='deletePhoto(this);'>删除</a></div>";
				form.before(str);
			});
		}
		form.ajaxForm({
			dataType : 'text',
			success : function(json) {
			if (json.indexOf("pre") > 0|| json.indexOf("PRE") > 0) {
				json = json.substring(5);
				json = json.substring(0, json.length - 6);
			}
			var data = JSON.parse(json);
			if (data.code + "" == "1") {
				var str="<div><input type='hidden' value='"+data.imageName+"' /><input type='hidden' value='"+data.imagePath+"'/><input type='hidden' value='"+data.smallImagePath+"'/> <a onClick='showPhoto(\""+rootPath+data.imagePath+"\")'><img src='"+rootPath+data.smallImagePath+"'></a>&nbsp;<a href='javascript:void(0)' onclick='deletePhoto(this);'>删除</a></div>";
				form.before(str);
			} else {
				alert("上传失败");
			}
			}
		});
	});	
}
function  gethousingPhoto(userHousingId){
	var housingPhotos;
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request_User_photo",
			condition: " user_housing_id="+userHousingId
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.code + "" == "1"){
				housingPhotos = data.results; 
			}
		}
	});
	return housingPhotos;
}
function userPhotosFun(userPhotos){
	$.each(userPhotos,function(i,userPhoto){
		var td= $("#userPhoto_table tbody tr td:nth-child(2) ")[userPhoto.type-2];
		var str="<div><input type='hidden' value='"+userPhoto.name+"' /><input type='hidden' value='"+userPhoto.photo+"'/><input type='hidden' value='"+userPhoto.smallPhoto+"'/><a onClick='showPhoto(\""+rootPath+userPhoto.photo+"\")'><img src='"+rootPath+userPhoto.smallPhoto+"'></a>&nbsp;<a href='javascript:void(0)' onclick='deletePhoto(this);'>删除</a></div>";
		$(td).append(str);
	});
}