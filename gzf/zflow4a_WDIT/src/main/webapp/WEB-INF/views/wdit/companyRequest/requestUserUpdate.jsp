<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>申请人员</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/script/jquery/jquery.form.js" />"></script>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestUser.js" />"></script>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestUserUpdate.js" />"></script>
 

<script type="text/javascript">
var requestId='${requestId}';
var requestUserId='${requestUserId}';
var companyId = '${sessionScope.USER.companyId}';
$(function(){
	$("#faimily_p").hide();
	 initPage();
	getRequestUserFun(requestUserId);
	var familyid=$("#applyForFamily_id").val();
	if(familyid+""=="503" || familyid+""=="504"){
		$("#faimily_p").show();
	}
	 $("#applyForFamily_id").change(function(){
	    	var familyid2=$("#applyForFamily_id").val();
	    	if(familyid2+""=="503" || familyid2+""=="504"){
	    		$("#faimily_p").show();
	    	}else{
	    		$("#faimily_p").hide();
	    	}
	});
})
function saveUser(){
	saveRequestUser();
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
                                <li>单位申请</li>
                                <li class="active">人才公寓申请</li>
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
                                    <form id="user_form" role="form" class="form-horizontal" >
                                        <h3 class="text-center padding-vertical-20">区筹公租房人员信息表</h3>
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="userName_id">
                                                        	<span style="color:#FF0000;">*</span>人员姓名
                                                    </label>
                                                    <div class="col-sm-9">
                                                        <input type="text" placeholder="人员姓名" id="userName_id" class="form-control">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="form-field-2">
                                                    	<span style="color:#FF0000;">*</span>户籍地
                                                    </label>
                                                    <div class="col-sm-9 radio-tab-nav" id="placeOfDomicile_div">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="form-field-3">
                                                        	<span style="color:#FF0000;">*</span>婚姻状况
                                                    </label>
                                                    <div class="col-sm-9"  id="maritalStatus_div"> 
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="form-field-5">
                                                        	<span style="color:#FF0000;">*</span>本市住房情况
                                                    </label>
                                                    <div class="col-sm-9" id="housingConditionsInTheCity_div"></div>
                                            	</div>
                                        	</div>
                                        </div>	
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="radioTab">
                                                    <div class="form-group">
                                                        <label class="col-sm-2 control-label" for="identityCardNumber_id">
                                                            	<span style="color:#FF0000;">*</span>身份证号码
                                                        </label>
                                                        <div class="col-sm-9">
                                                            <input type="text" placeholder="身份证号码" id="identityCardNumber_id" class="form-control">
                                                        </div>
                                                    </div>
                                                     
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="applicantPhone_id">
                                                       	 <span style="color:#FF0000;">*</span>申请人电话
                                                    </label>
                                                    <div class="col-sm-9">
                                                        <input type="text" placeholder="申请人电话" id="applicantPhone_id" class="form-control">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="residencePermitNumber_id">
                                                        	居住证号码
                                                    </label>
                                                    <div class="col-sm-9">
                                                        <input type="text" placeholder="居住证号码" id="residencePermitNumber_id" class="form-control">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="housingAccumulationFundAccount_id">
                                                        	住房公积金账户
                                                    </label>
                                                    <div class="col-sm-9">
                                                        <input type="text" placeholder="住房公积金账户" id="housingAccumulationFundAccount_id" class="form-control">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="pickDwelling_id">
                                                    	<span style="color:#FF0000;">*</span>所选小区
                                                    </label>
                                                    <div class="col-sm-9">
                                                    	<input type="hidden" id="pickDwelling_id">
                                                        <label id="pickDwelling_Name" style="margin-top: 6px"></label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="applyForFamily_id">
                                                        	<span style="color:#FF0000;">*</span>申请户型
                                                    </label>
                                                    <div class="col-sm-9">
                                                        <select class="form-control"  id="applyForFamily_id">
                                                        </select>
                                                        <p style="margin-top:10px" id="faimily_p">注：该户型仅适用单人使用！</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="form-group">
                                                    <label class="col-sm-1 control-label" for="permanentAddress_id">
                                                        	<span style="color:#FF0000;">*</span>户籍地址
                                                    </label>
                                                    <div class="col-sm-10">
                                                        <input type="text" placeholder="户籍地址" id="permanentAddress_id" class="form-control">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="form-group">
                                                    <label class="col-sm-1 control-label" for="address_id">
                                                        	联系地址
                                                    </label>
                                                    <div class="col-sm-10">
                                                        <input type="text" placeholder="联系地址" id="address_id" class="form-control">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                       	<div class="row">
                                            <div class="col-sm-6">
                                                <div class="radioTab">
                                                    <div class="form-group">
                                                        <label class="col-sm-2 control-label" for="education_id">
                                                            	<span style="color:#FF0000;">*学历</span>
                                                        </label>
                                                        <div class="col-sm-9">
                                                            <select id="education_id" class="form-control" ></select>
                                                        </div>
                                                    </div>
                                                     
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="educationCardCode_id">
                                                       	 <span style="color:#FF0000;">*学历证书编码</span>
                                                    </label>
                                                    <div class="col-sm-9">
                                                        <input type="text" placeholder="学历证书编码" id="educationCardCode_id" class="form-control">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="radioTab">
                                                    <div class="form-group">
                                                        <label class="col-sm-2 control-label" for="graduationSchool_id">
                                                            	<span style="color:#FF0000;">*毕业学校</span>
                                                        </label>
                                                        <div class="col-sm-9">
                                                            <input type="text" placeholder="毕业学校" id="graduationSchool_id" class="form-control">
                                                        </div>
                                                    </div>
                                                     
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label" for="graduationTime_id">
                                                       	 <span style="color:#FF0000;">*毕业时间</span>
                                                    </label>
                                                    <div class="col-sm-9">
                                                        <input type="text" placeholder="毕业时间 请按1999-01-01格式填写" id="graduationTime_id" class="form-control">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="radioTab">
                                                    <div class="form-group">
                                                        <label class="col-sm-2 control-label" for="specialty_id">
                                                            	<span style="color:#FF0000;">*专业</span>
                                                        </label>
                                                        <div class="col-sm-9">
                                                            <input type="text" placeholder="专业" id="specialty_id" class="form-control">
                                                        </div>
                                                    </div>
                                                     
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="radioTab">
                                                    <div class="form-group">
                                                        <label class="col-sm-2 control-label" for="specialty_id">
                                                            	<span style="color:#FF0000;">*符合条件</span>
                                                        </label>
                                                        <div class="col-sm-9">
                                                            <select id="accordwith" class="form-control" ><option value=''>请选择</option></select>
                                                        </div>
                                                    </div>
                                                     
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <h5 class="over-hidden">共同申请人<a  href="javascript:void(0)"  class="btn btn-xs btn-info pull-right"  onclick="addUserTelativeFun()" > 增加共同申请人 <i class="iconfont icon-arrowright"></i></a></h5>
                                                <div class="table-responsive">
                                                    <table id="requestUserTelative_table" class="table table-bordered table-hover text-center table-striped">
                                                        <thead>
                                                            <tr>
                                                                <td>与本人关系</td>
                                                                <td width="100">姓名</td>
                                                                <td>身份证号码</td>
                                                                <td width="100">操作</td>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="text-center">
                                        	<a  class="btn btn-info margin-right-10"  href="<c:url value="/companyRequest/requestUserList?requestId=${requestId}"/>">返回人员列表</a>                                        	
                                            <a href="javascript:void(0)" class="btn btn-info margin-right-10"  onClick="toUserExpandFun('userExpand_form')">下一步</a>
                                        </div>
                                    </form>
                                    
                                      <form  id="userExpand_form" role="form" class="form-horizontal"  style="display: none;">
                                        <h3 class="text-center padding-vertical-20">区筹公租房人员信息表</h3>
                                        <div class="row">
                                            <div class="col-sm-12" id="housing_tables">
                                                <h5 class="over-hidden">本市户籍住房信息<a href="javascript:void(0)" class="btn btn-xs btn-info pull-right" onclick="adduserHousing();">  增加住房信息 <i class="iconfont icon-arrowright"></i></a></h5>
                                                <div class="table-responsive" id="user_housing_table">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <h5 class="over-hidden">上传材料照片</h5>
                                                <div class="table-responsive">
                                                    <table id="userPhoto_table" class="table table-bordered table-hover text-center table-striped">
                                                        <thead>
                                                            <tr>
                                                                <td width="200">材料内容</td>
                                                                <td>照片</td>
                                                                <td width="100">样张</td>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td>申请人身份证<br>请分别上传身份证正反面照片</td>
                                                                <td id="applicantIdPhoto_td"></td>
                                                                <td><img src="<c:url value="/wdit/assets/images/sfz.jpg"/>" height="160" alt=""></td>
                                                            </tr>
                                                            <tr id="placeOfDomicile2_photo_tr">
                                                                <td>申请人居住证<br>请请上传户口本直到空白页的照片</td>
                                                                <td id="residencePermit_td"></td>
                                                                <td><img src="<c:url value="/wdit/assets/images/jzz.jpg"/>" height="160" alt=""></td>
                                                            </tr>
                                                            <tr id="placeOfDomicile1_photo_tr">
                                                                <td>申请人户口本<br>请上传户口本直到空白页的照片</td>
                                                                <td id="householdRegister_td"></td>
                                                                <td><img src="<c:url value="/wdit/assets/images/hkb.jpg"/>" height="160" alt=""></td>
                                                            </tr>
                                                            <tr id="maritalStatus_photo_tr">
                                                                <td>申请人结婚证<br>请上传结婚证照片</td>
                                                                <td id="marriageCertificate_td"></td>
                                                                <td><img src="<c:url value="/wdit/assets/images/jhz.jpg"/>" height="160" alt=""></td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="text-center">
                                        	<a  href="javascript:void(0)" class="btn btn-info margin-right-10" href="#" onClick="toUserExpandFun('user_form')">上一步</a>
                                            <a href="javascript:void(0)" class="btn btn-info margin-right-10" href="#" onclick="saveUser()">确认</a>
                                            <a class="btn btn-default" href="<c:url value="/companyRequest/requestUserList?requestId=${requestId}"/>" >关闭</a>
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
    </div>
</body>
</html>