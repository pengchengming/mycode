package com.bizduo.zflow.controller.zsurvey;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.domain.zsurvey.AnswerSheet;
import com.bizduo.zflow.domain.zsurvey.Outside;
import com.bizduo.zflow.domain.zsurvey.Question;
import com.bizduo.zflow.domain.zsurvey.Questionnaire;
import com.bizduo.zflow.service.zsurvey.IAnswerSheetService;
import com.bizduo.zflow.service.zsurvey.IQuestionService;
import com.bizduo.zflow.service.zsurvey.IQuestionnaireService;


@Controller
@RequestMapping(value = "/answersheet")
public class AnswerSheetController {
	
	@Autowired
	private IQuestionnaireService questionnaireService;
	
	@Autowired
	private IQuestionService questionService;
	
	@Autowired
	private IAnswerSheetService answerSheetService;
	/*
	@Autowired
	private IOptionService optionService;
	
	@Autowired
	private IAnswerService answerService;
	
	@Autowired
	private IMemberService memberService;
	
	@Autowired
	private IOutsideService outsideService;
	
	@Autowired
	private IBusinessService businessService;
	*/
	
	/**
	 * 答卷详情
	 */
	@RequestMapping(value = "/getAnswerSheet" ,method = RequestMethod.GET)
	public ModelAndView getQuestionnaire(Integer ansId, String message, @RequestParam(value = "id", required = true) Integer id) throws Exception{
		ModelAndView mav = new ModelAndView("zsurvey/answersheet/show");
		Questionnaire questionnaire = questionnaireService.findObjByKey(Questionnaire.class, id);
		mav.addObject("questionnaire",questionnaire);
		List<Question> questions = questionService.getByParentId(id);
		AnswerSheet answerSheet = answerSheetService.findObjByKey(AnswerSheet.class, ansId);
		if(answerSheet!=null){
			mav.addObject("answerSheetId", answerSheet.getId());
		}
//		Member member = new Member();
//		if(answerSheet!=null){
//			member = memberService.findObjByKey(Member.class, answerSheet.getMember().getId());
//			mav.addObject("member", member);
//		}
		Outside outside = answerSheet.getOutside();
		if(outside!=null){
			mav.addObject("outside", outside);
		}
		mav.addObject("questions", questions);
		mav.addObject("message", message);
		return mav;
	}	

}
