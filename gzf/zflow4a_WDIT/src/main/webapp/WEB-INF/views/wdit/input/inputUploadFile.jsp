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
		formhtml += '<form id="upLoadForm" name="upLoadForm" method="post" enctype="multipart/form-data" action="'+rootPath+'/uploadFile">';
		formhtml += '<input type="file" id="fileUpload" name="file" value="点击选择" /> <input  name="filePath" > '; 
		formhtml += '<input id="upload" type="button" value="上传页面" onclick="uploadExcel();"/></form>';                                      
    $("#inportdata_div").append(formhtml);
}




function uploadExcel() {
	var fileName = $("#fileUpload").val();
    if(null != fileName && 0 < fileName.length){
        var fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLocaleLowerCase();
        if("jsp" == fileType ||"js" == fileType){
            $("#upLoadForm").ajaxSubmit({
                dataType : "json",
                success: function(data){
                    if(data.code == '1'){
                        alert("上传成功"); 
                    }else
                    	alert("上传失败");
                }
            }); 
        }else{
			alert("Please Upload .jsp or .js files!");
            $("#fileUpload").val("");
        }
    }
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
						           <td width="100%" class="tabletitle" style="font-size: 16px; font-weight: bold; text-align: center; padding:10px;">上传页面</td>
						       </tr>
						       <tr>
						           <td class="buttonarea">
						               <div id = "inportdata_div" style="text-align:left;">                                    
										上传页面: 
						               </div>
						           </td>
						       </tr>
						  	</table>  
							<div class="clear"></div>
						</div> 
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