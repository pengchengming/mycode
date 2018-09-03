package com.bizduo.zflow.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.base.PageNewTrace;
import com.bizduo.zflow.domain.base.Region;
import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.base.IRegionService;
import com.bizduo.zflow.service.sys.IOrganizationService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.TimeUitl;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping(value = "/userRole")
public class UserRoleController { 
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrganizationService organizationService;	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private IRegionService regionService;
	
	
	//用户维护角色
	@RequestMapping(value = "/template", produces = "text/html")
	public String precreate(@RequestParam(value = "type", required = false) Integer type, Model uiModel){		
		uiModel.addAttribute("user", UserUtil.getUser());
		return "/user/userRole/template";
	}
	
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>   save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> value = new HashMap<String, Object>();
		String id= request.getParameter("id");
		String username= request.getParameter("username");
		String password= request.getParameter("password");
		String realname= request.getParameter("realname");
		String tel= request.getParameter("tel");
		String email= request.getParameter("email");
		String report2= request.getParameter("report2");
		String appuid= request.getParameter("appuid");
		String orgId= request.getParameter("orgId");
		String entityid= request.getParameter("entityid");
		String birthday= request.getParameter("birthday");
		String gender= request.getParameter("gender"); //性别
		String height= request.getParameter("height");//身高
		String weight= request.getParameter("weight");//体重
		String district= request.getParameter("district");//区县
		try{
			boolean isblank=false;
			String blankValue = null;
			if(StringUtils.isBlank(username)){
				isblank=true;
				blankValue="用户名必填";
			}
			if(StringUtils.isBlank(password)){
				isblank=true;
				blankValue="密码必填";
			}/*
			if(StringUtils.isBlank(orgId)){
				isblank=true;
				blankValue="分公司必填";
			}*/
			if(isblank){
				value.put("code", 0);
				value.put("errorMsg",blankValue);
				return value;
			}	
			password=passwordEncoder.encode(password.trim());
			User user=new User();
			if(StringUtils.isNotBlank(id)){
				user =this.userService.findObjByKey(User.class, Integer.parseInt(id));
				user.setUsername(username); 
				user.setRealname(realname); 
				user.setPassword(password); 
				user.setTel(tel);
				user.setEmail(email);
			}else
				user=new User(username, realname, password, tel, email);
			user.setTel(tel);
			if(birthday!=null&&!birthday.trim().equals(""))
				user.setBirthday(TimeUitl.returnDateFormat(birthday, "yyyy-MM-dd"));
			if(gender!=null&&!gender.trim().equals(""))
				user.setGender(Integer.parseInt(gender));
			if(height!=null&&!height.trim().equals(""))
				user.setHeight(Double.parseDouble(height));
			if(weight!=null&&!weight.trim().equals(""))
				user.setWeight(Double.parseDouble(weight));
			if(district!=null&&!district.trim().equals(""))
				user.setDistrict(Integer.parseInt(district));
			if(StringUtils.isNotBlank(report2))
				user.setReport2(Integer.parseInt(report2));
			if(StringUtils.isNotBlank(appuid))
				user.setAppuid(Integer.parseInt(appuid));
			if(StringUtils.isNotBlank(entityid))
				user.setEntityid(Integer.parseInt(entityid));
			if(orgId!=null&&!orgId.trim().equals("")){
				Organization organization=this.organizationService.findObjByKey(Organization.class, Integer.parseInt(orgId));
				user.setOrganization(organization);
			}	
			this.userService.create(user);
			value.put("code", 1);
			value.put("successMsg", null == id ? "保存成功!" : "修改成功!");
		} catch (Exception e) {
			value.put("code", 0);
			value.put("errorMsg", null == id ? "保存失败!" : "修改失败!");
		}
		return value;
	}
	
	@RequestMapping(value = "enabled", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>   enabled(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> value = new HashMap<String, Object>();
		Integer enabled=Integer.parseInt(request.getParameter("enabled")) ;
		Integer id=Integer.parseInt(request.getParameter("id"));
		try{
			User user =this.userService.findObjByKey(User.class, id);
			boolean enabledval=true;
			if(enabled!=1)
				enabledval=false;
			user.setEnabled(enabledval);
			this.userService.create(user);
			value.put("code", 1);
			value.put("successMsg", null == id ? "保存成功!" : "修改成功!");
		} catch (Exception e) {
			value.put("code", 0);
			value.put("errorMsg", null == id ? "保存失败!" : "修改失败!");
		}
		return value;
	}
	
	
		
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>   list(HttpServletRequest request, HttpServletResponse response) { 
		Map<String, Object> value = new HashMap<String, Object>(); 
		
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
		
		try {
			Map<String ,Object> map=new HashMap<String, Object>();
			map.put("userName", request.getParameter("userName"));	 
			List<User>  userList=userService.listByMap(map,pageTrace); 
			List<User> newuserList=new ArrayList<User>();
			if(userList!=null){
				for (User user : userList) {
					User newUser=new User(user.getId(), user.getUsername(),user.getFirstname(), user.getLastname(), user.getEmail(),user.getPassword(), user.getFullname(), user.getProcessrole(), user.getTel(),user.getRealname(),user.getAppuid(),user.getEntityid(),user.getReport2(),user.isEnabled());
					newUser.setBirthday(user.getBirthday());
					newUser.setGender(user.getGender());
					newUser.setHeight(user.getHeight());
					newUser.setWeight(user.getWeight());
					if(user.getDistrict()!=null){
						newUser.setDistrict(user.getDistrict());
						Region region=this.regionService.findObjByKey(Region.class, user.getDistrict());
						if(region!=null){
							newUser.setDistrictName(region.getName());
							Region regionCity=this.regionService.findObjByKey(Region.class, region.getParentRegion().getId());
							newUser.setCityName(regionCity.getName());
							Region regionProvince=this.regionService.findObjByKey(Region.class, regionCity.getParentRegion().getId());
							newUser.setProvinceName(regionProvince.getName());
						}
					}
					if(user.getOrganization()!=null&&user.getOrganization().getId()!=null){
						Organization organization= this.organizationService.findObjByKey(Organization.class, user.getOrganization().getId()) ;
						Organization organizationtemp= new Organization();
						organizationtemp.setId(organization.getId());	
						organizationtemp.setName(organization.getName());
						newUser.setOrganization(organizationtemp);
					}
					newuserList.add(newUser);
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
			value.put("results", newuserList);
			value.put("code", 1);
		} catch (Exception e) {
			e.printStackTrace();
			value.put("code", 0);
		}
		return value;
		 
	}
	
}
