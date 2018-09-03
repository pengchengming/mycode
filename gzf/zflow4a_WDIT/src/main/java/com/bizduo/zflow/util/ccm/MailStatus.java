package com.bizduo.zflow.util.ccm;


public class MailStatus {
	/**  未发送  0*/
	public static int UNSENTSTATUS=0;
	/** 发送  1*/
	public static int SENDSTATUS =1;
	/** 填单完成 1 */
	public static int FILLSTATUS =1; 
	/** TALeader审批完成，选择的pp组   ==2*/
	public static int PPSELECTSTATUS =2;
	/**TALeader审批完成，非选择的pp组 ==3**/
	public static int PPUNSELECTSTATUS =3;
	/**2天  选择的pp组,第一次提醒 ==4**/
	public static int MAILCOUNTONESTATUS =4;
	/**2天之后的第二次提醒  ==5**/
	public static int MAILCOUNTTWOESTATUS =5;
	/**认领之后， 系统的通知邮件，选中pp组中未认领的人  ==6**/
	public static int UNCLAIMSTATUS =6;
	/**认领之后，认领任务的人 ==7**/
	public static int CLAIMFEEDBACKSTATUS =7;
	/**2天给一次  ==8**/
	public static int FEEDBACKTWOSTATUS =8;
	
	////notice
	/**未选中PPGroup的通知 1 ***/
	public static int UNPPGROUPSELECT=1; 
	/****选中PPGroup的通知 2****/
	public static int PPGROUPSELECT=2; 
	/***未认领的通知   301 ***/
	public static int UNPPGROUPCLAIMINC=301; 
	/***未认领的通知   302 ***/
	public static int UNPPGROUPCLAIMACC=302;
	/**help 通知 除自己的所有 4***/
	public static int PPGROUPHELP=4;
	/**待反馈的 通知  自己 5*******/
	public static int PPGROUPFEEDBACK=5; //
	/**反馈通知  所有PPGroup  除自己 6*****/
	public static int UNPPGROUPFEEDBACK=6; 
	//是否读取
	/**未读  0**/
	public static int UNREAD=0; 
	/**已取  1**/
	public static int READ=1;
}
