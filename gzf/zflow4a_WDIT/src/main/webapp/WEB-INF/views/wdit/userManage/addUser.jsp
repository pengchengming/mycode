<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>新增人员信息</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>

<script type="text/javascript"
	src="<c:url value="/script/createUI/createTableConfig.js" />"></script>

<script type="text/javascript">
function check(){
	var flag = false;
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
						if(obj.username==username){
							alert("该账号已存在！");
							flag=true;
							return false;
						};
						if(obj.realname==realname){
							alert("该姓名已存在！");
							flag=true;
							return false;
						};
					})
				}		
			}
		});
	 if(flag){
		 return false;
	 }
	 
	 var userobj=new Object();
		userobj.username=$("#username").val();
		userobj.realname=$("#realname").val();
		userobj.tel=$("#tel").val();
		userobj.accountNonExpired=1;
		userobj.accountNonLocked=1;
		userobj.credentialsNonExpired=1;
		userobj.enabled=enable;
		userobj.passwordChanged=0;
	//保存操作
	var jsonString={
			"formId":24,
			"register":userobj
	};
		url =rootPath+'/hmcheck/saveFormDataJson';		
		$.ajax({
			url : url,
			type : "POST", 
	        async: false,
	        data : {
	        	json : JSON.stringify(jsonString)
	        }, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
					if(data.successMsg){
						alert(data.successMsg);
						window.location.href="<c:url value='/hmcheck/userlist' />";
			 		}else{
			 			alert("操作失败");
			 			layer.close(index);
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
                                <li>用户管理</li>
                                <li class="active">新增人员信息</li>
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
</html>