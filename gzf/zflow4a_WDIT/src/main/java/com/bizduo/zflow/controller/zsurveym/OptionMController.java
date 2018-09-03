package com.bizduo.zflow.controller.zsurveym;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.zsurvey.Option;
import com.bizduo.zflow.domain.zsurvey.Question;
import com.bizduo.zflow.domain.zsurvey.Questionnaire;
import com.bizduo.zflow.service.zsurvey.IOptionService;
import com.bizduo.zflow.service.zsurvey.IQuestionService;
import com.bizduo.zflow.service.zsurvey.IQuestionnaireService;

/**
 * 答案选项 - Controller
 * 
 * @author zs
 *
 */
@Controller
@RequestMapping(value = "/zsurveym_option")
public class OptionMController extends BaseController{

	@Autowired
	private IQuestionService questionService;
	
	@Autowired
	private IOptionService optionService;
	
	@Autowired
	private IQuestionnaireService questionnaireService;
	
	/**
	 * 选项列表
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public String list(Model model,Integer questionId,Integer questionnaireId) throws Exception{
		model.addAttribute("question", questionService.findObjByKey(Question.class, questionId)) ;
		model.addAttribute("questionnaire", questionnaireService.findObjByKey(Questionnaire.class, questionnaireId)) ;
		model.addAttribute("options", optionService.getByParentId(questionId)) ;
		return "option/list";
	}
	
	/**
	 * 添加
	 * @throws Exception 
	 */
	@RequestMapping(value = "/add")
	public String add(Model model,Integer questionId,Integer questionnaireId) throws Exception{
		model.addAttribute("questionnaireId", questionnaireId) ;
		model.addAttribute("question", questionService.findObjByKey(Question.class, questionId));
		return "option/add";
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(Option option,Integer questionId,Integer questionnaireId, RedirectAttributes redirectAttributes) throws Exception{
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("MMddss");  
        String date = sdf.format(d);  
		option.setCode(date);
		option.setQuestion(questionService.findObjByKey(Question.class, questionId));
		optionService.create(option);
		return "redirect:list?questionId="+questionId+"&questionnaireId="+questionnaireId;
	}
	
	/**
	 * 编辑
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit")
	public String edit(Model model,Integer optionId,Integer questionId,Integer questionnaireId) throws Exception{
		model.addAttribute("option", optionService.findObjByKey(Option.class, optionId));
		model.addAttribute("question", questionService.findObjByKey(Question.class, questionId));
		model.addAttribute("questionnaireId", questionnaireId);
		return "option/edit";
	}
	
	
	/**
	 * 更新
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public String update(Option option,Integer questionId,Integer questionnaireId, RedirectAttributes redirectAttributes) throws Exception{
		Option upOption = optionService.findObjByKey(Option.class, option.getId());
		upOption.setCntxt(option.getCntxt());
		upOption.setScore(option.getScore());
		optionService.update(upOption);
		return "redirect:list?questionId="+questionId+"&questionnaireId="+questionnaireId;
	}
	
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	public String delete(Integer questionId,Integer questionnaireId, Integer id, RedirectAttributes redirectAttributes) throws Exception{
		optionService.delete(Option.class, id);
		return "redirect:list?questionId="+questionId+"&questionnaireId="+questionnaireId;
	}
}
