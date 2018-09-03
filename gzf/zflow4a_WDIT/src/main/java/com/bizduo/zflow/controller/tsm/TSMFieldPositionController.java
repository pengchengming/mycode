package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmfieldposition")
public class TSMFieldPositionController {


	@RequestMapping("/fieldpositionlist")
	public String fieldpositionlist(){
		return "tsm/fieldposition/fieldpositionlist";
	}
	
	
	
	
}
