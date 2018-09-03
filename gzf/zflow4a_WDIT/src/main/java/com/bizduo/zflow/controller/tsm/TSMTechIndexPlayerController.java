package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/tsmtechindexplayer")
public class TSMTechIndexPlayerController {

	@RequestMapping("/techindexplayerlist")
	public String techindexplayerlist(){
		return "tsm/techindexplayer/techindexplayerlist";
	}
	
}
