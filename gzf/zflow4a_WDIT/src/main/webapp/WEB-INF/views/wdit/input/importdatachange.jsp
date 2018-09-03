<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/views/wdit/commonCss.jsp"%> 
<html> 
<head>   
<link rel="stylesheet" href="<c:url value="/css/index.css" /> " type="text/css"> 
<link rel="stylesheet" type="text/css" href="<c:url value="/script/jquery/ui/jquery-ui.min.css" />"/>
<script type="text/javascript" src="<c:url value="/script/jquery/jquery-1.11.1.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/script/jquery/ui/jquery-ui.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/script/jquery/jquery.json-2.4.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/script/jquery/jquery-ui-1.8.18.custom.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/script/jquery/jquery.form.js" />"></script>

<script type="text/javascript">
var index = 0;
var intvalid;
var code = '${code}'

var rootPath = "<c:url value="/" />";
$(function () {
  initdiv(code);

})

function initdiv(code){
	var formhtml = '';
	if(code == 'tmp_deco_item'){
	        formhtml += '<form id="upLoadForm" name="upLoadForm" method="post" enctype="multipart/form-data" action='+rootPath+'/uploaddownload/formUpload2>'
	                  +'<input type="file" id="fileUpload" name="file" value="点击选择" /><input name="name" type="hidden" value="tmp_deco_item"/>';     
	}else{
		formhtml += '<form id="upLoadForm" name="upLoadForm" method="post" enctype="multipart/form-data" action='+rootPath+'/uploaddownload/formUpload>';
		if(code == 'tmp_deco_package_item&tmp_deco_package_material' || code == 'tmp_deco_package_switch_category'){
			formhtml += '<select id="code" name="code"></select><div id = "version_div"></div>';}  
		    formhtml += '<input type="file" id="fileUpload" name="file" value="点击选择" />';
		if(code == 'tmp_zflow_form'){
			formhtml += '<input name="name" type="hidden" value="tmp_zflow_form"/>';
		}else if (code == 'tmp_zflow_form_property'){
			formhtml += '<input name="name" type="hidden" value="tmp_zflow_form_property"/>';
		}else if (code == 'tmp_zflow_form_view'){
			formhtml += '<input name="name" type="hidden" value="tmp_zflow_form_view"/>';
		}else if (code == 'tmp_zflow_form_view_property'){
			formhtml += '<input name="name" type="hidden" value="tmp_zflow_form_view_property"/>';
		}else if(code == 'tmp_sys_datadictionary'){
			formhtml += '<input name="name" type="hidden" value="tmp_sys_datadictionary"/>';
		}else if(code == 'tmp_wdit_company'){
        	formhtml += '<input name="name" type="hidden" value="tmp_wdit_company"/>';
        }else if(code == 'tmp_wdit_request_user'){
        	formhtml += '<input name="name" type="hidden" value="tmp_wdit_request_user"/>';
        }
	}
	formhtml += '<input id="batchNo" name="batchNo" type="hidden"/><input id="upload" type="button" value="数据导入" onclick="uploadExcel();"/></form>';                                      
    $("#inportdata_div").append(formhtml);
    
  
}




function uploadExcel() {
	var fileName = $("#fileUpload").val();
    if(null != fileName && 0 < fileName.length){
        var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLocaleLowerCase();
        if("xls" == fileType || "xlsx" == fileType){
        	$("#results_id").empty();
            $("#results_log").show();
            $("#results_log").html("<div style='text-align: center'><img src='"+rootPath+"/css/images/ajax-loading.gif'>&nbsp;加载中......</div>");
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
<body style="background-image:none;">
	<div class="content">

		<div class="main">
			<div class="main_body">

					<div class="container">
						<div class="banner">
							<div class="banner-title"></div>
						</div>
						<div class="container" style="margin-top:50px;background:#fff;">
   <table id="wsd_inputtables">
       <tr>
           <td width="100%" class="tabletitle" style="font-size: 16px; font-weight: bold; text-align: center; padding:10px;">导入${title}数据</td>
       </tr>
       <tr>
           <td class="buttonarea">
               <div id = "inportdata_div" style="text-align:left;">                                    
				上传EXCEL数据文件: 
               </div>
           </td>
       </tr>
  	</table> 
  	<div style="height:420px;width:820px;overflow-y: scroll;margin-bottom:10px;" class="container" >
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
	<div class="clear"></div>
	</div>
	<div class="clear"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="foot">
			<span style="font-family: Verdana; margin-right: 10px;">Version	1.0</span>
		</div>
	</div>
</body>
</html>