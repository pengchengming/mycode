<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<div class="index_left_menu">
		<span>菜单项</span>
		
		<c:forEach var="thmi" items="${sessionScope.leftMenus}" >
			<ul style="color:red;" >
				
				<li><a href="<c:url value="${thmi.url}"/>"  class="">${thmi.name}</a></li>
			</ul>	
		</c:forEach>
	</div>