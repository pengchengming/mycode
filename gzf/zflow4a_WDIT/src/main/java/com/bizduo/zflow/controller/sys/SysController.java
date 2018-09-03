package com.bizduo.zflow.controller.sys;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/sys")
public class SysController {

	
	// 编辑
		@RequestMapping("/powerlist")
		public ModelAndView powerlist() {
			return new ModelAndView("sys/powerlist");
		}
	   //编辑详情页
		@RequestMapping("/powerhandle/{code}")
		public String powerhandle(@PathVariable("code") String code) {
			if (code != null) {
					return "sys/"+code;
			}
			return null;
		}
}
