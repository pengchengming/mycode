package com.bizduo.zflow.controller.importdata;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.service.importdata.IImportLogService;
@Controller
@RequestMapping(value = "/importLog")
public class ImportLogController extends BaseController {

	@Autowired
	private IImportLogService importLogService;
	
	@RequestMapping(value = "/getByParam", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getByParam(@RequestParam(value = "id", required = true) Integer id,   
			@RequestParam(value = "batchNo", required = true) String batchNo, 
			@RequestParam(value = "sdate", required = true) String sdate){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("code", 1);
			map.put("results", importLogService.getByParam(id, batchNo));
			map.put("message", "Success !");
		}catch(Exception e){
			map.put("code", 0);
			map.put("message", "Error, System fault !");
		}
		return map;
	}
}
