package com.bizduo.zflow.controller.WDIT;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping("/assign")
public class AssignController {

	@RequestMapping("/approvelist")
	public String approvelist(){
		return "/wdit/assign/approvelist";
	}
	
	@RequestMapping("/companyapproval")
	public ModelAndView companyapproval (@RequestParam(value="requreid",required=true)Integer requreid,
			                             @RequestParam(value="showdiv",required=false)Integer showdiv){
		ModelAndView mv = new ModelAndView( "/wdit/assign/companyapproval");
		mv.addObject("type", 3);
		mv.addObject("requreid", requreid);
		mv.addObject("showdiv", showdiv);
		return mv;
	}
	
	@RequestMapping("/userview")
	public String userview (@RequestParam(value="requestUserId",required=true)Integer requestUserId,Model model){
		model.addAttribute("requestUserId", requestUserId);
//		return "/wdit/assign/userview";
		return "/wdit/assign/userview";
	}
	
	//科经商委企业查看
	@RequestMapping("assigncompanylist")
	public String kjscompanylist(){
		
		return "/wdit/assign/assignCompanyList";
	}
	//科经商委企业添加
	@RequestMapping("addassigncompany")
	public String addkjscompany(Model model){
		model.addAttribute("user", UserUtil.getUser());
		return "/wdit/assign/addassignCompany";
	}
	//科经商委修改企业
	@RequestMapping("updateassigncompany")
	public String updatekjscompany(@RequestParam("id")int id,@RequestParam("type")int type,Model model){
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		model.addAttribute("user", UserUtil.getUser());
		return "/wdit/assign/addassignCompany";
	}
	//科经商委查看企业详情
	@RequestMapping("showassigncompany")
	public String showkjscompany(@RequestParam("id")int id,@RequestParam("type")int type,Model model){
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		return "/wdit/assign/showassignCompany";
	}
}
