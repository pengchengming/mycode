package cn.pcm.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.pcm.utils.SendMail;

@Controller
public class SendMailController {

	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public void SendMail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String address = request.getParameter("address");
   
		if (SendMail.sendEmail(address, "hi", "我是彭成铭同学...")) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}

	@RequestMapping(value = "/testAjax/{id}", method = RequestMethod.POST)
	public String testAjax(@PathVariable("id") Integer id) {

		System.out.println(id);
		return "hello" + id;
	}

}
