package com.bizduo.zflow.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.base.PageNewTrace;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping(value = "/roleMaintain")
public class RoleMaintainController {
	@Autowired
	private IRoleService roleService;
	
	@RequestMapping(value = "/template", produces = "text/html")
	public String precreate(@RequestParam(value = "type", required = false) Integer type, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/user/roleMaintain/template";
	}
	@RequestMapping(value = "/rolePermission", produces = "text/html")
	public String rolePermission(@RequestParam(value = "id", required = false) Integer id, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		uiModel.addAttribute("roleId",id );
		return "/user/roleMaintain/rolePermission";
	}
	 
	
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> value = new HashMap<String, Object>(); 
		try {

			Integer pageIndex=null;
			if(request.getParameter("pageIndex")!=null){
				pageIndex=Integer.parseInt(request.getParameter("pageIndex"));
			} 
			Integer pageSize=null;
			if(request.getParameter("pageSize")!=null){
				pageSize=Integer.parseInt(request.getParameter("pageSize"));
			}
			PageTrace pageTrace = new PageTrace(pageSize);
			pageTrace.setPageIndex(pageIndex);
			 

			Map<String ,Object> map=new HashMap<String, Object>();
			map.put("roleName", request.getParameter("roleName"));	
			List<Role> roleList= (List<Role>) roleService.findByMap(map,pageTrace);
			List<Role> newRoleList=new ArrayList<Role>();
			if(roleList!=null){
				for (Role role : roleList) {
					Role newrole=new Role(role.getId(), role.getName(), role.getAcronym(), role.getDescription());
					newRoleList.add(newrole);
				}
				PageNewTrace pageNewTrace=new PageNewTrace();
				pageNewTrace.setPageIndex(pageIndex);
				pageNewTrace.setLcsCount(pageSize);				 
				pageNewTrace.setCordCnt(pageTrace.getTotal());
				pageNewTrace.setPageCnt(pageTrace.getLastPageIndex());
				
				String pagem = "";
				for(int i=1;i<=pageTrace.getLastPageIndex();i++){
	                pagem = pagem + i + "_";
	            }
				pageNewTrace.setPagem(pagem);
				value.put("paged", pageNewTrace);	
			} 

			value.put("results", newRoleList);
			value.put("code", 1);
		} catch (Exception e) {
			value.put("code", 0);
		}
		return value;
	}

}
