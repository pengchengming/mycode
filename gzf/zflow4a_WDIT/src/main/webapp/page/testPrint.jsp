<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>用户管理-用户角色</title> 

<script language="javascript" src="jquery-1.4.4.min.js"></script>
<script language="javascript" src="jquery.jqprint-0.3.js"></script>

</head>

<script type="text/javascript">
function ddd(){
	$("#ddd").jqprint();
}
function bbb(){
	$("#bbb").jqprint();
}
	
  
</script>

</head>
<body> 
<div id="ddd">
  <table>
    <tr>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
    </tr>
    <tr>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
    </tr>
    <tr>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
    </tr>
    <tr>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
    </tr>
    <tr>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
      <td>test</td>
    </tr>
  </table>
</div>

<Div Id="bbb">
  <Table>
    <Tr>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test1111</Td>
      <Td>Test</Td>
    </Tr>
    <Tr>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test1111</Td>
      <Td>Test</Td>
    </Tr>
    <Tr>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test1111</Td>
      <Td>Test</Td>
    </Tr>
    <Tr>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test1111</Td>
      <Td>Test</Td>
    </Tr>
    <Tr>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test111</Td>
      <Td>Test1111</Td>
      <Td>Test</Td>
    </Tr>
  </Table>
</div>
<input type="button" onclick="ddd()" value="打印"/>
<input type="button" onclick="bbb()" value="打印1"/>
</body>
</html>