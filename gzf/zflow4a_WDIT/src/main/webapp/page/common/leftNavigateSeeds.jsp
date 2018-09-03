<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="left-menu">
				<div class="left-menu-title">
					后台管理
				</div>
				<div class="left-menu-main">
					<ul id="navigate_1" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_1');"><div class="first-li-title">种子管理</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_217.jsp"/>" id="jsp_217">种子采购</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_215.jsp"/>" id="jsp_215">种子导入</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_218.jsp"/>" id="jsp_218">种子分配</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_214.jsp"/>" id="jsp_214">种子维护</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_212.jsp"/>" id="jsp_212">种子库存</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_213.jsp"/>" id="jsp_213">业务绑定</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_211.jsp"/>" id="jsp_211">种子查询</a></li>
					</ul>
					<ul id="navigate_2" class="left-navi" >
						<li class="first-li" onclick="showOrhed('navigate_2');"><div class="first-li-title">日志管理</div></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_220.jsp"/>" id="jsp_220">日志抽取</a></li>
						<li class="second-li"><a href="<c:url value="/page/jsp/seeds/jsp_221.jsp"/>" id="jsp_221">日志查询</a></li>
					</ul>
				</div>
</div>