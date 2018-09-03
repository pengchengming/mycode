<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/commonCss.jsp"%>
<%@ include file="/page/common/commonJs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
#selectable .ui-selecting {
	background: #FECA40;
}
</style>
<title>文件上传测试</title>
<script type="text/javascript">
	//var URL_PREFIX = location.href.substring(0,4) == "http" ? ".." : "http://192.168.6.105:8080/web_admin";
	var URL_PREFIX = location.href.substring(0, 4) == "http" ? ".."
			: "http://web3q.sunrisecorp.net/imx";

	$(function() {

	/* 	$.msgc.init("UploadTest", URL_PREFIX + "/cometd/service"); */

	});

	$(function() {

		/* $.msgc.handler("UPLOAD_INIT", function(mt, mb) {
			$("#uploadProgress").text("开始上传，总共" + mb + "字节。");
		});

		$.msgc.handler("UPLOAD_ABORTED", function(mt, mb) {
			$("#uploadProgress").text(mb);
		});

		$.msgc.handler("UPLOAD_CANCELLED", function(mt, mb) {
			$("#uploadProgress").text("已取消");
		});

		$.msgc.handler("UPLOAD_PROGRESS", function(mt, mb) {
			$("#uploadProgress").text(mb);
		});

		$.msgc.handler("UPLOAD_REMAINED", function(mt, mb) {
			$("#uploadProgress").text("还需要" + mb + "。");
		});

		$.msgc.handler("UPLOAD_COMPLETED", function(mt, mb) {
			$("#uploadProgress").text("上传成功，总共花费了" + mb + "。");
		});

		$.msgc.handler("UPLOAD_SAVED", function(mt, mb) {
			$("#uploadedFilename").val(mb.name);
		}); */

		var oldFile = "";

		function uploadFile(event) {

			var newFile = $("#imageFile").val();

			//$.msgc.log("uploadFile newFile=" + newFile);

			if (newFile != "" && oldFile != newFile) {
				oldFile = newFile;

				// TODO: 对newFile进行处理，因为发现在IE、Chrome等平台，上传之后，文件名就变成空了
				alert(newFile);
				$.ajaxFileUpload({
					url : Zflow.url.saveImage,
					secureuri : false,
					fileElementId : 'imageFile',
					dataType : 'json',
					success : function(data, status) {
						alert(JSON.stringify(data));
						var data = JSON.stringify(data);
						var src = data.url;
						$("#Filename").attr("src",src);
						
					/* 	$.msgc.log("ajaxFileUpload success data="
								+ JSON.stringify(data) + " status=" + status);
						if (data.file) {
							$.msgc.log("ajaxFileUpload: file " + data.file
									+ " size " + data.size);
						} else {
							$("#uploadProgress").text(data.error);
						} */
					},
					error : function(data, status, e) {
						$.msgc.log("ajaxFileUpload error data="
								+ JSON.stringify(data) + " status=" + status
								+ " e=" + e);
						$("#uploadProgress").text(e);
					}
				});
			}

			if (event) {
				event.preventDefault();
				event.stopPropagation();
			}

			return false;
		}

		$("#buttonUpload").click(uploadFile);
/* 
		$("#fileToUpload").blur(uploadFile);
		$("#fileToUpload").change(uploadFile); */
		//$("#fileToUpload").get(0).onpropertychange = uploadFile;

		//window.setInterval(uploadFile, 500);

	});
</script>
</head>
<body>
	<form name="form" action="" method="POST" enctype="multipart/form-data">
		<input id="imageFile" type="file" size="45" name="imageFile"class="input" /> 
		<button class="button" id="buttonUpload">Upload</button>
		<span id="uploadProgress"></span>
	</form>
	<img id="Filename" src="/upload/imageFile/201204/817b5613b8094349879508a5cd6d486a.png" alt="aa" />
</body>
</html>