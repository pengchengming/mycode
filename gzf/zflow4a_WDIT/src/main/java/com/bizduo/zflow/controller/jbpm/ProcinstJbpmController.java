package com.bizduo.zflow.controller.jbpm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.domain.dm.MailMessage;
import com.bizduo.zflow.domain.sys.Employee;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.domain.tableData.DataTableToPage;
import com.bizduo.zflow.service.base.IJbpmService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.dm.IMailMessageService;
import com.bizduo.zflow.service.jbpm.IProcinstJbpmService;
import com.bizduo.zflow.service.sys.IEmployeeService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.Configure;
import com.bizduo.zflow.util.UserUtil;
import com.bizduo.zflow.util.ccm.MailStatus;


@Controller
@RequestMapping(value = "/procinstJbpm")
public class ProcinstJbpmController {
	@Autowired
	private IProcinstJbpmService procinstJbpmService;
	@Autowired
	private IJbpmService jbpmService;
	@Autowired
	private IFormService formService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMailMessageService mailMessageService;
	/*
	@Autowired
	private IRankService rankService;
*/
	@Autowired
	private IEmployeeService employeeService;
	
 
	//////////ccm////////////// 
	//首页
	@RequestMapping(value = "ccmhome", method = RequestMethod.GET)
    public void initCCM(HttpServletRequest request, 
    		HttpServletResponse response) throws IOException {   
//		initIdentityService();
		User user=(User)request.getSession().getAttribute("USER");
		
		User currentUser = (User) request.getSession().getAttribute("USER");
		Employee currentemployee = employeeService.getByUserame(currentUser
				.getUsername());
		
		request.getSession().setAttribute("currentemployee", currentemployee);
		
		
//		User user= UserUtil.getUser();
		user = userService.getByName(user.getUsername());
		if(user==null){
			//ModelAndView mav = new ModelAndView("login");
			response.sendRedirect(Configure.getConfigure("root")+"/login");
			//return mav;
		}
		
		response.sendRedirect(Configure.getConfigure("root")+user.getIndexUrl());
		
//		User Userprocessrole = userService.findByUserId(UserUtil.getUser().getId());
//		String processrole = Userprocessrole.getProcessrole();
//		Long nowDateTime=new Date().getTime();
//		if("admin".equalsIgnoreCase(user.getProcessrole())){
//			//"/employees/template"
//			
//			//return mav;
//		}
//		else
//			response.sendRedirect(Configure.getConfigure("root")+"/pan/index");
		//return mav;
//		if(processrole.equals("admin")){ 
//		}else if(processrole.equals("ppGroup")){
//			ModelAndView mav = new ModelAndView("ccm/ccmhome");
//			mav.addObject("pds", jbpmService.getProcessDefinitions());
//			mav.addObject("pis", jbpmService.getProcessInstances());
//			mav.addObject("hpis", jbpmService.getHistoryProcessInstances());
//			mav.addObject("tasks", jbpmService.getTasks(Userprocessrole.getUsername()));
//			mav.addObject("nowDateTime",nowDateTime);
//			return mav;
//		}else if(processrole.equals("taLeader")){ 
//			ModelAndView mav = new ModelAndView("ccm/ccmHomeTaLeader");
//			mav.addObject("userId",UserUtil.getUser().getId());
//			mav.addObject("nowDateTime",nowDateTime);
//			List<Task> taskList= jbpmService.getTasks(Userprocessrole.getUsername());
//			String taskIds="";
//			for (Task task : taskList) {
//				taskIds+=task.getId()+",";
//			}
//			if(taskIds!=null&&!taskIds.trim().equals(""))
//				taskIds=taskIds.substring(0, taskIds.length()-1);
//			mav.addObject("taskIds",taskIds ); 
//			return mav;
//		}else if(processrole.equals("DataInput")){ 
//			ModelAndView mav = new ModelAndView("ccm/pipeline/pipelineList");
//			mav.addObject("nowDateTime",nowDateTime);
//			return mav;
//		}else{
//			 processrole ="owler";
//			ModelAndView mav = new ModelAndView("ccm/ccmHomeOrdinary");
//			mav.addObject("nowDateTime",nowDateTime);
//			mav.addObject("userId",UserUtil.getUser().getId());
//			return mav;
//		}  
    }
	//跳转填单
	@RequestMapping(value = "ccmdeployStart", method = RequestMethod.GET)
	public ModelAndView ccmdeployStart(@RequestParam(value = "zflowCode" , required = true) String zflowCode) throws JSONException{
		ModelAndView mav = new ModelAndView("ccm/ccmFormHtml");
		 
		if(zflowCode!=null){
			String sql="select id from zflow_form where formName='"+zflowCode +"'"; 
			List<String> zformlist=new ArrayList<String>();
			zformlist.add("id");
			JSONArray jsonArray = procinstJbpmService.getJbpmDeployment(sql,zformlist);
			if(jsonArray.length() > 0) {
				JSONObject tempObj = jsonArray.getJSONObject(0);
				String zformId = (String)tempObj.get("id");
				mav.addObject("zformId", zformId);
			}
		}
		
		return mav;
	}
	//填单
	@RequestMapping(value = "/ccmasking.do", method = RequestMethod.POST)
	@ResponseBody
	public  Map<String, Object> ccmasking(@RequestParam(value = "jsonString", required = true) String jsonString,
			@RequestParam(value = "isSubmit", required = true) String isSubmit) throws JSONException{

		Map<String, Object> map = new HashMap<String, Object>();
		User createUser= UserUtil.getUser();
		if(createUser==null||createUser.getId()==null)
		{
			map.put("code", "0");
			map.put("errorMsg", "用户已失效，请重新登录"); 
			return map;
		}
		try {
			JSONObject jsonObj = new JSONObject(jsonString); 
			//JSONObject tempJsonObj = jsonObj.getJSONObject("register");
			 
			Long formId = jsonObj.getLong("formId");
			if(null == formId || 0L == formId){
				map.put("code", "0");
				map.put("errorMsg", "表单标识未传入"); 
				return map;
			}else{ 

				String status="";
				if(jsonObj.has("tableDataId")){
					String tableDataId = jsonObj.get("tableDataId").toString();
					String tasksql="select status from dbo.clientcall  where id ='"+tableDataId +"' ";  //DBID_=
					List<String> list1=new ArrayList<String>(); 
					list1.add("status"); 
					JSONArray taskjsonArray = procinstJbpmService.getJbpmDeployment(tasksql,list1);
					if(taskjsonArray.length() > 0) {
						JSONObject tempObj = taskjsonArray.getJSONObject(0); 
						status  = (String)tempObj.get("status"); 
					}
				}
				
				
				if(isSubmit!=null&&isSubmit.trim().equals("0")){
					//保存表单  
					if(jsonObj.has("tableDataId")){
						String tableDataId = jsonObj.get("tableDataId").toString();
						//修改
						if(tableDataId!=null&&!tableDataId.trim().equals("")){ 
							this.formService.updateFormData(formId, jsonObj);
							map.put("successMsg", "保存成功");
						}
					}else{ 
						Long dataId= this.formService.saveFormData(formId, jsonObj); 
						this.formService.updateFormStatus("clientCall",dataId,"c1","clientStatus","6",createUser);
						if(dataId!=null&&dataId!=0l){
							map.put("successMsg", "保存成功");  
						}  else{
							map.put("errorMsg", "保存错误"); 
						} 
					}
					
				}else {
					//isSubmit 0TALeader保存 1TALeader提交 2 pipeline
					boolean isSave=false;
					if((status!=null&&status.trim().equals("1"))){
						this.formService.updateFormData(formId, jsonObj);
						map.put("successMsg", "保存成功");
					}else {
						isSave=true;
					}
					if(isSave||isSubmit.trim().equals("2")){

						Map<String, Object> map1 = new HashMap<String, Object>();
						map1.put("owner", UserUtil.getUser().getUsername()); 
						//ProcessInstance procinst = 
								jbpmService.startProcessByKey("clientCall", map1);
						
						//List<Task> tasklist = jbpmService.getProcessInstanceTaskList(procinst);
						String taskId = jbpmService.findPersonalTasks(UserUtil.getUser().getUsername()); 
						Long dataId= this.formService.saveFormData(formId, jsonObj); 
						saveTaskData(formId, "clientCall", dataId, taskId);
						//保存表单 
//						Form form= this.formService.findFormById(formId);
//						this.formService.updateFormDataTask( formId,form.getFormCode(),dataId,taskId, participationId);

						//this.formService.updateFormDataTask( formId,"clientCall",dataId,taskId, "");
						//status  1.填单，2。TA Leader审批，3 p/p 认领 , 4. p/p 反馈 ,5 end1 
						
						this.formService.updateFormStatus("clientCall",dataId,"c1","clientStatus","1",createUser);
						
						if(dataId!=null&&dataId!=0l){
							List<User> toUserList= this.userService.findByProcessrole("taLeader");
							//String taskId21="";
							//String zform = null;
							for(User user : toUserList){
								MailMessage mailMessage=new MailMessage(user, "TA Leader 审批", "请 TA Leader 审批", dataId ,Long.parseLong(taskId), 0,1,"clientCall", createUser,"taLeader",MailStatus.FILLSTATUS);
								mailMessage =this.mailMessageService.saveMailMessage(mailMessage); 
								//this.mailMessageService.updateResendMail(mailMessage);
							} 
							map.put("successMsg", "保存成功");  
						}  else{
							map.put("errorMsg", "保存错误"); 
						}
					
					}
					
				
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "保存错误"); 
		} 
		return map;
	}
	 
	/**
	 * 
	 * @param formId
	 * @param tableName
	 * @param dataId
	 * @param taskId
	 * @param participationId
	 */
	private void saveTaskData(Long formId,String tableName,Long dataId,String taskId){ 

		Map<String, Object> map1 = new HashMap<String, Object>();
		String username = UserUtil.getUser().getUsername();
		map1.put("owner", username);
		Date date = new Date();
        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyyMMdd");//可以方便地修改日期格式     
        String  dateStr  = dateFormat.format(date);
		map1.put("day", Integer.parseInt(dateStr));
		map1.put("dataId", dataId);
		map1.put("tableName", tableName);
		map1.put("formId", formId);
		map1.put("name", username);
		jbpmService.complete(taskId, map1); 
	}
	
	 
	

	///显示数据列表  
	@RequestMapping(value = "/ccmtableDataJson.do")
	@ResponseBody
	public DataTableToPage formTableData(
			@RequestParam(value = "tableName", required = true) String tableName,
			@RequestParam(value = "parameter", required = true) String parameter,
			@RequestParam(value = "pageIndex", required = true) int pageIndex 
			){

		Map<String,String> fieldMap=new HashMap<String, String>();
		fieldMap.put("id", "id");
		fieldMap.put("createDate", "createDate");
		fieldMap.put("name", "name");
		fieldMap.put("company", "company");
		fieldMap.put("department", "department");
		fieldMap.put("competenceCenter", "competenceCenter");
		fieldMap.put("revenue", "revenue");
		fieldMap.put("phone1", "phone1");
		fieldMap.put("phone2", "phone2");
		fieldMap.put("Topics", "Topics");
		fieldMap.put("specialRFC", "specialRFC");
		fieldMap.put("note", "note");
		fieldMap.put("statusName", "statusName");
		fieldMap.put("status", "status"); 
		fieldMap.put("infosource", "infosource");  
		fieldMap.put("office", "office");
		fieldMap.put("infosourceOther", "infosourceOther");  
		fieldMap.put("email", "email");    
		
		String sql="select id,createDate,name,company,department,competenceCenter,revenue,phone1,email," +
				" phone2, Topics,specialRFC,note,status,statusName,infosource ,office,infosourceOther from "+tableName +"  "+parameter;
		sql+="  order by id desc";
		Map<String,Integer> pageMap=new HashMap<String, Integer>();
		pageMap.put("pageIndex", pageIndex);
		pageMap.put("pageSize", 10); 
		return null;// clientCallService.getClientCallDatas(fieldMap,sql,pageMap);  
	}
	////外部调用
	@RequestMapping(value = "getTaskById", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTaskById(@RequestParam(value = "id", required = false) String id ) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("id", id);
		Set<String> strSet = new HashSet<String>();
		strSet.add("owner");
		strSet.add("day");
		strSet.add("dataId");
		strSet.add("tableName");
		strSet.add("formId");
		strSet.add("name"); 
		Map<String, Object> variables= jbpmService.getVariables(id, strSet); 
		map.put("map",variables ); 
		return map;
	}

	
	////以下方法已废除
	@RequestMapping(value = " operating", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> operating(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "resultValue", required = false) String resultValue) throws Exception{
		
		
		Map<String, Object> map = new HashMap<String, Object>(); 
		jbpmService.completeByManager(id, resultValue);
		map.put("code", "0");
		map.put("msg", "成功");
		return map;
	}
	
	
	// ppGroup 认领 
	@RequestMapping(value = "ppGroup", method = RequestMethod.GET)
	public ModelAndView ppGroup(@RequestParam(value = "id" , required = true) String id) {
		ModelAndView mav = new ModelAndView("ccm/ppGroupForm");
		mav.addObject("id", id);
		Set<String> strSet = new HashSet<String>();
		strSet.add("owner");
		strSet.add("day");
		strSet.add("dataId");
		strSet.add("tableName");
		strSet.add("formId");
		strSet.add("name"); 
		Map<String, Object> variables= jbpmService.getVariables(id, strSet);
		mav.addObject("map",variables );
		return mav;
	} 
	// ppGroup  反馈
	@RequestMapping(value = "ppFeedback", method = RequestMethod.GET)
	public ModelAndView ppFeedback(@RequestParam(value = "id" , required = true) String id) {
		ModelAndView mav = new ModelAndView("ccm/ppFeedbackForm");
		mav.addObject("id", id);
		Set<String> strSet = new HashSet<String>();
		strSet.add("owner");
		strSet.add("day");
		strSet.add("dataId");
		strSet.add("tableName");
		strSet.add("formId");
		strSet.add("name"); 
		Map<String, Object> variables= jbpmService.getVariables(id, strSet);
		mav.addObject("map",variables );
		return mav;
	}
	//////////ccm//////////////	
	
	
	@RequestMapping(value = "indexjbpmhome", method = RequestMethod.GET)
    public ModelAndView init() {   
		ModelAndView mav = new ModelAndView("indexjbpmhome");
		String userName = UserUtil.getUser().getUsername();
		mav.addObject("pds", jbpmService.getProcessDefinitions());
		mav.addObject("pis", jbpmService.getProcessInstances());
		mav.addObject("hpis", jbpmService.getHistoryProcessInstances());
		mav.addObject("tasks", jbpmService.getTasks(userName));
		return mav;
    }
	@RequestMapping(value = "deployStart", method = RequestMethod.GET)
	public String  deployStart()throws JSONException{
		//创建流程实例
		String deploymentId= jbpmService.deploy("leave","leave");
		
		String sql="select STRINGVAL_ from jbpm4_deployprop where DEPLOYMENT_="+deploymentId +" and KEY_='pdid'"; 
		List<String> list=new ArrayList<String>();
		list.add("STRINGVAL_");
		
		JSONArray jsonArray = procinstJbpmService.getJbpmDeployment(sql,list);
		if(jsonArray.length() > 0) {
			JSONObject tempObj = jsonArray.getJSONObject(0);
			String stringval = (String)tempObj.get("STRINGVAL_");
			//启动流程
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("owner", UserUtil.getUser().getUsername()); 
			jbpmService.start(stringval , map);
		}
		return "redirect:/procinstJbpm/indexjbpmhome";
	}
	@RequestMapping(value = "ccmDeployJBPMStart", method = RequestMethod.GET)
	public String  ccmDeployJBPMStart()throws JSONException{
		//创建流程实例
		String deploymentId= jbpmService.deploy("ccm","ccm");
		
		String sql="select STRINGVAL_ from jbpm4_deployprop where DEPLOYMENT_="+deploymentId +" and KEY_='pdid'"; 
		List<String> list=new ArrayList<String>();
		list.add("STRINGVAL_");
		
		JSONArray jsonArray = procinstJbpmService.getJbpmDeployment(sql,list);
		if(jsonArray.length() > 0) {
			JSONObject tempObj = jsonArray.getJSONObject(0);
			String stringval = (String)tempObj.get("STRINGVAL_");
			//启动流程
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("owner", UserUtil.getUser().getUsername()); 
			jbpmService.start(stringval , map);
		}
		return "redirect:/procinstJbpm/indexjbpmhome";
	}	
	@RequestMapping(value = "loanDeployStart", method = RequestMethod.GET)
	public String  loanDeployStart()throws JSONException{
		//创建流程实例
		String deploymentId= jbpmService.deploy("leave","loan");
		
		String sql="select STRINGVAL_ from jbpm4_deployprop where DEPLOYMENT_="+deploymentId +" and KEY_='pdid'"; 
		List<String> list=new ArrayList<String>();
		list.add("STRINGVAL_");
		
		JSONArray jsonArray = procinstJbpmService.getJbpmDeployment(sql,list);
		if(jsonArray.length() > 0) {
			JSONObject tempObj = jsonArray.getJSONObject(0);
			String stringval = (String)tempObj.get("STRINGVAL_");
			//启动流程
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("owner", UserUtil.getUser().getUsername()); 
			jbpmService.start(stringval , map);
		}
		return "redirect:/procinstJbpm/indexjbpmhome";
	}
	
	 
	/**
	 * 填写事由
	 * @return
	 */
	@RequestMapping(value = "asking", method = RequestMethod.GET)
	public ModelAndView asking(@RequestParam(value = "id" , required = true) String taskId) throws JSONException{
		ModelAndView mav = new ModelAndView("zform/formHtml");
		mav.addObject("taskId", taskId);
		//jbpm4_task
		String tasksql="select  zform, EXECUTION_ID_  from  jbpm4_task  where DBID_='"+taskId +"'";
		List<String> list=new ArrayList<String>();
		list.add("zform");
		list.add("EXECUTION_ID_");
		JSONArray taskjsonArray = procinstJbpmService.getJbpmDeployment(tasksql,list);
		String zform = null;
		String execution=null;
		if(taskjsonArray.length() > 0) {
			JSONObject tempObj = taskjsonArray.getJSONObject(0);
			zform = (String)tempObj.get("zform");
			execution  = (String)tempObj.get("EXECUTION_ID_");
		}
		if(execution!=null){
			String[] executionStrs= execution.split("\\.");
			if(executionStrs.length>1){
				String executionId=executionStrs[1];
				String sql="select PROCDEFID_ from jbpm4_execution where DBID_='"+executionId +"'"; 
				List<String> zformlist=new ArrayList<String>();
				zformlist.add("PROCDEFID_");
				JSONArray jsonArray = procinstJbpmService.getJbpmDeployment(sql,zformlist);
				if(jsonArray.length() > 0) {
					JSONObject tempObj = jsonArray.getJSONObject(0);
					String procdefId = (String)tempObj.get("PROCDEFID_");
					mav.addObject("participationId", procdefId);
				}
			}
		}
		//zflow_form
		if(zform!=null){
			String sql="select id from zflow_form where formName='"+zform +"'"; 
			List<String> zformlist=new ArrayList<String>();
			zformlist.add("id");
			JSONArray jsonArray = procinstJbpmService.getJbpmDeployment(sql,zformlist);
			if(jsonArray.length() > 0) {
				JSONObject tempObj = jsonArray.getJSONObject(0);
				String zformId = (String)tempObj.get("id");
				mav.addObject("zformId", zformId);
			}
		}
		
		
		return mav;
	}
	
	@RequestMapping(value = "formhtml", method = RequestMethod.GET)
	public ModelAndView formhtml(@RequestParam(value = "formName" , required = true) String formName) throws JSONException{
		ModelAndView mav = new ModelAndView("zform/formHtml");
		String sql="select id from zflow_form where formName='"+formName +"'"; 
		List<String> zformlist=new ArrayList<String>();
		zformlist.add("id");
		JSONArray jsonArray = procinstJbpmService.getJbpmDeployment(sql,zformlist);
		if(jsonArray.length() > 0) {
			JSONObject tempObj = jsonArray.getJSONObject(0);
			String zformId = (String)tempObj.get("id");
			mav.addObject("zformId", zformId);
		}
		return mav;
	}
	
	/**
	 * 经理审批请求 
	 * @return
	 */
	@RequestMapping(value = "manager", method = RequestMethod.GET)
	public ModelAndView manager(@RequestParam(value = "id" , required = true) String id) {
		ModelAndView mav = new ModelAndView("zform/managerformTableView");
		mav.addObject("id", id);
		Set<String> strSet = new HashSet<String>();
		strSet.add("owner");
		strSet.add("day");
		strSet.add("dataId");
		strSet.add("tableName");
		strSet.add("formId");
		strSet.add("name");
//		
		Map<String, Object> variables= jbpmService.getVariables(id, strSet);
		mav.addObject("map",variables );
		return mav;
	}
	/**
	 * 
	 */
	@RequestMapping(value = "fromDataHtml", method = RequestMethod.GET)
	public ModelAndView fromDataHtml(@RequestParam(value = "tableName" , required = true) String tableName,
			@RequestParam(value = "dataId" , required = true) String dataId) {
		ModelAndView mav = new ModelAndView("zform/fromDataHtml"); 
//		
		mav.addObject("tableName",tableName );
		mav.addObject("dataId",dataId );
		/*int i=0;
		++i;
		int j=0;
		j++ ;*/
		return mav;
	}
	/**
	 * 老板请求
	 * 
	 * @return
	 */
	@RequestMapping(value = "boss", method = RequestMethod.GET)
	public ModelAndView boss(@RequestParam(value = "id" , required = true) String id) {
		ModelAndView mav = new ModelAndView("zform/bossformTableView");
		mav.addObject("id", id);
		Set<String> strSet = new HashSet<String>();
		strSet.add("owner");
		strSet.add("day");
		strSet.add("dataId");
		strSet.add("tableName");
		strSet.add("formId");
		strSet.add("name");
//		
		Map<String, Object> variables= jbpmService.getVariables(id, strSet);
		mav.addObject("map",variables );
		return mav;
	}
	
	
//	@RequestMapping(value = "managerOperating", method = RequestMethod.GET)
//	public ModelAndView managerOperating(@RequestParam(value = "id" , required = true) String id) {
//		ModelAndView mav = new ModelAndView("zform/managerformTableView");
//		mav.addObject("id", id);
//		Set<String> strSet = new HashSet<String>();
//		strSet.add("owner");
//		strSet.add("day");
//		strSet.add("dataId");
//		strSet.add("tableName");
//		strSet.add("formId");
//		strSet.add("name");
////		
//		Map<String, Object> variables= jbpmService.getVariables(id, strSet);
//		mav.addObject("map",variables );
//		return mav;
//	}
	
	
	
	@RequestMapping(value = "managerOperating", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> managerOperating(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "resultValue", required = false) String resultValue) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			jbpmService.completeByManager(id, resultValue);
			map.put("code", "0");
			map.put("msg", "成功");
		} catch (Exception e) {
			map.put("code", "1");
			map.put("msg", "成功");
		}
		return map;
	}
	
	@RequestMapping(value = "bossOperating", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> bossOperating(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "resultValue", required = false) String resultValue) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//jbpmService.complateByBoss(id);
			jbpmService.boss(id);
			map.put("code", "0");
			map.put("msg", "成功");
		} catch (Exception e) {
			map.put("code", "1");
			map.put("msg", "成功");
		}
		return map;
	}
	
	
	/**
	 * 修改密码
	 * @throws Exception 
	 */
	@RequestMapping(value = "/updatePw")
	@ResponseBody
	public Map<String, Object> updatePw(String username) throws Exception{
		User user = userService.getByName(username);
		Map<String, Object> map = new HashMap<String, Object>();
		if(user!=null){
			user.setPassword("3d4f2bf07dc1be38b20cd6e46949a1071f9d0e3d");
			userService.update(user);
			map.put("code", "0");
			map.put("msg", "重置密码成功!");
		}else{
			map.put("code", "1");
			map.put("msg", "重置密码失败!");
		}
		return map;
	}
	
}
