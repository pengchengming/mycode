package com.bizduo.zflow.controller.sys;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.sys.IOrganizationService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.status.OrganType;
import com.bizduo.zflow.util.ExchangeMail;
import com.bizduo.zflow.util.SelectToJson;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IOrganizationService organizationService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private IFormService formService;

	@RequestMapping(value = "template", produces = "text/html")
	public String template() {
		return "user/template";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> save(@RequestBody User user) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			OrganType type = null;
			for (OrganType o : OrganType.values()) {
				if (o.getIndex() == user.getRole()) {
					type = o;
					break;
				}
			}
			Role role = roleService.findObjByKey(Role.class, user.getRole());
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			switch (type) {
			case TYY:
				break;
			case TLY:
				user.setRoles(roles);
				userService.createUser(user, true);
				break;
			case TJQ:
				Organization org = user.getOrganization();
				Organization temp = organizationService.getByName(user
						.getOrganization().getName());
				if (null == temp) {
					// org.setCreateBy(UserUtil.getUser().getUsername());
					org.setCreateDate(new Date());
					org = organizationService.create(org);
					user.setOrganization(org);
					user.setRoles(roles);
					userService.createUser(user, true);
				} else {
					map.put("errorMsg", "提示：\n   景区名称已经存在，请修改后重试!");
				}
				break;
			case TSJ:
				org = user.getOrganization();
				temp = organizationService.getByName(user.getOrganization()
						.getName());
				if (null == temp) {
					// org.setCreateBy(UserUtil.getUser().getUsername());
					org.setCreateDate(new Date());
					org.setParent(organizationService
							.getByAcronym(OrganType.TSJ.name()));
					org = organizationService.create(org);
					user.setOrganization(org);
					user.setRoles(roles);
					userService.createUser(user, true);
				} else {
					map.put("errorMsg", "提示：\n   商家名称已经存在，请修改后重试!");
				}
				break;
			case TGR:
				org = user.getOrganization();
				temp = organizationService.getByName(user.getOrganization()
						.getName());
				if (null == temp) {
					// org.setCreateBy(UserUtil.getUser().getUsername());
					org.setCreateDate(new Date());
					org.setParent(organizationService
							.getByAcronym(OrganType.TSJ.name()));
					org = organizationService.create(org);
					user.setOrganization(org);
					user.setRoles(roles);
					userService.createUser(user, true);
				} else {
					map.put("errorMsg", "提示：\n   个人名称已经存在，请修改后重试!");
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "提示：\n   用户添加失败,请稍后再试!");
			return map;
		}
		map.put("successMsg", "提示：\n   用户添加成功!");
		return map;
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> modify(@RequestBody User user) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			// 查询当前User的用户名是否已经存在
			User o = userService.getByName(user.getId(), user.getUsername());
			if (null == o) {
				// 取到当前User的信息，取到User的Role属性，设置给参数user
				o = userService.findObjByKey(User.class, user.getId());
				user.setRoles(o.getRoles());
				if (null != user.getPassword()
						&& !("").equals(user.getPassword())) {
					userService.createUser(user, true);
				} else {
					user.setPassword(o.getPassword());
					userService.createUser(user, false);
				}
			} else {
				map.put("errorMsg", "提示：\n   用户名已存在,请尝试其他用户名!");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "提示：\n   用户信息修改失败,请稍后再试!");
			return map;
		}
		map.put("successMsg", "提示：\n   用户信息修改成功!");
		return map;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> create(@RequestBody User user) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			this.userService.create(user);
		} catch (Exception e) {
			e.printStackTrace();
			if (null == user.getId())
				map.put("errorMsg", "添加失败,请稍后再试!");
			else
				map.put("errorMsg", "修改失败,请稍后再试!");
			return map;
		}
		if (null == user.getId())
			map.put("successMsg", "用户添加成功!");
		else
			map.put("successMsg", "用户修改成功!");
		return map;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, String> delete(@PathVariable("id") Integer id,
			Model uiModel) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			this.userService.delete(User.class, id);
		} catch (Exception e) {
			map.put("errorMsg", "删除失败!");
			return map;
		}
		map.put("successMsg", "删除成功!");
		return map;
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new User());
		return "user/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		uiModel.addAttribute("user", userService.findObjByKey(User.class, id));
		uiModel.addAttribute("itemId", id);
		return "user/show";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	@ResponseBody
	public User get(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		User user = userService.findObjByKey(User.class, id);
		User temp = new User();
		temp.setId(user.getId());
		temp.setRealname(user.getRealname());
		temp.setUsername(user.getUsername());
		return temp;
	}

	@RequestMapping(value = "showByName", method = RequestMethod.POST)
	@ResponseBody
	public List<User> showByName(
			@RequestParam(value = "name", required = false) String name)
			throws Exception {
		List<User> user = this.userService.findByUserName(name);
		for (User u : user) {
			u.setOrganization(null);
		}
		return user;
	}

	// @ZflowJsonFilters(value = {@ZflowJsonFilter(mixin = UserListFilter.class,
	// target = User.class),
	// @ZflowJsonFilter(mixin = OrganizationInUserFilter.class, target =
	// Organization.class)})
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public List<User> list(
			@RequestParam(value = "orgId", required = false) Integer orgId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		// if (page != null || size != null) {
		// int sizeNo = size == null ? 10 : size.intValue();
		// final int firstResult = page == null ? 0 : (page.intValue() - 1) *
		// sizeNo;
		// uiModel.addAttribute("user", User.findDoctorEntries(firstResult,
		// sizeNo));
		// float nrOfPages = (float) User.countDoctors() / sizeNo;
		// uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages
		// || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		// } else {
		// uiModel.addAttribute("user", User.findAllDoctors());
		// }
		// return "user/list";
		if (null != orgId && 0L != orgId) {
			return (List<User>) this.userService.findByOrganization(orgId);
		} else {
			List<User> userList = (List<User>) this.userService
					.findAll(User.class);
			List<User> newuserList = new ArrayList<User>();
			if (userList != null) {
				for (User user : userList) {
					User newUser = new User(user.getId(), user.getUsername(),
							user.getFirstname(), user.getLastname(),
							user.getPassword(), user.getFullname(),
							user.getProcessrole(), user.getTel());
					newuserList.add(newUser);
				}
			}
			return newuserList;
		}
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid User user, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws Exception {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, user);
			return "user/update";
		}
		uiModel.asMap().clear();
		// /user.merge();
		userService.update(user);
		return "redirect:/user/"
				+ encodeUrlPathSegment(user.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/updateUserRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateUserRole(
			@RequestParam(value = "userId", required = true) Integer userId,
			@RequestParam(value = "ids[]", required = true) Integer[] ids)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		// org.codehaus.jackson.map.ObjectMapper obj = new
		// org.codehaus.jackson.map.ObjectMapper();
		// String json = "[";
		// for(int i = 0; i < ids.length; i++){
		// json = json + "{id : " + ids[i] + "}";
		// }
		// json = json + "]";
		// List<LinkedHashMap<String, Role>> list = obj.readValue(json,
		// List.class);
		try {
			this.userService.updateUserRole(userId, ids);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "保存失败,请稍后再试!");
			return map;
		}
		map.put("successMsg", "用户角色设置成功!");
		return map;
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel)
			throws Exception {
		populateEditForm(uiModel,
				(User) userService.findObjByKey(User.class, id));
		return "user/update";
	}

	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces
	// = "text/html")
	// public String delete(@PathVariable("id") Long id,
	// @RequestParam(value = "page", required = false) Integer page,
	// @RequestParam(value = "size", required = false) Integer size,
	// Model uiModel) throws Exception {
	// // User user = (User)userService.findObjByKey(User.class, id);
	// userService.delete(User.class, id);
	// uiModel.asMap().clear();
	// uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	// uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	// return "redirect:/user";
	// }

	// @RequestMapping(method = RequestMethod.POST, produces = "text/html")
	// public String create(@Valid User user, BindingResult bindingResult,
	// Model uiModel, HttpServletRequest httpServletRequest) {
	// if (bindingResult.hasErrors()) {
	// populateEditForm(uiModel, user);
	// return "user/create";
	// }
	// uiModel.asMap().clear();
	// try {
	// userService.create(user); // user.persist(); commit
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return "redirect:/user/"
	// + encodeUrlPathSegment(user.getId().toString(),
	// httpServletRequest);
	// }

	void populateEditForm(Model uiModel, User user) {
		uiModel.addAttribute("user", user);
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}

	@RequestMapping(value = "/userList.do")
	@ResponseBody
	public void userList(
			HttpServletResponse response,
			@RequestParam(value = "parameter", required = true) String parameter,
			@RequestParam(value = "pageIndex", required = true) int pageIndex,
			@RequestParam(value = "pageSize", required = true) int pageSize)
			throws IOException {
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("id", "id");
		fieldMap.put("fullname", "fullname");
		fieldMap.put("username", "username");
		String sql = "  select a.id,a.fullname,a.username from global_user  a  "
				+ "  where 1=1  " + parameter;
		sql += " order by id desc  ";
		Map<String, Integer> pageMap = new HashMap<String, Integer>();
		pageMap.put("pageIndex", pageIndex);
		pageMap.put("pageSize", pageSize);

		String designerJson = SelectToJson.executionsql(jdbcTemplate, sql,
				"query", fieldMap, pageMap);
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter().print(designerJson);
	}

	// /业务员
	@RequestMapping(value = "/userCurrentSales")
	@ResponseBody
	public Map<String, Object> userCurrentSales(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String roleCode = request.getParameter("roleCode");
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("roleCode", roleCode);

			User createUser = UserUtil.getUser();
			if (createUser == null || createUser.getId() == null) {
				map.put("code", "0");
				map.put("errorMsg", "提示：\n   请先登录!");
				return map;
			} else
				createUser = this.userService.findObjByKey(User.class,
						createUser.getId());
			if (createUser.getEntityid() != null)
				paramMap.put("entityid", createUser.getEntityid().toString());
			else {
				map.put("code", "0");
				map.put("errorMsg", "提示：\n   分公司不存在，请检查当前登录人信息!");
				return map;
			}
			List<User> userList = this.userService.getUserByMap(paramMap);
			// List<User> userList= this.userService.getUserCurrentSales();
			if (userList != null && userList.size() > 0) {
				List<User> newuserList = new ArrayList<User>();
				for (User user : userList) {
					User newuser = new User();
					newuser.setId(user.getId());
					newuser.setUsername(user.getUsername());
					newuser.setRealname(user.getRealname());
					newuserList.add(newuser);
				}
				map.put("code", 1);
				map.put("results", newuserList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 0);
		}
		return map;
	}

	// /修改密码
	@RequestMapping(value = "/updatePassword")
	@ResponseBody
	public Map<String, String> updatePassword(HttpServletResponse response,
			@RequestParam(value = "password", required = true) String password) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			User user = UserUtil.getUser();
			JSONObject userJson = new JSONObject();
			userJson.put("formId", 24);
			userJson.put("tableDataId", user.getId());

			JSONObject registerJson = new JSONObject();
			String repassword = passwordEncoder.encode(password);
			registerJson.put("password", repassword);
			userJson.put("register", registerJson);
			Long id = formService.saveFormDataJson(userJson.toString());
			if (id == null || id.longValue() == 0l) {
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
				return map;
			}
			map.put("code", "1");
			map.put("successMsg", "保存成功");
		} catch (Exception e) {
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
			return map;
		}
		return map;
	}

	// 发送邮箱
	@RequestMapping(value = "/bindsendEmail")
	@ResponseBody
	public Map<String, String> bindsendEmail(@RequestParam(value = "email", required = true) String email,HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		 
		String radomInt = passwordEncoder.encode(new Random().nextInt(9999) + "");//随机数
		String requestUrl = request.getRequestURL().toString();
		String url = requestUrl.substring(0, requestUrl.indexOf("//",requestUrl.indexOf("//")+1));
		String path = url + "/user/bindCheckEmail?code="+ radomInt;//路径
		JSONObject userJson = new JSONObject();
		userJson.put("formId", 95);
		JSONObject registerJson = new JSONObject();
		registerJson.put("type", 1);
		User user = (User) request.getSession().getAttribute("USER");
		registerJson.put("companyId", user.getCompanyId());
		registerJson.put("code", radomInt);
		registerJson.put("mail",email);
		registerJson.put("isSuccess",0);
		userJson.put("register", registerJson);
		try {
			int result = ExchangeMail.sendEmail(email,path);
			if(result == 0 ||new Integer(result) == null){
				map.put("code", "0");
				map.put("errorMsg", "邮件发送失败");
				return map;
			}
			Long id = formService.saveFormDataJson(userJson.toString());
			if (id == null || id.longValue() == 0l) {
				map.put("code", "0");
				map.put("errorMsg", "数据库记录未生成");
				return map;
			}
			map.put("code", "1");
			map.put("successMsg", "发送成功");
		} catch (Exception e) {
			map.put("code", "0");
			map.put("errorMsg", "系统记录失败");
		}
		return map;
	}
	
	// 绑定邮箱
		@RequestMapping(value = "/bindCheckEmail")
		public String bindCheckEmail(@RequestParam(value="code",required=true)String code,Model model,HttpServletRequest request){
			Map<String,String> map =new HashMap<String, String>();
			map.put("formCode", "WDIT_bind");
			String condition =" code = '" + code + "'";
			Integer global_user_Id = null;
			Integer companyId = null;
			String email = "";
			Integer bindId = null;
			List list = new ArrayList();
			JSONObject configJson = new JSONObject();
			configJson.put("formCode","global_user");
			configJson.put("currentField","companyId");
			configJson.put("parentId","companyId");
			configJson.put("aliasesName","userDetail");
			configJson.put("fieldName","id");
			list.add(configJson);
			JSONObject fromConfig = new JSONObject();
			fromConfig.put("fromConfig", list);
			JSONObject json= formService. getDataByFormId(map, condition, null,fromConfig+"",1);
			if(json.has("code") &&json.getInt("code")== 1){
				if(json.has("results") && json.getJSONArray("results").length()>0){
					JSONObject bind= json.getJSONArray("results").getJSONObject(0);
					if(bind.has("userDetail") && bind.getJSONArray("userDetail").length()>0){
						JSONObject user= bind.getJSONArray("userDetail").getJSONObject(0);
						global_user_Id = user.getInt("id");
					}
					if(bind.has("companyId")){
						companyId = Integer.parseInt(bind.getString("companyId"));
						email = bind.getString("mail");
						bindId = Integer.parseInt(bind.getString("id"));
					}
				}	
			}
			if(bindId != null && global_user_Id != null && email != null && companyId != null && !email.equals("")){
				String[] jsons = new String[3];//
				JSONObject bindobj = new JSONObject();
				bindobj.put("formId", 95);
				bindobj.put("tableDataId", bindId);
				JSONObject bindobjRegister = new JSONObject();
				bindobjRegister.put("isSuccess", 1);
				bindobj.put("register", bindobjRegister);
				jsons[0]=bindobj.toString();
				JSONObject userobj = new JSONObject();
				userobj.put("formId", 24);
				userobj.put("tableDataId", global_user_Id);
				JSONObject userobjRegister = new JSONObject();
				userobjRegister.put("email", email);
				userobj.put("register", userobjRegister);
				jsons[1]=userobj.toString();
				JSONObject comobj = new JSONObject();
				comobj.put("formId", 58);
				comobj.put("tableDataId", companyId);
				JSONObject comobjRegister = new JSONObject();
				comobjRegister.put("email", email);
				comobj.put("register", comobjRegister);
				jsons[2]=comobj.toString();
				try {
					JSONObject returnObj= formService.saveFormDataJson(jsons);
					Boolean  isCorrect =returnObj.getBoolean("isCorrect");
					if(!isCorrect){
						map.put("code", "0");
						map.put("errorMsg", "保存错误");
						
					}else{
						map.put("code", "1");
					    map.put("successMsg",  "提示：\n   绑定成功!");	
					}	
					 
			}catch(Exception e){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
			}
				model.addAttribute("map",map);
			}
			return "redirect:/j_spring_security_logout";
			
		}
	
	
	
	
	
	
	
	
	
}
