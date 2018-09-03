package com.bizduo.zflow.controller.zsurveym;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.domain.zsurvey.Answer;
import com.bizduo.zflow.domain.zsurvey.AnswerQuestion;
import com.bizduo.zflow.domain.zsurvey.AnswerSheet;
import com.bizduo.zflow.domain.zsurvey.Outside;
import com.bizduo.zflow.domain.zsurvey.Question;
import com.bizduo.zflow.domain.zsurvey.Questionnaire;
import com.bizduo.zflow.service.zsurvey.IAnswerQuestionService;
import com.bizduo.zflow.service.zsurvey.IAnswerService;
import com.bizduo.zflow.service.zsurvey.IAnswerSheetService;
import com.bizduo.zflow.service.zsurvey.IQuestionService;
import com.bizduo.zflow.service.zsurvey.IQuestionnaireService;


@Controller
@RequestMapping(value = "/zsurveym_answersheet")
public class AnswerSheetMController {
	
	@Autowired
	private IQuestionnaireService questionnaireService;
	
	@Autowired
	private IQuestionService questionService;
	
	@Autowired
	private IAnswerSheetService answerSheetService;
	
	@Autowired
	private IAnswerQuestionService answerQuestionService;
  
	@Autowired
	private IAnswerService answerService;
	/*
	@Autowired
	private IMemberService memberService;
	
	@Autowired
	private IOutsideService outsideService;
	
	@Autowired
	private IBusinessService businessService;
	@Autowired
	private IOptionService optionService;
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
	
	/**
	 * 答卷/答题详情
	 */
	
	@RequestMapping(value="/info/{answersheetCode}",method=RequestMethod.GET)
    public ModelAndView  getProductInfo(@PathVariable String answersheetCode, HttpServletRequest request,HttpServletResponse response) throws Exception {
    
          ModelAndView mav = new ModelAndView("answersheet/show");
  		AnswerSheet answersheet  = answerSheetService.findByCode(AnswerSheet.class, answersheetCode);
  		List<AnswerQuestion> answerquestions = answerQuestionService.getByAnswersheetCode(answersheetCode);  
		mav.addObject("answersheet", answersheet);
  		mav.addObject("answerquestions",answerquestions);
          return mav;
         
    }
	
	
	@RequestMapping(value="/answersheet",method=RequestMethod.PUT)
    public ModelAndView updateAnswerSheet(@RequestBody AnswerSheet answersheet, HttpServletRequest request,HttpServletResponse response) throws Exception {
   
  Map<String,Object> map1 = new HashMap<String,Object>();
  ModelAndView mav=new ModelAndView("answersheet/show",map1);
  
  answersheet.setStates(1);
  
  answersheet=answerSheetService.create(answersheet);
 
  mav.addObject("answersheet", answersheet); 
   
  return mav;
         
    }

	
	@RequestMapping(value="/answerquestionlist",method=RequestMethod.PUT)
    public ModelAndView updateAnswerSheet(@RequestBody List<AnswerQuestion> answerquestionlist, HttpServletRequest request,HttpServletResponse response) throws Exception {
   
  Map<String,Object> map1 = new HashMap<String,Object>(); 
   
  ModelAndView mav=new ModelAndView("answersheet/show",map1);
  
  AnswerSheet answersheet = null;
  
  for(int i=0;i<answerquestionlist.size();i++){
	  AnswerQuestion answerQuestion =  answerquestionlist.get(i);

	  if(answersheet==null)
		  answersheet =  answerSheetService.findObjByKey(AnswerSheet.class, answerQuestion.getAnswerSheet().getId());
	  
	  Question question = questionService.findObjByKey(Question.class, answerQuestion.getQuestion().getId());
	  
	  answerQuestion = answerQuestionService.create(answerQuestion);
	  
	  for(Answer answer:answerQuestion.getAnswerOptions()){
		  answer.setAnswerQuestion(answerQuestion);
		  answer.setAnswerSheet(answersheet);
		  answer.setQuestion(question);
		  answer =  answerService.create(answer);
	  }
	   
	   
  }
  
  int score =  answerSheetService.getAnswerSheetScore(answersheet.getId().intValue());
  
  mav.addObject("score", score); 
  
  return mav;
         
    }
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/callshellprocedure" )
    public ModelAndView  getAnswerSheetList(@RequestParam(value = "p", required = false) String p, HttpServletRequest request,HttpServletResponse response) throws Exception {
    
          ModelAndView mav = new ModelAndView("answersheet/show");
          List   resultset  = answerSheetService.callShellProcedure(p);
  		
          mav.addObject("resultset", resultset); 
          return mav;
    }
		

}
