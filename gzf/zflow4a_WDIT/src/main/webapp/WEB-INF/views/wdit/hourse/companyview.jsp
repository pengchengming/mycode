<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>房管局-材料预审列表</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript" src="<c:url value="/wdit/wdit/request/requestCompanyShow.js" />"></script>

<script type="text/javascript">
var createTable = new createTable();
var step=1;

var id='${requreid}';
var companystutus = ${companystutus}; //公司审核状态
var type='${type}';


$(function(){
	initPage();
// 	$.ajax({
// 		url : '<c:url value="/data/procedure"/>',
// 		type : "POST", 
// 	    async: false,
// 	    data : {
// 	    	p : 'R2014001|'+ id 
// 	    }, complete : function(xhr, textStatus){
// 			var data = JSON.parse(xhr.responseText);
// 			if(data&&data.length>0){
// 				var obj = data[0];
// 				if(obj.result == 1 && companystutus == 103){//当只有公司不通过，人员都不需要审的时候 
// 					$("#isforbid").attr("href","javascript:return false");
// 				}
// 			}
// 	   }
// 	})
});  




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
                                <li>房管局/材料预审</li>
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
                                    <div class="title padding-20">
                                         <ul class="nav nav-tabs" role="tablist">
                                            <li role="presentation" class="active"><a class="f18" href="#tab-1" aria-controls="tab-1" role="tab" data-toggle="tab">公司信息</a></li>
                                            <li role="presentation"><a class="f18" href="#tab-2" aria-controls="tab-2" role="tab" data-toggle="tab" id="isforbid">员工</a></li>
                                          
                                        </ul>
									 </div>
									 <div class="tab-content">
                                         <div role="tabpanel" class="tab-pane active" id="tab-1">
                                            <div class="table-responsive">
                                                <input type="hidden" id="companyId" />
                                                <%@ include file="/wdit/wdit/request/requestCompanyShow.jsp"%>
                                            </div>
                                           </div> 
									     <div role="tabpanel" class="tab-pane" id="tab-2">
									     <h4>人员信息列表</h4>
                                           <div class="table-responsive" id="form_table"></div>
                                        </div>
                                    </div>
                                </div>
                             </div>  
                         </div>
                    </div>
                </div>
                     </div>
                    </div>
                    <!-- end: PAGE CONTENT-->
                </div>
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>

</body>
</html>