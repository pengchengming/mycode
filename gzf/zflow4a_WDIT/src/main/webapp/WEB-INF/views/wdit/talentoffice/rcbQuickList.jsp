<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>人才办-绿色通道审核</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<style type="text/css">
.bgred{ color:red}	
</style>
<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var viewCode = 'quickapproval_v1';
var tableController;
var index=0;
$(function(){
	
 	tableController = createTableConfig.initData(viewCode);//画查询表头查询
   	var field = new Object();
 	field.title = '操作';
 	field.type = 'cutsomerRender';
 	field.doRender = function(data, container, config, rowIndex){
 		var id=data['id'];
 		var a = document.createElement('a');
 		var status=data['status'];
 		var ref='查看';
 		if(data['statusname']=='待审核' ){
 			ref='审核';
 			if(data['mistiming']<=0){
 	 			index++;
 	 		}
 		}
 		$(a).attr('href', rootPath + 'talentoffice/rcbQuickshowCompany?id='+id+'&type=0').html('&nbsp;'+ref+'&nbsp;');
 		
 		$(container).append(a);
 	};
 	tableController.tableConfig.fields.push(field);
	queryClick(1);
});

var createTable = new createTable();
var currentpage;
function queryClick(pageIndex) {
	$("#form_table").html("");  //清空form_table的div
	currentpage = pageIndex;  //查询页面数据
    var where= createTableConfig.getSelectConditionSql(tableController);//画查询，输入文本框查询需要使用
	if(!where) where+= " where 1=1 and t1.companyClassification=1504 ";  //判断where语句 companyClassification 1504=企业类型人才办
// 	where +=" and t3.status in (4202,5202,5204,5203) ";
	where +=" and (t1.status >500 OR t1.status =202) ";
	
	var companytype=$("#approvalstatus").val();
	if(companytype&&companytype.length>0){
		if(companytype==0){
			where +=" and t1.status in(202,504,505) ";	
		}else if(companytype==1){
			where +=" and t1.status not in(202,504,505) ";
		}
	}
	var begin=$("#begin").val();
	var end =$("#end").val();
	if(begin)
		where += " and (DATE(t1.createDate) > '" + begin+"' or DATE(t1.createDate) = '"+begin+"')";
	if(end)
		where +=  " and (DATE(t1.createDate) < '"+ end + "' or DATE(t1.createDate) = '"+end+"') ";
		
	var acceptanceNumber=$("#acceptanceNumber").val();
	if(acceptanceNumber&&acceptanceNumber.length>0){
		where +=" and acceptanceNumber like '%"+acceptanceNumber+"%'";
	}
	var cardnum=$("#cardnum").val();
	if(cardnum&&cardnum.length>0){
		where +=" and exists( select 1 from  wdit_company_request_user where request_id=t1.id and  identityCardNumber like '%"+cardnum+"%'  ) ";
	}
	var applicant = $("#applicant").val();
	if(applicant!=null && applicant.length>0){
		where += " and t1.applicant like '%" + applicant +"%'";
	};
	
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
           		var html=$("#form_table_createDiv tbody tr:eq("+i+") td:eq(5)").html();
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

function onclear(){
	$(":input").each(function(){
		 if($(this).attr("type")=="text"){
			 $(this).val("");
		 }
	})
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
                                <li>人才办</li>
                                <li class="active">绿色通道审核</li>
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
                                    <div class="row">
                                        <div class="col-sm-12">
	                                        <table style="margin-top:10px;">
		                                        <tbody>
		                                        	<tr>
		                                        		<td>单位名称：</td>
		                                        		<td><input type="text" id='applicant' /></td>
		                                        		<td>审核状态：</td>
		                                        		<td><select id="approvalstatus" style="width:163px;">
		                                        			<option>全部</option>
		                                        			<option selected="selected" value="0">待审核</option>
		                                        			<option value="1">已审核</option>
		                                        		</select></td>
		                                        		<td>提交时间：</td>
		                                        		<td><input type="text" style="width: 130px;height: 30px" class="Wdate" id="begin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
		                                        		~<input type="text" style="width: 130px;height: 30px" class="Wdate" id="end" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
		                                        	</tr>
		                                        	<tr>
		                                        		<td>受理号：</td>
		                                        		<td><input type="text" id='acceptanceNumber' /></td>
		                                        		<td>身份证号码：</td>
		                                        		<td><input type="text" id="cardnum" /></td>
		                                        		<td colspan="2"><a class="btn btn-info" href="javascript:void(0);" onclick="queryClick(1)">检索</a>
                                                	<a class="btn btn-default" onclick="onclear()">清空</a></td>
		                                        	</tr>
		                                        </tbody>
	                                        </table>
                                        	<div class="table-responsive" id="form_table" style="margin-top:10px;">
                                        		
                                        	</div>
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