<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="left-menu">
				<div class="left-menu-title">
					基础数据
				</div>
				<div class="left-menu-main">
					<ul id="navigate_1" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_1');"><div class="first-li-title">服务</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/dm/jsp_110.jsp"/>" id="jsp_110">认证服务类型</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/dm/jsp_120.jsp"/>" id="jsp_120">认证服务器</a></li>
						<%--<li class="second-li"><a href="<c:url value="/page/jsp/dm/jsp_130.jsp"/>" id="jsp_130">Agent Host信息</a></li>--%>
					</ul>
					<ul id="navigate_2" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_2');"><div class="first-li-title">供应商</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/dm/jsp_140.jsp"/>" id="jsp_140">供应商录入</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/dm/jsp_150.jsp"/>" id="jsp_150">供应商维护</a></li>
					</ul>
				</div>
</div>