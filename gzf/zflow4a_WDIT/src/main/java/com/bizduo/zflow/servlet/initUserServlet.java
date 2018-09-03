package com.bizduo.zflow.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.base.IJbpmService;
import com.bizduo.zflow.service.sys.IUserService;

/**
 * Servlet implementation class initUser
 */
public class initUserServlet extends HttpServlet {
	private static final long serialVersionUID = 4974355309479534373L;
	@Autowired
	private IUserService userService;
//	@Autowired
	private IJbpmService jbpmService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public initUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void init() throws ServletException {
		 //WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		//this.proxy = (Servlet)wac.getBean(targetBean); 
		WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		userService = (IUserService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IUserService.class);
		jbpmService = (IJbpmService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IJbpmService.class);
					
		// TODO Auto-generated method stub
		super.init();
		List<User> userAllList= this.userService.findByAllUser();
		List<String> userlist = new ArrayList<String>();
		if(userAllList!=null){
			for (User user : userAllList) {
				userlist.add(user.getUsername());
			}
		} 
		///ta
		Map<String, List<String>> groupmap=new HashMap<String, List<String>>();
		String[] tas=new String[]{"ta"};
		List<User> userTaList= this.userService.findByProcessroles(tas);
		List<String> groupMembership = new ArrayList<String>();
		if(userTaList!=null){
			for (User user : userTaList) {
				groupMembership.add(user.getUsername());
			}
		}  
		groupmap.put("taUser", groupMembership);  
		//taLeader
		groupMembership = new ArrayList<String>();
		String[] taLeaders=new String[]{"taLeader"};
		List<User> userTaLeaderList= this.userService.findByProcessroles(taLeaders);
		if(userTaLeaderList!=null){
			for (User user : userTaLeaderList) {
				groupMembership.add(user.getUsername());
			}
		}
		groupmap.put("taLeader", groupMembership);
		// "ppGroup","ppFeedback"
		groupMembership = new ArrayList<String>();
		String[] ppGroups=new String[]{"ppGroup","ppFeedback"};
		List<User> userPPGroupsList= this.userService.findByProcessroles(ppGroups);
		if(userTaLeaderList!=null){
			for (User user : userPPGroupsList) {
				groupMembership.add(user.getUsername());
			}
		}  
		groupmap.put("ppGroup", groupMembership);
		
		//
		jbpmService.deletetearDownIdentityService();
		//
		boolean iscreate= jbpmService.createUserIdentity(userlist);
		if(iscreate)
			jbpmService.createMembership(groupmap);
		//jbpmService.setUpIdentityService(userlist,groupmap);
	}

}
