package com.bizduo.zflow.controller.WDIT;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.service.customform.IFormService;

@Controller
@RequestMapping("settime")
public class SetTimeController extends BaseController {
	@Autowired
	private IFormService formService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//审核参数管理
	@RequestMapping("settimeList")
	public String companylist(){
		return "/wdit/settime/settimeList";
	} 
	//审核进度
	@RequestMapping("approvalplan")
	public String approvalplan(){
		return "/wdit/settime/approvalplan";
	}
}
