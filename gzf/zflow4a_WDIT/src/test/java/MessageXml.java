import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;


public class MessageXml {

	   private void  manageMessage(String requestStr )throws ServletException,IOException{  
           
           String responseStr;  
            
           try {  
                XMLSerializer xmlSerializer=new XMLSerializer();  
                JSONObject jsonObject =(JSONObject) xmlSerializer.read(requestStr);  
                String event =jsonObject.getString("Event");  
                String msgtype =jsonObject.getString("MsgType");  
                if("CLICK".equals(event) && "event".equals(msgtype)){ //菜单click事件  
                    String eventkey =jsonObject.getString("EventKey");  
                    if("hytd_001".equals(eventkey)){ // hytd_001 这是好友团队按钮的标志值  
                        jsonObject.put("Content", "欢迎使用好友团队菜单click按钮.");  
                    }  
                     
                }  
                responseStr =creatRevertText(jsonObject);//创建XML  
               /* OutputStream os =response.getOutputStream();  
                os.write(responseStr.getBytes("UTF-8"));*/  
           }   catch (Exception e) {  
               e.printStackTrace();  
           }  
             
   }  
	   

	    private String creatRevertText(JSONObject jsonObject){  
	            StringBuffer revert =new StringBuffer();  
	            revert.append("<xml>");  
	            revert.append("<ToUserName><![CDATA["+jsonObject.get("ToUserName")+"]]></ToUserName>");  
	            revert.append("<FromUserName><![CDATA["+jsonObject.get("FromUserName")+"]]></FromUserName>");  
	            revert.append("<CreateTime>"+jsonObject.get("CreateTime")+"</CreateTime>");  
	            revert.append("<MsgType><![CDATA[text]]></MsgType>");  
	            revert.append("<Content><![CDATA["+jsonObject.get("Content")+"]]></Content>");  
	            revert.append("<FuncFlag>0</FuncFlag>");  
	            revert.append("</xml>");  
	            return revert.toString();  
	        }  
	public static void main(String[] args) {
		MessageXml mesg=new MessageXml();
		//String mesgStr="<xml><ToUserName><![CDATA[gh_3c0af0408175]]></ToUserName><FromUserName><![CDATA[o1kHgjjw-aJWLA0xcvw9nDLkJ894]]></FromUserName><CreateTime>1486290506</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[1111]]></Content><MsgId>6383569116068155610</MsgId></xml>";		
		String  mesgStr="<xml><ToUserName><![CDATA[gh_3c0af0408175]]></ToUserName><FromUserName><![CDATA[o1kHgjjw-aJWLA0xcvw9nDLkJ894]]></FromUserName><CreateTime>1486290476</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[subscribe]]></Event><EventKey><![CDATA[]]></EventKey></xml>";
		try {
			mesg.manageMessage(mesgStr);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
