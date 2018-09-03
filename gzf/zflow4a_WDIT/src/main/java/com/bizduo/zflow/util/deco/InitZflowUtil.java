package com.bizduo.zflow.util.deco;

import java.util.List;
import java.util.Map;

import com.bizduo.zflow.domain.bizType.DataDictionaryValue;
import com.bizduo.zflow.domain.form.ExcelTableList;
import com.bizduo.zflow.domain.form.FormView;
import com.bizduo.zflow.domain.form.SelectTableList;

public class InitZflowUtil {
	//FormView
	public static Map<String,FormView> staticFormViewMap;
	//SelectTableList
	public static Map<String,SelectTableList> staticSelectTableListMap;
	//staticDictionary
	public static Map <String , List<DataDictionaryValue>> staticDictionaryMap;
	//excelTableList
	public static Map<String,ExcelTableList> staticExcelTableListMap;
	//packageItem
	public static Map<String,List<Map<String, Object>>> staticPackageItemListMap;
	
}
