<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>申请参数设置</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var viewCode = 'log_v1';
var tableController;
$(function(){
 	tableController = createTableConfig.initData(viewCode,"search_div","queryClick(1)");//画查询表头查询
	queryClick(1);
});

var createTable = new createTable();
var currentpage;
function queryClick(pageIndex) {
	$("#form_table").html("");  //清空form_table的div
	currentpage = pageIndex;  //查询页面数据
    var where= createTableConfig.getSelectConditionSql(tableController);//画查询，输入文本框查询需要使用
	if(!where) where+= " where 1=1 and t1.saveType =10 ";  //判断where语句
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
        }else{
			$("#form_table").html("<div style='text-align: center'>没有记录</div>");
        } 
    });

}
function savebtn(){
	var sql=$("#sql").val();
	$.ajax({
		url : rootPath+'/sql/updatesql',
		type : "POST", 
        async: false,
        data : {
        	sql:sql,
        	type:$("#sql_type").val()
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			$("#returnSelect_id").val(xhr.responseText);
			if(data.successMsg){
				alert("操作成功！！");
				queryClick(1);
	 		}else{
	 			alert("操作失败");
	 			return false;
	 		}
		    }
        });
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
                                <li>申请参数设置</li>
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
                                	<form class="form-horizontal" role="form">
                                	<div class="row">
                                    	<div class="col-sm-9">
                               				<div class="form-group">
                               					<label class="col-sm-2 control-label" for="form-field-1">执行sql</label>
                               					<div class="col-sm-9">
                               						<textarea id="sql" class="form-control"  ></textarea>
                               						sql类型：<input id="sql_type" value="1" > （1为执行，2为查询，必填）
                               					</div>
                               				</div>
                               			</div>
                                    </div>
                                    <div class="row">
                                    	<div class="col-sm-9">
                               				<div class="form-group">
											<input type="button" value="提交" onclick="savebtn()" />
                               				</div>
                               			</div>
                               		</div>
                               		<div class="table-responsive" id="form_table"> 
                                    </div>
                                    <textarea id="returnSelect_id" cols="200" rows="50"></textarea>
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

</body>
</html>