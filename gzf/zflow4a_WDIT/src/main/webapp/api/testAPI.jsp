<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/commonCss.jsp"%>
<%@ include file="/page/common/commonJs.jsp"%>
<%@ include file="/page/common/formJs.jsp" %>
	 
<script>
    $(function(){
        $("#main_id").find("select").hide(); 
        $("#selectType").show();
        $("#selectType").val(0);
    });

	function selectTypeFun(me){

        $("#main_id").find("select").hide(); 
        $("#selectType").show();
        var value = $(me).val(); 
        switch(value){
	        case '1' : 
	            $("#loginType").show();
	            break;
	        case '2' : 
	        	$("#taskType").show();
	        	break; 
	        case '3' : 
	        	$("#piplineType").show();
	        	break;
        }
    }

	function loginTypeFun(me){
        var value = $(me).val(); 
        if(value == 1){
        	$("#url_id").val(rootPath+"/api/login.htm"); 
          	$("#param_id").val("username=admin&password=111111&IMEI=125678254596&MODEL=10.1.1.1&versionName=1.10");
        } else if(value==2){
        	$("#url_id").val(rootPath+"/api/changePassword.htm"); 
          	$("#param_id").val("userName=Na_Chi11&oldpassword=111111&password=123456");
        }
    }
	function taskTypeFun(me){
		var value = $(me).val(); 
        if(value == 1){
          	$("#url_id").val(rootPath+"/api/tasklist.htm"); 
  		   	$("#param_id").val("sessionID=c093f508-7f9c-4c42-9dc6-0c2bc67b7909&pageSize=10&pageNo=1");
        }else if(value == 2){
        	$("#url_id").val(rootPath+"/api/tasknum.htm"); 
  		   	$("#param_id").val("sessionID=103acad4-1b8e-4037-955b-d1e18a8c2e18");
        }else if(value == 3){
        	$("#url_id").val(rootPath+"api/listCalls.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017&pageSize=10&pageNo=1&type=2,5");
        }else if(value == 4){
        	$("#url_id").val(rootPath+"api/readtask.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017&noticeId=49");
        }else if(value == 5){
        	$("#url_id").val(rootPath+"api/taskdetail.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017&noticeId=49");
        }else if(value == 6){
        	$("#url_id").val(rootPath+"/api/accept.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017&noticeId=49");
        } else if(value == 7){
        	$("#url_id").val(rootPath+"/api/ppcomment.htm"); 
  		   	$("#param_id").val("sessionID=d15cc488-0d6f-4a4c-8c49-b08301f331c7&noticeId=76&comment=adbc");
        } else if(value == 8){
        	$("#url_id").val(rootPath+"api/readComment.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017&noticeId=126");
        }else if(value == 9){
        	$("#url_id").val(rootPath+"api/ppfeedback.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017&noticeId=76&itemId=4&itemText=Please specify here if needed&remark=123ddd&note=11111");
        }else if(value == 10){
        	$("#url_id").val(rootPath+"api/reportMonth.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017");
        }else if(value == 11){
        	$("#url_id").val(rootPath+"/api/reportWeek.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017");
        } else if(value == 12){
        	$("#url_id").val(rootPath+"api/remind.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017&pageSize=10&pageNo=1");
        } else if(value == 13){
        	$("#url_id").val(rootPath+"api/callTrack.htm"); 
  		   	$("#param_id").val("sessionID=06f855a4-aab9-4519-890e-7e4b0c4ce017&noticeId=304");
        } 
	}
	function  piplineTypeFun(me){
		var value = $(me).val(); 
        if(value == 1){
          	$("#url_id").val(rootPath+"apiPipline/pipelinelist.htm"); 
  		   	$("#param_id").val("sessionID=d15cc488-0d6f-4a4c-8c49-b08301f331c7");
        }else if(value == 2){
          	$("#url_id").val(rootPath+"apiPipline/pipelinedetail.htm"); 
  		   	$("#param_id").val("sessionID=d15cc488-0d6f-4a4c-8c49-b08301f331c7&dataID=1"); 
        }else if(value == 3){
          	$("#url_id").val(rootPath+"apiPipline/almostconfirmedlist.htm"); 
  		   	$("#param_id").val("sessionID=d15cc488-0d6f-4a4c-8c49-b08301f331c7"); 
        }else if(value == 4){
          	$("#url_id").val(rootPath+"apiPipline/almostconfirmeddetail.htm"); 
  		   	$("#param_id").val("sessionID=d15cc488-0d6f-4a4c-8c49-b08301f331c7&dataID=1"); 
        }
	}
	
    function submitTest(){
        var url = $("#url_id").val();
        var actionParm = $("#action_id").val();
        var param_id = $("#param_id").val();
        var param = {}; 
        var array = param_id.split("&");
        $.each(array, function(i, str){
        	var _array = str.split("=");
        	var paramName = _array[0];
        	var paramVal= _array[1];
        	param[paramName] = paramVal;
        });
 
       $.post(
       		url,
       		param,
			function(data){   
				var dataJson = $.toJSON(data);
	            $("#returnDate_id").html(dataJson);
		});
    }
</script>

<html>
<head  >
    <title></title>
</head>
<body> 
    <div id="main_id">
        <h2>调试工具</h2>
		<select id="selectType" onchange="selectTypeFun(this)">
			<option value="0" >请选择</option>
			<option value="1">登录</option>
			<option value="2">getTask</option>
			<option value="3">apiPipline</option>
		</select> 
		<select id="loginType" onchange="loginTypeFun(this)">
			<option>请选择</option>
			<option value="1">登录校验</option>
			<option value="2">修改密码</option>
		</select>
		<select id="taskType" onchange="taskTypeFun(this)">
			<option>请选择</option>
			<option value="1">getTask</option>
			<option value="2">tasknum 统计</option>
			<option value="3">listCalls </option>
			<option value="4">readtask 已读</option>
			<option value="5">taskdetail 明细</option>
			<option value="6">accept 认领</option>
			<option value="7">ppcomment help</option>
			<option value="8">readComment 读取help </option>
			<option value="9">ppfeedback 反馈</option> 
			<option value="10">reportMonth 月份报表</option>
			<option value="11">reportWeek 周报表</option> 
			<option value="12">remind</option> 
			<option value="13"> Call 流程显示跟踪</option> 
		</select>
		<select id="piplineType" onchange="piplineTypeFun(this)">
			<option value="0">请选择 </option>
			<option value="1">ConfirmList </option>
			<option value="2">ConfirmDetail </option>
			<option value="3">TBCList </option>
			<option value="4">TBCDetail </option>
		</select>
		
		<table>
            <tr>
                <th>URl:</th>
                <td><input id="url_id" style="width :500px" /></td>
            </tr> 
            <tr>
                <th>参数：</th>
                <td>
                    <textarea id="param_id" cols="60" rows="6"></textarea>
                </td>
            </tr>
            <tr>
                <td><input type=button value='提交' onclick="submitTest()"></td>
            </tr>
        </table>
        返回结果：
        <div id="returnDate_id"></div>
        
    </div> 
</body>
</html>