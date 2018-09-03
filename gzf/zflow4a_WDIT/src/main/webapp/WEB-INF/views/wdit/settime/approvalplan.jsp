<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>审核进度</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var viewCode='approvalstep_v1';

var createTable = new createTable();


$(function(){
	layer.config({
		skin : 'layer-ext-moon',
		extend : 'skin/moon/style.css'
	});
	
 	tableController = createTableConfig.initData(viewCode,"","queryClick(1)");//画查询表头查询
   	var field = new Object();
 	field.title = '审核状态';
 	field.type = 'cutsomerRender';
 	field.doRender = function(data, container, config, rowIndex){
 		var laststep=data['lastStep'];
 		var laststatus=data['laststatus'];
 		var onestep=data['onestep'];
 		var twostep=data['twostep'];
 		var threestep=data['threestep'];
 		var fourstep=data['fourstep'];
 		var fivestep=data['fivestep'];
 		

 		if(onestep)
 			onestep=onestep.replace(/-/g,"/");
 		if(twostep)
 			twostep=twostep.replace(/-/g,"/");
 		if(threestep)
 			threestep=threestep.replace(/-/g,"/");
 		if(fourstep)
 			fourstep=fourstep.replace(/-/g,"/");
 		if(fivestep)
 			fivestep=fivestep.replace(/-/g,"/");
 		  
 		
 		var aDate = new Date(twostep);
 		var bDate = new Date(onestep);
 		var fen1 = ((aDate.getTime()-bDate.getTime())/1000)/60;
 		var onetime = parseInt(fen1/(24*60));
 		
 		var cDate = new Date(fourstep);
 		var dDate = new Date(twostep);
 		var fen2 = ((cDate.getTime()-dDate.getTime())/1000)/60;
 		var twotime = parseInt(fen2/(24*60));
 		
 		var eDate = new Date(fivestep);
 		var fDate = new Date(fourstep);
 		var fen3 = ((eDate.getTime()-fDate.getTime())/1000)/60;
 		var threetime = parseInt(fen3/(24*60));
 		
 		var gDate = new Date(fivestep);
 		var hDate = new Date(twostep);
 		var fen4 = ((gDate.getTime()-hDate.getTime())/1000)/60;
 		var fourtime = parseInt(fen4/(24*60));
 			
//  		var onetime=data['onetime'];
//  		var twotime=data['twotime'];
//  		var threetime=data['threetime'];
 		var table="";
 		if(laststep==5){
 			if(isNaN(twotime)){
 				table+='<table>'+
 				'<tr><td><font style="color:green">受理</font></td>'+
 				'<td style="color:green">-</td>'+
 				'<td><font style="color:green">公租房资质审核（'+onetime+'）</font></td>'+
 				'<td style="color:green">{&nbsp;</td>'+
 				'<td>企业审核<br/>人员审核</td>';
 	 			if(laststatus==5202){
 	 				table+='<td style="color:green">}</td><td style="color:green">终审（'+fourtime+'）</td>'+
 					'</table>';
 	 			}else if(laststatus==5203){
 	 				table+='<td style="color:green">}</td><td style="color:red">终审（'+fourtime+'）</td>'+
 					'</table>';
 	 			}else{
 	 				table+='<td>}</td><td>终审</td>'+
 					'</table>';
 	 			}
 			}else{
	 			table+='<table>'+
				'<tr><td><font style="color:green">受理</font></td>'+
				'<td style="color:green">-</td>'+
				'<td><font style="color:green">公租房资质审核（'+onetime+'）</font></td>'+
				'<td style="color:green">{&nbsp;</td>'+
				'<td style="color:green">企业审核（'+twotime+'）<br/>人员审核（'+twotime+'）</td>';
	 			if(laststatus==5202){
	 				table+='<td style="color:green">}</td><td style="color:green">终审（'+threetime+'）</td>'+
					'</table>';
	 			}else if(laststatus==5203){
	 				table+='<td style="color:green">}</td><td style="color:red">终审（'+threetime+'）</td>'+
					'</table>';
	 			}else{
	 				table+='<td style="color:green">}</td><td>终审</td>'+
					'</table>';
	 			}
 			
 			}
 		}else if(laststep==4){
 			var status=data['status'];
 			var companyDate=data['companyDate'];
 			
 			if(companyDate)
 				companyDate=companyDate.replace(/-/g,"/");
 			var comdate = new Date(companyDate);
 	 		var onedate = new Date(onestep);
 	 		var codate = ((comdate.getTime()-onedate.getTime())/1000)/60;
 	 		var qiyedate = parseInt(codate/(24*60));
 			table+='<table>'+
			'<tr><td><font style="color:green">受理</font></td>'+
			'<td style="color:green">-</td>'+
			'<td><font style="color:green">公租房资质审核（'+onetime+'）</font></td>'+
			'<td style="color:green">{&nbsp;</td>';
			
 			if(laststatus==4202){
 				table+='<td><span style="color:green">企业审核（'+twotime+'）</span><br/><span style="color:green">人员审核（'+twotime+'）</span></td>';
 			}else if(laststatus==4203){
 				table+='<td><span style="color:green">企业审核（'+twotime+'）</span><br/><span style="color:red">人员审核（'+twotime+'）</span></td>';
 			}else{
 				if(status==302){ 				
 	 				table+='<td><span style="color:green">企业审核('+qiyedate+')</span><br/><span>人员审核</span></td>';
 	 			}else{
 		 			table+='<td><span>企业审核</span><br/><span>人员审核</span></td>';
 	 			}
 			}
 			
 			table+='<td>}</td><td>终审</td>'+
			'</table>';
 		}else if(laststep==3){
 			var threeDate = new Date(threestep);
 	 		var twoDate = new Date(twostep);
 	 		
 	 		var fenthree = ((threeDate.getTime()-twoDate.getTime())/1000)/60;
 	 		var time3 = parseInt(fenthree/(24*60));
 			table+='<table>'+
			'<tr><td><font style="color:green">受理</font></td>'+
			'<td style="color:green">-</td>'+
			'<td><font style="color:green">公租房资质审核（'+onetime+'）</font></td>'+
			'<td style="color:green">{&nbsp;</td>';
 				table+='<td><span style="color:red">企业审核('+time3+')</span><br/><span>人员审核</span></td>';
 			table+='<td>}</td><td>终审</td>'+
			'</table>';
 		}else if(laststep==2){
 			var status=data['status'];
 			var companyDate=data['companyDate'];
 			if(companyDate)
 				companyDate=companyDate.replace(/-/g,"/");
 			var comdate = new Date(companyDate);
 	 		var onedate = new Date(onestep);
 	 		var codate = ((comdate.getTime()-onedate.getTime())/1000)/60;
 	 		var qiyedate = parseInt(codate/(24*60));
 			
 			table+='<table>'+
			'<tr><td><font style="color:green">受理</font></td>'+
			'<td style="color:green">-</td>';
			
			
 			if(laststatus==2202){
 				table+='<td><font style="color:green">公租房资质审核（'+onetime+'）</font></td>'+
 				'<td style="color:green">{&nbsp;</td>';
 				if(status==302){ 				
 	 				table+='<td><span style="color:green">企业审核('+qiyedate+')</span><br/><span>人员审核</span></td><td>}</td><td>终审</td>'+
 	 				'</table>';
 	 			}else{
 		 			table+='<td><span>企业审核</span><br/><span>人员审核</span></td><td>}</td><td>终审</td>'+
 		 			'</table>';
 	 			}
 			}else if(laststatus==2203){
 				table+='<td><font style="color:red">公租房资质审核（'+onetime+'）</font></td>'+
 				'<td>{&nbsp;</td>';
 				table+='<td><span>企业审核</span><br/><span>人员审核</span></td><td>}</td><td>终审</td>'+
		 			'</table>';
 			}else{
 				table+='<td><font>公租房资质审核</font></td>'+
 				'<td>{&nbsp;</td>';
 				table+='<td><span>企业审核</span><br/><span>人员审核</span></td><td>}</td><td>终审</td>'+
		 			'</table>';
 			}
 			
			
 		}else if(laststep==1){
 			if(laststatus==1202){
 				table+='<table>'+
 				'<tr><td><font style="color:green">受理</font></td>'+
 				'<td style="color:green">-</td>';
 			}else if(laststatus==1203){
 				table+='<table>'+
 				'<tr><td><font style="color:red">受理</font></td>'+
 				'<td style="color:green">-</td>';
 			}else{
 				table+='<table>'+
 				'<tr><td><font style="color:green">受理</font></td>'+
 				'<td style="color:green">-</td>';
 			}
 			table+='<td><font>公租房资质审核</font></td>'+
				'<td>{&nbsp;</td>'+
				'<td><span>企业审核<span><br/><span>人员审核</span></td><td>}</td><td>终审</td>'+
			'</table>';
 		}else if(laststep==0){
 			table+='<table>'+
				'<tr><td><font>受理</font></td>'+
				'<td>-</td>'+'<td><font>公租房资质审核</font></td>'+
				'<td>{&nbsp;</td>'+
				'<td><span>企业审核<span><br/><span>人员审核</span></td><td>}</td><td>终审</td>'+
			'</table>';
 		}else if(laststep==6){
 			if(isNaN(twotime)){
 				table+='<table>'+
 				'<tr><td><font style="color:green">受理</font></td>'+
 				'<td style="color:green">-</td>'+
 				'<td><font style="color:green">公租房资质审核（'+onetime+'）</font></td>'+
 				'<td style="color:green">{&nbsp;</td>'+
 				'<td>企业审核<br/>人员审核</td>';
 	 			if(laststatus==6202){
 	 				table+='<td style="color:green">}</td><td style="color:green">终审（'+fourtime+'）</td>'+
 					'</table>';
 	 			}else if(laststatus==6203){
 	 				table+='<td style="color:green">}</td><td style="color:red">终审（'+fourtime+'）</td>'+
 					'</table>';
 	 			}else{
 	 				table+='<td>}</td><td>终审</td>'+
 					'</table>';
 	 			}
 			}else{
	 			table+='<table>'+
				'<tr><td><font>受理</font></td>'+
				'<td>-</td>'+
				'<td><font style="color:green">公租房资质审核（'+onetime+'）</font></td>'+
				'<td style="color:green">{&nbsp;</td>'+
				'<td style="color:green">企业审核（'+twotime+'）<br/>人员审核（'+twotime+'）</td>';
	 			if(laststatus==6202){
	 				table+='<td style="color:green">}</td><td style="color:green">终审（'+threetime+'）</td>'+
					'</table>';
	 			}else if(laststatus==6203){
	 				table+='<td style="color:green">}</td><td style="color:red">终审（'+threetime+'）</td>'+
					'</table>';
	 			}else{
	 				table+='<td>}</td><td>终审</td>'+
					'</table>';
	 			}
 			
 			}
 		}
 		$(container).append(table);
 		
 	};
 	tableController.tableConfig.fields.push(field);
	queryClick(1);
});



var currentpage;
function queryClick(pageIndex) {
	$("#form_table").html("");  //清空form_table的div
	currentpage = pageIndex;  //查询页面数据
    var where= createTableConfig.getSelectConditionSql(tableController);//画查询，输入文本框查询需要使用
	if(!where) where+= " where 1=1 and t1.status>0 ";  //判断where语句
	
	var applicant=$("#applicant").val();
	if(applicant&&applicant.length>0){
		where +=" and t1.applicant like '%"+applicant+"%' ";
	}
	var username=$("#username").val();
	if(username&&username.length>0){
		where +=" and t2.userName like '%"+username+"%' ";
	}
	
	var begin=$("#begin").val();
	var end =$("#end").val();
	
	if(begin)
		where += " and (t1.status>102 and Date(getmodify(t1.id,2,1)) > '" + begin+"' or Date(getmodify(t1.id,2,1)) = '"+begin+"')";
	if(end)
		where +=  " and (t1.status>102 and Date(getmodify(t1.id,2,1)) < '"+ end + "' or Date(getmodify(t1.id,2,1)) = '"+end+"')";
	/* 
	if(begin&&begin.length>0){
		if(end&&end.length>0){
			where +=" and t1.status>102  and ((select Date(createDate) from wdit_company_user_approval where approvalStep=1 and requestUser_id=t2.id order by modifyDate LIMIT 0,1)) >= '"+begin+"' and ((select Date(createDate) from wdit_company_user_approval where approvalStep=1 and requestUser_id=t2.id order by modifyDate LIMIT 0,1))<'"+end+"'";	
		}
	} */
	
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
                                <li>审核进度</li>
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
                                
						        </div>
                                    <div class="form-search">
                                        <label>单位名称：</label>
                                        <input type="text" id='applicant' />
                                        <label>个人姓名：</label>
                                        <input type="text" id="username" />
                                        <label>受理时间：</label>
                                        <input type="text" id="begin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />~
                                        <input type="text" id="end" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
                                        <a class="btn btn-info" href="javascript:void(0);" onclick="queryClick(1)">检索</a>
                                        <a class="btn btn-default" onclick="onclear()">清空</a>
                                        <input type="hidden" id="userid" />
                                    </div>
									<div id="form_table">
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