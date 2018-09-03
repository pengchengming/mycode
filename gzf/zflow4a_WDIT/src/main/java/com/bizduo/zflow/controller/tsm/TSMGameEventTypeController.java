package com.bizduo.zflow.controller.tsm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/tsmgameeventtype")
public class TSMGameEventTypeController {

	@RequestMapping("/gameeventtypelist")
	public String gameeventtypelist(){
		return "tsm/gameeventtype/gameeventtypelist";
	}
	
}
