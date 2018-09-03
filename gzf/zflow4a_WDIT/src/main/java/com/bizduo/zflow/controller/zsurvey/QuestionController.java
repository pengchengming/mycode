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
import com.bizduo.zflow.domain.zsurvey.Question;
import com.bizduo.zflow.domain.zsurvey.QuestionType;
import com.bizduo.zflow.domain.zsurvey.Questionnaire;
import com.bizduo.zflow.service.zsurvey.IQuestionService;
import com.bizduo.zflow.service.zsurvey.IQuestionTypeService;
import com.bizduo.zflow.service.zsurvey.IQuestionnaireService;
/**
 * 问题 - Controller
 * 
 * @author zs
 *
 */
@Controller
@RequestMapping(value = "/question")
public class QuestionController extends BaseController{

	@Autowired
	private IQuestionService questionService;
	
	@Autowired
	private IQuestionnaireService questionnaireService;
	
	@Autowired
	private IQuestionTypeService questionTypeService;
	
	/**
	 * 问题列表
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public String list(Model model,Integer questionnaireId) throws Exception{
		model.addAttribute("questions", questionService.getByStates(questionnaireId)) ;
		model.addAttribute("questionnaire", questionnaireService.findObjByKey(Questionnaire.class, questionnaireId)) ;
		return "question/list";
	}
	
	/**
	 * 添加
	 * @throws Exception 
	 */
	@RequestMapping(value = "/add")
	public String add(Model model,Integer questionnaireId) throws Exception{
		Questionnaire questionnaire = questionnaireService.findObjByKey(Questionnaire.class, questionnaireId);
		model.addAttribute("questionnaire", questionnaire) ;
		model.addAttribute("questionTypes", questionTypeService.findAll(QuestionType.class)) ;
		return "question/add";
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(Question question,Integer questionnaireId,Integer questionTypeId, RedirectAttributes redirectAttributes) throws Exception{
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("MMddss");  
        String date = sdf.format(d);  
		question.setCode(date);
		//SimpleDateFormat catesdf = new SimpleDateFormat("yyyy-MM-dd"); 
		//String catedate = catesdf.format(d);
		question.setCreatTime(d);
		question.setQuestionType(questionTypeService.findObjByKey(QuestionType.class, questionTypeId));
		question.setQuestionnaire(questionnaireService.findObjByKey(Questionnaire.class, questionnaireId));
		questionService.create(question);
		return "redirect:list.do?questionnaireId="+questionnaireId;
	}
	
	
	/**
	 * 编辑
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit")
	public String edit(Model model,Integer questionId, Integer questionnaireId) throws Exception{
		Question question = questionService.findObjByKey(Question.class, questionId);
		Questionnaire questionnaire = questionnaireService.findObjByKey(Questionnaire.class, questionnaireId);
		model.addAttribute("question", question) ;
		model.addAttribute("questionnaire", questionnaire) ;
		model.addAttribute("questionTypes", questionTypeService.findAll(QuestionType.class)) ;
		return "question/edit";
	}
	
	
	/**
	 * 更新
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public String update(Question question,Integer questionnaireId,Integer questionTypeId, RedirectAttributes redirectAttributes) throws Exception{
		Question upQuestion = questionService.findObjByKey(Question.class, question.getId());
		upQuestion.setContext(question.getContext());
		upQuestion.setStatus(question.getStatus());
		upQuestion.setQuestionType(questionTypeService.findObjByKey(QuestionType.class, questionTypeId));
		upQuestion.setQuestionnaire(questionnaireService.findObjByKey(Questionnaire.class, questionnaireId));
		questionService.update(upQuestion);
		return "redirect:list.do?questionnaireId="+questionnaireId;
	}
	
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	public String delete(Integer questionId, Integer questionnaireId, RedirectAttributes redirectAttributes) throws Exception{
		questionService.delete(Question.class, questionId);
		return "redirect:list.do?questionnaireId="+questionnaireId;
	}
}
