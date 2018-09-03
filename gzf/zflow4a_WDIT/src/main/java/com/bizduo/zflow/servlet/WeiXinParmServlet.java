package com.bizduo.zflow.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bizduo.zflow.domain.bizType.BizType;
import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.util.cube.HttpsGetUtil;

/**
 * Servlet implementation class WeiXinParmServlet
 */
public class WeiXinParmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeiXinParmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		IBizTypeService bizTypeService =(IBizTypeService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IBizTypeService.class);
		
		String returnString="";
		String code= request.getParameter("code");
		if(StringUtils.isNotBlank(code) 
				&&(code.equals("ACCESS_TOKEN")||code.equals("TICKET"))){
			if(code.equals("ACCESS_TOKEN")){
				String get_access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"+ "&appid="+ User.APPID+ "&secret="+ User.APPSECRET;
		        String accessTokenjson = HttpsGetUtil.doHttpsGetJson(get_access_token_url);
 
				 if(StringUtils.isNotBlank(accessTokenjson)){
		        	 JSONObject jsonObject = JSONObject.fromObject(accessTokenjson);
		        	 if(jsonObject.has("access_token")){
		        		 returnString = jsonObject.getString("access_token");
		        	 }
				 }
			}else if(code.equals("TICKET")){
				String access_token= request.getParameter("token");
				if(StringUtils.isNotBlank(access_token)){
		 	        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
		 	        String jssdkTicketStr = HttpsGetUtil.doHttpsGetJson(requestUrl);
		 			System.out.println("jssdkTicket:"+jssdkTicketStr);
		 			 if(StringUtils.isNotBlank(jssdkTicketStr)){
		             	 JSONObject jsonObject = JSONObject.fromObject(jssdkTicketStr);
		             	 if(jsonObject.has("ticket")){
		             		 returnString=jsonObject.getString("ticket");  
		             	 }
		 			 }
				}
			}
	       
	    	 try {
	    		List<BizValue>  bizValueList= bizTypeService.getBizValuesByCode(code);
	    		BizValue bizValue=new BizValue();
		    	if(bizValueList!=null){
		    		bizValue=bizValueList.get(0);
		    	}
	 			BizType bizType= bizTypeService.getBizTypeByCode(code);
	 			bizValue.setBizType(bizType); 
	 			bizValue.setDisable(false);
	 			bizValue.setDisplayValue(returnString);
	 			bizValue.setRemark("存储"+code);
	 			bizTypeService.create(bizValue);
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 		}
	    	 OutputStream os =response.getOutputStream();  
             os.write(returnString.getBytes("UTF-8"));  
		} 
    	 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
