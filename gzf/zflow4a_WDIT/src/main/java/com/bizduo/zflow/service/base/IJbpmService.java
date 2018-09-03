package com.bizduo.zflow.service.base;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;

/**
 * jbpm管理类接口
 * 
 * @author Administrator
 * 
 */
public interface IJbpmService {
	/**
	 * 获取以部署的流程
	 * 
	 * @return
	 */
	public List<ProcessDefinition> getProcessDefinitions();

	/**
	 * 发布新流程
	 */
	public String deploy(String path,String zipname );

	/**
	 * 移除新流程
	 * 
	 * @param id
	 */
	public void undeploy(String id);

	/**
	 * 启动流程实例
	 * 
	 * @param id
	 * @param object
	 */
	public void start(String id, Map<String, Object> map);

	/**
	 * 启动流程实例
	 * 
	 * @param id
	 * @param object
	 */
	public String startccm(String id, Map<String, Object> map);
	
	/**
	 * 获取流程实例
	 * 
	 * @return
	 */
	public List<ProcessInstance> getProcessInstances();

	/**
	 * 获取代办任务列表
	 * 
	 * @param roleName
	 * @return
	 */
	public List<Task> getTasks(String username);

	/**
	 * 处理任务
	 * 
	 * @param taskId
	 * @param map
	 */
	public void complete(String taskId, Map<String, Object> map);

	/**
	 * 查询流程实例的参数集合
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> manager(String id);

	/**
	 * 经理处理流程
	 * 
	 * @param id
	 * @param result
	 */
	public void completeByManager(String id, String result);

	/**
	 * 老板请求
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> boss(String id);

	/**
	 * 老板处理
	 * 
	 * @param id
	 */
	public void completeByBoss(String id);

	/**
	 * 获取历史流程实例
	 * 
	 * @return
	 */
	public List<HistoryProcessInstance> getHistoryProcessInstances();

	/**
	 * 查找流程图的活动坐标
	 * 
	 * @param id
	 * @return
	 */
	public ActivityCoordinates findActivityCoordinates(String id);

	/**
	 * 查找图片输入流
	 * 
	 * @param id
	 * @return
	 */
	public InputStream findPicInputStream(String id);
	/**
	 * 获取保存jbpm的信息
	 */
	public Map<String, Object> getVariables(String id ,Set<String> strSet);
	
	/**
	 * 设置IdentityService
	 */
	public void setUpIdentityService(List<String> userlist,Map<String, List<String>> groupmap);
	/**
	 * 删除user和Membership
	 */
	public void deletetearDownIdentityService();
	/**
	 * //保存到jbpm——user中
	 * @param userlist
	 * @return
	 */
	public boolean createUserIdentity(List<String> userlist);
	
	/**
	 * //保存到jbpm—Membership中
	 * @param userlist
	 * @return
	 */ 
	public boolean createMembership(Map<String, List<String>> groupmap);
	
	/**
	 * 
	 * 根据流程名称启动流程的最新版本呢
	 * @param processDefinitionKey
	 * @param map
	 * @return
	 */
	
	public ProcessInstance startProcessByKey(String processDefinitionKey, Map<String, Object> map) ;
	/**
	 *
	 * 
	 * @param pi
	 * @return
	 */
	public List<Task> getProcessInstanceTaskList(ProcessInstance pi);
	/**
	 * 根据用户名查找任务,并完成
	 * @param userid
	 * @return
	 */
	public String findPersonalTasks(String userid);
	
}
