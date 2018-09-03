package com.bizduo.zflow.controller.boshliving;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.util.SelectToJson;

@Controller
@RequestMapping(value = "/designersController")
public class DesignersController {

	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value = "/designerstoPage")
	@ResponseBody
	public ModelAndView touserPage(HttpServletRequest request,@RequestParam(value = "pageType", required = true) String pageType){
		ModelAndView mav = new ModelAndView("boshliving/homeTemp");
		if(pageType.equals("showDesigners")||pageType.equals("updateDesigners")){ 
			String id =request.getParameter("id");
			mav.addObject("dataId",id); 
		}
		mav.addObject("includePage",pageType );  //"updateDesigners"
		return mav;
	} 
	
	@RequestMapping(value = "/designerstoList")
	@ResponseBody 
	public void designerstoList( 
			HttpServletResponse response,
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "parameter", required = true) String parameter,
			@RequestParam(value = "pageIndex", required = true) int pageIndex 
			) throws IOException{
		Map<String,String> fieldMap=new HashMap<String, String>();
		fieldMap.put("id", "id");
		fieldMap.put("name", "name");
		fieldMap.put("appellative", "appellative");
		fieldMap.put("duty", "duty");
		fieldMap.put("rating", "rating");
		fieldMap.put("company", "company");
		fieldMap.put("TerritoryCode", "TerritoryCode");
		fieldMap.put("TerritoryName", "TerritoryName");
		fieldMap.put("provinces", "provinces");
		fieldMap.put("city", "city");
		fieldMap.put("address", "address");
		fieldMap.put("phone1", "phone1");
		fieldMap.put("telephone1", "telephone1");
		String sql="select a.id,a.name,a.appellative,a.duty,a.rating,a.company,b.TerritoryCode,b.TerritoryName,c.name as provinces,d.name as city,a.address,a.phone1,a.telephone1 ";
		sql +=" from designers a left join dm_SalesTerritory b on a.territory=b.TerritoryCode left join dbo.dm_region_code c on a.provinces=c.code left join dbo.dm_region_code d on a.city=d.code ";
		sql +=" order by id desc  ";
		Map<String,Integer> pageMap=new HashMap<String, Integer>();
		pageMap.put("pageIndex", pageIndex);
		pageMap.put("pageSize", 10); 
		
		String designerJson= SelectToJson.executionsql(jdbcTemplate, sql, "query", fieldMap, pageMap); 
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter().print(designerJson);
	}
	@RequestMapping(value = "/accordTaskList")
	@ResponseBody 
	public void accordTaskList( 
			HttpServletResponse response,
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "parameter", required = true) String parameter,
			@RequestParam(value = "pageIndex", required = true) int pageIndex 
			) throws IOException{
		Map<String,String> fieldMap=new HashMap<String, String>();
		fieldMap.put("id", "id");
		fieldMap.put("arrangeTime", "arrangeTime");
		fieldMap.put("executeSales", "executeSales");
		fieldMap.put("project", "project");
		fieldMap.put("projectStage", "projectStage");
		fieldMap.put("importance", "importance");
		fieldMap.put("contactType", "contactType");
		fieldMap.put("leadsLevel", "leadsLevel");
		fieldMap.put("togetherDirector", "togetherDirector");
		fieldMap.put("togetherVisit", "togetherVisit");
		String sql="select id, arrangeTime,executeSales ,project,projectStage,importance,contactType,leadsLevel,togetherDirector,togetherVisit from accordingTask ";
		sql+=parameter;
		sql +=" order by id desc  ";
		Map<String,Integer> pageMap=new HashMap<String, Integer>();
		pageMap.put("pageIndex", pageIndex);
		pageMap.put("pageSize", 10); 
		
		String designerJson= SelectToJson.executionsql(jdbcTemplate, sql, "query", fieldMap, pageMap); 
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter().print(designerJson);
	}
	
}
