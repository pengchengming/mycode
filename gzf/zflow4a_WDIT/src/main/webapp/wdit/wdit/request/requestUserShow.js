function initPage(){
	getuser();
//	getRelative();
//	userHousing();
//	userPhoto();
//	getvalue();
	userApprovalList(step);
}
 
function userApprovalList(step){
	$.post(rootPath+'/createSelect/findselectData', { 
		code : "approvaluser_v1",
		selectConditionSql : " where t1.status not in(1204,2204,4204,5204) and t1.approvalStep<="+step+" and t1.requestUser_id="+requestUserId
	}, function(data) {
		if (data.code + "" == "1") {
			var results = eval("(" + data.results + ")");
			if(results &&results.length>0){
				$.each(results,function(i,json){
					var approval_Date="";
					if(json.approval_Date){
						try{
							approval_Date=new Date(parseInt(json.approval_Date)*1000).format('yyyy-MM-dd hh:mm')
						}catch (e) {}
					}
					var  trhtml="<tr><td class='text-right' width='150'>"+json.approvalStep+"</td>" +
							"<td id='step4' class='text-left' style='color:green' width='80'>"+json.status+"</td>" +
							"<td class='text-right' width='100'>审核时间：</td><td id='time4' class='text-left' style='color:green'>"+approval_Date+"</td>" +
							"<td class='text-right' width='100'>审核人:</td><td id='user4' class='text-left' style='color:green'" ;
							if(!json.approvalBasis){
								if(!json.acceptanceNumber){
									trhtml+="colspan='3' >"+json.realname+"</td>";
								}else{
									trhtml+=">"+json.realname+"</td>" +
									"<td class='text-right' width='100'>受理号:</td><td   class='text-left'>"+json.acceptanceNumber+"</td></tr>";
								}
							}else{
								if(json.status=="不通过"){
									trhtml+="colspan='3' >"+json.realname+"</td>";
								}else{									
									trhtml+=">"+json.realname+"</td>" +
									"<td class='text-right' width='100'>审核依据:</td><td   class='text-left'>"+json.approvalBasis+"</td></tr>";
								}
							}
					$("#approvaluser").append(trhtml);
				});
			}
		}
	}); 
}

function getuser(){
	
	var  byIdconfig2={
			fromConfig:[{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"placeOfDomicile",
				aliasesName:"getvalue",
				fieldName:"id,displayValue"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"maritalStatus",
				aliasesName:"getvalue1",
				fieldName:"id,displayValue"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"housingConditionsInTheCity",
				aliasesName:"getvalue2",
				fieldName:"id,displayValue"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"applyForFamily",
				aliasesName:"getvalue3",
				fieldName:"id,displayValue"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"accordwith",
				aliasesName:"getvaluez",
				fieldName:"id,displayValue"
			},{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"education",
				aliasesName:"getvalue4",
				fieldName:"id,displayValue"
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
				aliasesName:"user_housing",
				fieldName:"id,housingLocatedAddress,areaType,area,propertyOwner,theHousingAllNumpeople,ownerType",
				fromConfig:[{
					formCode:"WDIT_Company_Request_User_photo",
					currentField:"user_housing_id",
					parentId:"id",
					aliasesName:"housePhoto",
					fieldName:"id,type,name,smallPhoto,photo"
				},{
					formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"ownerType",
					aliasesName:"ownerTypes",
					fieldName:"displayValue"	
				}]
			},{
				formCode:"WDIT_Company_Request_User_relative",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"user_relative",
				fieldName:"id,relative,name,identityCardNumber",
				fromConfig:[{
					formCode:"WDIT_Company_Request_User_photo",
					currentField:"user_relative_id",
					parentId:"id",
					aliasesName:"userPhoto",
					fieldName:"id,type,name,smallPhoto,photo"
				},{
					formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"relative",
					aliasesName:"relatives",
					fieldName:"id,displayValue"
				}]
			},{
				formCode:"WDIT_Company_Request_User_photo",
				currentField:"requestUser_id",
				parentId:"id",
				aliasesName:"userphoto",
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
			var results = data.results;
			if(results&&results.length>0){
				var obj=results[0];
				$("#request_id").val(obj.request_id);
				$("#userName").html(obj.userName);
				
				var getvalue=obj.getvalue[0];
				if(getvalue)
					$("#placeOfDomicile").html(getvalue.displayValue);
				
				var getvalue1=obj.getvalue1[0];
				if(getvalue1)
					$("#maritalStatus").html(getvalue1.displayValue);
				
				var getvalue2=obj.getvalue2[0];
				if(getvalue2)
					$("#housingConditionsInTheCity").html(getvalue2.displayValue);
				
				if(obj.housingConditionsInTheCity==402){
					$("#housemessage").hide();
				}
				
				var getvalue3=obj.getvalue3[0];
				if(getvalue3)
					$("#applyForFamily").html(getvalue3.displayValue);
				
				var linkman = obj.requestpickDwelling[0].linkman;
	            var  email = obj.requestpickDwelling[0].email;
				if(linkman)
					$("#linkman").val(linkman);
				if(email)
					$("#email").val(email);
				var pickDwelling = obj.requestpickDwelling[0].pickDwellingname[0].displayValue;
				if(pickDwelling){
					 $("#pickDwelling").html(pickDwelling);
				}
				$("#applicantPhone").html(obj.applicantPhone);
				$("#identityCardNumber").html(obj.identityCardNumber);
				$("#residencePermitNumber").html(obj.residencePermitNumber);
				$("#housingAccumulationFundAccount").html(obj.housingAccumulationFundAccount);
				$("#permanentAddress").html(obj.permanentAddress);
				$("#address").html(obj.address);
				$("#companyId").val(obj.companyId);
				var education = obj.getvalue4[0];
				if(education){
					 $("#education").html(education.displayValue);
				}
				$("#educationCardCode").html(obj.educationCardCode);
				$("#graduationSchool").html(obj.graduationSchool);
				$("#graduationTime").html(obj.graduationTime);
				$("#specialty").html(obj.specialty);
				var accord = obj.getvaluez[0];
				if(accord){
					 $("#accordwith").html(accord.displayValue);
				}
				
				var status=obj.status;
				if(step&&step==4){
					$("#accord").val(obj.accordwith);
					if((status>4201 && status!=4204)||status<2201||status==2203 ){
						$("#approval_div").hide();
					}
				}else if(step&&step==5){
					if((status>5201 && status!=5204)||status<4201||status==4203 ){
						$("#approvalstep").hide();
					}
				}
				
				var userreHousings=obj.user_housing;
				if(userreHousings&& userreHousings.length>0){
					$.each(userreHousings,function(i,obj){
						var houing='<table class="table table-bordered table-hover text-center table-horizontal"><tbody>'+
						'<tr><th width="200">房屋坐落地址</th><td id="houseaddress">'+obj.housingLocatedAddress+'</td>';
						if(obj.areaType==1){
							houing+='<th width="200">建筑面积</th><td id="area">'+obj.area+'</td></tr>';
						}else if(obj.areaType==2){
							houing+='<th width="200">居住面积</th><td id="area">'+obj.area+'</td></tr>';
						}
						var ownerType=obj.ownerTypes[0];
						houing+='<tr><th width="200">产权承租人</th><td id="propertyOwner">'+obj.propertyOwner+'</td><th width="200">该住房人口总数</th>'+
						'<td id="theHousingAllNumpeople">'+obj.theHousingAllNumpeople+'</td></tr>'+
						'<tr><th width="200">产权人类型</th>'+
						'<td id="ownerType" colspan="3">'+ownerType.displayValue+'</td></tr>'+
						'<tr><th width="200">房产证或使用权凭证照片</th><td id="photo1" colspan="3">';
						var housPhotos =obj.housePhoto;
						if(housPhotos && housPhotos.length>0){
							$.each(housPhotos,function(i,housPhoto){
								houing+='<a onClick="showPhoto(\''+rootPath+housPhoto.photo+'\')"><img src="'+rootPath+housPhoto.smallPhoto+'" height="180" alt=""></a>';
							});
						}
						houing+='</td></tr>'+
						'</tbody></table>';
						$("#userhouing").append(houing);
					})
				}
//				if(userrelative){
//					$("#houseaddress").html(userrelative.housingLocatedAddress);
//					$("#area").html(userrelative.area);
//					$("#propertyOwner").html(userrelative.propertyOwner);
//					$("#theHousingAllNumpeople").html(userrelative.theHousingAllNumpeople);
//					var housephotos=userrelative.housePhoto[0];
//					$("#photo1").html('<a onClick="showPhoto(\''+rootPath+housephotos.photo+'\')"><img src="'+rootPath+housephotos.smallPhoto+'" height="180" alt=""></a>');
//				}
				var relatives= obj.user_relative;
				if(relatives&&relatives.length>0){
					$.each(relatives,function(i,relative){//共同申请人
						var userphoto=relative.userPhoto;
						var relativeimgHtml="";
						$.each(userphoto,function(i,photo){
							relativeimgHtml +='<a onClick="showPhoto(\''+rootPath+photo.photo+'\')"><img src="'+rootPath+photo.smallPhoto+'" height="180" alt=""></a>'
						})
						
						var display=relative.relatives[0];//数据字典取的value
						if(display){
							var relativess="<tr><td>"+display.displayValue+"</td><td>"+relative.name+"</td><td>"+relative.identityCardNumber+"</td><td >"+relativeimgHtml+"</td></tr>";
							$("#relative tbody").append(relativess);	
						}
					})
				}
				var userphotos=obj.userphoto;
				if(userphotos&&userphotos.length>0){
					$.each(userphotos,function(i,userphoto){
						var type=userphoto.type;
						if(type==1){
							type="房产证或使用凭证照片";
						}else if(type==2){
							type="申请人身份证";
						}else if(type==3){
							type="申请人居住证";
						}else if(type==4){
							type="申请人户口本";
						}else if(type==5){
							type="申请人婚姻情况证明";
						}else if(type==6){
							type="共同申请人证件";
						}else if(type==7){
							type="授权书";
						}
						var photoTypeId= $("#requestUserType_"+userphoto.type+"_id").html();
						if(photoTypeId){
							$("#requestUserType_"+userphoto.type+"_id").append('<a onClick="showPhoto(\''+rootPath+userphoto.photo+'\')"><img src="'+rootPath+userphoto.smallPhoto+'" height="180" alt=""></a>');
						}else{
							var userphotohtml='<tr><td>'+type+'</td><td  id="requestUserType_'+userphoto.type+'_id"><a onClick="showPhoto(\''+rootPath+userphoto.photo+'\')"><img src="'+rootPath+userphoto.smallPhoto+'" height="180" alt=""></a></td></tr>';
							$("#userphoto tbody").append(userphotohtml);
						}
					})					
				}
			}
			
		}
	});
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
//function getRelative(){
//	$.ajax({
//		url : rootPath+'/forms/getDataByFormId',
//		type : "POST",
//        async: false,
//		data:{
//			formCode:"WDIT_Company_Request_User_relative",
//			condition: " requestUser_id="+requestUserId
//		},
//		complete : function(xhr, textStatus){
//			var data = JSON.parse(xhr.responseText);
//			var results = data.results;
//// 			var getuser=results[0];
//// 			var userRelative=getuser.userRelative[0];
//			
//			$.each(results,function(i,obj){
//				var relative="<tr><td>"+obj.relative+"</td><td>"+obj.name+"</td><td>"+obj.identityCardNumber+"</td><td id='relativeimg'></td></tr>";
//				$("#relative tbody").append(relative);
//			})
//			
//		}
//	});
//}

//function userHousing(){
//	$.ajax({
//		url : rootPath+'/forms/getDataByFormId',
//		type : "POST",
//        async: false,
//		data:{
//			formCode:"WDIT_Company_Request_User_housing",
//			condition: " requestUser_id="+requestUserId
//		},
//		complete : function(xhr, textStatus){
//			var data = JSON.parse(xhr.responseText);
//			var results = data.results;
//			var userhousing=results[0];
//// 			var userRelative=getuser.userRelative[0];
//			$("#houseaddress").html(userhousing.housingLocatedAddress);
//			$("#area").html(userhousing.area);
//			$("#propertyOwner").html(userhousing.propertyOwner);
//			$("#theHousingAllNumpeople").html(userhousing.theHousingAllNumpeople);
//			
//		}
//	});
//}
//function userPhoto(){
//	$.ajax({
//		url : rootPath+'/forms/getDataByFormId',
//		type : "POST",
//        async: false,
//		data:{
//			formCode:"WDIT_Company_Request_User_photo",
//			condition: " requestUser_id="+requestUserId
//		},
//		complete : function(xhr, textStatus){
//			var data = JSON.parse(xhr.responseText);
//			var results = data.results;
//			$.each(results,function(i,obj){
//				var type=obj.type;
//				if(type==1){
//					type="房产证或使用凭证照片";
//				}else if(type==2){
//					type="申请人身份证";
//				}else if(type==3){
//					type="申请人居住证";
//				}else if(type==4){
//					type="申请人户口本";
//				}else if(type==5){
//					type="申请人结婚证";
//				}else if(type==6){
//					type="共同申请人身份证";
//				}else if(type==7){
//					type="授权书";
//				}
//				var userphoto="<tr><td>"+type+"</td><td><img src='"+rootPath+obj.smallPhoto+"' height='180' alt=''></td></tr>"
//				$("#userphoto tbody").append(userphoto);

//			})
//		}
//	});
//}


//function getvalue(){
//	$.ajax({
//		url : rootPath+'/forms/getDataByFormId',
//		type : "POST",
//	    async: false,
//		data:{
//			formCode:"sys_datadictionary_value",
//			condition: " allid="+1
//		},
//		complete : function(xhr, textStatus){
//			var data = JSON.parse(xhr.responseText);
//			var results = data.results;
//			var placeOfDomicile=$("#placeOfDomicile").html();
//			var maritalStatus=$("#maritalStatus").html();
//			var housingConditionsInTheCity=$("#housingConditionsInTheCity").html();
//			var applyForFamily=$("#applyForFamily").html();
//			$.each(results,function(i,obj){
//				if(placeOfDomicile==obj.id){
//					$("#placeOfDomicile").html(obj.displayValue);
//				}
//				if(maritalStatus==obj.id){
//					$("#maritalStatus").html(obj.displayValue);
//				}
//				if(housingConditionsInTheCity==obj.id){
//					$("#housingConditionsInTheCity").html(obj.displayValue);
//				}
//				if(applyForFamily==obj.id){
//					$("#applyForFamily").html(obj.displayValue);
//				}
//			})
//			
//		}
//	});
//}