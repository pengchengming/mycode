<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="left-menu">
				<div class="left-menu-title">
					客户管理
				</div>
				<div class="left-menu-main">
					<ul id="navigate_1" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_1');"><div class="first-li-title">用户管理</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/client/jsp_411.jsp"/>" id="jsp_411">企业信息维护</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/client/jsp_413.jsp"/>" id="jsp_413">账号管理</a></li>
					</ul>
					<ul id="navigate_2" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_2');"><div class="first-li-title">个人用户</div></li>
						<li class="second-li" onclick="showCurrent();"><a href="<c:url value="/page/jsp/client/jsp_421.jsp"/>" id="jsp_421">个人信息维护</a></li>
						<!-- 
						<li class="second-li" onclick="showCurrent();"><a href="<c:url value="/page/jsp/client/jsp_423.jsp"/>">账号管理</a></li>
						 -->
					</ul>
				</div>
</div>