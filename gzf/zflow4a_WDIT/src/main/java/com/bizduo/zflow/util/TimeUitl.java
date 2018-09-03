package com.bizduo.zflow.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class TimeUitl {

	//dateStr 时间格式
	public static Date returnDateFormat(String dateStr,String format){
		if(format==null||format.trim().equals(""))
			format="yyyy-MM-dd";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(dateStr); 
			return date;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//dateStr 时间格式
	public static String dateFormat(String dateStr,String format){
		if(format==null||format.trim().equals(""))
			format="yyyy-MM-dd";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(dateStr);
			return date.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 计算两个日期之间的天数
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }   
	public static Date getCurrentYearFirst(){
		
        Date date = new Date();
        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyy");//可以方便地修改日期格式     
        String  years  = dateFormat.format(date);
        
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.YEAR,Integer.valueOf(years).intValue());//设为当前年
        lastDate.set(Calendar.MONTH,1);//设为1月
        lastDate.set(Calendar.DATE,1);//设为1号 
        lastDate.set(Calendar.HOUR,1);//设为1号 
        lastDate.set(Calendar.MINUTE,1);//设为1号 
        lastDate.set(Calendar.SECOND,1);//设为1号 
        return lastDate.getTime();
        
	}
	
	public static Date getDate(int year,int month,int day){		
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.YEAR,year);//设为当前年
        lastDate.set(Calendar.MONTH,month);//设为1月
        lastDate.set(Calendar.DATE,day);//设为1号 
        lastDate.set(Calendar.HOUR_OF_DAY,0);//设为1号 
        lastDate.set(Calendar.MINUTE,0);//设为1号 
        lastDate.set(Calendar.SECOND,0);//设为1号 
        return lastDate.getTime();       
	}
    /**
     * 返回给定日期前面8天日期,时分秒都是0
     * @param d 日期(要求是周一，否则不能返回正好前一周）
     * @return 8元素的日期数组,其中Date[7]和d是同一天
     */
    public static Date[] getInWeekToReport(Date d){
		Date[] dateArray = new Date[8];
		d = TimeUitl.getQueryBeginTime(d.getTime());
		Calendar  c=Calendar.getInstance();   
		c.setTime(d);
		Date d9 = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		Date d8 = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		Date d7 = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		Date d6 = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		Date d5 = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		Date d4 = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		Date d3 = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		Date d2 = c.getTime();
		dateArray[7] = d9;
		dateArray[6] = d8;
		dateArray[5] = d7;
		dateArray[4] = d6;
		dateArray[3] = d5;
		dateArray[2] = d4;
		dateArray[1] = d3;
		dateArray[0] = d2;
		return dateArray; 
	}
	public static Date getTimeSecondBefore(int year,int month,int day){		
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.YEAR,year);//设为当前年
        lastDate.set(Calendar.MONTH,month);//设为1月
        lastDate.set(Calendar.DATE,day);//设为1号 
        lastDate.set(Calendar.HOUR_OF_DAY,0);//设为1号 
        lastDate.set(Calendar.MINUTE,0);//设为1号 
        lastDate.set(Calendar.SECOND,0);//设为1号 
        lastDate.add(Calendar.SECOND, -1);
        return lastDate.getTime();       
	}
	
	
	//将时间转化为当天的起始时间（用作查询条件中的起始时间） (2011-12-12 0:0:0)
	public  static Date getQueryBeginTime(Long beginTime){
		 Date de= new Date(beginTime);
		 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd");
		 String data=dataFm.format(de);
		 Date startDate=java.sql.Date.valueOf(data);
		 Calendar  calendar=Calendar.getInstance();   
		 calendar.setTime(startDate);   
		 calendar.add(Calendar.DATE,0); 
		 startDate=calendar.getTime();
		 return startDate;
	}
	//Long 时间格式化
	public static String getTimeLongTime(Long timeLong,String format) throws ParseException{ 
			Date de= new Date(timeLong);
			 SimpleDateFormat dataFm=new SimpleDateFormat(format);
			 String data=dataFm.format(de); 
			 return data; 
	}
	//将查询条件中的开始时间——格式化，在server里小于等于
	public  static Date getQuerystartTime(Long startTime){
		 Date de= new Date(startTime);
		 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd");
		 String data=dataFm.format(de);
		 Date endDate=java.sql.Date.valueOf(data);
		 return endDate;
	}
	//将查询条件中的截止时间——加一天，在server里小于不等于
	
	//将时间转化为当天的结束时间 （用作查询条件中的截止时间）
	public  static Date getQueryEndTime(Long endTime){
		 Date de= new Date(endTime);
		 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd");
		 String data=dataFm.format(de);
		 Date endDate=java.sql.Date.valueOf(data);
		 Calendar  calendar=Calendar.getInstance();   
		 calendar.setTime(endDate);   
		 calendar.add(Calendar.DATE,1); 
		 endDate=calendar.getTime();
		 Long end=endDate.getTime();
		 return new Date(end);
	}
	
     public static List<Date> getDateByHourInterval(Long start,Long end){
		
		List<Date> d = new ArrayList<Date>();
		
		long dayInterval = (end-start)/(1000*60*60*24);
		int hourInterval = DataUtil.getHourInterval(dayInterval);
		Calendar c = Calendar.getInstance();
		Date st = new Date(start);
		d.add(st);
		Date endDate = new Date(end);
		while(st.before(endDate)){
			c.setTime(st);
			c.add(Calendar.HOUR_OF_DAY, hourInterval);
			st = c.getTime();
			d.add(st);
		}
		int length = d.size(); 
		d.remove(length-1);
		d.remove(length-2);
		d.add(endDate);
		return d;
	}
     
    /**
     * 是否周一
     * @param d
     * @return
     */
    public static boolean isMonday(Date d){
    	
    	Calendar  calendar=Calendar.getInstance();   
		calendar.setTime(d); 
		if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){//判断是否为周一
			return true;
		}
		return false;
	}
    
    /**
     * 是否某个月的第一天
     * @param d
     * @return
     */
    public static boolean isFirstDayOfMonth(Date d){
    	Calendar  calendar=Calendar.getInstance();   
		calendar.setTime(d); 
		if(calendar.get(Calendar.DATE)==calendar.getActualMinimum(Calendar.DAY_OF_MONTH)){
			return true;
		}
		return false;
	}
	/**
	 * 将日期转化为"yyyy-MM-dd"
	 * @param date
	 * @return
	 */
    public static String getDatePart(Date date,String formatStr){
    	SimpleDateFormat dataFm=new SimpleDateFormat(formatStr);
		 String data=dataFm.format(date);
		 return data;
	}
    
    
    /**
     * 返回给定日期前面7天日期，依次放在数组中
     * @param d 日期
     * @return
     */
    public static Date[] getInWeek(Date d){
		Date[] dateArray = new Date[7];
		d = TimeUitl.getQueryBeginTime(d.getTime());
		Calendar  calendar=Calendar.getInstance();   
		calendar.setTime(d);   
		calendar.add(Calendar.HOUR,8);//08:00:00 当天
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		Date d7 = calendar.getTime();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		Date d6 = calendar.getTime();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		Date d5 = calendar.getTime();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		Date d4 = calendar.getTime();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		Date d3 = calendar.getTime();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		Date d2 = calendar.getTime();
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		Date d1 = calendar.getTime();
		dateArray[6] = d7;
		dateArray[5] = d6;
		dateArray[4] = d5;
		dateArray[3] = d4;
		dateArray[2] = d3;
		dateArray[1] = d2;
		dateArray[0] = d1;
		return dateArray; 
	}
}
