package com.bizduo.zflow.controller.exportdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.importdata.ImportTable;
import com.bizduo.zflow.service.importdata.IImportTableService;
@Controller
@RequestMapping(value = "/export")
public class ExportController extends BaseController{

	@Autowired
	private IImportTableService importTableService;
	
	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public ModelAndView excel(@RequestParam(value = "name", required = false) String name){
		ImportTable obj = importTableService.getByName(name);
		String desc = null == obj ? name : obj.getDescription();
		ModelAndView mav = new ModelAndView("export/excel"); 
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
}
