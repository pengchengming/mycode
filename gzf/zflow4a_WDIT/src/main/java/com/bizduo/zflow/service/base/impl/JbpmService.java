package com.bizduo.zflow.service.base.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.identity.User;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizduo.zflow.service.base.IJbpmService;

@Service
public class JbpmService implements IJbpmService {
	/*@Autowired
	private ProcessEngine processEngine;*/
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ExecutionService executionService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	
	public void setUpIdentityService(List<String> userlist,Map<String, List<String>> groupmap)
	{
		
		identityService.createUser("tauser", "John", "Doe");
	    identityService.createUser("taleaderuser", "John", "Doe");
	    identityService.createUser("ppgroupuser", "John", "Doe");
	    identityService.createUser("selectedppgroupuser1", "John", "Doe");
	    
		for(String username:userlist){
			identityService.createUser(username, username, username);
		}
	    
		for(Map.Entry<String, List<String>> entry: groupmap.entrySet()) {
			 String group = identityService.createGroup(entry.getKey());
			 List<String> membershiplist =( List<String>) entry.getValue();
			 for(String member:membershiplist){
				 identityService.createMembership(member, group, "unknown");
			 }
			}
	}

	//保存到jbpm——user中
	@SuppressWarnings("unused")
	public boolean createUserIdentity(List<String> userlist) {
		String usernames="";
		try {
			for(String username:userlist){
				usernames=username;
				identityService.createUser(username, username, username);
				
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} 
	//保存到jbpm—Membership中
	public boolean createMembership(Map<String, List<String>> groupmap) {
		try {
			for(Map.Entry<String, List<String>> entry: groupmap.entrySet()) {
				 String group = identityService.createGroup(entry.getKey());
				 List<String> membershiplist =( List<String>) entry.getValue();
				 for(String member:membershiplist){
					 identityService.createMembership(member, group, "unknown");
				 }
				}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void deletetearDownIdentityService()
	{ 
		List<User>  userlist  = identityService.findUsers(); 
		for(User user:userlist){
			List<String> grouplist = identityService.findGroupIdsByUser(user.getId());
			
			for(String groupId:grouplist){
				identityService.deleteGroup(groupId);
			}	
			identityService.deleteUser(user.getId());
		} 
	}
	
	
	public List<ProcessDefinition> getProcessDefinitions() {
		return repositoryService.createProcessDefinitionQuery().list();
	}

	public String deploy(String path,String zipname) {
		// repositoryService.createDeployment().addResourceFromClasspath(
		// "/com/jbpm/source/leave.jpdl.xml").deploy();
		ZipInputStream zis = new ZipInputStream(this.getClass()
				.getResourceAsStream("/com/bizduo/zflow/flow/"+path+"/"+zipname+".zip"));
		// 发起流程，仅仅就是预定义任务，即在系统中创建一个流程，这是全局的，
		//与具体的登陆 用户无关。然后，在启动流程时，才与登陆用户关联起来
//		String did = 
		return		repositoryService.createDeployment()
				.addResourcesFromZipInputStream(zis).deploy();
	}
	public String ccmdeploy(String zipname) {
		// repositoryService.createDeployment().addResourceFromClasspath(
		// "/com/jbpm/source/leave.jpdl.xml").deploy();
		ZipInputStream zis = new ZipInputStream(this.getClass()
				.getResourceAsStream("/com/bizduo/zflow/flow/ccm/"+zipname+".zip"));
		// 发起流程，仅仅就是预定义任务，即在系统中创建一个流程，这是全局的，
		//与具体的登陆 用户无关。然后，在启动流程时，才与登陆用户关联起来
//		String did = 
		return		repositoryService.createDeployment()
				.addResourcesFromZipInputStream(zis).deploy();
	}

	public void undeploy(String id) {
		repositoryService.deleteDeploymentCascade(id);

	}
	public ProcessInstance startProcessByKey(String processDefinitionKey, Map<String, Object> map) {
		ProcessInstance p= executionService.startProcessInstanceByKey(processDefinitionKey, map);
		return p; 	
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Task> getProcessInstanceTaskList(ProcessInstance pi){
		TaskQuery tq = taskService.createTaskQuery();  
		List tasklist =tq.processInstanceId(pi.getId()).list();
		return tasklist;
	}
	@SuppressWarnings("unused")
	public void start(String id, Map<String, Object> map) {
		ProcessInstance p= executionService.startProcessInstanceById(id, map);
		String pid= p.getId(); 
	}
	public String startccm(String id, Map<String, Object> map) {
		ProcessInstance p= executionService.startProcessInstanceById(id, map);
		return p.getId(); 
	}

	public List<ProcessInstance> getProcessInstances() {
		return executionService.createProcessInstanceQuery().list();
	}

	public List<Task> getTasks(String userName) {
//		identityService.createUser("tauser", "John", "Doe");

		List<Task> personalTaskList = taskService.findPersonalTasks(userName);
		List<Task> groupTaskList = taskService.findGroupTasks(userName);
		List<Task> taskList = new ArrayList<Task>();
		taskList.addAll(personalTaskList);
		taskList.addAll(groupTaskList);
		return taskList;
	}

	public void complete(String taskId, Map<String, Object> map) {
		taskService.setVariables(taskId, map);
		taskService.completeTask(taskId);
	}

	public Map<String, Object> manager(String id) {
		Task task = taskService.getTask(id);
		String taskId = task.getId();
		Set<String> strSet = new HashSet<String>();
		strSet.add("owner");
		strSet.add("day");
		strSet.add("reason");
		strSet.add("name");

		return taskService.getVariables(taskId, strSet);
	}
	
	public Map<String, Object> getVariables(String id ,Set<String> strSet) {
		Task task = taskService.getTask(id);
		String taskId = task.getId();
//		Set<String> strSet = new HashSet<String>();
//		strSet.add("owner");
//		strSet.add("day");
//		strSet.add("reason");
//		strSet.add("name");
		return taskService.getVariables(taskId, strSet);
	}	
	

	public void completeByManager(String id, String result) {
		taskService.completeTask(id, result);
	}

	public Map<String, Object> boss(String id) {
		Task task = taskService.getTask(id);
		String taskId = task.getId();
		Set<String> strSet = new HashSet<String>();
		strSet.add("owner");
		strSet.add("day");
		strSet.add("reason");
		strSet.add("name");
		strSet.add("age");
		strSet.add("address");
		taskService.completeTask(id);
		return taskService.getVariables(taskId, strSet);
	}

	public List<HistoryProcessInstance> getHistoryProcessInstances() {
		return historyService.createHistoryProcessInstanceQuery().list();
	}

	public ActivityCoordinates findActivityCoordinates(String id) {
		// 通过id查到流程实例
		ProcessInstance processInstance = executionService
				.findProcessInstanceById(id);
		Set<String> activityNames = processInstance.findActiveActivityNames();
		// Coordinates为相依衣物
		return repositoryService.getActivityCoordinates(processInstance
				.getProcessDefinitionId(), activityNames.iterator().next());

	}

	public InputStream findPicInputStream(String id) {
		// 通过流程实例id查找到流程实例
		ProcessInstance processInstance = executionService
				.findProcessInstanceById(id);
		// 通过流程实例查找流程定义id
		String processDefinitionId = processInstance.getProcessDefinitionId();
		// 通过流程id查找流程
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().processDefinitionId(
						processDefinitionId).uniqueResult();
		return repositoryService.getResourceAsStream(processDefinition
				.getDeploymentId(), "leave.png");
	}

	public void completeByBoss(String id) {
		taskService.completeTask(id);
	}

	public String findPersonalTasks(String userid) {
		// TODO Auto-generated method stub
		List<Task>  taskList =taskService.findPersonalTasks(userid);
		Task task = taskList.get(0);
//		taskService.completeTask(task.getId());
		return task.getId();
	}



}
