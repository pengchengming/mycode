<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css">
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
<script>
$(document).on("pageinit",function(event){
  $(window).on("orientationchange",function(){
    if(window.orientation == 0)
    {
      $("p").text("方向已经变为 portrait ！").css({"background-color":"yellow","font-size":"300%"});
    }
    else
    {
      $("p").text("方向已经变为 landscape ！").css({"background-color":"pink","font-size":"200%"});
    }
  });                   
});
</script>
</head>
<body>

<div data-role="page">
  <div data-role="header">
    <h1>orientationchange 事件</h1>
  </div>

  <div data-role="content">
    <p>请试着旋转您的设备！</p>
    <p><b>注释：</b>您必须使用移动设备或者移动模拟器来查看该事件的效果。</p>
  </div>

  <div data-role="footer">
    <h1>页脚文本</h1>
  </div>
</div> 

</body>
</html>
