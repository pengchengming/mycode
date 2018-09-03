<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>修改人员信息</title>

<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>

<script type="text/javascript" >
	
var formId = '24';//数据库查
var id = '${dataId}';//获取传递
var rootPath = "<c:url value="/" />";

$(function(){
	var oldusername;
	var oldrealname;
	//$("#CostCenter_id").append('<a onclick="show_category(1)" style="margin-left: 20px;">？</a><a onclick="clearinput()">X</a>');
	readdetail();
	
 }); 
 
function clearinput(){
	$("#CostCenter_Names").val("");
}
var category='costcenter_v2';
var layerindex;
//弹层
function show_category(pageIndex){
	var selecthtml = '<div id="search_div"></div>';
	selecthtml += '<div id="list" style=""></div><div id="pager"></div>';
	selecthtml += '<div name="laypage1.3" ><button style="margin-left:550px" class="button_btn_blue layui-layer-close layui-layer-close2"  onclick="addtype()">确定</button></div>'; 
	
	layerindex= layer.open({
	    type: 1,
	    title:'',
	    area : ['820px','530px'],
	    content: selecthtml
	});

	tableController = createTableConfig.initData(category,"search_div","queryClick(1)");
	
	var field = new Object();
    field.title = '';
    field.type = 'checkbox';
    field.data = 'id';
    tableController.tableConfig.fields.splice(0,0,field);
	queryClick(1);
}

var createTable = new createTable();//创建
function queryClick(pageIndex) { 

    var where= createTableConfig.getSelectConditionSql(tableController);//画查询，输入文本框查询需要使用
	if(!where) where+= " where  1=1 ";  //判断where语句

	$.post('<c:url value="/createSelect/findselectData"/>', { 
   		code : category,
   		selectConditionSql : where,
        pageIndex : pageIndex,
    	pageSize : 5
	}, function(data){
			if(data.code + "" == "1"){
			data.results = eval("(" + data.results + ")");
           	data.paged = eval("(" + data.paged + ")");
           	createTable.registTable($('#list'), tableController.tableConfig, data, "queryClick");
           	
        }else{
			$("#list").html("<div style='text-align: center'>没有记录</div>");
        } 
    });
} 
/* 
function check(){
	var userName = '';
	 userName = $("#code_id").val();//人员账号
	var realname = $("#name_id").val();//姓名
	var tel = $("#tel_id").val(); //电话
	var ment = $("#Department_ids").val(); //部门
	var num = 0;//统计是否存在 

	
	
	$.ajax({
		url : '<c:url value="/data/procedure"/>',
		type : "POST", 
	    async: false,
	    data : {
        	p : 'R2009003|'+ userName +'|' +id
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data&&data.length>0){
				var obj = data[0];
				if(obj.valid == 0){
					alert("账号已存在，请重新输入！");
					return;
				}	
				 if(userName != ''){
					   if(realname !=''){
						   if(tel != ''){
							   if(ment !=''){
								   checkde();
							   }else{
								   alert("部门信息不能为空！");
								  return false;
							   }
						   }else{
							   alert("电话不能为空！");
							  return false;
						   }
					   }else{
						   alert("姓名不能为空！");
						  return false;
					   }
				   }else{
					   alert("账号不能为空！");
					  return false;
				   }
			}
		}
      
    })
} */

 //保存操作
 /*
function checkde(){	
	var jsonString={
			"formId":formId,
			"tableDataId":id,
			 "register":{"username":$("#code_id").val(),"realname":$("#name_id").val(),"tel":$("#tel_id").val(),
						"NPR_Department_id":$("#Department_ids").val(),"NPR_CostCenter_names":$("#CostCenter_Names").val(),
						"description":$("#description_id").val(),
						"valid":$('input[name="valid_1n"]:checked').val()
						} 
					
	};
		url =rootPath+'/forms/saveFormDataJson';		
		$.ajax({
			url : url,
			type : "POST", 
	        async: false,
	        data : {
	        	json : JSON.stringify(jsonString)
	        }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
					if(data.successMsg){
						alert("修改"+data.successMsg);
						window.opener.queryClick(1);
						window.close();
				 	}
			    }
	     });
}
 */
 
 
 function check(){
	 var flag=false;
	//字段验证
	 var username=$("#username").val();
	 if(username.length<=0){
		 alert("请输入账户");
		 return;
	 }
	 if(username.length>=255){
		 alert("账户长度超出规范");
		 return;
	 }
	 var realname=$("#realname").val();
	 if(realname.length<=0){
		 alert("请填写姓名");
		 return;
	 }
	 if(realname.length>=255){
		 alert("姓名长度超出规范");
		 return;
	 }
	 var tel=$("#tel").val();
	 if(tel.length<=0){
		 alert("请填写联系方式");
		 return;
	 }
	 if(tel.length>=255){
		 alert("联系方式长度超出规范");
		 return;
	 }
	 
	 var enable=0;
	 if($("#enable").get(0).checked){
		 enable=1;
	 }
//	 alert("enabled"+enable);

	//校验账号、姓名是否重复
		$.ajax({
			url : rootPath+'/forms/getDataByFormId',
			type : "POST",
	        async: false,
			data:{
				formCode:"global_user",
				condition: " id !=1 and companyId is null "
			},
			complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				var results = data.results;
				if(results&&results.length>0){
					$.each(results,function(i,obj){
						if(oldusername!=obj.username){
							if(obj.username==username){
								alert("该账号已存在！");
								flag=true;
								return false;
							};
						}
						if(oldrealname!=obj.realname){
							if(obj.realname==realname){
								alert("该姓名已存在！");
								flag=true;
								return false;
							};
						}
					})
				}		
			}
		});
	 if(flag){
		  return false;
	 }
	 var jsonString={
				"formId":formId,
				"tableDataId":id,
				"register":{"username":$("#username").val(),"realname":$("#realname").val(),"tel":$("#tel").val(),
							"enabled":enable
							}
		};
			url =rootPath+'/forms/saveFormDataJson';		
			$.ajax({
				url : url,
				type : "POST", 
		        async: false,
		        data : {
		        	json : JSON.stringify(jsonString)
		        }, complete : function(xhr, textStatus){
					var data = JSON.parse(xhr.responseText);
						if(data.successMsg){
							alert("修改"+data.successMsg);
							/* window.opener.queryClick(1);
							window.close(); */
							location="userlist";
							
					 	}
				    }
		     });
 }
 
 
//页面input框添值
function readdetail(){
	$.ajax({
		url : '<c:url value="/forms/getDataByFormId"/>',
		type : "POST", 
	    async: false,
	    data : {
	    	formCode:"global_user",
	    	dataId: id

        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			console.log(data);
			//alert(data.results[0].username);
			if(data.results&&data.results.length>0){
				var $obj = data.results[0];
				$("#username").val($obj.username);
				$("#realname").val($obj.realname);
				$("#tel").val($obj.tel);
				oldusername=$obj.username;
				oldrealname=$obj.realname;
				if($obj.enabled==1){//是否有效
			 	  $("#enable").get(0).checked=true;
				}else {
					$("#enable").get(0).checked=false;
				}
			}
		}})
		//查询出下拉框的值并插入到下拉框
	/* 	$.ajax({
		url : '<c:url value="/data/procedure"/>',
		type : "POST", 
	    async: false,
	    data : {
        	p : 'R2007002'
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data&&data.length>0){
			$.each(data,function(i,obj){
				$("#Department_ids").append('<option  value="'+obj.id+'">'+obj.name+'</option>');
			})
		    $("#Department_ids").val(NPR_Department_id);
			}
	}}) */
	
}

/* //判断修改勾选状态
function initchecked(){
	
	var costids = '';
	costids = $("#CostCenter_ids").val();//input框的值
	var costidsarray = new Array();//数组
	var inputs =  $("input[type='checkbox']");//所有选中的input
	
	if(costids != ''){//判断是否为空字符串
		
		if(costids.indexOf(",") > 0 ){//有逗号切割
			
			costidsarray=costids.split(","); //字符分割
			
			for (var i=1;i<inputs.length ;i++ )	{//所有勾选框的数组
				for(var j = 0;j<costidsarray.length ;j++){//input选中的
					if( $(inputs[i]).val()==costidsarray[j]){//比较如果相同
						$(inputs[i]).prop("checked", true);//就修改勾选框的状态
					}
				}
			}
		}else{//如果没有逗号
			for (var i=1;i<inputs.length ;i++ )	{
				if( $(inputs[i]).val()==costids){
					$(inputs[i]).prop("checked", true);//修改勾选框的状态
				}
			}
		}
	}
}
 */
/* function addtype(){
	var names='';//存放成本中心号码
	var costids ='';//存放成本中心ID集合
	
	var inputs = $("input[type='checkbox']:checked");//所有选中的值
	
	for(var i = 1;i<inputs.length+1;i++){
		names += $(inputs[i-1]).parent().parent().children('td').eq(1).html();
		costids += $(inputs[i-1]).val();
		if(i != inputs.length){
			names += ",";
			costids +=  ",";
		}
	}
	$("#CostCenter_Names").val(names);	
	$("#CostCenter_ids").val(costids);
}

 */

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
                                <li>用户管理</li>
                                <li class="active">修改信息</li>
                            </ol>
                        </div>
                    </div>
				
				 <div class="row">
                        <div class="col-sm-12">
                           
                            <div class="panel panel-white">
                                <div class="panel-body no-padding-top">
                                    <div class="row">
                                        <div class="col-sm-12">
                                           	<table class="table">
                                           		<tr>
                                           			<td>账号:</td>
                                           			<td><input type="text" id="username" /></td>
                                           			<td>姓名:</td>
                                           			<td><input type="text" id="realname" /></td>
                                           		</tr>
                                           		
                                           		<tr>
                                           			<td>联系方式:</td>
                                           			<td><input type="text" id="tel" /></td>
                                           			<td>是否有效:</td>
                                           			<td><input type="checkbox" name="valid_1n" id="enable" /></td>
                                           		</tr>
                                           		<tr border="1px" >
                                           			<td  colspan="4" align="center">
                                           				<button class="btn btn-primary" type="button" onclick="check()">保存</button>
                                           			</td>
                                           			
                                           		</tr>
                                           	
                                           	</table>
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
<%-- <body>  

<div class="detail">
	<div class="detail_title">
		<span id="tablehead">查看并修改人员信息</span>
	</div>
	<!--表单表格展示-->
	<div class="detail_main"> 
			<table  class="form_table">
				<tr><td colspan="4">人员基本信息</td></tr>
				<tr><th>账号</th><td><input type="text" id="username"/></td><th>姓名</th><td><input type="text" id="realname"/></td></tr>
				<tr><th>联系方式</th><td><input type="text" id="tel"/></td><th>部门</th><td><input type="text" id="company"/></td></tr>
				<!-- <tr><th>成本中心</th><td id="CostCenter_id"><input type="hidden" id="CostCenter_ids"/><input type="text" disabled="true " id="CostCenter_Names"/></td>
				    <th>是否有效</th><td id = "valid_td">
				    				  <input type="radio" name="valid_1n" value="1" />是
				                      <input type="radio" name="valid_1n"  value="0" />否
				                  </td>
				</tr>
				<tr><th>更新时间</th><td  id="date_id"></td><th>更新人</th><td id="update_id"></td></tr>
				<tr><th>备注</th><td><input type="text" id="description_id"/></td></tr> -->
			</table>
    </div>	
	<div id="click" style="text-align:center;margin-top:-10px;margin-bottom:40px;">	
		<button  class="button_btn_blue" onclick="check();">保存</button>
	</div>  				
</div>
	 <%@ include file="/WEB-INF/views/wdit/foot.jsp"%>
</body> --%>
</html>