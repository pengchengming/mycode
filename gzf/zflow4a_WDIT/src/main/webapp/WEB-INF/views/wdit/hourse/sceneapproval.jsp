<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>房管局-现场审核列表</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<style type="text/css">
.bgred{ color:red}	
</style>
<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var viewCode = 'sceneapproval_v1';
var tableController;
var index=0;
$(function(){
 	tableController = createTableConfig.initData(viewCode);//画查询表头查询
   	var field = new Object();
 	field.title = '操作';
 	field.type = 'cutsomerRender';
 	field.doRender = function(data, container, config, rowIndex){
 		var id=data['id'];
 		var ref='查看';
 		var a = document.createElement('a');
 		if(data['statusname']=='待审核' ){
 			ref='审核';
 			if(data['mistiming']<=0){
 	 			index++;
 	 		}
 		}
 		$(a).attr('href', rootPath + 'hmcheck/sceneshowcompany?id='+id+'&type=1').html('&nbsp;'+ref+'&nbsp;');
 		/* $(a).attr('target', '_blank').attr('href', rootPath + 'company/rcbshowCompany?id='+id+'&type=0').html('&nbsp;查看&nbsp;'); */
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
	var applicant = $("#applicant").val();           	//单位名称
	var cardnum = $("#cardnum").val();					//身份证号码
	var acceptanceNumber = $("#acceptanceNumber").val(); //受理号
	var companytype = $("#companytype").val();			//审核状态
	var begin = $("#begin").val();			//提交开始时间
	var end = $("#end").val();			//提交结束时间
	var printname = $("#printname").val();
	
	$.post('<c:url value="/hmcheck/findXCSListData"/>', { 
   		code : viewCode,
        pageIndex : pageIndex,
   		"applicant":applicant,
   		"companytype":companytype,
   		"begin":begin,
   		"end":end,
   		"acceptanceNumber":acceptanceNumber,
   		"cardnum":cardnum,
   		"printname":printname,
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
	
	$(".btn-default").click(function(){
		$("#applicant").val("");         
		$("#cardnum").val("");				
		$("#acceptanceNumber").val("");
		$("#companytype").val(0);			
		$("#begin").val("");			
		$("#end").val("");		
		$("#printname").val("");
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
                                <li>房管局</li>
                                <li class="active">现场审核</li>
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
	                                        <table height="80" width=100%">
		                                        <tbody>
		                                        	<tr>
		                                        		<td>单位名称：</td>
		                                        		<td><input type="text" id='applicant' /></td>
		                                        		<td>审核状态：</td>
		                                        		<td><select id="companytype" style="width:200px;">
		                                        				<option value="0">全部</option>
		                                        				<option selected="selected" value="1">待审核</option>
		                                        				<option value="2">已审核</option>
		                                        			</select>
		                                        		</td>
		                                        		<td>提交时间：</td>
		                                        		<td><input type="text" style="width: 130px;height: 30px" class="Wdate" id="begin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
		                                        		~<input type="text" style="width: 130px;height: 30px" class="Wdate" id="end" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></td>
		                                        	</tr>
		                                        	<tr >
		                                        		<td>受理号：</td>
		                                        		<td><input type="text" id='acceptanceNumber' /></td>
		                                        		<td>身份证号码：</td>
		                                        		<td><input type="text" id="cardnum" /></td>
		                                        		<td>是否打印</td>
		                                        		<td><select id="printname" style="width:163px;">
		                                        		    <option value="">全部</option>
		                                        			<option value="0">否</option>
		                                        			<option value="1">是</option>
		                                        		</select></td>
		                                        	</tr>	
		                                        	<tr>
		                                        		<td colspan="6" align="right">
		                                        		&nbsp;<a class="btn btn-info" href="javascript:void(0);" onclick="queryClick(1)"> 检索 </a>&nbsp;
                                                			<a class="btn btn-default" href="#"> 清空 </a>
                                                			<%-- <a class="btn btn-default" target="_blank" href="<c:url value="#" />"> 新增 </a> --%>
                                                		</td>
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