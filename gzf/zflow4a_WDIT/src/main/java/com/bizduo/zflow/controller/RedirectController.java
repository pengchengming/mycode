package com.bizduo.zflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/redirects")
public class RedirectController {
	
	@RequestMapping(value = "/redirectToFormindex", method = RequestMethod.GET)
	public String redirectToFormindex(){
		return null;
	}
	
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public String redirect(){
		return "/page/jsp/form/formindex.jsp";
	}
}
