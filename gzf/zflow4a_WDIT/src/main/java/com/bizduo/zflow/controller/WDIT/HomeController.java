package com.bizduo.zflow.controller.WDIT;

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
public class HomeController {
	@Autowired
	private IUserService userService;	 
	//首页
	@RequestMapping(value = "/wditIndex", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		User user = UserUtil.getUser();
		user = userService.getByName(user.getUsername());
		String password= user.getPassword();
		Integer companyId= user.getCompanyId();
		user = user.shadowClone(user);
		ModelAndView mav = new ModelAndView();
		if(companyId!=null){
			boolean  isSame=userService.checkPassword("Gzf@2017_XuHui",password);		
			if(isSame){
				mav.setViewName("/wdit/firstPage");
			}else {
				String email = user.getEmail();
				if(email == null || email.equals("")){
					mav.setViewName("/wdit/bindEmailPage");//绑定邮箱
				}else{
					mav.setViewName("/wdit/index");	
				}
			}
			mav.addObject("isSame", isSame);
		}else {
			mav.setViewName("/wdit/index");
		}
		mav.addObject("user", user);
		return mav;		 
	}
	      
  
}
