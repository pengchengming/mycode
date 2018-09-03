
new function() {
	var me = this;
	//id: table的ID, code : formView的Code, type : base=主表, expand=扩展表, formId : form的ID, dataId : 当前记录的ID
	this.controller = {
			form : function(id,code,type,formId,isShow) {
				this.formPropertyList=new Array();
				this.formPropertyExpandList=new Array(); 
				this.afterPropSet = function(abc) {
					$.ajax({
						url : rootPath+'/formView/findFormPropertyList',
						type : "POST",
						async:false,
						data:{
							 code:code
						}, 
						complete : function(xhr, textStatus) {
							var data = eval("("+xhr.responseText+")");
							if (data.code + "" == "1"){
			                	if(type=="base"){
			                		abc.formPropertyList=data.results;
			                		abc.formPropertyShow(id,data.results,data.occupyColumn,isShow);
			                	}else if(type=="expand"){ 
			                		abc.formPropertyExpandList=data.results;
			                		abc.formPropertyShow(id,data.results,data.occupyColumn,isShow);
			                	}
			            	}	
						}
					}); 
				},
				//显示表格
				this.formPropertyShow=function(id, results, column,isShow){
					var foreignkeyControls="";
					var formPropertyTable="<table class='form_table'><tr>";
					var isTr=false;
					var isTd=0;
					 $.each(results,function(i,temp){
						 if(isTr){
							 formPropertyTable+="</tr><tr>";
							 isTr=false;
							 isTd=0;
						 }  
						 if(temp.type=="3"){
							 if(temp.extraAttributes!=null&&temp.extraAttributes!=''){
								 var extraAttributes=eval("("+temp.extraAttributes+")");
								 foreignkeyControls+=' <input id="'+extraAttributes.id+'" type="hidden" > ';	 
							 } 
						 }else  if(temp.type=="2"){//整行
							 formPropertyTable+='<th colspan="'+column+'"  ';
							 if(temp.extraAttributes!=null&&temp.extraAttributes!=''){
								 var extraAttributes=eval("("+temp.extraAttributes+")");
								 formPropertyTable+=' id="title_'+extraAttributes.id+'" >';
								 formPropertyTable+=' <input id="'+extraAttributes.id+'" type="hidden" > ';	 
							 } else {
								 formPropertyTable+='  id="title_'+temp.idx+'" > ';
							 }
							 formPropertyTable+='</th>  '; 
							 isTr=true;
						 }else if(temp.type=="1"){
							 var extraAttributes=eval("("+temp.extraAttributes+")");
							 formPropertyTable+='<th width="150">';
							 if(!temp.empty)
								 formPropertyTable+='<font color="red">*</font>';
							 formPropertyTable+=temp.formProperty.comment+':</th>  '; 
							 if(temp.occupyColumn > 2){
								 formPropertyTable+='<td colspan="'+(temp.occupyColumn-1)+'" id="'+extraAttributes.id+'_td" >';
							 }else {
								 formPropertyTable+='<td id="'+extraAttributes.id+'_td" >';
							 }
							 isTd=isTd+temp.occupyColumn;
							  
							 if(temp.tdHtml)
								 formPropertyTable+=temp.tdHtml;
							 else {
								 if(extraAttributes.type=="text"){
										formPropertyTable+='<input type="text" id="'+extraAttributes.id+'" '; 
										if(extraAttributes.style!=null&&extraAttributes.style!="")
											formPropertyTable+=' style="'+extraAttributes.style+'" ';
										if(temp.formProperty.fieldType=="attrDate")
											formPropertyTable+=' class="Wdate"  onfocus="WdatePicker({dateFmt:\'yyyy-MM-dd\'})"  ';
										else if(temp.formProperty.fieldType=="attrInt")
											formPropertyTable+=" onkeyup=\"value=value.replace(/[^\\d]/g,'')\"  "; 
										else if(temp.formProperty.fieldType=="attrFloat" ||temp.formProperty.fieldType=="attrNumber"  ){
											formPropertyTable+=" onkeyup=\"createFormPage.repNum(this,"+temp.formProperty.fieldLength+") \"  ";
										 }
										formPropertyTable+='>';
									}else if(extraAttributes.type=="textarea"){
										formPropertyTable+='<textarea type="textarea" id="'+extraAttributes.id+'"  cols="'+extraAttributes.cols+'" rows="'+extraAttributes.rows+'" ';
										if(extraAttributes.style!=null&&extraAttributes.style!="")
											formPropertyTable+=' style="'+extraAttributes.style+'" ';
										formPropertyTable+=' ></textarea>';
									}else if(extraAttributes.type=="select" ||extraAttributes.type=="radio" ||extraAttributes.type=="checkbox"){
										if(temp.formProperty.dataType=="yhm"&&(!isShow || isShow!=1)){
											if(extraAttributes.type=="select"){
												var option ='<select id="'+extraAttributes.id+'" ';
												if(extraAttributes.style!=null&&extraAttributes.style!="")
													option +=' style="'+extraAttributes.style+'" ';
												option +=' > ';
												option +="<option value='' >请选择</option>";
												option +='</select>';
												formPropertyTable+=option; 
											}
										}else { 
											if(temp.formProperty.dictionaryCode&&(!isShow || isShow!=1)){ 
												$.ajax({
													url : rootPath+'/dictionarys/dataDictionaryValueList.do',
													type : "POST",
													async:false,
													data:{
														code :temp.formProperty.dictionaryCode
													},
													complete : function(xhr, textStatus) {
														var dictionaryCodeData = eval("("+xhr.responseText+")");
														if(dictionaryCodeData &&dictionaryCodeData .length>0){
															var option="";
															if(extraAttributes.type=="select"){
																option +='<select id="'+extraAttributes.id+'" ';
																if(extraAttributes.style!=null&&extraAttributes.style!="")
																	option +=' style="'+extraAttributes.style+'" ';
																option +=' > ';
																option +="<option value='' >请选择</option>";
															}
															$.each(dictionaryCodeData,function(i,n){
																if(extraAttributes.type=="radio"){
																	option+="<input name='name_"+temp.formProperty.fieldName+"' value='"+n.id+"' type='radio'><span>"+n.displayValue+"</span>";
																}else if(extraAttributes.type=="checkbox"){
																	option +="<input name='name_"+temp.formProperty.fieldName+"' value='"+n.id+"' type='checkbox' ><span>"+n.displayValue+"</span>"; 
																}else if(extraAttributes.type=="select"){
																	option+= "<option value='"+n.id+"' >"+n.displayValue+"</option>";
																}
															});
															if(extraAttributes.type=="select"){
																option +='</select>';
															}
															formPropertyTable+=option; 
														} 
													}
												});	
											}
										}
									}
							 }
							 
							 formPropertyTable+='</td>';
							 //空余的列
							 if(temp.blankColumn&&temp.blankColumn>0){
								 var blankColumn = temp.blankColumn/2;
								 if((temp.blankColumn+isTd)> column){
									 blankColumn = (column-isTd)/2;
								 }
								 if(blankColumn > 0){
									 for(var rowIndex=0;rowIndex<blankColumn;rowIndex++){
										 formPropertyTable+="<th></th><td></td>";
									 }
								 }
								 isTd=isTd+temp.blankColumn;
							 }
							 
							 if((results.length-1)==i){ //最后一个
								 var remnantRow=column-isTd;
								 if(remnantRow>0){
									 var remnantColumn= remnantRow/2;
									 for(var rowIndex=0;rowIndex<remnantColumn;rowIndex++){
										 formPropertyTable+="<th></th><td></td>";
									 } 
								 } 
							 }else {
								 if(results[(i+1)].occupyColumn > 2){ //下一列大于1
									 var remnantRow=column-isTd;//剩余的列
									 if(results[(i+1)].occupyColumn > remnantRow){
										 var remnantColumn= remnantRow/2;
										 for(var rowIndex=0;rowIndex<remnantColumn;rowIndex++){
											 formPropertyTable+="<th></th><td></td>";
										 }
										 isTr=true;
									 }
								 }
							 }
							if(isTd >= column)isTr=true;
						 }
					 });
					 formPropertyTable+="</tr></table>";  
					 $("#"+id).append(foreignkeyControls);
					 $("#"+id).append(formPropertyTable);
				};
				
			},
			//显示保存的详细数据
			formAssignmentData: function (propertyList,data){
				$.each(propertyList,function(i,temp){
					if(temp.extraAttributes!=null&&temp.extraAttributes!=''){
						var extraAttributes=eval("("+temp.extraAttributes+")");
						
						if(extraAttributes.type=="text"||extraAttributes.type=="select"||extraAttributes.type=="textarea"){
							$("#"+extraAttributes.id).val(data[temp.formProperty.fieldName]);
							if(extraAttributes.type=="select"){
								$("#"+extraAttributes.id).trigger('change');
							}
						}else if(extraAttributes.type=="radio"){
							$("input[name='name_"+temp.formProperty.fieldName+"'][value='"+data[temp.formProperty.fieldName]+"']").prop("checked",true);
						}else if(extraAttributes.type=="checkbox"){
							var tempDataStr= data[temp.formProperty.fieldName];
							var tempDatas= tempDataStr.split(",");
							$.each(tempDatas,function(i,tempData){
								$("input[name='name_" + temp.formProperty.fieldName + "']").each(function () {					
									if($(this).val()==tempData)
										$(this).prop("checked", true);
						        });
							});
						}
					 }
				});
			},
			//用于展示数据
			showFormAssignmentData: function (propertyList,data){
				$.each(propertyList,function(i,temp){
					if(temp.type+""=="2"){
						 if(temp.extraAttributes!=null&&temp.extraAttributes!=''){
							var extraAttributes=eval("("+temp.extraAttributes+")");
							$("#"+extraAttributes.id).val(data[temp.formProperty.fieldName]);
						 } 				
					}else {
						var extraAttributes=eval("("+temp.extraAttributes+")");
						$("#"+extraAttributes.id+"_td").html("");
						if(extraAttributes.type=="text"||extraAttributes.type=="textarea"){
							$("#"+extraAttributes.id+"_td").html(data[temp.formProperty.fieldName]); 
						}else if(extraAttributes.type=="select"||extraAttributes.type=="radio"){
							var val= data[temp.formProperty.fieldName];
							if(temp.formProperty.dataType=="yhm"){
								createFormPage.yhmFun(extraAttributes.id,val);
							}else {
								if(val!=null&&val!="")
									createFormPage.dataDictionaryValues(extraAttributes.id+"_td",val);
							}
							
						} else if(extraAttributes.type=="checkbox"){
							var val= data[temp.formProperty.fieldName];
							if(val!=null&&val!="")
								createFormPage.dataDictionaryValues(extraAttributes.id+"_td",val);
						}
					}
				});
			},
			 //保存数据的JSON
			formPropertyDataJson: function  (formPropertyList,isChick){
				 var dataBase = {};
				 var isCorrect=true;
				 $.each(formPropertyList, function(i,n) { 
					 if(n.extraAttributes){
						  var extraAttributes=eval("("+n.extraAttributes+")");		  
						  if(extraAttributes.type=="text"||extraAttributes.type=="select"||extraAttributes.type=="textarea"){
							  var str = $("#"+extraAttributes.id).val();
							  dataBase[n.formProperty.fieldName] = str;
						  }else {
								if(extraAttributes.type == "radio" ||extraAttributes.type=="checkbox"){
									if(extraAttributes.type=="radio"){
										var  str=$("input[name='name_"+n.formProperty.fieldName+"']:checked").val(); 
										dataBase[n.formProperty.fieldName] =str;
									}else if(extraAttributes.type=="checkbox"){						
										var josnString = "";
										var checkboxs = $("input[name='name_"+n.formProperty.fieldName+"']:checked");  
										if(checkboxs!=null&&checkboxs.length>0){
											checkboxs.each(function(){
												if(this.checked==true){
													josnString+=$(this).val()+','; 
												}
											}); 
											str=josnString.substring(0,josnString.lastIndexOf(","))+"";
											dataBase[n.formProperty.fieldName] =str; 
										} 
									}
								}else{
									alert(extraAttributes.type);
								}	
							}	
						 
						  var str= dataBase[n.formProperty.fieldName] ;
						  if(!n.empty&&(!str||str=="")&&
								  (isChick+""==""|| isChick+""=="undefined" || isChick+""=="1")){
							  alert(n.formProperty.comment+"为必填项，请填写！");
							  isCorrect=false;
							  return false;
						  }else if(str&&((str+"").length>n.formProperty.fieldLength)){
							  alert(n.formProperty.comment+"长度过长，请重新填写！");
							  isCorrect=false;
							  return false;
						  } 
						  /*
						  if(n.formProperty.fieldType=="attrInt" ||n.formProperty.fieldType=="attrFloat" ||n.formProperty.fieldType=="attrNumber"  ){
							 if(!isNaN(str)){
								 alert(n.formProperty.comment+" 填写错误，请重新填写！");
								 isCorrect=false;
								 return false;
							 } 
						  }*/
					 }
					});
				 dataBase.isCorrect=isCorrect;
				 return dataBase;
			 } 
	};
	this.viewPageController={
			form : function(id,code) {
				this.propertyList=new Array();
				this.afterPropSet = function(abc) {
					$.ajax({
						url : rootPath+'/viewPage/findViewPagePropertyList',
						type : "POST",
						async:false,
						data:{
							 code:code
						},
						async: false,
						complete : function(xhr, textStatus) {
							var data = eval("("+xhr.responseText+")");
							if (data.code + "" == "1"){
								abc.propertyList=data.results;
		                		abc.viewPropertyShow(id,data.results,data.occupyColumn);
			            	}	
						}
					}); 
				},
				//显示表格
				this.viewPropertyShow=function(id, results, column){
					var foreignkeyControls="";
					var formPropertyTable="<table class='form_table'><tr>";
					var isTr=false;
					var isTd=0;
					 $.each(results,function(i,temp){
						 if(isTr){
							 formPropertyTable+="</tr><tr>";
							 isTr=false;
							 isTd=0;
						 }  
						 if(temp.type=="3"){//控件隐藏
							 if(temp.extraAttributes!=null&&temp.extraAttributes!=''){
								 var extraAttributes=eval("("+temp.extraAttributes+")");
								 foreignkeyControls+=' <input id="'+extraAttributes.id+'" type="hidden" > ';	 
							 } 
						 }else  if(temp.type=="2"){//整行
							 formPropertyTable+='<th colspan="'+column+'"  ';
							 if(temp.extraAttributes!=null&&temp.extraAttributes!=''){
								 var extraAttributes=eval("("+temp.extraAttributes+")");
								 formPropertyTable+=' id="title_'+extraAttributes.id+'" >';
								 formPropertyTable+=' <input id="'+extraAttributes.id+'" type="hidden" > ';	 
							 } else {
								 formPropertyTable+='  id="title_'+temp.idx+'" > ';
							 }
							 formPropertyTable+='</th>  '; 
							 isTr=true;
						 }else if(temp.type=="1"){
							 var extraAttributes=eval("("+temp.extraAttributes+")");
							 formPropertyTable+='<th>';
							 if(!temp.empty)
								 formPropertyTable+='<font color="red">*</font>';
							 formPropertyTable+=temp.description+':</th>  '; 
							 if(temp.occupyColumn > 2){
								 formPropertyTable+='<td colspan="'+(temp.occupyColumn-1)+'" id="'+extraAttributes.id+'_td" >';
							 }else {
								 formPropertyTable+='<td id="'+extraAttributes.id+'_td" >';
							 }
							 isTd=isTd+temp.occupyColumn;
							  
							 if(temp.tdHtml)
								 formPropertyTable+=temp.tdHtml;
							 else {
								 if(extraAttributes.type=="text"){
										formPropertyTable+='<input type="text" id="'+extraAttributes.id+'" '; 
										if(extraAttributes.style!=null&&extraAttributes.style!="")
											formPropertyTable+=' style="'+extraAttributes.style+'" ';
										if(temp.fieldType=="attrDate")
											formPropertyTable+=' class="Wdate"  onfocus="WdatePicker({dateFmt:\'yyyy-MM-dd\'})"  ';
										else if(temp.fieldType=="attrInt")
											formPropertyTable+=" onkeyup=\"value=value.replace(/[^\\d]/g,'')\"  "; 
										else if(temp.fieldType=="attrFloat" ||temp.fieldType=="attrNumber"  ){
											formPropertyTable+=" onkeyup=\"createFormPage.repNum(this,"+temp.formProperty.fieldLength+") \"  ";
										 }
										formPropertyTable+='>';
									}else if(extraAttributes.type=="textarea"){
										formPropertyTable+='<textarea type="textarea" id="'+extraAttributes.id+'"  cols="'+extraAttributes.cols+'" rows="'+extraAttributes.rows+'" ';
										if(extraAttributes.style!=null&&extraAttributes.style!="")
											formPropertyTable+=' style="'+extraAttributes.style+'" ';
										formPropertyTable+=' ></textarea>';
									}else if(extraAttributes.type=="select" ||extraAttributes.type=="radio" ||extraAttributes.type=="checkbox"){
										if(temp.dataType=="yhm"){
											if(extraAttributes.type=="select"){
												var option ='<select id="'+extraAttributes.id+'" ';
												if(extraAttributes.style!=null&&extraAttributes.style!="")
													option +=' style="'+extraAttributes.style+'" ';
												option +=' > ';
												option +="<option value='' >请选择</option>";
												option +='</select>';
												formPropertyTable+=option; 
											}
										}else { 
											if(temp.dictionaryCode){ 
												$.ajax({
													url : rootPath+'/dictionarys/dataDictionaryValueList.do',
													type : "POST",
													async:false,
													data:{
														code :temp.dictionaryCode
													},
													complete : function(xhr, textStatus) {
														var dictionaryCodeData = eval("("+xhr.responseText+")");
														if(dictionaryCodeData &&dictionaryCodeData .length>0){
															var option="";
															if(extraAttributes.type=="select"){
																option +='<select id="'+extraAttributes.id+'" ';
																if(extraAttributes.style!=null&&extraAttributes.style!="")
																	option +=' style="'+extraAttributes.style+'" ';
																option +=' > ';
																option +="<option value='' >请选择</option>";
															}
															$.each(dictionaryCodeData,function(i,n){
																if(extraAttributes.type=="radio"){
																	option+="<input name='name_"+temp.aliasesName+"' value='"+n.id+"' type='radio'><span>"+n.displayValue+"</span>";
																}else if(extraAttributes.type=="checkbox"){
																	option +="<input name='name_"+temp.aliasesName+"' value='"+n.id+"' type='checkbox' ><span>"+n.displayValue+"</span>"; 
																}else if(extraAttributes.type=="select"){
																	option+= "<option value='"+n.id+"' >"+n.displayValue+"</option>";
																}
															});
															if(extraAttributes.type=="select"){
																option +='</select>';
															}
															formPropertyTable+=option; 
														} 
													}
												});	
											}
										}
									}
							 }
							 
							 formPropertyTable+='</td>';
							 //空余的列
							 if(temp.blankColumn&&temp.blankColumn>0){
								 var blankColumn = temp.blankColumn/2;
								 if((temp.blankColumn+isTd)> column){
									 blankColumn = (column-isTd)/2;
								 }
								 if(blankColumn > 0){
									 for(var rowIndex=0;rowIndex<blankColumn;rowIndex++){
										 formPropertyTable+="<th></th><td></td>";
									 }
								 }
								 isTd=isTd+temp.blankColumn;
							 }
							 
							 if((results.length-1)==i){ //最后一个
								 var remnantRow=column-isTd;
								 if(remnantRow>0){
									 var remnantColumn= remnantRow/2;
									 for(var rowIndex=0;rowIndex<remnantColumn;rowIndex++){
										 formPropertyTable+="<th></th><td></td>";
									 } 
								 } 
							 }else {
								 if(results[(i+1)].occupyColumn > 2){ //下一列大于1
									 var remnantRow=column-isTd;//剩余的列
									 if(results[(i+1)].occupyColumn > remnantRow){
										 var remnantColumn= remnantRow/2;
										 for(var rowIndex=0;rowIndex<remnantColumn;rowIndex++){
											 formPropertyTable+="<th></th><td></td>";
										 }
										 isTr=true;
									 }
								 }
							 }
							if(isTd >= column)isTr=true;
						 }
					 });
					 formPropertyTable+="</tr></table>";  
					 $("#"+id).append(foreignkeyControls);
					 $("#"+id).append(formPropertyTable);
				};
				
			},
			//显示保存的详细数据
			viewAssignmentData: function (propertyList,data){
				$.each(propertyList,function(i,temp){
					if(temp.extraAttributes!=null&&temp.extraAttributes!=''){
						var extraAttributes=eval("("+temp.extraAttributes+")");
						
						if(extraAttributes.type=="text"||extraAttributes.type=="select"||extraAttributes.type=="textarea"){
							$("#"+extraAttributes.id).val(data[temp.aliasesName]);
							if(extraAttributes.type=="select"){
								$("#"+extraAttributes.id).trigger('change');
							}
						}else if(extraAttributes.type=="radio"){
							$("input[name='name_"+temp.aliasesName+"'][value='"+data[temp.aliasesName]+"'").attr("checked",true);
						}else if(extraAttributes.type=="checkbox"){
							var tempDataStr= data[temp.aliasesName];
							var tempDatas= tempDataStr.split(",");
							$.each(tempDatas,function(i,tempData){
								$("input[name='name_" + temp.aliasesName + "']").each(function () {					
									if($(this).val()==tempData)
										$(this).prop("checked", true);
						        });
							});
						}
					 }
				});
			},
			//用于展示数据
			showViewAssignmentData: function (propertyList,data){
				$.each(propertyList,function(i,temp){
					if(temp.type+""=="2"){
						 if(temp.extraAttributes!=null&&temp.extraAttributes!=''){
							var extraAttributes=eval("("+temp.extraAttributes+")");
							$("#"+extraAttributes.id).val(data[temp.aliasesName]);
						 } 				
					}else {
						var extraAttributes=eval("("+temp.extraAttributes+")");
						$("#"+extraAttributes.id+"_td").html("");
						if(extraAttributes.type=="text"||extraAttributes.type=="textarea"){
							$("#"+extraAttributes.id+"_td").html(data[temp.aliasesName]); 
						}else if(extraAttributes.type=="select"||extraAttributes.type=="radio"){
							var val= data[temp.aliasesName];
							if(temp.dataType=="yhm"){
								createFormPage.yhmFun(extraAttributes.id,val);
							}else {
								if(val!=null&&val!="")
									createFormPage.dataDictionaryValues(extraAttributes.id+"_td",val);
							}
							
						} else if(extraAttributes.type=="checkbox"){
							var val= data[temp.aliasesName];
							if(val!=null&&val!="")
								createFormPage.dataDictionaryValues(extraAttributes.id+"_td",val);
						}
					}
				});
			}
	};
	this.isEmpty = function(value){
		if(null == value || "" == value || "undefined" == value || undefined == value || "null" == value){
			return true;
		}else{
			value = (value + "").replace(/\s/g, '');
			if("" == value){
				return true;
			}
			return false;
		}
	};
	//校验输入长度，且自动修正输入的长度
	this.setLength = function(obj, length){
		if(length < obj.value.length)
			obj.value = obj.value.substr(0, length); 
	};
	this.repInt = function(obj, length){
		if(!createFormPage.isEmpty(length)){
			if(length < obj.value.length)
				obj.value = obj.value.substr(0, length); 
		}
		var reg = /^[\d]+$/g;
		if(!reg.test(obj.value)){
			var txt = obj.value;
			txt.replace(/[^0-9]+/, function(char, index, val) {//匹配第一次非数字字符
				obj.value = val.replace(/\D/g, "");//将非数字字符替换成""
				var rtextRange = null;
				if(obj.setSelectionRange){
					obj.setSelectionRange(index, index);
				}else{//支持ie
					rtextRange = obj.createTextRange();
					rtextRange.moveStart('character', index);
					rtextRange.collapse(true);
					rtextRange.select();
				}
			})
		}
	};
	//保留两位小数，且校验长度
	this.repNum = function(obj, length,type){
		if(!createFormPage.isEmpty(length)){
			if(length < obj.value.length)
				obj.value = obj.value.substr(0, length); 
		}
		//先把非数字的都替换掉，除了数字和.
		if(type)
			obj.value = obj.value.replace(/[^-\d.]/g,"");
		else 
			obj.value = obj.value.replace(/[^\d.]/g,"");
		//必须保证第一个为数字而不是.
		obj.value = obj.value.replace(/^\./g,"");
		//保证只有出现一个.而没有多个.
		obj.value = obj.value.replace(/\.{2,}/g,".");
		//保证.只出现一次，而不能出现两次以上
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		//保留两位小数 
		var dot = obj.value.indexOf(".");
	    if(-1 != dot){
	    	var dotCnt = obj.value.substring(dot + 1, obj.value.length);
	        if(dotCnt.length > 2){
	        	obj.value = Math.floor(obj.value * 100)/100; 
	        }
	    }
	};
	//强制保留两位小数
	this.changeTwoDecimal = function(value){
	    var f = parseFloat(value);
	    if(isNaN(f)){
	        alert('输入的不是数字类型，无法转换为小数!');
	        event.keyCode=9;
	        return false;
	    }
	    var f = Math.round(f * 100) / 100;
	    var s = f.toString();
	    var pos_decimal = s.indexOf('.');
	    if(0 > pos_decimal){
	        pos_decimal = s.length;
	        s += '.';
	    }
	    while(s.length <= pos_decimal + 2){
	        s += '0';
	    }
	    return s;
	};	
	 //新增  
	/***
	 * id  页面Id
	 * code formViewCode
	 * type  base 主表扩展表
	 * formId 
	 * 是否显示 isShow =1 是：否
	 */
	this.createFormController=function(id,code,type,formId,isShow){
		var abc = new this.controller.form(id,code,type,formId,isShow);
		abc.afterPropSet(abc);
		return abc;
	};
	//修改
	/*this.modifyFormController=function(id,code,type,formId,dataId){
		var abc = new this.controller.form(id,code,type,formId);
		abc.afterPropSet(abc);
		createFormPage.showFormAllData(formId,dataId,abc);
		return abc;
	};*/
	//显示
	this.showFromTable=function(id,code,type,formId,dataId) {
		$("#"+id).hide();
		var abc = new this.controller.form(id,code,type,formId,dataId);
		abc.afterPropSet(abc);
		createFormPage.showTableData(formId,dataId,abc);
		$("#"+id).show();
		return abc;
	};
	//显示保存的数据
	this.showFormAllData= function (formId,dataId,abc){
		var me=this;
	  	$.ajax({
			url : rootPath+'/createSelect/showFormDataById',
			type : "POST",
			async:false,
			data:{
				formId:formId,
				dataId:dataId
			},complete : function(xhr, textStatus) {
				var data = eval("("+xhr.responseText+")");
				if(data.results){
					data.results=eval("("+data.results+")");
					var result= data.results[0];
					if(abc.formPropertyList)
						 me.controller.formAssignmentData(abc.formPropertyList,result);
					if(abc.formPropertyExpandList)
						 me.controller.formAssignmentData(abc.formPropertyExpandList,result);
				}	
			}
		});
	};

	//显示表单
	this.showTableData= function (formId,dataId,abc){
		var me=this;
	  	$.ajax({
			url : rootPath+'/createSelect/showFormDataById',
			type : "POST",
			async:false,
			data:{
				formId:formId,
				dataId:dataId
			},
			async: false,
			complete : function(xhr, textStatus) {
				var data = eval("("+xhr.responseText+")");
				if(data.results){
					data.results=eval("("+data.results+")");
					var result= data.results[0];
					  
					if(abc.formPropertyList)
						me.controller.showFormAssignmentData(abc.formPropertyList,result); 
					if(abc.formPropertyExpandList)
						me.controller.showFormAssignmentData(abc.formPropertyList,result);   
				}	
			}
		});
	};
	
	this.showFormInternalDataById= function (formId,dataId,propertyCodes){
	  	$.ajax({
			url : rootPath+'/createSelect/showFormInternalDataById',
			type : "POST",
			async:false,
			data:{
				formId:formId,
				dataId:dataId,
				propertyCodes:propertyCodes
			},
			async: false,
			complete : function(xhr, textStatus) {
				var data = eval("("+xhr.responseText+")");
				if(data.results){
					data.results=eval("("+data.results+")");
					var result= data.results[0];
					   $.each(result,function(name,dataJson){
						  $("#"+name+"_td").html(dataJson) ;
					   });
					
				}	
			}
		});
	};
	

	this.dataDictionaryValues= function (showId,ids){
		$.ajax({
			url : rootPath+'/dictionarys/dataDictionaryValues.do',
			type : "POST",
			async:false,
			data:{
				ids :ids
			},
			async: false,
			complete : function(xhr, textStatus) {
				var dictionaryCodeData = eval("("+xhr.responseText+")");
				if(dictionaryCodeData.code==1){
					var results= dictionaryCodeData.results;
					var resultStr="";
					$.each(results,function(i,result){
						resultStr+=result.displayValue;
					});
					$("#"+showId).html(resultStr);
				}
			}
		});
	};
	 
	this.yhmFun=function(showId,valId){
		$.ajax({
	 		url : rootPath+"/region/getRegionById",
	 		type : "POST", 
	         async : false,
	         data : {
	         	id : valId
	         }, complete : function(xhr, textStatus){
	 			var data = JSON.parse(xhr.responseText);
	 			if('1' == data['code']){
	 				var results= data.results; 
	 				if(results&&results.name)
	 					$("#"+showId+"_td").html(results.name);
	 			}
	 		}
	 	});
	}  ; 
	this.provinceFun= function(level,parentId,provinceId,cityId,districtId){
		
	 	$.ajax({
	 		url : rootPath+"/region/regionList",
	 		type : "POST", 
	         async : false,
	         data : {
	         	level : level,
	         	parentId:parentId
	         }, complete : function(xhr, textStatus){
	 			var data = JSON.parse(xhr.responseText);
	 			if('1' == data['code']){
	 				var results= data.results;
	 				var  provinceSelect=document.createElement("select");
	 				
	 				var option=document.createElement("option");
	 				$(option).attr("value","").html("请选择");
	 				$(provinceSelect).append(option);
	 				$.each(results,function(i,result){
	 					var option=document.createElement("option");
	 					$(option).attr("value",result.id).html(result.name);
	 					$(provinceSelect).append(option);
	 				});
	 				var id=provinceId;	 				
	 				if(level==2){
						id=cityId;
	 				}	
	 				else if(level==3){
		 				id=districtId;
	 				}
	 				var style= $("#"+id).attr("style");
	 				if(style) 
	 					$(provinceSelect).attr("style",style);
	 				$(provinceSelect).attr("id",id);
	 				if(parentId!=""&&level!=3){
	 					
	 					$(provinceSelect).bind('change', function() {
	 						var thisId=$(this).attr("id");
	 						if(thisId==provinceId){
	 							level=1;
	 							var cityStyle="";
	 							if($("#"+cityId).attr("style"))
	 								cityStyle=$("#"+cityId).attr("style");
	 							$("#"+cityId).html("<select id='"+cityId+"' style='"+cityStyle+"' ><option value=''>请选择</option></select>");
	 							
	 							var districtStyle="";
	 							if($("#"+districtId).attr("style"))
	 								districtStyle=$("#"+districtId).attr("style");
	 							$("#"+districtId).html("<select id='"+districtId+"' style='"+districtStyle+"' ><option value=''>请选择</option></select>");
	 						}else if(thisId==cityId){
	 							level=2;
	 						}
	 						level++;
	 						var changeVal=$(this).val(); 
	 						if(changeVal)
	 						createFormPage.provinceFun(level,$(this).val(),provinceId,cityId,districtId);	
	 					  	  
	 					});
	 				}	 
	 				$("#"+id+"_td").html(provinceSelect);
	 			}
	 		}
	 	});
	 }; 
	  
	 
	 //获取页面上元素的值
	 this.getPropertyDataJson = function(formId, dataId,abc,isChick){
		 	var jsonString = {};
			jsonString.formId = formId;
			if(dataId) jsonString.tableDataId = dataId;
			
			var dataJson=new this.controller.formPropertyDataJson(abc.formPropertyList,isChick); 
			if(!dataJson.isCorrect){
				return false;
			}
			jsonString.register=dataJson; 
			if(abc.formPropertyExpandList!=null){
				var dataExpandJson=new this.controller.formPropertyDataJson(abc.formPropertyExpandList,isChick);
				if(!dataJson.isCorrect){
					return false;
				}	
				jsonString.registerExpand=dataExpandJson;	
			}
			return jsonString;
	 };
	//保存数据
	 this.formPropertySaveData2 = function(formId, dataId,abc,isAlert){
		 	var jsonString = {};
			jsonString.formId = formId;
			if(dataId) jsonString.tableDataId = dataId;
			
			var dataJson=new this.controller.formPropertyDataJson(abc.formPropertyList);
			if(!dataJson.isCorrect){
				return false;
			}
			jsonString.register=dataJson; 
			 
			url =rootPath+'/forms/saveFormDataJson';
			 var iscode=false;
			$.ajax({
				url : url,
				type : "POST",
				async:false,
				data : {
					json : JSON.stringify(jsonString)
				},
				complete : function(xhr, textStatus) {
					var data = eval("("+xhr.responseText+")");
					if(data.errorMsg ) {
						if(isAlert)
							alert(data.errorMsg);
					}
					if(data.successMsg){
						iscode=true;
						if(isAlert)
							alert(data.successMsg);
					}
				}
			}); 
			return iscode;
	 };
	 
	//保存数据
	 this.formPropertySaveData = function(formId, dataId,abc){
		 	var jsonString = {};
			jsonString.formId = formId;
			if(dataId) jsonString.tableDataId = dataId;
			
			var dataJson=new this.controller.formPropertyDataJson(abc.formPropertyList);
			if(!dataJson.isCorrect){
				return false;
			}
			jsonString.register=dataJson; 
			if(abc.formPropertyExpandList!=null){
				var dataExpandJson=new this.controller.formPropertyDataJson(abc.formPropertyExpandList);
				if(!dataJson.isCorrect){
					return false;
				}	
				jsonString.registerExpand=dataExpandJson;	
			} 
			 var url="";
			 if(dataId){
				 url =rootPath+'/forms/updateFormData.do';
			 }else {
				 url =rootPath+'/forms/saveFormData.do';
			 } 
			 var iscode=false;
			$.ajax({
				url : url,
				type : "POST",
				async:false,
				data : {
					jsonString : JSON.stringify(jsonString)
				},
				complete : function(xhr, textStatus) {
					var data = eval("("+xhr.responseText+")");
					if(data.errorMsg) {
						alert(data.errorMsg);
					}
					if(data.successMsg){
						iscode=true;
						alert(data.successMsg);
					}
				}
			}); 
			return iscode;
	 };
	 //新增 ViewPage  
	this.createViewPageController=function(id,code){
		var abc = new this.viewPageController.form(id,code);
		abc.afterPropSet(abc);
		return abc;
	};
	
	//显示保存的数据
	this.showViewCodeAllData= function (code,abc){ 
		var me=this;
	  	$.ajax({
			url : rootPath+'/viewPage/showViewDataByCode',
			type : "POST",
			async:false,
			data:{
				code:code
			},complete : function(xhr, textStatus) {
				var data = eval("("+xhr.responseText+")");
				if(data.results){
					data.results=eval("("+data.results+")");
					var result= data.results[0];
					 me.viewPageController.viewAssignmentData(abc.propertyList,result);
				}	
			}
		});
	};
	//显示表单
	this.showviewTableData= function (code,abc){
		var me=this;
	  	$.ajax({
			url : rootPath+'/viewPage/showViewDataByCode',
			type : "POST",
			async:false,
			data:{
				code:code
			}, 
			complete : function(xhr, textStatus) {
				var data = eval("("+xhr.responseText+")");
				if(data.results){
					data.results=eval("("+data.results+")");
					var result= data.results[0]; 
					me.viewPageController.showViewAssignmentData(abc.propertyList,result);
				}	
			}
		});
	};
	/*
	** randomWord 产生任意长度随机字母数字组合
	** randomFlag-是否任意长度 min-任意长度最小位[固定位数] max-任意长度最大位
	** 
	*/
	this.randomWord = function(randomFlag, min, max){
//		 Math.random().toString(36).substr(2);
//		很有意思，研究了一下，基本上toString后的参数规定可以是2-36之间的任意整数，不写的话默认是10（也就是十进制），此时返回的值就是那个随机数。
//		若是偶数，返回的数值字符串都是短的，若是奇数，则返回的将是一个很大长度的表示值。
//		若<10 则都是数字组成，>10 才会包含字母。
//		所以如果想得到一长串的随机字符，则需使用一个 > 10 且是奇数的参数，另外根据长度自行使用slice(2,n)截取！
	    var str = '',
	        range = min,
	        arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
	    // 随机产生
	    if(randomFlag)
	        range = Math.round(Math.random() * (max-min)) + min;
	    
	    for(var i = 0; i < range; i++){
	        pos = Math.round(Math.random() * (arr.length - 1));
	        str += arr[pos];
	    }
	    return str;
	};
	window.createFormPage = this;
}();


 