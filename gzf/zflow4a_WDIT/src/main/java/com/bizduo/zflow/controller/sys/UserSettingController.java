package com.bizduo.zflow.controller.sys;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//TODO 用户设置字段
@Controller
@RequestMapping(value = "userSetting")
public class UserSettingController {
	
	@RequestMapping("/wdit")
	public ModelAndView powerlist() {
		return new ModelAndView("wdit/sys/userSetting");
	}
	
	@RequestMapping(value="/findValByCode")
	public String findValueByCode(HttpServletRequest request,HttpServletResponse response){
		//根据code，再包装一个formcode加condition .表：sys_datadictionary_value	
		String id = request.getParameter("id");
		return "forward:/forms/getDataByFormId?formCode="+"sys_datadictionary_value&condition=dataDictionaryCode_id="+id;
	}
  
}

