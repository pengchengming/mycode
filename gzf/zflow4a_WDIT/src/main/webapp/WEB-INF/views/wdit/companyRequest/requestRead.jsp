<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>需知</title>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%>
<%@ include file="/WEB-INF/views/wdit/commonJs.jsp"%>
 
<script type="text/javascript">

var companyId = '${sessionScope.USER.companyId}';
var requestId='${requestId}';
var i;
$(function(){
// 	var isRequest=false;
// 	$.ajax({
// 		url : rootPath+'/forms/getDataByFormId',
// 		type : "POST",
//         async: false,
// 		data:{
// 			formCode:"wdit_company_request",
// 			condition: "companyId="+companyId
// 		},
// 		complete : function(xhr, textStatus){
// 			var data = JSON.parse(xhr.responseText);
// 			var results = data.results;  
// 			if(results&&results.length>0){
// 				var requestCompany=results[0];
// 				var status=requestCompany.status;//编号 
// 				if(!status||status+""!="0"){
// 					alert("不可重复申请");
// 					isRequest=true;
// 				}
// 			}
// 		}
// 	});
// 	if(!isRequest){
		i=setInterval("showbution()",1000);
// 	}
});
var index=0;
function showbution(){
	if(index==0){
		$("#read_id").html("我已阅读，开始申请");
		$("#read_id").attr("href",rootPath+"/companyRequest/requestCompany?requestId="+requestId);
		clearInterval(i); 
	}else {
		index--;
		$("#read_id").html("我已阅读（"+index+"秒），开始申请");
	}
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
            <div class="main-content" >
                <div class="container">
                    <!-- start: BREADCRUMB -->
                    <div class="row hidden-sm hidden-xs">
                        <div class="col-md-12">
                            <ol class="breadcrumb">
                                 <li>企业用户</li>
                                <li>提交申请</li>
                                <li class="active">申请须知</li>
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
                                            <div class="notice">
                                                <h3 class="text-center">区筹公租房-人才公寓申请须知</h3>
                                                <h4>一、申请条件</h4>
                                                <p>人才公寓由人才所在单位承租，承租单位应当符合以下条件</p>
                                                <p>（一）、工商、税务登记均注册在的企业；机关事宜单位、社会团体等；</p>
                                                <p>（二）、申请之日起前三年内日常经营中未发生不良行为记录。</p>
                                                <p>人才公寓承租单位，实配人员应当符合以下条件</p>
                                                <p>（一）、在本市人均住房建筑面积低于15平方米；</p>
                                                <p>（二）、未享受本市廉租住房、共有产权保障政策；</p>
                                                <p>（三）、持有《上海市居住证》或《上海市临时居住证》，在沪缴纳社会保险金，与本市单位签订二年以上（含二年）劳动合同，且单位同意由单位承租公共租赁住房的。</p>
                                                <h4>二、申请人才公寓需要提交的材料</h4>
                                                <p>（一）、公共租赁住房准入资格申请表（含单位初审意见）； </p>
                                                <p>（二）、单身申请人（申请家庭成员）的身份证复印件； </p>
                                                <p>（三）、单身申请人（申请家庭成员）的本市户籍证明复印件，或《上海市居住证》复印件及相关办理证明； </p>
                                                <p>（四）、申请家庭成员的婚姻状况证明复印件； </p>
                                                <p>（五）、单身申请人（申请家庭成员）拥有本市产权住房的《房地产权证》复印件，承租本市公有住房的《租用居住公房凭证》；其中本市户籍人员，还需提供户籍地住房的相关证明材料； </p>
                                                <p>（六）、持有《上海市居住证》的单身申请人（申请家庭主申请人）的社会保险缴纳证明； </p>
                                                <p>（七）、单身申请人（申请家庭主申请人）的劳动合同；</p> 
                                                <p>（八）、其他相关材料。 </p>
                                                <h4>三、供应标准</h4>
                                                <p>宿舍 ：单身</p>
                                                <p>一居室：单身或家庭</p>
                                                <p>二居室：个单身人员或二人以上家庭</p>
                                                <h4>四、相关注意事项</h4>
                                                <p>（一）、申请人才公寓准入资格的人员，不得同时申请本市其他保障性住房，必须待公租房准入资格审核完成（未通过）后，方可申请本市共有产权保障住房等其他类型保障住房；</p>
                                                <p>咨询电话：021-54820201</p>
                                                <p>公司网址：<a target="_blank" href="http://www.shxhgzf.com/">http://www.shxhgzf.com/</a></p>
                                                <p>微信：hjhzgzf</p>
                                                <div class="text-center">
                                                    <a class="btn btn-info margin-right-10"  href="javascript:void(0)"  id="read_id" >我已阅读，开始申请</a>
                                                    <a   class="btn btn-default" href="<c:url value="/companyRequest/requestList"/>">关闭</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- end: DATE/TIME PICKER PANEL -->
                        </div>
                    </div>
                    </div>
                    <!-- end: PAGE CONTENT-->
                </div>

            </div> 
        </div> 
		<%@ include file="/WEB-INF/views/wdit/foot.jsp"%>
    </div>
</body>
</html>