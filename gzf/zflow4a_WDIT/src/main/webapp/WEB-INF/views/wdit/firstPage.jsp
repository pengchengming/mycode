<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>修改密码</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#password_id").val("");
})
	function savePasswordFun(){
		var password= $("#password_id").val();
		var comPassword=$("#comPassword_id").val();
		if(!password){
			alert("请填写新密码");
			return false;
		}else if(!comPassword){
			alert("请填写确认新密码");
			return false;
		}else if(password=="Gzf@2017_XuHui"){
			alert("新密码和初始密码不能一样");
			return false;
		}else if(password!=comPassword){
			alert("新密码和确认新密码不一致");
			return false;
		}
		$.ajax({
			url : rootPath+'/user/updatePassword',
			type : "POST", 
	      	async: false,
	      	data : {
	      		password : password
	      	}, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.successMsg){
					alert(data.successMsg);
					window.location.href=rootPath+"/wditIndex";
			 	}
			}
	      });
	}
</script>
</head>

<body class="horizontal-menu-fixed">
    <div class="main-wrapper">
		<%@ include file="head.jsp"%>
        <!-- start: MAIN CONTAINER -->
        <div class="main-container inner">
            <!-- start: PAGE -->
            <div class="main-content">
                <div class="container">
                    <!-- start: BREADCRUMB -->
                    <div class="row hidden-sm hidden-xs">
                        <div class="col-md-12">
                            <ol class="breadcrumb">
                                <li></li>
                                <li class="active"></li>
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
                                     	<div role="form" class="form-horizontal">
		                                        <h3 class="text-center padding-vertical-20">修改密码</h3>
		                                        <div class="row">
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="password_id">
		                                                    	<span style="color:#FF0000;">*</span>新密码：
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="password" placeholder="必填" id="password_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                       </div>
		                                       <div class="row">                 
		                                             <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="comPassword_id">
		                                                    	<span style="color:#FF0000;">*</span> 确认新密码:
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="password" placeholder="必填" id="comPassword_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div> 
		                                       <div class="row">
		                                       	 	<div class="col-sm-6">
		                                       	 	 <a href='javascript:void(0)' class="btn btn-info margin-right-10" onclick="savePasswordFun()">确定</a>	
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
        <%@ include file="foot.jsp"%>
    </div>
</body>
</html>
