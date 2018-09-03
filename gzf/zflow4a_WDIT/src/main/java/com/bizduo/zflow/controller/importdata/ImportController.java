package com.bizduo.zflow.controller.importdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.importdata.ImportTable;
import com.bizduo.zflow.service.importdata.IImportTableService;
@Controller
@RequestMapping(value = "/import")
public class ImportController extends BaseController{

	@Autowired
	private IImportTableService importTableService;
	
	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public ModelAndView excel(@RequestParam(value = "name", required = false) String name){
		ImportTable obj = importTableService.getByName(name);
		String desc = null == obj ? name : obj.getDescription();
		ModelAndView mav = new ModelAndView("import/excel"); 
		mav.addObject("name", name);
		mav.addObject("title", desc);
		return mav;
	}
	
	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public ModelAndView template(@RequestParam(value = "name", required = false) String name){
		ModelAndView mav = new ModelAndView("redirect:/dataImport"); 
		mav.addObject("name", name);
		return mav;
	}
	
	@RequestMapping(value = "/importdatalist", method = RequestMethod.GET)
	public ModelAndView importdatalist(@RequestParam(value = "name", required = false) String name){
		ModelAndView mav = new ModelAndView("importdata/importdatalist");
		mav.addObject("name", name);
//		mav.addObject("title", name.substring(name.indexOf("_") + 1));
		return mav;
	}
	
	//将导入数据copy给客服使用
	@RequestMapping(value = "/importdatadetail", method = RequestMethod.GET)
	public ModelAndView importdatadetail(@RequestParam(value="type",required = false)Integer type,
			                          @RequestParam(value = "name", required = false) String name){
		ModelAndView mav = new ModelAndView("importdata/importdatadetail"+type);
		mav.addObject("name", name);
		mav.addObject("type", type);
//		mav.addObject("title", name.substring(name.indexOf("_") + 1));
		return mav;
	}
	
	
		
		//将导入数据copy给客服使用 (PathVariable )
				@RequestMapping(value = "/importPath/{code}")
		public ModelAndView importPath(@PathVariable("code") String code){
					ModelAndView mav = new ModelAndView("/wdit/input/importdatachange");
//			ModelAndView mav = new ModelAndView("importdata/importdatachange");		
					mav.addObject("code", code);
					return mav;
				}
				
				//导入客户
				@RequestMapping(value = "/importPaths/{code}")
		public ModelAndView importPaths(@PathVariable("code") String code){
			ModelAndView mav = new ModelAndView("importdata/importcustomer");		
					mav.addObject("code", code);
					return mav;
				}
	
	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public ModelAndView customer(@RequestParam(value = "name", required = false) String name){
		ModelAndView mav = new ModelAndView("deco/customer/create"); 
//		mav.addObject("name", name);
//		mav.addObject("title", name.substring(name.indexOf("_") + 1));
		return mav;
	}
	
}
