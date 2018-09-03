package com.bizduo.zflow.controller.uploaddownload;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bizduo.zflow.controller.BaseController;

@Controller
@RequestMapping("/userFile")
public class UserFileController extends BaseController{

	/**
	 * 上传文件
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public void upload(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String, Object> result = new HashMap<String, Object>();
		
//		String folder = "";
//		try {
//			folder = getFolder(request, response, result);
//		} catch (Exception ex) {
//			result.put("message", "获取folder失败");
//			return ajaxHtml(JsonUtil.getJsonString4JavaPOJO(result), response);
//		}
//		if (StringUtil.isEmpty(folder)) {// step-1 获得文件夹
//			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
//			if (!result.containsKey("message")) {
//				result.put("message", "处理失败");
//			}
//			return ajaxJson(JsonUtil.getJsonString4JavaPOJO(result), response);
//		}
		if (handler(request, response, result)) {
			result.put("status", "success");
			result.put("message", "上传成功");
			response.setContentType("text/plain;charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.getWriter().write("{\"status\" : \"success\", \"message\" : \"上传成功\"}");
            response.getWriter().flush();
		}
	}
	
	/**
	 * 处理文件上传
	 */
	public boolean handler(MultipartHttpServletRequest request, HttpServletResponse response, Map<String, Object> result) throws IOException{
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		MultipartFile file = request.getFile("file");
//		if (file == null) {// step-2 判断file
//			return getError("文件内容为空", HttpStatus.SC_UNPROCESSABLE_ENTITY, result, response, null);
//		}
		String orgFileName = file.getOriginalFilename();
		orgFileName = (orgFileName == null) ? "" : orgFileName;
		Pattern p = Pattern.compile("\\s|\t|\r|\n");
        Matcher m = p.matcher(orgFileName);
        orgFileName = m.replaceAll("_");
		String realFilePath = "E:/"  + File.separator + "admin" + File.separator;
		if(!(new File(realFilePath).exists())){
			new File(realFilePath).mkdirs();
		}
		String bigRealFilePath = realFilePath  + File.separator + FilenameUtils.getBaseName(orgFileName).concat(".") + fileName.concat(".").concat(FilenameUtils.getExtension(orgFileName).toLowerCase());
		if (file.getSize() > 0) {
			File targetFile = new File(bigRealFilePath);
			file.transferTo(targetFile);//写入目标文件
		}


		return true;
	}
	
//	boolean getError(String message, HttpStatus status, Map<String, Object> result, HttpServletResponse response, Exception ex) {
//		response.setStatus(status.value());
//		result.put("message", message);
//		LOG.warn(message, ex);
//		return false;
//	}
	
	/**文件下载**/
//    @RequestMapping("download")
//    public String download(HttpServletRequest request, HttpServletResponse response) {
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	try {
//    		String fileId = request.getParameter("fileId");
//    		if(StringUtil.isEmpty(fileId)){
//    			map.put("status", "error");
//    			map.put("message", "下载错误");
//    			return ajaxJson(JsonUtil.getJsonString4JavaPOJO(map), response);
//    		}
//        	map.put("file_id", fileId);
//        	List<UserFileDTO> list = userFileService.find(map);
//        	UserFileDTO file = list.get(0);
//			FileOperateUtil.download(request, response, "application/octet-stream; charset=utf-8", file.getFilePath(), file.getFileName());
//			return null;
//		} catch (IOException e) {
//			logger.error("文件下载出错");
//			map.put("status", "error");
//			map.put("message", "下载错误");
//		}
//        return ajaxJson(JsonUtil.getJsonString4JavaPOJO(map), response);
//    }

    /**获取文件大小**/
//    @RequestMapping(value = "/getfilesize")
//	@ResponseBody
//	public String getFileSize(HttpServletRequest request) {
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	String fileId = request.getParameter("fileId");
//    	map.put("file_id", fileId);
//    	List<UserFileDTO> list = userFileService.find(map);
//    	if(list.size() != 0){
//    		UserFileDTO file = list.get(0);
//        	Long fileLength = new File(file.getFilePath()).length();
//        	return fileLength.toString();
//    	}
//    	return (new Long(0L)).toString();
//	}
}