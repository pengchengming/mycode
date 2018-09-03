package com.bizduo.zflow.mybatis.service;
 
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizduo.zflow.mybatis.domain.SMS;
import com.bizduo.zflow.mybatis.domain.SMSResult;
import com.bizduo.zflow.mybatis.mapper.SMSMapper;
import com.bizduo.zflow.util.HttpUtils;
import com.bizduo.zflow.util.barcode.BarcodeFactory;
import com.google.zxing.BarcodeFormat;

/**
 * @author zhuc
 * @create 2013-3-11 下午1:19:03
 */
@Service("smsService")
//@Transactional(value = "isap", rollbackFor = Exception.class)
public class SMSService extends BaseMySqlService {
	protected static ResourceBundle Configure_Resource = ResourceBundle.getBundle("configure", Locale.getDefault());

	@Autowired
	private SMSMapper smsMapper;
 
	/**
	 * @return
	 */
	public List<SMS> findAll() {
		return smsMapper.findAll();
	}

	/**
	 * @return
	 */
	public List<SMS> findSMS2Send() {
		return smsMapper.findSMS2Send();
	}
	
	/**
	* @return
	*/
	public int updateSMSByID(SMS sms) {
		return smsMapper.updateSMSByID(sms);
	}
	
	@SuppressWarnings("rawtypes")
	public void sendSMS(){
		
		SMSResult smsresult;
		
		String responsecontent  ="<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms>"
				+" <returnstatus>Success</returnstatus>"
				+" <message>ok</message>"
				+" <remainpoint>163</remainpoint>"
				+" <taskID>281804</taskID>"
				+" <successCounts>1</successCounts>"
				+" </returnsms>";

		String action="send";
		String userid="2107";
		String account="goodin";
		String password="123456";
		String mobile="13916337676";
		String sign="旅游云";
		String content="你好，早上";
		String sendTime="";
		String taskName ="";
		String checkcontent="1";
		int mobilenumber=1;
		int countnumber =1;
		int telephonenumber=1;
				
		
//		【旅游云】你好，早上&sendTime=&taskName=旅游云&checkcontent=1&mobilenumber=10&countnumber=12&telephonenumber=1"
			
			List<SMS> smslist=findSMS2Send();
			for(int i=0;i<smslist.size();i++){
				System.out.println(((SMS)smslist.get(i)).getMobile());
				SMS sms =(SMS)smslist.get(i);
				//二维码校验，先生成二维码
				if(sms.verifytype==2){
					File file=new File(Configure_Resource.getString("staticpath").trim()+sms.verifycode+".png");
					BarcodeFactory.encode(sms.verifycode, file, BarcodeFormat.QR_CODE, 200, 200, null);
				}
				
				mobile =	sms.getMobile();
				content = new String("【"+sign+"】"+sms.getContent());
				try {
					content = URLEncoder.encode(content, "UTF-8");
//					url = ""+url;
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
//				try {
//					//content = new String(("【"+sign+"】"+sms.getContent()).getBytes("ISO-8859-1"),"utf-8");
//					
//				} catch (UnsupportedEncodingException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				//
				
				
				String url="http://115.29.171.5:9001/sms.aspx?action="+action+"&userid="+userid+"&account="+account
						+"&password="+password+"&mobile="+mobile+"&content="+content+"&sendTime="+sendTime
						+"&taskName="+taskName+"&checkcontent="+checkcontent+"&mobilenumber="+mobilenumber
						+"&countnumber="+countnumber+"&telephonenumber="+telephonenumber;

				responsecontent  = HttpUtils.httpGet(url);
				sms.setMsg(responsecontent);
				sms.setSubmittime(new Date());
				
				System.out.println(responsecontent);
				smsresult= new SMSResult();
				Document doc = null;  
				  try {
					  doc = DocumentHelper.parseText(responsecontent);
			          Element elements = doc.getRootElement(); // 获取根节点  

			          System.out.println("根节点：" + elements.getName()); // 拿到根节点的名称  
			          
			          for (Iterator j = elements.elementIterator(); j.hasNext();) {  
			        	  Element node = (Element) j.next();  
			        	  String nodname = node.getName();
			        	  if("returnstatus".equals(nodname)){
			        		  smsresult.setReturnstatus(node.getText());
			        	  }
			        	  else if("message".equals(nodname)){
			        		  smsresult.setMessage(node.getText());
			        	  }
			        	  else if("remainpoint".equals(nodname)){
			        		  smsresult.setRemainpoint(node.getText());
			        	  }
			        	  else if("taskID".equals(nodname)){
			        		  smsresult.setTaskID(node.getText());
			        	  }
			        	  else if("successCounts".equals(nodname)){
			        		  smsresult.setSuccessCounts(node.getText());
			        	  }
			        	   
			          }  
			    		} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 将字符串转为XML  
				  
				  if("Success".equals(smsresult.getReturnstatus()))		{
					  sms.setStatus(1);
				  }
				  else
					  sms.setStatus(2);
				  try{
					  updateSMSByID(sms);
				  }
				  catch(Exception ex){
					  System.out.println(ex.getMessage());
				  }
			}
					

							
	}
}
