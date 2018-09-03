package com.bizduo.zflow.service.customform.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.form.ExcelList;
import com.bizduo.zflow.domain.form.ExcelTableList;
import com.bizduo.zflow.domain.form.SelectConditions;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.customform.IExcelListService;
import com.bizduo.zflow.service.customform.IExcelTableListService;
import com.bizduo.zflow.util.SelectToJson;
@Service
public class ExcelTableListService extends BaseService<ExcelTableList, Long> 
	implements IExcelTableListService {

	@Autowired
	private IExcelListService  excelListService;
	
	@SuppressWarnings("unchecked")
	public List<ExcelTableList>  findExcelTableListByAll(){ 
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ExcelTableList.class);
		List<ExcelTableList>  excelTableList=super.queryDao.getByDetachedCriteria(cri);
		cri.addOrder(Order.asc("id"));
		return excelTableList;
	}
	@SuppressWarnings("unchecked")
	public ExcelTableList findByCode(String code){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ExcelTableList.class);
		if(null != code&&!code.trim().equals(""))
			cri.add(Restrictions.eq("code", code));
		List<ExcelTableList>  excelTableList=super.queryDao.getByDetachedCriteria(cri);
		if(excelTableList!=null&&excelTableList.size()>0)
			return excelTableList.get(0);
		return null;
	}

	@Autowired
	private IBizTypeService  bizTypeService;
	public  Map<String,Object> selectBySqlPage(ExcelTableList excelTableList,String selectConditionSql){
		String selectTableSql=excelTableList.getExcelTableSql();
		String selectFromSql= excelTableList.getExcelFromSql();
		String orderBy= excelTableList.getOrderBy();
		
		List<BizValue> listBizValue = bizTypeService.getBizValuesByCode(SelectConditions.ISESCAPE);
		if (listBizValue != null && listBizValue.size() > 0) {
 			String value = listBizValue.get(0).getDisplayValue();
 			if(StringUtils.isNotBlank(value)&& value.equals("1") ){
 				if(selectConditionSql.toLowerCase().indexOf(" where ")>0||
 						selectConditionSql.toLowerCase().indexOf(" select ")>0||
 						selectConditionSql.toLowerCase().indexOf(" count")>0||
 						selectConditionSql.toLowerCase().indexOf(" and ")>0||
 						selectConditionSql.toLowerCase().indexOf(" count ")>0||
 						selectConditionSql.toLowerCase().indexOf(" or ")>0||
 						selectConditionSql.toLowerCase().indexOf(" exists ")>0||
 						selectConditionSql.toLowerCase().indexOf("delete")>0||
 						selectConditionSql.toLowerCase().indexOf("drop")>0){ 
 				 
 					 return null;
 				}
 				List<BizValue> keyBizValueList = bizTypeService.getBizValuesByCode(SelectConditions.KEYWORDS);
 		 		if (keyBizValueList != null && keyBizValueList.size() > 0) {
 		 			for (int i = 0; i < keyBizValueList.size(); i++) {
 		 				BizValue bizValue=keyBizValueList.get(i); 
 		 				selectConditionSql=selectConditionSql.replace(bizValue.getRemark(), " "+bizValue.getDisplayValue()+" ");
					}
 		 		}
 			}
 		}
		Map<String,String> sqlMap=new HashMap<String, String>(); 
		if(selectConditionSql!=null&&!selectConditionSql.trim().equals("")) {
			selectTableSql += selectConditionSql ; 
		}
		if(selectFromSql!=null&&!selectFromSql.trim().equals("")) {
			if(selectConditionSql!=null&&!selectConditionSql.trim().equals("")) {
				selectFromSql += selectConditionSql ; 
			}
			sqlMap.put("sqlForm",selectFromSql); 
		}
		if(orderBy!=null&&!orderBy.trim().equals(""))
			selectTableSql +="  "+ orderBy ;
		List<ExcelList> excelList= excelListService.findByTableId(excelTableList.getId());
 
		Map<String,String> fieldValMap=new HashMap<String, String>();
		List<String> fieldList=new ArrayList<String>(); 
		if(excelList!=null){
			for (ExcelList excelListtemp : excelList) {
				fieldValMap.put(excelListtemp.getDescription(), excelListtemp.getAliasesName()); 
				fieldList.add(excelListtemp.getDescription());
			}
		}		
		sqlMap.put("sql",selectTableSql);
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			JSONObject jsonObject= SelectToJson.jdbcTemplateQuerysql(jdbcTemplate, sqlMap, fieldValMap, null);
			JSONArray jsonArray=jsonObject.getJSONArray("results");
			map.put("fieldValMap", fieldValMap);
			map.put("fieldList", fieldList);
			map.put("results", jsonArray.toString());
			return  map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
}
