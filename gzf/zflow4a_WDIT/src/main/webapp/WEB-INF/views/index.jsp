<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form/jquery-ui-1.8.17.custom.css" />"/>
<%@ include file="/WEB-INF/views/commonCss.jsp" %>
<%@ include file="/WEB-INF/views/commonJs4Dragable.jsp" %> 
<script type="text/javascript" src="<c:url value="/script/ui.js"/>"></script>
<script type="text/javascript" src="<c:url value="/script/jquery-ex.js"/>"></script>
<script type="text/javascript">
var ctx=null;
var searchForm=null;
var table1=null;
var table2=null;
var addForm;
var initTreeData = [
//                 	{acronym : "SVW", name: "上海大众"}
                ];
                
var orgTreeSetting = {
		data : {
			simpleData : {
				enable : true,
				pIdKey : "parentId"
			}
		},
		async : {
			type : "post",
			enable : true,
			url : '<c:url value="/organizations/orgTreeSetting"/>',
			autoParam : ["acronym"]
		},
		callback : {
			onAsyncSuccess : function(event, treeId, treeNode, msg){
				if(!treeNode){
					msg = $.evalJSON(msg);
					$(".banner-title").html(msg.name);
				}
			},
			onClick : function(event, treeId, node){
				$("#organizationId").val(node.id);
				selectClick();
			}
		}
};

function getConfig(){
	var orgSource = new ui.selectProxy({//动态获取MediaType的类型
		url : '<c:url value="/organizations/getBySelectProxy.do"/>?' + new Date(),
		paramType : "static"
	});
	var config = {
			fields:[{
				title : "上级机构",
				data : "parent",
				type : "singleSelect",
				valueField : "id",
				displayField : "name",
				dataSource : {proxy : orgSource}
//	 		},{
//	 			title : "单位类型",
//	 			data : "type",
//	 			type : "singleSelect",
//	 			valueField : "id",
//	 			displayField : "name",
//	 			dataSource : {
//	 				'PRODUCE' : '生产车间',
//	 				'ASSISTPRODUCE' : '辅助生产',
//	 				'OUTSOURCING' : '外包厂家'
//	 			}
			}]
		};
	return config;
}
/*
 * 画出的表格的表头
 */
var tableConfig = {
	fields : [{
		title : "序号",
		data : "lineNo",
		type : "indexRender"
	},{
		title:"单位名称",
		data:"name"
	},{
		title : "单位描述",
		data : "description"
	},{
		title : "单位简称",
		data : "acronym"
	},{
		title : "单位排序编号",
		data : "level"
	},{
		title : "操作",
		data : "details",
		type : "cutsomerRender",
		doRender:function(data, container, config, rowIndex){
			var data = this.parent.data;
			var htmlElement = document.createElement("a");
			$(htmlElement).attr("href", "javascript:showUpdateWin(" + data["id"] + ");");
			$(htmlElement).attr("title","修改"); 
			$(htmlElement).html("修改");
			$(htmlElement).addClass("ui-input-link");
			$(container).append(htmlElement);
			$(container).addClass("ui-input-container");
		}
	},{
		title : "操作",
		data : "details",
		type : "cutsomerRender",
		doRender:function(data, container,config, rowIndex){
			var data = this.parent.data;
			var htmlElement = document.createElement("a");
			$(htmlElement).attr("href", "javascript:showDeleteWin(" + data["id"] + ");");
			$(htmlElement).attr("title", "删除");
			$(htmlElement).html("删除");
			$(htmlElement).addClass("ui-input-link");
			$(container).append(htmlElement);
			$(container).addClass("ui-input-container");	
		}
	}]
};

$(function() {
	caculateMiddleSize();	
// 	jQuery('#head_menu').superfish({delay : 200});
	$.fn.zTree.init($("#org_tree"), orgTreeSetting, initTreeData);
	selectClick();
	$(".left-menu-bar").css("background-color","gray");
	$(".left-menu-bar").draggable({
		containment: [171,20,324,460],
		scope: 'tasks',
		axis: 'x',
		cursor: 'crosshair',
		start: function (event, ui) {
			x0 = event.pageX;
		},
		stop: function(event, ui) {
			var barX = $(".left-menu-bar")[0].offsetLeft;
			var main_old_width =parseInt($(".left-menu-main").css("width"));
			var main_new_width = barX-2;
			var xc = main_new_width-main_old_width;
			$(".left-menu-title").css("width", parseInt($(".left-menu-title").css("width"))+xc);
			$(".left-menu-main").css("width", main_new_width);
			$("#org_tree").css("width", parseInt($("#org_tree").css("width"))+xc);
			$(".scroll").css("width", parseInt($(".scroll").css("width"))-xc);
		}
	});
});

//查询
function selectClick(){
	if (ui.currentController)
		ui.currentController.confirm();
	$.ajax({
		url : '<c:url value="/organizations/getByNameOrParentId.do"/>',
        type : "POST",
        data : {
        	parentId : $("#organizationId").val(),
        	name : $.trim($("#_queryname").val())
		}, 
		complete : function(xhr, textStatus){
			var data = $.evalJSON(xhr.responseText);
	   		table1 = ui.registTable($("#data_table"), tableConfig, data);	 
		}
	});
}//打开新增窗口
function insertClick(){
		if (ui.currentController)
			ui.currentController.confirm();
		var parentId = $.trim($("#organizationId").val());
		$("#_id").val("");
	    $("#_name").val("");
   		$("#_address").val("");
	    $("#_acronym").val("");
		$("#_description").val("");
	    $("#_level").val("");
// 	    $("#_financeCode").val("");
// 	    $("#_financeAcronym").val("");
// 	    addForm.subController("type").refreshData("PRODUCE");
		addForm = ui.registInputContext($("#_addForm"), getConfig(), null);
	    addForm.subController("parent").refreshData("0");
  		openWin("window_details", {width:300});
};

//打开修改窗口
function showUpdateWin(id){
	$.ajax({
		url : '<c:url value="/organizations/getById"/>',
		type : "POST",
		dataType : "json",
		data : {
			id : id
		},
		complete:function(xhr,textStatus){
			var data = $.evalJSON(xhr.responseText);
		    $("#_id").val(data.id);
		    $("#_name").val(data.name);
		    $("#_level").val(data.level);
// 	   		$("#_address").val(data.address);
		    $("#_acronym").val(data.acronym);
			$("#_description").val(data.description);
// 			$("#_financeCode").val(data.financeCode);
// 			$("#_financeAcronym").val(data.financeAcronym);
// 			if(null == data.type || '' == data.type){
// 				addForm.subController("type").refreshData("PRODUCE");
// 			}else{
// 				if(0 == data.type)
// 					addForm.subController("type").refreshData("PRODUCE");
// 				else if(1 == data.type)
// 					addForm.subController("type").refreshData('ASSISTPRODUCE');
// 				else if(2 == data.type)
// 					addForm.subController("type").refreshData('OUTSOURCING');
// 			}
			var addForm = ui.registInputContext($("#_addForm"), getConfig(), null);
			if(null != data.parentOrganization)
				addForm.subController("parent").refreshData(data.parentOrganization.id);
			else
				addForm.subController("parent").refreshData(0);
			openWin("window_details",{width:300});
		}
	});
}
//删除
function showDeleteWin(id){
	if(confirm("确定要删除吗?")){
		$.ajax({
			url : "<c:url value='/organizations/" + id + "'/>",
			type : "DELETE",
			data : {
				id : id
			},
			complete:function(xhr,textStatus){
				var data = $.evalJSON(xhr.responseText);
				if(data["successMsg"]){
					alert(data["successMsg"]);
					$.fn.zTree.init($("#org_tree"), orgTreeSetting, initTreeData);
		 			selectClick();
				}else if(data["errorMsg"]){
					alert(data["errorMsg"]);
				}
			}
		});
	}
}

//新增和修改的保存
function save(){
	var name = $.trim($("#_name").val());
	if(null == name || "" == name){
		alert("单位名称不能为空!");
		return false;
	}
	var address = $.trim($("#_address").val());
	var acronym = $.trim($("#_acronym").val());
	if(null == acronym || "" == acronym){
		alert("单位简称不能为空!");
		return false;
	}
	var description = $.trim($("#_description").val());
	var level = $.trim($("#_level").val());
	if(null == level || "" == level){
		alert("排序编号不能为空!");
		return false;
	}else{
		var pattern = /^[1-9]\d*$/;
        if(!pattern.test(level)){
	         alert("排序编号必须是正整数!");
	         return false;
	    }
	}
// 	var type = addForm.subController("type").data;
	var parent = addForm.subController("parent").data;
	parent = '0' == parent ? null : parent;
	$.ajax({
		url : '<c:url value="/organizations/create"/>',
		type : "POST",
		dataType : "json",      
        contentType : "application/json",               
		data : JSON.stringify({
			id : $("#_id").val(),
			name : name,
			nlevel : level,
			parentOrganization : {id : parent},
			acronym : acronym,
			description : description}),
		complete : function(xhr, textStatus){
			var data = $.evalJSON(xhr.responseText);
			if(data["successMsg"]){
				alert(data["successMsg"]);
				$.fn.zTree.init($("#org_tree"), orgTreeSetting, initTreeData);
	 			selectClick();
			    javascript:closeWin('window_details');
			}else if(data["errorMsg"]){
				alert(data["errorMsg"]);
			}
		}
	});
}
</script> 
<title>组织架构</title>
</head>
<body>
<div class="content">
	<%@ include file="/head2.jsp"%>
		<div class="middle">
		</div>
		<div class="foot">
			<span style="font-family:Verdana;margin-right:10px;">Version 1.0</span>
		</div>
	</div>
</body>
</html>