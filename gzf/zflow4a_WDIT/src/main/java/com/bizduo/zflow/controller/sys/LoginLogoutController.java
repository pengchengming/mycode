package com.bizduo.zflow.controller.sys;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.util.UserUtil;

@Controller
public class LoginLogoutController extends BaseController {
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(){
		String ua = request.getHeader("User-Agent");
		boolean isPhonePage= UserUtil.checkAgentIsMobile(ua);
		if(isPhonePage){
			return "/decoPhone/login";
		}else {
			return "/deco/login";
		} 
	} 
	
	@RequestMapping(value = "/phonelogin1", method = RequestMethod.GET)
	public String phonelogin(){
		return "/decoPhone/login"; 
	} 
	@RequestMapping(value = "/logoutPage1", method = RequestMethod.GET)
	public String logon(){
		String ua = request.getHeader("User-Agent");
		boolean isPhonePage= UserUtil.checkAgentIsMobile(ua);
		if(isPhonePage){
			return "/decoPhone/login";
		}else {
			return "/deco/login";
		} 
	}	
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(){
		return "/menu";
	}

}
