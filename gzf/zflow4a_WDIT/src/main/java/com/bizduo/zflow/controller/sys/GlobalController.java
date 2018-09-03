package com.bizduo.zflow.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.sys.Global;
import com.bizduo.zflow.service.sys.IGlobalService;

/**
 * 配置信息 - Controller
 * 
 * @author zs
 *
 */
@Controller
@RequestMapping(value = "/global")
public class GlobalController extends BaseController{

	@Autowired
	private IGlobalService globalService;
	
	/**
	 * 列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Model model){
		model.addAttribute("globals", globalService.getVisibleGlobals());
		return "global/list";
	}
	
	/**
	 * 添加
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(){
		return "global/add";
	}
	
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Global global,RedirectAttributes redirectAttributes) throws Exception{
		globalService.create(global);
		return "redirect:list.do";
	}
	
	
	/**
	 * 编辑
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit")
	public String edit(Integer id,Model model) throws Exception{
		model.addAttribute("global", globalService.findObjByKey(Global.class, id));
		return "global/edit";
	}
	
	
	/**
	 * 更新
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Global global,RedirectAttributes redirectAttributes) throws Exception{
		globalService.update(global);
		return "redirect:list.do";
	}
	
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	public String delete(Integer id,RedirectAttributes redirectAttributes) throws Exception{
		globalService.delete(Global.class, id);
		return "redirect:list";
	}
	
}
