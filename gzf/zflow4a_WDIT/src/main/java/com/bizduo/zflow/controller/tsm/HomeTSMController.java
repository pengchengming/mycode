package com.bizduo.zflow.controller.tsm;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.UserUtil;

@Controller
public class HomeTSMController {
	@Autowired
	private IUserService userService;
	//首页
	@RequestMapping(value = "/tsmIndex", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = UserUtil.getUser();
		user = userService.getByName(user.getUsername());
		user = user.shadowClone(user);
		
		ModelAndView mav = new ModelAndView();
		/*String ua = request.getHeader("User-Agent");
 		boolean isPhonePage= UserUtil.checkAgentIsMobile(ua); 
         if(isPhonePage)
        	 mav.setViewName("/decoPhone/index");
         else */
        	 mav.setViewName("/tsm/index");
		mav.addObject("user", user);
		return mav;		 
	}
 
}
