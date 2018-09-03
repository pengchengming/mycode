package cn.pcm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.sshd.common.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pcm.domain.User;
import cn.pcm.service.ArticleService;
import cn.pcm.service.UserService;
import cn.pcm.utils.MD5Util;
import cn.pcm.utils.SendMail;

@Controller
public class UserController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUser(User user, HttpServletRequest request) {
		String password = MD5Util.string2MD5(user.getPassword());
		user.setPassword(password);
		user.setType("user");
		userService.addUser(user);
		SendMail.sendEmail(user.getEmail(), "你好啊....", "我是彭成铭同学啊....");
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		return "redirect:/index.jsp";
	}

	@RequestMapping(value = "/updataEmail", method = RequestMethod.POST)
	public void updataEmail (HttpServletRequest request,HttpServletResponse response) throws IOException {
		String email =request.getParameter("email");
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		user.setEmail(email);
		userService.updateUserEmail(user);
		response.getWriter().write("success");
	}
	
	@RequestMapping(value = "/updataPassword", method = RequestMethod.POST)
	public void updataPassword( HttpServletRequest request,HttpServletResponse response) throws IOException {
		String password = MD5Util.string2MD5(request.getParameter("password"));
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		user.setPassword(password);
		userService.updateUserPassword(user);
		response.getWriter().write("success");
	}
	
	@RequestMapping(value = "/getUser", method = RequestMethod.POST)
	public String getUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");
		String password = MD5Util.string2MD5( request.getParameter("password"));
		User user = userService.getUser(userName, password);	
		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			return "/index";
		} else {
			request.setAttribute("msg", "用户名或密码错误");
			return "login";
		}
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public void checktUser(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");
		User user = userService.getUser(userName);
		if (user != null) {
			response.getWriter().write("no");
		} else {
			response.getWriter().write("yes");
		}
	}
}