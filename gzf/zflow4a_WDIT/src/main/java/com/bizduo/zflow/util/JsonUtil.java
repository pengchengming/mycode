package com.bizduo.zflow.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
/**
 * 功能描述： 处理json的工具类，负责json数据转换成java对象和java对象转换成json<br>
 */

public class JsonUtil {
	/**
	 * 从一个JSON 对象字符格式中得到一个java对象
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	public static Object getObject4JsonString(String jsonString, Class<Object> pojoCalss) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return JSONObject.toBean(jsonObject, pojoCalss);
	}

	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator<String> keyIter = jsonObject.keys();
		String key;
		Object value;
		Map<String, Object> valueMap = new HashMap<String, Object>();
		while(keyIter.hasNext()){
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}
		return valueMap;
	}

	/**
	 * 将一个java对象转换为object对象
	 * 
	 * @param object
	 * @return
	 * @author:shizhongwen
	 * @date:2012-10-29
	 */
	public static Map<String, Object> getMap4Object(Object object) {
		String josn = getJsonString4JavaPOJO(object);
		return getMap4Json(josn);
	}

	/**
	 * 从json数组中得到相应java数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArray4Json(String jsonString) {
		return JSONArray.fromObject(jsonString).toArray();
	}

	/**
	 * 从json对象集合表达式中得到一个java对象列表
	 * 
	 * @param jsonString
	 * @param pojoClass
	 * @return
	 */
	public static List<Object> getList4Json(String jsonString, Class<Object> pojoClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);
		}
		return list;
	}

	/**
	 * 从json数组中解析出java字符串数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String[] getStringArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}
		return stringArray;
	}

	/**
	 * 从json数组中解析出javaLong型对象数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Long[] getLongArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Long[] longArray = new Long[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			longArray[i] = jsonArray.getLong(i);
		}
		return longArray;
	}

	/**
	 * 从json数组中解析出java Integer型对象数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Integer[] getIntegerArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Integer[] integerArray = new Integer[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			integerArray[i] = jsonArray.getInt(i);
		}
		return integerArray;
	}

	/**
	 * 从json数组中解析出java Integer型对象数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Double[] getDoubleArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Double[] doubleArray = new Double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			doubleArray[i] = jsonArray.getDouble(i);
		}
		return doubleArray;
	}

	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param javaObj
	 * @return
	 */
	public static String getJsonString4JavaPOJO(Object javaObj) {
		return JSONObject.fromObject(javaObj).toString();
	}

	public static String getJsonString4JavaArray(Object[] objects) {
		return JSONArray.fromObject(objects).toString();
	}

	/**
	 * 将java对象转换成json字符串,并设定日期格式
	 * 
	 * @param javaObj
	 * @param dataFormat
	 * @return
	 */
	public static String getJsonString4JavaPOJO(Object javaObj,
			String dataFormat) {
		JsonConfig jsonConfig = configJson(dataFormat);
		return JSONObject.fromObject(javaObj, jsonConfig).toString();
	}

	/**
	 * JSON 时间解析器具
	 */
	public static JsonConfig configJson(String datePattern) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "" });
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor(datePattern));
		return jsonConfig;
	}

	/**
	 * 除去不想生成的字段（特别适合去掉级联的对象）+时间转换
	 * 
	 * @param excludes
	 *            除去不想生成的字段
	 * @param datePattern
	 * @return
	 */
	public static JsonConfig configJson(String[] excludes, String datePattern) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(true);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor(datePattern));
		return jsonConfig;
	}

	/**
	 * 根据LIST，和总数向页面打印列表数据
	 */
	public static void returnJsonListData(HttpServletResponse response,
			List<Object> list, int count, String name) {
		Map<String, Object> mapJson = new Hashtable<String, Object>();
		mapJson.put("total", count);
		mapJson.put("rows", list);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor("yyyy-MM-dd hh:mm:ss"));

		JSONObject jsonObject = JSONObject.fromObject(mapJson, jsonConfig);
		System.out.print(jsonObject);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
//			LOGGER.error(LogUtil.getLogStr("IOException", "500", "", "", e.getMessage()),e);
		}
		out.print(jsonObject);
		out.close();
	}

	/**
	 * 向页面回去data字符串
	 */
	public static void returnJsonStringData(HttpServletResponse response, String data){
		PrintWriter out = null;
		try{
			out = response.getWriter();
		}catch (IOException e){
//			LOGGER.error(LogUtil.getLogStr("IOException", "500", "", "", e.getMessage()),e);
		}
		out.write(data);
		out.close();
	}

	/**
	 * 将list转换为json字符串
	 */
	public static String getJsonArray4JavaList(List<Object> list) {
		JSONArray jsonArray = JSONArray.fromObject(list);
		String jsonArrayStr = jsonArray.toString();
		jsonArrayStr = jsonArrayStr.substring(jsonArrayStr.indexOf("["));
		return jsonArray.toString();
	}

	/**
	 * 拼接饼图数据
	 *  
	 * @param resultList
	 * @return List<Map<String,Object>>
	 * @author:Jiyong.Wei 
	 * @date:2013-4-9
	 */
	public static List<Map<String,Object>> getPieData(List<Map<String,Object>> resultList){
		List<Map<String,Object>> result; 
		if(null == resultList || 0 >= resultList.size()){
			return null;
		}
		result=new ArrayList<Map<String,Object>>();
		int size = resultList.size();
		for(int i = 0; i < size; i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("desc", Validation.toString(resultList.get(i).get("description")));
			map.put("value", Validation.toInteger(resultList.get(i).get("count")));
			result.add(map);
		}
		return result;
	}
	
	public static org.json.JSONArray listChangeToJSONArray(List<Map<String, Object>> list){
		org.json.JSONArray array = new org.json.JSONArray();
		for(Map<String, Object> map : list){
			array.put(mapChangeToJSONObject(map));
		}
		return array;
	}
	public static org.json.JSONObject mapChangeToJSONObject(Map<String, Object> map){
		org.json.JSONObject obj = new org.json.JSONObject();
		for(Iterator<String> it = map.keySet().iterator(); it.hasNext();){
			String key = it.next();
			obj.put(key, map.get(key));
		}
		return obj;
	}
	/**
	 * 仅用于全套餐报价调整
	 * @param array
	 * @param parent
	 * @return
	 */
	public static org.json.JSONArray getByParent(org.json.JSONArray array, org.json.JSONObject parent){
		org.json.JSONArray temp = new org.json.JSONArray();
		for(int i = 0; i < array.length(); i++){
			org.json.JSONObject obj = (org.json.JSONObject) array.get(i);
			if(parent.getInt("id") == obj.getInt("detailId"))
				temp.put(obj);
		}
		return temp;
	}
	/**
	 * 仅用于清工辅料报价调整
	 * @param array
	 * @param parent
	 * @return
	 */
	public static org.json.JSONArray getByParent2(org.json.JSONArray array, org.json.JSONObject parent){
		org.json.JSONArray temp = new org.json.JSONArray();
		for(int i = 0; i < array.length(); i++){
			org.json.JSONObject obj = (org.json.JSONObject) array.get(i);
			if(parent.getInt("id") == obj.getInt("type"))
				temp.put(obj);
		}
		return temp;
	}
}
