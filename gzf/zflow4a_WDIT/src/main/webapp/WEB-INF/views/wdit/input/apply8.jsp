<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>导入数据</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<link rel="stylesheet" type="text/css"
	href='<c:url value="/script/layer/skin/layer.css" />' />
<link rel="stylesheet" type="text/css"
	href='<c:url value="/script/layer/skin/layer.ext.css" />' />
<script type="text/javascript"
	src='<c:url value="/script/layer/layer.js" />'></script>
<script type="text/javascript" charset="utf-8"
	src="<c:url value="/javascript/My97DatePicker/WdatePicker.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/script/deco/createTableCompleteConfig.js" />"></script>
<script type="text/javascript">

var roles = "${sessionScope.ROLES}";
var guserid=${sessionScope.USER.id}; 

$(function(){
// 	var html =   '<a target="_blank" href="<c:url value="/import/importdatadetail?type=9" />">导入form</a></br>'+
//  '<a target="_blank" href="<c:url value="/import/importdatadetail?type=10" />">导入form_property</a></br>'+
//  '<a target="_blank" href="<c:url value="/import/importdatadetail?type=11" />">导入form_view</a></br>'+
//  '<a target="_blank" href="<c:url value="/import/importdatadetail?type=12" />">导入form_view_property</a></br>'+
//  '<a target="_blank" href="<c:url value="/import/importdatadetail?type=1" />">导入数据词典</a></br>'+
//  '<a target="_blank" href="<c:url value="/import/importdatadetail?type=9" />">导入品类</a></br>'+
//	'<a target="_blank" href="<c:url value="/import/importdatadetail?type=2" />">导入A004材料主数据</a></br>'+
//	'<a target="_blank" href="<c:url value="/import/importdatadetail?type=3" />">导入A004品牌</a></br>'+
//	'<a target="_blank" href="<c:url value="/import/importdatadetail?type=4" />">导入A005_施工项基础表</a></br>'+
//	'<a target="_blank" href="<c:url value="/import/importdatadetail?type=5" />">导入A004套系</a></br>'+
//	'<a target="_blank" href="<c:url value="/import/importdatadetail?type=6" />">导入A006_基材计算公式</a></br>'+
//	'<a target="_blank" href="<c:url value="/import/importdatadetail?type=7" />">导入A007_套餐主数据-0510  S3&S5</a></br>'+
//	'<a target="_blank" href="<c:url value="/import/importdatadetail?type=8" />">导入A007开关面板</a></br>';
	var html = '<a target="_blank" href="<c:url value="/import/importPath/tmp_zflow_form" />">导入form</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_zflow_form_property" />">导入form_property</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_zflow_form_view" />">导入form_view</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_zflow_form_view_property" />">导入form_view_property</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_sys_datadictionary" />">导入字典表A002</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_NPR_Category" />">导入物品类别表</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_NPR_Goods" />">导入物品表</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_NPR_Vendor" />">导入供应商</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_NPR_CostCenter_Budget" />">导入成本中心预算</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_NPR_CostCenter" />">导入成本中心</a></br></br>' 
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_NPR_OutSubject" />">导入外方科目</a></br></br>' 
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_NPR_CnSubject" />">导入中方科目</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_wdit_company" />">导入公司</a></br></br>'
    +'<a target="_blank" href="<c:url value="/import/importPath/tmp_wdit_request_user" />">导入房管局资质审核人员表</a></br></br>';
//     +'<a target="_blank" href="<c:url value="/import/importPaths/tmp_deco_customer" />">导入客户</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_switch_panel_category" />">导入开关面板功能分类A002</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_material_category" />">导入材料品类A002</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_material_brand" />">导入材料品牌A004</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_material" />">导入材料A004</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_item" />">导入施工项A005</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_material_set" />">导入材料套系A004</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_base_material_item&tmp_deco_base_material_item_detail" />">导入基材计算公式A006</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_package_item&tmp_deco_package_material" />">导入套餐主数据A007</a>'
//     +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_package_item" />">导入S3标配施工项A007</a>'
//     +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_package_material"/>">导入S5套餐材料A007</a></br></br>'
//     +'<a target="_blank" href="<c:url value="/import/importPath/tmp_deco_package_switch_category" />">导入全装修开关面板A007</a></br></br>';




$('#form_table').append(html);

});

// function testloading(){
// 	  $.ajax({
// 	        type:"get",
// 	        cache:false,
// 	        url:"ajaxpage.aspx?t=getcity",
// 	        dataType:"json",
// 	        beforeSend:function(){
// 	           $("#vvv").append('<img src='../css/images/ajax-loading.gif'  />');
// 	        },
// 	        success:function(data){
// 	           $("#city").html(data.info);//添加下拉框的option
// 	        },
// 	        complete: function() {$("#vvv").remove();
// 	        }

// 	     })
// }
	
</script>
</head>
<!-- <body> -->
<%-- <%@ include file="/WEB-INF/views/wdit/head.jsp"%>  --%>
<!-- 	<div class="content"> -->
<%-- 		<%@ include file="/WEB-INF/views/wdit/leftMenu.jsp"%>  --%>
<!-- 		<div class="main"> -->
<!-- 			<div class="main_body"> -->
<!-- 				<div style="text-align: left;"></div> -->
<!-- 				<div class="main_title">导入数据</div> -->
<!-- 				<div class="search" id="search_div"></div> -->
<!-- 				搜索表格展示 -->
<!-- 				<div id="form_table"></div> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- 	</div>  -->
<%-- 	<%@ include file="/WEB-INF/views/wdit/foot.jsp"%> --%>
<!-- </body> -->
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
                                <li>导入数据</li>
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
                               		<div id="form_table"></div>
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