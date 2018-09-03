<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%
    request.setCharacterEncoding("UTF-8");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.top {
	width: 80%;
	height: 10%;
	margin-left: 10%;
	margin-right: 10%;
}
</style>
<script charset="utf-8" src="<%=basePath%>/js/jquery-1.7.1.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#upload").load(function(){
		
		var win = document.getElementById('upload').contentWindow;
		var text=win.document.body.innerText;//获取到文本 
		var obj = eval ("(" + text + ")");

		if("yellow"==obj.result[0].race){
			var f_race ="人种：黄种人 "+"<br/>";
		}else if("white"==obj.result[0].race){
			var f_race ="人种：白种人  "+"<br/>";
		}else if("black"==obj.result[0].race){
			var f_race ="人种：黑种人   "+"<br/>";
		}else{
			var f_race ="人种：阿拉伯人  "+"<br/>";
		}
		
		if("0"==obj.result[0].expression){
			var f_expression = "表情：不笑 <br/> ";
		}else if("1"==obj.result[0].expression){
			var f_expression = "表情：微笑  <br/> ";
		}else{
			var f_expression = "表情：大笑  <br/> ";
		}
		
		if("0"==obj.result[0].glasses){
			var f_glasses = "是否带眼镜 ：不带眼镜  <br/> ";
		}else if("1"==obj.result[0].glasses){
			var f_glasses = "是否带眼镜 ：带眼镜  <br/> ";
		}else{
			var f_glasses = "是否带眼镜 ：墨镜   <br/> ";
		}
		
	    var f_beauty="魅力值："+Math.ceil(obj.result[0].beauty)+"<br/>";
	    var f_age="年龄 ："+parseInt(obj.result[0].age)+"<br/>";
	 
	    if(obj.result[0].gender == "male"){
	    	 var f_gender="性别 ：男  "+"<br/>";
	    }else{
	    	 var f_gender="性别 ：女   "+"<br/>";
	    }
		   
	    $("#show").append(f_race+f_beauty+f_age+f_gender+f_expression+f_glasses);
	    
	    $("#show").append("你的脸型是（数值越大你脸型的可能性越大 ）：<br/>");
	    $("#show").append("方脸："+obj.result[0].faceshape[0].probability+"<br/>");
	    $("#show").append("瓜子脸："+obj.result[0].faceshape[1].probability+"<br/>");
	    $("#show").append("椭圆脸："+obj.result[0].faceshape[2].probability+"<br/>");
	    $("#show").append("心形脸 ："+obj.result[0].faceshape[3].probability+"<br/>");
	    $("#show").append("圆脸："+obj.result[0].faceshape[4].probability+"<br/>");



		win.document.body.innerText=""; //重新传值;
		});
	
});
function changImg(e){
	document.getElementById("show").innerHTML="";
    for (var i = 0; i < e.target.files.length; i++) {  
        var file = e.target.files.item(i);  
       // if (!(/^image\/.*$/i.test(file.type))) {  
       //     continue; //不是图片 就跳出这一次循环  
       // }  
        //实例化FileReader API  
        var freader = new FileReader();  
        freader.readAsDataURL(file);  
        freader.onload = function(e) {  
            $("#myImg").attr("src",e.target.result);  
        };
    }
    
}

</script>
</head>
<body>
<div class="top"><jsp:include page="top.jsp" flush="true" /></div>
<div style="margin-left: 35%;">
<form id="form" action="<%=basePath%>/getFace" enctype="multipart/form-data" method="post" target="upload">
<img alt="暂无图片" style="width: 240px;height: 300px;" id="myImg"src="" ><br>
<!--   <input type="file" width="140px" name="file" accept="image/*" onchange="changImg(event)">-->
<input type="file" width="140px" name="file"  onchange="changImg(event)">
<input type="submit" id="submit" value="检测">
 </form>  
 <div id="show"></div>
 </div>
<iframe name="upload" id="upload" style="display: none;"></iframe> 

</body>  
</html>