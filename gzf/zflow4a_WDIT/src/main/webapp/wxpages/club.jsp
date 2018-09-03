<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<link href="css/style.css" rel="stylesheet" type="text/css">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>club-俱乐部</title>
</head>

<body>
	<banner><img src="images/club_banner.jpg" width="100%"></banner>
    <content>
    	<img src="images/club_01.jpg" width="161" height="70">
		<li>市场价值8万的培训课程（40人天）</li>
		<li>网上财税及管理培训咨询（一个工作日内回复）</li>
		<li>专家上门财税健康诊断4小时，不含报告</li>
		<li>以7折优惠价格参加专业培训、主题工作坊、小型论坛，及在线主题讨论等活动</li>
    </content>
<content>
    	<img src="images/club_02.jpg" width="161" height="70">
		<li>市场价值12万的培训课程(60人天）</li>
        <li>网上财税及管理培训咨询（一个工作日内回复）</li>
        <li>专家上门财税健康诊断8小时（2次）</li>
        <li>以6折优惠价格参加专业培训、主题工作坊、小型论坛，在线主题讨论等活动</li>
    </content>
<content>
    	<img src="images/club_03.jpg" width="161" height="70">
		<li>市场价值16万的培训课程（80人天）</li>
        <li>免费电话财税咨询</li>
        <li>网上财税及管理培训咨询（一个工作日内回复）</li>
        <li>专家团上门财税健康诊断（累计24小时）</li>
        <li>主题工作坊，小型论坛,及在线主题活动，另可以参与私董会</li>
        <li>赠送价值2.5 到3万的内训（一天）</li>
        <li>外地参观旅游2人次</li>
    </content>
    <bottom_div>
    <input type="button" class="club_button"  value="加入VIP" onclick="javascripts:signup();">
    <script type="text/javascript">
    	function signup(){
    		window.location="/zflow/wxflows/wxsignup?openid=<%=request.getParameter("openid")%>&activitycode=2014032003";
    	}
    </script>
    
    
    <a href="http://210.14.69.86/wxpages/wx01/club_01.html">会员独享服务</a>
    </bottom_div>
</body>
</html>
