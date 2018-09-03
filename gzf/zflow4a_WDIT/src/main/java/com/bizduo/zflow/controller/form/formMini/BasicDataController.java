package com.bizduo.zflow.controller.form.formMini;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping(value = "/basicData")
public class BasicDataController {
	
 
	@RequestMapping(value = "/formMini/createSelectList", produces = "text/html")
	public String createSelectList(@RequestParam(value = "type", required = false) Integer type, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/basicData/formMini/createSelectList";
	}
	
	@RequestMapping(value = "/formMini/execlPage", produces = "text/html")
	public String execlPage(@RequestParam(value = "type", required = false) Integer type, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/basicData/formMini/execlPage";
	} 
}
