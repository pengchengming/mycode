<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="left-menu">
				<div class="left-menu-title">
					系统管理
				</div>
				<div class="left-menu-main">
					<ul id="navigate_1" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_1');"><div class="first-li-title">系统管理</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/sys/jsp_611.jsp"/>" id="jsp_611">用户管理</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/sys/jsp_612.jsp"/>" id="jsp_612">角色管理</a></li>
					</ul>
					<ul id="navigate_2" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_2');"><div class="first-li-title">系统初始化</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/sys/jsp_621.jsp"/>" id="jsp_621">模块</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/sys/jsp_622.jsp"/>" id="jsp_622">资源</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/sys/jsp_623.jsp"/>" id="jsp_623">操作</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/sys/jsp_624.jsp"/>" id="jsp_624">自定义权限</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/sys/jsp_625.jsp"/>" id="jsp_625">菜单</a></li>
					</ul>
				</div>
</div>