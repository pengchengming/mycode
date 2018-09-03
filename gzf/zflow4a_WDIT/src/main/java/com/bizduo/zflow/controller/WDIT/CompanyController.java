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
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping("company")
public class CompanyController extends BaseController {
	@Autowired
	private IFormService formService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	//企业我的申请
	@RequestMapping("mycompany")
	public String mycompany(Model model){
		model.addAttribute("user", UserUtil.getUser());
		return "/wdit/companyRequest/requestList";
	}
	//企业修改我的申请
	@RequestMapping("showmycompany")
	public String showmycompany(@RequestParam("id")int id,Model model){
		model.addAttribute("id", id);
		model.addAttribute("user", UserUtil.getUser());
		return "/wdit/company/showMyCompany";
	}
	//企业查看我的申请
	@RequestMapping("requestshowcompany")
	public String requestshowcompany(@RequestParam("id")int id,Model model){
		model.addAttribute("id", id);
		model.addAttribute("user", UserUtil.getUser());
		return "/wdit/company/requestShowCompany";
	}
	//企业查看我的员工
	@RequestMapping("requestshowuser")
	public String requestshowuser(@RequestParam("requestUserId")int requestUserId,Model model){
		model.addAttribute("requestUserId", requestUserId);
		return "/wdit/company/requestShowUser";
	}
}
