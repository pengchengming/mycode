<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="left-menu">
				<div class="left-menu-title">
					业务管理
				</div>
				<div class="left-menu-main">
					<ul id="navigate_1" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_1');"><div class="first-li-title">设备业务</div></li>
						<li class="second-li" ><a href="<c:url value="/page/jsp/vpn/jsp_312.jsp"/>" id="jsp_312">业务开通</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/dm/jsp_130.jsp"/>" id="jsp_130">Agent Host信息</a></li>
						<li class="second-li" ><a href="<c:url value="/page/jsp/vpn/jsp_310.jsp"/>" id="jsp_310">业务变更</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_327.jsp"/>" id="jsp_327">追加令牌</a></li>
						<%--<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_313.jsp"/>" id="jsp_313">业务绑定</a></li> --%>
						<%-- <li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_311.jsp"/>" id="jsp_311">VNP取消绑定</a></li>--%>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_314.jsp"/>" id="jsp_314">业务暂停与恢复</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_315.jsp"/>" id="jsp_315">业务删除</a></li>
					</ul>
					<ul id="navigate_2" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_2');"><div class="first-li-title">应用业务</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_322.jsp"/>" id="jsp_322">业务开通</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/dm/jsp_130.jsp"/>" id="jsp_130">Agent Host信息</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_323.jsp"/>" id="jsp_323">业务变更</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_327.jsp"/>" id="jsp_327">追加令牌</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_313.jsp"/>" id="jsp_313">业务绑定</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_321.jsp"/>" id="jsp_321">业务取消绑定</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_324.jsp"/>" id="jsp_324">业务暂停与恢复</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_325.jsp"/>" id="jsp_325">业务删除</a></li>
						<!-- 
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_326.jsp"/>" id="jsp_326">分配种子</a></li>
						 -->
					</ul>
					<ul id="navigate_3" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_3');"><div class="first-li-title">令牌管理</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_331.jsp"/>" id="jsp_331">重发令牌</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/vpn/jsp_332.jsp"/>" id="jsp_332">令牌发放记录查询</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/client/jsp_414.jsp"/>" id="jsp_414">授权</a></li>
					</ul>
				</div>
</div>