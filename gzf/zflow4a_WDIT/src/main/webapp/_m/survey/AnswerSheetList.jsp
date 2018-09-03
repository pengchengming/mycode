<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html manifest="/web2/off.appcache">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1"/>
	<link rel="stylesheet" href="<c:url value="/_m/css/themes/default/jquery.mobile.icons-1.4.5.min.css"/>">
	<link rel="stylesheet" href="<c:url value="/_m/theme-classic/theme-classic.css"/>">
	<link rel="stylesheet" href="<c:url value="/_m/css/themes/default/jquery.mobile.structure-1.4.5.min.css"/>">
	<link rel="shortcut icon" href="../favicon.ico">
	<script src="<c:url value="/_m/js/jquery.js"/>"></script>
	<script src="<c:url value="/_m/js/jquery.mobile-1.4.5.min.js"/>"></script>
	<script type="text/javascript">
		var store = window.localStorage;
	</script>
</head>
<body>
<script type="text/javascript"> 
var store = window.localStorage;
var fbc =  Math.round(Math.random()*(999999-100000)+100000);
var Questionnaireslist = null;
var questionnairesmap = {};
var serverquestionrystatus=null;
var OnGoingQnID=0;
var publicOpenid;
var buttontext ="";
var statustext ="";
var statusList={}; 
var alertmessage="";
var username="";
$(function(){
	initStatusList();
	//alert("initStatusList");
	//$("#qustionarylistUL").html("");
	LoadPage();
	// $.mobile.changePage('#dialog', 'dialog', true, true);
	//	 setInterval(function(){
	//   $("#dialog").dialog("close");
	//		}, 2000);//两秒后关闭

if(alertmessage!="")
{
	//	myalert(alertmessage);
	//$(document).off("pagehide").on("pagehide","#alertDialog",function(){
	////alert("11111111111");
	//alertmessage="";
	//});   
	alert(alertmessage);
	}	
	
}); 

function   myalert(message){
		$("#dialogcontent").html(message);
		//$("#dialog").dialog("popup");
	  $.mobile.changePage('#alertDialog', { role: "dialog" });
}

function   myalertclose(){
	   $("#alertDialog").dialog("close");   
}
function initStatusList(){
				var object;
//100:等待考试状态/未考试
			 object =new Object();
			 object.buttontext="下载试卷";
			 object.statustext="未考试";
			 statusList[100]=object;
//110:下载成功
			 object =new Object();
			 object.buttontext="开始考试";
			 object.statustext="试卷下载成功";
			 statusList[110]=object;
			 
//120:已开始考试（考试异常/考试中）
			 object =new Object();
			 object.buttontext="";
			 object.statustext="考试异常";
			 statusList[120]=object;
	
//130:考试已交卷(考试结束等待上传)
			 object =new Object();
			 object.buttontext="上传试卷";
			 object.statustext="考试已交卷";
			 statusList[130]=object;

//140:考试异常有补考
			 object =new Object();
			 object.buttontext="";
			 object.statustext="考试异常有补考";
			 statusList[140]=object;

//141:考试合格
			 object =new Object();
			 object.buttontext="";
			 object.statustext="考试合格";
			 statusList[141]=object;

//142:考试异常无补考
			 object =new Object();
			 object.buttontext="";
			 object.statustext="考试异常无补考";
			 statusList[142]=object;

//143:考试不合格
			 object =new Object();
			 object.buttontext="";
			 object.statustext="考试不合格";
			 statusList[143]=object;
			 
//150:等待补考
			 object =new Object();
			 object.buttontext="下载试卷";
			 object.statustext="等待补考";
			 statusList[150]=object;

//160:下载补考试卷成功
			 object =new Object();
			 object.buttontext="开始补考";
			 object.statustext="下载补考试卷成功";
			 statusList[160]=object;

//170:开始补考（补考中/补考异常）
			 object =new Object();
			 object.buttontext="";
			 object.statustext="补考异常";
			 statusList[170]=object;

//180:补考已交卷(试结束等待上传)
			 object =new Object();
			 object.buttontext="上传试卷";
			 object.statustext="补考已交卷";
			 statusList[180]=object;

//190:补考不合格
			 object =new Object();
			 object.buttontext="";
			 object.statustext="补考不合格";
			 statusList[190]=object;
//191:补考合格		 
			 object =new Object();
			 object.buttontext="";
			 object.statustext="补考合格";
			 statusList[191]=object;

}
function ReLoadPage(){
	store.setItem("OnGoingQnID",0);
	//$("#qustionarylistUL").html("");
	LoadPage();
}

function LoadPage(){
publicOpenid = store.getItem("publicOpenid");


					//http://sfa.jahwa.com.cn:8080?p=180101_oJP7Xt5K_7naJHLABrKHwC0jjc-Y
				$.ajax({
				    type: "POST",
				    async:false,
				    url: "/zflow/zsurveym_answersheet/callshellprocedure.json?t="+fbc,
				    timeout:100*1000,
				//    contentType: 'application/json',
				    data:{
				    	offline_data: true,
				    	p:"180101_"+publicOpenid
				    },
				    error:function (XMLHttpRequest, textStatus, errorThrown) {
						},
						success :function (data, textStatus) {
							username = data.resultset[0].code;
				
				
						} 
				  });
				
//如果用户信息和系统中保存的不一致，需要清除缓存信息
if(username!=store.getItem("username")){
							store.setItem("username",username);
							store.removeItem("questionnairesmap");
							
				}


   try{
	questionnairesmap = JSON.parse(store.getItem("questionnairesmap"));
   } catch(e){   }

	if(questionnairesmap==null){
		questionnairesmap={};
	}
	 
$.each(questionnairesmap,function(i,questionary){
	//手机端存在未提交的答卷，系统自动提交
	if(questionary.status==130 ||questionary.status==180 ) {
		//alert("手机端存在未提交的答卷，系统自动提交");  
		submitAnswerSheet(i);
		//sendMsg180104(questionary.status,i,questionary.status,questionary.answersheetguid);
	}
	
	
//列表刷新时出现中断的考试,需要报告服务器更新状态
	if(questionary.status==120 ||questionary.status==170 ) {  
		//alert("列表刷新时出现中断的考试");
		sendMsg180104(questionary.status,i,questionary.status,questionary.answersheetguid);
	}	
	
});
loadQuestionairesList();

	//else
	//	{
	//		alert("222222222222");
	//		loadQuestionairesList();
	//		
	//	}

  }


function loadQuestionairesList(){
		
				
				$.ajax({
				    type: "POST",
				    async:false,
				    url: "/zflow/zsurveym_answersheet/callshellprocedure.json?t="+fbc,
				    timeout:100*1000,
				//    contentType: 'application/json',
				    data:{
				    	offline_data: true,
				    	p:"180103_"+username
				    },
				    error:function (XMLHttpRequest, textStatus, errorThrown) {
						},
						success :function (data, textStatus) {
							serverquestionrystatus =data;
							store.setItem("serverquestionrystatus",JSON.stringify(serverquestionrystatus));
							
				
						
				} 
				});

$.ajax({
    type: "POST",
    async:false,
    url: "/zflow/zsurveym_questionnaire/listQuestionnaires?&t="+fbc,
    timeout:100*1000,
    //contentType: 'application/json',
    data:{
    	//offline_data: true,
    	username:store.getItem("username")
    },
    error:function (XMLHttpRequest, textStatus, errorThrown) {
    	alert("网络错误，读取本地缓存！");
    	  Questionnaireslist=JSON.parse(localStorage.getItem("Questionnaireslist"));
		},
		success :function (data, textStatus) {
			store.setItem("Questionnaireslist",JSON.stringify(data));
			Questionnaireslist = data;
// 			var jsondata = store.getItem("listQuestionnaires");
// 			var data=eval(jsondata);

// 			alert(data);			
// 			alert("success");
// 			isSuccess(dataObj);
		} 
  });	

	if(Questionnaireslist!=null)
	initQuestionnairesList(Questionnaireslist);
}

function initQuestionnairesList(data){
		$("#qustionarylistUL").html("");

//  	$("#qustionarylistUL").append(" <li ><table width=\"100%\"><tr><td>启初入职考试</td><td><div c><input type=\"button\" value=\"下载试卷\" data-theme=\"b\"  onclick=\"test()\" ></div></td></tr></table> </li>");
// $("#qustionarylistUL").trigger('create');
//   try{
//	questionnairesmap = JSON.parse(store.getItem("questionnairesmap"));
//   } catch(e){   }
//
//	if(questionnairesmap==null){
//		questionnairesmap={};
//	}
	
	var result= data.results;
 
	if(result!=null)
	{
	for(var i =result.length-1 ; i>=0; i--){
	 var questionary = result[i];
	 if(typeof(questionnairesmap[questionary.id])!= 'object'){
			 var object =new Object();
			 object.questionary=questionary;
			 object.status =100;
			 questionnairesmap[questionary.id]=object;
		 }
	}
	

	for(var i=0;i<serverquestionrystatus.resultset.length;i++){
		if(questionnairesmap[serverquestionrystatus.resultset[i].id]!=null){
			if(questionnairesmap[serverquestionrystatus.resultset[i].id].status!=130 && questionnairesmap[serverquestionrystatus.resultset[i].id].status!=180 )    
			if(questionnairesmap[serverquestionrystatus.resultset[i].id].status<=serverquestionrystatus.resultset[i].status){
					questionnairesmap[serverquestionrystatus.resultset[i].id].status 
						=serverquestionrystatus.resultset[i].status;
					questionnairesmap[serverquestionrystatus.resultset[i].id].correctNum 
							=serverquestionrystatus.resultset[i].correctNum;
					questionnairesmap[serverquestionrystatus.resultset[i].id].makeupcorrectNum 
							=serverquestionrystatus.resultset[i].makeupcorrectNum;		
		}	
//合并后出现中断的考试，视为异常立即报告服务器段更新状态
		if(questionnairesmap[serverquestionrystatus.resultset[i].id].status==120 ||questionnairesmap[serverquestionrystatus.resultset[i].id].status==170 ) {  
			sendMsg180104(questionnairesmap[serverquestionrystatus.resultset[i].id].status,serverquestionrystatus.resultset[i].id,questionnairesmap[serverquestionrystatus.resultset[i].id].status,questionnairesmap[serverquestionrystatus.resultset[i].id].answersheetguid);
		}	
		}
			
	}
 
	 
 
	for(var i =result.length-1 ; i>=0; i--){
	 var questionary = result[i];
	// if(typeof(questionnairesmap[questionary.id])!= 'object'){
	//		 var object =new Object();
	//		 object.questionary=questionary;
	//		 object.status =100;
	//		 questionnairesmap[questionary.id]=object;
	//	 }
	/*
	
		  if(questionnairesmap[questionary.id].status ==100)
	  {
		 buttontext = '下载试卷';
		 statustext ='未考试';
		 }
	 else if(questionnairesmap[questionary.id].status ==110)
	  {
		 buttontext = '开始考试';
		 statustext ='试卷下载成功';
		 }
	 else if(questionnairesmap[questionary.id].status ==120)
	  {
		 buttontext = '';
		 statustext ='考试异常';
		 }
	 else if(questionnairesmap[questionary.id].status ==130)
	  {
		 buttontext = '等待上传';
		 statustext ='考试结束等待上传';
		 }
	 else if(questionnairesmap[questionary.id].status ==140)
	  {
		 buttontext = '';
		 statustext ='考试未通过有补考';
		 }
	 else if(questionnairesmap[questionary.id].status ==141)
	  {
		 buttontext = '';
		 statustext ='考试已通过';
		 }
	 else if(questionnairesmap[questionary.id].status ==142)
	  {
		 buttontext = '';
		 statustext ='考试未通过无补考';
		 }
	 else if(questionnairesmap[questionary.id].status ==150)
	  {
		 buttontext = '下载补考';
		 statustext = '等待补考';
		 }
	 else if(questionnairesmap[questionary.id].status ==160)
	  {
		 buttontext = '开始补考';
		 statustext = '补考下载成功';
		 }
	 else if(questionnairesmap[questionary.id].status ==170)
	  {
		 buttontext = '';
		 statustext = '开始补考'; //（补考中/补考异常
		 }
	 else if(questionnairesmap[questionary.id].status ==180)
	  {
		 buttontext = '等待上传';
		 statustext = '补考已交卷(试结束等待上传)';
		 }	 
	 else if(questionnairesmap[questionary.id].status ==190)
	  {
		 buttontext = '';
		 statustext = '补考不合格';
		 }
	 else if(questionnairesmap[questionary.id].status ==191)
	  {
		 buttontext = '';
		 statustext = '补考合格';
		 }
		 */

	 var qninfotable="";
// 	 qninfotable = "<tr><td colspan=2><table>";
	 qninfotable = qninfotable+"<tr><td colspan=2>"+questionary.cnname+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>考试日期：</td><td>"+shortTimeFormatter(questionary.startDate)+"至"+shortTimeFormatter(questionary.endDate)+"</td></tr>";
	 if(questionary.isMakeupExamination == 1)
	 	qninfotable = qninfotable+"<tr><td>补考日期：</td><td>"+shortTimeFormatter(questionary.makeupStartDate)+"至"+shortTimeFormatter(questionary.makeupEndDate)+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>答题时间：</td><td>"+questionary.examinationTime+"分钟</td></tr>";
	 qninfotable = qninfotable+"<tr><td>题目数量：</td><td>"+questionary.questionNum+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>合格指标：</td><td>"+questionary.qualifiedNum+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>考试成绩：</td><td id=correctNumtd_"+questionary.id+">";
	 if(questionnairesmap[questionary.id].status >130)
	 	qninfotable = qninfotable+questionnairesmap[questionary.id].correctNum
	 qninfotable = qninfotable+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>状态：</td><td id=statustd_"+questionary.id+">"+statusList[questionnairesmap[questionary.id].status].statustext+"</td></tr>";
//	 if(questionary.isMakeupExamination == 1)
//	 	qninfotable = qninfotable+"<tr><td>补考成绩</td><td>"+questionnairesmap[questionary.id].makeupcorrectNum+"</td></tr>";
// 	 qninfotable = qninfotable+"</table></td></tr>";
   var btnhtml="";
   if(statusList[questionnairesmap[questionary.id].status].buttontext!="")
   btnhtml ="<input id=btn_qn_"+questionary.id+" type=\"button\" value=\""+statusList[questionnairesmap[questionary.id].status].buttontext+"\" data-theme=\"b\"  onclick=\"clickQuestionary(this,"+questionary.id+")\" >";
	 else btnhtml="";
	 	
	 $(" <li ><table width=\"100%\"><tr><td><table>"+qninfotable
	 +"<tr><td colspan=2><div id=btndiv_"+questionary.id+">"
	 +btnhtml
	 +"</div></td></tr>"+
// 			 +qninfotable
			 +"</table> </li>").appendTo("#qustionarylistUL").trigger('create');
	}
}
else
	{
		$("<li><table><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>您当前没有需要参与的考试!</td></tr></table></li>").appendTo("#qustionarylistUL").trigger('create');
	}
	
// 	 $("#qustionarylistUL").trigger('create');
	$("#qustionarylistUL").listview('refresh');
	store.setItem("questionnairesmap",JSON.stringify(questionnairesmap));
	
	
}



function reloadQuestionnairesList(data){
		$("#qustionarylistUL").html("");
	
//  	$("#qustionarylistUL").append(" <li ><table width=\"100%\"><tr><td>启初入职考试</td><td><div c><input type=\"button\" value=\"下载试卷\" data-theme=\"b\"  onclick=\"test()\" ></div></td></tr></table> </li>");
// $("#qustionarylistUL").trigger('create');

	var result= data.results;
 
	if(result!=null)
	{
	for(var i =result.length-1 ; i>=0; i--){
	 var questionary = result[i];
	 if(typeof(questionnairesmap[questionary.id])!= 'object'){
			 var object =new Object();
			 object.questionary=questionary;
			 object.status =100;
			 questionnairesmap[questionary.id]=object;
		 }
	}
	 

//	for(var i=0;i<serverquestionrystatus.resultset.length;i++){
//		if(questionnairesmap[serverquestionrystatus.resultset[i].id]!=null){
//			if(questionnairesmap[serverquestionrystatus.resultset[i].id].status<=serverquestionrystatus.resultset[i].status){
//			questionnairesmap[serverquestionrystatus.resultset[i].id].status 
//			=serverquestionrystatus.resultset[i].status;
//		questionnairesmap[serverquestionrystatus.resultset[i].id].correctNum 
//		=serverquestionrystatus.resultset[i].correctNum;
//		questionnairesmap[serverquestionrystatus.resultset[i].id].makeupcorrectNum 
//		=serverquestionrystatus.resultset[i].makeupcorrectNum;		
//	}
//		}
//	}
 
	 
	
 
	for(var i =result.length-1 ; i>=0; i--){
	 var questionary = result[i];
	// if(typeof(questionnairesmap[questionary.id])!= 'object'){
	//		 var object =new Object();
	//		 object.questionary=questionary;
	//		 object.status =100;
	//		 questionnairesmap[questionary.id]=object;
	//	 }
		 

/*
		  if(questionnairesmap[questionary.id].status ==100)
	  {
		 buttontext = '下载试卷';
		 statustext ='未考试';
		 }
	 else if(questionnairesmap[questionary.id].status ==110)
	  {
		 buttontext = '开始考试';
		 statustext ='试卷下载成功';
		 }
	 else if(questionnairesmap[questionary.id].status ==120)
	  {
		 buttontext = '';
		 statustext ='考试异常';
		 }
	 else if(questionnairesmap[questionary.id].status ==130)
	  {
		 buttontext = '等待上传';
		 statustext ='考试结束等待上传';
		 }
	 else if(questionnairesmap[questionary.id].status ==140)
	  {
		 buttontext = '';
		 statustext ='考试未通过有补考';
		 }
	 else if(questionnairesmap[questionary.id].status ==141)
	  {
		 buttontext = '';
		 statustext ='考试已通过';
		 }
	 else if(questionnairesmap[questionary.id].status ==142)
	  {
		 buttontext = '';
		 statustext ='考试未通过无补考';
		 }
	 else if(questionnairesmap[questionary.id].status ==150)
	  {
		 buttontext = '下载补考';
		 statustext = '等待补考';
		 }
	 else if(questionnairesmap[questionary.id].status ==160)
	  {
		 buttontext = '开始补考';
		 statustext = '补考下载成功';
		 }
	 else if(questionnairesmap[questionary.id].status ==170)
	  {
		 buttontext = '';
		 statustext = '开始补考'; //（补考中/补考异常
		 }
	 else if(questionnairesmap[questionary.id].status ==180)
	  {
		 buttontext = '等待上传';
		 statustext = '补考已交卷(试结束等待上传)';
		 }	 
	 else if(questionnairesmap[questionary.id].status ==190)
	  {
		 buttontext = '';
		 statustext = '补考不合格';
		 }
	 else if(questionnairesmap[questionary.id].status ==191)
	  {
		 buttontext = '';
		 statustext = '补考合格';
		 }


*/

	 var qninfotable="";
// 	 qninfotable = "<tr><td colspan=2><table>";
	 qninfotable = qninfotable+"<tr><td colspan=2>"+questionary.cnname+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>考试日期：</td><td>"+shortTimeFormatter(questionary.startDate)+"至"+shortTimeFormatter(questionary.endDate)+"</td></tr>";
	 if(questionary.isMakeupExamination == 1)
	 	qninfotable = qninfotable+"<tr><td>补考日期：</td><td>"+shortTimeFormatter(questionary.makeupStartDate)+"至"+shortTimeFormatter(questionary.makeupEndDate)+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>答题时间：</td><td>"+questionary.examinationTime+"分钟</td></tr>";
	 qninfotable = qninfotable+"<tr><td>题目数量：</td><td>"+questionary.questionNum+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>合格指标：</td><td>"+questionary.qualifiedNum+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>考试成绩：</td><td id=correctNumtd_"+questionary.id+">";
	 if(questionnairesmap[questionary.id].status >130)
	 	qninfotable = qninfotable+questionnairesmap[questionary.id].correctNum
	 qninfotable = qninfotable+"</td></tr>";
	 qninfotable = qninfotable+"<tr><td>状态：</td><td id=statustd_"+questionary.id+">"+statusList[questionnairesmap[questionary.id].status].statustext+"</td></tr>";
//	 if(questionary.isMakeupExamination == 1)
//	 	qninfotable = qninfotable+"<tr><td>补考成绩</td><td>"+questionnairesmap[questionary.id].makeupcorrectNum+"</td></tr>";
// 	 qninfotable = qninfotable+"</table></td></tr>";
   var btnhtml="";
   if(statusList[questionnairesmap[questionary.id].status].buttontext!="")
   btnhtml ="<input id=btn_qn_"+questionary.id+" type=\"button\" value=\""+statusList[questionnairesmap[questionary.id].status].buttontext+"\" data-theme=\"b\"  onclick=\"clickQuestionary(this,"+questionary.id+")\" >";
	 else btnhtml="";
	 	
	 $(" <li ><table width=\"100%\"><tr><td><table>"+qninfotable
	 +"<tr><td colspan=2><div id=btndiv_"+questionary.id+">"
	 +btnhtml
	 +"</div></td></tr>"+
// 			 +qninfotable
			 +"</table> </li>").appendTo("#qustionarylistUL").trigger('create');
	}
}
else
	{
		$("<li><table><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr><tr><td>您当前没有需要参与的考试!</td></tr></table></li>").appendTo("#qustionarylistUL").trigger('create');
	}
	
// 	 $("#qustionarylistUL").trigger('create');
	$("#qustionarylistUL").listview('refresh');
	store.setItem("questionnairesmap",JSON.stringify(questionnairesmap));
	
	
}


function fullTimeFormatter(value) {

    var da = new Date(value);

    return da.getFullYear() + "-" + (da.getMonth() + 1) + "-" + da.getDate() + " " + da.getHours() + ":" + da.getMinutes() + ":" + da.getSeconds();

}


function shortTimeFormatter(value) {

    var da = new Date(value);

    return da.getFullYear() + "-" + (da.getMonth() + 1) + "-" + da.getDate() ;

}

function startQuestionary(me,qnId){
   //alert("startQuestionary");
   store.setItem("OnGoingQnID",qnId);
   store.setItem("OnGoingQtIndex",0);
   
   var answersheetarray = store.getItem("answersheetarray");
   if(answersheetarray == null){
	   answersheetarray = new Array();
	   store.setItem("answersheetarray",answersheetarray);
   }
	if(questionnairesmap[qnId].status ==110){   
	questionnairesmap[qnId].status =120;
	$("#statustd_"+qnId).html("考试中");
}
	else if(questionnairesmap[qnId].status ==160){
	questionnairesmap[qnId].status =170;
	$("#statustd_"+qnId).html("补考中");
	}
	
 //alert(	guid())
	questionnairesmap[qnId].answersheetguid=guid();
	alert('请注意：开始考试后禁止退出或返回，否则作为异常处理');
		store.setItem("questionnairesmap",JSON.stringify(questionnairesmap));
		
	  sendMsg180104(questionnairesmap[qnId].status,qnId,questionnairesmap[qnId].status,questionnairesmap[qnId].answersheetguid);
	  if(questionnairesmap[qnId].status ==120||questionnairesmap[qnId].status ==170){
	 					 $("#btndiv_"+qnId).html(""); 
	 					window.location="AnswerSheetDetail.jsp?t="+Math.round(Math.random()*(999999-100000)+100000);   
	 }

	/*
	myalert('请注意：开始考试后禁止退出或返回，否则作为异常处理');//questionnairesmap[qnId].answersheetguid
	$(document).off("pagehide","#alertDialog").on("pagehide","#alertDialog",function(){
		//alert("后台继续执行了");
		store.setItem("questionnairesmap",JSON.stringify(questionnairesmap));
		
	  sendMsg180104(questionnairesmap[qnId].status,qnId,questionnairesmap[qnId].status,questionnairesmap[qnId].answersheetguid);
	  if(questionnairesmap[qnId].status ==120||questionnairesmap[qnId].status ==170){
	 					 $("#btndiv_"+qnId).html(""); 
	 					window.location="AnswerSheetDetail.jsp?t="+Math.round(Math.random()*(999999-100000)+100000);   
	 }
 });
 */

}

 
function sendMsg180104(opcode,qnId,qnstatus,answersheetcode){
		//alert("180104_"+store.getItem("username")+"_"+opcode+"_"+qnId+"_"+qnstatus+"_"+answersheetcode+"_");
		$.ajax({
	    type: "POST",
	    async:false,
	    url: "/zflow/zsurveym_answersheet/callshellprocedure.json?t="+fbc,
	    timeout:100*1000,
	//    contentType: 'application/json',
	    data:{
	    	offline_data: true,
	    	p:"180104_"+store.getItem("username")+"_"+opcode+"_"+qnId+"_"+qnstatus+"_"+answersheetcode+"_"
	    },
	    error:function (XMLHttpRequest, textStatus, errorThrown) {
	    	alert("网络异常错误!本操作必须保证网络在线!(180104)");
			},
			success :function (data, textStatus) {
				if(data.resultset[0].code!="0000")
					alert("网络异常错误!");
				else{
					questionnairesmap[qnId].status = data.resultset[0].status;
//--					 $("#btndiv_"+qnId).html(statusList[questionnairesmap[qnId].status].buttontext);
//--					 $("#statustd_"+qnId).html(statusList[questionnairesmap[qnId].status].statustext); 

			 Questionnaireslist=JSON.parse(localStorage.getItem("Questionnaireslist"));
						if(Questionnaireslist!=null)
				reloadQuestionnairesList(Questionnaireslist);


					}
			} 
	  });
	
}

function guid() {
	return 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'.replace(/[xy]/g,
			function(c) {
				var r = Math.random() * 16 | 0, v = c == 'x' ? r
						: (r & 0x3 | 0x8);
				return v.toString(16);
			});
}

function downloadQuestionary(me,qnId){
																				$.ajax({
																			    type: "POST",
																			    async:false,
    																			url: "/zflow/zsurveym_questionnaire/info/"+qnId+".json",
																			    timeout:100*1000,
																			    //contentType: 'application/json',
																			    data:{
																			    	//offline_data: true,
																			    	username:store.getItem("username")
																			    },
																			    error:function (XMLHttpRequest, textStatus, errorThrown) {
    alert("网络错误，读取本地缓存！");
																					},
																					success :function (data, textStatus) {
// 			alert(JSON.stringify(data));
//正常考试清除补考题
			for(var i=data.questions.length-1;i>=0;i--){
				if(questionnairesmap[qnId].status==100){
					if(data.questions[i].isMakeup==1){
						data.questions = removeElement(i,data.questions);
					}
				}
//补考清除补考题
				else if(questionnairesmap[qnId].status==150){
					if(data.questions[i].isMakeup==0){
						data.questions = removeElement(i,data.questions);
					}
				}
			}
			store.setItem("Questionnaire_"+qnId,JSON.stringify(data));
// 			alert(data.questions[0].context);
		  if(questionnairesmap[qnId].status ==100){
				//questionnairesmap[qnId].status =110;
					$("#statustd_"+qnId).html("已下载试卷");
				$('#btn_qn_'+qnId).val("开始考试");
			}else
				if(questionnairesmap[qnId].status ==150){
				//	questionnairesmap[qnId].status =160;
					$("#statustd_"+qnId).html("已下载补考试卷");
				$('#btn_qn_'+qnId).val("开始补考");
			}
			
 			sendMsg180104(questionnairesmap[qnId].status,qnId,questionnairesmap[qnId].status,questionnairesmap[qnId].answersheetguid);
 			
				
			$('#btn_qn_'+qnId).button( "refresh" );
			store.setItem("questionnairesmap",JSON.stringify(questionnairesmap));
																					} 
																			  });			
																			  
	
	
}

function removeElement(index,array)
{
 if(index>=0 && index<array.length)
 {
  for(var i=index; i<array.length; i++)
  {
   array[i] = array[i+1];
  }
  array.length = array.length-1;
 }
 return array;
}

function clickQuestionary(me,qnId){
//   if($('#btn_qn_'+qnId).val()=="下载试卷")
	if(questionnairesmap[qnId].status ==100||questionnairesmap[qnId].status ==150)
	  	downloadQuestionary(me,qnId);
	else if(questionnairesmap[qnId].status ==110||questionnairesmap[qnId].status ==160)
		startQuestionary(me,qnId);
//   else if($('#btn_qn_'+qnId).val()=="开始考试")
	else if(questionnairesmap[qnId].status ==130||questionnairesmap[qnId].status ==180)
		submitAnswerSheet(qnId);		
}


function submitAnswerSheet(qnId){
	var tempanswersheetid;
	//alert("1");
	$.ajax({
		type : "PUT",
	    async:false,
		url : "/zflow/zsurveym_answersheet/answersheet.json",
		data : JSON.stringify(questionnairesmap[qnId].answersheetobjects.answersheet),
		contentType : "application/json", //发送至服务器的类型
		dataType : "json", //预期服务器返回类型
		success : function(data) {
	//alert("2");

			answerSheetData = data.answersheet;
			tempanswersheetid = answerSheetData.id;

			for (var i = 0; i < questionnairesmap[qnId].answersheetobjects.anwserquestionlist.length; i++) {
				questionnairesmap[qnId].answersheetobjects.anwserquestionlist[i].answerSheet.id = answerSheetData.id;
			}
		}

	});	
	$.ajax({
		type : "PUT",
	    async:false,
		url : "/zflow/zsurveym_answersheet/answerquestionlist.json",
		data : JSON.stringify(questionnairesmap[qnId].answersheetobjects.anwserquestionlist),
		contentType : "application/json", //发送至服务器的类型
		dataType : "json", //预期服务器返回类型
		success : function(data) {
			//alert(JSON.stringify(data));

		}

	});

	$.ajax({
	    type: "POST",
	    async:false,
	    url: "/zflow/zsurveym_answersheet/callshellprocedure.json?t="+fbc,
	    timeout:100*1000,
	//    contentType: 'application/json',
	    data:{
	    	offline_data: true,
	    	p:"180105_"+tempanswersheetid+"_"
	    },
	    error:function (XMLHttpRequest, textStatus, errorThrown) {
	    	alert("网络错误!");
			},
			success :function (data, textStatus) {
				if(data.resultset[0].code!="0000")
					alert("网络异常错误!");
				else{
					
					
						alertmessage = "考试成绩:"+data.resultset[0].correctNum;
					if(data.resultset[0].correctNum>=questionnairesmap[qnId].questionary.qualifiedNum)
						alertmessage = alertmessage +"(合格)";
					else 
						alertmessage = alertmessage +"(不合格)";
						
						//myalert(message);
					//$("#correctNumtd")=data.resultset[0].correctNum;
					//alert($("#correctNumtd_"+qnId));
					//alert($("#correctNumtd_"+qnId).innerHTML);
 
								$("#correctNumtd_"+qnId).html(data.resultset[0].correctNum);
								//questionnairesmap[qnId].status =data.resultset[0].status;
								if(data.resultset[0].correctNum>=questionnairesmap[qnId].questionary.qualifiedNum)
											$("#statustd_"+qnId).html("合格");
								else
											$("#statustd_"+qnId).html("不合格");
						 
				}
			} 
	  });
	
	/*
	$.ajax({
	    type: "POST",
	    async:false,
	    url: "/zflow/zsurveym_answersheet/callshellprocedure.json?t="+fbc,
	    timeout:100*1000,
	//    contentType: 'application/json',
	    data:{
	    	offline_data: true,
	    	p:"180106_"+tempanswersheetid+"_"
	    },
	    error:function (XMLHttpRequest, textStatus, errorThrown) {
	    	alert("网络错误!");
			},
			success :function (data, textStatus) {
				if(data.resultset[0].code!="0000")
					alert("网络异常错误!");
				else{
					alert("考试成绩:"+data.resultset[0].correctNum);
					//$("#correctNumtd")=data.resultset[0].correctNum;
					$("#correctNumtd_"+qnId).html(data.resultset[0].correctNum);
					questionnairesmap[qnId].status =data.resultset[0].status;
				}
			} 
	  });
	*/

 			sendMsg180104(questionnairesmap[qnId].status,qnId,questionnairesmap[qnId].status,questionnairesmap[qnId].answersheetguid);

	
	store.setItem("questionnairesmap",JSON.stringify(questionnairesmap));
	$('#btn_qn_'+qnId).val("上传完成");
	$('#btn_qn_'+qnId).button( "refresh" );

}

function gohome(){
	window.location="/web2/indexNew.php?opentype=1&openid="+publicOpenid;
	
}
</script>
<div data-role="page" id="pageone" data-theme="b" >
<div data-role="header"   data-position="inline">
  <a href="#" onclick="gohome();" data-role="button">返回</a>
  <h1>考试列表</h1>
</div>

  <div data-role="content" > 
    <ul data-role="listview" data-inset="true" id="qustionarylistUL">

    </ul>
  </div>
  

  <div data-role="footer"   data-position="inline">
    <a href="#" data-role="button" data-icon="arrow-d" onclick="ReLoadPage();" >强制刷新</a>   
  </div>  
  
  
</div> 

<div data-role="dialog" id="alertDialog">
  <div data-role="header">
    <h1>提示信息</h1>
  </div>
  <div data-role="content" id="dialogcontent">
     
    
  </div>
  <div data-role="footer">
    <h4><a href="#" data-role="button" onclick="myalertclose();" >确定</a></h4>
  </div>
</div>
  

</body>
</html>



