package cn.pcm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("/{page}")
	public String ShowPage(@PathVariable String page, HttpServletRequest request) {
		String pages[] = { "kindEditor", "upload" ,"info"};
		for (String str : pages) {
			if (page.equals(str)) {
				HttpSession session = request.getSession();
				if (session.getAttribute("user") != null) {
					return "/WEB-INF/jsp/" + page;
				} else {
					request.setAttribute("loginMsg", "请先登录");
					return "/login";
				}

			}
		}
		return page;
	}
}
