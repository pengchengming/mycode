package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmrun")
public class TSMRunController {

	@RequestMapping("/runlist")
	public String runlist(){
		return "tsm/run/runlist";
	}
	
	@RequestMapping("runhandle")
	public ModelAndView  runhandle(@RequestParam(value="id",required=true)String id){
	  ModelAndView mv  = new ModelAndView("tsm/run/runhandle");
	  mv.addObject("id",id );
	  return mv;
	}
	

	@RequestMapping("/addrun")
	public String addclub(){
		return "tsm/run/runhandle";
	}
	
}
