	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<!-- 表单工具 -->
					<div id="toolHtml" style="display: none">
						<font style="color: #666; font-size: 14px; font-family: 'Arial'">
								<input type="button" onClick="Zform.createHtml()" value="保存" />
								<input type="button" id="changMerge" value="启动合并单元格" />
								<input type="button"id="merge" value="合并" /> 
								<input type="button" id="changeHinht" value="启动改变行高" />
								<input type="button" id="addRows" value="添加行" onClick="Zform.addLine()" />
								<input type="button" id="addCols" value="添加列" />
								<input type="button" onclick ="Zform.updateTableStyle()" value="表单属性" />
								是否保存为模版<input type="checkbox" id="isTemplate" value="是否保存为模版" > 
						</font>
					</div>
					<div id="selectTable_tool" style="display: none">
						<font style="color: #666; font-size: 14px; font-family: 'Arial'">
							<input type="button" onClick="Zform.saveSelectTable()" value="保存" />
						</font>
					</div>