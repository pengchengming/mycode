<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>

	<container>			
		<wrapper>
			<form> 
				<h1>报 名</h1> 
				<p> 
					<label>您的姓名:</label>
					<input fieldid="1" id="name"  type="text" value="${name}" >
				</p>
				<p> 
					<label>您的联系电话:</label>
					<input fieldid="2" id="telephone" type="text" value="${telephone}">
				</p>
				<p> 
					<label data-icon="u" >您的Email:</label>
					<input fieldid="3" id="email" type="text" value="${email}">
				</p>
				<p> 
					<label>您所在公司名称:</label>
					<input fieldid="4" id="company" type="text" value="${company}">
				</p>
				<input class="button" type="button" value="确 定"  onclick="saveFormData()" />
				
				<input fieldid="5" id="openid" size="50" type="hidden" value="${openid}">
				<input fieldid="6" id="activitycode" size="50" type="hidden"  value="${activitycode}">				
			</form>
		</wrapper>
    </container>

</body>
	</html>		
	