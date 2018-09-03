package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bizduo.zflow.controller.BaseController;

@Controller
public class LoginTSMController extends BaseController {
	@RequestMapping(value = "/tsmLogin", method = RequestMethod.GET)
	public String login(){
		return "/tsm/login";
	}
}
