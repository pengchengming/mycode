new function() {
	var me=this;
	
	this.controller = { 
			table : function(code,conditionId,queryFun,abc) {
				this.selectTableSql="";
				this.tableConfig=new Object;
				this.subTableConfig=new Object;				
				this.selectConditionsList=new Array();
				
				this.afterPropSet = function(abc) {
					$.ajax({
						url : rootPath+'/createSelect/findSelectTableList',
						type : "POST",
						async:false,
						data:{
							code:code
						},
						async: false,
						complete : function(xhr, textStatus) {
							var data = eval("("+xhr.responseText+")");
							 if (data.code + "" == "1"){
				                	var result=data.results; 
				                	abc.selectConditionsList=result.selectConditionsList; //查询条件
				                	abc.selectTableSql=result.selectTableSql; 
				                	var selectLists=result.selectLists;
				                	var fields=new Array();
				                	var subfields=new Array();
				                	if(selectLists){ 
				                		if(result.selectTableListSubset!=null&&result.selectTableListSubset.selectLists!=null){
				                			var field=new Object();
				                			field.title= "明细";
				                			field.type="tableLoad";
				                			field.cdivId=result.selectTableListSubset.code;
				                			field.url=rootPath+'/createSelect/findsubSelectData',
				                			field.subCode=result.selectTableListSubset.code;
				                			field.subCondition=result.subCondition;
				                			var paramArray=new Array();
				                			paramArray.push("id");
				                			field.param=paramArray;
				                			 
						                	var subSelectLists= result.selectTableListSubset.selectLists;
					                		$.each(subSelectLists,function(i,column){	
					                			if(column.isDisplay+""=="1"){
					                				var field=new Object();
						                			field.title=column.description;
						                			field.data=column.aliasesName;
						                			subfields.push(field);	
					                			}
					                		});
					                		var details=new Object();
					                		details.fields=subfields;
					                		field.details=details;
					                		fields.push(field);
					                	};
				                		$.each(selectLists,function(i,column){	                			
				                			if(column.isDisplay+""=="1"){
				                				var field=new Object();
					                			field.title=column.description;
					                			field.data=column.aliasesName;
					                			if(column.exportType){
					                				field.type=column.exportType;
					                			}
					                			fields.push(field);	
				                			}
				                		});
				                	}
				                	abc.tableConfig["fields"]=fields;
				                	if(conditionId)
				                	abc.selectConditionsFun(conditionId, result.selectConditionsList, result.occupyColumn,queryFun); 
				            	}	
						}
					});  
				},
				//显示查询
				this.selectConditionsFun=function(id, results, column,queryFun){
					var formPropertyTable="<table class='search_table'><tr>";
					var isTr=false;
					var isTd=0;
					 $.each(results,function(i,temp){
						 if(isTr){
							 formPropertyTable+="</tr><tr>";
							 isTr=false;
							 isTd=0;
						 }  
						 
						 if(temp.type=="2"){//整行
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
							 formPropertyTable+='<th>'+temp.description+':</th>  '; 
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
										if(temp.formProperty){
											if(temp.formProperty.fieldType=="attrDate")
												formPropertyTable+=' class="Wdate"  onfocus="WdatePicker({dateFmt:\'yyyy-MM-dd\'})"  ';
											else if(temp.formProperty.fieldType=="attrInt")
												formPropertyTable+=" onkeyup=\"value=value.replace(/[^\\d]/g,'')\"  ";
											else if(temp.formProperty.fieldType=="attrFloat" ||temp.formProperty.fieldType=="attrNumber"  ){
												formPropertyTable+=" onkeyup=\"createFormPage.repNum(this,"+temp.formProperty.fieldLength+") \"  ";
											 }
										}
										formPropertyTable+='>';
									}else if(extraAttributes.type=="textarea"){
										formPropertyTable+='<textarea type="textarea" id="'+extraAttributes.id+'"  cols="'+extraAttributes.cols+'" rows="'+extraAttributes.rows+'" ';
										if(extraAttributes.style!=null&&extraAttributes.style!="")
											formPropertyTable+=' style="'+extraAttributes.style+'" ';
										formPropertyTable+=' ></textarea>';
									}else if(extraAttributes.type=="select" ||extraAttributes.type=="radio" ||extraAttributes.type=="checkbox"){
										if(temp.formProperty.dataType=="yhm"){
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
											if(temp.formProperty.dictionaryCode){ 
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
					 formPropertyTable+="</tr>"+
					 "<tr ><td colspan='"+column+"'  align='right'><a onclick='queryClick(1)' class='a_btn_blue' href='javascript:void(0);'>搜索</a>  </td></tr> </table>";  
					 $("#"+id).append(formPropertyTable);
				}; 
				
			}
	};
 
	this.getSelectConditionSql=function(abc){
		var selectConditionsList=abc.selectConditionsList;
		var selectConditionSql="";
		if(selectConditionsList!=null&&selectConditionsList.length>0){
			 $.each(selectConditionsList, function(i,n) {
				 if(n.isSelectData&&n.isSelectData+""=="1"){
					 return ;
				 }
				 if(n.extraAttributes){
					  var extraAttributes=eval("("+n.extraAttributes+")");	
					  var str="";
					  if(extraAttributes.type=="text"||extraAttributes.type=="select"||extraAttributes.type=="textarea"){
						  str = $("#"+extraAttributes.id).val(); 
					  }else {
							if(extraAttributes.type == "radio" ||extraAttributes.type=="checkbox"){
								if(extraAttributes.type=="radio"){
									str=$("input[name='name_"+n.formProperty.fieldName+"']:checked").val(); 
								}else if(extraAttributes.type=="checkbox"){						
									var josnString = "";
									var checkboxs = $("input[name='name_"+n.formProperty.fieldName+"']:checked");  
									if(checkboxs!=null&&checkboxs.length>0){
										checkboxs.each(function(){
											if(this.checked==true){
												if(n.formProperty.fieldType=="attrInt")
													josnString+=$(this).val()+','; 
												  else 
													  josnString+="'"+$(this).val()+"',"; 
											}
										}); 
										if(josnString!=""){
											str=josnString.substring(0,josnString.lastIndexOf(","))+""; 
										}
									} 
								}
							}else{
								alert(extraAttributes.type);
							}	
						} 
				 }
				 if(extraAttributes.type=="checkbox"){		
					 selectConditionSql+=" and "+n.columnName+" in ("+str+") ";	
				 }else {
					 if(str){
						 selectConditionSql+=" and "+n.columnName+"";
						 if(n.operatorType){
							 if(n.operatorType =="like") 
								 selectConditionSql+=" like '%"+str+"%' "; 
							 if(n.operatorType =="in") 
								 selectConditionSql+=" in ("+str+") ";
							 if(n.operatorType =="isnull"){
								 if(str+""=="1") 
									 selectConditionSql+=" is not null ";
								 else if(str+""=="0")
									 selectConditionSql+=" is   null ";
							 }
						 }else {
							 selectConditionSql+=" = ";
							 if(n.formProperty&&n.formProperty.fieldType=="attrInt")
								  selectConditionSql+=str; 
							  else 
								  selectConditionSql+="  '"+str+"' ";
						 } 
					  }
				 }
				});
		} 
		if(selectConditionSql!="")
			selectConditionSql =" where 1=1 "+selectConditionSql;
		return selectConditionSql;
	};
	
	this.initData= function (code,conditionId,queryFun){
		var abc = new this.controller.table(code,conditionId,queryFun);
		abc.afterPropSet(abc);
		return abc;
	};
	window.createTableConfig = this;
}();
