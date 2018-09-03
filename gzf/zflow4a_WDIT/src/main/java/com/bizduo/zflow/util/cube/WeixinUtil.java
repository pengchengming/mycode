package com.bizduo.zflow.util.cube;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.bizduo.zflow.domain.bizType.BizType;
import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.bizType.DataDictionaryValue;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.bizType.IDataDictionaryService;

public class WeixinUtil {
	//获取
	public static Double getDictionaryValue(IDataDictionaryService dataDictionaryService,String code ){ 
		Double value=null;
		DecimalFormat    df   = new DecimalFormat("######0.00");   
		List<DataDictionaryValue> dataDictionaryValueList=dataDictionaryService.getDataDictionaryValueByCode(code);
		if(dataDictionaryValueList!=null&& dataDictionaryValueList.size()>0){
			String rechargeMoneyStr= dataDictionaryValueList.get(0).getValue(); 
			if(StringUtils.isNotBlank(rechargeMoneyStr) ){                                 
				value=Double.parseDouble(df.format(Double.parseDouble(rechargeMoneyStr))); 
			}
		} 
		return value;
	}
	
	
	
		/**
	     * 获取微信JSSDK的access_token 
	     * @author Benson
	     */
	    public static String getJSSDKAccessToken(IBizTypeService bizTypeService) {  
	    	
	    	List<BizValue>  bizValueList= bizTypeService.getBizValuesByCode("ACCESS_TOKEN");
	    	if(!(bizValueList!=null&&bizValueList.size()>0)){
	    		String returnString=""; 
		        String get_access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"+ "&appid="+ User.APPID+ "&secret="+ User.APPSECRET;
	            String accessTokenjson = HttpsGetUtil.doHttpsGetJson(get_access_token_url);
				System.out.println("accessTokenjson:"+accessTokenjson);
				
				 if(StringUtils.isNotBlank(accessTokenjson)){
	            	 JSONObject jsonObject = JSONObject.fromObject(accessTokenjson);
	            	 if(jsonObject.has("access_token")){
	            		 returnString = jsonObject.getString("access_token");
	            	 }
				 }
				 try {
					BizType bizType= bizTypeService.getBizTypeByCode("ACCESS_TOKEN");
					BizValue bizValue=new BizValue();
					bizValue.setBizType(bizType); 
					bizValue.setDisable(false);
					bizValue.setDisplayValue(returnString);
					bizValue.setRemark("存储token");
					bizTypeService.create(bizValue);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        return returnString;  
	    	}else {
	    		return bizValueList.get(0).getDisplayValue();
	    	}
	    }
	    /**
	     * 获取微信JSSDK的ticket 
	     * @author Benson
	     */
	    public static String getJSSDKTicket(String access_token,IBizTypeService bizTypeService) {  
	    	List<BizValue>  bizValueList= bizTypeService.getBizValuesByCode("TICKET");
	    	if(!(bizValueList!=null&&bizValueList.size()>0)){
	    		String returnString="";
	 	        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
	 	        String jssdkTicketStr = HttpsGetUtil.doHttpsGetJson(requestUrl);
	 			System.out.println("jssdkTicket:"+jssdkTicketStr);
	 			
	 			 if(StringUtils.isNotBlank(jssdkTicketStr)){
	             	 JSONObject jsonObject = JSONObject.fromObject(jssdkTicketStr);
	             	 if(jsonObject.has("ticket")){
	             		 returnString=jsonObject.getString("ticket");  
	             	 }
	 			 }
	 			try {
					BizType bizType= bizTypeService.getBizTypeByCode("TICKET");
					BizValue bizValue=new BizValue();
					bizValue.setBizType(bizType); 
					bizValue.setDisable(false);
					bizValue.setDisplayValue(returnString);
					bizValue.setRemark("存储ticket");
					bizTypeService.create(bizValue);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	        return returnString;  
	    	}else {
	    		return bizValueList.get(0).getDisplayValue();
	    	}
	    }  
	    public static String SHA1(String decript) {  
	        try {  
	            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");  
	            digest.update(decript.getBytes());  
	            byte messageDigest[] = digest.digest();  
	            // Create Hex String  
	            StringBuffer hexString = new StringBuffer();  
	            // 字节数组转换为 十六进制 数  
	                for (int i = 0; i < messageDigest.length; i++) {  
	                    String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
	                    if (shaHex.length() < 2) {  
	                        hexString.append(0);  
	                    }  
	                    hexString.append(shaHex);  
	                }  
	                return hexString.toString();  
	       
	            } catch (NoSuchAlgorithmException e) {  
	                e.printStackTrace();  
	            }  
	            return "";  
	    }

		  
}
