﻿<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
@charset "utf-8";
/* CSS Document */

/* 宋体************************************ */
* {
	padding:0px; 
	margin:0px;
	font-size:12px; 
}
html{
	/*overflow-y: hidden; */
}
body {
	font-family:"宋体", Arial, Helvetica, sans-serif;
	margin:0 auto;
	/*height:auto;*/
	background:#fff;
	/*overflow-y: hidden; */
}
li {
	list-style:none;
}

a {
	color:#646464;
	text-decoration:none;
}
a:hover {
	text-decoration:none;
}
td{
	/* background-color:#fff; */
}
.content{
	margin:0 auto;
	background:url(images/body_img.jpg) no-repeat right bottom;
}
.head{
	width:100%;
	height:109px;
	background:url(<c:url value="/_parts/_idp/head_left.jpg"/>) no-repeat left top;
}


.bookmark-head2{
    left: 880px;
    position: absolute;
    top: 235px;
    width: 100px;
}
.nav{
	float:left;	
	width:100%;	
}
ul.nav-menu{
	float:left;
	margin:0;
	margin-top:40px;
	padding:0;
	margin-left:30px;
}
.nav-menu li{
	float:left;
	margin:0;
	padding:0;	
	width:100px;
	text-align:left;
	height:20px;
	display:inline;
	list-style: none; 
	cursor:pointer;
}
.nav-menu li a{
	color:#5e82ca;
	display:block;
	height:15px;
	text-align:center;
	padding-top:2px;
	padding-left:3px;	
	margin-top:2px;
	width:60px;
}
.nav-menu li a:hover{
	height:15px;
	color:#fff;
	padding-left:3px;
	padding-top:2px;
	background:url(images/nav-current.jpg) no-repeat left top;
}
.nav-menu li a.current{
	height:15px;
	color:#fff;
	padding-left:3px;
	padding-top:2px;
	background:url(images/nav-current.jpg) no-repeat left top;
}
.drop_menu{
	color:#cce8f9;
	background:#4972ba;
	z-index:10001;
	position:absolute;
	width:100px;
}
.drop_menu li{
	width:90px;
	margin-left:5px;
}
.drop_menu li a:hover{
	background:none;
}
.nav-right{
	float:right;
	height:25px;
	padding-top:80px;
	padding-right:20px;

}
.user{
	float:left;
	padding-left:20px;
	padding-top:3px;
	height:15px;
	width:200px;
	background:url(images/user-icon.jpg) no-repeat left top;
}
.user a{
        color:#fff;
}
.change_password{
	float:left;
	display:block;
	padding-top:3px;
	width:50px;
	height:15px;
	margin-left:10px;
}
.bookmark_head{
	float:left;
	display:block;
	padding-top:3px;
	width:80px;
	height:15px;
	margin-left:10px;
}

.loginout{
	float:left;
	display:block;
	width:46px;
	height:17px;
	margin-left:20px;
	background:url(images/loginout.jpg) no-repeat left top;
}
.middle{
	margin-top:3px;
	
}
.left-menu{
	float:left;
	width:167px;
/* 	height:500px; */
	background:url(images/left_menu_bg.jpg) repeat-x left top;

}
.ztree{
	border-bottom:3px solid #517fca;
}
.left-menu-title{
	font-weight:bold;
	height:20px;
	padding-top:6px;
	margin-left:28px;
	color:#eceff8;
	background:url(images/left_menu_title.jpg) no-repeat right top;
}
.left-menu-main{
	color:#666;
	background:url(images/left_main_re.jpg) repeat-y right top;
}
.left-menu-bottom{
	height:10px;
	background:url(images/left_menu_bottom.jpg) no-repeat right top;
}
.left-menu-main ul{
	margin:0px;
	padding:0px;
}

.changepasswordbox { display:none; position:absolute;  width:400px; border:1px solid #acb5c3; background-color:#fff; border-radius:5px; box-shadow: 2px 2px 1px #888888; min-height:288px; _height:288px; z-index: 299;  }
.changepasswordbox h2 { line-height:25px; height:25px; background-color:#ffd9f3; color:#00264f; padding:0 15px; }

/**
.left-menu-main ul li{
	list-style:none outside none;
	margin:0px; 
	font-size:12px;	
	height:22px;
	text-align:left;
}
.left-menu-main a{
	display:block;
	width:116px;
	height:16px;
	padding-left:21px;
	padding-top:4px;
	background:url(images/list-img.jpg) no-repeat 9px 5px;
}

.left-menu-main a:hover{
	display:block;
	width:116px;
	height:16px;
	padding-left:21px;
	padding-top:4px;
	color:#5479c2;
	background:url(images/left-current.jpg) no-repeat -2px 0px;

.left-menu-main a.left-menu-current{
	display:block;
	width:116px;
	height:16px;
	padding-left:21px;
	padding-top:4px;
	color:#5479c2;
	font-weight:bold;
	
}
.left-menu-main ul li.first-li{
	height:20px;
}
.first-li-title{
	display:block;
	cursor:pointer;
	width:127px;
	height:17px;
	padding-left:10px;
	color:#5479c2;
	padding-top:4px;
	background:url(images/first-li-open-bg.jpg) no-repeat left top;
}
.first-li-title-closed{
	display:block;
	cursor:pointer;
	width:127px;
	height:17px;
	padding-left:10px;
	color:#5479c2;
	padding-top:4px;
	background:url(images/first-li-closed-bg.jpg) no-repeat left top;
}
}*/
.left-menu-bar{
	float:left;
	width:1px;
	height:480px;
	margin-left:2px;
	cursor:e-resize;
}
.scroll{
	float:right;
	overflow:auto;
	overflow-y:scroll;
	margin-left:2px;
	
}
.main{
	color:#666;
	background:#ffffff url(images/main_top.jpg) repeat-x left top;
}
.main_index{
	color:#666;
	background:#ffffff url(images/main_top.jpg) repeat-x left top;
}
.work{
	border:3px solid #517fca;
	border-top:0;
	min-width:651px;
}
.work-title{
	height:24px;
	background:url(images/main_right.jpg) no-repeat right top;
}
.work-title-left{
	width:230px;
	height:18px;
	color:#eceff8;
	font-weight:bold;
	padding-left:30px;
	padding-top:6px;
	background:url(images/main_title.jpg) no-repeat left top;	
}
.to-load{
	width:100px;
	height:100px;
	background:url(images/131.gif) no-repeat center top;	
}
.form-container{
	width:840px;
}
.com-form{
	border:1px solid #cddbdc;
	border-collapse:collapse;
}
.com-form th{
	height:20px;
	color:#333;
	text-align:right;
	padding-right:5px;
	font-weight:normal;
	vertical-align:center;
	border:1px solid #cddbdc;
	background:#e5f1fd url(images/td-label-bg.jpg) repeat-x left top;	
}
.com-form td{
	height:22px;
	padding:2px;
	vertical-align:center;
	border:1px solid #cddbdc;
}
.table-container{
	border:1px solid #86a2d2;
	overflow-x:scroll;
	height:auto;
	overflow:auto;
}
.ui-com-table{
	width:100%;
	border-collapse:collapse;
}
.ui-com-table th{
	height:21px;
	color:#526790;
	vertical-align:center;
	padding:0px 3px;
	font-weight:normal;
	background:url(images/table_th_bg.jpg) repeat-x left bottom;	
	border-left:1px solid #86a2d2;
	border-right:1px solid #86a2d2;
	white-space:nowrap;
}
.ui-com-table td{
	height:20px;
	padding:1px 3px;
	vertical-align:center;
	border:1px solid #d3d3d3;
	/**white-space:nowrap;**/
	line-height:18px;
}
.ui-com-table tr.ui-tr-even td{
	background:#f3f2f2 url(images/even_td_bg.jpg) repeat-x left top;	
}
.ui-input-link{
	color:#a3763b;
}
td.tools{	
	padding-left:10px;
}
td.ui-td-wrap{	
	width:600px;
}

.detail{
	background:url(images/detail_icon.gif) no-repeat left top;	
	padding-left:16px;
	padding-top:2px;
	line-height:20px;
	margin-left:5px;
	color:#a3763b;
}
.edit{
	background:url(images/edit_icon.gif) no-repeat left top;	
	padding-left:16px;
	padding-top:2px;
	line-height:20px;
	margin-left:5px;
	color:#a3763b;
}
.delete{
	background:url(images/delete_icon.gif) no-repeat left 1px;	
	padding-left:16px;
	padding-top:2px;
	line-height:20px;
	margin-left:5px;
	color:#a3763b;
}
.button-container{
	width:100%;
	text-align:center;
	height:30px;
	padding-top:10px;
	background:url(images/button-container-bg.jpg) repeat-x left top;
}
.btn-short{
	border:0;
	color:#fff;
	width:61px;
	height:20px;
	margin:0 10px;
	cursor:pointer;
	background:url(images/btn-short.jpg) no-repeat left top;	
}
.btn-normal{
	border:0;
	color:#fff;
	width:90px;
	height:20px;
	margin:0 10px;
	cursor:pointer;
	background:url(images/btn-normal.jpg) no-repeat left top;	
}
.btn-long{
	border:0;
	color:#fff;
	width:110px;
	height:20px;
	margin:0 10px;
	cursor:pointer;
	background:url(images/btn-long.jpg) no-repeat left top;	
}
.toolbar{
	height:23px;
}
.toolbar table{
	float:right;
}
.toolbar table td{
	white-space:nowrap;
	height:20px;

}
.first{
	width:16px;
	height:14px;
	display:block;
	margin-left:5px;
	background:url(images/cpb_lb11.gif) no-repeat left top;
}
.back{
	width:16px;
	height:14px;
	display:block;
	margin-left:5px;
	background:url(images/cpb_lb12.gif) no-repeat left top;
}
.page{
	width:30px;
	height:13px;
}
.refresh{
	width:16px;
	height:14px;
	display:block;
	margin-left:5px;
	background:url(images/cpb_lb8.gif) no-repeat left top;
}
.forward{
	width:16px;
	height:14px;
	display:block;
	margin-left:5px;
	background:url(images/cpb_lb9.gif) no-repeat left top;
}
.last{
	width:16px;
	height:14px;
	display:block;
	margin-left:5px;
	background:url(images/cpb_lb10.gif) no-repeat left top;
}
.ten{
	width:16px;
	height:16px;
	display:block;
	margin-left:5px;
	background:url(images/cpb_lb5.gif) no-repeat left top;
}
.twenty{
	width:16px;
	height:16px;
	display:block;
	background:url(images/cpb_lb6.gif) no-repeat left top;
}
.fifty{
	width:16px;
	height:16px;
	display:block;
	background:url(images/cpb_lb7.gif) no-repeat left top;
}
.foot{
	width:100%;
	height:18px;
 	text-align:center; 
	color:#4779dc;
	padding:10px 0;
	font-weight:bold;
        border-top:1px solid #f0e8e5;
}
.search-container{
	padding-right:5px;
	border:1px solid #cddbdc;
	background:#e5f1fd url(images/td-label-bg.jpg) repeat-x left top;
}
.search-table th{
	background:none;
	text-align:right;
	padding-left:10px;
	font-weight:normal;
	height:24px;
	color:#333;	
	vertical-align:center;
}
.search-table td{
	background:none;
	text-align:left;
	
	font-weight:normal;
	height:24px;
	color:#333;	
	vertical-align:center;
}
.search-table td.search-icon{
	width:35px;
	background:url(images/search_icon.jpg) no-repeat left top;
}
.search-table td.detail-title-icon{
	width:35px;
	background:url(images/detail_top_icon.jpg) no-repeat left top;
}
.banner{
	border-top:1px solid #6794d9;
	border-bottom:1px solid #cddbdc;
	height:22px;
	width:100%;
	background:url(images/work_banner_bg.jpg) repeat-x left top;	
}
.banner-title{
	height:17px;
	width:500px;
	padding-left:25px;
	padding-top:5px;
	background:url(images/work_title_icon.jpg) no-repeat 5px top;	
}
.ui-window-container {
	position: absolute;
	overflow: visible;
	z-index: 50;
	font-size: 12px;
}
.ui-window-frame{
	background:#fff;
	border:2px solid #6794D9;
}
.ui-window-title{
	height:22px;
	color:#fff;
	background:url(images/box-title-bg.jpg) repeat-x left top;
}
.ui-window-close-button {
	display:block;
	width:24px;
	height:22px;
	float: right;
}
.ui-window-title-inner{
	height:22px;
	background:url(images/box-title.jpg) no-repeat left top;	
}
/**澶嶉�夋**/
.ui-table-tr-unSelected {
	background-color: #00ff00;
	background: url("pics/checkbox.gif") no-repeat 0px 0px;
	cursor: pointer;
	width: 13px;
	height: 13px;
	float: left;
	margin: auto 3px;
}

.ui-table-tr-selected {
	background-color: #00ff00;
	background: url("pics/checkbox.gif") no-repeat 0px -13px;
	cursor: pointer;
	width: 13px;
	height: 13px;
	float: left;
	margin: auto 3px;
}
/**鏃ユ湡鎺т欢**/
.ui-input  {
	position:absolute;
}
.ui-input-date {
	background-color: #fff;
	border:1px solid #6993de;
}
.ui-input-date-text{

	margin:0px;
	height:16px;
	cursor:pointer;
	background:#ffffff url(pics/data-img.jpg) no-repeat right -1px;
}
.search-table .ui-input-date-text{
	border:1px solid #abadb3;
}
.ui-input-date td {
	text-align: center;

}

.ui-input-date-year-table {
	width: 160px;
	background-color: #f1ffe6;
	border-collapse:collapse;
}
.ui-input-date-year-table td{
	background-color: #e5f1fd;
}
.ui-input-date-month-table {
	width: 160px;
	background-color: #f1ffe6;
	border-top:1px solid #6993de;
	border-collapse:collapse;
}

.ui-input-date-month-table td{
	background-color: #e5f1fd;
}

.ui-input-date-table {
	width: 160px;
	border-collapse:collapse;
}

.ui-input-date-table thead tr th{
	background:#6993de;
	padding: 2px;
	color:#fff;
}

.ui-input-date-button-table {
	width: 160px;
}

.ui-input-date td.ui-input-date-td {
	cursor: pointer;
	padding: 3px;
	background:url(pics/date-td-bg.jpg) repeat-x left 1px;
}

.ui-input-date td.ui-iunput-date-hover {
	background:url(pics/date-hover.jpg) no-repeat 0px 0px;
}

.ui-input-date td.ui-iunput-date-selected {
	background:url(pics/date-now.jpg) no-repeat -1px 0px;
}

.ui-input-date-button {
	cursor: pointer;
}
.ui-input-date-button-table .ui-input-date-button{
	padding-top:1px;
	padding-bottom:2px;
}
.ui-input-date-year-quick-back {
	width:19px;
	height:15px;
	font-size:11px;
	background:url(pics/date-btn.jpg) no-repeat 0px -1px;
}

.ui-input-date-year-quick-forward {
	width:19px;
	height:15px;
	font-size:11px;
	background:url(pics/date-btn.jpg) no-repeat 0px -1px;	
}

.ui-input-date-year-back {
	width:19px;
	height:15px;
	font-size:11px;
	background:url(pics/date-btn.jpg) no-repeat 0px -1px;	
}

.ui-input-date-year-forward {
	width:19px;
	height:15px;
	font-size:11px;
	background:url(pics/date-btn.jpg) no-repeat 0px -1px;	
}

.ui-input-date-month-back {
	width:19px;
	height:15px;
	font-size:11px;
	background:url(pics/date-btn.jpg) no-repeat 0px -1px;	
}

.ui-input-date-month-forward {
	width:19px;
	height:15px;
	font-size:11px;
	background:url(pics/date-btn.jpg) no-repeat 0px -1px;	
}
.ui-input-date-button-ok{
	background:url(pics/date-bottom-btn.gif) no-repeat 5px 0px;	
}
.ui-input-date-button-today{
	background:url(pics/date-bottom-btn.gif) no-repeat 5px 0px;	
}
.ui-input-date-button-cancel{
	background:url(pics/date-bottom-btn.gif) no-repeat 5px 0px;	
}
.ui-input-date-button-clearnull{
	background:url(pics/date-bottom-btn.gif) no-repeat 5px 0px;	
}


/***登录***/
.login_body{
	background:#65a3de url(images/login_bg.jpg) repeat-x left top;	
	margin:0;
	padding:0;
}
.login_main{
	width:1000px;
	height:370px;
	margin:190px auto;
	background:url(images/login_float.png) no-repeat center top;	
}
.login_box{
	float:left;
	width:450px;
	height:310px;
	margin-left:275px;
	margin-left:275px;
	margin-top:50px;
	background:url(images/login_box.png) no-repeat center top;
}
.login_table{
	margin-left:250px;
	margin-top:50px;
}
.login_table td{
	background:none;
}
.login_table td input.login_text{
	width:160px;
	height:18px;
	background:#fff;
	border:0;
	margin-bottom:30px;
}
.button1 {
        background: url("images/button.png") no-repeat scroll 0 0 rgba(0, 0, 0, 0);
        border: 0 none;
        color: #8c96a0;
        font-weight: bold;
        height: 32px;
        margin: 5px;
        padding-right: 5px;
        width: 112px;
}
.button2-1 {
    	color: #8c96a0;
    	font-weight: bold;
		height: 32px;
		line-height: 32px;
		padding-left: 27px;

}
.button2-2 {
	cursor:pointer;
	position:absolute;
	opacity:0;
	left:0;
	top:0;
	width: 112px;
}
.progress {
	float:right;
	margin:10px 40px 10px 0;
}
.progress_bar {
	-moz-border-radius:10px;
	-webkit-border-radius:10px;
	border-radius:10px;
	background: url("images/progress_bar_bg.jpg") repeat-x ;
	height:24px;
}
.progress_bar1 {
	background: url("images/progress_bar.jpg") repeat-x ;
	height:24px;
	float:left;
	-moz-border-radius:10px;
	-webkit-border-radius:10px;
	border-radius:10px;
	
}
.progress_bar2 {
	height:24px;
	line-height:24px;
	float:right
}
#progressBar {
    background:rgba(0,0,0,0.7)none repeart scroll !important;
    background:#000;
    filter:Alpha(opacity=70);
    opacity:0.7;
    height: 100%;
    position: fixed;
    width: 100%;
    top:0;
    z-index: 19701;
}
#progressBar #theMeter{
    background: none repeat scroll 0 0 #fff;
    height: 100px;
    left: 40%;
    padding: 10px;
    position: relative;
    top: 40%;
    width: 350px;
}
#progressBar #progressBarText {font-size:14px;color#666;padding:10px 0; font-weight: bold;}
#progressBar #progressStatusText {font-size:14px;color#666;padding:10px 0; font-weight: bold;}
