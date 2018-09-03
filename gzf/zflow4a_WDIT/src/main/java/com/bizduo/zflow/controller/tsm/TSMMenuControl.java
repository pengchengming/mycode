package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tsmmenu")
public class TSMMenuControl {

	@RequestMapping("/menulist")
	public String menulist(){
		return "tsm/menu/menulist";
	}
	
}
