package com.bizduo.zflow.controller.WDIT;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping("bureauhuman")
public class BureauhumanController extends BaseController {
	@Autowired
	private IFormService formService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	//人社局审批
	@RequestMapping("rsjApprovalList")
	public String approvalCompany(Model model){
		return "/wdit/bureauhuman/rsjApprovalList";
	}
	//人社局审批
	@RequestMapping("rsjshowCompany")
	public String rsjshowCompany(@RequestParam("id")int id,@RequestParam("type")int type,Model model){
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		return "/wdit/bureauhuman/rsjshowCompany";
	}
	//人社局人员审批
	@RequestMapping("rsjShowUser")
	public String rsjShowUser(@RequestParam("requestUserId")int requestUserId,Model model){
		model.addAttribute("requestUserId", requestUserId);
		return "/wdit/bureauhuman/rsjShowUser";
	}
	//人社局人员审批
	@RequestMapping("getuser")
	@ResponseBody
	public String getuser(@RequestParam("level")int level,ServletRequest request, ServletResponse response){
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		String roles=(String) session.getAttribute("ROLES");
		System.out.println(roles);
		if(level==1){//房管局角色
			if(roles.equals("HOUSEMANAGE_ROLE,")){
				return "ok";
			}else{
				return "no";
			}
		}else if(level==2){//科委角色
			if(roles.equals("SCIASSIGN_ROLE,")){
				return "ok";
			}else{
				return "no";
			}
		}else if(level==3){//商委角色
			if(roles.equals("ASSIGN_ROLE,")){
				return "ok";
			}else{
				return "no";
			}
		}else if(level==6){//经委角色
			if(roles.equals("BUSASSIGN_ROLE,")){
				return "ok";
			}else{
				return "no";
			}
		}else if(level==4){//人社局角色
			if(roles.equals("BUREAUHUMAN_ROLE,")){
				return "ok";
			}else{
				return "no";
			}
		}else if(level==5){//人才办角色
			if(roles.equals("TALENTOFFICE_ROLE,")){
				return "ok";
			}else{
				return "no";
			}
		}else{
			return "";
		}
	}
}
