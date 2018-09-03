package com.bizduo.zflow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.domain.base.ProcessDto;
import com.bizduo.zflow.service.base.IJbpmService;
import com.bizduo.zflow.util.UserUtil;
//@Controller
//@RequestMapping(value = "/flows")
public class JbpmController {
//	@Autowired
	private IJbpmService jbpmService;
	// 流程图活动坐标
//	private ActivityCoordinates activityCoordinates;
	/**
	 * 初始化(取到所有的流程定义、已经启动的流程、历史流程和当前用户关联的流程)
	 * 
	 * @return
	 */
	@RequestMapping(value = "init", method = RequestMethod.GET)
    public ModelAndView init() {   
		ModelAndView mav = new ModelAndView("index1");
		String roleName = UserUtil.getUser().getUsername();
		
		mav.addObject("pds", jbpmService.getProcessDefinitions());
		mav.addObject("pis", jbpmService.getProcessInstances());
		mav.addObject("hpis", jbpmService.getHistoryProcessInstances());
		mav.addObject("tasks", jbpmService.getTasks(roleName));
		
		return mav;
    }

	/**
	 * 发布新流程
	 * 
	 * @return
	 */
	@RequestMapping(value = "deploy", method = RequestMethod.GET)
	public String deploy() {
		//String aa= jbpmService.deploy("leave");
		return "redirect:/flows/init";
	}

	/**
	 * 删除不需的以部署流程
	 * 
	 * @return
	 */
	@RequestMapping(value = "undeploy", method = RequestMethod.GET)
	public String undeploy(@RequestParam(value = "id" , required = true) String id) {
		jbpmService.undeploy(id);
		return "redirect:/flows/init";
	}

	/**
	 * 启动一个流程并关联到用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "start", method = RequestMethod.GET)
	public String start(@RequestParam(value = "id" , required = true) String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("owner", UserUtil.getUser().getUsername()); 
		jbpmService.start(id, map);
		return "redirect:/flows/init";
	}

	/**
	 * 申请请求
	 * 
	 * @return
	 */
	@RequestMapping(value = "asking", method = RequestMethod.GET)
	public ModelAndView asking(@RequestParam(value = "id" , required = true) String id) {
		return new ModelAndView("request").addObject("id", id);
	}

	/**
	 * 处理用户申请请求
	 * 
	 * @return
	 */
	@RequestMapping(value = "submit", method = RequestMethod.POST)
	public String submit(ProcessDto processDto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("owner", processDto.getOwner());
		map.put("day", Integer.parseInt(processDto.getDay()));
		map.put("reason", processDto.getReason());
		map.put("name", processDto.getOwner());
		jbpmService.complete(processDto.getTaskId(), map);
		return "redirect:/flows/init";
	}

	/**
	 * 经理审批请求
	 * 
	 * @return
	 */
	@RequestMapping(value = "manager", method = RequestMethod.GET)
	public ModelAndView manager(@RequestParam(value = "id" , required = true) String id) {
		ModelAndView mav = new ModelAndView("manager");
		mav.addObject("id", id);
		mav.addObject("map", jbpmService.manager(id));
		return mav;
	}

	/**
	 * 经理审批
	 * 
	 * @return
	 */
	@RequestMapping(value = "submitManager", method = RequestMethod.POST)
	public String submitManager(@RequestParam(value = "id" , required = true) String id,
								@RequestParam(value = "result" , required = true) String result) {
		jbpmService.completeByManager(id, result);
		return "redirect:/flows/init";
	}

	/**
	 * 老板请求
	 * 
	 * @return
	 */
	@RequestMapping(value = "boss", method = RequestMethod.GET)
	public ModelAndView boss(@RequestParam(value = "id" , required = true) String id) {
		ModelAndView mav = new ModelAndView("boss");
		mav.addObject("id", id);
		mav.addObject("map", jbpmService.boss(id));
		return mav;
	}

	/**
	 * 老板处理
	 * 
	 * @return
	 */
	@RequestMapping(value = "submitBoss", method = RequestMethod.POST)
	public String submitBoss(@RequestParam(value = "id" , required = true) String id) {
		jbpmService.completeByBoss(id);
		return "redirect:/flows/init";
	}

	/**
	 * 请求显示流程图片
	 * 
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(value = "id" , required = true) String id) {
		ModelAndView mav = new ModelAndView("view");
		mav.addObject("id", id);
		mav.addObject("activityCoordinates", jbpmService.findActivityCoordinates(id));
		return mav;
	}

	/**
	 * 获取流程图片
	 * 
	 * @return
	 */
	@RequestMapping(value = "pic", method = RequestMethod.GET)
	public ModelAndView pic(@RequestParam(value = "id" , required = true) String id, 
			HttpServletResponse response) {
		InputStream inputStream = jbpmService.findPicInputStream(id);
		PrintWriter pw = null;
		if (inputStream == null) {
			try {
				pw = response.getWriter();
				pw.write("error");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				pw.close();
			}
		} else {
			byte[] b = new byte[1024];
			int len = -1;
			ServletOutputStream sos = null;
			try {
				sos = response.getOutputStream();
				while ((len = inputStream.read(b, 0, 1024)) != -1) {
					sos.write(b, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (sos != null) {
					try {
						sos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return new ModelAndView("view");
	}
}
