<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>用户设置</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<%-- <script type="text/javascript" src='<c:url value="/script/layer/test.json" />'></script> --%>
<script type="text/javascript" src="<c:url value="/script/createUI/createTableConfig.js" />"></script>
<script type="text/javascript">
var viewCode = 'usersetting_code_v1';
var tableController;
$(function(){
	layer.config({
		skin : 'layer-ext-moon',
		extend : 'skin/moon/style.css'
	});
	
	//dictionaryValueHtml=getDataDictionaryValueHtml(150);
	//$("#companytype").html("<option value=''>请选择</option>"+dictionaryValueHtml);
 	tableController = createTableConfig.initData(viewCode,"search_div","queryClick(1)");//画查询表头查询
   	var field = new Object();
 	field.title = '操作';
 	field.type = 'cutsomerRender';
 	field.doRender = function(data, container, config, rowIndex){
 		var id=data['id'];
 		var code = data['code'];
 		var a1 = document.createElement('a');
 		$(a1).attr('href','javascript:updateCodeForm('+id+',\''+code+'\')').html('&nbsp;修改&nbsp;');
 		var a2 = document.createElement('a');
 		$(a2).attr('href', 'javascript:addNewCodeValue(\''+id+'\')').html('&nbsp;新增明细&nbsp;');
 		
 		$(container).append(a1).append(a2);
 	};
 	
 	///script/layer/test.json  /userSetting/findValByCode
 	//filed0明细table的配置
 	var field0 ={
            title: "明细",
            data: "",
            cdivId: "formExpandProperty",
            url: '<c:url value="/userSetting/findValByCode"/>',
            param: [
                "id"
            ],
            type: "tableLoad",
            details: {
                fields: [
                { 
                    type: 'text',
                    title: '值ID',
                    data: 'id'
                },
                 { type: 'text',
                     title: 'Value',
                     data: 'value'
                 }, {
                     type: 'text',
                     title: '描述',
                     data: 'displayValue'
                 },
                 {
          			title:'操作',
          			type:'cutsomerRender',
          			doRender:function(data, container, config, rowIndex){
          				var id=data['id'];
          		 		var code = data['code'];
          		 		var a1 = document.createElement('a');
          		 		$(a1).attr('href','javascript:updateValueForm('+id+')').html('&nbsp;修改&nbsp;');
          		 		var a2 = document.createElement('a');
          		 		$(a2).attr('href', 'javascript:deleteValue(\''+id+'\')').html('&nbsp;删除&nbsp;');
          		 		$(container).append(a1).append(a2);
          			}
         		 }]
            }};
 	tableController.tableConfig.fields.push(field0);
 	tableController.tableConfig.fields.push(field);
 	
	queryClick(1);
});

var createTable = new createTable();
var currentpage;
function queryClick(pageIndex) {
	$("#form_table").html("");  //清空form_table的div
	currentpage = pageIndex;  //查询页面数据
    var sqlQuery= createTableConfig.getSelectConditionSql(tableController);//画查询，输入文本框查询需要使用
	if(!sqlQuery) sqlQuery+= " where 1=1";  //判断where语句
	var user_query_con = $("#wdit_company_applicant").val();
	sqlQuery += ' and display like  \'%'+user_query_con+'%\'';
	$.post('<c:url value="/createSelect/findselectData"/>', { 
   		code : viewCode,
   		selectConditionSql : sqlQuery,
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

function deleteValue(id){
	debugger;
	var jsonString = {
			 "formId":69,
			 "tableDataId":id	
	};
	$.ajax({
		url : rootPath+'/forms/delteFormDataJson',
		type : "POST", 
        async: false,
        data : {
        	json : JSON.stringify(jsonString)
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.successMsg){
				alert("操作成功！！");
				window.location.reload();
				return true;
	 		}else{
	 			alert("操作失败");
	 			return false;
	 		}
		    }
        });
}
function saveValueForm(codeid){
	console.log('codeid',codeid);
	var saveData = {
			displayValue: $('#dic_value_desc').val(),
			value:$('#dic_code_val').val(),
			dataDictionaryCode_id:codeid
	};
	
	// formID 69 数据字典value表    
	 jsonString={
			   "formId":69,
			   "register":saveData
	  }
	
	//保存到数据库
	 $.ajax({
		url : rootPath+'/forms/saveFormDataJson',
		type : "POST", 
        async: false,
        data : {
        	json : JSON.stringify(jsonString)
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.successMsg){
				alert("操作成功！！");
				window.location.reload();
				return true;
	 		}else{
	 			alert("操作失败");
	 			return false;
	 		}
		    }
        });
	
}

function updateValueForm(id){
	debugger;
	console.log("add new code vlaue-- code id ", id);
	var formTable='<table class="form_table">'+
	'<tr><th>值:   </th><td>';
	
	formTable+='<input type="text" id="dic_code_val"/></td></tr>'+ 
	'<tr><th>值的描述:   </th><td><input type="text" id="dic_value_desc"></td></tr>'
	+'</table>';

	formTable+='<br/><input type="button" class="a_btn_blue" onclick="saveUpdateValueForm(\''+id+'\')" value="保存" >';
	layerindex= layer.open({
		title:"修改Value",
	    type: 10,
	    area: ['450px', '330px'],
	    skin: 'layui-layer-rim', //加上边框
	    content: formTable
	});
}

function saveUpdateValueForm(id){
	var saveData = {
			displayValue: $('#dic_value_desc').val(),
			value:$('#dic_code_val').val()
	}
	
	// formID 69 数据字典value表    
	 jsonString={
			   "formId":69,
			   "tableDataId":id,
			   "register":saveData
	  }
	
	//保存到数据库
	 $.ajax({
		url : rootPath+'/forms/saveFormDataJson',
		type : "POST", 
        async: false,
        data : {
        	json : JSON.stringify(jsonString)
        }, complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.successMsg){
				alert("操作成功！！");
				window.location.reload();
				return true;
	 		}else{
	 			alert("操作失败");
	 			return false;
	 		}
		    }
        });
	
}
function addNewCode(){
	var formTable='<table class="form_table">'+
	'<tr><th>Code:   </th><td>';
	
	formTable+='<input type="text" id="dic_code" onblur="validateUnique(this.value)"/></td></tr>'+ 
	'<tr><th>Code描述:   </th><td><input type="text" id="dic_code_desc"></td></tr>'
	+'</table>';

	formTable+='<br/><input type="button" class="a_btn_blue" onclick="saveCodeForm()" value="保存" >';
	layerindex= layer.open({
		title:"新增Code",
	    type: 10,
	    area: ['450px', '330px'],
	    skin: 'layui-layer-rim', //加上边框
	    content: formTable
	});
}
function addNewCodeValue(codeid){
	console.log("add new code vlaue-- code id ", codeid);
	var formTable='<table class="form_table">'+
	'<tr><th>值:   </th><td>';
	
	formTable+='<input type="text" id="dic_code_val"/></td></tr>'+ 
	'<tr><th>值的描述:   </th><td><input type="text" id="dic_value_desc"></td></tr>'
	+'</table>';

	formTable+='<br/><input type="button" class="a_btn_blue" onclick="saveValueForm(\''+codeid+'\')" value="保存" >';
	layerindex= layer.open({
		title:"新增Code对应的值",
	    type: 10,
	    area: ['450px', '330px'],
	    skin: 'layui-layer-rim', //加上边框
	    content: formTable
	});
}
function updateCodeForm(id,code){
	var formTable='<table class="form_table">'+
	'<tr><th>Code:   </th><td>';
	
	formTable+='<input type="text" id="dic_code" value="'+code+' " disabled /></td></tr>'+ 
	'<tr><th>Code描述:   </th><td><input type="text" id="dic_code_desc"></td></tr>'
	+'</table>';

	formTable+='<br/><input type="button" class="a_btn_blue" onclick="saveCodeForm(true,'+id+')" value="保存" >';
	layerindex= layer.open({
		title:"修改Code",
	    type: 10,
	    area: ['450px', '330px'],
	    skin: 'layui-layer-rim', //加上边框
	    content: formTable
	});
}


function saveCodeForm(isUpdate,id){
	
	var code = $('#dic_code').val();
	var code_desc = $('#dic_code_desc').val();
	var saveData = {
			code: code,
			display:code_desc,
			isupdate:1,
			code_type:'select',
			dataDictionaryType:1
	}
	
	// formID 100 数据字典code表
    var jsonString={
	   "formId":100,
	   "register":saveData
    }
	if(isUpdate){
		jsonString={
			"formId":100,
			"tableDataId":id,
			"register":saveData
				
		}
	}
	
	var valResult = false;
	if(isUpdate == null){
		valResult =  validateUnique(code);
	}
	if(code!="" && valResult || isUpdate){
		//保存到数据库
		 $.ajax({
			url : rootPath+'/forms/saveFormDataJson',
			type : "POST", 
	        async: false,
	        data : {
	        	json : JSON.stringify(jsonString)
	        }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.successMsg){
					alert("操作成功！！");
					window.location.reload();
					return true;
		 		}else{
		 			alert("操作失败");
		 			return false;
		 		}
			    }
	        });
		
	}
}


function validateUnique(code){
	var unique = false;
	//查看code是否已经存在
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"sys_datadictionary_code",
			condition: "code= '" + code + "'"
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			if(results.length ==0) unique = true;
		}
	});
	if(!unique) alert('code已存在，请更换一个！')
	return unique;
}

function getDataDictionaryValueHtml(code){
	   var   dictionaryValueHtml="";
	   $.ajax({
		    url : rootPath+"/dictionarys/dataDictionaryValueList.do",
			type : "POST",
			async:false,
			data:{
				code :code
			},
			complete : function(xhr, textStatus){
				var dictionaryCodeData = eval("("+xhr.responseText+")");
				if(dictionaryCodeData &&dictionaryCodeData .length>0){
					$.each(dictionaryCodeData,function(i,n){ 
						dictionaryValueHtml+='<option value="'+n.id+'">'+ n.displayValue + '</option>';
	        		});
			   }
			}});
	   return dictionaryValueHtml;
}
function delcompany(id){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"global_user",
			condition: "companyId="+id
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			var results = data.results;
			$.each(results,function(i,obj){
				$("#userid").val(obj.id);
			})
		}
	});
	var userid=$("#userid").val();
    if(confirm("确定删除该记录？")){
       var obj=new Object();
       obj.valid=0;
       
       var jsonString={
    		   "formId":58,
    		   "tableDataId":id,
    		   "register":obj
       }
       var jsonString2={
    		   "formId":24,
    		   "tableDataId":userid,
    		   "register":{"enabled":0}
       }
       $.ajax({
			url : rootPath+'/forms/saveFormDataJsons',
			type : "POST", 
	        async: false,
	        data : {
	        	jsons : [JSON.stringify(jsonString),JSON.stringify(jsonString2)]
	        }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.successMsg){
					alert("删除成功！！");
					window.location.reload();
					return true;
		 		}else{
		 			alert("操作失败");
		 			return false;
		 		}
			    }
	        });
    }
	return false;
 }
 
var layerindex;
function uppassword(id,userid){
	var shipDiv="<div><table class='table table-bordered table-hover text-center table-striped'><tr><th>请输入密码：</th><td><input id='password' type='password' /></td></tr>"+
	 "<tr><th>确认密码：</th><td><input id='repassword' type='password' /></td></tr><tr><td colspan='2'><input type='button' class='button_btn_blue' value='确定' onclick='updatepassword("+id+","+userid+")'/> <input type='button'  class='button_btn_blue layui-layer-close layui-layer-close1' value='取消'/></td></tr></table> </div>";
	 layerindex= layer.open({
	        type: 1,
	        area: ['420px', '300px'],
	        skin: 'layui-layer-rim', //加上边框
	        content:shipDiv
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
function onclear(){
	$("#wdit_company_applicant").val("");
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
                                <li>用户设置</li>
                                <li class="active">字典编辑</li>
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
                                            <div class="form-search">
                                                <label>描述搜索：</label>
                                                <input type="text" id='wdit_company_applicant' />
                                                <!-- <label>行业分类：</label> -->
												<!--  <select id="companytype">
                                                </select> -->
                                                <a class="btn btn-info" href="javascript:void(0);" onclick="queryClick(1)">检索</a>
                                                <!-- <a class="btn btn-default" onclick="onclear()">清空</a> -->
                                                <a class="btn btn-default" href="javascript:addNewCode();">新增Code</a>
                                                <input type="hidden" id="userid" />
                                            </div>
                                        	<div class="table-responsive" id="form_table">
                                        		
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