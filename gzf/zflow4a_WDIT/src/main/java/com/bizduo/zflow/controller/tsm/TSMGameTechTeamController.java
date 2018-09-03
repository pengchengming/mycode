package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/tsmgametechteam")
public class TSMGameTechTeamController {

	@RequestMapping("/gametechteamlist")
	public String gametechteamlist(){
		return "tsm/gametechteam/gametechteamlist";
	}
	
	
	
	@RequestMapping("/addgametechteam")
	public String addgametechteam(){
		return "tsm/gametechteam/gametechteamhandle";
	}
	
	@RequestMapping("/gametechteamhandle")
   public String gametechteamhandle (String id,Model model){
	    model.addAttribute("id",id);
	  return "tsm/gametechteam/gametechteamhandle";
   }
	
}
