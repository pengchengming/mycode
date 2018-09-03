package com.bizduo.zflow.controller.uploaddownload;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bizduo.zflow.util.ccm.uploadFile.UploadFileUtil;
@Controller
public class UploadFileController {
	@RequestMapping("/inputUploadFile")
	public String approvelist(){
		return "/wdit/input/inputUploadFile";
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createOrUpdate(@RequestParam("file") MultipartFile file, 
    		@RequestParam("filePath") String  filePath,
    		HttpServletRequest request, 
    		HttpServletResponse response) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>(); 
		try {
			UploadFileUtil.copyFile(file.getInputStream(),filePath);
			map.put("code", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0"); 
		}
		return map;
	}
	
}
