<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="head">
	<div>
		<img src='<c:url value="/images/banner.jpg" />'>
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
		   
		     <security:authorize ifAnyGranted="ROLE_ACC,ROLE_ACC_NORMAL,ROLE_ACC_MANAGER">				
		    <li class="menu-item"><a id="head_href_acc" href="<c:url value="/acc/index.htm" />">电子账本</a></li>
		    </security:authorize>
		    
			<security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
				<li class="menu-item"><a id="head_href_user" href=" <c:url value="/user/changePwdReq.htm" />">修改密码</a></li>  
			</security:authorize>
		</ul>  
	</div>  
</div>