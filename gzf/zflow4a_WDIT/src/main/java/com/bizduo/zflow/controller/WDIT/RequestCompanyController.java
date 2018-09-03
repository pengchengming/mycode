package com.bizduo.zflow.controller.WDIT;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.service.wdit.IRequestService;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping("/companyRequest")
public class RequestCompanyController {
	//申请列表
	@RequestMapping(value = "/requestList", produces = "text/html")
	public String requestList( Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/wdit/companyRequest/requestList";
	} 
	//申请须知
	@RequestMapping(value = "/requestRead", produces = "text/html")
	public String requestRead(@RequestParam(value = "requestId", required = false) Integer requestId, Model uiModel){
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("requestId",requestId);
		return "/wdit/companyRequest/requestRead";
	}
	//企业信息
	@RequestMapping(value = "/requestCompany", produces = "text/html")
	public String requestCompany(@RequestParam(value = "requestId", required = false) Integer requestId, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("requestId",requestId);
		return "/wdit/companyRequest/requestCompany";
	}
	//企业申请人员列表
	@RequestMapping(value = "/requestUserList", produces = "text/html")
	public String requestUserList(@RequestParam(value = "requestId", required = true) Integer requestId, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("requestId", requestId);
		return "/wdit/companyRequest/requestUserList";
	}
	//企业申请人员
	@RequestMapping(value = "/requestUser", produces = "text/html")
	public String requestUser(@RequestParam(value = "requestId", required = true) Integer requestId, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("requestId", requestId);
		return "/wdit/companyRequest/requestUser";
	}
	//企业申请人员
	@RequestMapping(value = "/requestUserUpdate", produces = "text/html")
	public String requestUserUpdate(@RequestParam(value = "requestId", required = true) Integer requestId, 
			@RequestParam(value = "requestUserId", required = true) Integer requestUserId,
			Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("requestId", requestId);
		uiModel.addAttribute("requestUserId", requestUserId);
		
		return "/wdit/companyRequest/requestUserUpdate";
	}
	//企业申请人员扩展
	@RequestMapping(value = "/requestUserExpand", produces = "text/html")
	public String requestUserExpand(Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/wdit/companyRequest/requestUserExpand";
	}
	//企业申请人员扩展
	@RequestMapping(value = "/requestConfirm", produces = "text/html")
	public String requestConfirm(@RequestParam(value = "requestId", required = true) Integer requestId,Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("requestId", requestId);
		return "/wdit/companyRequest/requestConfirm";
	}	
	
	public JSONObject  getJSONObject(String formCode,String currentField,String parentId,
			String aliasesName,String fieldName){
		JSONObject json=new JSONObject();
		json.put("formCode", formCode);
		json.put("currentField",currentField);
		json.put("parentId",parentId);
		json.put("aliasesName",aliasesName);
		json.put("fieldName",fieldName);
		return  json;
	}
	//, method = RequestMethod.POST
	
	
	@Autowired
	public IRequestService requestService;
	/***
	 * 获取外网申请数据  ，日期之后，状态是提交状态
	 * @param nowDate
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	/*@RequestMapping(value = "httpRequest")
	@ResponseBody
	public void httpRequest(@RequestParam(value = "nowDate", required = false) Long nowDate,
			HttpServletRequest request,HttpServletResponse response) {		
		try { 
			JSONObject results= requestService.getInfoBy(nowDate);
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(results.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	/***
	 * 
	 * @param nowDate
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "httpImageRequest")
	@ResponseBody
	public void httpImageRequest(@RequestParam(value = "nowDate", required = false) Long nowDate,
			HttpServletRequest request,HttpServletResponse response) {		
		try { 
			 ResourceBundle WB = ResourceBundle.getBundle("synchronizeConfig", Locale.getDefault());
			 String synImagePath= WB.getString("syn.synImagePath");
			 
			 JSONObject results= requestService.getImageRequest(synImagePath,nowDate);
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(results.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
