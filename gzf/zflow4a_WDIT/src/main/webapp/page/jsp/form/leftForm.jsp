	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<div class="thin_title">
					<font style="color: #fff; font-size: 14px; font-family: 'Arial'">
						Composer Assistant </font>
				</div>
				<div class="left_menu_inner">
					<div style="height: 700px; background: #eee;">
						<div id="accordion">
							<h3>
								<a href="#section1">基础数据</a>
							</h3>
							<div>
								<ul>
									<div id="form_dictionary">数据字典</div>
								</ul>
							</div>
							<h3>
								<a href="#section2">自定义表单</a>
							</h3>
							<div>
								<ul>
									<div id="create_form" onClick="Zform.createFormDialog();">新建表单</div>
									<div id="find_form">显示所有表单</div>
									<div id="select_form" onclick="Zform.selectTableLeftFun()">新建查询</div> 
								</ul>
							</div>
							<h3>
								<a href="#section3">数据交互</a>
							</h3>
							<div>
								<ul>
									<div onClick="tableData.enteringDatabase()">录入信息</div>
									<div onClick="tableData.findDatabase()">显示录入信息</div>
								</ul>
							</div>
							<h3>
								<a href="#section4">系统初始化</a>
							</h3>
							<div>
								<ul>
									<div onClick="system.module()">模块</div>
									<div onClick="system.action()">操作</div>
									<div onClick="system.resource()">资源</div>
									<div onClick="system.menuitem()">菜单</div>
									<div onClick="system.permission()">权限定义</div>
								</ul>
							</div>
							<h3>
								<a href="#section5">系统管理</a>
							</h3>
							<div>
								<ul>
									<div onClick="system.role()">角色</div>
									<div onClick="system.user()">人员</div>
									<div onClick="system.organization()">组织架构</div>
								</ul>
							</div>
						</div>
					</div>
				</div>