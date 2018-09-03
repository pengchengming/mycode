package com.bizduo.zflow.controller.WDIT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.controller.BaseController;
import com.bizduo.zflow.domain.form.SelectTableList;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.domain.tableData.DataTableToPage;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.ISelectTableListService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.util.UserUtil;

@Controller
@RequestMapping("talentoffice")
public class TalentofficeController extends BaseController {
	@Autowired
	private IFormService formService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IUserService userService;
	@Autowired
	public ISelectTableListService selectTableListService;
	@Autowired
	private IRoleService roleService;
	//人才办企业查看
	@RequestMapping("companylist")
	public String companylist(){
		
		return "/wdit/talentoffice/companyList";
	} 
	//人才办增加企业
	@RequestMapping("addcompany")
	public String addCompanry(){
		
		return "/wdit/talentoffice/addCompany";
	}
	//人才办修改企业
	@RequestMapping("updatecompany")
	public String updatecompany(@RequestParam("id")int id,@RequestParam("type")int type,Model model){
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		model.addAttribute("user", UserUtil.getUser());
		return "/wdit/talentoffice/addCompany";
	}
	//人才办查看企业详情
	@RequestMapping("showcompany")
	public String showcompany(@RequestParam("id")int id,Model model){
		model.addAttribute("id", id);
		return "/wdit/talentoffice/showCompany";
	}
	//人才办保存企业
	@RequestMapping(value = "/saveFormDataJson", method = RequestMethod.POST)
	@ResponseBody   	
	public Map<String, Object> saveFormDataJson(@RequestParam(value = "json", required = true) String json, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			JSONObject userJson = new JSONObject(json);
			JSONObject tempJsonObj = userJson.getJSONObject("register");
			String password= passwordEncoder.encode("Gzf@2017_XuHui");
			tempJsonObj.put("password",password);
			json=userJson.toString();
			Long id= formService.saveFormDataJson(json);			
			if(id==null||id.longValue()==0l){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
				return map;
			}
			User u = (User)userService.findByUserId(Integer.parseInt(id.toString()));
			Collection<Role> roles = new ArrayList<Role>();			
			roles.add(this.roleService.findObjByKey(Role.class, 4));
			u.getRoles().clear();
			u.getRoles().addAll(roles);
			userService.update(u);
			
			map.put("id", id);
			map.put("code", "1");
			map.put("successMsg",  "提示：\n    保存成功!");
		}catch(Exception e){
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
		}
		return map;
	}
	//人才办 修改企业密码
	@RequestMapping(value = "/saveFormDataJson1", method = RequestMethod.POST)
	@ResponseBody   	
	public Map<String, Object> saveFormDataJson1(@RequestParam("password")String password,@RequestParam(value = "json", required = true) String json, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			JSONObject userJson = new JSONObject(json);
			JSONObject tempJsonObj = userJson.getJSONObject("register");
			String repassword= passwordEncoder.encode(password);
			tempJsonObj.put("password",repassword);
			json=userJson.toString();
			Long id= formService.saveFormDataJson(json);			
			if(id==null||id.longValue()==0l){
				map.put("code", "0");
				map.put("errorMsg", "保存错误");
				return map;
			}
			User u = (User)userService.findByUserId(Integer.parseInt(id.toString()));
			Collection<Role> roles = new ArrayList<Role>();			
			roles.add(this.roleService.findObjByKey(Role.class, 4));
			u.getRoles().clear();
			u.getRoles().addAll(roles);
			userService.update(u);
			
			map.put("id", id);
			map.put("code", "1");
			map.put("successMsg",  "提示：\n    保存成功!");
		}catch(Exception e){
			map.put("code", "0");
			map.put("errorMsg", "保存错误");
		}
		return map;
	}
	//人才办审批
	@RequestMapping("rcbApprovalList")
	public String rcbApprovalList(Model model){
		return "/wdit/talentoffice/rcbApprovalList";
	}
	//人才办审批
	@RequestMapping("rcbshowCompany")
	public String rcbshowCompany(@RequestParam("id")int id,@RequestParam("type")int type,Model model){
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		return "/wdit/talentoffice/rcbshowCompany";
	}
	//人才办审批
	@RequestMapping("rcbShowUser")
	public String rcbShowUser(@RequestParam("requestUserId")int requestUserId,Model model){
		model.addAttribute("requestUserId", requestUserId);
		return "/wdit/talentoffice/rcbShowUser";
	}
	//人才办修改企业
	@RequestMapping("updatekjscompany")
	public String updatekjscompany(@RequestParam("id")int id,@RequestParam("type")int type,Model model){
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		model.addAttribute("user", UserUtil.getUser());
		return "/wdit/talentoffice/addkjsCompany";
	}
	//人才办查看企业详情
	@RequestMapping("showkjscompany")
	public String showkjscompany(@RequestParam("id")int id,@RequestParam("type")int type,Model model){
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		return "/wdit/talentoffice/showkjsCompany";
	}
	//人才办绿色通道审批
	@RequestMapping("rcbQuickList")
	public String rcbQuickList(Model model){
		return "/wdit/talentoffice/rcbQuickList";
	}
	//人才办绿色通道审批
	@RequestMapping("rcbQuickshowCompany")
	public String rcbQuickshowCompany(@RequestParam("id")int id,@RequestParam("type")int type,Model model){
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		return "/wdit/talentoffice/rcbQuickshowCompany";
	}
	//人才办绿色通道审批
	@RequestMapping("rcbQuickShowUser")
	public String rcbQuickShowUser(@RequestParam("requestUserId")int requestUserId,Model model){
		model.addAttribute("requestUserId", requestUserId);
		return "/wdit/talentoffice/rcbQuickShowUser";
	}
	@RequestMapping(value = "/findTalentofficeListDate")
	@ResponseBody	
	public  DataTableToPage  findselectData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("applicant")String  applicant,
			@RequestParam("companytype")String  companytype,
			@RequestParam("start_time")String  start_time,
			@RequestParam("end_time")String  end_time,
			@RequestParam("acceptanceNumber")String  acceptanceNumber,
			@RequestParam("cardnum")String  cardnum,
			@RequestParam("printname")String  printname
			) throws IOException{
		 
		String  selectConditionSql= " where 1=1 ";  //判断where语句
		selectConditionSql +=" and t1.status in(402,502,503,504,505,602,603,604) ";
		selectConditionSql +=" and t1.companyClassification in(1501,1502,1503) ";
		
		if(StringUtils.isNotBlank(applicant)){
			selectConditionSql +=" and t1.applicant like '%"+applicant+"%'";
		}
		
		if(StringUtils.isNotBlank(companytype)){
			if(companytype.equals("全部")){
			}else if(companytype.equals("待审核")){
				selectConditionSql +=" and t1.status in(402,504)";	
			}else if(companytype.equals("已审核")){
				selectConditionSql +=" and t1.status not in(402,504,505)";
			}
		}
		if(StringUtils.isNotBlank(start_time))
			selectConditionSql += " and (DATE(t1.createDate) > '" + start_time+"' or DATE(t1.createDate) = '"+start_time+"')";
		if(StringUtils.isNotBlank(end_time))
			selectConditionSql +=  " and (DATE(t1.createDate) < '"+ end_time + "' or DATE(t1.createDate) = '"+end_time+"') ";
			
		if(StringUtils.isNotBlank(acceptanceNumber)){
			selectConditionSql +=" and t1.acceptanceNumber like '%"+acceptanceNumber+"%'";
		}
		if(StringUtils.isNotBlank(cardnum)){
			selectConditionSql +=" and exists( select 1 from  wdit_company_request_user where request_id=t1.id and  identityCardNumber like '%"+cardnum+"%'  ) ";
		}
		if(StringUtils.isNotBlank(printname)){
			if( printname.trim().equals("1"))
				selectConditionSql+= " and EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 5 and type = 1) "+
			            " and EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 5 and type = 2) ";
			else if(printname.trim().equals("0"))
				selectConditionSql+= " and ((not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 5 and type = 1) "+
	                    " and not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 5 and type = 2)) "+
	                    " or (EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 5 and type = 1) and "+
	                    "  not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 5 and type = 2))"+
	                    " or (not EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 5 and type = 1) and "+
	                    "  EXISTS (SELECT 1 FROM WDIT_Company_Request_Print WHERE requestId = t1.id and step = 5 and type = 2)))";
		}
		
		 
		String code= request.getParameter("code");
		
		DataTableToPage dataTableToPage = new DataTableToPage();
		if(!(code!=null&&!code.trim().equals(""))){
			dataTableToPage.setCode(0);
			return dataTableToPage;
		}
		   
			SelectTableList selectTableList=new SelectTableList();
			try {
				  selectTableList=selectTableListService.findByTitle(code);
			} catch (Exception e) {
				// TODO: handle exception
			} 
			if(selectTableList==null||selectTableList.getId()==null){
				dataTableToPage.setCode(0);
				dataTableToPage.setErrorMsg("传入的code编号出错");
			}
			PageTrace pageTrace = null;
			if(request.getParameter("pageIndex")!=null){
				Integer pageIndex= Integer.parseInt(request.getParameter("pageIndex"));
				Integer pageSize= Integer.parseInt(request.getParameter("pageSize"));
				pageTrace = new PageTrace(pageSize);
				pageTrace.setPageIndex(pageIndex);
			}
			String json = selectTableListService.selectBySqlPage(selectTableList,selectConditionSql,pageTrace,0);
			try {
				JSONObject jsonObj = new JSONObject(json);
				if (jsonObj.has("code"))
					dataTableToPage.setCode(Integer.parseInt(jsonObj.get("code").toString()));
				else
					dataTableToPage.setCode(0);
				dataTableToPage.setResults(jsonObj.getJSONArray("results").toString());
				if (jsonObj.has("paged"))
					dataTableToPage.setPaged(jsonObj.getJSONObject("paged").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		return dataTableToPage;
	}
	
	
	
	
	
}
