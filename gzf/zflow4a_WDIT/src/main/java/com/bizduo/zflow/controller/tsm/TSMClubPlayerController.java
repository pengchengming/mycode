package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmclubplayer")
public class TSMClubPlayerController {

	@RequestMapping("/clubplayerlist")
	public String clubplayerlist(){
		return "tsm/clubplayer/clubplayerlist";
	}
	
	@RequestMapping("clubplayeredit")
	public String clubplayeredit(@RequestParam("clubId")String clubId,Model model){
		model.addAttribute("clubId", clubId);
		return "tsm/clubplayer/clubplayeredit";
	}
	
	@RequestMapping("/addclubplayer")
	public String addclubplayer(){
		return "tsm/clubplayer/clubplayerhandle";
	}
}
