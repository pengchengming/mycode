package com.bizduo.zflow.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.domain.base.Region;
import com.bizduo.zflow.service.base.IRegionService;

@Controller
@RequestMapping(value = "/region")
public class RegionController {
	@Autowired
	private IRegionService regionService;
	
	@RequestMapping(value = "/regionList")
	@ResponseBody
	public  Map<String,Object>  findFormPropertyList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String level= request.getParameter("level");
		String parentId= request.getParameter("parentId");
		try {
			Map<String,String> pmap =new HashMap<String, String>();
			pmap.put("level", level);
			pmap.put("parentId", parentId);
			List<Region> RegionList= regionService.findProvincesByLevel(pmap);
			if(RegionList!=null&&RegionList.size()>0){
				List<Region> newRegionList=new ArrayList<Region>();
				for (Region region : RegionList) {
					Region newprovince=new  Region(region.getId(),region.getCode(), region.getName(), region.getLevel());
					newRegionList.add(newprovince);
				}
				map.put("results", newRegionList);	
			}
			map.put("code", 1); 
		} catch (Exception e) {
			map.put("code", 0);
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "/getRegionById")
	@ResponseBody
	public  Map<String,Object>  getProvinceById(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id= request.getParameter("id");
		try {
			if(id!=null&&!id.trim().equals("")){
				Region region= regionService.findObjByKey(Region.class,Integer.parseInt(id));
				Region newRegion=new  Region(region.getId(),region.getCode(), region.getName(), region.getLevel());
				map.put("results", newRegion);	
			} 
			map.put("code", 1); 
		} catch (Exception e) {
			map.put("code", 0);
			e.printStackTrace();
		}
		return map;
	}
	
	
}
