
var rootPath;
$(function () {
    rootPath = getRootPath();
});
function getRootPath() {
    var pathName = window.location.pathname.substring(1);
    var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/'));
    return window.location.protocol + '//' + window.location.host + '/' + webName + '/';
}


function createTable() {
    subThreeOver = "";
    selectLoadData = "";
};
createTable.prototype = {
    //获取选中的值，参数 tabledata ：初始化Table时返回的值
    getChecked: function (tabledata) {
        var tableId = tabledata.id;
        var tablejsonData = tabledata.data.results;
        var returnData = [];
        var tablecheckeds = $("#" + tableId).find(":checkbox");
        for (var i = 1; i < tablecheckeds.length; i++) {
            var tablechecked = tablecheckeds[i];
            if ($(tablechecked)[0].checked) {
                returnData.push(tablejsonData[i - 1]);
            }
        }
        return returnData;
    },
    //全选事件
    checkboxClick: function (me) {
        var fieldName = $(me).attr("selectAll");
        var metable = $(me).parents("table");
        metable.find("input[name='" + fieldName + "_checkbox']").each(function () {
            if (!($(this).attr("disabled")))
                $(this).prop("checked", me.checked);
        });
    },
    // 查询 创建table 
    registTable: function (id, config, data, toPageParam, isChild) {
        var _this = this;
        var idtext = id.attr("id");
        //创建table
        var table = document.createElement("table");
        $(table).attr("cellspacing", "0");
        $(table).attr("border", "1");
        $(table).attr("rules", "all");
        $(table).addClass("list_table");

        $(table).attr("enableemptycontentrender", "true");

        $(table).attr("id", idtext + "_createDiv");
        //$(table).attr("class", "crateTableClass");
        var tbody = document.createElement("tbody");
        var thead = document.createElement("thead");
        var tfoot = document.createElement("tfoot");
        //var tr=null;
        //		//画表格
        //表头
        var tr = document.createElement("tr");
        //$(tr).addClass("trtitlist");
        for (var j = 0; j < config.fields.length; j++) {
            var fieldConfig = config.fields[j];
            var th = document.createElement("th");
            if(fieldConfig.width)
            	$(th).css("width",fieldConfig.width);
            if(fieldConfig.height)
            	$(th).css("height",fieldConfig.height);
            if (fieldConfig.type == "hidden" || fieldConfig.type == "none") {
                continue;
            }
            //如果是复习宽,创建全选的文本宽
            if (fieldConfig.type == "checkbox" && (!fieldConfig.checktype || fieldConfig.checktype == "0")) {
            	var isCheckShow=true;
            	if(fieldConfig.isCheckAll!=null){
            		if(!(fieldConfig.isCheckAll+""=="true"||fieldConfig.isCheckAll==1)){
                    	isCheckShow=false;
                    }
            	}
            	if(isCheckShow){
                	var checkbox = document.createElement("input");
                    $(checkbox).attr("type", "checkbox");
                    $(checkbox).attr("id", fieldConfig.data + "All_id");
                    $(checkbox).attr("value", "checkedAll");
                    $(checkbox).attr("selectAll", fieldConfig.data);
                    $(checkbox).click(function () {
                        _this.checkboxClick(this);
                    });
                    $(th).append(checkbox);
            	}
            }
            $(th).append(fieldConfig.title);

            if (fieldConfig.orderBy == "ASC" || fieldConfig.orderBy == "DESC") { //ASC  DESC
                var _index = fieldConfig.index;
                $(th).click(function () {

                    var _fieldConfig = config.fields[_index];
                    if (_fieldConfig.orderBy == "ASC")
                        _fieldConfig.orderBy = "DESC";
                    else
                        _fieldConfig.orderBy = "ASC";
                    _fieldConfig.tdClick.call(this, _fieldConfig.orderBy);
                });
            }

            $(tr).append(th);
        }
        $(thead).append(tr);
        //数据
        //var datas=	data[dataName];
        var tableData = null;
        if (data) {
            tableData = data.results;
        }
        if (tableData&&tableData.length>0) {
            for (var i = 0; i < tableData.length; i++) {
                var tr = document.createElement("tr");
                if (i % 2 == 0) {
                    //$(tr).addClass("trevenline");
                } else {
                    //$(tr).addClass("troddline");
                }
                $(tr).hover(
                  function () {
                      $(this).attr("style", "background-color:rgb(240,205,173);");
                  },
                  function () {
                      $(this).attr("style", "");
                  }
                );
                $(tbody).append(tr);
                var trdata = tableData[i];
                //实现数据
                //结构
                for (var j = 0; j < config.fields.length; j++) {
                    var fieldConfig = config.fields[j];
                    var fieldData = this.getValueFromObj(trdata, fieldConfig.data);
                    var th = document.createElement("td");
                    $(th).attr("index" ,j);	
                    if (fieldConfig.type == "line") {
                        var line = (i + 1);
                        $(th).append(line);
                    }else if (fieldConfig.type == "none") {
                        $(th).attr("style", "display:none;"); 
                    	$(th).append(fieldData);
                    }else if (fieldConfig.type == "hidden") {
                        var hideInput = document.createElement("input");
                        $(hideInput).attr("type", "hidden");
                        $(hideInput).attr("id", fieldConfig.data);
                        $(hideInput).attr("name", fieldConfig.title);
                        $(hideInput).attr("class", "inputhide");
                        $(hideInput).val(fieldData);
                        $(th).attr("style", "display:none;");
                        $(th).append(hideInput);
                        //$(th).remove();
                        //$(tr).append(hideInput);
                    }else if(fieldConfig.type == "radio"){
                    	var radio = document.createElement("input");
                        $(radio).attr("type", "radio");
                        $(radio).attr("index", i);
                        $(radio).attr("value", fieldData);
                        $(radio).attr("name", fieldConfig.data + "radio");
                        if (fieldConfig.IsSelect) {
                            if (this.getValueFromObj(trdata, fieldConfig.IsSelect) && this.getValueFromObj(trdata, fieldConfig.IsSelect) + '' == '1')
                                $(radio).attr("checked", "checked");
                        }
                        $(th).append(radio);
                    }
                    else if (fieldConfig.type == "checkbox") {
                        var checkbox = document.createElement("input");
                        $(checkbox).attr("type", "checkbox");
                        //$(checkbox).attr("id",fieldData+"_id");
                        $(checkbox).attr("index", i);
                        $(checkbox).attr("value", fieldData);
                        $(checkbox).attr("name", fieldConfig.data + "_checkbox");
                        if (fieldConfig.IsSelect) {
                            if (this.getValueFromObj(trdata, fieldConfig.IsSelect) && this.getValueFromObj(trdata, fieldConfig.IsSelect) + '' == '1')
                                $(checkbox).attr("checked", "checked");
                        }
                        $(th).append(checkbox);	
                    } else if (fieldConfig.type == "action") {

                    }
                    else if (fieldConfig.type == 'imageRender') {
                        var img = document.createElement("img");
                        $(img).attr("src", fieldData);
                        $(th).append(img);
                    } else if (fieldConfig.type == "tableRender") {
                        //i（数据）,j（格式） 索引   循环 tr 当前的（在当前下一行设置） ，
                        //th 当前的 （在当前的）
                        this.tableRender(i, j, tr, th, config, fieldConfig, trdata);

                    } else if (fieldConfig.type == "cutsomerRender") {
                        fieldConfig.doRender.call(this, trdata, th, fieldConfig, i, tr, config, tableData.length);
                    } else if (fieldConfig.type == "tableLoad") {
                        this.tableLoad(i, j, th, tr, fieldConfig, trdata, config.fields.length);
                    }

                    else if (fieldConfig.type == "date") {
                        if (fieldData) {
                            if (fieldConfig.format) {
                                //var str = new Date(Date.parse(fieldData.replace(/-/g, "/"))).format(fieldConfig.format);
                            	var str = new Date(fieldData).format(fieldConfig.format);                            	
                                $(th).append(str);
                            } else {
                            	fieldData=fieldData.toString();
                            	fieldData=  fieldData.replace(/-/g,"/");
                            	var oDate1 = new Date(fieldData);
                                //var str = new Date(Date.parse(fieldData.replace(/-/g, "/"))).format('Y-m-d');
                                //var str = new Date(fieldData).format('yyyy-MM-dd');                            	
                                $(th).append(fieldData);
                            }
                        }
                    }
                    else if (!fieldConfig.type||fieldConfig.type == "text") {
                        $(th).append(fieldData);
                    }
                    else if (fieldConfig.type == "changeClick") {
                        fieldConfig.doRender.call(this, trdata, th, fieldConfig, i);
                        $(th).attr("width", "150");
                    }
                    else if (fieldConfig.type == "selectType") {
                        var divselect = document.createElement("div");
                        var divselectId = i + "_showDiv_select_" + fieldConfig.id;
                        $(divselect).attr("id", divselectId);
                        var divDataShow = this.getValueFromObj(trdata, fieldConfig.showData);
                        $(divselect).append(divDataShow);
                        $(th).append(divselect);
                        //
                        var inputselect = document.createElement("input");
                        $(inputselect).val(fieldData);
                        $(inputselect).attr("value", fieldData);
                        $(inputselect).hide();
                        $(th).append(inputselect);
                        ///下拉填充值
                        var select = document.createElement("select");
                        var selectId = i + "_select_" + fieldConfig.id;
                        $(select).attr("id", selectId);
                        $(select).hide();
                        this.selectLoad(fieldConfig, select, this, trdata);
                        $(th).append(select);


                    } else if (fieldConfig.type == "inputTextType") {
                        var divInput = document.createElement("div");
                        var divInputId = i + "_showDiv_input_" + fieldConfig.id;
                        $(divInput).attr("id", divInputId);
                        var inputData = this.getValueFromObj(trdata, fieldConfig.data);
                        $(divInput).append(inputData);
                        $(th).append(divInput);

                        // $(th).append(fieldData);

                        var input = document.createElement("input");
                        $(input).attr("id", i + "_input_" + fieldConfig.id);
                        if (fieldConfig.checkType == "money") {
                            $(input).keyup(function () { checkMoney(this); });
                        }
                        $(input).hide();
                        $(th).append(input);
                    }
                    else if (fieldConfig.type == "input") {
                        var input = document.createElement("input");
                        $(input).attr("id", i + "_input_" + fieldConfig.id);
                        if (fieldConfig["class"]) {
                        	$(input).addClass(fieldConfig["class"]);
                        }
                        if (fieldConfig.width) {
                            $(input).css("width", fieldConfig.width);
                        }
                        if (fieldConfig.checkType == "money") {
                            $(input).keyup(function () { checkMoney(this); });
                        } else if (fieldConfig.checkType == "int") {
                            $(input).attr("onkeyup", "value=value.replace(/[^\d]/g,'')");
                        }
                        $(input).val(fieldData);
                        $(th).append(input);
                    }
                    $(tr).append(th);
                }

            }
        }else{
        	var tr = document.createElement("tr");
        	var newTd = document.createElement("td");
            $(newTd).attr("colSpan", config.fields.length);
            $(newTd).css("text-align","center");
            $(newTd).html("没有记录");
            $(tr).append(newTd);

            $(tbody).append(tr);
        }
        if (data.paged&&data.paged.pageCnt) {
            var tr = document.createElement("tr");
            this.pageBar(tr, config.fields.length, data.paged, toPageParam);
            $(tfoot).append(tr);
        }


        $(table).append(thead);
        $(table).append(tbody);
        $(table).append(tfoot);
        //清空id对应的div

        if (id) {
            id.empty();
            //if (isChild) {
            // id.html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            // }
            id.append(table);
        }
        //是否要返回值
        var tabledata = { id: idtext + "_createDiv", data: data };
        return tabledata;

    },
    tableLoad: function (i, j, th, tr, fieldConfig, data, colSpanLength) {
        var me = this;
        //创建展开 操作
        var cdivId = fieldConfig.cdivId;
        var div = document.createElement("div");
        if (createTable.subThreeOver) {
            $(div).attr("id", i + "_" + j + cdivId + "_" + createTable.subThreeOver);
        } else {
            $(div).attr("id", i + "_" + j + cdivId);
        }

        $(div).attr("style", "cursor:pointer;");
        $(div).html('<img class="expand_gif" src="' + rootPath + '/images/zhankai.gif"/>');
        $(th).append(div);
        //二级tr
        var newtr = document.createElement("tr");
        if (createTable.subThreeOver) {
            $(newtr).attr("id", i + "_" + j + cdivId + "_" + createTable.subThreeOver + "_tr");
        } else {
            $(newtr).attr("id", i + "_" + j + cdivId + "_tr");
        }

        $(newtr).insertAfter(tr);
        $(newtr).hide();

        //创建 明细的td
        var newTd = document.createElement("td");
        $(newTd).attr("colSpan", colSpanLength);
        //创建 明细的table
        var divtDetail = document.createElement("div");
        if (createTable.subThreeOver) {
            $(divtDetail).attr("id", i + "_" + j + cdivId + "_" + createTable.subThreeOver + "_divHtml");
        } else {
            $(divtDetail).attr("id", i + "_" + j + cdivId + "_divHtml");
        }

        $(divtDetail).attr("style", "padding:0px");

        $(newTd).append(divtDetail);
        $(newtr).append(newTd);


        $(div).click(function () {
            var div_id = $(this).attr("id");
            var divHtml = $(this).html();
            if (divHtml.indexOf("expand_gif") > 0) {
                $("#" + div_id + "_tr").show();
                var img = $(this).find("img");
                img.attr("class", "packup_gif");
                //$(this).html('<img class="packup_gif" src="../Resources/Images/zhankai.gif"/>');

                var params = fieldConfig.param;
                var param = {};
                var queryConditions="";//拼接额外条件
                param.action = fieldConfig.action;
                for (var parami = 0; parami < params.length; parami++) {
                    var paramVal = params[parami];
                    param[paramVal] = me.getValueFromObj(data, paramVal);
                    //var paramData = me.getValueFromObj(data, paramVal);
                    //queryConditions+="  and  "+paramVal+"= '"+paramData+"' "; 
                }
                if(params&&params.length>0){
                	var paramNames=[].concat(params);;
                	var paramIndex= paramNames.indexOf("id");
                	paramNames.splice(paramIndex,1);
                	param["paramNames"]=paramNames+"";
                }
                if(fieldConfig.subCode){
                	param["code"]=fieldConfig.subCode;
                	if(fieldConfig.subCondition)
                		param["subCondition"]=fieldConfig.subCondition;
                }
                if(fieldConfig.queryConditions){
                	queryConditions+=fieldConfig.queryConditions;
                }
                param["queryConditions"]=queryConditions;
                var gettables = $("#" + div_id + "_divHtml table");
                if (!gettables || gettables.length < 1) {
                    $.post(
                    fieldConfig.url,
                    param,
                    function (rsp) {
                        if (rsp) {
                            var detailsdata =rsp;// eval("(" + rsp + ")");
                            //var data1 = $.evalJSON(rsp);
                            //alert(1)

                            createTable.selectLoadData = null;
                            if (!createTable.subThreeOver || createTable.subThreeOver != div_id)
                                createTable.subThreeOver = div_id;
                            if (detailsdata.code + "" == "1") {
                            	if(typeof (detailsdata.results) === "string"){
                            		detailsdata.results=eval("("+detailsdata.results+")"); 
                            	}
                                createTable.registTable($("#" + div_id + "_divHtml"), fieldConfig.details, detailsdata);
                            } else {
                                $("#" + div_id + "_divHtml").html("<div style='text-align: center'>没有数据</div>");
                            }
                        }
                    }
                );
                }
            } else {
                $("#" + div_id + "_tr").hide();
                $(this).html('<img class="expand_gif" src="' + rootPath + '/images/zhankai.gif"/>');
            }

        });
    },
    //二级不用load
    tableRender: function (i, j, tr, th, config, fieldConfig, trdata) {
        //创建展开 操作
        var div = document.createElement("div");
        $(div).attr('id', i + "_" + j);
        $(div).html('<img class="expand_gif"  src="' + rootPath + '/images/zhankai.gif"/>');
        $(th).append(div);
        //创建 明细的tr
        var newtr = document.createElement("tr");
        $(newtr).attr("id", i + "_" + j + "_tr");
        $(newtr).insertAfter(tr);
        $(newtr).hide();
        //创建 明细的td
        var newTd = document.createElement("td");
        $(newTd).attr("colSpan", config.fields.length);
        //创建 明细的table
        var divtDetail = document.createElement("div");
        $(divtDetail).attr("id", i + "_" + j + "_div");

        $(divtDetail).attr("style", "margin:10px");
        if (trdata[fieldConfig.data + ""])
            this.registTable($(divtDetail), fieldConfig.details, trdata[fieldConfig.data + ""], true);
        //$(divtDetail).append("<br/>")
        $(newTd).append(divtDetail);
        $(newtr).append(newTd);

        $(div).click(function () {
            var div_id = $(this).attr("id");
            var divHtml = $(this).html();
            var img = $(this).find("img");
            if ($(img).attr("class") != 'packup_gif') {
                img.removeClass();
                img.addClass("packup_gif");
            } else {
                img.removeClass();
                img.addClass("expand_gif");
            }
            $("#" + div_id + "_tr").toggle();
        });
        if (config.display) {
            $(newtr).show();
        }
    },
    pageBar: function (newtr, colspan, pageData, toPageParam) {
        //创建 明细的tr
        //var newtr = document.createElement("tr");

        //$(newtr).hide();
        //创建 明细的td
        var newTd = document.createElement("td");
        $(newTd).attr("colSpan", colspan);
        //创建 明细的table
        var divtDetail = document.createElement("div");
        $(divtDetail).attr("id", "pageBar_div");

        $(divtDetail).attr("style", "margin:10px");

        var s = "第 " + pageData.pageIndex + "/" + pageData.pageCnt + "页,本页 " + pageData.lcsCount + " 条,共 " + pageData.cordCnt + " 条&nbsp;&nbsp;";
        $(divtDetail).html(s);

        var homePageElement = document.createElement("a");
        $(homePageElement).attr("href", "javascript:" + toPageParam + "('1')");
        $(homePageElement).text("首页");
        $(divtDetail).append(homePageElement);
        var pagem = pageData.pagem;
        var str = pagem.split("_");
         
        var pageCnt=pageData.pageCnt;
        var pageIndex=pageData.pageIndex;
        var pageStart=1; 
        var pageEnd=20;
        if(pageIndex<20){
        	if(pageEnd>pageCnt)
        		pageEnd=pageCnt;
        }else {
        	//var pageSurplus= pageCnt-pageIndex;
        	var pageSurplus=pageCnt-(pageIndex+9);
        	if(pageSurplus>0){
        		pageEnd=pageIndex+9;
        		if((pageIndex-9)>0){        			
        			pageStart=pageIndex-9;
        		}else{
        			pageStart=1;
        		} 
        	}else {
        		pageEnd=pageCnt;
        		
        		var absSurplus= Math.abs(pageSurplus);
        		var pageStartSurplus= pageIndex-9-absSurplus;
        		if(pageStartSurplus>0)
        			pageStart=pageStartSurplus;
        		else 
        			pageStart=1;
        	}
        } 
        //for (var i = ((pageIndex - 1) / 10) * 10 + 1; i <= ((pageIndex) / 10) * 10 + 20 && i <= pageCnt; i++)
        for (var i = pageStart; i <= pageEnd; i++)
        {
        		$(divtDetail).append("  ");
                var pagemea = document.createElement("a");
                if (i + "" == "" + pageData.pageIndex) {
                    var fun = "void(0)";
                    $(pagemea).attr("style", "text-decoration:none;color:#999;");
                } else {
                    var fun = toPageParam + "('" + i + "')";
                }
                $(pagemea).attr("href", "javascript:" + fun);
                $(pagemea).text(i);
                $(divtDetail).append(pagemea); 
        }
        var fun2 = toPageParam + "('" + pageData.pageCnt + "')";
        var endPageElement = document.createElement("a");
        $(endPageElement).attr("href", "javascript:" + fun2);
        $(endPageElement).text(" 末页");
        $(divtDetail).append(endPageElement);


        $(newTd).append(divtDetail);
        $(newtr).append(newTd);
    },

    ////下拉列表的集合填充
    selectLoad: function (fieldConfig, select, me, data) {
        if (fieldConfig.loadType == "one") { //一次
            if (!createTable.selectLoadData) {
                var params = fieldConfig.param;
                var param = {};
                param.action = fieldConfig.action;
                for (var parami = 0; parami < params.length; parami++) {
                    var paramVal = params[parami];
                    param[paramVal] = me.getValueFromObj(data, paramVal);
                }

                $.ajax({
                    type: "POST",
                    url: fieldConfig.url,
                    data: param,
                    async: false,
                    success: function (rsp) {
                        if (rsp) {
                            createTable.selectLoadData = eval("(" + rsp + ")");
                            if (createTable.selectLoadData && createTable.selectLoadData.code == 1) {
                                $.each(createTable.selectLoadData.results, function (i, data) {
                                    $(select).append("<option value='" + data.id + "'>" + data.name + "</option>");
                                });
                            }
                        }
                    }
                });


            } else {
                $.each(createTable.selectLoadData.results, function (i, data) {
                    $(select).append("<option value='" + data.id + "'>" + data.name + "</option>");
                });
            }


        } else if (fieldConfig.loadType == "Each") {//每次
            var params = fieldConfig.param;
            var param = {};
            param.action = fieldConfig.action;
            for (var parami = 0; parami < params.length; parami++) {
                var paramVal = params[parami];
                param[paramVal] = me.getValueFromObj(data, paramVal);
            }

            $.ajax({
                type: "POST",
                url: fieldConfig.url,
                data: param,
                async: false,
                success: function (rsp) {
                    if (rsp) {
                        var selectLoadData = eval("(" + rsp + ")");
                        if (selectLoadData) {
                            if (selectLoadData && selectLoadData.code == 1) {
                                $.each(selectLoadData.results, function (i, data) {
                                    $(select).append("<option value='" + data.id + "'>" + data.name + "</option>");
                                });
                            }
                        }
                    }
                }
            });
        }
    },
    getValueFromObj: function (obj, name) {
        if (!name || !obj)
            return null;
        var dotPos = name.indexOf(".");
        if (dotPos < 0) {
            if (obj[name] == undefined || obj[name] == null)
                return null;
            return obj[name];
        } else {
            if (obj[name])
                return obj[name];
            var subObjName = name.substring(0, dotPos);
            var subSubObjName = name.substring(dotPos + 1);
            var subobj = obj[subObjName];
            if (!subobj)
                return null;
            return this.getValueFromObj(obj[subObjName], subSubObjName);
        }
    }
};

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
    };
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
        RegExp.$1.length == 1 ? o[k] :
        ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

//验证输入是否是金额
function checkMoney(me) {
    var value = $(me).val();
    //var patrn = /^(([1-9]{1}\d*)|([0]{1}))?$/;
    //$(me).val(value.replace(/[^0-9]/g, ''));
    var patrn = /^(([1-9]{1}\d*)|([0]{1}))?(\.)?((\d){1,2})?$/;
    $(me).val(value.replace(/[^0-9(\.)]/g, ''));
    var currVal = $(me).val();
    if (currVal.length > 0 && !patrn.exec(currVal)) {
        $(me).val(currVal.substring(0, currVal.length - 1));
    }
}

