package com.bizduo.zflow.service.formView.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.ViewPage;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.formView.IViewPageService;
import com.bizduo.zflow.util.SelectToJson;
@Service
public class ViewPageService extends BaseService<ViewPage, Long>  implements IViewPageService {

	@SuppressWarnings("unchecked")
	public ViewPage findViewPageByCode(String code){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(ViewPage.class);
		if(null != code&&!code.trim().equals("")){
			cri.add(Restrictions.eq("code", code));
			List<ViewPage>  viewList=super.queryDao.getByDetachedCriteria(cri);
			if(viewList!=null&&viewList.size()>0)
				return viewList.get(0);
		}
		return null;
	}
	//根据sql 查询
	public  String findViewPageBySql(String sql,Map<String,String> fieldMap) throws Exception{
		Map<String,String> sqlMap=new HashMap<String, String>(); 
		sqlMap.put("sql",sql);
		String json =SelectToJson.jdbcTemplateQuerysql(jdbcTemplate, sqlMap, fieldMap, null).toString(); 
		return json;
	}
}
