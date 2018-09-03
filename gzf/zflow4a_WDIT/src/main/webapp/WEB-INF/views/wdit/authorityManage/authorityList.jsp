<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>权限管理</title>


<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>

<script type="text/javascript">
var viewCode = 'authoritylist_v1';
var tableController;

$(function(){
	
 	tableController = createTableConfig.initData(viewCode,"search_div","queryClick(1)");//画查询表头查询
   	var field = new Object();
 	field.title = '操作';
 	field.type = 'cutsomerRender';
 	field.doRender = function(data, container, config, rowIndex){
 		var id=data["id"];
 		var a = document.createElement('a');
 		$(a).html('&nbsp;设置角色&nbsp;');
 		
 		
 		/* if (statusname != '待审核')
 			$(a).html('&nbsp;查看1&nbsp;'); */
 		//$(a).attr('href', rootPath + 'hmcheck/companyapproval?requreid='+requreid+'&companystutus='+companystutus);
 		$(a).attr('href', "javascript:void(0)").attr("onClick","roleFun("+id+")");
 		
 		$(container).append(a);
 		
 	};

 	tableController.tableConfig.fields.push(field);

	queryClick(1);
 	$("tbody tr td:nth-child(5)").html("是");
});

function roleFun(userId){
	//alert(userId);
	$.post( 
    		'<c:url value="/role/showRoleByUser.do" />',
            {
    			userId:userId
            },
            function (data) {
            	if(data&&data.length>0){
            		var roleTable='<table  class="list_table">'+
            		'<tr><th></th><th>序号</th><th>角色编号</th><th>描述</th></tr>';
            		$.each(data,function(i,temp){
            			roleTable+='<tr><td><input name="roleUser" value="'+temp.role.id+'" type="checkbox"';
            			if(temp.checked){
            				roleTable+='checked="'+temp.checked+'"';
            			}
            			roleTable+=' ></td><td>'+(i+1)+'</td><td>'+temp.role.name+'</td><td>'+temp.role.description+'</td></tr>';
            		});
            		roleTable+='</tr></table><br/><input type="button" class="a_btn_blue" onclick="saveUserRole('+userId+')" value="保存" >';
            		
            		layerindex= layer.open({
            	        type: 1,
            	        area: ['520px', '540px'],
            	        skin: 'layui-layer-rim', //加上边框
            	        content: roleTable
            	    });
            	}
            }); 
}
function saveUserRole(id){
	var roles=new  Array();
	$("[name='roleUser']:checkbox").each(function(){
		if(this.checked==true){
			roles.push($(this).val());
		}
	});
	$.ajax({
		url : '<c:url value="/user/updateUserRole.do" />',
		type : "POST",
		data:{
			userId:id,
			'ids[]':roles
		},	
		complete : function(xhr, textStatus) {
			var msg = JSON.parse(xhr.responseText);
			if(msg.errorMsg){
				alert(msg.errorMsg);
			} else if(msg.successMsg){
				alert(msg.successMsg);
				layer.close(layerindex);
				queryClick(1);
			}
		}
	});
	
}

var createTable = new createTable();
var currentpage;
function queryClick(pageIndex) {
	$("#form_table").html("");  
	currentpage = pageIndex; 
	var where= createTableConfig.getSelectConditionSql(tableController);//画查询，输入文本框查询需要使用
	where += " where t1.id!=1 and t1.companyId is null "
	
	var username=$("#user_username").val();//用户账号
	var realname=$("#user_realname").val();//用户姓名
	var user_role=$("#user_rolename").val();//用户角色名
	var user_company=$("#user_company").val();//用户部门
	var user_enabled=$("#user_enabled").val();//是否有效
	if(username&&$.trim(username)){
		where+="and t1.username like '%"+username+"%'";
	}
	if(realname&&$.trim(realname)){
		where+="and t1.realname like '%"+realname+"%'";
	}
	if(user_role&&$.trim(user_role)){
		where += "and t4.description like '%"+user_role+"%'";
	}
	if(user_company&&$.trim(user_company)){
		where +="and t2.applicant  ='"+user_company+"'";
	}
	  if(user_enabled&&$.trim(user_enabled)){
		 
		 if(user_enabled=="是"){
			user_enabled=1;
		}else{
			user_enabled=0;
		}
		 where+="and t1.enabled ='"+user_enabled+"'";
	}  
	
	
	
	
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


function enpty_condition(){
	$("#wdit_global_user_username").val('');
	$("#wdit_global_user_realname").val('');
	$("#user_role").val(101);
	
}












</script>



</head>
<body class="horizontal-menu-fixed">
	<div class="main-wrapper">
		<%@ include file="/WEB-INF/views/wdit/head.jsp"%>
		<%@ include file="/WEB-INF/views/wdit/leftMenu.jsp"%>
		<div class="main-container inner">
			<div class="main-content" style="min-height: 542px;">
				<div class="container">
						<!-- breadcrumb导航 -->
					  <div class="row hidden-sm hidden-xs">
                        <div class="col-md-12">
                            <ol class="breadcrumb">
                                <li class="active">权限管理</li>
                            </ol>
                        </div>
                    </div>
				
				 <div class="row">
                        <div class="col-sm-12">
                           
                            <div class="panel panel-white">
                                <div class="panel-body no-padding-top">
                                    <div class="row">
                                        <div class="col-sm-12">
                                           <div class="form-search">
                                                <label>人员账号：</label>
                                                <input   type="text" id='user_username' width="250" />
                                                <label>姓名：</label>
                                                <input   type="text" id='user_realname' width="250" />
                                                <label>角色：</label>
                                                <!-- <input   type="text" id='user_rolename' width="250" /> -->
                                                <select id="user_rolename" style="width: 140px;">
												   <option selected="selected" value="">全部</option>
												  <!--  <option value="企业">企业</option>	 -->
												   <option value="房管局">房管局</option>
												   <option value="商委">商委</option>
												   <option value="人社局">人社局</option>
												   <option value="人才办">人才办</option>
												  <option value="管理员">管理员</option>
												   <option value="经委">经委</option>
												   <option value="科委">科委</option>		  
                                                </select>
                                                <!-- <label>公司：</label>
                                                 <select id="user_company" style="width: 140px;">
												   <option selected="selected" value="">全部</option>
												   <option></option>		  
                                                </select> -->
                                                <!-- <label>是否有效：</label>
                                                <select id="user_enabled" style="width: 140px;">
												   <option selected="selected" value="">请选择</option>
												   <option value="是">是</option>
												   <option value="否">否</option>		
                                                </select> -->
                                                <a class="btn btn-info" href="javascript:void(0);" onclick="queryClick(1)">查询</a>
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
		





	</div>

	<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>

</body>

</html>