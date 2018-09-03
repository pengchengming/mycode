package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmclub")
public class TSMClubController {

	@RequestMapping("/clublist")
	public String clublist(){
		return "tsm/club/clublist";
	}
	
	@RequestMapping("clubhandle")
	public ModelAndView  clubhandle(@RequestParam(value="id",required=true)String id){
	  ModelAndView mv  = new ModelAndView("tsm/club/clubhandle");
	  mv.addObject("id",id );
	  return mv;
	}
	
	@RequestMapping("/addclub")
	public String addclub(){
		return "tsm/club/clubhandle";
	}
}
