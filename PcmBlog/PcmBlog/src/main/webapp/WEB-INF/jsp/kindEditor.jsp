<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%
request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path;
%>
<script charset="utf-8" src="<%=basePath%>/js/kindeditor-4.1.10/kindeditor.js"></script>
<script charset="utf-8"src="<%=basePath%>/js/kindeditor-4.1.10/lang/zh-CN.js"></script>
<script charset="utf-8" src="<%=basePath%>/js/jquery-1.7.1.js"></script>

<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="content"]', {
				uploadJson : '<%=basePath%>/upload_json.jsp',
				fileManagerJson : '<%=basePath%>/file_manager_json.jsp',
				allowFileManager : true,
				 resizeType:0,
				afterBlur : function(){//编辑器失去焦点时直接同步，可以取到值
                    this.sync();
                },
                items:[
                       'source', '|', 'undo', 'redo', '|', 'preview', 'template', 'code', 'cut', 'copy', 'paste',
                       'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                       'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                       'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                       'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                       'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|',  'multiimage',
                       'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                       'link', 'unlink'
               ]
			});
		});
     function getMessage(){
    	var style= document.getElementById("style").value;
    	var title = document.getElementById("title").value;
    	var content = document.getElementById("content").value;
    	if(title==""){
    	alert("标题不能为空 ");
    	}
    	else if(content==""){
    		alert("内容不能为空！ ");
    	}
    	else{
    		var form = document.forms[0];
    		form.action = "/PcmBlog/article/addArticle";
    		form.method="post";
    		form.submit();
    	}
    		
     }

	</script>
<style type="text/css">

body {
	background-color: #f8f8f8;
}

#head {
	margin-top: 3%;
	margin-left: 5%;
	margin-bottom: 20px;
	margin-left: 10%;

}

#content {
	margin-left: 10%;
	visibility: hidden;
	width: 80%;
	height: 400px;
}
.t_content{
margin-left: 10%;
margin-bottom: 5%;
}
#get {
	float: right;
	margin-right: 20%;
}

#style {
	margin-top: 20px;
}
</style>
</head>
<body>
<div class="top"><jsp:include page="top.jsp" flush="true" /></div>
<form action="" name="addArticle">
<div id="head">标题： <input type="text" name="title" id="title" /><br>
文章分类:<select  name="style" id="style">
	<option>科技类</option>
	<option>文学类</option>
</select></div>

<div class="t_content">
<textarea id="content" name="content" cols="100" rows="19"></textarea> <br />
<input type="button" id="get" name="button" value="提交内容" onclick="getMessage()" />
</div>
</form>
</body>
</html>
