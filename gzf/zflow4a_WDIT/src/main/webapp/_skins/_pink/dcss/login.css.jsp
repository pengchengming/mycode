<%@ page language="java" contentType="text/css; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page  language="java" import="com.bizduo.zflow.util.Configure" %>
@charset "utf-8";
/* CSS Document */
* {
	margin:0 auto;
	padding:0;
}
body {
	background: url("<c:url value="/_parts/" /><%=Configure.getConfigure("parts") %>/login_bg.jpg") repeat-x;
	background-color: #e7adbb;
}
.top {
	background: url("<c:url value="/_parts/" /><%=Configure.getConfigure("parts") %>/logo.jpg") no-repeat;
	height:77px;
	position: relative;
	top: 4px;
}
.body {
  background: url("<c:url value="/_parts/" /><%=Configure.getConfigure("parts") %>/login_table.jpg") no-repeat;
	min-height:645px;
	height:100%/*670px*/;
	width:1200px;
	position:relative;
}
.login-table {
    left: 300px;
    position: absolute;
    top: 350px;
    width: 300px;
}

.bookmark-login{
    left: 1010px;
    position: absolute;
    top: 30px;
    width: 100px;
}
.input01 {
	background: url("../images/input01.jpg") no-repeat scroll 10px 8px #FFFFFF;
	border: 1px solid #CCCCCC;
	border-top-left-radius: 5px;
	border-top-right-radius: 5px;
	font-size: 18px;
	height: 45px;
	height: 30px\9;/*ie*/
	padding: 5px 0 5px 50px;
	width: 250px;
	width: 200px;\9;/*ie*/
	font-family: 'Microsoft YaHei';
	color: #666666;
}
.input02 {
	background: url("../images/input02.jpg") no-repeat scroll 10px 8px #FFFFFF;
	border-bottom-left-radius: 5px;
	border-bottom-right-radius: 5px;
	font-size: 18px;
	height: 45px;
	height: 30px\9;/*ie*/
	padding: 5px 0 5px 50px;
	width: 250px;
	width: 200px;\9;/*ie*/
	font-family: 'Microsoft YaHei';
	color: #666666;
	border:1px solid #CCCCCC;
	margin-top:-1px;
}
.button {
	background: url("../images/buttom_bg.jpg") no-repeat scroll ;
    color: #FFFFFF;
    display: inline-block;
    margin-right: 5px;
    line-height:32px;
    text-align: center;
    width: 100px;
    cursor:pointer;
    font-size:16px;
    width:100px;
    height:32px;
    border:0;
}
input:hover {
	    border-color: #4A9BDF !important;
	    box-shadow: 0 0 5px #AEDCFA !important;
	    z-index:100;
	    position: relative;
	}