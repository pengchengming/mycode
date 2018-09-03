package com.bizduo.zflow.controller.WDIT;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.UserUtil;

@Controller
public class LoginController extends BaseController {
	@Autowired
	private IUserService userService;	
	@RequestMapping(value = "/wditLogin", method = RequestMethod.GET)
	public String login(){
		/*String ua = request.getHeader("User-Agent");
		boolean isPhonePage= UserUtil.checkAgentIsMobile(ua);
		if(isPhonePage){
			return "/nprPhone/login";
		}else {
			return "/npr/login";
		}*/
		return "/wdit/login";
	}
	
	@RequestMapping(value = "/logoutPage", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest httpServletRequest){
		HttpServletRequest req = (HttpServletRequest)httpServletRequest;
		HttpSession session = req.getSession();
		
		User user = UserUtil.getUser();
		user = userService.getByName(user.getUsername());
		String password= user.getPassword();
		Integer companyId= user.getCompanyId();
		user = user.shadowClone(user);
		
		 if(null == session.getAttribute("MENUITEMS")){
			 return "/wdit/login";	 
		 }else {
			 if(companyId!=null){
					boolean  isSame=userService.checkPassword("Gzf@2017_XuHui",password);		
					if(isSame){
						return "/wdit/firstPage";
					}else {
						String email = user.getEmail();
						if(email == null || email.equals("")){
							return "/wdit/bindEmailPage" ;//绑定邮箱
						}
						return "/wdit/index";
					}
				}else {
					return "/wdit/index";
				}
		 }
		 
	}	
	
	
}
