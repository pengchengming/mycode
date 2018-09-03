<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--表单头部  -->
	<div id="carts" style="display: none">
		<div>
			<h1 align="center">表单</h1>
			<div id="table_id"></div>
		</div>
	</div>


	<!-- 创建表单窗口-->
	<div id="create_form_dialog" style="display: none">
		<div>
			<form id="myForm" method="post">
				<div>
					表单名称: <input type="text" id="formName" name="formName" />
				</div>
				<div>
					表单描述: <input type="text" id="formId" name="formCode" />
				</div>
			</form>
		</div>
	</div>
	<!-- 显示所有表单信息 -->
	<div id="formShow" style="display: none">
		<div>
			<table cellspacing="0" style="width: 99.95%">
				<thead>
					<tr>
						<th>表单描述</th>
						<th>表单名称</th>
						<th>修改</th>
						<th>删除</th>
						<th>查看页面</th>
						<th>设计</th>
						<th>发布</th>
						<th>表单向导 </th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td colspan="9" id="getUserFoot"></td>
					</tr>
				</tfoot>
				<tbody id="formFindAll">
				</tbody>
			</table>
		</div>
	</div>

	<!-- template formShow view -->
	<p style="display: none">
		<textarea id="template_formFindAll" rows="0" cols="0">
			<!--
						{#foreach $T.formList as info}
						<tr>
							<td>{$T.info.formCode}</td>
							<td>{$T.info.formName}</td>
							<td><a href="#" onClick="Zform.updateForm(this)"  formId="{$T.info.id}">修改</a></td>
							<td><a href="#" onClick="Zform.deleteForm(this)" formId="{$T.info.id}">删除</a></td>
							{#if $T.info.formHtml =="" || $T.info.formHtml ==null}
							<td><a href="#" onClick="Zform.findForm(this);" formId="{$T.info.id}"></a></td>
							{#else}
							<td><a href="#" onClick="Zform.findForm(this);" formId="{$T.info.id}">查看</a></td>
							{#/if}
							<td><a href="#" onClick="Zform.deviseForm(this)" formCode="{$T.info.formName}"  formId="{$T.info.id}">设计</a></td>
							<td><a href="#" onClick="Zform.createTable(this)" formId="{$T.info.id}">发布</a></td>
							<td><a href="#"  onClick="Zform.formGuide(this,'{$T.info.formName}');"  formId="{$T.info.id}">表单向导  </a></td>
						</tr>
						{#/for}
		-->
		</textarea>
	</p>


	<!-- 录入表单信息 -->
	<div id="entering" style="display: none">
		<div>
			<table cellspacing="0" style="width: 99.95%">
				<thead>
					<tr>
						<th>表单编号</th>
						<th>表单名称</th>
						<th>录入信息</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td colspan="9"></td>
					</tr>
				</tfoot>
				<tbody id="enteringDatabase">
				</tbody>
			</table>
		</div>
	</div>


	<!-- template dataBaseShow view -->
	<p style="display: none">
		<textarea id="template_enteringDatabase" rows="0" cols="0">
			<!--
						{#foreach $T.formList as info}
						<tr>
							<td>{$T.info.formCode}</td>
							<td>{$T.info.formName}</td>
							<td><a href="#" class="enteringData" formId="{$T.info.id}">录入</a></td>
						</tr>
						{#/for}
		-->
		</textarea>
	</p>


	<!-- 显示数据信息 -->
	<div id="dataBaseShow" style="display: none">
		<div>
			<table cellspacing="0" style="width: 99.95%">
				<thead>
					<tr>
						<th>用户</th>
						<th>表单编号</th>
						<th>表单名称</th>
						<th>查询条件</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td colspan="9"></td>
					</tr>
				</tfoot>
				<tbody id="dataBaseFindAll">
				</tbody>
			</table>
		</div>
	</div>

	<!-- template dataBaseShow view -->
	<p style="display: none">
		<textarea id="template_dataBaseFindAll" rows="0" cols="0">
			<!--
				{#foreach $T.formList as infos}
						<tr>
							<td></td>
							<td class="dataBase" name="{$T.infos.id}">{$T.infos.formCode}</td>
							<td class="dataBase" name="{$T.infos.id}">{$T.infos.formName}</td>
							<td class="dataBase" name="{$T.infos.id}">
								<a href="#" onclick="tableData.showConditionSelect({$T.infos.id},'{$T.infos.formName}')">查询
							</td>
						</tr>
				{#/for}		
		-->
		</textarea>
	</p>



	<!-- 表单Html显示窗口-->
	<div id="show_html_dialog" style="display: none">
		<div id="formHtml"></div>
	</div>

<!--表单向导   字段选择-->
	<div id="show_form_guide" style="display: none">
		<div id="form_Guide"></div>
		<div id="form_Action">
			<div>请选择操作类型</div>
			<input type="checkbox" value="view" name="form_Action" onchange="Zform.viewFunction(this)"/>视图
			<input type="checkbox" value="update" name="form_Action"/>编辑
			<input type="checkbox" value="delete" name="form_Action"/>删除
		</div>
		<div id="viewRadio_div" style="display: none;">
			<input name="viewRadio" type="radio" url="jumpUrl/tableView.do">表单视图
			<input name="viewRadio" type="radio" url="jumpUrl/formView.do">表格视图
			<input name="viewRadio" type="radio" url="jumpUrl/formTableView.do" >表单和表格视图
		</div>
		
	</div>
<!--表单向导	 查询页面-->
	<div id="show_selectField_guide" style="display: none">
		<div id="selectPage">
			<div>
			   	<div id="selecthide_id" class="selecthide">查询页面</div>
			   	<div id="selectContent_id" class="selectContent" >
			   		
			   	</div>
			   	<div>
			   		<input id="tableName_id" type="hidden"/>
			   		<input type="button" value="查询" onclick="tableData.selectFieldData('#tableName_id','','#fieldContent_id')"> 
			   	</div>
			</div>
			<div>
			 	<div id="fieldhide" class="fieldhide">列表页面</div>
			 	<div id="fieldContent_id" class="field">
			 	</div>
			</div>
		</div>
	</div>
		
				
	
	<!-- 属性窗口-->
	<div id="formFieldDialog" style="display: none">
		<div id="emptyInput">
			<div>
				<input id="orreadonly" type="checkbox" />是否只读
			</div> 
			<div>
				数据类型: <select id="attrSelect" name="formTextSelect">
							<option value="attrString">字符串</option>
							<option value="attrInt">整型</option>
							<option value="attrFloat">浮点型</option>
							<option value="attrNumber">数字型</option>
							<option value="attrDate">日期</option>
						</select>
				</div>
			<div id="insert" style="float: right;">
				<input id="insertDataBase" type="checkbox" />插入数据集
			</div>
			<div>
				属性描述: <input id="comment" type="text" />
			</div>
			<div>
				属性名称: <input type="text" id="attrId" />
			</div>
			<div id="typeLength">
				属性长度: <input id="minlength" size="5" type="text" />~<input id="attrLength" type="text" size="5" />
			</div>
			<div id="rowandcol">
				<div id="typeLength">
					rows: <input id="rows" type="text" />
				</div>
				<div id="typeLength">
					cols: <input id="cols" type="text" />
				</div>
			</div>
			<div id="insetInto" style="display: none;">
				<select id="database_dactionary"></select>
				<select id="database_children"></select>
			</div>
			<div id="KeyValue" >
				<div style="float: left; width: 180px">数据值</div>
				<div style="float: none">显示值</div>
				<a href="#" id="addKey" style="float: right; width: 120px">添加</a>
				<div id="addValue">
					<div class="removeAdd" style="float: left; width: 180px">
						<input type="text" />
					</div>
					<div>
						<input type="text" />
					</div>
				</div>
			</div>
			<div>
				<input id="empty" type="checkbox" />是否非空
			</div> 
			<div>
				验证规则：<select id="validator">
							<option value="0">请选择</option>
							<option value="digital">数字</option>
							<option value="telephone">家庭电话</option>
							<option value="email">邮箱</option>
							<option value="phone">手机号码</option>
							<option value="letter">字母</option>
							<option value="letterNumber">字母和数字</option>
							<option>身份证号码</option>
							<option value="chinese">汉字</option>
							<option value="selected">必选项</option>
						</select>
			</div> 
			<div>
				<input id="bindingDate" type="checkbox" />是否绑定其他日期
			</div> 
				<select id="otherDate"></select>
			</div>
				<div>
					宽度设置:<input id="tdWidth" type="text" />
				</div>
				<div>
					高度设置:<input id="tdHeight" type="text" />
				</div>
				<div>
					背景设置:<input id="tdBackground" value="#ffffff" type="text" />
				</div>
				<div>
					边框设置:<input id="tdBorder" type="text" />
				</div>
	</div>
		
		
		<!-- 列表属性窗口-->
	<div id="listDialog" style="display: none">
			<div>
				列表名称: <input type="text" id="ListName" />
			</div>
	</div>

	<!-- 列表属性窗口-->
	<div id="listFieldDialog" style="display: none">
		<div id="listEmptyInput">
			<div>
				数据类型: <select id="listSelect" name="formTextSelect">
					<option value="attrString">字符串</option>
					<option value="attrInt">整型</option>
					<option value="attrFloat">浮点型</option>
					<option value="attrNumber">数字型</option>
				</select>
			</div>
			<div>
				表头名称: <input type="text" id="listName" />
			</div>
			<div>
				属性名称: <input type="text" id="listId" />
			</div>
			<div>
				属性长度: <input id="listLength" type="text" />
			</div>
		</div>
	</div>

	<!-- addValue 下拉框的值 -->
	<div id="KeyValueHtml" style="display: none;"></div>
	
	<!-- radio 模板  -->
	<p style="display: none">
		<textarea id="template_radio" rows="0" cols="0">
				<!--
				<div class='drag' type='options'>
				{#foreach $T.option as info}
					<input name='name_{$T.name}' type='radio' value='{$T.info.id}'/><span>{$T.info.name}</span>
				{#/for}
				</div>
				-->
			</textarea>
	</p>

	<!-- 下拉模板 -->
	<p style="display: none">
			<textarea id="template_select" rows="0" cols="0">
				<!--
				{#foreach $T.option as info bengin=0}
				<option value="{$T.info.code}" id="{$T.info.id}">{$T.info.name}</option>
				{#/for}
				-->
			</textarea>
	</p>

	<!-- 下拉模板 -->
	<p style="display: none">
			<textarea id="template_select1" rows="0" cols="0">
				<!--
				{#foreach $T.option as info bengin=0}
				<option value="{$T.info.id}">{$T.info.name}</option>
				{#/for}
				-->
			</textarea>
	</p>
	
	<!-- 多选模板 -->
	<p style="display: none">
		<textarea id="template_check" rows="0" cols="0">
				<!--
				<div class='drag' type='options'>
				{#foreach $T.option as info}
				<input name='name_{$T.name}' type='checkbox' value='{$T.info.id}'/><span>{$T.info.name}</span>
				{#/for}
				</div>
				-->
			</textarea>
	</p>

	<!--  选择框 -->
	<div id="options_dialog" style="display: none">
		<div>
			<table id="options_table">
				<tbody>
					<tr>
						<td>标签名称</td>
						<td><input id="options_label" type="text" />
						</td>		
						<td>描述</td>
						<td><input id="options_describe" type="text" />
						</td>
					</tr>
					<tr>
						<td>属性名称</td>
						<td><input id="options_id" type="text" />
						</td>
						<td>长度</td>
						<td><input id="options_length" type="text" />
						</td>
					</tr>
					<tr>
						<td>字段类型</td>
						<td><select id="option_select">
								<option value="radio">单选框</option>
								<option value="select">下拉框</option>
								<option value="checkbox">多选框</option>
						</select>
						</td>
						<td>选择项来源</td>
						<td><select>
								<option>多选择输入</option>
								<option>SQL查询</option>
								<option>取系统数据源</option>
								<option>数据传递</option>
						</select>
						</td>
					</tr>
					<tr id="Multiple">
						<td>多选值</td>
						<td colspan="3"><textarea id="Multiple_textarea" cols="30"
								rows="4">
							</textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!--动态生成列表 table  -->

		 <p style="display:none"><textarea id="template_list" rows="0" cols="0"><!--
		 <div class="drag">
		 <div>
		 <p style="display:none" class='pShow'><input type="button" class="createList" value="创建"><input class="deleteListRow" type="button" value="删除"></p>
				 <table border="1" id ="tabel_{$T.middleTableId}" type="lists" fieldid="list">
				 	<thead>
							<tr height="25px">
							<td width="80" ><input type="checkbox" class="listAll"/></td>
							{#foreach $T.tablePropertyList as info}
								<td width="300" ><span nameId="{$T.info.tablePropertyName}">{$T.info.tablePropertyLabel}</span></td>
							{#/for}
							</tr>
					</thead>
					<tbody id="tbody_{$T.middleTableId}">
					</tbody>		
				 </table>
			<div>	 
		</div>				
	--></textarea></p>
	
	
	
		<!-- 显示列表属性 -->
	<div id="listShow_dialog" style="display: none">
		<div>
			<table cellspacing="0" style="width: 99.95%">
				<thead>
					<tr>
						<th>列表名称</th>
						<th>属性名称</th>
						<th>类型</th>
						<th>长度</th>
						<th>操作</th>
						<th>是否显示</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td colspan="9" ></td>
					</tr>
				</tfoot>
				<tbody id="listFindAll">
				</tbody>
			</table>
		</div>
	</div>
	
	
	<!-- template listFindAll view -->
	<p style="display: none">
		<textarea id="template_listFindAll" rows="0" cols="0">
			<!--
						{#foreach $T.tablePropertyFields as info}
						<tr>
							<td>{$T.info.tablePropertyLabel}</td>
							<td>{$T.info.tablePropertyName}</td>
							<td>{$T.info.tablePropertyType}</td>
							<td>{$T.info.tablePropertyLength}</td>
							<td><a href="#" onClick="Zplugin.updateTableList(this)" tableId="{$T.middleTableId}" listId="{$T.info.tablePropertyId}">修改</a><a href="#" onClick="Zplugin.deleteTableList()" tableId="{$T.middleTableId}" listId="{$T.info.tablePropertyId}">删除</a></td>
							<td><input type="checkbox" checked="checked"/></td>
						</tr>
						{#/for}
		-->
		</textarea>
	</p>
	
	
	

	
	<!-- html画板-->
	<div id="template_html" style="display: none">
		<table id="selectable" border="1">
			<tr height="25px">
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px" class="connectedSortable">
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class="dome"></td>
				<td width="300" class="dome"></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class="dome"></td>
				<td width="300" class="dome"></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class="dome"></td>
				<td width="300" class="dome"></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class="dome"></td>
				<td width="300" class="dome"></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class="dome"></td>
				<td width="300" class="dome"></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class="dome"></td>
				<td width="300" class="dome"></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class="dome"></td>
				<td width="300" class="dome"></td>
				<td width="300" class='dome'></td>
				<td width="300" class='dome'></td>
			</tr>
			<tr height="25px">
				<td width="300" class="dome"></td>
				<td width="300" class="dome"></td>
				<td width="300" class='dome'></td>	
				<td width="300" class='dome'></td>
			</tr>
		</table>
	</div>
	
		<!-- 标签窗口-->
	<div id="formLabelDialog" style="display: none">
		<div>
		<textarea  id="labelDialog" style="width:100%;height:100%;"></textarea>
		</div>
		<div>背景颜色:<input type="text" id="labelBackground" value="#ffffff"/></div>
	</div>
	
	<!--数据字典窗口 -->
	<div id="dictionaryDialog" style="display: none">
			<div><span>代码类别:</span><select id="DataDictionaryType"></select><span>字典代码:</span><input type='text' id="dictionaryCode"  /><span>字典名称:</span><input id="dictionaryValue" type='text' /><input type='button' id='selectDictionary' value='查询'/><a href="#" id="add_dictionary">新增</a><a href="#" id="update_dictionary">修改</a><a href="#" id="delete_dictionary">删除</a></div>
	</div>

	<!-- 数据字典列表 -->
	<div id="dictionary_list" style="display: none">
		<div>
			<table cellspacing="0" style="width: 99.95%">
				<thead>
					<tr>
						<th>序号</th>
						<th>代码</th>
						<th>代码描述</th>
						<th>代码类别</th>
						<th>单选/多选</th>
						<th>允许维护	</th>
						<th>操作	</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td><input id="addCode" type='button' value="新增"/></td>
						<td colspan="7" ></td>
					</tr>
				</tfoot>
				<tbody id="listDictionary">
				</tbody>
			</table>
		</div>
	</div>
	
	<!-- template dictionary_list view -->
	<p style="display: none">
		<textarea id="template_dictionary_list" rows="0" cols="0">
			<!--
						{#foreach $T as info begin=0}
						<tr>
							<td>{$T.info$index + 1}</td>
							<td><a href="#" class="findWord" code="{$T.info.code}" listId="{$T.info.id}">{$T.info.code}</a></td>
							<td>{$T.info.display}</td>
							<td>{$T.info.dataDictionaryTypeValue}</td>
							{#if $T.info.type == "radio"}
							<td>单选</td>
							{#else}
							<td>多选</td>
							{#/if}
							{#if $T.info.isupdate == true}
							<td>允许</td>
							{#else}
							<td>禁止</td>
							{#/if}
							<td><a href="#" class="updateWord" listId="{$T.info.id}">编辑</a></td>
							<td><a href="#" class="deleteWord" listId="{$T.info.id}">删除</a></td>
						</tr>
						{#/for}
		-->
		</textarea>
	</p>
	
		<!-- 类别添加窗口-->
	<div id="create_dictionary_dialog" style="display: none">
		<div>
				<div>
					数据值: <input type="text" id="dictionary_code" />
				</div>
				<div>
					显示值: <input type="text" id="dictionary_name" />
				</div>
		</div>
	</div>	
	
			<!-- 代码添加窗口-->
	<div id="create_word_dialog" style="display: none">
		<div>
				<div>
					代码: <input type="text" id="word_code" />
				</div>
				<div>
					代码描述: <input type="text" id="word_name" />
				</div>
				<div>
					代码类别: <select id="word_select"></select>
				</div>
				<div>
					单选/多选: <select id="word_type">
					   				<option value="radio">单选</option>
					   				<option value="checkbox">多选</option>
							  </select>
				</div>
				<div>
					允许维护: <select id="isupdate">
							    	<option value="0">是</option>
					   				<option value="1">否</option>	
							 </select>
				</div>
		</div>
	</div>	
	
   <!-- 数据定义模板-->
  
   	<div id="value_list" style="display: none">
			<div>
				代码 :<span id="Codevalue"></span>
			</div>	
			<div>数据项定义</div>
			<table cellspacing="0" style="width: 99.95%">
				<thead>
					<tr>
						<th>序号</th>
						<th>数据值</th>
						<th>数据值描述</th>
						<th>序号</th>
						<th>所属机构</th>
						<th>操作</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td><input id="add_Value" type='button' value="新增"/></td>
						<td colspan="6" ></td>
					</tr>
				</tfoot>
				<tbody id="listValue">
				</tbody>
			</table>
	</div>	
	
		<!-- template DataDictionaryValue_list view -->
	<p style="display: none">
		<textarea id="template_value_list" rows="0" cols="0">
			<!--
					{#foreach $T as info begin=0}
						<tr>
							<td>{$T.info$index + 1}</td>
							<td>{$T.info.value}</td>
							<td>{$T.info.displayValue}</td>
							<td>{$T.info.ordinal}</td>
							<td>{$T.info.organization}</td>
							<td><a href="#" class="updateValue" listId="{$T.info.id}">编辑</a></td>
							<td><a href="#" class="deleteValue" listId="{$T.info.id}">删除</a></td>
						</tr>
						{#/for}
		-->
		</textarea>
	</p>
	
	
	
				<!--数据值添加窗口-->
	<div id="create_value_dialog" style="display: none">
		<div>
				<div>
					数据值: <input type="text" id="value_value" />
				</div>
				<div>
					数据值描述: <input type="text" id="value_displayValue" />
				</div>
				<div>
					序号: <input type="text" id="value_ordinal"/>
				</div>
				<div>
					所属机构：<input type="text" id="value_organization"/>
				</div>
			
		</div>
	</div>	

	
	
	<!--创建查询控件窗口  -->
	<div id="create_select_dialog" style="display: none">
		<div>
				<div>
					 绑定属性名称：<input type="text" id="Field_value" />
				</div>
				<div>
					选择查询表: <input type="text" id="select_Table"/>
				</div>
				<div>
					字段描述: 	<textarea  id="Field_display" style="width:100%;height:100%;"></textarea>
				</div>
		</div>
	</div>	
		<!--创建组织结构控件窗口  -->
	<div id="create_tree_dialog" style="display: none">
		<div>
				<div>
					 绑定属性名称：<input type="text" id="tree_value" />
				</div>
				<div>
					展现形式：<span><input type="radio" value="radio"  name="tree" checked='true' />单选</span><span><input type="radio"  name="tree"  value="true"/>节点单选</span><span><input type="radio" value="checkbox"  name="tree" />多选</span>
				</div>
				<div>
					字段描述: 	<textarea  id="tree_display" style="width:100%;height:100%;"></textarea>
				</div>
		</div>
	</div>	
		
	<!--展示组织结构窗口  -->
	<div id="show_tree_dialog" style="display: none">
		<div class="zTreeDemoBackground left">
			<ul id="treeDemo" class="ztree"></ul>
		</div>
	</div>
	
	<!--所有表列表窗口  -->
	<div id="table_list_dialog" style="display: none">
		<div id="tableList">
		</div>
	</div>
	
	<!--表单属性窗口  -->
	<div id="table_property_dialog" style="display: none">
		<div>
				<table>
					<tbody>
						<tr><td>表单宽度：<input type="text" id="tableWidth"/></td><td>表单高度：<input type="text" id="tableHeight" /></td></tr>
						<tr><td>表单行数：<input type="text" id="tableRows"/></td><td>表单列数：<input type="text" id="tableColumn"/></td></tr>
						<tr><td>表单背景:<input type="text" id="tableBackground" value="#ffffff"/></td><td>表单边框:<input id="tableBorder" type="text" /></td></tr>
					</tbody>
				</table>
		</div>
	</div>

<!--选择表单颜色窗口  -->
	<div id="select_tableColor_dialog" style="display: none" >
		<div id="pickerTable"></div>
	</div>	
<!--选择控件背景颜色窗口  -->
	<div id="select_tdColor_dialog" style="display: none" >
		<div id="pickerTd"></div>
	</div>

<!--选择控件背景颜色窗口  -->
	<div id="select_labelColor_dialog" style="display: none" >
		<div id="pickerLabel"></div>	
	</div>
 
	<!-- 创建表单查询信息 -->
	<div id="select_from_template" style="display: none">
		<div>
		<table cellspacing="0" style="width: 99.95%">
			<tr>
				<td >显示字段：</td>　
			</tr>
			<tr>
				<td ><div id="selectshowList_id"> </div></td>
			</tr>
			<tr>
				<td  >关系： <a href="#" onclick="Zplugin.addTableRelations()"> 增加</a> </td>
			</tr>
			<tr>
				<td  > 
					<table> 
						<tr> 
							<td>左表</td>
							<td>关系</td>
							<td>右表</td>
							<td>关联关系</td>
							<td>删除</td> 
						 </tr>
						 <tr class="selectConnection_tr">
						 	<td><select class="selectLeft_form"></select> </td>
							<td>
								<select class="selectConnection_form">
									<option value="">请选择</option>
									<option value=" let join ">左连接</option>
									<option value=" right join ">右连接</option>
									<option value=" inner join ">内连接</option>
									<option value=" out join ">全连接</option>
								</select> 
							</td>
							<td><select class="selectRight_form"></select> </td>
							<td><div class="selectLCR_form"></div> </td>
							<td class="deletetr"></td>
						 </tr>
				 	</table>
				 </td>
			</tr>
			<tr>
				<td>查询条件</td>
			</tr>
			<tr>
				<td>
					<table id="selectable_form" border="1">
						<tr height="25px">
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
						</tr>
						<tr height="25px" class="connectedSortable">
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
						</tr>
						<tr height="25px">
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
						</tr>
						<tr height="25px">
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
						</tr>
						<tr height="25px">
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
						</tr>
						<tr height="25px">
							<td width="300" class="dome"></td>
							<td width="300" class="dome"></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
						</tr>
						<tr height="25px">
							<td width="300" class="dome"></td>
							<td width="300" class="dome"></td>
							<td width="300" class='dome'></td>
							<td width="300" class='dome'></td>
						</tr> 
					</table>
				</td>
			</tr>
		</table>
		
			 
		</div>
	</div>
	