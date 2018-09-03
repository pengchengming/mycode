package com.bizduo.zflow.service.base.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.base.Region;
import com.bizduo.zflow.service.base.IRegionService;
@Service
public class RegionService   extends BaseService<Region, Integer> implements IRegionService{
	
	@SuppressWarnings("unchecked")
	public  List<Region> findProvincesByLevel(Map<String, String> pmap){
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(Region.class);
		if(pmap.get("parentId")!=null&&!pmap.get("parentId").trim().equals("")){
			cri.add(Restrictions.eq("parentRegion.id", Integer.parseInt(pmap.get("parentId"))));	
		}
		if(pmap.get("level")!=null&&!pmap.get("level").trim().equals("")){
			cri.add(Restrictions.eq("level", pmap.get("level")));	 
		}
		return super.queryDao.getByDetachedCriteria(cri);
	}
}
