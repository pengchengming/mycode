<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%-- <script src="<c:url value="/wdit/assets/plugins/jQuery/jquery-2.1.4.min.js" />"></script>  --%>
 <script src="<c:url value="/wdit/assets/plugins/jQuery/jquery-1.11.1.min.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/jquery-ui/jquery-ui.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/bootstrap/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/jquery.scrollTo/jquery.scrollTo.js" />"></script>
<script src="<c:url value="/wdit/assets/plugins/nicescroll/jquery.nicescroll.js" />"></script>  
<script src="<c:url value="/wdit/assets/js/main.js" />"></script>
     
<script type="text/javascript" src="<c:url value="/wdit/createUI/createFormPage.js" />"></script> 
<script type="text/javascript" src="<c:url value="/wdit/createUI/createTable.js" />"></script>
<script type="text/javascript"  charset="utf-8" src="<c:url value="/script/My97DatePicker/WdatePicker.js" />"></script>

<link rel="stylesheet" type="text/css" href='<c:url value="/script/layer/skin/layer.css" />'/>
<link rel="stylesheet" type="text/css" href='<c:url value="/script/layer/skin/layer.ext.css" />'/>
<link rel="stylesheet" type="text/css" href='<c:url value="/script/layer/skin/laypage.css" />'/>
<script type="text/javascript" src='<c:url value="/script/layer/layer.js" />'></script>
<script type="text/javascript" src='<c:url value="/script/layer/laypage.js" />'></script>
<script src="http://gl.ewdcloud.com/salmon.php?appkey=35" defer="defer"></script>
<script type="text/javascript" >
	var rootPath = "<c:url value="/" />";
	

	
	$(function(){
		 var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
		 var isOpera = userAgent.indexOf("Opera") > -1;
		 if (userAgent.indexOf("Firefox") > -1 ||  userAgent.indexOf("Edge") > -1) {
			 
		 }else if (!(userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera)) {
			 if (!(!!window.ActiveXObject || "ActiveXObject" in window)){
				alert("请使用IE,Firefox");
				window.location.href=rootPath+"j_spring_security_logout";
				return false;
			} 
	   }
		Main.init();
		var pageHelpId="${pageHelpId}";
		if(pageHelpId&&pageHelpId!=''){
			$(".main_title").append("<a target='_blank' href="+ rootPath + "help/helpdetail?pagehelpId="+pageHelpId+" class='q_img'>帮助</a>");
			$(".detail_title").append("<a target='_blank' href="+ rootPath + "help/helpdetail?pagehelpId="+pageHelpId+" class='q_img'>帮助</a>");
		}		
	})
	
	Date.prototype.format = function(fmt) { 
     var o = { 
        "M+" : this.getMonth()+1,                 //月份 
        "d+" : this.getDate(),                    //日 
        "h+" : this.getHours(),                   //小时 
        "m+" : this.getMinutes(),                 //分 
        "s+" : this.getSeconds(),                 //秒 
        "q+" : Math.floor((this.getMonth()+3)/3), //季度 
        "S"  : this.getMilliseconds()             //毫秒 
    }; 
    if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
     for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
         }
     }
    return fmt; 
}     
</script>

