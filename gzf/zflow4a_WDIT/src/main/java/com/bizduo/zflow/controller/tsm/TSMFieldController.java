package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmfield")
public class TSMFieldController {

	@RequestMapping("/fieldlist")
	public String clublist(){
		return "tsm/field/fieldlist";
	}
	
	@RequestMapping("fieldhandle")
	public ModelAndView  clubhandle(@RequestParam(value="id",required=true)String id){
	  ModelAndView mv  = new ModelAndView("tsm/field/fieldhandle");
	  mv.addObject("id",id );
	  return mv;
	}
	
	@RequestMapping("/addfield")
	public String addclub(){
		return "tsm/field/fieldhandle";
	}
}
