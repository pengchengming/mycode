package com.bizduo.zflow.controller.form.formMini;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.util.ExecutionSql;
 
@Controller
@RequestMapping(value = "/test")
public class TestController {
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	//展示表单页面
	@RequestMapping(value = "/jsonSave", produces = "text/html")
	public String jsonSave(@RequestParam(value = "pageType",required = true)Integer pageType,
			HttpServletRequest request, Model uiModel){
		uiModel.addAttribute("pageType", pageType);
		if(pageType.intValue()==2||pageType.intValue()==4){
			String page_helpSql=" SELECT a.pageHelp  FROM deco_page_help  a   where  a.id= 1";
			SqlRowSet rset1 = jdbcTemplate.queryForRowSet(page_helpSql); 
		    while(rset1.next()){
		    	String pageHelp= rset1.getString("pageHelp");
		    	uiModel.addAttribute("pageHelp", pageHelp);
		    	try {
			    	if(null != pageHelp && !("").equals(pageHelp)){
					    byte[] b = pageHelp.getBytes("ISO-8859-1");
					    String s = new String(b, "UTF-8"); 
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
		    }
		}
		return "/basicData/test/jsonSave"; 
	}
	 
	@RequestMapping(value = "/save2", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> save2(HttpServletRequest request){
		String content = request.getParameter("content");
		String id = request.getParameter("id");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String insertJsonObjSql=" update  deco_page_help set pageHelp='"+content+"' where id="+id;
			ExecutionSql.execution(jdbcTemplate, insertJsonObjSql);	
		} catch (Exception e) {
			map.put("code", 0);
			return map;
		}
		map.put("code", 1);
		return map; 
	}
}
