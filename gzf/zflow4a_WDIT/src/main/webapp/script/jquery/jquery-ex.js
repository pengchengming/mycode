jQuery.extend( {
//	toJSON:function(o){
//		return Ext.util.JSON.encode(o);
//	},
//
//	evalJSON:function(encoded){
//		return Ext.util.JSON.decode(encoded);
//	},
	objClone : function(o) {
		if (o == null)
			return null;
		var returnObj = {};
		var baseClone = function(old) {
			var ret = {};
			var otype = typeof (old);
			if (otype == "number" || otype == "boolean" || otype == "string") {
				ret = old;
				return ret;
			}
			if (old.constructor === Date) {
				return new Date(old.getTime());
			}
			if (old.constructor === Array) {
				ret = new Array();
				for ( var i = 0; i < old.length; i++) {					
					var child = baseClone(old[i]);
					ret.push(child);
				}
			} else {
				for (prop in old) {
					var value = old[prop];
					var type = typeof (value);
					if (value === null || type == "undefined")
						ret[prop] = null;
					else if (type == "number" || type == "boolean")
						ret[prop] = value;
					else if (type == "string")
						ret[prop] = value;
					else if (type == 'object') {
						if (value.constructor === Date) {
							ret[prop] = new Date(value.getTime());
						} else if (value.constructor === Array) {
							ret[prop] = baseClone(value);
						} else {
							ret[prop] = baseClone(value);
						}
					}
				}
			}
			return ret;
		};
		returnObj = baseClone(o);
		return returnObj;
	},
	parseHtmlToText : function(str) {
		var retStr = $.trim(str);
		retStr = retStr.replace(/<br\s*\/{0,1}>/ig, "\n");
		retStr = retStr.replace(/&nbsp;\s*/gi, " ");
		return (retStr);
	},
	parseTextToHtml : function(str) {
		if (str == null) {
			str = "";
		}
		var retStr = $.trim(str);
		//		retStr = retStr.replace(/\n/g, "<br/>");
		//		retStr = retStr.replace(/\s/g, "&nbsp;");
		if(str.length==0)
			return "&nbsp;";
		return (retStr);
	},
	getValueFromObj : function(obj, name) {
		if (!name || !obj)
			return null;
		var dotPos = name.indexOf(".");
		if (dotPos < 0) {
			if (obj[name]==undefined || obj[name]==null)
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
	},
	setValueToObj : function(obj, name, value) {
		var dotPos = name.indexOf(".");
		if (dotPos < 0) {
			if (!obj[name])
				obj[name] = null;
			obj[name] = value;
		} else {
			var subObjName = name.substring(0, dotPos);
			var subSubObjName = name.substring(dotPos + 1);
			var subobj = obj[subObjName];
			if (!subobj) {
				obj[subObjName] = {};
			}
			this.setValueToObj(obj[subObjName], subSubObjName, value);
		}
	}
});

Date.prototype.format = function(pattern) {
	var wholePattern = "y-MM-dd HH:mm:ss";
	var year = /(y+)/;
	var month = /(M+)/;
	var day = /(d+)/;
	var hour = /(H+)/;
	var minute = /(m+)/;
	var second = /(s+)/;
	if (year.test(pattern)) {
		var result = year.exec(pattern);
		var length = result[0].length;
		pattern = pattern.replace(year, (this.getFullYear() + "")
			.substring(4 - length));
	}
	if (month.test(pattern)) {
		var result = month.exec(pattern);
		pattern = pattern.replace(month, this.getMonth() + 1 > 9 ? (this
			.getMonth() + 1 + "") : "0" + (this.getMonth() + 1));
	}
	if (day.test(pattern)) {
		var result = day.exec(pattern);
		pattern = pattern.replace(day, this.getDate() > 9 ? (this
			.getDate() + "") : "0" + (this.getDate()));
	}

	if (hour.test(pattern)) {
		var result = hour.exec(pattern);
		pattern = pattern.replace(hour, this.getHours() > 9 ? (this
			.getHours() + "") : "0" + (this.getHours()));
	}
	
	if (minute.test(pattern)) {
		var result = minute.exec(pattern);
		pattern = pattern.replace(minute, this.getMinutes() > 9 ? (this
			.getMinutes() + "") : "0" + (this.getMinutes()));
	}

	if (second.test(pattern)) {
		var result = second.exec(pattern);
		pattern = pattern.replace(second, this.getSeconds() > 9 ? (this
			.getSeconds() + "") : "0" + (this.getSeconds()));
	}
	return pattern;
};
