<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- <link rel="stylesheet" type="text/css" href="<c:url value="/css/form/jquery-ui-1.8.17.custom.css" />"/> --%>
<link href="<c:url value="/css/ccm/common.css"/>" rel="stylesheet" type="text/css" />
<%@ include file="/WEB-INF/views/commonCss.jsp" %>
<%@ include file="/WEB-INF/views/commonJs4Dragable.jsp" %> 
<%-- <script type="text/javascript" src="<c:url value="/script/ui.js"/>"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/script/jquery-ex.js"/>"></script> --%>
<script type="text/javascript" src="<c:url value="/script/jquery.js"/>"></script>

<%-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form/jquery-ui-1.8.17.custom.css" />"/>
<%@ include file="/WEB-INF/views/commonCss.jsp" %>
<%@ include file="/WEB-INF/views/commonJs4Dragable.jsp" %> 
<script type="text/javascript" src="<c:url value="/script/ui.js"/>"></script>
<script type="text/javascript" src="<c:url value="/script/jquery-ex.js"/>"></script> --%>
<script type="text/javascript">

$().ready(function() {
 $('#formq .dx input[type=radio]').click(function(){
	$(this).parents('ul').nextAll('input').eq(1).val($(this).attr('name'));
		$(this).parents('ul').nextAll('input').eq(0).val($(this).val());
		})
});

function fun(){
	 if(confirm('是否交卷'))
     {
		 var dox=$('.dox');
			var doxNum=dox.length;
		 for(var i=0;i<doxNum;i++){
			   var checkbox=dox.eq(i).find('input[type=checkbox]:checked');
			   var checkboxNum=checkbox.length;
			   var staring="";
			  for(var k=0;k<checkboxNum;k++){
				   staring+=checkbox.eq(k).val()+"o";
			  }
			  dox.find('input[type=hidden]').eq(0).val(staring);
			 }
			 $('#formq').submit();
	  }
}
</script> 
<title>问卷</title>
</head>
<body>
<div class="content">
	<%@ include file="/head2.jsp"%>
		<div class="middle" style="padding-left:400px; width:700px;">
		<div align="center" class="page-desc">
		<h3>${questionnaire.cnname}</h3>
		</div>
		<br>
		<div style="font-size: 1.3em;">
		<table cellpadding="0" border="1px"  cellspacing="1" width="100%">
	  <tr>
	  	<td>合同编号:&nbsp;&nbsp;&nbsp;${outside.oscarId}</td>
	    <td>分公司:&nbsp;&nbsp;&nbsp;${outside.business.name}</td>
	    <td>国&nbsp;&nbsp;&nbsp;&nbsp;别:&nbsp;&nbsp;&nbsp;${outside.nation}</td>
	    <td>调查日期:&nbsp;&nbsp;&nbsp;${outside.surveyDate}</td>
	  </tr>
	  <tr>
	    <td>姓&nbsp;&nbsp;&nbsp;&nbsp;名:&nbsp;&nbsp;&nbsp;${outside.stuName}</td>
	    <td>生&nbsp;&nbsp;日:&nbsp;&nbsp;&nbsp;${outside.birthday}</td>
	    <td>联系方式:&nbsp;&nbsp;&nbsp;${outside.tel}</td>
	    <td>顾&nbsp;&nbsp;&nbsp;&nbsp;问:&nbsp;&nbsp;&nbsp;${outside.consultantOne}</td>
	  </tr>
	</table>
	</div>
	<h3>调查话述</h3>
	<p>${outside.stuName} ${questionnaire.description}</p>
	<form action="../questionnaire/save_questionnaire" id="formq" method="post">
	<input type="hidden" name="memberName" value="${outside.stuName}"/>
	<input type="hidden" name="quesnId" value="${questionnaire.id}"/>
	<input type="hidden" name="answerSheetId" value="${answerSheetId}"/>
	<c:forEach items="${questions}" var="question" varStatus="a">
	<br><h3>Q${a.index+1}:${question.context}</h3>
	<c:if test="${question.questionType.id=='1'}">
	<div class="dx">
	<ul>
	<li>
	<c:forEach items="${question.options}" var="opstion" varStatus="b">
	<c:if test="${question.questionType.id=='1'}">
	<div>
	${b.index+1}. <input type="radio" id="radio"  value="${opstion.id}" name="${question.id}"/>${opstion.cntxt}
		<c:if test="${option.hastextvalue=='1'}">
			${option.textvaluenote}<input type="Text" name="optiontextvalue" value="888"/>
			<input type="hidden" name="optiontextvalueIDs" value="${option.id}"/>
		</c:if>	
	</div>
	</c:if>
	</c:forEach>
	</li>
	</ul>
	<input type="hidden" name="optionNames" value=""/>
	<input type="hidden" name="questionIds" value=""/>
		<c:if test="${question.hastextvalue=='1'}">
			${question.textvaluenote}<input type="Text" name="questiontextvalue" value=""/>
			<input type="hidden" name="questiontextvalueIDs" value="${question.id}"/>
		</c:if>
	</div>
	</c:if>
	<c:if test="${question.questionType.id=='2'}">
	<div class="dox">
	<ul>
	<li>
	<c:forEach items="${question.options}" var="opstion" varStatus="d">
	<c:if test="${question.questionType.id=='2'}">
	<div>
	${d.index+1}. <input type="checkbox" name="${question.id}"  value="${opstion.id}"/>${opstion.cntxt}<br>
	<br>
		<c:if test="${option.hastextvalue=='1'}">	
			${option.textvaluenote}<input type="Text" name="optiontextvalue" value=""/>
			<input type="hidden" name="optiontextvalueIDs" value="${option.id}"/>
		</c:if>	
	</div>
	</c:if>
	</c:forEach>
	</li>
	</ul>
	<input type="hidden" name="optionNames" value=""/>
	<input type="hidden" name="questionIds" value="${question.id}"/>
	</div>
	</c:if>
	<c:if test="${question.questionType.id=='3'}">
	<div>
	<ul>
	<li>
	<textarea rows="4" cols="30" id="textarea" name="text" style="resize: none;"></textarea><br>
	<input type="hidden" name="textId" value="${question.id}"/>
		<c:if test="${question.hastextvalue=='1'}">
			<input type="Text" name="questiontextvalue" value=""/>
			<input type="hidden" name="questiontextvalueIDs" value="${question.id}"/>
		</c:if>

	</li>
	</ul>
	</div>
	</c:if>
	</c:forEach>
	<br>
	<button type="button" onclick="fun()">提交问卷</button>
	<c:if test="${message=='1'}">
	<span style="color:red;">不可重复提交!</span>
	</c:if>
	</form>
	<br><p>${questionnaire.entitle}</p>
		</div>
		<div class="foot" style="color:white;">
			<span style="font-family:Verdana;margin-right:10px;">Version 1.0</span>
		</div>
	</div>
</body>
</html>