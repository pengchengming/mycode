package com.bizduo.zflow.controller.bizType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.bizType.DataDictionaryCode;
import com.bizduo.zflow.domain.bizType.DataDictionaryType;
import com.bizduo.zflow.domain.bizType.DataDictionaryValue;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.bizType.IDataDictionaryService;
import com.bizduo.zflow.util.JsonToObjectUtil;
import com.bizduo.zflow.util.ccm.UploadFileStatus;
import com.bizduo.zflow.util.deco.InitZflowUtil;
@Controller
@RequestMapping("/dictionarys")
public class DataDictionaryController{
	@Autowired
	private IDataDictionaryService dataDictionaryService;
	@Autowired
	private IBizTypeService  bizTypeService;
	/**
	 * 查询类别
	 * @return
	 */
	@RequestMapping(value="/dataDictionaryTypeList.do")
	@ResponseBody
	public Map<String,Object> dataDictionaryTypeList(){
		Map<String,Object> map=new HashMap<String, Object>();
		List<DataDictionaryType> dataDictionaryTypeALLList = this.dataDictionaryService.dataDictionaryTypeAllList();
		map.put("option", dataDictionaryTypeALLList);
		return map;
	}
	@RequestMapping(value="/dictionaryTypeById.do")
	@ResponseBody
	public DataDictionaryType dictionaryTypeById(
			@RequestParam(value="dictionaryTypeid",required=true) Long dictionaryTypeid
			) throws Exception{
		DataDictionaryType dictionaryType=(DataDictionaryType)this.dataDictionaryService.findObjByKey(DataDictionaryType.class,dictionaryTypeid);
		return dictionaryType;
	}
	/**
	 * 2.	添加/修改类别
	 */
	@RequestMapping(value="/addOrUpdateDictionaryType.do")
	@ResponseBody
	public Map<String, Object>  addOrUpdateDictionaryType(
			@RequestParam(value="code",required=true)String code,
			@RequestParam(value="displayValue",required=false) String displayValue,
			@RequestParam(value="dictionaryTypeid" ,required=false) Long dictionaryTypeid
		){
		//json数 据  
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			DataDictionaryType dataDictionaryType=new DataDictionaryType();
			dataDictionaryType.setCode(code);
			dataDictionaryType.setName(displayValue);
			if(dictionaryTypeid !=null){//修改
				//判断Code是否重复
				DataDictionaryType dataDictionaryTypeisnull=this.dataDictionaryService.getdictionaryTypeByIdisnull(dictionaryTypeid,code);
				if(dataDictionaryTypeisnull!=null){
					map.put("errorMsg", "修改失败,编号已经存在,请重新填写编号");
					return map;
				}
				dataDictionaryType.setId(dictionaryTypeid);
				//this.dataDictionaryService.updateDictionaryType(dataDictionaryType);
				this.dataDictionaryService.update(dataDictionaryType);
				map.put("successMsg", "修改成功");
				return map;
			}else {//保存
				//判断Code是否重复
				DataDictionaryType dataDictionaryTypeisnull=this.dataDictionaryService.getdictionaryTypeByIdisnull(null,code);
				if(dataDictionaryTypeisnull!=null){
					map.put("errorMsg", "修改失败,编号已经存在,请重新填写编号");
					return map;
				}
				//this.dataDictionaryService.saveDictionaryType(dataDictionaryType);
				this.dataDictionaryService.create(dataDictionaryType);
				map.put("successMsg", "新增成功");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "操作失败");
			return map;
		}
	}
	/**
	 * 3.	删除数据
	 * @return
	 */
	@RequestMapping(value="/deleteDictionaryType.do")
	@ResponseBody
	public Map<String, Object> deleteDictionaryType(
			@RequestParam(value="dictionaryTypeid" ,required=true) Long dictionaryTypeid
			){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			//this.dataDictionaryService.deleteDictionaryType(dictionaryTypeid );
			this.dataDictionaryService.delete(DataDictionaryType.class,dictionaryTypeid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "操作失败");
			return map;
		}
		map.put("successMsg", "删除成功");
		return map;
	}
	/**
	 * 1.	查询代码
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/dataDictionaryCodeList.do")
	@ResponseBody
	public List<DataDictionaryCode>  dataDictionaryCodeList(
			@RequestParam(value="code",required=true) String code,
			@RequestParam(value="dictionaryCode",required=false) String dictionaryCode,
			@RequestParam(value="dictionaryValue" ,required=false) String dictionaryValue
		){
		Map map=new HashMap();
		map.put("code", code);
		map.put("dictionaryCode", dictionaryCode);
		map.put("dictionaryValue", dictionaryValue);
		List<DataDictionaryCode> dataDictionaryCodeALLList=this.dataDictionaryService.getDictionaryCodeByTypeCode(map);
		return dataDictionaryCodeALLList;
	}
	/**
	 * 2.	显示单个为修改
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/dictionaryCodeById.do")
	@ResponseBody
	public DataDictionaryCode dictionaryCodeById(
			@RequestParam(value="dictionaryCodeid",required=false) Long dictionaryCodeid
		) throws Exception{
		DataDictionaryCode dataDictionaryCode =(DataDictionaryCode)this.dataDictionaryService.findObjByKey(DataDictionaryCode.class,dictionaryCodeid);		
		dataDictionaryCode.setDataDictionaryTypeCode(dataDictionaryCode.getDataDictionaryType().getCode());
		dataDictionaryCode.setDataDictionaryType(null);
		return dataDictionaryCode;
	}
	/**
	 * 2.	添加/修改代码
	 * @return
	 */
	@RequestMapping(value="/addOrUpdateDictionaryCode.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addOrUpdateDictionaryCode(
			@RequestParam(value="dictionaryTypeid",required=true) Long dictionaryTypeid,
			@RequestParam(value="dictionaryCodeid",required=true) Long dictionaryCodeid,
			@RequestParam(value="code",required=true) String code,
			@RequestParam(value="displayValue" ,required=false) String displayValue,
			@RequestParam(value="type",required=false) String type,
			@RequestParam(value="isupdate",required=false) Integer isupdate
		){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			DataDictionaryCode dataDictionaryCode=new DataDictionaryCode();
			dataDictionaryCode.setCode(code);
			dataDictionaryCode.setDisplay(displayValue);
			dataDictionaryCode.setType(type);
			
			if(isupdate!=null){
				if(isupdate==0)
					dataDictionaryCode.setIsupdate(true);
				else 
					dataDictionaryCode.setIsupdate(false);
			}
			else
				dataDictionaryCode.setIsupdate(true);
			
			if(dictionaryTypeid!=null){
				//DataDictionaryType dataDictionaryType =this.dataDictionaryService.getDictionaryCodeByTypeId(dictionaryTypeid);
				DataDictionaryType dataDictionaryType =(DataDictionaryType)this.dataDictionaryService.findObjByKey(DataDictionaryType.class,dictionaryTypeid);
				dataDictionaryCode.setDataDictionaryType(dataDictionaryType);
			}
			if(dictionaryCodeid !=null&&dictionaryCodeid!=0l){//修改
				//判断Code是否重复
				DataDictionaryCode dictionaryCodeValueisnull=this.dataDictionaryService.getdictionaryCodeByIdisnull(dictionaryTypeid,dictionaryCodeid,code);
				if(dictionaryCodeValueisnull!=null){
					map.put("errorMsg", "修改失败,编号已经存在,请重新填写编号");
					return map;
				}
				dataDictionaryCode.setId(dictionaryCodeid);
				//this.dataDictionaryService.updateDataDictionaryCode(dataDictionaryCode);
				this.dataDictionaryService.update(dataDictionaryCode);
				map.put("successMsg", "修改成功");
			}else {
				///判断Code是否重复
				DataDictionaryCode dictionaryCodeTypeisnull=this.dataDictionaryService.getdictionaryCodeByIdisnull(dictionaryTypeid,null,code);
				if(dictionaryCodeTypeisnull!=null){
					map.put("errorMsg", "修改失败,编号已经存在,请重新填写编号");
					return map;
				}
				this.dataDictionaryService.create(dataDictionaryCode);
				map.put("successMsg", "新增成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg","操作失败");
			return map;
		}
		return map;
	}
	/**
	 * 3.	删除数据代码
	 * @return
	 */
	@RequestMapping(value="/deleteDictionaryCode.do")
	@ResponseBody
	public Map<String, Object> deleteDictionaryCode(
			@RequestParam(value="dictionaryCodeid",required=true)Long dictionaryCodeid
		){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			this.dataDictionaryService.delete(DataDictionaryCode.class,dictionaryCodeid);
			map.put("successMsg", "删除成功");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "删除失败");
			return map;
		}
	}
	/**
	 * 查询代码值
	 * @return
	 */
	@RequestMapping(value="/dataDictionaryValueList.do")
	@ResponseBody
	public List<DataDictionaryValue>  dataDictionaryValueList(
			@RequestParam(value="code",required=true)String code
			){
		List<BizValue> list = bizTypeService.getBizValuesByCode(UploadFileStatus.CACHING);
		boolean ischeck=false;//true 从缓存中取值
		if (list != null && list.size() > 0) {
			String value = list.get(0).getDisplayValue();
			if(StringUtils.isNotBlank(value)&& value.equals("1") ){
				ischeck=true;
			}
		} 
		if(ischeck){
			if(InitZflowUtil.staticDictionaryMap!=null){
				Map <String , List<DataDictionaryValue>> staticDictionaryMap=  InitZflowUtil.staticDictionaryMap;
				List<DataDictionaryValue> dataDictionaryValueAllList= staticDictionaryMap.get(code);
				return dataDictionaryValueAllList;	
			}		
		}else{
			List<DataDictionaryValue> dataDictionaryValueAllList=	this.dataDictionaryService.getDataDictionaryValueByCode(code);
			if(dataDictionaryValueAllList!=null){
				for (DataDictionaryValue dataDictionaryValue : dataDictionaryValueAllList) {
					dataDictionaryValue.setDataDictionaryCode(null);
				}
			}
			return dataDictionaryValueAllList;
		}
		return null;
	}
	/**
	 * 显示单个为修改
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/dictionaryValueById.do")
	@ResponseBody
	public DataDictionaryValue dictionaryValueById(
			@RequestParam(value="dictionaryValueid",required=true)Long dictionaryValueid
			) throws Exception{
		DataDictionaryValue dataDictionaryValue=(DataDictionaryValue)this.dataDictionaryService.findObjByKey(DataDictionaryValue.class,dictionaryValueid);
		JSONObject dataDictionaryValueJson= JsonToObjectUtil.toJSON(dataDictionaryValue);
		dataDictionaryValue =(DataDictionaryValue)JsonToObjectUtil.toObject(dataDictionaryValueJson, DataDictionaryValue.class);
		return dataDictionaryValue;
	}
	/**
	 * 添加/修改值
	 * @return
	 */
	@RequestMapping(value="/addOrUpdateDictionaryValue.do")
	@ResponseBody
	public Map<String, Object> addOrUpdateDictionaryValue(
			@RequestParam(value="displayValue",required=false)String displayValue,
			@RequestParam(value="ordinal",required=false)Integer ordinal,
			@RequestParam(value="organization",required=false)String organization,
			@RequestParam(value="value",required=false)String value,
			@RequestParam(value="dictionaryCodeid",required=false)Long dictionaryCodeid,
			@RequestParam(value="dictionaryValueid",required=false)Long dictionaryValueid
			){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			DataDictionaryValue dataDictionaryValue=new DataDictionaryValue();
			dataDictionaryValue.setDisplayValue(displayValue);
			dataDictionaryValue.setOrdinal(ordinal);
			dataDictionaryValue.setOrganization(organization);
			dataDictionaryValue.setValue(value);
			if(dictionaryCodeid !=null){
				//DataDictionaryCode dataDictionaryCode =this.dataDictionaryService.getDictionaryCodeByCodeId(dictionaryCodeid);
				DataDictionaryCode dataDictionaryCode=(DataDictionaryCode)this.dataDictionaryService.findObjByKey(DataDictionaryCode.class,dictionaryCodeid);
				dataDictionaryValue.setDataDictionaryCode(dataDictionaryCode);
			}
			if(dictionaryValueid !=null){//修改
				//判断value是否重复
				DataDictionaryValue dictionaryCodeValueisnull=this.dataDictionaryService.getdictionaryValueByIdisnull(dictionaryCodeid,dictionaryValueid,value);
				if(dictionaryCodeValueisnull!=null){
					map.put("errorMsg", "修改失败,编号已经存在,请重新填写编号");
					return map;
				}
				dataDictionaryValue.setId(dictionaryValueid);
				this.dataDictionaryService.update(dataDictionaryValue);
				map.put("successMsp","修改成功");
				return map;
			}else {
				//判断value是否重复
				DataDictionaryValue dictionaryCodeValueisnull=this.dataDictionaryService.getdictionaryValueByIdisnull(dictionaryCodeid,null,value);
				if(dictionaryCodeValueisnull!=null){
					map.put("errorMsg", "修改失败,编号已经存在,请重新填写编号");
					return map;
				}
				this.dataDictionaryService.create(dataDictionaryValue);
				map.put("successMsp","新增成功");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg","操作失败");
			return map;
		}
	}
	/**
	 * 删除数据值
	 * @return
	 */
	@RequestMapping(value="/deleteDictionaryValue.do")
	@ResponseBody
	public Map<String, Object>  deleteDictionaryValue(
			@RequestParam(value="dictionaryValueid",required=false)Long dictionaryValueid
		){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			this.dataDictionaryService.delete(DataDictionaryValue.class,dictionaryValueid);
			map.put("successMsp","删除成功");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg","删除失败");
			return map;
		}
	}
		
	
	@RequestMapping(value = "/dataDictionaryValues.do")
	@ResponseBody
	public  Map<String,Object>  dataDictionaryValues(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String idStr= request.getParameter("ids");
		try {
			if(idStr!=null&&!idStr.trim().equals("")){
				String[] ids=idStr.split(",");
				List<DataDictionaryValue> dataDictionaryValueList=new ArrayList<DataDictionaryValue>();
 				for (String idstr : ids) {
					Long id= Long.parseLong(idstr);
					DataDictionaryValue dictionaryCodeValue=(DataDictionaryValue)this.dataDictionaryService.findObjByKey(DataDictionaryValue.class, id);
					if(dictionaryCodeValue!=null){
						DataDictionaryValue newDictionaryCodeValue=new DataDictionaryValue(dictionaryCodeValue.getId(), dictionaryCodeValue.getValue(), dictionaryCodeValue.getDisplayValue());
						dataDictionaryValueList.add(newDictionaryCodeValue);	
					}
				}
				map.put("results", dataDictionaryValueList);		
			}
		} catch (Exception e) {
			map.put("code",0); 
			return map;
		}
		map.put("code",1); 
		return map;
	}
	
}
