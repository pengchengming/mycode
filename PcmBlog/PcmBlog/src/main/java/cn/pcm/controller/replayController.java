package cn.pcm.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.pcm.domain.Replay;
import cn.pcm.domain.User;
import cn.pcm.serializable.user;
import cn.pcm.service.ReplayService;


@Controller
public class replayController {
	@Autowired
	@Qualifier("replayService")
	private ReplayService replayService;
	
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public void addReplay(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String comment = request.getParameter("comment");
		HttpSession session=request.getSession();
		User u=(User) session.getAttribute("user");
		int replayId = Integer.parseInt(request.getParameter("replayId"));
		Replay replay=new Replay();
		replay.setDatetime(new Timestamp(System.currentTimeMillis()));
		replay.setReplayid(replayId);
		replay.setUserName(u.getUserName());
		replay.setWords(comment);
		replay.setStatus(1);
		replayService.addReplay(replay);
		response.getWriter().write("success");
	}
}
