package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/tsmtechindexteam")
public class TSMTechIndexTeamController {

	@RequestMapping("/techindexteamlist")
	public String techindexteamlist(){
		return "tsm/techindexteam/techindexteamlist";
	}
	
}
