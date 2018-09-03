package com.bizduo.zflow.controller.WDIT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping("info")
public class InfoController extends BaseController {
	@Autowired
	private IFormService formService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;
	//数据同步接口列表
	@RequestMapping("infolist")
	public String infolist(){
		return "/wdit/info/infolist";
	} 
	
	//数据同步接口修改
	@RequestMapping("updateinfo")
	public ModelAndView updateinfo(@RequestParam(value="id",required=true)Integer infoid){
		ModelAndView mv = new ModelAndView("/wdit/info/updateinfo");
		mv.addObject("infoid", infoid);
		return mv;
	}
	
	//申请数据同步接口列表
	@RequestMapping("inforequestlist")
	public String inforequestlist(){
		return "/wdit/info/inforequestlist";
	} 
	
	//申请数据同步接口修改
	@RequestMapping("updateinforequest")
	public ModelAndView updateinforequest(@RequestParam(value="id",required=true)Integer inforequestid){
		ModelAndView mv = new ModelAndView("/wdit/info/updateinforequest");
		mv.addObject("inforequestid", inforequestid);
		return mv;
	}
	
	//数据同步接口_内网数据
	@RequestMapping("infoextranetlist")
	public String infoextranetlist(){
		return "/wdit/info/infoextranetlist";
	} 
		
	//数据同步接口_内网数据
	@RequestMapping("updateinfoextranet")
	public ModelAndView updateinfoextranet(@RequestParam(value="id",required=true)Integer infoextranetid){
		ModelAndView mv = new ModelAndView("/wdit/info/updateinfoextranet");
		mv.addObject("infoextranetid", infoextranetid);
		return mv;
	}
	
	//查询检查
	@RequestMapping("selectchecklist")
	public String selectchecklist(){
		return "/wdit/info/selectchecklist";
	}
			
	/**
	 * 查询检查 修改
	 * @param infoextranetid
	 * @return
	 */
	@RequestMapping("updateselectcheck")
	public ModelAndView updateselectcheck(@RequestParam(value="id",required=true)Integer selectcheckid){
		ModelAndView mv = new ModelAndView("/wdit/info/updateselectcheck");
		mv.addObject("selectcheckid", selectcheckid);
		return mv;
	}
}
