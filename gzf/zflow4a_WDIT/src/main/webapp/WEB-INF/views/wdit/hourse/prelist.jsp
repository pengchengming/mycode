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
<style type="text/css">
.bgred{ color:red}	
</style>
<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var viewCode = 'precheck_v1';
var tableController;
var index=0;
$(function(){
 	tableController = createTableConfig.initData(viewCode);//画查询表头查询
   	var field = new Object();
 	field.title = '操作';
 	field.type = 'cutsomerRender';
 	field.doRender = function(data, container, config, rowIndex){
 		var a = document.createElement('a');
 		$(a).html('&nbsp;审核&nbsp;');
 		if (data['statusname'] != '待审核'){
 			$(a).html('&nbsp;查看&nbsp;');
 		}else{
 			if(data['mistiming']<=0){
		 		index++;
		 }
 		}
 		var requreid = data['id'];
 		var companystutus = data['status'];
 		$(a).attr('href', rootPath + 'hmcheck/companyapproval?requreid='+requreid+'&companystutus='+companystutus);
 		$(container).append(a);
 	};
 	tableController.tableConfig.fields.push(field);
	queryClick(1);
});

var createTable = new createTable();
var currentpage;
function queryClick(pageIndex) {
	$("#form_table").html("");  
	var where= " where 1 =1 and t1.status >100"
	var applicant=$("#wdit_company_applicant").val();
	if(applicant&&$.trim(applicant))
		where +=" and t1.applicant  like '%"+applicant+"%'";
	var status = $("#companystatus").val();
	
	if(status==101)//待审核
		where += " and t1.status in (101,104,105)";
	else if(status == 102)//已审核
		where += " and t1.status not in(101,104,105)";
	else if(status == 103)//全部
		where += " ";
	var start_time = $("#start_time").val();
	var end_time = $("#end_time").val();
	if(start_time)
		where += " and (DATE(t1.createDate) > '" + start_time+"' or DATE(t1.createDate) = '"+start_time+"')";
	if(end_time)
		where +=  " and (DATE(t1.createDate) < '"+ end_time + "' or DATE(t1.createDate) = '"+end_time+"') ";
		
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
	$("#companystatus").val(101);
	$("#start_time").val('');
	$("#end_time").val('');
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
                                <li>房管局</li>
                                <li class="active">材料预审</li>
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
                                                <input   type="text" id='wdit_company_applicant' width="250" />
                                                <label>审核状态：</label>
												 <select id="companystatus" style="width: 140px;">
												   <option value="101">待审核</option>
												   <option value="102">已审核</option>
												   <option value="103">全部</option>											  
                                                </select>
                                                <label>提交时间：</label>
                                                <input type="text" style="width: 130px;height: 30px"  id="start_time" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" >
                                                -
                                                <input type="text" style="width: 130px;height: 30px" id="end_time"  class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" >
                                                <a class="btn btn-info" href="javascript:void(0);" onclick="queryClick(1)">检索</a>
                                                <a class="btn btn-default" href="javascript:void(0);" onclick= "enpty_condition()">清空</a>
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