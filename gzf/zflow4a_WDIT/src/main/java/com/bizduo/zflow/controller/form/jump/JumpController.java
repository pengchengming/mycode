package com.bizduo.zflow.controller.form.jump;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 当前为跳转的跳转的url
 * 当前类的跳转
 * 1.formView
 * 2.formTableView
 * 3.tableView
 * 参数
 * form表单的code
 * 
 * @author dzt
 *
 *将要实现的为
 *1.表单的扩展
 *2.表单和表格的扩展
 *3.表格的扩展
 */

@Controller
@RequestMapping("/jumpUrl")
public class JumpController {
	
	@RequestMapping(value = "formView", method = RequestMethod.GET)
    public ModelAndView formView(@RequestParam(value = "code" , required = true) String code) {   
		ModelAndView mav = new ModelAndView("zform/formView");
		//String userName = UserUtil.getUser().getUsername();
		mav.addObject("code", code);
		return mav;
    }
	
	
	@RequestMapping(value = "tableView", method = RequestMethod.GET)
    public ModelAndView tableView(@RequestParam(value = "code" , required = true) String code) {   
		ModelAndView mav = new ModelAndView("zform/tableView");
		//String userName = UserUtil.getUser().getUsername();
		mav.addObject("code", code);
		return mav;
    }
	
	@RequestMapping(value = "formTableView", method = RequestMethod.GET)
    public ModelAndView formTableView(@RequestParam(value = "code" , required = true) String code,
    		@RequestParam(value = "tableDataId" , required = true) String tableDataId) {   
		ModelAndView mav = new ModelAndView("zform/formTableView");
		//String userName = UserUtil.getUser().getUsername();
		mav.addObject("code", code);
		mav.addObject("tableDataId", tableDataId);
		return mav;
    }
	
}
