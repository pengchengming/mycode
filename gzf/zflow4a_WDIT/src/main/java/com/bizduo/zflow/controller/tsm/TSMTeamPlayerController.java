package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmteamplayer")
public class TSMTeamPlayerController {

	@RequestMapping("/teamplayerlist")
	public String teamplayerlist(){
		return "tsm/teamplayer/teamplayerlist";
	}
	
	@RequestMapping("/addteamplayer")
	public String addteamplayer(){
		return "tsm/teamplayer/teamplayerhandle";
	}
	
	
	@RequestMapping("/teamplayerhandle")
	public ModelAndView teamplayerhandle(@RequestParam("teamId")String teamId){
		ModelAndView mv = new ModelAndView("tsm/teamplayer/teamplayerhandle");
		mv.addObject("teamId", teamId);
		return mv;
	}
	
	@RequestMapping("/teamplayeredit")
	public ModelAndView teamplayeredit(@RequestParam("teamId")String teamId){
		ModelAndView mv = new ModelAndView("tsm/teamplayer/teamplayeredit");
		mv.addObject("teamId", teamId);
		return mv;
	}
}
