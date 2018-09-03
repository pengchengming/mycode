package com.bizduo.zflow.service.customform.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.form.SelectConditions;
import com.bizduo.zflow.domain.form.SelectList;
import com.bizduo.zflow.domain.form.SelectTableList;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.ISelectListService;
import com.bizduo.zflow.service.customform.ISelectTableListService;
import com.bizduo.zflow.util.SelectToJson;
@Service
public class SelectTableListService  extends BaseService<SelectTableList, Long>  implements ISelectTableListService {
	@Autowired
	public ISelectListService selectListService;
	@Autowired
	private IBizTypeService  bizTypeService;
	@Autowired
	private IFormService formService;
	
	@SuppressWarnings("unchecked")
	public List<SelectTableList>  findSelectTableListByAll(){ 
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(SelectTableList.class);
		List<SelectTableList>  selectTableList=super.queryDao.getByDetachedCriteria(cri);
		cri.addOrder(Order.asc("id"));
		return selectTableList;
	}
	
	@SuppressWarnings("unchecked")
	public SelectTableList findByTitle(String code){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(SelectTableList.class);
		if(null != code&&!code.trim().equals(""))
			cri.add(Restrictions.eq("code", code));
		List<SelectTableList>  selectTableList=super.queryDao.getByDetachedCriteria(cri);
		if(selectTableList!=null&&selectTableList.size()>0)
			return selectTableList.get(0);
		return null;
	}
	
	public  String selectBySqlPage(SelectTableList selectTableList,
			String selectConditionSql,PageTrace pageTrace,int type){

		String selectTableSql=selectTableList.getSelectTableSql();
		String selectFromSql= selectTableList.getSelectFromSql();
		String orderBy= selectTableList.getOrderBy();
		String groupBy= selectTableList.getGroupBy();
		 
	    List<BizValue> listBizValue = bizTypeService.getBizValuesByCode(SelectConditions.ISESCAPE);
 
 		if (type==1 &&listBizValue != null && listBizValue.size() > 0) {
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
 					try {
 	 					JSONObject saveInfo = new JSONObject();
 	 					JSONObject register = new JSONObject();
 	 					saveInfo.put("formId", 97);
 	 					register.put("code", selectTableList.getCode());
 	 					register.put("type", 2);
 	 					register.put("selectConditionSql",selectConditionSql);
 	 					register.put("remark", "selectBySqlPage");
 	 					register.put("fromSql", selectTableList.getSelectTableSql());
 	 					saveInfo.put("register", register);
						this.formService.saveFormDataJson(saveInfo.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 					 return "";
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
			if(StringUtils.isNotBlank(groupBy))
				selectFromSql +=" "+groupBy;
			sqlMap.put("sqlForm",selectFromSql); 
		}
		if(StringUtils.isNotBlank(groupBy) ) {
			sqlMap.put("groupBy",groupBy);
			selectTableSql +=" "+groupBy;
		}
		if(orderBy!=null&&!orderBy.trim().equals(""))
			selectTableSql +=" "+ orderBy  ;
		List<SelectList> selectList= this.selectListService.findByTableId(selectTableList.getId());

		Map<String,String> fieldMap=new HashMap<String, String>(); 
		if(selectList!=null){
			for (SelectList selectListtemp : selectList) {
				fieldMap.put(selectListtemp.getAliasesName(), selectListtemp.getAliasesName()); 
			}
		}		
		sqlMap.put("sql",selectTableSql);
		try {
			return  SelectToJson.jdbcTemplateQuerysql(jdbcTemplate, sqlMap, fieldMap, pageTrace).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
