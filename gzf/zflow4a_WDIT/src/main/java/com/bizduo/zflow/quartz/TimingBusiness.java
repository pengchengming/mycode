package com.bizduo.zflow.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bizduo.zflow.domain.dm.CCMNotice;
import com.bizduo.zflow.domain.dm.CCMPush;
import com.bizduo.zflow.domain.dm.MailMessage;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.dm.IMailMessageService;
import com.bizduo.zflow.service.dm.INoticeService;
import com.bizduo.zflow.service.dm.IPushService;
import com.bizduo.zflow.util.ExecutionSql;
import com.bizduo.zflow.util.TimeUitl;
import com.bizduo.zflow.util.ccm.HttpUrlConnectionUtil;
import com.bizduo.zflow.util.ccm.MailStatus;
import com.bizduo.zflow.util.ccm.PushUtil;

public class TimingBusiness {
	
	protected final Log log = LogFactory.getLog(TimingBusiness.class);  
	@Autowired
	private IMailMessageService mailMessageService; 	 

	@Autowired
	private INoticeService noticeService;
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	@Autowired
	private IPushService pushService;
	 
	protected static ResourceBundle timingBusiness = ResourceBundle.getBundle("timingBusiness", Locale.getDefault()); 

	/**
	 * 邮件和提醒，推送  5分钟一次
	 */
	public void mailSend(){
		//发送邮件
		int status=0;
		List<MailMessage>  mailMessageList=mailMessageService.getMailMessageBy(null,status); 
		for (MailMessage mailMessage : mailMessageList) {
			try {
				this.mailMessageService.updateResendMail(mailMessage);
				//推送 数据
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
 		ccmPush();
	} 
	/**
	 * 推送 数据
	*/
	private void ccmPush(){
		List<CCMPush> pushList=this.pushService.getPushByEnabled(true);
		if(pushList!=null){
			for (CCMPush push : pushList) {
				if(true){
					try {
						String httpPush = timingBusiness.getString("httpPush");
						String url=httpPush+ PushUtil.pushUri(push.getPushType());
						//String url="http://localhost:8081/mongodb/testTem.do";
						StringBuffer params = new StringBuffer(); 
				        // 表单参数与get形式一样
						String date= push.getDate();
						if(date!=null){
							//"2014-06-06 00:00:00.0";
							int i= date.indexOf("00:00:00");
							if(i>0){
								date= date.substring(0, i-1)+" 00:00:00";
							}		
						}
						//
				        params.append("taskId").append("=").append(push.getTaskId())
				              .append("&officeName").append("=").append(push.getOfficeName())
				              .append("&date").append("=").append(TimeUitl.getDatePart(push.getCreateDate(),"yyyy-MM-dd HH:mm:ss")) 
				              .append("&clientName").append("=").append(push.getClientName())
				              .append("&ccName").append("=").append(push.getCcName())
				              .append("&ppName").append("=").append(push.getPpName())
				              .append("&infoSourceName").append("=").append(push.getInfoSourceName())
				              .append("&comment").append("=").append(push.getComment());
				        params.append(PushUtil.pushParams(push));
				              /*.append("releatedJsonArray").append("=").append(push.getReleatedJsonArray())
				              .append("unReleatedJsonArray").append("=").append(push.getUnReleatedJsonArray())
				              .append("allPPJsonArray").append("=").append(push.getAllPPJsonArray())
				              .append("acceptJsonArray").append("=").append(push.getAcceptJsonArray());
				        */
				        String resultMsg= HttpUrlConnectionUtil.readContentFromPOST(url, params.toString());
				        JSONObject resultObj=new JSONObject(resultMsg);
				        //result
				        if(resultObj.has("isSuccess")){
				        	String isSuccess =resultObj.getString("isSuccess");
				        	if(isSuccess!=null&&isSuccess.trim().equals("true")){
				        		push.setEnabled(false); 
				        		push.setErrorMsg(resultMsg);
								this.pushService.update(push);
				        	}else {
				        		System.out.println("error==="+resultMsg);
				        		push.setErrorMsg(resultMsg);
								this.pushService.update(push);
				        	}
				        }
						
					} catch (Exception e) { 
						e.printStackTrace();
					}
				}
			}
		} 
	}
	//提醒，创建数据
	public void remindSend(){
		remindAccept();
		remindReply(); 
	}
  /**
   * 定时提醒 accept
   */
	private void remindAccept(){
		//2天提醒一次 
		/*String sql= "select sendstatus ,dataid  from (  select max(sendStatus)  as sendstatus ,dataid   from mailmessage       group by dataid  ) as a  ";
		String sql23=sql+" where (sendstatus="+MailStatus.PPSELECTSTATUS+" or sendstatus="+MailStatus.PPUNSELECTSTATUS+") ";
		 sendRemind(sql23,MailStatus.MAILCOUNTONESTATUS);
		 
		 String sql4=sql+" where sendstatus="+ MailStatus.MAILCOUNTONESTATUS;
		 sendRemind(sql4,MailStatus.MAILCOUNTTWOESTATUS);
		 
		 String sql7=sql+" where sendstatus="+ MailStatus.CLAIMFEEDBACKSTATUS;
		 sendRemind(sql7,MailStatus.FEEDBACKTWOSTATUS);
		 
		System.out.println(1111111111);
		*/
		//改变 notice remind 
		//taLeader 执行 approve 两天后
		//两天提醒一次，连续提醒两次
		 int remindCount=2;
		List<CCMNotice> noticeList= noticeService.getNoticeByTypeRemind(MailStatus.PPGROUPSELECT, remindCount);
		Long taskId=0l;
		Map<String ,List<CCMNotice>> acceptNoticeMap=new HashMap<String ,List<CCMNotice>>();
		for (int i = 0; i < noticeList.size(); i++) {
			CCMNotice notice=noticeList.get(i); 
			try {
//			 int dayin= TimeUitl.daysBetween(notice.getCreateDate(), new Date());
			 //不是今天，并且是2天的倍数
				int	dayin=2;
			 if(dayin!=0&&dayin%2==0){
				//notice.setRemindCount(notice.getRemindCount()+1); 
				//this.noticeService.update(notice);
				 if(taskId!=notice.getTaskId()){
					 taskId=notice.getTaskId();
					 int updateRemind= notice.getRemindCount()+1;
					String updateSql="update ccm_notice set remindCount="+updateRemind+" where taskId='"+notice.getTaskId()+"' and dataId='"+notice.getDataId()+"' ";
					ExecutionSql.executionsql(null, "execution", jdbcTemplate, updateSql, null);
					//
					if(acceptNoticeMap.get(notice.getDataId().toString())==null){
						List<CCMNotice> noticeMapList=new ArrayList<CCMNotice>();
						noticeMapList.add(notice);
						acceptNoticeMap.put(notice.getDataId().toString(), noticeMapList);
					}else {
						List<CCMNotice> noticeMapList=acceptNoticeMap.get(notice.getDataId().toString());
						noticeMapList.add(notice);
					}
				 } 
			 } 
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
		//推送
		if(acceptNoticeMap!=null){
			for (Iterator<String> it = acceptNoticeMap.keySet().iterator(); it .hasNext();) {  
				String dataId = it.next();
				List<CCMNotice> noticeMapList=acceptNoticeMap.get(dataId);
				JSONArray releatedJsonArray=new JSONArray();
				int index=0;
				if(noticeMapList!=null&&noticeMapList.size()>0){
					try {
						for (CCMNotice ccmNotice : noticeMapList) {
							
							JSONObject obj= new JSONObject();
							obj.put("username", ccmNotice.getReadUser().getUsername());
							obj.put("noticeId", ccmNotice.getId());
							String isoDevic=""; 
							User user= ccmNotice.getReadUser();
							if(user.getIosDevic()!=null)
								isoDevic=user.getIosDevic();
							obj.put("ios_device",isoDevic); 
							releatedJsonArray.put(index, obj);
							index++;
						}
						CCMNotice ccmNotice =noticeMapList.get(0);
						//ClientCallUtil.savePush(dataId, ccmNotice.getTaskId().toString(),  clientCallService, pushService,null,releatedJsonArray.toString(), "","","",CCMPush.REMINDACCEPT,null);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 定时提醒 reply
	 */
	private void remindReply(){ 
		//accept 两天后没有被reply
		//每天提醒一次，连续10次
		int remindCount=10;
		List<CCMNotice>  noticeList= noticeService.getNoticeByTypeRemind(MailStatus.MAILCOUNTTWOESTATUS, remindCount); 
			Map<String ,List<CCMNotice>> replyNoticeMap=new HashMap<String ,List<CCMNotice>>();
			if(noticeList!=null){
				for (int i = 0; i < noticeList.size(); i++) {
					CCMNotice notice=noticeList.get(i); 
					try {
//					 int dayin= TimeUitl.daysBetween(notice.getCreateDate(), new Date());
					 //不是今天，并且是2天的倍数
//					 boolean isremind=false;
//					 if(dayin!=0){ //第一次
//						 if(notice.getRemindCount()==0)isremind=true;
//						 else {
//							 //第二次
//							 if(dayin>notice.getRemindCount()&&dayin!=(notice.getRemindCount()+1))
//								 isremind=true;
//						 } 
//					 }
						 boolean isremind=true;
					 if(isremind){
							notice.setRemindCount(notice.getRemindCount()+1); 
							notice =this.noticeService.update(notice);
							//
							if(replyNoticeMap.get(notice.getDataId().toString())==null){
								List<CCMNotice> noticeMapList=new ArrayList<CCMNotice>();
								noticeMapList.add(notice);
								replyNoticeMap.put(notice.getDataId().toString(), noticeMapList);
							}else {
								List<CCMNotice> noticeMapList=replyNoticeMap.get(notice.getDataId().toString());
								noticeMapList.add(notice);
							}
					 } 
					} catch (Exception e) { 
						e.printStackTrace();
					}
				}
				//推送
				if(replyNoticeMap!=null){
					for (Iterator<String> it = replyNoticeMap.keySet().iterator(); it .hasNext();) {  
						String dataId = it.next();
						List<CCMNotice> noticeMapList=replyNoticeMap.get(dataId);
						JSONArray acceptedPPJsonArray=new JSONArray();
						int index=0;
						if(noticeMapList!=null&&noticeMapList.size()>0){
							try {
								for (CCMNotice ccmNotice : noticeMapList) {
									
									JSONObject obj= new JSONObject();
									obj.put("username", ccmNotice.getReadUser().getUsername());
									obj.put("noticeId", ccmNotice.getId());  
									User user= ccmNotice.getReadUser();
									String isoDevic="";
									if(user.getIosDevic()!=null)
										isoDevic=user.getIosDevic();
									obj.put("ios_device",isoDevic); 
									acceptedPPJsonArray.put(index, obj);
									index++;
								}
								CCMNotice ccmNotice =noticeMapList.get(0);
								//ClientCallUtil.savePush(dataId, ccmNotice.getTaskId().toString(),  clientCallService, pushService,null, "", "",acceptedPPJsonArray.toString(),"",CCMPush.REMINDREPLY,null);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
			} 
	}


	@SuppressWarnings("unused")
	private void sendRemind(String sql ,int mailStatus ){ 
		List<String> list=new ArrayList<String>();
		list.add("sendstatus");
		list.add("dataId");  
		JSONArray jsonArray= ExecutionSql.querysqlJSONArray(jdbcTemplate, sql, list);
		if(jsonArray.length() > 0) { 
			try {
				JSONObject tempObj = jsonArray.getJSONObject(0);   
				Long dataid=Long.parseLong(tempObj.get("dataId").toString());
				List<MailMessage> mailMessageList=  this.mailMessageService.findMailMessageByDataId(dataid);
				for (MailMessage mailMessage1 : mailMessageList) { 
					
					MailMessage mailMessage=new MailMessage(mailMessage1.getToUser(), " to p/p 反馈", "请 p/p 反馈", dataid ,mailMessage1.getTaskId(), 0,2,mailMessage1.getFormcode(), null,"ppGroup",mailStatus);
					mailMessage=mailMessageService.saveMailMessage(mailMessage);
					mailMessageService.updateResendMail(mailMessage);
					 
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
	}
}
