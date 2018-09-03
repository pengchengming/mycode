package com.bizduo.zflow.quartz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.data.IDataService;
import com.bizduo.zflow.util.ExchangeMail;
import com.mysql.fabric.xmlrpc.base.Array;

public class EmailQuartz {
	
	@Autowired
	private IDataService dataService;
	@Autowired
	private IFormService formService;
	@Autowired
	private IBizTypeService  bizTypeService;
	
	//给公司发邮件通知审核拒绝等结果
	public void sendemailbyresult(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("formCode", "wdit_company_request_sendemail");
		JSONObject json= this.formService. getDataByFormId(map, " flag = 0 ", null,null,1);
		if(json.has("code") &&json.getInt("code")== 1){
			if(json.has("results") && json.getJSONArray("results").length()>0){
				JSONArray  emailObjs =   json.getJSONArray("results");
				String[] saveJson=new String[emailObjs.length()];
     			for(int i = 0;i < emailObjs.length();i++){
     				JSONObject emailObj = emailObjs.getJSONObject(i);
     				Integer id = emailObj.getInt("id");
     				String email = emailObj.getString("email");
     				String content = emailObj.getString("content");
     				if(email != null && !email.equals("") && !content.equals("")&&content != null && id != null){
    					Boolean  isSend = ExchangeMail.sendbystatus(email, content);
    					JSONObject objmap = new JSONObject();
    					objmap.put("formId", 99);
    					objmap.put("tableDataId", id);
    					JSONObject registermap = new JSONObject();
    					int flag = 2;//默认成功
    					if(!isSend){
    						flag = 1;//失败
    					}
    					registermap.put("flag", flag);
    					objmap.put("register", registermap);
    					saveJson[i] = objmap.toString();
			        }	
			    }
     			int saveJsonlength = saveJson.length;
     			if(saveJsonlength == 1){
     				try {
						formService.saveFormDataJson(saveJson[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
     			}else if(saveJsonlength > 1){
     				try {
						formService.saveFormDataJson(saveJson);
					} catch (Exception e) {
						e.printStackTrace();
					}
     			}			
	       }
	    }
	}
	
	//把待审批是0的去除掉
	public  List<Map<String, Object>>  relistbyzero (List<Map<String, Object>> numlist){
		List<Map<String, Object>>  newNumList = new ArrayList<Map<String,Object>>();
		for(int z = 0;z<numlist.size();z++){
			Map<String, Object>  numobj = numlist.get(z);
			Long requestnum = (Long) numobj.get("requestnum");
			if(requestnum != 0){
				newNumList.add(numobj);
			} 
		}
		return newNumList;
	}
	
	private static String SETUPINTRANET = "SetupIntranet";
	
	public  void addjson(String email, String content){
		List<String> saveJson = new ArrayList<String>();
		 Boolean  isSend = ExchangeMail.sendbystatus(email, content);
	     JSONObject objmap = new JSONObject();
		 objmap.put("formId", 99);
		 JSONObject registermap = new JSONObject();
		 int flag = 2;//默认成功
		 if(!isSend){
				flag = 1;//失败
		 }
		registermap.put("flag", flag);
		registermap.put("email", email);
		registermap.put("content", content);
		registermap.put("type", 2);//2为发给审核人
		objmap.put("register", registermap);
		saveJson.add(objmap.toString());
		if(saveJson.size()>0){
			String[] arrString  = (String[]) saveJson.toArray(new String[saveJson.size()]) ;
			int saveJsonLength = arrString.length;
			if(saveJsonLength != 0){
				if(saveJsonLength == 1){
	 				try {
	 					formService.saveFormDataJson(arrString[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
	 			}else if(saveJsonLength > 1){
	 				try {
						formService.saveFormDataJson(arrString);
					} catch (Exception e) {
						e.printStackTrace();
					}
	 			}		
			}
		}
	}
	

	
	

	
	//给各个审批人发送邮件告诉人家每天待审批条数
	public void sendemailbycheck(){
//		List<Map<String,String>> emailJson = new ArrayList<Map<String,String>>();
		Map<String,String> mapobj =  new HashMap<String, String>();
		String value = null ;
		List<BizValue> listBizValue = bizTypeService.getBizValuesByCode(SETUPINTRANET);
		if (listBizValue != null && listBizValue.size() > 0) {
			value = listBizValue.get(0).getDisplayValue();//设置内外网1外网2内网
		}	
		if(value != null && !value.equals("")){
			List<Map<String, Object>>  rolelist =  dataService.callShellProcedure("R2014007|"+value); //房管局邮箱      商委，人社局,人才办邮箱
			List<Map<String, Object>>  numlist =  dataService.callShellProcedure("R2014008|"+value);//房管局查待审批    商委，人社局,人才办查待审批
			                           numlist = this.relistbyzero(numlist);
			if(numlist.size() > 0){
				SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd");
				String currentdate = myFmt.format(new Date());
				if(value.equals("1")){ //查房管局	
					String content = " 您好：截止"+currentdate+" 8:00，您当前的  ";
					for(int j = 0;j<numlist.size();j++){
						Map<String, Object>  numobj = numlist.get(j);
						Long requestnum = (Long) numobj.get("requestnum");//待审批条数
						Long step = (Long) numobj.get("step");//审批步骤
						int stepValue = new Long(step).intValue();
						switch (stepValue) {
						case 1:
							content += " 资料预审的待审批数为"+ requestnum + "条 ";
							break;
		                case 2:
		                	content += " 公租房资格审核的待审批数为"+ requestnum + "条 ";
							break;
		                case 6:
		                	content += " 现场审核待审批数为"+ requestnum + "条 ";
							break;
						}
					}  
					for(int i = 0;i<rolelist.size();i++){
						Map<String, Object>  emailobj = rolelist.get(i);
						String email = (String) emailobj.get("email");
						String realname = (String) emailobj.get("realname");
						String newcontent = realname + content;
						mapobj.put(email, newcontent);
					}
				}else if(value.equals("2")){ //委办 人社局 人才办
					for(int i = 0;i<rolelist.size();i++){
						Map<String, Object>  emailobj = rolelist.get(i);
						Integer roleId = (Integer) emailobj.get("role_id");
						String email = (String) emailobj.get("email");
						String realname = (String) emailobj.get("realname");
						for(int j = 0;j<numlist.size();j++){
							Map<String, Object>  numobj = numlist.get(j);
							Long role_id = (Long) numobj.get("role_id");//角色id
	                        if(roleId == new Long(role_id).intValue()){  
	                        	Long requestnum = (Long) numobj.get("requestnum");//待审批条数
	    						Long step = (Long) numobj.get("step");//审批步骤
	    						int stepValue = new Long(step).intValue();
	    						String content = realname + "您好：截止"+currentdate+" 8:00，您当前的  ";
	    						switch (stepValue) {
	    						 case 3:
	    	                     	content += " 委办资质审核的待审批数为"+ requestnum + "条 ";
	    								break;
	    	                     case 4:
	    	                     	content += " 人社局审核待审批数为"+ requestnum + "条 ";
	    								break;
	    	                     case 5:
	    	                     	content += " 人才办审核待审批数为"+ requestnum + "条 ";
	    								break;
	    						}	
	    						mapobj.put(email, content);
							}
						}  
					}	
				}
              Map<String, Integer> resultmap = ExchangeMail.sendbymap(mapobj);
              String[] saveJson=new String[resultmap.keySet().size()];
              for(int i = 0;i<saveJson.length;i++){
	              for (String resultemail: resultmap.keySet()) {
					 for (String email : mapobj.keySet()) {
						     if(resultemail.equals(email)){
							     JSONObject objmap = new JSONObject();
								 objmap.put("formId", 99);
								 JSONObject registermap = new JSONObject();
								registermap.put("flag", resultmap.get(resultemail));
								registermap.put("email", email);
								registermap.put("content", mapobj.get(email));
								registermap.put("type", 2);//2为发给审核人
								objmap.put("register", registermap);
								saveJson[i] = objmap.toString();	
							}
						}
					}
			  }
            int saveJsonLength = saveJson.length;
  			if(saveJsonLength != 0){
  				if(saveJsonLength == 1){
  	 				try {
  	 					formService.saveFormDataJson(saveJson[0]);
  					} catch (Exception e) {
  						e.printStackTrace();
  					}
  	 			}else if(saveJsonLength > 1){
  	 				try {
  						formService.saveFormDataJson(saveJson);
  					} catch (Exception e) {
  						e.printStackTrace();
  					}
  	 			}		
  			}
              
			}                        
		}		
	}
	
	
}
