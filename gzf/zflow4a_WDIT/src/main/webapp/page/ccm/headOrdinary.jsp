<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<div class="index_banner">
		<img style="float:left" src="/images/logo.jpg">
		<div class="index_top">
  		<ul>
  			<li><a href="${logoutUrl}">Logout</a></li>
  			<li><a href="#">Welcome： ${sessionScope.USER.username}</a></li>
  		</ul>
  		</div>
  		<div class="clear"></div>
	</div> 