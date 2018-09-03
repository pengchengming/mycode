package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmgameteamrecord")
public class TSMGameTeamRecordController {

	@RequestMapping("/gameteamrecordlist")
	public String gameteamrecordlist(){
		return "tsm/gameteamrecord/gameteamrecordlist";
	}
	
	@RequestMapping("/addgameteamrecord")
	public String addgameteamrecord(){
		return "tsm/gameteamrecord/gameteamrecordhandle";	
	}
	
	@RequestMapping("/gameteamrecordhandle")
	public ModelAndView gameteamrecordhandle(String id){
		ModelAndView mv = new ModelAndView("tsm/gameteamrecord/gameteamrecordhandle");
		mv.addObject("id",id);
		return mv;	
	}
	
}
