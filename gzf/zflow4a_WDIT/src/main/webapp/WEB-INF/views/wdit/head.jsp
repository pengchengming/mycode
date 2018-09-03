<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 
<script type="text/javascript">
var userRole = "${sessionScope.ROLES}";
var ROLEALL = '${sessionScope.ROLEALL}';


$(function(){
	$.ajax({
		url : rootPath+'/forms/getDataByFormId',
		type : "POST",
        async: false,
		data:{
			formCode:"sys_datadictionary_value",
			condition: " dataDictionaryCode_id=1031"
		},
		complete : function(xhr, textStatus){
			var data = JSON.parse(xhr.responseText);
			if(data.code + "" == "1" &&data.results.length>0){
				var displayValue=data.results[0].displayValue; 
 				if(displayValue&&displayValue!="")
 					$("#Notice_id").html(displayValue);
			} 
		}
	}); 
});
</script>
 
<!-- start: TOPBAR -->
<div class="topbar navbar navbar-inverse navbar-fixed-top inner" style="height: 36px">
    <!-- start: TOPBAR CONTAINER -->
    <div class="container">
        <div class="navbar-header">
            <a class="sb-toggle-left hidden-md hidden-lg" href="#main-navbar"><i class="iconfont icon-thlist"></i></a>
            <!-- end: LOGO -->
                <h4 class="site-name"> 公租房 并联审批系统</h4>
	    </div>
		<div style="float:right;color:red;" id="Notice_id" ></div>
		<div class="topbar-tools">
				
				<ul class="nav navbar-right hidden-xs">
					<li>欢迎： ${sessionScope.USER.realname}  </li>
					<li><a href="<c:url value="/j_spring_security_logout" />" >[退出登录]</a></li>
				</ul>
				<a class="sb-toggle-right hidden-sm hidden-md hidden-lg" href="#main-navbar"><i class="iconfont icon-plussign"></i></a>
		</div>
    </div>
        <!-- end: TOPBAR CONTAINER -->
</div>
<!-- end: TOPBAR --> 