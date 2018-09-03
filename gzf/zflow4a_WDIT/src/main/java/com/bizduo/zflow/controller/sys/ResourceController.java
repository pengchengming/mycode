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

import com.bizduo.zflow.domain.sys.Resource;
import com.bizduo.zflow.service.sys.IResourceService;

@Controller
@RequestMapping(value = "/resource")
public class ResourceController {
	@Autowired
	private IResourceService resourceService;
	
	@RequestMapping(value = "/template", produces = "text/html")
	public String template(){
		return "resource/template";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> create(@RequestBody Resource resource){
		Map<String, String> map = new HashMap<String, String>();
		try {
			this.resourceService.create(resource);
		} catch (Exception e) {
			e.printStackTrace();
			if(null == resource.getId())
				map.put("errorMsg", "添加失败,请稍后再试!");
			else
				map.put("errorMsg", "修改失败,请稍后再试!");	
			return map;
		}
		if(null == resource.getId())
			map.put("successMsg", "添加成功!");
		else
			map.put("successMsg", "修改成功!");
		return map;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, String> delete(@PathVariable("id") Integer id, Model uiModel){
		Map<String, String> map = new HashMap<String, String>();
		try{
			this.resourceService.delete(Resource.class, id);
		}catch(Exception e){
			map.put("errorMsg", "删除失败!");
			return map;
		}
		map.put("successMsg", "删除成功!");
		return map;
	}

//	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
//	public String create(@Valid Resource resource, BindingResult bindingResult,
//			Model uiModel, HttpServletRequest httpServletRequest) {
//		if (bindingResult.hasErrors()) {
//			populateEditForm(uiModel, resource);
//			return "resource/create";
//		}
//		uiModel.asMap().clear();
//		try {
//			resourceService.create(resource); // resource.persist(); commit
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "redirect:/resource/"
//				+ encodeUrlPathSegment(resource.getId().toString(),
//						httpServletRequest);
//	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Resource());
		return "resource/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		uiModel.addAttribute("resource",
				resourceService.findObjByKey(Resource.class, id));
		uiModel.addAttribute("itemId", id);
		return "resource/show";
	}

	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public List<Resource> list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		// if (page != null || size != null) {
		// int sizeNo = size == null ? 10 : size.intValue();
		// final int firstResult = page == null ? 0 : (page.intValue() - 1) *
		// sizeNo;
		// uiModel.addAttribute("resource", Resource.findDoctorEntries(firstResult,
		// sizeNo));
		// float nrOfPages = (float) Resource.countDoctors() / sizeNo;
		// uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages
		// || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		// } else {
		// uiModel.addAttribute("resource", Resource.findAllDoctors());
		// }
		// return "resource/list";
		List<Resource>  resourceList= (List<Resource>) this.resourceService.findAll(Resource.class);
		 return resourceList;
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Resource resource, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, resource);
			return "resource/update";
		}
		uiModel.asMap().clear();
		// /resource.merge();
		resourceService.update(resource);
		return "redirect:/resource/"
				+ encodeUrlPathSegment(resource.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		populateEditForm(uiModel,
				(Resource) resourceService.findObjByKey(Resource.class, id));
		return "resource/update";
	}

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//	public String delete(@PathVariable("id") Long id,
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "size", required = false) Integer size,
//			Model uiModel) throws Exception {
//		// Resource resource = (Resource)resourceService.findObjByKey(Resource.class, id);
//		resourceService.delete(Resource.class, id);
//		uiModel.asMap().clear();
//		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//		return "redirect:/resource";
//	}

	void populateEditForm(Model uiModel, Resource resource) {
		uiModel.addAttribute("resource", resource);
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
