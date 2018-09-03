package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/tsmplayerfieldposition")
public class TSMPlayerFieldPositionController {

	@RequestMapping("/playerfieldpositionlist")
	public String clubplayerlist(){
		return "tsm/playerfieldposition/playerfieldpositionlist";
	}
	
	@RequestMapping("/playerfieldpositionhandle")
	public String clubplayerhandle(@RequestParam("playerId")String playerId,Model model){
		model.addAttribute("playerId", playerId);
		return "tsm/playerfieldposition/playerfieldpositionhandle";
	}
	
	@RequestMapping("/playerfieldpositionedit")
	public String clubplayeredit(@RequestParam("playerId")String playerId,Model model){
		model.addAttribute("playerId", playerId);
		return "tsm/playerfieldposition/playerfieldpositionedit";
	}
	
	@RequestMapping("/addplayerfieldposition")
	public String addplayerfieldposition(){
		return "tsm/playerfieldposition/playerfieldpositionhandle";
	}
	
}
