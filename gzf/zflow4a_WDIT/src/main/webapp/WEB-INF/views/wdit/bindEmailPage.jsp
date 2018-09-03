<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>绑定邮箱</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
<script type="text/javascript">
$(function(){
	$("#email_id").val("");
})
	function saveEmail(){
		var email= $("#email_id").val();
		var comEmail=$("#comemail_id").val();
		if(!email){
			alert("请填写邮箱");
			return false;
		}else if(!comEmail){
			alert("请填写确认邮箱");
			return false;
		}else if(email!=comEmail){
			alert("邮箱和确认邮箱不一致");
			return false;
		}
		if(!isEmail(email)){
			 alert("邮件格式不正确!");
			 return false;
		}

		$.ajax({
			url : rootPath+'/user/bindsendEmail',
			type : "POST", 
	      	async: false,
	      	data : {
	      		email : email
	      	}, complete : function(xhr, textStatus){
				var data = JSON.parse(xhr.responseText);
				if(data.successMsg){
					alert("发送成功");
                    mailSite(email);
			 	}else{
			 		alert("发送失败");
			 	}
			}
	      });
	}
	
	
function isEmail(str){
	var reg = /^((([A-Z|a-z|0-9_\\.-]+)@([0-9|A-Z|a-z\.-]+)\.([A-Z|a-z\.]{2,6}\;))*(([A-Z|a-z|0-9_\\.-]+)@([0-9|A-Z|a-z\.-]+)\.([A-Z|a-z\.]{2,6})))$/;
    if(!reg.test(str)){
         return false;
     }
     return true;
}
	
function mailSite(url) {  //参数为用户输入的邮箱地址

    //用正则选出@及之前的内容
    var reg = /\w+@/g;

    //邮箱品牌及其地址，这里我只用这三家举例
    var arr = ["gmail", "qq", "163","bizduo","126"];
    var site = ["mail.google.com", "mail.qq.com", "mail.163.com", "mail.bizduo.com", "mail.126.com"];

    //把@及之前的部分全部删掉
    var add = url.replace(reg, "");

    //判断：如果是数组中的邮箱品牌的话，则打开其相对应的网址，否则显示<p>标签的提示
    for (var i = 0; i < arr.length; i++) {
        if (add.indexOf(arr[i]) !== -1) {
            window.location.href = "http://" + site[i];
        }
    }
}
	
	
function returnlogin(){
	window.location.href=rootPath+"/wditLogin";
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
		                                        <h3 class="text-center padding-vertical-20">绑定邮箱</h3>
		                                        <div class="row">
		                                            <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="email_id">
		                                                    	<span style="color:#FF0000;">*</span>输入邮箱：
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="email_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                       </div>
		                                       <div class="row">                 
		                                             <div class="col-sm-6">
		                                                <div class="form-group">
		                                                    <label class="col-sm-2 control-label" for="comemail_id">
		                                                    	<span style="color:#FF0000;">*</span> 再次输入邮箱:
		                                                    </label>
		                                                    <div class="col-sm-9">
		                                                        <input type="text" placeholder="必填" id="comemail_id" class="form-control">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                        </div> 
		                                       <div class="row">
		                                       	 	<div class="col-sm-6">
		                                       	 	 <a href='javascript:void(0)' class="btn btn-info margin-right-10" onclick="saveEmail()">确定</a>
		                                       	 	 <a href='javascript:void(0)' class="btn btn-info margin-right-10" onclick="returnlogin()">取消</a>	
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
