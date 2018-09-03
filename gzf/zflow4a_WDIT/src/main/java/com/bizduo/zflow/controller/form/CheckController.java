package com.bizduo.zflow.controller.form;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping(value = "/check")
public class CheckController {
	
	@Autowired 
	private IUserService userService;	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	/*@Autowired 
	private IUserService userService;
	@Autowired
	private IFormService formService;*/
				//apply1 质检验收申请展示                         apply2材料验收申请展示           apply3 多方验收申请展示
                //apply4 结案申请展示
		       //apply6  施工计划列表展示          apply7  施工进度查询列表展示         
				@RequestMapping(value = "/apply",method = RequestMethod.GET)
	            public ModelAndView apply(@RequestParam(value = "type",required = true)Integer type,Model uiModel){
					
//					ModelAndView mav = new ModelAndView("/importdata/apply8");
					ModelAndView mav = new ModelAndView("/wdit/input/apply8");
					return mav;
				}	
				// accept1   质检验收确认展示              accept2材料验收确认展示          accept3 多方验收确认展示
				//accept4 结案确认展示           accept5 开工确认列表展示
				//accept6   施工计划审核列表展示         
			   @RequestMapping(value = "/accept",method = RequestMethod.GET)	
				public ModelAndView accept(@RequestParam(value = "type",required = true)Integer type,Model uiModel){
				   if(type.intValue()==3){
						uiModel.addAttribute("pageHelpId",115);
					}else if(type.intValue()==5){
						uiModel.addAttribute("pageHelpId",120);
					}else if(type.intValue()==1){
						uiModel.addAttribute("pageHelpId",122);
					}else if(type.intValue()==2){
						uiModel.addAttribute("pageHelpId",124);
					}else if(type.intValue()==6){
						uiModel.addAttribute("pageHelpId",131);
					}else if(type.intValue()==4){
						uiModel.addAttribute("pageHelpId",154);
					}
					ModelAndView mav = new ModelAndView("deco/check/accept" + type);
					return mav;
				}
			   //handle1 质检验收项目申请验收                  handle2 质检验收项目确认验收   
			   //handle3 材料验收申请验收                         handle4财料验收项目确认验收 
			   //handle5多方验收查看详情                          handle6 结案申请上传
			   //handle7 结案确认审核                              handle8 开工确认
			   //handle9 施工计划详细列表展示并保存               handle10 施工计划审核详细确认或拒绝
			   @RequestMapping(value = "/handle",method = RequestMethod.GET)
			   public ModelAndView handle(@RequestParam(value = "type",required = true)Integer type,
					                      @RequestParam(value = "dataId",required = false)String dataId,
					                      @RequestParam(value = "dataId2",required = false)String dataId2,
					                      @RequestParam(value = "addressId",required = false)String addressId,
					                      @RequestParam(value = "dataId4",required = false)String dataId4,
					                      @RequestParam(value = "dataId5",required = false)String dataId5,
					                      @RequestParam(value = "dataId3",required = false)String dataId3,Model uiModel){
				   if(type.intValue()==19){
						uiModel.addAttribute("pageHelpId",30);
					}else if(type.intValue()==11){
						uiModel.addAttribute("pageHelpId",60);
					}else if(type.intValue()==13){
						uiModel.addAttribute("pageHelpId",89);
					}else if(type.intValue()==5){
						uiModel.addAttribute("pageHelpId",92);
					}else if(type.intValue()==9){
						uiModel.addAttribute("pageHelpId",94);
					}else if(type.intValue()==1){
						uiModel.addAttribute("pageHelpId",111);
					}else if(type.intValue()==3){
						uiModel.addAttribute("pageHelpId",113);
					}else if(type.intValue()==6){
						uiModel.addAttribute("pageHelpId",118);
					}else if(type.intValue()==8){
						uiModel.addAttribute("pageHelpId",121);
					}else if(type.intValue()==2){
						uiModel.addAttribute("pageHelpId",123);
					}else if(type.intValue()==4){
						uiModel.addAttribute("pageHelpId",125);
					}else if(type.intValue()==10){
						uiModel.addAttribute("pageHelpId",132);
					}else if(type.intValue()==7){
						uiModel.addAttribute("pageHelpId",155);
					}
				   ModelAndView mav = new ModelAndView("deco/check/handle" + type);
				   
				   uiModel.addAttribute("dataId", dataId);
				   uiModel.addAttribute("dataId2", dataId2);
				   uiModel.addAttribute("dataId3", dataId3);
				   uiModel.addAttribute("dataId4", dataId4);
				   uiModel.addAttribute("dataId5", dataId5);
				   uiModel.addAttribute("addressId", addressId);
				   return mav; 
			   }
			   @RequestMapping(value = "show",method=RequestMethod.GET)
			   public ModelAndView show(@RequestParam(value = "type",required = false)Integer type,
					                    @RequestParam(value = "dataId",required = false)String dataId,
					                    @RequestParam(value = "dataId2",required = false)String dataId2,Model uimodel){
				   if(type.intValue()==1){
					   uimodel.addAttribute("pageHelpId",127);
					}
				   
				   ModelAndView mv = new ModelAndView("deco/contract/formal/show"+type);
				   uimodel.addAttribute("dataId", dataId);
				   return mv;
			   }
			   
			
				//客户进场财料验收列表展示  薛栋
				@RequestMapping(value = "/proposal36", method = RequestMethod.GET)
				public ModelAndView proposal36(@RequestParam(value = "dataId", required = false) String dataId, HttpServletRequest request, Model uiModel){
								ModelAndView mav = new ModelAndView("deco/proposal/listplan24");
								return mav;		
				}
				//主材验收  薛栋
				@RequestMapping(value = "/proposal37", method = RequestMethod.GET)
				public ModelAndView proposal37(@RequestParam(value = "dataId", required = false) String dataId, HttpServletRequest request, Model uiModel){
								ModelAndView mav = new ModelAndView("deco/proposal/constructionplan12");
								uiModel.addAttribute("dataId",  dataId);
								return mav;		
				}
				//压项列表  薛栋
				@RequestMapping(value = "/proposal38", method = RequestMethod.GET)
				public ModelAndView proposal138(@RequestParam(value = "dataId", required = false) String dataId, HttpServletRequest request, Model uiModel){
								ModelAndView mav = new ModelAndView("deco/proposal/listplan26");
								return mav;		
				}
	
	
	//上传施工图
	@RequestMapping(value = "/construction", method = RequestMethod.GET)
	public ModelAndView handle(@RequestParam(value = "proposalId", required = false) String proposalId, Model uiModel){
		ModelAndView mav = new ModelAndView("deco/proposal/construction");
		uiModel.addAttribute("proposalId", proposalId);
		return mav;
	}
	
//////////////////////////////////////////*****************手机端****************////////////////
	
	
	//开工确认列表展示
	   @RequestMapping(value = "/phone/orstartlist",method = RequestMethod.GET)	
		public ModelAndView phoneorstartlist( Model uimodel ){
			ModelAndView mav = new ModelAndView("decoPhone/check/orstartlist");
			User user = userService.findByUserId(UserUtil.getUser().getId());
			uimodel.addAttribute("uid", user.getId());
	        uimodel.addAttribute("username",user.getRealname());
			return mav;
		}
	 //开工确认处理展示
	   @RequestMapping(value = "/phone/orstarthandle",method = RequestMethod.GET)	
		public ModelAndView phoneorstarthandle( @RequestParam(value = "dataId",required = false)String dataId,
				                                @RequestParam(value = "dataId2",required = false)String dataId2,
				                                @RequestParam(value = "dataId3",required = false)String dataId3,
				                                @RequestParam(value = "addressId",required = false)String addressId,Model uimodel ){
			ModelAndView mav = new ModelAndView("decoPhone/check/orstarthandle");
			User user = userService.findByUserId(UserUtil.getUser().getId());
			uimodel.addAttribute("uid", user.getId());
	        uimodel.addAttribute("username",user.getRealname());
	        uimodel.addAttribute("dataId", dataId);
	        uimodel.addAttribute("dataId2", dataId2);
	        uimodel.addAttribute("dataId3", dataId3);
	        uimodel.addAttribute("addressId", addressId);
	        return mav;
		}
	   
	 //apply1 质检验收申请展示                         apply2材料验收申请展示           apply3 多方验收申请展示
       //apply4 结案申请展示
      //apply6  施工计划列表展示             
	   //type=7 批款申请
	   @RequestMapping("/phoneapplylist")
	   public ModelAndView  phoneapplylist(@RequestParam(value = "type",required = false) Integer type,Model uimodel){
		   ModelAndView mv = new ModelAndView("decoPhone/check/applylist"+type);
		   return mv;
	   } 
	
	 //handle1 质检验收项目申请验收                  handle2 质检验收项目确认验收   
	   //handle3 材料验收申请验收                         handle4财料验收项目确认验收 
	   //handle5多方验收查看详情                          handle6 结案申请上传
	   //handle7 结案确认审核                              handle8 开工确认
	   //handle9 施工计划详细列表展示并保存               handle10 施工计划审核详细确认或拒绝
	   @RequestMapping(value = "/phonehandle",method = RequestMethod.GET)
	   public ModelAndView phonehandle(@RequestParam(value = "type",required = true)Integer type,
			                      @RequestParam(value = "dataId",required = false)String dataId,
			                      @RequestParam(value = "dataId2",required = false)String dataId2,
			                      @RequestParam(value = "dataId3",required = false)String dataId3,Model uiModel){
		   ModelAndView mav = new ModelAndView("decoPhone/check/phonehandle" + type);
		   uiModel.addAttribute("dataId", dataId);
		   uiModel.addAttribute("dataId2", dataId2);
		   uiModel.addAttribute("dataId3", dataId3);
		   return mav; 
	   }
	   
	// accept1   质检验收确认展示              accept2材料验收确认展示          accept3 多方验收确认展示
		//accept4 结案确认展示           accept5 开工确认列表展示
		//accept6   施工计划审核列表展示         
	   @RequestMapping(value = "/phoneacceptlist",method = RequestMethod.GET)	
		public ModelAndView phoneacceptlist(@RequestParam(value = "type",required = true)Integer type){
			ModelAndView mav = new ModelAndView("decoPhone/check/acceptlist" + type);
			return mav;
		}
	   
	 //order1 指派项目经理  order2指派监理         
	   @RequestMapping("/phoneorderlist")
	   public ModelAndView  phoneorderlist(@RequestParam(value = "type",required = false) Integer type,Model uimodel){
		   ModelAndView mv = new ModelAndView("decoPhone/check/orderlist"+type);
		   return mv;
	   } 
	   @RequestMapping(value = "/phonechange",method = RequestMethod.GET)
	   public ModelAndView phonechange(@RequestParam(value = "type",required = true)Integer type,
			                      @RequestParam(value = "dataId",required = false)String dataId,
			                      @RequestParam(value = "dataId2",required = false)String dataId2,
			                      @RequestParam(value = "dataId3",required = false)String dataId3,Model uiModel){
		   ModelAndView mav = new ModelAndView("decoPhone/check/phonechange" + type);
		   uiModel.addAttribute("dataId", dataId);
		   uiModel.addAttribute("dataId2", dataId2);
		   uiModel.addAttribute("dataId3", dataId3);
		   return mav; 
	   }
	   @RequestMapping(value = "/phonehandlelayer",method = RequestMethod.GET)
	   public ModelAndView phonehandlelayer(@RequestParam(value = "type",required = true)Integer type,
			                      @RequestParam(value = "dataId",required = false)String dataId,
			                      @RequestParam(value = "dataId2",required = false)String dataId2,
			                      @RequestParam(value = "dataId3",required = false)String dataId3,
			                      @RequestParam(value = "dataId4",required = false)String dataId4,Model uiModel){
		   ModelAndView mav = new ModelAndView("decoPhone/check/phonehandlelayer" + type);
		   uiModel.addAttribute("dataId", dataId);
		   uiModel.addAttribute("dataId2", dataId2);
		   uiModel.addAttribute("dataId3", dataId3);
		   uiModel.addAttribute("dataId4", dataId4);
		   return mav; 
	   }
	 
	 //批款申请   1：前期  2：中期  3：尾期 4:质保金  
	   @RequestMapping(value = "/phonefinanceapply",method = RequestMethod.GET)	
		public ModelAndView phonefinanceapply(@RequestParam(value = "type",required=true)Integer type,
				@RequestParam(value = "dataId",required = false)Integer dataId,
				@RequestParam(value = "dataId2",required = false)Integer dataId2,Model uimodel){
			ModelAndView mav = new ModelAndView("decoPhone/check/phonefinanceapply" + type);
		    uimodel.addAttribute("dataId", dataId);
		    uimodel.addAttribute("dataId2", dataId2);
			return mav;
		}
	
}
