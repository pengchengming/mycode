<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" >
	var rootPath = "<c:url value="/" />";
</script>

<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-1.7.1.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-ex.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery.form.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/ext/3.2.1/adapter/jquery/ext-jquery-adapter.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/ext/3.2.1/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.core.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.widget.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.position.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.autocomplete.js" />"></script>

<script type="text/javascript" src="<c:url value="/javascript/ui.js"/>"></script>
<!-- 
<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.core-3.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.excheck-3.0.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.exedit-3.0.js"/>"></script>
 -->
<script type="text/javascript" src="<c:url value="/js/url.js"/>"></script>


<script type="text/javascript" src="<c:url value="/javascript/js/jquery.getAttributes.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.accordion.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.mouse.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.droppable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.draggable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.dialog.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.resizable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.position.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery-jtemplates.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.datepicker.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/jquery.ui.selectable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/icalendar.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/xheditor-1.1.13/xheditor-1.1.13-zh-cn.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/prototype.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/selectTable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/ajaxfileupload.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/js/farbtastic.js"/>"></script>

<script type="text/javascript" src="<c:url value="/javascript/formJs/workspace.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/formJs/createTable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/formJs/tableData.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/formJs/orgTreeNode.js"/>"></script>
<%
   String p = request.getContextPath();
%>
<script type="text/javascript">

Date.prototype.format = function(format) {  
    /* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */  
    var o = {  
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" : this.getMilliseconds()  
        // millisecond  
    };  
  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
                        - RegExp.$1.length));  
    }  
  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1  
                            ? o[k]  
                            : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
};

 	var dataJSON={};//表单向导用到的对象
	var elementType;
	var fieldid;
function headpage(id){
	$("#" + id).addClass("current");
}

//查看所有类别
function findDictionaryList(id){
	$.ajax({
		url : Zflow.url.dataDictionaryTypeList,
		type : "POST",
		complete : function(xhr, textStatus) {
					var data = $.evalJSON(xhr.responseText);
					DataDictionaryType = data;
					$("#"+id).setTemplateElement("template_select");
					$("#"+id).processTemplate(data);
					if(DataDictionaryType.option.length == 0){
						alert("代码类别暂未数据,请添加！");
					}
					if(id == "database_dactionary")
					 dataDictionaryCodeList($("#database_dactionary").val(),"database_children");
					 $("#database_dactionary").unbind("change");
					 $("#database_dactionary").change(function(){
						 dataDictionaryCodeList($("#database_dactionary").val(),"database_children");
					 });
		}
	});
};
//查看代码
function dataDictionaryCodeList(code,id){
	$.ajax({
		url : Zflow.url.dataDictionaryCodeList,
		type : "POST",
		data:{
			code :code,
			dictionaryCode:$("#dictionaryCode").val(),
			dictionaryValue:$("#dictionaryValue").val()
		},
		complete : function(xhr, textStatus) {
			var data = $.evalJSON(xhr.responseText);
			if(data.length == 0){
				alert("未查到相关联数据!")
			}
			if(data.successMsg){
				alert(data.successMsg);
			}
			if(data.errorMsg){
				alert(data.errorMsg);
			}
			if(id == "listDictionary"){
				$("#listDictionary").setTemplateElement("template_dictionary_list");
				$("#listDictionary").processTemplate(data);
			}
			if(id == "database_children"){
				var option = new Array();
				$.each(data,function(i,n){
					option.push({code:n.code,name:n.display});
				});
				stringJson.option = option;
				$("#"+id).setTemplateElement("template_select");
				$("#"+id).processTemplate(stringJson);
			}
		}
	});
};
function dataDictionaryValueList(code,id){
	$.ajax({
		url : Zflow.url.dataDictionaryValueList,
		type : "POST",
		data:{
			code :code
		},
		complete : function(xhr, textStatus) {
			var data = $.evalJSON(xhr.responseText);
			if(data.successMsg){
				alert(data.successMsg);
			}
			if(data.errorMsg){
				alert(data.errorMsg);
			}
			if(id =="listValue"){
				$("#listValue").setTemplateElement("template_value_list");
				$("#listValue").processTemplate(data);
				$("#Codevalue").text(codeCode);
				$("#value_list").dialog({
					width:"750",
					title:"操作",
					position:'center',
					buttons: {
						"关闭": function() {
							$(this).dialog("close");
						}
					},
					modal: true
				});
			}else{
				var option = new Array();
				$.each(data,function(i,n){
					option.push({code:n.dataDictionaryCode,name:n.displayValue});
				});
				dictionvalue.option = option;
			}
		}
	});
}
</script>
