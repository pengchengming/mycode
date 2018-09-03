<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>打印申请表</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>

 <script src="<c:url value="/wdit/assets/plugins/jQuery/jquery-1.4.4.min.js" />"></script>
<script language="javascript" src="<c:url value="/script/jquery/jquery.jqprint-0.3.js" />"></script>

<script type="text/javascript">
var requreid = ${requreid};
var step = ${step};

var rootPath = "<c:url value="/" />";
$(function(){
	var  byIdconfig={
			fromConfig:[{
				 formCode:"sys_datadictionary_value",
					currentField:"id",
					parentId:"pickDwelling",
					aliasesName:"pickDwellingname",
					fieldName:"displayValue"
			 },{
				formCode:"WDIT_Company_Request_Photo",
				currentField:"request_id",
				parentId:"id",
				aliasesName:"companyPhoto",
				fieldName:"taxRegistersCardNumber,registrationNumber,photoType"
			},{
				formCode:"WDIT_Company_Request_User",
				currentField:"request_id",
				parentId:"id",
				aliasesName:"requestUsers",
				fieldName:"id,userName,applicantPhone,identityCardNumber,permanentAddress,address,residencePermitNumber,housingAccumulationFundAccount,pickDwelling",	
				fromConfig:[{
						formCode:"WDIT_Company_Request_User_relative",
						currentField:"requestUser_id",
						parentId:"id",
						aliasesName:"usersRelatives",
						fieldName:"relative,identityCardNumber,name",
						fromConfig:[{
							formCode:"sys_datadictionary_value",
							currentField:"id",
							parentId:"relative",
							aliasesName:"relativename",
							fieldName:"displayValue"
					 }]
				 },{
					    formCode:"WDIT_Company_Request_User_housing",
						currentField:"requestUser_id",
						parentId:"id",
						aliasesName:"usersHousings",
						fieldName:"housingLocatedAddress,areaType,area,propertyOwner,theHousingAllNumpeople"
				 }]
				
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
			if(data.code + "" == "1"){
				var conpanyinfo = data.results[0];
				var applicant = conpanyinfo.applicant;
				$("#com_applicant").html(applicant);
				var registerAddress = conpanyinfo.registerAddress;
				$("#com_registerAddress").html(registerAddress);
				var officeAddress = conpanyinfo.officeAddress;
				$("#com_officeAddress").html(officeAddress);
				var registerMoney=toDecimal2(conpanyinfo.registerMoney)
				var oneYearIsTaxAmount=toDecimal2(conpanyinfo.oneYearIsTaxAmount)
				$("#com_registerMoney").html(registerMoney);
				$("#com_oneYearIsTaxAmount").html(oneYearIsTaxAmount);
				$("input[type='checkbox'][name='companyClassification']").attr("disabled","disabled");
				$("#com_staffNum").html(conpanyinfo.staffNum);
				$("#com_linkman").html(conpanyinfo.linkman);
				$("#com_tel").html(conpanyinfo.tel);
				$("#com_email").html(conpanyinfo.email);
				$("#com_applicationCompany").html(conpanyinfo.applicationCompany);
				var pickDwellingname = conpanyinfo.pickDwellingname[0].displayValue;
                var companyPhotos=conpanyinfo.companyPhoto;
                var registrationNumber,taxRegistersCardNumber;
				if(companyPhotos&&companyPhotos.length>0){
					$.each(companyPhotos,function(i,companyPhoto){
					   var photoType=companyPhoto.photoType;//照片类型
					      if(photoType+""=="2"){
					    	 registrationNumber = companyPhoto.registrationNumber;
					    	 taxRegistersCardNumber = companyPhoto.taxRegistersCardNumber;
					    	 if(registrationNumber)
							   $("#com_registrationNumber").html(registrationNumber);
					    	 if(taxRegistersCardNumber)
							   $("#com_taxRegistersCardNumber").html(taxRegistersCardNumber);
						 }
					})
				}
				
				//人员信息
				var requestUsers = conpanyinfo.requestUsers;
			    var areaarray = new Array();
				if(requestUsers&&requestUsers.length>0){
				   $.each(requestUsers,function(k,requestUser){	 
					   var registrationNumber = registrationNumber?registrationNumber:'';
					   var pickDwellingValue = pickDwellingname?pickDwellingname:'';
					   var tablehtml = '<h4>'+pickDwellingValue+'配租人员信息表</h4><table class="table table-bordered table-hover text-center table-horizontal" style="width: 800px;margin: auto;">'+
					               '<tr><td class="text-center" width="40%">申请单位(全称)</td><td colspan="4" class="text-center"  width="60%">'+applicant+'</td></tr>'+
					               '<tr><td class="text-center" width="40%">注册地址</td><td colspan="4" class="text-center" width="60%">'+registerAddress+'</td></tr>'+
					               '<tr><td class="text-center" width="40%">营业或办公地址</td><td colspan="4" class="text-center" width="60%">'+officeAddress+'</td></tr>'+
					               '<tr><td class="text-center" width="40%">工商登记号</td><td colspan="4" class="text-center" width="60%">'+registrationNumber+'</td></tr>';
					   var usersRelatives = requestUser.usersRelatives;
					   var usersRelativeslength = usersRelatives.length;
					   if (usersRelativeslength > 0){  //假如有共同申请人
						   tablehtml += '<tr><td width="40%" rowspan="'+(usersRelativeslength+1)+'">配租人员姓名</td><td width="15%">本人</td><td width="15%">'+requestUser.userName+'</td><td width="15%">身份证</td><td width="15%">'+requestUser.identityCardNumber+'</td></tr>';
						   $.each(usersRelatives,function(z,usersRelative){
							   tablehtml += '<tr><td width="15%">'+usersRelative.relativename[0].displayValue+'</td><td width="15%">'+usersRelative.name+'</td><td width="15%">身份证</td><td width="15%">'+usersRelative.identityCardNumber+'</td></tr>';
						   })
						}else{
							tablehtml += '<tr><td width="40%">配租人员姓名</td><td width="15%">本人</td><td width="15%">'+requestUser.userName+'</td><td width="15%">身份证</td><td width="15%">'+requestUser.identityCardNumber+'</td></tr>';
						}    
					      tablehtml +=  '<tr><td class="text-center" width="40%">配租人员联系电话</td><td colspan="4" class="text-center" width="60%">'+requestUser.applicantPhone+'</td></tr>'+
					                    '<tr><td class="text-center" width="40%">配租人员户籍地址</td><td colspan="4" class="text-center" width="60%">'+requestUser.permanentAddress+'</td></tr>'+
					                    '<tr><td class="text-center" width="40%">配租人员联系地址</td><td colspan="4" class="text-center" width="60%">'+requestUser.address+'</td></tr>'+
					                    '<tr><td class="text-center" width="40%">居住证号码<br>(配租人员本人)</td><td colspan="4" class="text-center" width="60%">'+requestUser.residencePermitNumber+'</td></tr>'+
					                    '<tr><td class="text-center" width="40%">住房公积金中心<br>(配租人员本人)</td><td colspan="4" class="text-center" width="60%">'+requestUser.housingAccumulationFundAccount+'</td></tr>';
					   var usersHousings = requestUser.usersHousings;//住房信息
					   if(usersHousings&&usersHousings.length>0){
						   $.each(usersHousings,function(j,usersHousing){
							   if(j<2){                                  
								   tablehtml += '<tr><td colspan="5"  class="text-center">本市户籍住房信息'+(j+1)+'</td></tr>'+
								                '<tr><td class="text-center" width="40%">房屋坐落地址</td><td colspan="4" class="text-center" width="60%">'+usersHousing.housingLocatedAddress+'</td></tr>'+
								                '<tr><td class="text-center" width="40%"><label><input type = "checkbox" value="1" name="areaType'+requestUser.id+j+'">建筑面积</label><label><input type = "checkbox" value="2" name="areaType'+requestUser.id+j+'">居住面积</label></td><td colspan="4" class="text-center"  width="60%">'+usersHousing.area+'</td></tr>'+
								                '<tr><td class="text-center" width="40%">产权人或承租人</td><td colspan="4" class="text-center"  width="60%">'+usersHousing.propertyOwner+'</td></tr>'+
								                '<tr><td class="text-center" width="40%">该住房户籍人口总数</td><td colspan="4" class="text-center"  width="60%">'+usersHousing.theHousingAllNumpeople+'</td></tr>';
                                       var areaobj = new Object();
				                       areaobj.name='areaType'+requestUser.id+j;
				                       areaobj.value = usersHousing.areaType;
				                       areaarray.push(areaobj);   
							   }
						   })
					   }
					   tablehtml += '<tr><td colspan="5"  class="text-center">注：1.《上海市居住证》包含《上海市长期居住证》及《上海市临时居住证》<br>'+
					                '2.如有配偶或子女必须填写<br>3.16周岁以下不持有居民身份证的居住人员，身份证号码按照《居名户口簿》内的身份证编号填写<br>'+
					                '4.户口所在地住房及产权住房不在一处的，必须同时填写</td></tr>';
                       $("#userlist").append(tablehtml +'</table><div style="page-break-after: always"><span style="display: none">&nbsp;</span></div>');    
                       
                       if(areaarray.length>0){
                    	   $.each(areaarray,function(i,areaobj){
                    		$("input[type='checkbox'][name='"+areaobj.name+"'][value="+areaobj.value+"]").attr("checked",true);
               				$("input[type='checkbox'][name='"+areaobj.name+"']").attr("disabled","disabled");
                    	   })
                       }
                       
				   })
				}
            }else{
			   alert("找不到公司信息");	
			}
		}
	}); 
});

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
	
function preview() {
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request_Print",
			condition: "requestId="+requreid + " and step = " + step + " and type = 1"
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.code + "" == "1"){
			    var result = data.results;
			    var json = new Object();
			    json['formId'] = 96;
			    if(result&& result.length == 0){
			    	json['register'] = {"requestId":requreid,"step":step,"type":1,"frequency":1};
			    }else if(result&& result.length > 0){
			    	var printComDetail = result[0];
			    	json['tableDataId']=printComDetail.id;
			    	var newFrequency = parseInt(1 + parseInt(printComDetail.frequency));
			    	json['register'] = {"frequency":newFrequency};
			    }
			    if(json['register']){
			    	$.ajax({
		    			url : '<c:url value="/forms/saveFormDataJson"/>',
		    			type : "POST", 
		    			async: false,
		    			data : { 
		    				json : JSON.stringify(json)
		    			}, complete : function(xhr, textStatus){
		    				var data = JSON.parse(xhr.responseText);
		    				if(data.code+""=="1"){
		    					 $("#print_div").css("display","none");//打印时隐藏  
		    					  $(".tab-content").jqprint({importCSS: true});
		    					  $("#print_div").css("display","");//打印时隐藏  //打印后再显示出来  
		    				}else {
		    					alert(data.errorMsg);
		    				}
		    			}
		    		});
			    }
			}
		}
	}) 
 } 	
	

 
</script>

</head>

<body class="horizontal-menu-fixed">
    <div class="main-wrapper">
<%-- 		<%@ include file="/WEB-INF/views/wdit/head.jsp"%>  --%>
		<%-- 		<%@ include file="/WEB-INF/views/wdit/leftMenu.jsp"%> --%>
        <!-- start: MAIN CONTAINER -->
        <div class="panel-body no-padding-top">
            <div class="main-content" style="min-height: 542px;">
                <div class="container">
                    
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="panel panel-white">
                                <div class="panel-body no-padding-top">
                                  
									 <div class="tab-content">
									     <div style="text-align:center;"><h3>区筹公租房单位申请意向登记单表</h3>
									        <table class="table table-bordered table-hover text-center table-horizontal" style="width: 800px;margin: auto;">
									           <tr><td class="text-center" width="40%">申请单位（全称）</td><td class="text-left" id="com_applicant" width="60%"></td></tr>
									           <tr><td class="text-center">注册地址</td><td class="text-left" id="com_registerAddress"></td></tr>
									           <tr><td class="text-center">营业或办公地址</td><td class="text-left" id="com_officeAddress"></td></tr>
									           <tr><td class="text-center">工商登记号</td><td class="text-left" id="com_registrationNumber"></td></tr>
									           <tr><td class="text-center">注册资本金(人民币/万元)</td><td class="text-left" id="com_registerMoney"></td></tr>
									           <tr><td class="text-center">税务登记号</td><td class="text-left" id="com_taxRegistersCardNumber"></td></tr>
									           <tr><td class="text-center">上一年度税金额(人民币/万元)</td><td class="text-left" id="com_oneYearIsTaxAmount"></td></tr>
									           <tr><td class="text-center" >申请单位所属委办</td>
									               <td class="text-left">
									                   <label><input name="companyClassification" type="checkbox" value="1" />金融</label><br>
									                   <label><input name="companyClassification" type="checkbox" value="2" />科技创新</label><br>
									                   <label><input name="companyClassification" type="checkbox" value="3" />IT|通信|电子|互联网 </label><br>
									                   <label><input name="companyClassification" type="checkbox" value="4" />生命医疗 </label><br>
									                   <label><input name="companyClassification" type="checkbox" value="5" />文化|传媒|创意 </label><br> 
									                   <label><input name="companyClassification" type="checkbox" value="6" />教育培训 </label><br>
									                   <label><input name="companyClassification" type="checkbox" value="7" />现代服务业 </label><br>
									                   <label><input name="companyClassification" type="checkbox" value="8" />商业贸易</label><br>
									                   <label><input name="companyClassification" type="checkbox" value="9" />能源|矿产|环保</label><br>
									                   <label><input name="companyClassification" type="checkbox" value="10" />房地产|建筑业</label><br>
									                   <label><input name="companyClassification" type="checkbox" value="11" />交通|运输|物流|仓储</label><br>
									                   <label><input name="companyClassification" type="checkbox" value="12" />生产|加工|制造</label><br>
									                   <label><input name="companyClassification" type="checkbox" value="13" />政府|非盈利机构</label><br>
									                   <label><input name="companyClassification" type="checkbox" value="14" />其他</label>
									               </td></tr>
									           <tr><td class="text-center">在职员工人数（与单位直接签订劳动合同）</td><td class="text-left" id="com_staffNum"></td></tr>
									           <tr><td class="text-center">单位申请公租房工作专职联系人</td><td class="text-left" id="com_linkman"></td></tr>
									           <tr><td class="text-center">联系人（电话）</td><td class="text-left" id="com_tel" ></td></tr>
									           <tr><td class="text-center">联系人（电子邮箱）</td><td class="text-left" id="com_email"></td></tr>
<!-- 									           <tr><td><div style="margin-top: 5px;">申请单位（需求说明）</div><br><br><br><br><br><br>法定代表人签字<br><br>日期</td><td><div style="margin-top: 5px;"  id="com_applicationCompany"></div><br><br><br><br><br><br><br><br><br><br><br>盖章</td></tr> -->
<!-- 									           <tr><td><div style="margin-top: 5px">主管部门推荐意见</div><br><br><br><br><br><br>日期</td><td><br><br><br><br><br><br><br><br><br><br><br>盖章</td></tr> -->
									        </table>
									           <h4>备注:请根据真实情况填写信息，如有虚假，将影响单位优先申请公租房资格</h4>
									     </div>
									        <div style="page-break-after: always"><span style="display: none">&nbsp;</span></div><br><br>
									        <div style="text-align:center;" id = "userlist"></div>
                                    </div>
                                     <div style="text-align: center;" id = "print_div">
                                              <input type="button" onclick="preview()"  class="btn btn-info margin-right-10"  value="打印" id="prints" />
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