package zflow;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.jbpm.api.IdentityService;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.job.Job;
import org.jbpm.api.task.Task;

public class CCMTest  {
	  String deploymentId;
	  
	  String ppgroup;
	  String tausergroup;
	  String taleadergroup;
	  String selectedppgroup; 
		private IdentityService identityService;
	  /*
	  private Wiser wiser = new Wiser();

	  
	  protected void setUp() throws Exception {
		  
		    
		    // create identities
		    tausergroup = identityService.createGroup("tausergroup");
		    taleadergroup = identityService.createGroup("taleadergroup");
		    ppgroup = identityService.createGroup("ppgroup");
		    selectedppgroup = identityService.createGroup("selectedppgroup");

		    identityService.createUser("tauser", "John", "Doe");
		    identityService.createUser("taleaderuser", "John", "Doe");
		    identityService.createUser("ppgroupuser", "John", "Doe");
		    identityService.createUser("selectedppgroupuser1", "John", "Doe");
		    
		    
		    identityService.createMembership("tauser", tausergroup, "unknown");
		    identityService.createMembership("taleaderuser", taleadergroup, "unknown");
		    identityService.createMembership("ppgroupuser", ppgroup, "unknown");
		    identityService.createMembership("selectedppgroupuser1", selectedppgroup, "unknown");
		    identityService.createMembership("selectedppgroupuser1", ppgroup, "unknown");
		    
		    
		    // start mail server
		    wiser.setPort(2525);
		    wiser.start();
		    
		    // deploy process
		    deploymentId = repositoryService.createDeployment()
		        .addResourceFromClasspath("org/jbpm/excercise/ccm/ccm.jpdl.xml")
		        .deploy();
		    
		  }
	  protected void tearDown() throws Exception {
		    // delete process deployment
		    repositoryService.deleteDeploymentCascade(deploymentId);

		    // delete identities
		    identityService.deleteGroup(ppgroup);
		    identityService.deleteGroup(tausergroup);
		    identityService.deleteGroup(taleadergroup);
		    identityService.deleteGroup(selectedppgroup);
		    identityService.deleteUser("tauser");
		    identityService.deleteUser("taleaderuser");
		    identityService.deleteUser("ppgroupuser");
		    identityService.deleteUser("selectedppgroupuser1");
//		    identityService.deleteUser("selectedppgroupuser2");

		    super.tearDown();
		  }

	 
		public void testCCMTest() throws MessagingException {    
		    //executionService.startProcessInstanceByKey("clientCall");
		    //启动流程
//		    Map<String,Object> map1 = new HashMap<String,Object>();
			  
		    Map<String, String> map1 = Collections.singletonMap("owner", "tauser");
		    //task already taken by tauser
		    
		    
//		    map1.put("owner", "tauser");
		    ProcessInstance processInstance =executionService.startProcessInstanceByKey("clientCall", map1);
		    
		    //TA填写单据
		    List<Task> taskList = taskService.findGroupTasks("tauser");
		    assertEquals("There is no Group Tasks", 0, taskList.size());

		    taskList = taskService.findPersonalTasks("tauser");
		    assertEquals(1, taskList.size());
		    Task task = taskList.get(0);
		    
		    String taskId = task.getId();
		    // lets assume that johndoe takes the task
		    //taskService.takeTask(taskId, "tauser");

		    // the next task will be created and assigned directly to johndoe
		    // this is because johndoe was the person that took the previous task 
		    // in the salesRepresentative swimlane.  so that person is most likely 
		    // to know the context of this case

		    // we'll check that the group task lists for johndoe and joesmoe are empty
		    assertEquals(0, taskService.findGroupTasks("tauser").size());

		   
		    // and that the task is directly assigned to johndoe
		    taskList = taskService.findPersonalTasks("tauser");
		    assertEquals(1, taskList.size());
		    task = taskList.get(0);
		    assertEquals("填单", task.getName());
		    assertEquals("tauser", task.getAssignee());

		    // submit the task
		    taskService.completeTask(taskId);

		    
		    //taleaderuser审核
		    taskList = taskService.findGroupTasks("taleaderuser");
		    assertEquals("Expected a single task in tauser task list", 1, taskList.size());
		    task = taskList.get(0);
		    taskId = task.getId();
		    assertEquals("TA Leader审批", task.getName());
		    assertNull(task.getAssignee());		    
		    taskService.takeTask(taskId, "taleaderuser");
		    taskList = taskService.findPersonalTasks("taleaderuser");
		    assertEquals(1, taskList.size());
		    task = taskList.get(0);
		    
		    assertEquals("TA Leader审批", task.getName());
		    assertEquals("taleaderuser", task.getAssignee());
		    taskId = task.getId();
		    assertEquals(0, taskService.findGroupTasks("taleaderuser").size());
		    
		    //测试用户为动态增加的用户
//		    identityService.createUser("selectedppgroupuser2", "John", "Doe");
//		    identityService.createMembership("selectedppgroupuser2", selectedppgroup, "unknown");
//		    identityService.createMembership("selectedppgroupuser2", ppgroup, "unknown");
		    
		    Map<String, String> map2 = Collections.singletonMap("creator", "tauser");
		    taskService.setVariables(taskId, map2);
		    taskService.completeTask(taskId);
		    
		    

		    // examine produced message
		    List<WiserMessage> wisMessages = wiser.getMessages();
		    // winston, bb, innerparty(obrien), thinkpol(charr, obrien)
		    //assertEquals(5, wisMessages.size());

		    for (WiserMessage wisMessage : wisMessages) {
		      Message message = wisMessage.getMimeMessage();
		      // from
		      Address[] from = message.getFrom();
		      assertEquals(1, from.length);
		      assertEquals("test@bizduo.com", from[0].toString());
		      // to
		      Address[] expectedTo = InternetAddress.parse("lixiao@bizduo.com");
		      Address[] to = message.getRecipients(RecipientType.TO);
		      assert Arrays.equals(expectedTo, to) : Arrays.toString(to);
		      // cc
//		      Address[] expectedCc = InternetAddress.parse("lixiao@bizduo.com,lixiao@bizduo.com");
//		      System.out.println(Arrays.toString(expectedCc));
//		      Address[] cc = message.getRecipients(RecipientType.CC);
//		      System.out.println(Arrays.toString(cc));
//		      assert Arrays.equals(expectedCc, cc) : Arrays.toString(cc);
		      // bcc - recipients undisclosed
//		      assertNull(message.getRecipients(RecipientType.BCC));
		      // subject
		      //assertEquals("rectify " + newspaper, message.getSubject());
		      // text
		      //assertTextPresent(newspaper + ' ' + date + " reporting bb dayorder", (String) message.getContent());
		    }		    
		    
		    Set<String> currentActivityNames = processInstance.findActiveActivityNames();
		    
		    
		    

		      

		       //流程实例现在流转到哪个Activity了

		       System.out.println(currentActivityNames.toString());		    
		    //ppgroup认领

		    taskList = taskService.findGroupTasks("ppgroupuser");
		    assertEquals("Expected a single task in ppgroupuser task list", 0, taskList.size());
		    
		    taskList = taskService.findPersonalTasks("ppgroupuser");
		    assertEquals(0, taskList.size());
		    
		    taskList = taskService.findPersonalTasks("selectedppgroupuser2");
		    assertEquals(0, taskList.size());
		    
		    taskList = taskService.findGroupTasks("selectedppgroupuser2");
		    assertEquals("Expected a single task in ppgroupuser task list", 1, taskList.size());
		    task = taskList.get(0);
		    taskId = task.getId();
		    assertEquals("pptake", task.getName());
		    assertNull(task.getAssignee());

		    taskService.takeTask(taskId, "selectedppgroupuser2");
		    
		    taskList = taskService.findGroupTasks("selectedppgroupuser2");
		    assertEquals("selectedppgroupuser1 Group Task list", 0, taskList.size());
		    taskList = taskService.findPersonalTasks("selectedppgroupuser2");
		    assertEquals(1, taskList.size());
		    taskService.completeTask(taskId);
		    
		    Job job = managementService.createJobQuery()
		            .processInstanceId(processInstance.getId())
		            .uniqueResult();

		        // timer was produced, no messages yet
		        assertNotNull("expected job to be non-null", job);
		        assertEquals(0, wiser.getMessages().size());

		        managementService.executeJob(job.getId());
		        
		    //ppgroup反馈		    
		    taskList = taskService.findGroupTasks("selectedppgroupuser1");
		    assertEquals("Expected a single task in ppgroupuser task list", 0, taskList.size());
		    
		    taskList = taskService.findGroupTasks("selectedppgroupuser2");
		    assertEquals("Expected a single task in ppgroupuser task list", 0, taskList.size());

		    taskList = taskService.findPersonalTasks("selectedppgroupuser1");
		    assertEquals(0, taskList.size());		    

		    taskList = taskService.findPersonalTasks("selectedppgroupuser2");
		    assertEquals(1, taskList.size());
		    task = taskList.get(0);
		    taskId = task.getId();
		    taskService.completeTask(taskId);
		    
		  }	  */
}
