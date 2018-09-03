package com.bizduo.zflow.controller.WDIT;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.util.ExecutionSql;

@Controller
@RequestMapping("/sql")
public class SqlController extends BaseController {
	
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	@RequestMapping("sql")
	public String companylist(){
		
		return "/wdit/settime/updatesql";
	} 
	@RequestMapping(value = "/updatesql", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updatesql(@RequestParam("sql")String sql){
		int isSuccess=0;
		try {
			ExecutionSql.execution(jdbcTemplate, sql);
			isSuccess=1;
		} catch (Exception e) {
			 e.printStackTrace();
		}
		String sql1=sql.replaceAll("'", "''");
		String insertJsonObjSql=" insert into zflow_saveFormDataJson_log(formDataId,jsonObj,saveType,isSuccess,createTime) values (null,'"+sql1+"',10,"+isSuccess+",NOW())  ";
		Boolean flag=ExecutionSql.execution(jdbcTemplate, insertJsonObjSql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(isSuccess==1){			
			map.put("code", "1");
			map.put("successMsg",  "提示：\n    保存成功!");
		}else{
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
		}
		return map;
	} 
	
}
