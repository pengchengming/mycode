package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmplayer")
public class TSMPlayerController {

	@RequestMapping("/playerlist")
	public String playerlist(){
		return "tsm/player/playerlist";
	}
	
	@RequestMapping("/addplayer")
	public String addplayer(){
		return "tsm/player/playerhandle";
	}
	
	@RequestMapping("playerhandle")
	public String playerhandle(@RequestParam("id")String id,Model model){
		model.addAttribute("id", id);
		return "tsm/player/playerhandle";
	}
}
