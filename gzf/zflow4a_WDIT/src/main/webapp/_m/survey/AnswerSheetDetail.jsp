<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<title>考试中</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link rel="stylesheet"
	href="<c:url value="/_m/css/themes/default/jquery.mobile.icons-1.4.5.min.css"/>">
<link rel="stylesheet"
	href="<c:url value="/_m/theme-classic/theme-classic.css"/>">
<link rel="stylesheet"
	href="<c:url value="/_m/css/themes/default/jquery.mobile.structure-1.4.5.min.css"/>">
<link rel="shortcut icon" href="../favicon.ico">
<script src="<c:url value="/_m/js/jquery.js"/>"></script>
<script src="<c:url value="/_m/js/jquery.mobile-1.4.5.min.js"/>"></script>
</head>
<body>
	<script type="text/javascript">
		var intDiff = parseInt(60);//倒计时总秒数量
		var oneminute=0;
		function timer(intDiff) {
			window.setInterval(function() {
				var day = 0, hour = 0, minute = 0, second = 0;//时间默认值 
				//intDiff =GetDateDiff(answersheet.create_time , new Date(), "second");
				     
				intDiff =(answersheet.create_time+Questionnaire.questionnaire.examinationTime*60*1000-new Date().getTime())/1000;    
				if (intDiff > 0) {
					//day = Math.floor(intDiff / (60 * 60 * 24));
					//hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
					minute = Math.floor(intDiff / 60);
					//- (day * 24 * 60)
					//		- (hour * 60);
					second = Math.floor(intDiff) 
					//- (day * 24 * 60 * 60)
					//		- (hour * 60 * 60) 
							- (minute * 60);
				}
				if (minute <= 9)
					minute = '0' + minute;
				if (second <= 9)
					second = '0' + second;
				$('#day_show').html(day + "天");
				$('#hour_show').html('<s id="h"></s>' + hour + '时');
				$('#minute_show').html('<s></s>' + minute + '分');
				$('#second_show').html('<s></s>' + second + '秒');
				intDiff--;
				if(intDiff<=60&&oneminute == 0) {
					oneminute = 1;
					alert("考试时间还有1分钟结束");
				}
				if(intDiff<=0){ 
					alert("考试时间结束，自动提交试卷");
					submitAnswerSheet();
				}
			}, 1000);
		}
		
/*
* 获得时间差,时间格式为 年-月-日 小时:分钟:秒 或者 年/月/日 小时：分钟：秒
* 其中，年月日为全格式，例如 ： 2010-10-12 01:00:00
* 返回精度为：秒，分，小时，天
*/		
function GetDateDiff(startTime, endTime, diffType) {
    //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式
    console.info(startTime);
    startTime = startTime.replace(/\-/g, "/");
    endTime = endTime.replace(/\-/g, "/");
    //将计算间隔类性字符转换为小写
    diffType = diffType.toLowerCase();
    var sTime = new Date(startTime);      //开始时间
    var eTime = new Date(endTime);  //结束时间
    //作为除数的数字
    var divNum = 1;
    switch (diffType) {
        case "second":
            divNum = 1000;
            break;
        case "minute":
            divNum = 1000 * 60;
            break;
        case "hour":
            divNum = 1000 * 3600;
            break;
        case "day":
            divNum = 1000 * 3600 * 24;
            break;
        default:
            break;
    }
    return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
}		
		 
	</script>

	<script type="text/javascript">
		var store = window.localStorage;
		var question;
		var optionnum;
		var OnGoingQnID;
		var Questionnaire;
		var OnGoingQtIndex;
		var totalnum;
		var answersheet;
		var answersheetarray;
		var currentAnswersheetQuestion;
		var answersheetobjects = new Object();
		$(document).ready(
				function() {
					answersheet = new Object();

					var anwserquestionlist = new Array();

					var myDate = new Date();

					OnGoingQnID = store.getItem("OnGoingQnID");
					Questionnaire = JSON.parse(store.getItem("Questionnaire_"
							+ OnGoingQnID));
					OnGoingQtIndex = store.getItem("OnGoingQtIndex");
					totalnum = Questionnaire.questions.length;

					questionnairesmap = JSON.parse(store.getItem("questionnairesmap"));

					answersheet.create_time = myDate.getTime();
					answersheet.questionnaire = new Object();
					answersheet.questionnaire.id = OnGoingQnID;
					answersheet.userName=store.getItem("username");
					answersheet.code = questionnairesmap[OnGoingQnID].answersheetguid;
					if(questionnairesmap[OnGoingQnID].status==120)
						answersheet.isMakeup = 0;
					else if(questionnairesmap[OnGoingQnID].status==170)
						answersheet.isMakeup = 1;

					for (var i = 0; i < Questionnaire.questions.length; i++) {
						var question = new Object();
						var options = new Array();
						var answerquestion = new Object();
						question.id = Questionnaire.questions[i].id;
						answerquestion.answerOptions = options;
						answerquestion.question = question;
						answerquestion.answerSheet = new Object();
						anwserquestionlist.push(answerquestion);
					}
					answersheetobjects.answersheet = answersheet;
					answersheetobjects.anwserquestionlist = anwserquestionlist;

//					store.setItem("answersheet_" + answersheet["id"], JSON
//							.stringify(answersheetobjects));

					var answersheetarray = store.getItem("answersheetarray");
					if (typeof (answersheetarray) != 'object') {
						answersheetarray = new Array();
					}

					answersheetarray.push("answersheet_" + answersheet.code);

					store.setItem("answersheetarray", JSON.stringify(answersheetarray));

					$('#index_show').html(
							"" + (parseInt(OnGoingQtIndex) + 1) + "/"
									+ totalnum);
					loadQuestion(OnGoingQtIndex);


			//		if(questionnairesmap[OnGoingQnID].status==1)
			//			questionnairesmap[OnGoingQnID].status = 2;
			//		else if(questionnairesmap[OnGoingQnID].status==6)
			//			questionnairesmap[OnGoingQnID].status = 7;
					store.setItem("questionnairesmap", JSON.stringify(questionnairesmap));
					
					intDiff=Questionnaire.questionnaire.examinationTime*60;
					timer(intDiff);
					
				});



		function loadQuestion(OnGoingQtIndex) {


			if (parseInt(OnGoingQtIndex) + 1 == totalnum){				
				$("#nextbtn1").css('display','none');
				$("#nextbtn2").css('display','none');
			}else{				
				$("#nextbtn1").css('display','');
				$("#nextbtn2").css('display','');
			}			

			if (parseInt(OnGoingQtIndex) ==0){				
				$("#prevbtn1").css('display','none');
				$("#prevbtn2").css('display','none');
			}
			else
				{				
				$("#prevbtn1").css('display','');
				$("#prevbtn2").css('display','');
			}
			
			currentAnswersheetQuestion = answersheetobjects.anwserquestionlist[OnGoingQtIndex];
			currentAnswersheetQuestionOptionArray = currentAnswersheetQuestion.answerOptions;
			question = Questionnaire.questions[OnGoingQtIndex];
			optionnum = question.options.length;

			var mydiv = $("#questionfieldset");
			//document.createElement("div"); 
			$(mydiv).empty();

			var legend = document.createElement("legend");
			legend.innerHTML = question.context;
			mydiv.append(legend);
			// mydiv.html("testesarasetaseaes");

			// mydiv.trigger('create');

			for (var i = 0; i < optionnum; i++)
			//for(var i=optionnum-1 ; i>=0;i--)
			{
				var option = question.options[i];

				var label = document.createElement("label");
				$(label).attr("for", "option_" + option.id);
				label.innerHTML = option.cntxt;

				mydiv.append(label);
				var input = document.createElement("input");
				input.id = "option_" + option.id;
				input.name = "question_" + OnGoingQtIndex;
				$(input).val(option.id);
				if (question.questionType.id == "1")
					$(input).attr("type", "radio");
				else if (question.questionType.id == "2")
					$(input).attr("type", "checkbox");

				mydiv.append(input);

			}
			// $("#questionfieldset").html(div.innerHTML);
			$(mydiv).trigger('create');

			for (var i = 0; i < currentAnswersheetQuestionOptionArray.length; i++) {
				$(
						"#"+ "option_"+ currentAnswersheetQuestionOptionArray[i].option.id)
						.attr('checked', true).checkboxradio("refresh");

			}
			

			

		}

		function nextquestion() {
			//保存当前题目
			savequestion();

			if (parseInt(OnGoingQtIndex) + 1 < totalnum) {
				OnGoingQtIndex = parseInt(OnGoingQtIndex) + 1;
				$('#index_show').html(
						"" + (parseInt(OnGoingQtIndex) + 1) + "/" + totalnum);
				loadQuestion(OnGoingQtIndex);
			} 
// 			else
// 				alert("已经是最后一题!");
		}

		function prevquestion() {
			if (parseInt(OnGoingQtIndex) > 0) {
				//保存当前题目
				savequestion();

				OnGoingQtIndex = parseInt(OnGoingQtIndex) - 1;
				$('#index_show').html("" + (parseInt(OnGoingQtIndex) + 1) + "/" + totalnum);
				loadQuestion(OnGoingQtIndex);
			} 
// 			else
// 				alert("已经是第一题!");

		}

		function savequestion() {
			var checkedOptions = $("input[name='question_" + OnGoingQtIndex
					+ "']:checked");
			currentAnswersheetQuestionOptionArray = new Array();
			for (var i = 0; i < checkedOptions.length; i++) {
				var option = new Object();
				option.id = checkedOptions[i].value;
				var answer = new Object();
				answer.option = option;
				currentAnswersheetQuestionOptionArray.push(answer);
			}
			currentAnswersheetQuestion.answerOptions = currentAnswersheetQuestionOptionArray;
			store.setItem("answersheet_" + answersheet.code, JSON.stringify(answersheet));

		}

		function test() {
			$("#option_33").attr('checked', true).checkboxradio("refresh");

			// 	alert("test");
			// 	var mydiv = $("#questionid");
			// 	mydiv.html("<legend>1.启初婴儿多效倍润面霜的卖点在哪里？（多选） </legend>");
			// 	alert(mydiv) 
		}

		function submitAnswerSheet() {
			
// 			if (parseInt(OnGoingQtIndex) + 1 < totalnum){
// 				$('#nextbtn1').button("enable");
// 				$('#nextbtn2').button("enable");
// 				}
// 			else{
// 				$('#nextbtn1').button("disable");
// 				$('#nextbtn2').button("disable");
// 				}
				
// 			if (parseInt(OnGoingQtIndex) == 0){
// 				$('#prevbtn1').button("disable");
// 				$('#prevbtn2').button("disable");
// 				}
// 			else{
// 				$('#prevbtn1').buttonn("enable");
// 				$('#prevbtn2').buttonn("enable");
// 				}				

			
			//保存当前题目
			savequestion();
			store.setItem("answersheetobjects", JSON
					.stringify(answersheetobjects));
			answersheetobjects = JSON
					.parse(store.getItem("answersheetobjects"));
			questionnairesmap[OnGoingQnID].answersheetobjects =answersheetobjects;
			
					if(questionnairesmap[OnGoingQnID].status==120)
						questionnairesmap[OnGoingQnID].status = 130;
					else if(questionnairesmap[OnGoingQnID].status==170)
						questionnairesmap[OnGoingQnID].status = 180;
			
			store.setItem("questionnairesmap", JSON.stringify(questionnairesmap));
			window.location="AnswerSheetList.jsp?t="+Math.round(Math.random()*(999999-100000)+100000);
		}
	</script>
	<div data-role="page" id="pageone" data-theme="b">
		<div data-role="header"  data-position="inline">
			<a href="#" id="prevbtn1" data-role="button" onclick="prevquestion();">上一题</a>
			<h1>
				<!--   1/10 剩余时间  -->
				<strong id="index_show">考试中</strong>

				<!--     <span id="day_show">0天</span> -->
				<!--     <strong id="hour_show">0时</strong> -->
				<strong id="minute_show"></strong> <strong id="second_show"></strong>
			</h1>
			<a href="#" id="nextbtn1" data-role="button" onclick="nextquestion();">下一题</a>
		</div>

		<div data-role="content" id=questionid>
			<fieldset data-role="controlgroup" id=questionfieldset></fieldset>

		</div>
		<div data-role="footer" data-position="inline" >
			<div ><font color="red">请不要点击浏览器的返回按钮，以免考试异常</font></div>
			<div>
			<a href="#" id="prevbtn2" data-role="button" onclick="prevquestion();">上一题</a>
			
			<a href="#" data-role="button" onclick="submitAnswerSheet()">提交试卷</a>
			
			<a href="#" id="nextbtn2" data-role="button" onclick="nextquestion();">下一题</a>
			</div>
		</div>

	</div>
</body>
</html>
