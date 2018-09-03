<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/views/commonCss.jsp" %>
<%@ include file="/WEB-INF/views/commonJs4Dragable.jsp" %>
<html> 
<head>   
<link rel="stylesheet" href="<c:url value="/css/index.css" /> " type="text/css"> 
<script type="text/javascript">
var index = 0;
var intvalid;
$(function(){

});

function uploadExcel() {
	var fileName = $("#fileUpload").val();
    if(null != fileName && 0 < fileName.length){
        var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLocaleLowerCase();
        if("xls" == fileType || "xlsx" == fileType){
        	$("#results_id").empty();
            $("#results_log").show();
            $("#results_log").html("<div style='text-align: center'><img src='../css/images/ajax-loading.gif'>&nbsp;加载中......</div>");
            $.post(
                    '<c:url value="/uploaddownload/getBatchNo"/>',
                    {code : "gen_batch_number"},
                    function(response){
//                         var data = $.parseJSON(rsp);
						var state = response["code"]; 
                        if(1 == state){ 
                            $("#batchNo").val(response.batchNo);
                            
                            showLog(response.batchNo);

                            $("#upLoadForm").ajaxSubmit({
                                dataType : "json",
                                success: function(data){
//                                     if(data.code == '0'){
//                                         alert(data.massage);
//                                         clearInterval(intvalid);
//                                     }
                                }
                            });
                            
                        }else if(0 == state){
                        	alert(response["message"]);
                        }
                    }
             );   
        }else{
			alert("Please Upload .xls or .xlsx files!");
            $("#fileUpload").val("");
        }
    }
}
function showLog(batchNo) {
    intvalid = setInterval(function () { postConflictcheck(batchNo); }, 1000);
}
function postConflictcheck(batchNo) {
	//先禁用数据导入按钮,添加disabled属性
	$("#upload").attr("disabled","true");
	  
    $.ajax({
        type : "POST",
         url : '<c:url value="/importLog/getByParam"/>',
       async : false,
        data : {
            id : index,
            batchNo : $("#batchNo").val(),
            sdate : new Date().toTimeString()
        },
        success : function(rsp){
//             var data = $.evalJSON(rsp);
//             var code = data.code;
            if(1 == rsp["code"]){
	            var results = rsp["results"];
	            if(undefined != results && null != results && 0 < results.length){
	                $.each(results, function(i, item){
	                    if(item.type == 2){
	                    	$("#results_id").append("<li> 错误：" + item.description + "</li>");
	                    }else{
	                        $("#results_id").append("<li>" + item.description + "</li>");
	                    }
	                    if (item.type == 4) {
	                        clearInterval(intvalid);
	                        $("#results_log").hide();
	                        //任务结束后启用导入数据按钮，移除disabled属性
	                        $("#upload").removeAttr("disabled"); 
	                    }
	                });
	                index = results[results.length - 1].id;
	            }
        }else if(0 == rep["code"]){
        	alert(rep["message"]);
        }
      }
    });
}

</script>
</head>
<body style="background:none;padding-top:20px;">
<div>
   <table id="wsd_inputtables">
       <tr>
           <td class="buttonarea">
               <div style="text-align:left;">                                    
				上传EXCEL数据文件: 
				    <form id="upLoadForm" name="upLoadForm" method="post" enctype="multipart/form-data" action='<c:url value="/uploaddownload/formUpload"/>'>
	                   <input class="width120" type="file" id="fileUpload" name="file" /> 
	                   <input name="name" type="hidden" value="${param.name}"/>
	                   <input id="batchNo" name="batchNo" type="hidden"/>                                    
	                   <input id="upload" type="button" value="数据导入" onclick="uploadExcel();"/>
				    </form>
               </div>
           </td>
       </tr>
  	</table> 
  	<div style="height:420px;width:820px;overflow-y: scroll;margin-top:10px;margin-bottom:10px;">
    <table  style="background: none repeat scroll 0 0 #F7FBFE; border: 1px solid #A0B7C5; width:800px; cellpadding:0; cellspacing:0;" >
	     <tr>
	         <td class="form_table_tit"> 导入结果 </td>
	     </tr> 
	     <tr>
	         <td>
	             <div id="results_log"></div>
	             <ul id="results_id"></ul>
	         </td>
	     </tr>  
 	</table>
 	</div>
</div>
</body>
</html>