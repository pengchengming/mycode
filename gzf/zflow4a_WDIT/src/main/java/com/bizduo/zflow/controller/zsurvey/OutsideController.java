package com.bizduo.zflow.controller.zsurvey;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.zsurvey.AnswerSheet;
import com.bizduo.zflow.domain.zsurvey.Business;
import com.bizduo.zflow.domain.zsurvey.Outside;
import com.bizduo.zflow.domain.zsurvey.Questionnaire;
import com.bizduo.zflow.service.zsurvey.IAnswerSheetService;
import com.bizduo.zflow.service.zsurvey.IBusinessService;
import com.bizduo.zflow.service.zsurvey.IOutsideService;
import com.bizduo.zflow.service.zsurvey.IQuestionnaireService;
/**
 * 外部调查 - Controller
 * 
 * @author zs
 *
 */
@Controller
@RequestMapping(value = "/outside")
public class OutsideController extends BaseController{

	@Autowired
	private IOutsideService outsideService;
	
	@Autowired
	private IBusinessService businessService;
	
	@Autowired
	private IQuestionnaireService questionnaireService;
	
	@Autowired
	private IAnswerSheetService answerSheetService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) throws Exception{
		model.addAttribute("outsides", outsideService.findAll(Outside.class));
		return "outside/list";
	}
	
	/**
	 * 发送问卷
	 * @throws Exception 
	 */
	@RequestMapping(value = "/postques")
	public String postques(Model model,Integer id) throws Exception{
		model.addAttribute("outsidesId", id);
		model.addAttribute("questionnaires", questionnaireService.findAll(Questionnaire.class));
		return "outside/postques";
	}
	
	/**
	 * 保存发送问卷
	 * @throws Exception 
	 */
	@RequestMapping(value = "/saveques",method = RequestMethod.POST)
	public String saveques(Integer outsidesId,Integer questionnaireId,RedirectAttributes redirectAttributes) throws Exception{
		AnswerSheet answerSheet = new AnswerSheet();
		Outside outside = outsideService.findObjByKey(Outside.class, outsidesId);
		Questionnaire questionnaire = questionnaireService.findObjByKey(Questionnaire.class, questionnaireId);
		Date date = new Date();  
		//SimpleDateFormat ssdf = new SimpleDateFormat("yyyy-MM-dd");  
//        String sdate = ssdf.format(d);  
		answerSheet.setCreate_time(date);
		answerSheet.setMemberName(outside.getStuName());
		answerSheet.setOutside(outside);
		answerSheet.setStates(0);
		answerSheet.setCondition(1);
		answerSheet.setQuestionnaire(questionnaire);
		answerSheetService.create(answerSheet);
		outside.setStates(1);
		outsideService.update(outside);
		return "redirect:list";
	}
	
	/**
	 * 添加（前台）
	 * @throws Exception 
	 */
	@RequestMapping(value = "/add")
	public String add(Model model) throws Exception{
		model.addAttribute("businesss", businessService.findAll(Business.class));
		return "outside/add";
	}
	
	
	/**
	 * 保存（前台）
	 * @throws Exception 
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(Outside outside,Integer businessId,RedirectAttributes redirectAttributes,Model model) throws Exception{
		Outside outside2 = outsideService.getByTelName(outside.getTel(), outside.getStuName());
		if(outside2==null){
		Business business = businessService.findObjByKey(Business.class, businessId);
		outside.setBusiness(business);
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("MMddss");  
        String date = sdf.format(d);  
		outside.setOscarId(date);
		SimpleDateFormat ssdf = new SimpleDateFormat("yyyy-MM-dd");  
        String sdate = ssdf.format(d);  
        java.sql.Date ssd = java.sql.Date.valueOf(sdate);
		outside.setSurveyDate(ssd);
		outsideService.create(outside);
		model.addAttribute("success", "提交成功!");
		model.addAttribute("businesss", businessService.findAll(Business.class));
		return "outside/add";
		}else{
			model.addAttribute("message", "不能重复提交!");
			model.addAttribute("businesss", businessService.findAll(Business.class));
			return "outside/add";
		}
	}
}
