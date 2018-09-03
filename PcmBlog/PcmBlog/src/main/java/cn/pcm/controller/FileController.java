package cn.pcm.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import cn.pcm.domain.Resource;
import cn.pcm.domain.User;
import cn.pcm.service.FileService;
import cn.pcm.utils.UploadUtils;

/*
 * //获取上传位置
 *String path = req.getRealPath("/attached/upload");
 *System.out.println(path);
 *D:\\develop\\newWorkspace2\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp25\\wtpwebapps\PcmBlog\attached\\upload
*-----------------------------------------------------
*System.out.println(req.getContextPath() + "/upload/"); 
* /PcmBlog/upload/

 * 
 */

@Controller
public class FileController {

	@Autowired
	@Qualifier("fileService")
	private FileService fileService;

	@RequestMapping(value = "/file/download")
	public void download(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Resource resource = fileService.getResource(id);
		String fileName = resource.getUuidName();
		String serverFilePath = request.getRealPath("/attached/upload") + "//"
				+ fileName;

		// 等级于 response.setHeader("Content-Type",xxx);
		response.setHeader("Content-Disposition", "attachment; filename="
				+ java.net.URLEncoder.encode(resource.getRealName(), "UTF-8"));
		InputStream in = new BufferedInputStream(new FileInputStream(
				serverFilePath));
		// 需要浏览器输出流
		OutputStream out = response.getOutputStream();
		int temp;
		while ((temp = in.read()) != -1) {
			out.write(temp);
		}
		out.close();
		in.close();
	}

	@RequestMapping("/file/batch")
	public String batch(@RequestParam("file") CommonsMultipartFile file[],
			@RequestParam("description") String desciption[],
			HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		String author = "佚名";
		HttpSession session = req.getSession();
		User u = (User) session.getAttribute("user");
		author = u.getUserName();
		for (int i = 0; i < file.length; i++) {
			Resource resource = new Resource();
			String filename = file[i].getOriginalFilename();
			// 判断文件是否有路径，如果存在路径将路径切掉
			filename = UploadUtils.subFileName(filename);
			resource.setRealName(filename);

			// 生成唯一文件名 uuidname
			String uuidname = UploadUtils.generateUUIDName(filename);
			resource.setUuidName(uuidname);
			File dir = new File(req.getRealPath("/attached/upload"));
			try {
				InputStream is = file[i].getInputStream();
				OutputStream os = new FileOutputStream(new File(dir, uuidname));
				int len = 0;
				byte[] buffer = new byte[400];
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
				os.close();
				is.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resource.setUploadTime( new Timestamp(System.currentTimeMillis()));
			resource.setAuthor(author);
			resource.setDescription(desciption[i]);
			resource.setStatus(1);
			fileService.addResource(resource);
		}
		return "/WEB-INF/jsp/uploadSuccess";
	}

	@RequestMapping(value = "/file/getResources")
	public ModelAndView getArticles(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		String page = req.getParameter("page");
		int count = fileService.getResourceCount();
		int start = Integer.parseInt(page) * 12 - 12;

		ModelAndView mv = new ModelAndView();
		mv.setViewName("showFile");
		List<Resource> list = fileService.getResource(start, 12);
		mv.addObject("showResource", list);
		mv.addObject("count", count);
		return mv;
	}

}
