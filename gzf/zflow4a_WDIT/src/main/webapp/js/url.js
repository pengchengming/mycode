var Zflow = {
	interval_id : null,
	loadingFlag : 0
};


Zflow.url = {
		//页面查看
		tableViewSelect :rootPath + "tableView/tableViewSelect.do",
		/**数据字典***/
		dataDictionaryTypeList:rootPath + "dictionarys/dataDictionaryTypeList.do",
		dictionaryTypeById:rootPath + "dictionarys/dictionaryTypeById.do",
		addOrUpdateDictionaryType :rootPath + "dictionarys/addOrUpdateDictionaryType.do",
		deleteDictionaryType:rootPath+"dictionarys/deleteDictionaryType.do",
		dataDictionaryCodeList:rootPath+"dictionarys/dataDictionaryCodeList.do",
		dictionaryCodeById:rootPath+"dictionarys/dictionaryCodeById.do",
		addOrUpdateDictionaryCode:rootPath+"dictionarys/addOrUpdateDictionaryCode.do",
		deleteDictionaryCode:rootPath+"dictionarys/deleteDictionaryCode.do",
		dictionaryValueById:rootPath+"dictionarys/dictionaryValueById.do",
		dataDictionaryValueList:rootPath+"dictionarys/dataDictionaryValueList.do",
		addOrUpdateDictionaryValue:rootPath+"dictionarys/addOrUpdateDictionaryValue.do",
		deleteDictionaryValue:rootPath+"dictionarys/deleteDictionaryValue.do",
		/*************zflow  系统初始化************************************/
		templateModule : rootPath + "module/template.do",
		createModule : rootPath + "module/create.do",
		updateModule : rootPath + "module/update.do",
		listModule : rootPath + "module/list.do",
		selectModule : rootPath + "module/select.do",
		showModule : rootPath + "module/show.do",
		deleteModule : rootPath + "module/",
		
		templateResource : rootPath + "resource/template.do",
		createResource : rootPath + "resource/create.do",
		updateResource : rootPath + "resource/update.do",
		listResource : rootPath + "resource/list.do",
		showResource : rootPath + "resource/show.do",
		deleteResource : rootPath + "resource/",
		
		templateAction : rootPath + "action/template.do",
		createAction : rootPath + "action/create.do",
		updateAction : rootPath + "action/create.do",
		listAction : rootPath + "action/list.do",
		showAction : rootPath + "action/show.do",
		deleteAction : rootPath + "action/",
		
		templateMenuItem : rootPath + "menuitem/template.do",
		createMenuItem : rootPath + "menuitem/create.do",
		updateMenu : rootPath + "menuitem/update.do",
		listMenu : rootPath + "menuitem/list.do",
		showMenu : rootPath + "menuitem/show.do",
		deleteMenu : rootPath + "menuitem/",
		
		templatePermission : rootPath + "permission/template.do",
		listPermission : rootPath + "permission/list.do",
		listPermissionByT : rootPath + "permission/listByT.do",
		savePermission : rootPath + "permission/save.do",
//		menuPermission : rootPath + "permissions/menuPermisList.do",
//		rolePermission : rootPath + "permissions/rolePermisList.do",
//		userPermission : rootPath + "permissions/userPermisList.do",
		updatePermission : rootPath + "permission/update.do",
		updatePermissionByT : rootPath + "permission/updateByT.do",
//		updateMenuPermission : rootPath + "permissions/updateMenuPermission.do",
		deletePermission : rootPath + "permission/",
		
		templateRole : rootPath + "role/template.do",
		templateRoleUser : rootPath + "role/r_u_template.do",
		createRole : rootPath + "role/create.do",
		updateRole : rootPath + "role/update.do",
		listRole : rootPath + "role/list.do",
		showRole : rootPath + "role/show.do",
		showRoleByUser : rootPath + "role/showRoleByUser.do",
		deleteRole : rootPath + "role/",
		
		templateUser : rootPath + "user/template.do",
		createUser : rootPath + "user/create.do",
		updateUser : rootPath + "user/update.do",
		updateUserRole : rootPath + "user/updateUserRole.do",
		listUser : rootPath + "user/list.do",
		showUser : rootPath + "user/show.do",
		deleteUser : rootPath + "user/",
		showByName : rootPath + "user/showByName.do",
		setRoleToUser : rootPath + "user/setRoleToUser.do",
		getRoleForUser : rootPath + "user/getRoleForUser.do",
		
		templateDepartment : rootPath + "departments/template.do",
		createDepartment : rootPath + "departments/create.do",
		updateDepartment : rootPath + "departments/update.do",
		listDepartment : rootPath + "departments/list.do",
		showDepartment : rootPath + "departments/show.do",
		deleteDepartment : rootPath + "departments/",
		
		
		templateOrganization : rootPath + "organization/template.do",
		listOrganization : rootPath + "organization/list.do",
		createOrganization : rootPath + "organization/create.do",
		deleteOrganization : rootPath + "organization/",
		selectOrganization : rootPath + "organization/select.do",
		
		/*************zflow  TreeNode************************************/
		orgTreeNode : rootPath + "controls/orgTreeNode.do",
		usersOnOrgTreeNode : rootPath + "controls/usersOnOrgTreeNode.do",
		redirectToFormindex : rootPath + "redirects/redirectToFormindex.do",
		selectTableAllForm :rootPath+"controls/selectTableAllForm.do",//查询所以生成的对象
		selectFormProperty:rootPath+"controls/selectFormProperty.do",//
		/*************zflow  基础************************************/
		saveOrUpdateForm : rootPath + "forms/createOrUpdate.do",
		findAllForm : rootPath + "forms/findAllForm.do",
		deleteForm : rootPath + "forms/deleteForm.do",
		findFormById : rootPath + "forms/findFormById.do",
		saveFormProperty: rootPath + "forms/saveFormProperty.do",
		formZCloumnGuide: rootPath + "forms/formZCloumnGuide.do",//表单向导
		formConfigFields:rootPath+"forms/formConfigFields.do", //根据formid查询 json
		formConfigFieldsByCode :rootPath+"forms/formConfigFieldsByCode.do", //根据formCode查询Json
		showFormbyId:rootPath+"forms/showFormbyId.do",
		/**************form 数据****************/
		formAllData : rootPath + "forms/formAllData.do",
		formDataById:rootPath + "forms/formDataById.do",
		middleTableData:rootPath + "forms/middleTableData.do",
		saveSelectField:rootPath + "forms/saveSelectField.do",
		/*************zflow  数据校验************************************/
		//saveRegister : rootPath + "register/saveRegister.do",
		deleteRegister : rootPath + "register/deleteRegister.do",
		getOneRegister : rootPath + "register/getOneRegister.do",
		findAllRegister : rootPath + "register/findAllRegister.do",
		/*************zflow  数据联动************************************/
		findAllDepartment : rootPath + "linkage/findAllDepartment.do",
		findPositionByDepartmentId : rootPath + "linkage/findPositionByDepartmentId.do",
		/*************zflow  上传图片************************************/
		saveImage : rootPath + "image/saveImage.do",
		/*************zflow  列表数据校验************************************/
		saveTableData : rootPath +"tableDatas/saveTableData.do",
		findOneTableData : rootPath +"tableDatas/findOneTableData.do",
		findAllTableData : rootPath +"tableDatas/findAllTableData.do",
		formTableData:rootPath +"tableDatas/formTableData.do",
		/****************发布************************************************/
		createTable : rootPath + "forms/createTable.do",
		saveRegister : rootPath + "forms/saveFormData.do",
		updateFormData: rootPath + "forms/updateFormData.do",
		///保存 发布之后的页面
		saveformHtml: rootPath+"forms/saveformHtml.do",
		/****************查询数据**********************/
		showConditionSelect:rootPath + "tableDatas/showConditionSelect.do",
		selectColumnList:rootPath + "tableDatas/selectColumnList.do",
		getTableAll:rootPath+"ztables/getTableAll.do",
		getObjectsCollection:rootPath+"tableDatas/getObjectsCollection.do",
		deleteTableData: rootPath+"tableDatas/deleteTableData.do" //删除数据
	};
