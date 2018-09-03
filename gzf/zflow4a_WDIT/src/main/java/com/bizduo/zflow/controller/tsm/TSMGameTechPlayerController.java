package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/tsmgametechplayer")
public class TSMGameTechPlayerController {

	@RequestMapping("/gametechplayerlist")
	public String gametechplayerlist(){
		return "tsm/gametechplayer/gametechplayerlist";
	}
	
	@RequestMapping("/addgametechplayer")
	public String addgametechplayer(){
		return "tsm/gametechplayer/gametechplayerhandle";
	}
	
	@RequestMapping("/gametechplayerhandle")
   public String gametechplayerhandle (String id,Model model){
	    model.addAttribute("id",id);
	  return "tsm/gametechplayer/gametechplayerhandle";
   }
	
}
