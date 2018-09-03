<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>用户管理</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

<script type="text/javascript"
	src="<c:url value="/script/createUI/createTableConfig.js" />"></script>

<script type="text/javascript">
	var viewCode = 'userlist_v1';
	var tableController;
	$(function(){
		
	 	tableController = createTableConfig.initData(viewCode,"search_div","queryClick(1)");//画查询表头查询
	   	var field = new Object();
	 	field.title = '操作';
	 	field.type = 'cutsomerRender';
	 	field.doRender = function(data, container, config, rowIndex){
	 		var id = data['id']
	 		
	 		var a = document.createElement('a');
	 		$(a).html('&nbsp;修改&nbsp;');
	 		var b = document.createElement('a');
	 		$(b).html('&nbsp;重置密码&nbsp;');
	 		
	 		$(a).attr('href', "<c:url value='/hmcheck/goUpdateUser?id="+id+"'/>");
	 		$(b).attr('href', "javascript:void(0)").attr("onclick",'show_category('+id+')');
	 		$(container).append(a);
	 		$(container).append(b);
	 	};

	 	tableController.tableConfig.fields.push(field);

		queryClick(1);
	});
	
	
	

	var createTable = new createTable();
	var currentpage;
	function queryClick(pageIndex) {
		$("#form_table").html("");  
		var where= createTableConfig.getSelectConditionSql(tableController);//画查询，输入文本框查询需要使用
		if(!where) where+= " where 1 =1 and t1.id !=1 and t1.companyId is null "
		
		var username=$("#wdit_global_user_username").val();//用户账号
		var realname=$("#wdit_global_user_realname").val();//用户姓名
		var user_role=$("#user_role").val(); 			   //用户角色
		var user_enabled=$("#user_enabled").val();
		if(username&&$.trim(username)){
			where+=" and username like '%"+username+"%'";
		}
		if(realname&&$.trim(realname)){
			where+=" and realName like '%"+realname+"%'";
		}
		if(user_role&&$.trim(user_role)){
			where += " and t3.description ='" + user_role + "'";
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
	
	

	//弹层
	function show_category(user_id){
		var divhtml = '<div></br></br></br></br>';
		    divhtml	+='<div class="detail_main">&nbsp&nbsp&nbsp新密码   &nbsp&nbsp&nbsp<input type="text" style="width:150px; height:25px;" id="password"/></br></br>&nbsp&nbsp&nbsp确认密码&nbsp<input type="text" style="width:150px; height:25px;" id="repassword" /><div>';
		    divhtml += '</br></br></br>';
		    divhtml += '</br><div style="text-align:center;"> <button   class="button_btn_blue" id="bt_save" onclick="updatepassword(0,'+user_id+')" >保存</button> </div></br></div>' 
		     layerindex= layer.open({
		  	    type: 1,
		  	    area: ['350px', '300px'],
		  	    skin: 'layui-layer-rim', //加上边框
		  	    title :'密码重置',
		  	    content: divhtml
		  	});
		    
		    

		    
		    
	}
	
    function updatepassword(id,userid){
    	
    	if($("#password").val()!=$("#repassword").val()){
    		alert("两次密码不一致，请重新确认");
    		return false;
    	}
    	
    	 var jsonString={
     		   "formId":24,
     		   "tableDataId":userid,
     		   "register":{"password":$("#repassword").val()}
        } 
      
    	$.ajax({
    		 url : rootPath+'/talentoffice/saveFormDataJson1', 
    		type : "POST", 
            async: false,
            data : {
            	password:$("#repassword").val(),
            	json : JSON.stringify(jsonString)
            }, complete : function(xhr, textStatus){
    			var data = JSON.parse(xhr.responseText);
    			console.log(data);
    			if(data.successMsg){
    				alert("修改成功！！");
    				window.location.reload();
    				return true;
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
		<div class="main-container inner">
			<div class="main-content" style="min-height: 542px;">
				<div class="container">
						<!-- breadcrumb导航 -->
					  <div class="row hidden-sm hidden-xs">
                        <div class="col-md-12">
                            <ol class="breadcrumb">
                                <li class="active">用户管理</li>
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
                                                <label>账号：</label>
                                                <input   type="text" id='wdit_global_user_username' width="250" />
                                                <label>姓名：</label>
                                                <input   type="text" id='wdit_global_user_realname' width="250" />
                                                <label>角色：</label>
												 <select id="user_role" style="width: 140px;">
												   <option selected="selected" value="">全部</option>
												   <!-- <option value="企业">企业</option>	 -->
												   <option value="房管局">房管局</option>
												   <option value="商委">商委</option>
												   <option value="人社局">人社局</option>
												   <option value="人才办">人才办</option>
												   <option value="管理员">管理员</option>
												   <option value="经委">经委</option>
												   <option value="科委">科委</option>		  
                                                </select>
                                               	
                                               	<label>是否有效：</label>
                                                <select id="user_enabled" style="width: 140px;">
												   <option selected="selected" value="">请选择</option>
												   <option value="是">是</option>
												   <option value="否">否</option>		
                                                </select>
                                                <a class="btn btn-info" href="javascript:void(0);" onclick="queryClick(1)">检索</a>
                                               <!--  <a class="btn btn-default" href="javascript:void(0);" onclick= "enpty_condition()">清空</a> -->
                                                <a class="btn btn-default"href="<c:url value="/hmcheck/addUser" />">新增</a>
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