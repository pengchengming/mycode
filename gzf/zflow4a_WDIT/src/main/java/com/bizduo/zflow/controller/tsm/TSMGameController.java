package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmgame")
public class TSMGameController {

	@RequestMapping("/gamelist")
	public String gamelist(){
		return "tsm/game/gamelist";
	}
	
	@RequestMapping("/gamehandle")
	public ModelAndView gamehandle(String id){
		ModelAndView mv = new ModelAndView("tsm/game/gamehandle");
		mv.addObject("id", id);
		return mv;
	}
	
	
	@RequestMapping("/addgame")
	public String addgame(){
		return "tsm/game/gamehandle";
	}
	
}
