<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>查看申请批次明细</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestCompanyShow.js" />"></script>
</head>

<script type="text/javascript">
var id='${id}';
var createTable = new createTable();
var step=0;
$(function(){
	initPage();
	$("#showstep").hide();
	$("#showreturnremark").hide();
});

function show(id){
	if(id==1){
		$("#showcompany").show();
		$("#showuser").hide();
	}else if(id==2){
		$("#showcompany").hide();
		$("#showuser").show();
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
                                <li class="active"><a  href="<c:url value="/companyRequest/requestList" />">我的申请</a></li>
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
                                <input id="companyId" type="hidden" />
                                	<div class="row" style="margin-bottom:10px;margin-top:10px;">
                                		<div class="row">
					                        <div class="col-sm-12">
					                            <ul class="step">
					                                <li id="shenqing">申请</li>
					                                <li id="shouli">受理</li>
					                                <li id="shenhe">审核</li>
					                                <li id="zhongshen">终审</li>
					                            </ul>
					                        </div>
					                    </div>
	                                    <div class="col-sm-12">
	                                        <div class="form-group">
	                                            <div class="title padding-20">
			                                         <ul class="nav nav-tabs" role="tablist">
			                                            <li role="presentation" class="active"><a class="f18" href="#" onclick="show(1)" aria-controls="tab-1" role="tab" data-toggle="tab">公司信息</a></li>
			                                            <li role="presentation"><a class="f18" href="#" onclick="show(2)" aria-controls="tab-2" role="tab" data-toggle="tab">员工</a></li>
			                                        </ul>
												 </div>
	                                        </div>
	                                    </div>
	                                </div> 
                                	<div id="showcompany" class="table-responsive" style="overflow: hidden;margin-top:10px;" tabindex="2">
										<%@ include file="/wdit/wdit/request/requestCompanyShow.jsp"%> 
                               		</div>
                               		<div id="showuser" style="display:none">
                               			<h4>人员信息列表</h4>
                               			<div class="table-responsive" id="form_table">
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

</body>
</html>