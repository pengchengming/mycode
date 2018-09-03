package com.bizduo.zflow.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.sys.MenuItem;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.sys.IMenuItemService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.Sorter;
import com.bizduo.zflow.util.UserUtil;

public class MenuItemFilter implements Filter {

	@Autowired
	private IMenuItemService menuItemService;
	@Autowired
	private IUserService userService;
//	private WebApplicationContext springContext;
	@Autowired
	private IBizTypeService  bizTypeService;

	public void destroy() {
		// TODO Auto-generated method stub
	}
  
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse res = (HttpServletResponse)response;
			HttpSession session = req.getSession();
			String path = req.getContextPath();
			
			Object menuId= request.getParameter("menuId");
			if(menuId!=null)
				session.setAttribute("menuId", menuId); 
			String[] uris={"/organization/getByType","/user/save","/deviceContent/btDevicecontent",
					"/deviceContent/cloudCoupon","/deviceContent/question",
					"/bindUser"};
			List<String> uriList=Arrays.asList(uris);
			
			//如果是ajax请求响应头会有，x-requested-with； 在响应头设置session状态 
			if(null == UserUtil.getUser() && ("XMLHttpRequest").equalsIgnoreCase(req.getHeader("x-requested-with"))){
				String ua = req.getHeader("User-Agent");
	     		boolean isPhonePage= UserUtil.checkAgentIsMobile(ua); 
				if(!uriList.contains(req.getServletPath())&&!isPhonePage){
					//设置Header请求头，设置错误编号，提醒用户重新登陆
					res.setHeader("sessionstatus", "timeout");
					res.sendError(401);
					res.getWriter().print("unLogin");
				}
	        }
			//判断是否 
			if(null != SecurityContextHolder.getContext().getAuthentication() && 
			   null != SecurityContextHolder.getContext().getAuthentication().getPrincipal() &&
			   null == session.getAttribute("MENUITEMS") && 
				   !("anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal()))){
				User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					
	             User user1= userService.findByUserId(user.getId());
            	 session.removeAttribute("loginType");
            	 user.setProcessrole(user1.getProcessrole()); 
            	 user1.setPermissions(user.getPermissions());
            	 user1.setPhonePermissions(user.getPhonePermissions());
            	 user1.setPermissionMap(user.getPermissionMap());
            	 user1.setPhonePermissionMap(user.getPhonePermissionMap());
            	 
				 session.setAttribute("USER", user1);
	             if(!user1.isEnabled()){
	            	 if( null != session.getAttribute("loginType") )session.removeAttribute("loginType");
	            	 session.setAttribute("loginType", "this user is not enabled!");
	            	 throw new BadCredentialsException("this user is not enabled!");
	             } 
	             List<BizValue> listBizValue = bizTypeService.getBizValuesByCode(User.ISROLEGROUP);
	      		boolean isRoleGroup=false;//true 从缓存中取值
	     		if (listBizValue != null && listBizValue.size() > 0) {
	     			String value = listBizValue.get(0).getDisplayValue();
	     			if(StringUtils.isNotBlank(value)&& value.equals("1") ){
	     				isRoleGroup=true;
	     			}
	     		} 
	     		//全部角色
	    		Collection<Role> roles =userService.getRolesByUserId(user1.getId());
	    		String  roleStr="";
	    		String roleSt0="";
	    		JSONArray roleArray = new JSONArray();
	    		int rolei=0;
	    		for (Role role : roles) {
	    			JSONObject roleObject = new JSONObject();
	    			roleObject.put("code",role.getName());
	    			roleObject.put("name",role.getDescription());
	    			roleArray.put(roleObject);
	    			if(isRoleGroup){
	    				String userRole= user1.getRoleName();
	    				if(userRole!=null&&role.getName()!=null&&
	    						userRole.equals(role.getName())	){
	    					user1.setRoleName(role.getName());
		    				roleStr=role.getName()+",";	
	    				}
	    			}
    				if(rolei==0){
    					rolei=1;
    					roleSt0=role.getName();
    				}
	    			if(!isRoleGroup)
	    				roleStr+=role.getName()+",";
	    			
	    		}
	    		if(roleStr.equals("")){
	    			roleStr=roleSt0+",";
	    			user1.setRoleName(roleSt0);
	    		}
	    		session.setAttribute("ROLES", roleStr);
	    		session.setAttribute("ROLEALL", roleArray.toString());
	     		//是否角色分拆 

            	String code = request.getParameter("code");
	     	    System.out.println("menuItem-->uri-->code:"+req.getServletPath()+"--->"+code);
	     		grantedAuthorityUserRole(session, req, res, user1, path,code, isRoleGroup);
				 String  uri= req.getRequestURI();
	             if(uri.endsWith("/") && !uri.endsWith("/wditLogin")){
	                res.sendRedirect(path + "/wditIndex");
	                return  ;
	             } 
			}else {
            	String code = request.getParameter("code");
	     	    System.out.println("menuItem-->uri-->code:"+req.getServletPath()+"--->"+code);
	     	    
				 String currentRole= req.getParameter("switching");
				 if(StringUtils.isNotBlank(currentRole)){
					 User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					 User user1= userService.findByUserId(user.getId());
	            	 session.removeAttribute("loginType");
	            	 user.setProcessrole(user1.getProcessrole()); 
	            	 user1.setPermissions(user.getPermissions());
	            	 user1.setPhonePermissions(user.getPhonePermissions());
	            	 user1.setPermissionMap(user.getPermissionMap());
	            	 user1.setPhonePermissionMap(user.getPhonePermissionMap());
						//是否角色分拆 
	            	  	List<BizValue> listBizValue = bizTypeService.getBizValuesByCode(User.ISROLEGROUP);
		  	      		boolean isRoleGroup=false;//true 从缓存中取值
		  	     		if (listBizValue != null && listBizValue.size() > 0) {
		  	     			String value = listBizValue.get(0).getDisplayValue();
		  	     			if(StringUtils.isNotBlank(value)&& value.equals("1") ){
		  	     				isRoleGroup=true;
		  	     			}
		  	     		} 
		  	     	//当前角色
		  	     	session.setAttribute("ROLES", currentRole+",");
		  	     	user1.setRoleName(currentRole);
			     	grantedAuthorityUserRole(session, req, res, user1, path,code, isRoleGroup);
				 }
				 String  uri= req.getRequestURI();
	             if(uri.endsWith("/")){
	                res.sendRedirect(path + "/wditIndex");
	                return  ;
	             } 
			} 
			chain.doFilter(req, response);
	}

	public void init(FilterConfig config) throws ServletException {
//		 springContext =   WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
	}

	
	@SuppressWarnings("unchecked")
	private void grantedAuthorityUserRole(HttpSession session,HttpServletRequest req,HttpServletResponse res  ,User user1,String path,String code,boolean isRoleGroup) throws IOException{
		
        //用户全部权限
        Set<GrantedAuthority>  sp = this.userService.getAllUserPermission(user1,isRoleGroup);   //mscUserService.getAllUserPermission(UserUtil.getUser().getId());
		if(sp!=null&&sp.size()>0){
			String[] ps=new String[sp.size()];
			GrantedAuthority[] pArray=new GrantedAuthority[sp.size()];
			sp.toArray(pArray);
			for(int i=0;i<sp.size();i++){
				ps[i]=pArray[i].getAuthority();
			}
			session.setAttribute("PERMISSIONS", ps); 
		}
			
       String ua = req.getHeader("User-Agent");
       int isPage= UserUtil.checkAgentIsMobileInt(ua); 
        if(isPage==1){ 
			try {
				List<MenuItem> menuitems = menuItemService.menuTreeNode(menuItemService.findPhoneByUser(user1,isRoleGroup));
				session.setAttribute("MENUITEMS", menuitems);
				if(menuitems!=null){
					JSONArray menuitemArray=new JSONArray();
					for (MenuItem menuItem : menuitems) {
						JSONObject menuitemObj=new JSONObject();
						menuitemObj.put("id", menuItem.getId() );
						menuitemObj.put("name", menuItem.getName());
						List<MenuItem> subMenuItemList= menuItem.getSubMenuItemList();
						JSONArray subMenuitemArray=new JSONArray();
						for (MenuItem menuItem2 : subMenuItemList) {
							JSONObject submenuitemObj=new JSONObject();
							submenuitemObj.put("id", menuItem2.getId() );
							submenuitemObj.put("name", menuItem2.getName());
							submenuitemObj.put("phoneUrl", menuItem2.getPhoneUrl());
							submenuitemObj.put("isPhone", menuItem2.getIsPhone());
							subMenuitemArray.put(submenuitemObj);
						}
						menuitemObj.put("subMenuItemList", subMenuitemArray);
						menuitemArray.put(menuitemObj);
					} 
					session.setAttribute("MENUITEMS2", menuitemArray);  
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			/*String  uri= req.getRequestURI();
            if(!uri.contains("/cube/phoneIndex")){
     	        String phoneUri="/cube/phoneIndex";
     	        if(StringUtils.isNotBlank(code))
     	        	phoneUri+="?code="+code;
     	        res.sendRedirect(path + phoneUri);
            } */
        }
        else if(isPage==2){
        	/*String  uri= req.getRequestURI();
            if(!uri.contains("/cube/padIndex")){
           	 res.sendRedirect(path + "/cube/padIndex");	 
            } */
        } else {
			try {
//				List<MenuItem> menuitems = menuItemService.menuTreeNode(menuItemService.findByUser(user1,isRoleGroup));
//				session.setAttribute("MENUITEMS", menuitems);
				Collection<MenuItem> menuItems= menuItemService.findByUser(user1,isRoleGroup);
				Map<MenuItem,List<MenuItem>> subMenuItemListTwoMap =menuItemService. getParentSubMenuItemMap(menuItems);   
			   	   //循环Map    获取 二级集合
		   	    subMenuItemListTwoMap= Sorter.sort(subMenuItemListTwoMap,"menu");
		   	    List<MenuItem> menuitems = menuItemService.getMenuItemListByMap(subMenuItemListTwoMap);
			   	session.setAttribute("MENUITEMS", menuitems); 
			   	
				if(menuitems!=null){
					JSONArray menuitemArray=new JSONArray();
					for (MenuItem menuItem : menuitems) {
						JSONObject menuitemObj=new JSONObject();
						menuitemObj.put("id", menuItem.getId() );
						menuitemObj.put("name", menuItem.getName());
						List<MenuItem> subMenuItemList= menuItem.getSubMenuItemList();
						JSONArray subMenuitemArray=new JSONArray();
						for (MenuItem menuItem2 : subMenuItemList) {
							JSONObject submenuitemObj=new JSONObject();
							submenuitemObj.put("id", menuItem2.getId() );
							submenuitemObj.put("name", menuItem2.getName());
							submenuitemObj.put("phoneUrl", menuItem2.getPhoneUrl());
							submenuitemObj.put("isPhone", menuItem2.getIsPhone());
							
							submenuitemObj.put("url", menuItem2.getUrl());
							submenuitemObj.put("isPc", menuItem2.getIsPc());
							subMenuitemArray.put(submenuitemObj);
						}
						menuitemObj.put("subMenuItemList", subMenuitemArray);
						menuitemArray.put(menuitemObj);
					} 
					session.setAttribute("MENUITEMS2", menuitemArray);  
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            /*String  uri= req.getRequestURI();
            if(!uri.contains("/wditIndex")){
           	 res.sendRedirect(path + "/wditIndex");	 
            } */
        } 
	}
}
