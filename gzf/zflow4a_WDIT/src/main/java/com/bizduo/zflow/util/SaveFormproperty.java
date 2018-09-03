package com.bizduo.zflow.util;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.tabulation.MiddleTable;
import com.bizduo.zflow.domain.tabulation.TableProperty;

public class SaveFormproperty {
	//修改对象
	public static  List<FormProperty> getUpdateFormPropertyList(Form form, List<FormProperty> jsonProps) {
		List<FormProperty> props = new ArrayList<FormProperty>();
		//遍历前台传入的数据
		for(FormProperty jsonProp : jsonProps){
			boolean isupdate = false;//判断新增修改
			FormProperty temp = new FormProperty();
			//遍历数据库中的数据
			if(form.getPropertyList()!=null&&form.getPropertyList().size()>0){
				for(FormProperty prop : form.getPropertyList()){
					//如果FieldId相等为修改
					if(jsonProp.getFieldId() == prop.getFieldId()){
						//数据库中的数据
						temp.setId(prop.getId());
						temp.setFieldId(prop.getFieldId());
						temp.setIsDelete(prop.getIsDelete());
						//前台传入的数据
						isupdate=true;
					}
				}
			}//为新增
			if(!isupdate)
				temp.setFieldId(jsonProp.getFieldId());
			temp.setFieldLength(jsonProp.getFieldLength());
			temp.setMinLength(jsonProp.getMinLength());
			temp.setFieldName(jsonProp.getFieldName());
			temp.setFieldType(jsonProp.getFieldType());
			temp.setComment(jsonProp.getComment());
			temp.setDictionaryCode(jsonProp.getDictionaryCode());
			temp.setExtraAttributes(jsonProp.getExtraAttributes());
			temp.setElementType(jsonProp.getElementType());
			temp.setForeignKey(jsonProp.getForeignKey());
			temp.setBindJs(jsonProp.getBindJs());
			temp.setEmpty(jsonProp.getEmpty());
			temp.setValidator(jsonProp.getValidator());
			temp.setForm(form);
			props.add(temp);
		}
		return props;
		}

		/**
		 * FormProperty
		 * @param tempJsonObj
		 * @param Property
		 * @return
		 * 2012-4-27
		 * @author lm
		 */
	public static List<FormProperty> getformPropertyListByJson(Form form,JSONArray tempJsonObj) {
	    	List<FormProperty> temp = new ArrayList<FormProperty>();
	    	try {
	    		for(int i = 0; i < tempJsonObj.length(); i++){
	    			JSONObject tempObj = tempJsonObj.getJSONObject(i);//json对象
	    			//定义对象
	    			FormProperty formProperty= (FormProperty)JsonToObjectUtil.toObject(tempObj.toString(), FormProperty.class);
	    			if(tempObj.has("extraAttributes")){
	    				String extraAttributes=tempObj.get("extraAttributes").toString();
	    				formProperty.setExtraAttributes(extraAttributes);
	    			}
	    			String fieldType = formProperty.getFieldType();//类型
	    			Integer fieldLength = formProperty.getFieldLength(); //长度
	    			if(fieldType != null && !fieldType.trim().equals("")){
	    				if(fieldLength != null){//如果是String 类型，并且长度长度大于255 就改变数据的类型
	    					if(fieldType.equals("attrString")&&Integer.parseInt(fieldLength.toString())>255)
	    						fieldType="longtext";
	    				}
	    			}
	    			if(fieldLength == null) fieldLength = 255;
	    			formProperty.setFieldLength(fieldLength);
	    			formProperty.setFieldType(fieldType);
	    			formProperty.setForm(form);
	    			temp.add(formProperty);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return temp;
		}

		
		/**
		 * MiddleTable
		 * @param tempJsonObj
		 * @param tableList
		 * @return
		 * 2012-4-27
		 * @author lm
		 */
	public static List<MiddleTable> getMiddleTableByJson(Form form,JSONArray tempJsonObj) {
	    	List<MiddleTable> middleTableList = new ArrayList<MiddleTable>();
	    	try {
	    		for(int i = 0; i < tempJsonObj.length(); i++){
	    			JSONObject tempJsonObject = tempJsonObj.getJSONObject(i);
	    			MiddleTable middleTable = (MiddleTable)JsonToObjectUtil.toObject(tempJsonObject.toString(), MiddleTable.class);
	    			List<TableProperty> tablePropertyList = new ArrayList<TableProperty>();
	    			org.json.JSONArray tablePropertyListJsonArray = tempJsonObject.getJSONArray("tablePropertyList");
	    			for(int j= 0; j < tablePropertyListJsonArray.length(); j++){
	    				JSONObject tempObj = tablePropertyListJsonArray.getJSONObject(j);
	    				//定义对象
	    				TableProperty tableProperty = (TableProperty)JsonToObjectUtil.toObject(tempObj.toString(), TableProperty.class);
	    				if(tempObj.has("extraAttributes")&&!tempObj.getString("extraAttributes").toString().trim().equals("")){
	    					String extraAttributes = tempObj.getJSONObject("extraAttributes").toString();
		    				tableProperty.setExtraAttributes(extraAttributes);	    					
	    				}
	    				/** 处理类型和长度*/
		    			String fieldType = tableProperty.getTablePropertyType();
		    			Long fieldLength = tableProperty.getTablePropertyLength();
		    			if(fieldType != null && !fieldType.trim().equals("")){
		    				if(fieldLength != null){
		    					if(fieldType.equals("attrString")&&Integer.parseInt(fieldLength.toString())>255)
		    						fieldType="longtext";
		    				}
		    			}
		    			if(fieldLength == null) fieldLength = 255l;
		    			tableProperty.setTablePropertyType(fieldType);
		    			tableProperty.setTablePropertyLength(fieldLength);
		    			tableProperty.setMiddleTable(middleTable);
	    				tablePropertyList.add(tableProperty);
	    			}
	    			middleTable.setTablePropertyList(tablePropertyList);
	    			middleTable.setForm(form);
	    			middleTableList.add(middleTable);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return middleTableList;
		}
	/**
	 * 根据formproperty的集合获取IDs
	 */
	public static Long[] formpropertyIds(List<FormProperty> formPropertyList){
		Long[] ids = new Long[formPropertyList.size()];
		for (int i = 0; i < formPropertyList.size(); i++) {
			Long id = formPropertyList.get(i).getId();
			if(id != null)
				ids[i] = id;
		}
		return ids;
	}
	/**
	 * 根据TableProperty的集合获取IDs
	 */
	public static Long[] tablePropertyIds(List<TableProperty> tablePropertyList){
		Long[] ids = new Long[tablePropertyList.size()];
		for (int i = 0; i < tablePropertyList.size(); i++) {
			Long id = tablePropertyList.get(i).getId();
			if(id != null)
				ids[i] = id;
		}
		return ids;
	}
}
