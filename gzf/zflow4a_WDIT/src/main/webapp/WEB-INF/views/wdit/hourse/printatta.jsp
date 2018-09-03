<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>打印复印件</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>

 <script src="<c:url value="/wdit/assets/plugins/jQuery/jquery-1.4.4.min.js" />"></script>
<script language="javascript" src="<c:url value="/script/jquery/jquery.jqprint-0.3.js" />"></script>

<script type="text/javascript">
var requreid = ${requreid};
var step = ${step};
var rootPath = "<c:url value="/" />";
$(function(){
	var  byIdconfig2={
			fromConfig:[{
				formCode:"WDIT_Company_Request_User",
				currentField:"request_id",
				parentId:"request_id",
				aliasesName:"requestUser",
				fieldName:"id"
			},{
				formCode:"WDIT_Company_Request",
				currentField:"id",
				parentId:"request_id",
				aliasesName:"requestInfo",
				fieldName:"id,companyNature"
			}]
	}
	
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request_Photo",
			condition: "request_id="+requreid,
			byIdconfig:JSON.stringify(byIdconfig2)
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.code + "" == "1"){
				var conpanyinfo = data.results[0];
				var requestUsers=conpanyinfo.requestUser;
				var companyNature = conpanyinfo.requestInfo[0].companyNature;
				var html = '';
				if(conpanyinfo.photoType + ""== "2"){  //未三证合一
				  if(companyNature + "" == "1701")
				   trhtml = '<tr style="page-break-after:always;"><td>营业执照或企业法人证：<br><br><img  src="'+rootPath+conpanyinfo.businessLicense+'" style="width:600px" ></td></tr>'+
						    '<tr style="page-break-after:always;"><td>组织机构代码证：<br><br><img  src="'+rootPath+conpanyinfo.organizationPhoto+'" style="width:600px" ></td></tr>'+
						    '<tr style="page-break-after:always;"><td>税务登记证：<br><br><img  src="'+rootPath+conpanyinfo.taxationPhoto+'" style="width:600px" ></td></tr>';
				  else if(companyNature + "" == "1702")
				   trhtml = '<tr style="page-break-after:always;"><td>营业执照或企业法人证：<br><br><img  src="'+rootPath+conpanyinfo.businessLicense+'" style="width:600px" ></td></tr>'+
					    '<tr style="page-break-after:always;"><td>组织机构代码证：<br><br><img  src="'+rootPath+conpanyinfo.organizationPhoto+'" style="width:600px" ></td></tr>';
				  else if(companyNature + "" == "1703")
					   trhtml = '<tr style="page-break-after:always;"><td>组织机构代码证：<br><br><img  src="'+rootPath+conpanyinfo.organizationPhoto+'" style="width:600px" ></td></tr>';
				}else if(conpanyinfo.photoType + ""== "1"){  //三证合一
				   trhtml = '<tr style="page-break-after:always;"><td>营业执照或企业法人证：<br><br><img  src="'+rootPath+conpanyinfo.businessLicense+'" style="width:600px" ></td></tr>';
				}
				$("#companyimage").append(trhtml);
				$.each(requestUsers,function(i,requestUser){
					getRequestUserFun(requestUser.id);
				})
			}else{
			   alert("找不到公司照片");	
			}
		}
	}); 
});

function getRequestUserFun(requestUserId){
		var  byIdconfig2={
				fromConfig:[{  //住房图片
					formCode:"WDIT_Company_Request_User_housing",
					currentField:"requestUser_id",
					parentId:"id",
					aliasesName:"user_housing",
					fieldName:"id",
					fromConfig:[{
						formCode:"WDIT_Company_Request_User_photo",
						currentField:"user_housing_id",
						parentId:"id",
						aliasesName:"housePhoto",
						fieldName:"id,type,photo"
					}]
				},{
					formCode:"WDIT_Company_Request_User_relative",//共同申请人
					currentField:"requestUser_id",
					parentId:"id",
					aliasesName:"user_relative",
					fieldName:"id,relative,name",//共同申请人关系名字
					fromConfig:[{
						formCode:"WDIT_Company_Request_User_photo",
						currentField:"user_relative_id",
						parentId:"id",
						aliasesName:"userPhoto",
						fieldName:"id,type,photo"
					}]
				},{
					formCode:"WDIT_Company_Request_User_photo",
					currentField:"requestUser_id",
					parentId:"id",
					aliasesName:"userphoto",
					fieldName:"id,type,photo"
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
						var userHousings=requestUser.user_housing;//房产照片
						var userRelatives=requestUser.user_relative;//共同申请人
						var userPhotos=requestUser.userphoto; //身份证 居住证 结婚证
						if(userHousings&&userHousings.length>0){
							var housingHtml="";
							$.each(userHousings,function(j,housing){
								var index=j+1;
								housingHtml+="";
								var housePhotos= housing.housePhoto;
								if(housePhotos&& housePhotos.length>0){
									$.each(housePhotos,function(i,housePhoto){
										housingHtml+="<table class='table table-bordered table-hover text-center table-horizontal' style='width: 800px;margin: auto;'><tr style='page-break-after:always;'><td>申请人"+requestUser.userName+"有第（"+index+"）住房的住房证件：<br><br><img src='"+rootPath+housePhoto.photo+"' style='width:600px'></td></tr></table>"+
										             "<div style='page-break-after: always'><span style='display: none'>&nbsp;</span></div>";
									});
								}
							}); 
							$("#userimage").append(housingHtml);
						}
						if(userPhotos&& userPhotos.length>0){
							var  userPhotoJson=new Object();
							$.each(userPhotos,function(i,userPhoto){
								if(userPhotoJson[userPhoto.type])
									userPhotoJson[userPhoto.type].push(userPhoto.photo);
								else{
									var photoArray=new Array();
									photoArray.push(userPhoto.photo);
									userPhotoJson[userPhoto.type]=photoArray;
								}
							})
							$.each(userPhotoJson,function(type,userPhotoArray){
								var housingHtml="";
								var trhtml2 = '';	
								var trhtml3 = '';
								var tablehtml2 = '';
								var tablehtml3 = '';
								if(type==2){
									tablehtml2="<table class='table table-bordered table-hover text-center table-horizontal' style='width: 800px;margin: auto;'><tr><td>申请人"+requestUser.userName+"身份证<br><br>";
								}else if(type==3){
									tablehtml3="<table class='table table-bordered table-hover text-center table-horizontal' style='width: 800px;margin: auto;'><tr><td>申请人"+requestUser.userName+"居住证<br><br>";
								}
								
								$.each(userPhotoArray,function(i,photo){
								    if (type == 4)
											housingHtml+="<table class='table table-bordered table-hover text-center table-horizontal' style='width: 800px;margin: auto;'><tr style='page-break-after:always;'><td>申请人"+requestUser.userName+"户口本：<br><br><img src='"+rootPath+photo+"' style='width:600px'></td></tr></table>"+
											             "<div style='page-break-after: always'><span style='display: none'>&nbsp;</span></div>";
									else if(type == 5)
											housingHtml+="<table class='table table-bordered table-hover text-center table-horizontal' style='width: 800px;margin: auto;'><tr style='page-break-after:always;'><td>申请人"+requestUser.userName+"婚姻情况证明：<br><br><img src='"+rootPath+photo+"' style='width:600px'></td></tr></table>"+
											             "<div style='page-break-after: always'><span style='display: none'>&nbsp;</span></div>";
									else if(type ==2){
											if(trhtml2 == '')
												trhtml2+="<img  src='"+rootPath+photo+"'  style='width:600px'></td></tr>";
											else
												trhtml2+="<tr style='page-break-after:always;'><td><img  src='"+rootPath+photo+"'  style='width:600px'></td></tr></table>"+
												         "<div style='page-break-after: always'><span style='display: none'>&nbsp;</span></div>";
									}else if(type ==3){
											if(trhtml3 == '')
												trhtml3+="<img  src='"+rootPath+photo+"'  style='width:600px'></td></tr>";
											else
												trhtml3+="<tr style='page-break-after:always;'><td><img  src='"+rootPath+photo+"'  style='width:600px'></td></tr></table>"+
												         "<div style='page-break-after: always'><span style='display: none'>&nbsp;</span></div>";
										} 
								})
								tablehtml2 = tablehtml2 +trhtml2;
								tablehtml3 = tablehtml3 +trhtml3;
								$("#userimage").append(tablehtml2);$("#userimage").append(tablehtml3);$("#userimage").append(housingHtml);
							});
						}
						if(userRelatives && userRelatives.length>0){
							$.each(userRelatives ,function(i,userRelative){								
								var userRelativeHtml="<table class='table table-bordered table-hover text-center table-horizontal' style='width: 800px;margin: auto;'><tr><td>共同申请人（"+userRelative.name+"）身份证<br><br>";
								var userRelativeHtml2="";
								var userRelativePhotos= userRelative.userPhoto
								if(userRelativePhotos&& userRelativePhotos.length>0){
									$.each(userRelativePhotos,function(j,userRelativePhoto){
										if(j>1){
											userRelativeHtml2+="<table class='table table-bordered table-hover text-center table-horizontal' style='width: 800px;margin: auto;'><tr style='page-break-after:always;'><td>共同申请人（"+userRelative.name+"）其它资料照片：<br><br><img src='"+rootPath+userRelativePhoto.photo+"' style='width:600px'></td></tr></table><div style='page-break-after: always'><span style='display: none'>&nbsp;</span></div>";
										}else if (j == 1){
											userRelativeHtml+="<tr style='page-break-after:always;'><td><img  src='"+rootPath+userRelativePhoto.photo+"'  style='width:600px'></td></tr></table><div style='page-break-after: always'><span style='display: none'>&nbsp;</span></div>";	
										}else if (j == 0){
											userRelativeHtml+="<img  src='"+rootPath+userRelativePhoto.photo+"'  style='width:600px'></td></tr>";
										}
									});
								}
								$("#userimage").append(userRelativeHtml);
								$("#userimage").append(userRelativeHtml2);
							});
						}
					}
				}
			}
		}); 
	}
	
function preview() {
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"WDIT_Company_Request_Print",
			condition: "requestId="+requreid + " and step = " + step + " and type = 2"
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.code + "" == "1"){
			    var result = data.results;
			    var json = new Object();
			    json['formId'] = 96;
			    if(result&& result.length == 0){
			    	json['register'] = {"requestId":requreid,"step":step,"type":2,"frequency":1};
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
									     <div  style="text-align:center;">
									       <table id="companyimage" class="table table-bordered table-hover text-center table-horizontal" style="width: 800px;margin: auto;"></table>
									     </div>
									     <div style="page-break-after: always"><span style="display: none">&nbsp;</span></div>
                                         <div id="userimage"></div>
                                         <div style="text-align: center;" id = "print_div">
                                         <input type="button" onclick="preview()"  class="btn btn-info margin-right-10" value="打印" id="prints" />
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