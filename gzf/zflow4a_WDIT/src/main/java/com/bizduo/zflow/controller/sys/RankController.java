package com.bizduo.zflow.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.sys.Rank;
import com.bizduo.zflow.service.sys.IRankService;

/**
 * 角色等级 - Controller
 * 
 * @author zs
 *
 */
@Controller
@RequestMapping(value = "/rank")
public class RankController extends BaseController{

	@Autowired
	private IRankService rankService;
	
	/**
	 * 列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Model model){
		model.addAttribute("ranks", rankService.findAll(Rank.class));
		return "rank/list";
	}
	
	/**
	 * 添加
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(){
		return "rank/add";
	}
	
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Rank rank,RedirectAttributes redirectAttributes) throws Exception{
		rankService.create(rank);
		return "redirect:list.do";
	}
	
	
	/**
	 * 编辑
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit")
	public String edit(Integer id,Model model) throws Exception{
		model.addAttribute("rank", rankService.findObjByKey(Rank.class, id));
		return "rank/edit";
	}
	
	
	/**
	 * 更新
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Rank rank,RedirectAttributes redirectAttributes) throws Exception{
		rankService.update(rank);
		return "redirect:list.do";
	}
	
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	public String delete(Integer id,RedirectAttributes redirectAttributes) throws Exception{
		rankService.delete(Rank.class, id);
		return "redirect:list";
	}
	
	
	@RequestMapping(value = "getBySelector")
	@ResponseBody
	public Map<String, Object> getBySelector(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<Rank> value = new ArrayList<Rank>();
			for(Rank o : rankService.findAll(Rank.class))
			{
				Rank rank = new Rank();
				rank.setId(o.getId());
				rank.setName(o.getName());
				value.add(rank);
				map.put("code", "1");
				map.put("massage", "Success !");
				map.put("results", value);
			}
		}catch(Exception e){
			map.put("code", "0");
			map.put("message", "Error, System fault!");
		}
		return map;
	}
}
