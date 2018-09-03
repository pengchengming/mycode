package cn.pcm.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.pcm.utils.FaceUtil;
import cn.pcm.utils.UploadUtils;

@Controller
public class FaceController {

	@RequestMapping("/getFace")
	public void batch(@RequestParam("file") CommonsMultipartFile file,
			HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		String jsonObject = FaceUtil.face(file.getBytes());
		System.out.println(jsonObject);
		response.getWriter().write(jsonObject);

	}
}
