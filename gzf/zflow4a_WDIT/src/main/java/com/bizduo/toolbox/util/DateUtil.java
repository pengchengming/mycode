package com.bizduo.toolbox.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期Util
 * @author Administrator
 *
 */
public class DateUtil {

	/**
	 * 日期转换 
	 * @author Administrator
	 * @param time
	 * @param fmt:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatTime(Timestamp time,String fmt) {
		if (time == null) {
			return "";
		}
		SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
		return myFormat.format(time);
	}
	
	
	/**
	 * 获取系统当前时间（秒）
	 * @author Administrator
	 * @return
	 */
	public static Timestamp getTime() {
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String mystrdate = myFormat.format(calendar.getTime());
		return Timestamp.valueOf(mystrdate);
	}
	
	/**
	 * 获取当前日期(时间00:00:00)
	 * @author Administrator
	 * @return
	 */
	public static Timestamp getDateFirst(){
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Calendar calendar = Calendar.getInstance();
		String mystrdate = myFormat.format(calendar.getTime());
		return Timestamp.valueOf(mystrdate);
	}
	
	/**
	 * 获取当前日期(时间23:59:59)
	 * @author Administrator
	 * @return
	 */
	public static Timestamp getDateLast(){
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		Calendar calendar = Calendar.getInstance();
		String mystrdate = myFormat.format(calendar.getTime());
		return Timestamp.valueOf(mystrdate);
	}
	
	/**
	 * 获取当前日期
	 * @author Administrator
	 * @return
	 */
	public static Date getDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss 转换成Timestamp
	 * @author Administrator
	 * @param timeString
	 * @return
	 */
	public static Timestamp getTime(String timeString){
		return Timestamp.valueOf(timeString);
	}
	
	
	/**
	 * 自定义格式的字符串转换成日期
	 * @author Administrator
	 * @param timeString
	 * @param fmt
	 * @return
	 * @throws Exception
	 */
	public static Timestamp getTime(String timeString,String fmt) throws Exception{
		SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
		Date date= myFormat.parse(timeString);
		myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getTime(myFormat.format(date));
	}
	
	/**
	 * 格式化日期
	 * @author Administrator
	 * @param date
	 * @param fmt
	 * @return
	 * @throws Exception
	 */
	public static String formatDate(Date date,String fmt) throws Exception{
		if (date == null) {
			return "";
		}
		SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
		return myFormat.format(date);
	}
	
	/**
	 * 返回日期或者时间，如果传入的是日期，返回日期的00:00:00时间
	 * @author Administrator
	 * @param timeString
	 * @return
	 * @throws Exception
	 */
	public static Timestamp getDateFirst(String timeString) throws Exception{
		if (timeString == null || timeString.equals("")) {
			return null;
		}
		if (timeString.length() > 10) {
			return getTime(timeString, "yyyy-MM-dd HH:mm:ss");
		} else {
			return getTime(timeString, "yyyy-MM-dd");
		}
	}
	
	
	/**
	 * 返回日期或者时间，如果传入的是日期，返回日期的23:59:59时间
	 * @author Administrator
	 * @param timeString
	 * @return
	 * @throws Exception
	 */
	public static Timestamp getDateLast(String timeString) throws Exception{
		if (timeString == null || timeString.equals("")) {
			return null;
		}
		if (timeString.length() > 10) {
			return getTime(timeString, "yyyy-MM-dd HH:mm:ss");
		} else {
			return getTime(timeString +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
		}
	}
	
	/**
	 * 获取本周 周一时间，返回 格式yyyy-MM-dd 00:00:00
	 * @author Administrator
	 * @return
	 */
	public static Timestamp getMonday(){
		Calendar calendar= Calendar.getInstance(); 
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayofweek == 0)
			dayofweek = 7;
		calendar.add(Calendar.DATE, -dayofweek + 1);
		
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String mystrdate = myFormat.format(calendar.getTime());
		return Timestamp.valueOf(mystrdate);
	}
	
	
	/**
	 * 获取本周 周日 时间，返回格式yyyy-MM-dd 23:59:59
	 * @author Administrator
	 * @return
	 */
	public static Timestamp getSunday(){
		Calendar calendar= Calendar.getInstance(); 
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayofweek == 0)
			dayofweek = 7;
		calendar.add(Calendar.DATE, -dayofweek + 7);
		
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		String mystrdate = myFormat.format(calendar.getTime());
		return Timestamp.valueOf(mystrdate);
	}
	
	
	/**
	 * 增加天数
	 * @author Administrator
	 * @param time
	 * @param day
	 * @return
	 */
	public static Timestamp addDay(Timestamp time,Integer day){
		Timestamp time2=new Timestamp(time.getTime()+day*1000l*60*60*24);
		return time2;
	}
	
	/**
	 * 比较2个日期格式的字符串 
	 * @author Administrator
	 * @param str1  格式：yyyyMMdd
	 * @param str2 格式：yyyyMMdd
	 * @return
	 */
	public static Integer compareDate(String str1,String str2) throws Exception{

		return Integer.parseInt(str1)-Integer.parseInt(str2);
		
	}
	
	/**
	 * 2个时间的相差天数
	 * @author Administrator
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static Integer getDay(Timestamp time1,Timestamp time2){
		Long dayTime=(time1.getTime()-time2.getTime())/(1000*60*60*24);
		return dayTime.intValue();
	}
	
	/**
	 * 获取系统当前时间（分）
	 * @author Administrator
	 * @return
	 */
	public static String getMinute() {
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar calendar = Calendar.getInstance();
		return myFormat.format(calendar.getTime());
	}
}
