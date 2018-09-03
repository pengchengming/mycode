package com.bizduo.zflow.controller.sys;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.bizduo.zflow.domain.sys.Action;
import com.bizduo.zflow.service.sys.IActionService;
@Controller
@RequestMapping(value = "/action")
public class ActionController {
	@Autowired
	private IActionService actionService;
	
	@RequestMapping(value = "/template", produces = "text/html")
	public String template(){
		return "action/template";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> create(@RequestBody Action action) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			this.actionService.create(action);
		} catch (Exception e) {
			e.printStackTrace();
			if(null == action.getId())
				map.put("errorMsg", "添加失败,请稍后再试!");
			else
				map.put("errorMsg", "修改失败,请稍后再试!");	
			return map;
		}
		if(null == action.getId())
			map.put("successMsg", "添加成功!");
		else
			map.put("successMsg", "修改成功!");
		return map;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, String> delete(@PathVariable("id") Long id, Model uiModel){
		Map<String, String> map = new HashMap<String, String>();
		try{
			this.actionService.delete(Action.class, id);
		}catch(Exception e){
			map.put("errorMsg", "删除失败!");
			return map;
		}
		map.put("successMsg", "删除成功!");
		return map;
	}

//	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
//	public String create(@Valid Action action, BindingResult bindingResult,
//			Model uiModel, HttpServletRequest httpServletRequest) {
//		if (bindingResult.hasErrors()) {
//			populateEditForm(uiModel, action);
//			return "action/create";
//		}
//		uiModel.asMap().clear();
//		try {
//			actionService.create(action); // action.persist(); commit
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "redirect:/action/"
//				+ encodeUrlPathSegment(action.getId().toString(),
//						httpServletRequest);
//	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Action());
		return "action/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel)
			throws Exception {
		uiModel.addAttribute("action",
				actionService.findObjByKey(Action.class, id));
		uiModel.addAttribute("itemId", id);
		return "action/show";
	}

	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public List<Action> list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		// if (page != null || size != null) {
		// int sizeNo = size == null ? 10 : size.intValue();
		// final int firstResult = page == null ? 0 : (page.intValue() - 1) *
		// sizeNo;
		// uiModel.addAttribute("action", Action.findDoctorEntries(firstResult,
		// sizeNo));
		// float nrOfPages = (float) Action.countDoctors() / sizeNo;
		// uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages
		// || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		// } else {
		// uiModel.addAttribute("action", Action.findAllDoctors());
		// }
		// return "action/list";
		return (List<Action>) this.actionService.findAll(Action.class);
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Action action, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, action);
			return "action/update";
		}
		uiModel.asMap().clear();
		// /action.merge();
		actionService.update(action);
		return "redirect:/action/"
				+ encodeUrlPathSegment(action.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel)
			throws Exception {
		populateEditForm(uiModel,
				(Action) actionService.findObjByKey(Action.class, id));
		return "action/update";
	}

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//	public String delete(@PathVariable("id") Long id,
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "size", required = false) Integer size,
//			Model uiModel) throws Exception {
//		// Action action = (Action)actionService.findObjByKey(Action.class, id);
//		actionService.delete(ActionController.class, id);
//		uiModel.asMap().clear();
//		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//		return "redirect:/action";
//	}

	void populateEditForm(Model uiModel, Action action) {
		uiModel.addAttribute("action", action);
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}
}
