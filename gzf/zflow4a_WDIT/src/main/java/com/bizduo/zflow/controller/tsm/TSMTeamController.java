package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmteam")
public class TSMTeamController {

	@RequestMapping("/teamlist")
	public String gamelist(){
		return "tsm/team/teamlist";
	}
	
	
	@RequestMapping("/addteam")
	public String addteam(){
		return "tsm/team/teamhandle";
	}
	
	@RequestMapping("/teamhandle")
	public ModelAndView teamhandle(String id){
		ModelAndView mv = new ModelAndView("tsm/team/teamhandle");
		mv.addObject("id", id);
		return mv;
	}
	
}
