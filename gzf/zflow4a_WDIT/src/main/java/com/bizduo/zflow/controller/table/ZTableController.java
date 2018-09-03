package com.bizduo.zflow.controller.table;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.service.table.IZTableService;

@Controller
@RequestMapping("/ztables")
public class ZTableController {
	@Autowired
	private IZTableService tableService;
	
	/**
	 * 查询表名用于显示
	 * @param response
	 */
	@RequestMapping(value="/getTableAll.do")
	@ResponseBody
	public void getTableAll(HttpServletResponse response){
		String ztablejson="";
		try {
			List<ZTable>  zTableList=this.tableService.getTableAll();
			ztablejson= this.getZTableListTableJson(zTableList);
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(ztablejson);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	private String getZTableListTableJson(List<ZTable> zTableList) throws JSONException {
		JSONArray ztableArray = new JSONArray();
		for(int i=0;i<zTableList.size();i++){
			ZTable zTable=zTableList.get(i);
			JSONObject zTableObj=new JSONObject();
			zTableObj.put("tablename", zTable.getTablename());
			zTableObj.put("description", zTable.getDescription());
			ztableArray.put(i,zTableObj);
		}
		return ztableArray.toString();
	}
}
