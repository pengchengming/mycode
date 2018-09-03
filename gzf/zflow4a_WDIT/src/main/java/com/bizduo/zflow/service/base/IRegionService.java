package com.bizduo.zflow.service.base;

import java.util.List;
import java.util.Map;

import com.bizduo.zflow.domain.base.Region;

public interface IRegionService extends IBaseService<Region, Integer>{

	List<Region> findProvincesByLevel(Map<String, String> pmap);

}
