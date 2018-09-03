package com.bizduo.zflow.controller.sys;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.bizduo.zflow.domain.sys.MenuItem;
import com.bizduo.zflow.service.sys.IMenuItemService;

@Controller
@RequestMapping(value = "/menuitem")
public class MenuItemController {
	@Autowired
	private IMenuItemService menuItemService;
	
	@RequestMapping(value = "template", produces = "text/html")
	public String template(){
		return "menuitem/template";
	}
  
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> create(@RequestBody MenuItem menuItem) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			this.menuItemService.create(menuItem);
		} catch (Exception e) {
			e.printStackTrace();
			if(null == menuItem.getId())
				map.put("errorMsg", "添加失败,请稍后再试!");
			else
				map.put("errorMsg", "修改失败,请稍后再试!");	
			return map;
		}
		if(null == menuItem.getId())
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
			boolean exists = (this.menuItemService.findObjByKey(MenuItem.class, id)).getSubMenuItemList().size() == 0 ? true : false;
			if(exists){
				this.menuItemService.delete(MenuItem.class, id);
			}else{
				map.put("errorMsg", "该菜单下有子菜单,请先删除子菜单!");
				return map;
			}
		}catch(Exception e){
			map.put("errorMsg", "该菜单已和权限关联,请先取消关联!");
			return map;
		}
		map.put("successMsg", "删除成功!");
		return map;
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new MenuItem());
		return "menuitem/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		uiModel.addAttribute("menuItem",
				this.menuItemService.findObjByKey(MenuItem.class, id));
		uiModel.addAttribute("itemId", id);
		return "menuitem/show";
	}


	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(HttpServletResponse response,HttpServletRequest request) throws Exception {
		Integer id=0;
		if(request.getParameter("id")!=null){
			id=Integer.parseInt(request.getParameter("id"));
		}
		Integer level=null;
		if(request.getParameter("level")!=null&&!request.getParameter("level").equals("null")){
			level=Integer.parseInt(request.getParameter("level"));
		}
		Boolean isBack=null;
		if(request.getParameter("isBack")!=null)
			isBack=Boolean.parseBoolean(request.getParameter("isBack"));
		
		/*Integer page=null;
		if(request.getParameter("page")!=null){
			page=Integer.parseInt(request.getParameter("page"));
		} 
		Integer size=null;
		if(request.getParameter("size")!=null){
			size=Integer.parseInt(request.getParameter("size"));
		}*/
		  
		//1.用来存放查询参数 (id = parentMenuItem.id, level = parentMenuItem.level)
		Map<String, Object> paramMap = new HashMap<String, Object>();
		MenuItem mi = null;
		//2.id代表parentMenuItem.id 不为空就pramMap.put
		if(null != id && 0 != id){
			paramMap.put("id", id);
			//3.如果请求是返回到上级菜单则查询出上级菜单的parentMenuItem(上级菜单的上一级菜单)
			if(isBack){
				mi = ((MenuItem) this.menuItemService.findObjByKey(MenuItem.class, id)).getParentMenuItem();
				if(null != mi) {
					mi = mi.shadowClone();
					paramMap.put("id", mi.getId());
				}
			}else{
				//4.如果是查询子菜单,则当前菜单就是子菜单的parent菜单
				mi = (MenuItem) this.menuItemService.findObjByKey(MenuItem.class, id);
			}
		}
		//5.查询一级菜单(level = 1 ,表示一级菜单)
		if(null == level || 0 == level){
			level = 1;
			paramMap.put("level", level);
		}else{
			//6.查询上级菜单
			if(isBack){
				//7.如果上级菜单就是一级菜单,则设置查询条件 parentId = null;(一级菜单没有上级菜单)
				if(1 == level) paramMap.put("id", null);
			}else{
				//8.查询子菜单时,level需要加1 (现在的规则是父菜单都比它的直属子菜单的level值大1)
				level += 1;
			}
			paramMap.put("level", level);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
		MenuItem newmi=new MenuItem();
		if(mi!=null){
			newmi.setId(mi.getId());
			newmi.setProjectCode(mi.getProjectCode());
			newmi.setUrl(mi.getUrl());
			newmi.setClsName(mi.getClsName());
			newmi.setName(mi.getName());
			newmi.setPageDisplayName(mi.getPageDisplayName());
			newmi.setDescription(mi.getDescription());
			newmi.setShortCutLink(mi.getShortCutLink());
			newmi.setIsLeafNode(mi.getIsLeafNode());
		}		
		List<MenuItem>  menuItemList=(List<MenuItem>)this.menuItemService.findByParam(paramMap);
		List<MenuItem>  newmenuItemList=new ArrayList<MenuItem>();
		if(menuItemList!=null&&menuItemList.size()>0){
			for (MenuItem menuItem : menuItemList) {
				MenuItem newmitemp=new MenuItem();
				newmitemp.setId(menuItem.getId());
				newmitemp.setProjectCode(menuItem.getProjectCode());
				newmitemp.setUrl(menuItem.getUrl());
				newmitemp.setClsName(menuItem.getClsName());
				newmitemp.setName(menuItem.getName());
				newmitemp.setPageDisplayName(menuItem.getPageDisplayName());
				newmitemp.setDescription(menuItem.getDescription());
				newmitemp.setShortCutLink(menuItem.getShortCutLink());
				newmitemp.setIsLeafNode(menuItem.getIsLeafNode());
				newmitemp.setIndexNum(menuItem.getIndexNum()); 
				newmitemp.setLevel(menuItem.getLevel()); 
				newmenuItemList.add(newmitemp);
			}
		}
		map.put("parentmi", newmi);
		map.put("menuitem", newmenuItemList);
		return map;
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid MenuItem menuItem, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, menuItem);
			return "menuitem/update";
		}
		uiModel.asMap().clear();
		// /menuItem.merge();
		menuItemService.update(menuItem);
		return "redirect:/menuitem/"
				+ encodeUrlPathSegment(menuItem.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		populateEditForm(uiModel,
				(MenuItem) menuItemService.findObjByKey(MenuItem.class, id));
		return "menuitem/update";
	}

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//	public String delete(@PathVariable("id") Long id,
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "size", required = false) Integer size,
//			Model uiModel) throws Exception {
//		// MenuItem menuItem = (MenuItem)menuItemService.findObjByKey(MenuItem.class, id);
//		menuItemService.delete(MenuItem.class, id);
//		uiModel.asMap().clear();
//		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//		return "redirect:/menuitem";
//	}
	
//	@SuppressWarnings("rawtypes")
//	@RequestMapping(method = RequestMethod.GET, produces = "text/html")
//	@ResponseBody
//	public List<Class> list(
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "size", required = false) Integer size,
//			Model uiModel) {
////		 if (page != null || size != null) {
////			 int sizeNo = size == null ? 10 : size.intValue();
////			 final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
////			 uiModel.addAttribute("menuitem", this.menuItemService.findMenuItemsByPage(firstResult, sizeNo));
////			 float nrOfPages = (float) this.menuItemService.findAll() / sizeNo;
////			 uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
////		 } else {
////			 uiModel.addAttribute("menuitem", MenuItem.findAllDoctors());
////		 }
//		
//		List<Class> list = this.menuItemService.findAll(MenuItem.class);
//		 return list;
//	}
	
//	@RequestMapping(value = "create", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String, String> create(@RequestBody MenuItem menuItem) {
//		Map<String, String> map = new HashMap<String, String>();
//		try {
//			this.menuItemService.create(menuItem); // menuItem.persist(); commit
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			map.put("errorMsg", "添加失败,请稍后再试!");
//			return map;
//		}
//		map.put("successMsg", "菜单添加成功!");
//		return map;
//	}

	void populateEditForm(Model uiModel, MenuItem menuItem) {
		uiModel.addAttribute("menuItem", menuItem);
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
