<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.top {
	width: 80%;
	height: 10%;
	margin-left: 10%;
	margin-right: 10%;
	margin-bottom: 5%;
}

body {
	font-size: 12px;
}

td {
	text-align: center;
}	
tr {
border:2px solid #DFDFDF;

}
#tb {
	margin-left: 15%;
	width: 50%;
	border:2px solid #DFDFDF;
	cellpadding: 0;
	cellspacing: 0;
}

td input[type='text'] {
	width: 90%;
}


</style>

<script type="text/javascript">
	var index = 1;
	function btnclick() {
		var flag=1;
		var node=document.getElementsByName("description");
		for(var i=0;i<node.length;i++){
			if(node[i].value==""){
				var j=i+1;
				flag=0;
				alert("请把第 "+j+"项文件描述信息填写好 ");
			}
		}
		if(flag==1){
			var form = document.forms[0];
			form.action = "/PcmBlog/file/batch ";
			form.method="post";
			form.submit();
		}
		
	}

	function addtr() //添加行
	{
		var tb = document.getElementById("tb");
		//alert(tb.innerHTML);
		var row, cell;
		//alert(tb.rows.length);
		row = tb.insertRow(tb.rows.length); //表示在表格末尾添加一行，insertRow方法的参数表示添加的位置
		if (row != null) {
			index++;
			cell = row.insertCell(0);
			cell.innerHTML = index;

			cell = row.insertCell(1);
			cell.innerHTML = "<input type='checkbox'/>";

			cell = row.insertCell(2);
			cell.innerHTML = " <input name='file' type='file' />";

			cell = row.insertCell(3);
			cell.innerHTML = " <input name='description' type='text' />  ";

			cell = row.insertCell(4);
			cell.innerHTML = "<a href='#' onclick='deleteitem(this)'>删除</a>";
		}
	}

	function selectall() {
		var chk = document.getElementById("Checkbox1");
		var inputs = document.getElementsByTagName("input");
		for ( var i = 0; i < inputs.length; i++) {
			if (inputs[i].type == "checkbox") {
				inputs[i].checked = chk.checked;
			}
		}
	}

	function deleteitem(obj) {
		var curRow = obj.parentNode.parentNode; //得到当前要删除的行
		if (confirm("确认要删除该行吗？")) {
			index--;
			curRow.parentNode.removeChild(curRow);
		}
		//解决序号的问题
		var tb = document.getElementById("tb");
		for ( var i = 1; i < tb.rows.length; i++) {
			tb.rows[i].cells[0].innerHTML = i;
		}
	}

	function deleteselect() {
		var tb = document.getElementById("tb");
		if (confirm("确认要删选中行吗？")) {
			for ( var i = tb.rows.length - 1; i > 0; i--) {
				if (tb.rows[i].cells[1].firstChild.checked) {
					tb.deleteRow(i);
					index--;
				}
			}
		}

		//解决序号的问题
		var tb = document.getElementById("tb");
		for ( var i = 1; i < tb.rows.length; i++) {
			tb.rows[i].cells[0].innerHTML = i;
		}
	}
</script>

</head>
<body>
<div class="top"><jsp:include page="top.jsp" flush="true" /></div>
<form action="" enctype="multipart/form-data" >
<table id="tb">
        <tr id="ftr">
            <th id="fth">序号</th>
            <th>
                <input id="Checkbox1" type="checkbox" onclick="selectall()"/>
            </th>
            <th>文件</th>
            <th>文件描述</th>
            <th>操作</th>            
        </tr>
        
        <tr>
            <td>1</td>
            <td><input id="Checkbox2" type="checkbox" /></td>
            <td>
                <input name="file" type="file" />
            </td>
            <td>
                <input name="description" type="text" />                
            </td>
            <td>
                <a href="#" onclick="deleteitem(this)">删除</a>
            </td>
        </tr>         
    </table>  
  </form>  
    <div align="center" style="margin-top: 3%;">
     <input id="Button1" type="button" value="上传" onclick="btnclick()" />
        <input id="btn" type="button" value="添加" onclick="addtr()" />
        <input id="btndel" type="button" value="删除选中项" onclick="deleteselect()"/>
       
    </div>
    

    
</body>
</html>
