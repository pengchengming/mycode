<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>委办-资质审核列表</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<style type="text/css">
.bgred{ color:red}	
</style>
<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var viewCode = 'approvelist_v1';
var roles = "${sessionScope.ROLES}";
var tableController;
var companyClassification;
var index=0;
$(function(){
 	tableController = createTableConfig.initData(viewCode,"search_div","queryClick(1)");//画查询表头查询
   	var field = new Object();
 	field.title = '操作';
 	field.type = 'cutsomerRender';
 	field.doRender = function(data, container, config, rowIndex){
 		var requreid = data['id'];
 		var statusname = data['statusname'];
 		var showdiv = data['showdiv'];
 		var a = document.createElement('a');
 		$(a).html('&nbsp;审核&nbsp;');
 		if (statusname != '待审核'){
 			$(a).html('&nbsp;查看&nbsp;');
 		}else{
 			if(data['mistiming']<=0){
 	 			index++;
 	 		}
 		}
 		$(a).attr('href', rootPath + '/assign/companyapproval?requreid='+requreid+'&showdiv='+showdiv);
 		$(container).append(a);
 	};
 	tableController.tableConfig.fields.push(field);
	queryClick(1);
});

var createTable = new createTable();
var currentpage;
function queryClick(pageIndex) {
	$("#form_table").html("");  
	var where= " where 1 = 1 "
	if(roles.indexOf("ASSIGN_ROLE") == 0){ //商委
		companyClassification = 1503;
	}else if(roles.indexOf("SCIASSIGN_ROLE") == 0){  //科委
		companyClassification = 1502;
	}else if(roles.indexOf("BUSASSIGN_ROLE") == 0){  //经委
		companyClassification = 1501;
	}
	if(companyClassification){
		where += " and t1.companyClassification = " + companyClassification;
	}
	//输入框条件
	var applicant = $("#wdit_company_applicant").val();
	if(applicant&&$.trim(applicant))
		where +=" and t1.applicant  like '%"+applicant+"%'";
	var status = $("#companystatus").val();
	if(status==301)//待审核
		where += " and (t1.status = 202 or t1.status = 301 or t1.status = 304 or t1.status = 305)";
	else if(status == 302)//已审核
		where += " and (t1.status = 302 or t1.status = 303 or (t1.status > 304 and t1.status != 305))";
	else if(status == 303)//全部
		where += " and (t1.status = 202 or t1.status > 205)";
	var acceptanceNumber = $("#acceptanceNumber").val();
	if(acceptanceNumber&&$.trim(acceptanceNumber))
		where +=" and t1.acceptanceNumber  like '%"+acceptanceNumber+"%'";
	var start_time = $("#start_time").val();
	var end_time = $("#end_time").val();
	if(start_time)
		where += " and (DATE(t1.createDate) > '" + start_time+"' or DATE(t1.createDate) = '"+start_time+"')";
	if(end_time)
		where +=  " and (DATE(t1.createDate) < '"+ end_time + "' or DATE(t1.createDate) = '"+end_time+"') ";
	var identityCardNumber = $("#identityCardNumber").val();
	if(identityCardNumber&&$.trim(identityCardNumber))
	    where+= " and EXISTS (SELECT 1 FROM WDIT_Company_Request_User WHERE request_id = t1.id and identityCardNumber like '%"+identityCardNumber+"%') ";
	
	
	
	$.post('<c:url value="/createSelect/findselectData"/>', { 
   		code : viewCode,
   		selectConditionSql : where,
        pageIndex : pageIndex,
    	pageSize : 10
	}, function(data){
			if(data.code + "" == "1"){
			data.results = eval("(" + data.results + ")");
           	data.paged = eval("(" + data.paged + ")");
           	createTable.registTable($('#form_table'), tableController.tableConfig, data, "queryClick");
           	for (var i = 0; i < 10; i++) {
           		if(index==0){
           			return false;
           		}
           		var html=$("#form_table_createDiv tbody tr:eq("+i+") td:eq(4)").html();
           		if(html=="待审核"){
           			$("#form_table_createDiv tbody tr:eq("+i+")").addClass("bgred");
           			index--;
           		}
			}
       		index=0;	
		}else{
			$("#form_table").html("<div style='text-align: center'>没有记录</div>");
        } 
    });
}

function enpty_condition(){
	$("#wdit_company_applicant").val('');
	$("#companystatus").val(301);
	$("#start_time").val('');
	$("#end_time").val('');
	$("#acceptanceNumber").val('');
	$("#identityCardNumber").val('');
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
                                <li>委办</li>
                                <li class="active">资质审核</li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <!-- start: DATE/TIME PICKER PANEL -->
                            <div class="panel panel-white">
                                <div class="panel-body no-padding-top">
                                    <div class="row">
                                        <div class="col-sm-12">
                                           <div class="form-search">
                                                <label>单位名称：</label>
                                                <input   type="text" id='wdit_company_applicant' width="300" />
                                                <label>审核状态：</label>
												 <select id="companystatus" style="width: 140px;">
												   <option value="301">待审核</option>
												   <option value="302">已审核</option>
												   <option value="303">全部</option>		
                                                </select>
                                                <label>提交时间：</label>
                                                <input type="text" style="width: 150px;height: 30px"  id="start_time"  class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" >
                                                -
                                                <input type="text" style="width: 150px;height: 30px"  id="end_time"  class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" > 
                                                 <br><br>
                                                 <label>受理号：</label>
                                                 <input  id="acceptanceNumber" type="text"  width="300" />
                                                 <label>身份证号码：</label>
                                                 <input  id="identityCardNumber"  type="text"  width="500" />
                                                 <a class="btn btn-info" href="javascript:void(0);" onclick="queryClick(1)" style="margin-left: 200px;">检索</a>
                                                 <a class="btn btn-default" href="javascript:void(0);"  onclick= "enpty_condition()">清空</a>
                                            </div>
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
        </div> 
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>
</body>
</html>