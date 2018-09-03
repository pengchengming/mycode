package com.bizduo.zflow.controller.sys;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.sys.Module;
import com.bizduo.zflow.service.sys.IModuleService;

@Controller
@RequestMapping(value = "/module")
public class ModuleController {
	@Autowired
	private IModuleService moduleService;

	@RequestMapping(value = "/template", produces = "text/html")
	public String template(){
		return "module/template";
	}
	
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> create(@RequestBody Module module) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			this.moduleService.create(module);
		} catch (Exception e) {
			e.printStackTrace();
			if(null == module.getId())
				map.put("errorMsg", "添加失败,请稍后再试!");
			else
				map.put("errorMsg", "修改失败,请稍后再试!");	
			return map;
		}
		if(null == module.getId())
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
			this.moduleService.delete(Module.class, id);
		}catch(Exception e){
			map.put("errorMsg", "删除失败!");
			return map;
		}
		map.put("successMsg", "删除成功!");
		return map;
	}
	
//	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
//	public String create(@Valid Module module, BindingResult bindingResult,
//			Model uiModel, HttpServletRequest httpServletRequest) {
//		if (bindingResult.hasErrors()) {
//			populateEditForm(uiModel, module);
//			return "module/create";
//		}
//		uiModel.asMap().clear();
//		try {
//			moduleService.create(module); // module.persist(); commit
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "redirect:/module/"
//				+ encodeUrlPathSegment(module.getId().toString(),
//						httpServletRequest);
//	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Module());
		return "module/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel)
			throws Exception {
		uiModel.addAttribute("module", moduleService.findObjByKey(Module.class, id));
		uiModel.addAttribute("itemId", id);
		return "module/show";
	}

	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		
		 Map<String, Object> map = new HashMap<String, Object>();
		 List<Module> module = new ArrayList<Module>();
		 if (page != null || size != null) {
			 int sizeNo = size == null ? 10 : size.intValue();
			 final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			 PageTrace pageTrace = new PageTrace(firstResult);
				pageTrace.setPageIndex(page);
			 module = moduleService.findByPage(pageTrace);
			 map.put("module", module);
			 float nrOfPages = (float) module.size() / sizeNo;
			 map.put("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
			 return map;
		 } else {
			 module = (List<Module>)moduleService.findAll(Module.class);
			 map.put("module", module);
			 return map;
		 }
	}

	@RequestMapping(value="/select", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, List<Module>> select() {
		Map<String, List<Module>> map = new HashMap<String, List<Module>>(); 
		List<Module> module = new ArrayList<Module>();
		List<Module> moduleList = (List<Module>) this.moduleService.findAll(Module.class);
		for(Module m : moduleList){
			 module.add(new Module(m.getId(), m.getName()));
		}
		map.put("option", module);
		return map;
	}

	
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Module module, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, module);
			return "module/update";
		}
		uiModel.asMap().clear();
		// /module.merge();
		moduleService.update(module);
		return "redirect:/module/"
				+ encodeUrlPathSegment(module.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel)
			throws Exception {
		populateEditForm(uiModel,
				(Module) moduleService.findObjByKey(Module.class, id));
		return "module/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) throws Exception {
		// Module module = (Module)moduleService.findObjByKey(Module.class, id);
		moduleService.delete(Module.class, id);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/module";
	}

	void populateEditForm(Model uiModel, Module module) {
		uiModel.addAttribute("module", module);
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
