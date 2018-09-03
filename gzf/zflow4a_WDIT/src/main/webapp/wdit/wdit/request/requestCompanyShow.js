function  initPage(){
	getcompanyrequest();//企业信息
	
	getRequestUserList(id,step); //人员列表

	companyApprovalList(id,step);//审批日志
	
	ReturnRemarkList(step);//退回备注
} 

function getcompanyrequest(){
	var  byIdconfig2={
			fromConfig:[{
				formCode:"sys_datadictionary_value",
				currentField:"id",
				parentId:"rent",
				aliasesName:"rent",
				fieldName:"displayValue"
			},{
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
				fieldName:"id,displayValue"
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
			formCode:"wdit_company_request",
			condition: " id="+id,
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
				var rent = obj.rent[0];
				if(rent)
					$("#rent").html(rent.displayValue);
				var pickDwelling = obj.pickDwelling[0];
				if(pickDwelling)
					$("#pickDwelling").html(pickDwelling.displayValue);
				var companyNature_id=obj.companyNature;
				var companyNature=obj.companyNatures[0];
				if(companyNature){
					$("#companyNature").html(companyNature.displayValue);
				}
				$("#linkman").html(obj.linkman);
				$("#phone").html(obj.phone);
				$("#email").html(obj.email);
				$("#tel").html(obj.tel);
				$("#applicationCompany").html(obj.applicationCompany);
				var status=obj.status;
				if(step&&step==3){
					if(status>301){
						//$("#").hide();
					}
				}else if(step&&step==5){
					if(status>501 && status !=505){
						$("#approvalstep").hide();
					}
				}else if(step&&step==7){
					if(status>501 && status !=505){
						//$("#approvalstep").hide();
					}
				}else if(step&&step==2){
					if(status>201 && status !=205){
						//$("#approvalstep").hide();
					}
				}else if(step&&step==6){
					if(status>601 && status !=605){
						//$("#approvalstep").hide();
					}
				}else if(step&&step==1){
					if(status>101){
						//$("#").hide();
					}
				}else if(step==0){
					var str=status.substring(0,1);
					if(str==0){
						$("#shenqing").attr("class","active");
					}else if(str==1){
						$("#shenqing").attr("class","active");
						if(status==102){
							$("#shouli").attr("class","active");
						}
					}else if(str==2||str==3||str==4){
						$("#shenqing").attr("class","active");
						$("#shouli").attr("class","active");
						$("#shenhe").attr("class","active");
					}else if(str==5){
						$("#shenqing").attr("class","active");
						$("#shouli").attr("class","active");
						$("#shenhe").attr("class","active");
						$("#zhongshen").attr("class","active");
					}else if(str==6){
						$("#shenqing").attr("class","active");
						$("#shouli").attr("class","active");
						$("#shenhe").attr("class","active");
						$("#zhongshen").attr("class","active");
					}
				}
				
				var displayValue= obj.companyClassification[0];
				if(displayValue)
					$("#companytype").html(displayValue.displayValue);
				var companyId=obj.companyId;
				$("#companyId").val(companyId);  
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



function getRequestUserList(id,step){
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
			},{
				title:'申请状态',
				data : '',
				type : 'cutsomerRender',
				doRender : function(data, container,config, rowIndex){
					var status = data.status;
					if(step==0){
						var str=status.substring(4,2);
						var statusname = '待审核';
						if(status==5202){
							statusname = "审核完";
						}else if(status==1201){
							statusname = "待受理";
						}else if(status==1203){
							statusname = "受理不通过";
						}else if(status>5204){
							statusname = "审核完";
						}else{
							if(str&&str==01){
								statusname = "待审核";
							}else if(str&&str==02){
								statusname = "待审核";
							}else if(str&&str==03){
								statusname = "审核不通过";
							}
						}
					}else if(step==1){
						var statusname = '';
						var status=parseInt(status);
						if(status+""=="1201"||status+""=="1204"||status+""=="")
							statusname = '待审核';
						else if(status+""=="1203")
							statusname = '不通过';
						else if(status+""=="1202"||status>1204)
							statusname = "已通过";
					}else if(step==2){
						var statusname = '待审核';
						var status=parseInt(status);
						if(status+""=="1203"){
							statusname = '不通过';
						}else if(status+""=="1202" || status+""=="2204")
							statusname = "待审核";
						else if(status+""=="2202")
							statusname = "已通过";
						else if(status+""=="2203")
							statusname = "不通过";
						else if(status >2204 )
							statusname = "已通过";
					}else if(step==3){
						var statusname = '';
						var status=parseInt(status);
						if(status+""=="1203" || status+""=="2203" || status+""=="3203")
							statusname = '不通过';
						else if(status+""=="2202"||status>3203)
							statusname = "已通过";
					}else if(step==4){
						  var statusname = '';
						  var status=parseInt(status);
						    if(status+""=="1203"){
							  statusname = '不通过';
							}else if(status+""=="2203"){
								statusname = '不通过';
							}else if(status+""=="2202" || status+""=="4204"){
								statusname = '待审核';
							}else if(status+""=="4202")
								statusname = "已通过";
							else if(status+""=="4203")
								statusname = "不通过";
							else if(status >4204 )
								statusname = "已通过";
					  }else if(step==5){
						var statusname = '待审核';
						var status=parseInt(status);
						if(status+""=="1203"){
							statusname = '不通过';
						}else if(status+""=="2203"){
							statusname = '不通过';
						}else if(status+""=="4203"){
							statusname = '不通过';
						}else if(status+""=="4202" || status+""=="5204")
							statusname = "待审核";
						else if(status+""=="5203")
							statusname = "不通过";
						else if(status+""=="5202")
							statusname = "已通过";
						else if(status>5204)
							statusname = "已通过";
					}else if(step==6){
						var statusname = '待审核';
						var status=parseInt(status);
						if(status+""=="1203"){
							statusname = '不通过';
						}else if(status+""=="2203"){
							statusname = '不通过';
						}else if(status+""=="4203"){
							statusname = '不通过';
						}else if(status+""=="5203")
							statusname = "不通过";
						else if(status+""=="5202")
							statusname = "待审核";
						else if(status+""=="6203")
							statusname = "不通过";
						else if(status+""=="6202")
							statusname = "已通过";
					}else if(step==7){
						var statusname = '待审核';
						var status=parseInt(status);
						if(status+""=="1203"){
							statusname = '不通过';
						}else if(status+""=="2202"|| status+""=="5204"){
							statusname = '待审核';
						}else if(status+""=="2203"){
							statusname = '不通过';
						}else if(status+""=="5203")
							statusname = "不通过";
						else if(status+""=="5202")
							statusname = "已通过";
						else if(status>5204)
							statusname = "已通过";
					}
					$(container).append(statusname);
				}
			},{
				title : '操作',
				data : '',
				type : 'cutsomerRender',
				doRender : function(data, container,config, rowIndex){
					var updateHtml="";
					var status = data.status;
					if(step==0){
						updateHtml="<a target='_blank'  href='"+rootPath+"/company/requestshowuser?requestUserId="+data.id+"'>查看</a> ";	
					}else if(step==1){
						if((data.status == 1201 || data.status == 1204) && companystutus != 103){
						    updateHtml="<a target='_blank'  href='"+rootPath+"/hmcheck/userapproval?requestUserId="+data.id+"&status="+data.status+"&requreid="+id+"&companystutus="+companystutus+"'>审核</a>";	
					    }else{
					    	updateHtml="<a target='_blank'  href='"+rootPath+"/hmcheck/userapproval?requestUserId="+data.id+"&status="+data.status+"&requreid="+id+"&companystutus="+companystutus+"'>查看</a>";	
					    }
					}else if(step==2){
						if(status==1202 || data.status == 2204){
							updateHtml="<a target='_blank'  href='"+rootPath+"/hmcheck/hmuserapproval?requestUserId="+data.id+"'>审批</a> ";
						}else{
							updateHtml="<a target='_blank'  href='"+rootPath+"/hmcheck/hmuserapproval?requestUserId="+data.id+"'>查看</a> ";
						}
					}else if(step==3){   
						updateHtml="<a target='_blank'  href='"+rootPath +"/assign/userview?requestUserId="+data.id+"'>查看</a> ";
					}else if(step==4){
						if(status==2202 || data.status == 4204){
							updateHtml="<a target='_blank'  href='"+rootPath+"/bureauhuman/rsjShowUser?requestUserId="+data.id+"'>审批</a> ";
						}else{
							updateHtml="<a target='_blank'  href='"+rootPath+"/bureauhuman/rsjShowUser?requestUserId="+data.id+"'>查看</a> ";
						}
					}else if(step==5){
						if(status==4202 || data.status == 5204){
							updateHtml="<a target='_blank'  href='"+rootPath+"/talentoffice/rcbShowUser?requestUserId="+data.id+"'>审批</a> ";
						}else{
							updateHtml="<a target='_blank'  href='"+rootPath+"/talentoffice/rcbShowUser?requestUserId="+data.id+"'>查看</a> ";
						}
					}else if(step==6){
						if(status==5202 ){
							updateHtml="<a target='_blank'  href='"+rootPath+"/hmcheck/sceneuserapproval?requestUserId="+data.id+"'>审批</a> ";
						}else{
							updateHtml="<a target='_blank'  href='"+rootPath+"/hmcheck/sceneuserapproval?requestUserId="+data.id+"'>查看</a> ";
						}
					}else if(step==7){
						if(status==2202 || data.status == 5204){
							updateHtml="<a target='_blank'  href='"+rootPath+"/talentoffice/rcbQuickShowUser?requestUserId="+data.id+"'>审批</a> ";
						}else{
							updateHtml="<a target='_blank'  href='"+rootPath+"/talentoffice/rcbQuickShowUser?requestUserId="+data.id+"'>查看</a> ";
						}
					}
					$(container).append(updateHtml);
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
	var condition="";
	if(step&&step==0){
	}else if(step&& step==2){//显示上级审批通过和拒绝和下级审批状态
		condition +=" and status in (1202,1203,2202,2203,2204,4202,4203,5202,5203,6202,6203)";
	}else if(step&& step==4){//显示上级，上上级审批拒绝和下级审批通过
		condition +=" and status in (1203,2203,2202,4202,4203,4204,5202,5203,6202,6203)";
	}else if(step&& step==5){//显示上级，上上级审批拒绝和上级审批通过
		condition +=" and status in (1203,2203,4203,4202,5202,5203,5204,6202,6203)";
	}else if(step&& step==6){//显示上级，上上级审批拒绝和上级审批通过
		condition +=" and status in (1203,2203,4203,5202,5203,6202,6203)";
	}else if(step&& step==7){//显示上级，上上级审批拒绝和上级审批通过
		condition +=" and status in (1203,2202,2203,5202,5203,5204,6202,6203)";
	}
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request_User",
			condition: " request_id="+id +condition ,
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


function companyApprovalList(id,step){
	//var where 判断步骤显示到本级为止
	$.post(rootPath+'/createSelect/findselectData', { 
		code : "approvalcompany_v1",
		selectConditionSql : " where t1.status not in(1104,2104,3104,4104,5104,11041,21041,31041,41041,51041,51042) and t1.approvalStep<="+step+" and t1.request_id="+id
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
							"<td id='step4' class='text-left' width='80' style='color:green'>"+json.status+"</td>" +
							"<td class='text-right' width='100'>审核时间：</td><td id='time4' class='text-left' width='120' style='color:green'>"+approval_Date+"</td>" +
							"<td class='text-right' width='100'>审核人：</td><td id='user4' class='text-left' width='80' style='color:green'" ;
							if(!json.approvalBasis)
								trhtml+="colspan='3' >"+json.realname+"</td>";
							else 
								if(json.status=="不通过"){
									trhtml+="colspan='3' >"+json.realname+"</td>";
								}else{									
									trhtml+=">"+json.realname+"</td>" +
									"<td class='text-right' width='100'>审核依据：</td><td   class='text-left'>"+json.approvalBasis+"</td></tr>";
								}
					$("#showstep").append(trhtml);
				});
			}
		}
	});
}

function ReturnRemarkList(step){
	if(step==1){
		var where=" where status in(11041,21041) and t1.request_id="+id
	}if(step==2){
		var where=" where status in(21041,31041,51042) and t1.request_id="+id
	}if(step==3){
		var where=" where status in(31041,41041,51041) and  t1.request_id="+id
	}if(step==4){
		var where=" where status in(31041,41041,51041) and  t1.request_id="+id
	}if(step==5){
		var where=" where status in(51041) and  t1.request_id="+id
	}if(step==6){
		var where=" where status in(61041,51041) and  t1.request_id="+id
	}if(step==7){//绿色通道
		var where=" where status in(51042) and t1.request_id="+id
	}
	 
	$.post(rootPath+'/createSelect/findselectData', { 
		code : "returnremark_v1",
		selectConditionSql : where
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
					"<td class='text-left' style='color:green'>"+returnRemark+"</td>"+
					"<td class='text-right' style='color:green' width='200'>退回时间："+approval_Date+"</td></tr>"
					$("#showreturnremark").append(trhtml);
				})
			}
	}); 
}
