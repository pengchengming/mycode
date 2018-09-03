package com.bizduo.zflow.controller.zsurvey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.zsurvey.Answer;
import com.bizduo.zflow.domain.zsurvey.AnswerQuestion;
import com.bizduo.zflow.domain.zsurvey.AnswerSheet;
import com.bizduo.zflow.domain.zsurvey.Business;
import com.bizduo.zflow.domain.zsurvey.Option;
import com.bizduo.zflow.domain.zsurvey.Outside;
import com.bizduo.zflow.domain.zsurvey.Question;
import com.bizduo.zflow.domain.zsurvey.Questionnaire;
import com.bizduo.zflow.service.zsurvey.IAnswerQuestionService;
import com.bizduo.zflow.service.zsurvey.IAnswerService;
import com.bizduo.zflow.service.zsurvey.IAnswerSheetService;
import com.bizduo.zflow.service.zsurvey.IBusinessService;
import com.bizduo.zflow.service.zsurvey.IOptionService;
import com.bizduo.zflow.service.zsurvey.IQuestionService;
import com.bizduo.zflow.service.zsurvey.IQuestionnaireService;

/**
 * 问卷 - Controller
 * 
 * @author zs
 *
 */

@Controller
@RequestMapping(value = "/questionnaire")
public class QuestionnaireController extends BaseController{
	
	@Autowired
	private IQuestionnaireService questionnaireService;
	
	@Autowired
	private IQuestionService questionService;
	
	@Autowired
	private IAnswerSheetService answerSheetService;
	
	@Autowired
	private IOptionService optionService;
	
	@Autowired
	private IAnswerService answerService;
	
	@Autowired
	private IAnswerQuestionService answerquestionService;
	/*
	@Autowired
	private IMemberService memberService;
	
	@Autowired
	private IOutsideService outsideService;
	*/
	@Autowired
	private IBusinessService businessService;
	
	/**
	 * 试卷列表
	 */
	@RequestMapping(value = "/template")
	public String template(Model model){
		List<AnswerSheet> lists = new ArrayList<AnswerSheet>();
		List<Questionnaire> questionnaires = questionnaireService.getByIsOnOff();
		List<AnswerSheet> answerSheets = (List<AnswerSheet>) answerSheetService.getByQuesnareIds();
		if(questionnaires!=null&&answerSheets!=null){
		for (AnswerSheet answerSheet : answerSheets) {
			for (Questionnaire questionnaire : questionnaires) {
				if(answerSheet.getQuestionnaire()==questionnaire){
					lists.add(answerSheet);
				}
			}
		}
		}
		model.addAttribute("answerSheets", lists) ;
		return "questionnaire/template";
	}
	
	/**
	 * 试卷详情
	 */
	@RequestMapping(value = "/getQuestionnaire" ,method = RequestMethod.GET)
	public ModelAndView getQuestionnaire(String message, @RequestParam(value = "id", required = true) Integer id) throws Exception{
		ModelAndView mav = new ModelAndView("questionnaire/show");
		Questionnaire questionnaire = questionnaireService.findObjByKey(Questionnaire.class, id);
		mav.addObject("questionnaire",questionnaire);
		List<Question> questions = questionService.getByParentId(id);
//		AnswerSheet answerSheet = answerSheetService.findObjByKey(AnswerSheet.class, ansId);
//		if(answerSheet!=null){
//			mav.addObject("answerSheetId", answerSheet.getId());
//		}
//		Member member = new Member();
//		if(answerSheet!=null){
//			member = memberService.findObjByKey(Member.class, answerSheet.getMember().getId());
//			mav.addObject("member", member);
//		}
//		Outside outside = answerSheet.getOutside();
//		if(outside!=null){
//			mav.addObject("outside", outside);
//		}
		mav.addObject("questions", questions);
		mav.addObject("message", message);
		return mav;
	}
	
	/**
	 * 提交试卷
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/save_questionnaire" ,method = RequestMethod.POST)
	public String saveQuestionnaire( Integer answerSheetId,String[] questionIds, String[] optionNames,
			Integer quesnId,String[] text, Integer[] textId,String[] questiontextvalue,String[] questiontextvalueIDs
			,String[] optiontextvalue,String[] optiontextvalueIDs,Model model,String memberName) throws Exception{
		
		HashMap questiontextvalueHashmap= new HashMap();
		if(questiontextvalueIDs!=null){
			for(Integer i=0;i<questiontextvalueIDs.length;i++){
				questiontextvalueHashmap.put(questiontextvalueIDs[i], questiontextvalue[i]);
			}
		}
		
		HashMap optiontextvalueHashmap= new HashMap();
		if(optiontextvalueIDs!=null){
			for(Integer i=0;i<optiontextvalueIDs.length;i++){
				optiontextvalueHashmap.put(optiontextvalueIDs[i], optiontextvalue[i]);
			}
		}
		
		AnswerSheet answerSheet = answerSheetService.findObjByKey(AnswerSheet.class, answerSheetId);
		if(answerSheet.getStates()==0){
		Date date = new Date();  
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//        String date = sdf.format(d);  
		answerSheet.setCreate_time(date);
		answerSheet.setStates(1);
		answerSheet.setMemberName(memberName);
		answerSheetService.update(answerSheet);
		
		//填空
		if(text!=null){
		for (Integer y=0;y<text.length;y++) {
			if(text[y]!=null&&!text[y].equalsIgnoreCase("")){
				Answer answer = new Answer();
			    Question question = questionService.findObjByKey(Question.class, textId[y]);
			    
			    
				//保存题干信息
				AnswerQuestion answerquestion= new AnswerQuestion();
				answerquestion.setAnswerSheet(answerSheet);
				answerquestion.setQuestion(question);
				if(questiontextvalueHashmap.containsKey(textId[y].toString()))
					answerquestion.setTextvalue(questiontextvalueHashmap.get(textId[y].toString()).toString());
				else
					answerquestion.setTextvalue("");
				answerquestionService.create(answerquestion);

				
				answer.setAnswerSheet(answerSheet);
				answer.setCreate_time(date);
				answer.setQuestion(question);
				answer.setText_value(text[y]);
				if(optiontextvalueHashmap.containsKey(textId[y].toString()))
					answer.setTextvalue(optiontextvalueHashmap.get(textId[y].toString()).toString());
				else
					answer.setTextvalue("");
								
				answerService.create(answer);
			}
		}
		}
		//多选
		for(Integer i=0;i<questionIds.length;i++){
			if(!questionIds[i].equalsIgnoreCase("")){
				Integer questionId = Integer.parseInt(questionIds[i]);
				Question question = questionService.findObjByKey(Question.class, questionId);
				
				//保存题干信息
				AnswerQuestion answerquestion= new AnswerQuestion();
				answerquestion.setAnswerSheet(answerSheet);
				answerquestion.setQuestion(question);
				if(questiontextvalueHashmap.containsKey(questionId.toString()))
					answerquestion.setTextvalue(questiontextvalueHashmap.get(questionId.toString()).toString());
				else
					answerquestion.setTextvalue("");
				
				answerquestionService.create(answerquestion);
				
				
				String str = optionNames[i];
				if(str!=null&&!str.equalsIgnoreCase("")){
					if(str.indexOf("o")!=-1){
						String strTmp = str.replaceAll ("o", ",");
						String str2 = strTmp.substring(0, strTmp.length() - 1);
						String [] stringArr= str2.split(","); 
						for(Integer j=0;j<stringArr.length;j++){
							Integer oId =Integer.parseInt(stringArr[j]);
							Option option = optionService.findObjByKey(Option.class, oId);
							Answer answer = new Answer();
							answer.setAnswerSheet(answerSheet);
							answer.setCreate_time(date);
							answer.setOption(option);
							answer.setScore(option.getScore());
							answer.setQuestion(question);
							answer.setText_value(option.getCntxt());
							if(optiontextvalueHashmap.containsKey(oId.toString()))
								answer.setTextvalue(optiontextvalueHashmap.get(oId.toString()).toString());
							
							answerService.create(answer);
						}
						}else{
							//单选
							Answer answer = new Answer();
							Integer optionId = Integer.parseInt(optionNames[i]);
							Option option = optionService.findObjByKey(Option.class, optionId);
							answer.setAnswerSheet(answerSheet);
							answer.setScore(option.getScore());
							answer.setCreate_time(date);
							answer.setOption(option);
							answer.setQuestion(question);
							answer.setText_value(option.getCntxt());
							if(optiontextvalueHashmap.containsKey(optionId.toString()))
								answer.setTextvalue(optiontextvalueHashmap.get(optionId.toString()).toString());
							
							answerService.create(answer);
						}
				}
			}
		}
		}else{
			return "redirect:getQuestionnaire?id="+quesnId+"&message=1";
		}
		return "redirect:template";
	}

	/**
	 * 提交试卷2
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/save_questionnaire2" ,method = RequestMethod.POST)
	public String saveQuestionnaire2( String[] questionIds, String[] optionNames,
			Integer quesnId,String[] text, Integer[] textId,String[] questiontextvalue,String[] questiontextvalueIDs
			,String[] optiontextvalue,String[] optiontextvalueIDs,Model model,String memberName) throws Exception{
		Questionnaire qustnr = questionnaireService.findObjByKey(Questionnaire.class, quesnId);
		AnswerSheet answerSheet = new AnswerSheet();
//				answerSheetService.findObjByKey(AnswerSheet.class, answerSheetId);
		HashMap questiontextvalueHashmap= new HashMap();
		if(questiontextvalueIDs!=null){
			for(Integer i=0;i<questiontextvalueIDs.length;i++){
				questiontextvalueHashmap.put(questiontextvalueIDs[i], questiontextvalue[i]);
			}
		}
		
		HashMap optiontextvalueHashmap= new HashMap();
		if(optiontextvalueIDs!=null){
			for(Integer i=0;i<optiontextvalueIDs.length;i++){
				optiontextvalueHashmap.put(optiontextvalueIDs[i], optiontextvalue[i]);
			}
		}
		
//		if(answerSheet.getStates()==0){
		Date date = new Date();  
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//        String date = sdf.format(d);  
		answerSheet.setCreate_time(date);
		answerSheet.setQuestionnaire(qustnr);
		answerSheet.setStates(1);
		answerSheet.setMemberName(memberName);
		answerSheet = answerSheetService.create(answerSheet);
		//填空
		if(text!=null){
		for (Integer y=0;y<text.length;y++) {
			if(text[y]!=null&&!text[y].equalsIgnoreCase("")){
				
				Answer answer = new Answer();
			    Question question = questionService.findObjByKey(Question.class, textId[y]);

				//保存题干信息
				AnswerQuestion answerquestion= new AnswerQuestion();
				answerquestion.setAnswerSheet(answerSheet);
				answerquestion.setQuestion(question);
				if(questiontextvalueHashmap.containsKey(textId[y].toString()))
					answerquestion.setTextvalue(questiontextvalueHashmap.get(textId[y].toString()).toString());
				else
					answerquestion.setTextvalue("");
				
				
				answerquestionService.create(answerquestion);
				
			    answer.setAnswerSheet(answerSheet);
				answer.setCreate_time(date);
				answer.setQuestion(question);
				answer.setText_value(text[y]);
				
				if(optiontextvalueHashmap.containsKey(textId[y].toString()))
					answer.setTextvalue(optiontextvalueHashmap.get(textId[y].toString()).toString());
				else
					answer.setTextvalue("");
				
				answerService.create(answer);
			}
		}
		}
		//多选
		for(Integer i=0;i<questionIds.length;i++){
			if(!questionIds[i].equalsIgnoreCase("")){
				Integer questionId = Integer.parseInt(questionIds[i]);
				Question question = questionService.findObjByKey(Question.class, questionId);
				//保存题干信息

				//保存题干信息
				AnswerQuestion answerquestion= new AnswerQuestion();
				answerquestion.setAnswerSheet(answerSheet);
				answerquestion.setQuestion(question);
				if(questiontextvalueHashmap.containsKey(questionId.toString()))
					answerquestion.setTextvalue(questiontextvalueHashmap.get(questionId.toString()).toString());
				else
					answerquestion.setTextvalue("");
				
				answerquestionService.create(answerquestion);
				

				String str = optionNames[i];
				if(str!=null&&!str.equalsIgnoreCase("")){
					if(str.indexOf("o")!=-1){
						String strTmp = str.replaceAll ("o", ",");
						String str2 = strTmp.substring(0, strTmp.length() - 1);
						String [] stringArr= str2.split(","); 
						for(Integer j=0;j<stringArr.length;j++){
							Integer oId =Integer.parseInt(stringArr[j]);
							Option option = optionService.findObjByKey(Option.class, oId);
							Answer answer = new Answer();
							answer.setAnswerSheet(answerSheet);
							answer.setCreate_time(date);
							answer.setOption(option);
							answer.setScore(option.getScore());
							answer.setQuestion(question);
							answer.setText_value(option.getCntxt());
							if(optiontextvalueHashmap.containsKey(oId.toString()))
								answer.setTextvalue(optiontextvalueHashmap.get(oId.toString()).toString());
							answerService.create(answer);
						}
						}else{
							//单选
							Answer answer = new Answer();
							Integer optionId = Integer.parseInt(optionNames[i]);
							Option option = optionService.findObjByKey(Option.class, optionId);
							answer.setAnswerSheet(answerSheet);
							answer.setScore(option.getScore());
							answer.setCreate_time(date);
							answer.setOption(option);
							answer.setQuestion(question);
							answer.setText_value(option.getCntxt());
							if(optiontextvalueHashmap.containsKey(optionId.toString()))
								answer.setTextvalue(optiontextvalueHashmap.get(optionId.toString()).toString());
							answerService.create(answer);
						}
				}
			}
		}
//		answerSheetService.create(answerSheet);
//		}else{
//			return "redirect:getQuestionnaire?id="+quesnId+"&message=1";
//		}
		return "redirect:template";
	}
	
	/**
	 * 试卷记录列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/result",method = RequestMethod.GET)
	public String result(Model model){
		model.addAttribute("answerSheets", answerSheetService.getByStates()) ;
		return "questionnaire/answerSheets";
	}
	
	/**
	 * 答案汇总 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getAnswer",method = RequestMethod.GET)
	public String getAnswer(Integer id,Model model) throws Exception{
		AnswerSheet answerSheet = answerSheetService.findObjByKey(AnswerSheet.class, id);
		Questionnaire questionnaire  = answerSheet.getQuestionnaire();
		List<Question> questions = questionService.getByParentId(questionnaire.getId());
		List<Answer> answers = answerService.getByAnswerSheetId(id);
		List<AnswerQuestion> answerquestions = answerquestionService.getByAnswerQuestionSheetId(id);
		
		Integer count = 0;
		for (Answer answer : answers) {
			if(answer.getScore()!=null)
			count+=answer.getScore();
		}
//		Member member = memberService.findObjByKey(Member.class, answerSheet.getMember().getId());
		Outside outside = answerSheet.getOutside();
//		model.addAttribute("member", member);
		model.addAttribute("outside", outside);
		model.addAttribute("answers", answers);
		model.addAttribute("answerquestions", answerquestions);		
		model.addAttribute("count", count);
		model.addAttribute("questions", questions);
		model.addAttribute("questionnaire", questionnaire);
//		model.addAttribute("questionnaireName", questionnaire.getCnname());
		return "questionnaire/answer_result";
	}
	
	/**
	 * 添加问卷（后台）
	 * @return
	 */
	@RequestMapping(value = "/add",method = RequestMethod.GET)
	public String addQuestionnaire(Model model){
		model.addAttribute("businesss", businessService.findAll(Business.class));
		return "questionnaire/add";
	}
	
	/**
	 * 保存问卷（后台）
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(Questionnaire questionnaire,Integer businessId, RedirectAttributes redirectAttributes) throws Exception{
		questionnaire.setIsDelete(0);
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("MMddss");  
        String date = sdf.format(d);  
		questionnaire.setCode(date);
		SimpleDateFormat catesdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String catedate = catesdf.format(d);
		questionnaire.setPublishDate(catedate);
		questionnaire.setBusiness(businessService.findObjByKey(Business.class, businessId));
		questionnaireService.create(questionnaire);
		return "redirect:list";
	}
	
	/**
	 * 编辑（后台）
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(Model model, Integer id) throws Exception{
		model.addAttribute("questionnaire", questionnaireService.findObjByKey(Questionnaire.class, id)) ;
		model.addAttribute("businesss", businessService.findAll(Business.class));
		return "questionnaire/edit";
	}
	
	/**
	 * 更新问卷（后台）
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public String update(Questionnaire questionnaire,Integer businessId, RedirectAttributes redirectAttributes) throws Exception{
		Questionnaire upQuestionnaire = questionnaireService.findObjByKey(Questionnaire.class, questionnaire.getId());
		upQuestionnaire.setBusiness(businessService.findObjByKey(Business.class, businessId));
		upQuestionnaire.setCnname(questionnaire.getCnname());
		upQuestionnaire.setDescription(questionnaire.getDescription());
		upQuestionnaire.setIsOnOff(questionnaire.getIsOnOff());
		upQuestionnaire.setEntitle(questionnaire.getEntitle());
		questionnaireService.update(upQuestionnaire);
		return "redirect:list";
	}
	
	/**
	 * 问卷列表（后台）
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public String list(Model model){
		model.addAttribute("questionnaires", questionnaireService.getByIsDelete());
		return "questionnaire/list";
	}
	
	
	/**
	 * 问卷详情（后台）
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/view",method = RequestMethod.GET)
	public String view(Integer id,Model model) throws Exception{
		model.addAttribute("questionnaire", questionnaireService.findObjByKey(Questionnaire.class, id)) ;
		return "questionnaire/view";
	}
	
	/**
	 * 删除记录(后台 - 假删除)
	 * @param id
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	public String delete(Integer id, RedirectAttributes redirectAttributes) throws Exception{
		Questionnaire questionnaire = questionnaireService.findObjByKey(Questionnaire.class, id);
		questionnaire.setIsDelete(1);
		questionnaireService.update(questionnaire);
		return "redirect:list";
	}
}
