<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="head">
	<div>
		<img src='<c:url value="/images/header_logo.jpg" />'><img src='<c:url value="/images/top_right_unternehmen.jpg" />'>
	</div>
	<div id="menu-container"> 
		<div class="head-user-desc">			  			
			<security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">				
				<security:authentication property="principal.fullname" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
				<a class="head-a" href="<c:url value="/logoff.jsp" />">退出</a>
			</security:authorize>
		</div>
		<ul class="menu">     
			<li class="menu-item"><a id="head_href_index" href='<c:url value="/" />' >首页</a></li>		
        	<security:authorize ifAnyGranted="ROLE_SALER">
			<li class="menu-item"><a id="head_href_sales" href="<c:url value="/sales/index.htm" />">销售员中心</a></li>
			</security:authorize>
			
			<security:authorize ifAnyGranted="ROLE_BIG_MANAGER,ROLE_FLOW">				
			<li class="menu-item"><a id="head_href_manager" href="<c:url value="/manager/index.htm" />">营销管理中心</a></li>
			</security:authorize>
					
        	<security:authorize ifAnyGranted="ROLE_BIG_MANAGER,ROLE_FLOW">				
		    <li class="menu-item"><a id="head_href_query" href="<c:url value="/query/index.htm" />">流程查询</a></li>
		    </security:authorize>
		    
			<security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
				<li class="menu-item"><a id="head_href_user" href=" <c:url value="/user/changePwdReq.htm" />">修改密码</a></li>  
			</security:authorize>
		</ul>  
	</div>  
</div>