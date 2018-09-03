package cn.pcm.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.mina.filter.reqres.Request;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.pcm.domain.ArticleContent;
import cn.pcm.domain.ArticleDetail;
import cn.pcm.domain.Replay;
import cn.pcm.domain.User;
import cn.pcm.service.ArticleService;
import cn.pcm.service.ReplayService;
import cn.pcm.utils.SendMail;

@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	@Qualifier("articleService")
	private ArticleService articleService;

	@Autowired
	@Qualifier("replayService")
	private ReplayService replayService;

	@RequestMapping(value = "/addArticle", method = RequestMethod.POST)
	public void addArticle(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ArticleDetail detail = new ArticleDetail();
		HttpSession session = request.getSession();
		ArticleContent content = new ArticleContent();
		String title = request.getParameter("title");
		String style = request.getParameter("style");
		String content1 = request.getParameter("content");
		String author = "佚名";
		User u = (User) session.getAttribute("user");
		author = u.getUserName();
		detail.setTitle(title);
		detail.setDatetime(new Timestamp(System.currentTimeMillis()));
		detail.setClassify(style);
		detail.setAuthor(author);
		content.setContent(Hibernate.createBlob(content1.getBytes()));
		// content.setArticleDeatil(detail);
		detail.setStatus(1);
		articleService.addArticle(detail, content);
		String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		response.sendRedirect(basePath+"/article/getArticles?page=1");
	}

	@RequestMapping(value = "/getArticles")
	public ModelAndView getArticles(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		String page = req.getParameter("page");
		int count = articleService.getArticleCount();
		int start = Integer.parseInt(page) * 12 - 12;

		ModelAndView mv = new ModelAndView();
		mv.setViewName("show");
		List<ArticleDetail> list = articleService.getArticles(start, 12);
		mv.addObject("showArticle", list);
		mv.addObject("count", count);
		return mv;
	}
	
	@RequestMapping(value = "/delArticle")
	public void delArticle(HttpServletRequest req,
			HttpServletResponse resp)  {
		int id = Integer.parseInt(req.getParameter("id"));
		articleService.delArticle(id);
		 User user=(User) req.getSession().getAttribute("user");
	        List<ArticleDetail> list =articleService.getArticleByUSer(user);
	        req.getSession().setAttribute("userArticle", list);
	}
	
//	@RequestMapping(value = "/userArticles",method=RequestMethod.GET)
//	@ResponseBody
//	public List<ArticleDetail> getUSerArticles(HttpServletRequest req,
//			HttpServletResponse resp) {
//        User user=(User) req.getSession().getAttribute("user");
//        List<ArticleDetail> list =articleService.getArticleByUSer(user);
//        return list;
//	}
	@RequestMapping(value = "/userArticles",method=RequestMethod.GET)
	public void getUSerArticles(HttpServletRequest req,
			HttpServletResponse resp) {
        User user=(User) req.getSession().getAttribute("user");
        List<ArticleDetail> list =articleService.getArticleByUSer(user);
        req.getSession().setAttribute("userArticle", list);
	}

	@RequestMapping(value = "/{id}")
	public String getContent(@PathVariable("id") int id,
			HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");

		ArticleDetail detail = articleService.getArticle(id);
		if (detail != null) {
			ArticleContent content = articleService.getContent(id);
			List<Replay> replay = replayService.getReplay(id);
			request.setAttribute("content", content);
			request.setAttribute("detail", detail);
			request.setAttribute("replay", replay);
		}
		return "content";
	}
}