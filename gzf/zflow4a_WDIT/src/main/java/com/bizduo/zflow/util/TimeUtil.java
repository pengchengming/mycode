package com.bizduo.zflow.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TimeUtil {
	/**
	 * 日期转成字符串
	 * @param d
	 * @return
	 */
	public static String date2Str(Date d){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  sf.format(d);
	}
	public static String date2Str(Date d,String format){
		if(format!=null&&!format.equals("")){
			SimpleDateFormat sf = new SimpleDateFormat(format);
			return  sf.format(d);
		}else {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			return  sf.format(d);	
		}
	}
	/**
	 * 时间a是不是在时间b前分钟（包含）
	 * @param a
	 * @param b
	 * @param x
	 * @return 是/否
	 */
	public static boolean isXMinuteBefore(Date a, Date b, int x){
		Calendar c =  Calendar.getInstance();
		c.setTime(a);
		c.add(Calendar.MINUTE, x);
		if(c.getTimeInMillis() <= b.getTime())
			return true;
		else
			return false;
	}
	/**
	 * 返回时间d一刻钟后的时间点
	 * @param d
	 * @return
	 */
	public static Date plus15Minute(Date d){
		Calendar c =  Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MINUTE, 15);
		Date nextTime = c.getTime();
		return nextTime;
	}
	/**
	 * 取时间d前后second秒的两个时间
	 * @param d
	 * @param second
	 * @return
	 */
	public static Date[] timesAllow(Date d,int second){
		Date[] times = new Date[2];
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.SECOND, -second);
		times[0] = c.getTime();
		c.setTime(d);
		c.add(Calendar.SECOND, second);
		times[1] = c.getTime();
		return times;
	}
	/**
	 * 是否是统计时间点，目前是0点
	 * @param time
	 * @return
	 */
	public static boolean isSTTimePoint(Date time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String s = sdf.format(time);
	    if("00:00:00".equals(s.substring(11,19)))
	    	return true;
	    else
	    	return false;
	}

	/** 
	 * 根据开始时间、结束时间得到两个时间段内所有的日期 
	 * @param start 开始日期 
	 * @param end   结束日期 
	 * @param calendarType  类型 
	 * @return  两个日期之间的日期 
	 */  
	public static Date[] getDateArrays(Date start, Date end, int calendarType) {  
	  ArrayList<Date> ret = new ArrayList<Date>();  
	  Calendar calendar = Calendar.getInstance();  
	  calendar.setTime(start);  
	  Date tmpDate = calendar.getTime();  
	  long endTime = end.getTime();  
	  while (tmpDate.before(end) || tmpDate.getTime() < endTime) {  
	    ret.add(calendar.getTime());  
	    calendar.add(calendarType, 1);  
	    tmpDate = calendar.getTime();  
	  }  
	  Date[] dates = new Date[ret.size()];  
	  return (Date[]) ret.toArray(dates);  
	} 
	
	
	/**
	 * 通过指定年月日构造日期
	 * @param year 年
	 * @param month 月（从0开始即1月份输入0）
	 * @param day 日（从1开始）
	 * @return 日期（0时0分0秒）
	 */
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
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
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
	/**
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date getNextDay(Date date,int amount){		
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, amount);
        return c.getTime();       
	}
	public static Date subOneDay(Date date){
		Calendar calendar = Calendar.getInstance();   
		calendar.setTime(date);   
		calendar.add(Calendar.DATE, -1); 
		return calendar.getTime();
	}
	public static Date subTwoDay(Date date) {
		Calendar calendar = Calendar.getInstance();   
		calendar.setTime(date);   
		calendar.add(Calendar.DATE, -2); 
		return calendar.getTime();
	}
	public static Date subOneWeek(Date date) {
		Calendar calendar = Calendar.getInstance();   
		calendar.setTime(date);   
		calendar.add(Calendar.DATE, -7); 
		return calendar.getTime();
	}
	public static Date addOneDay(Date date,Integer i){
		Calendar calendar = Calendar.getInstance();   
		calendar.setTime(date);   
		calendar.add(Calendar.DATE, i); 
		return calendar.getTime();
	}
	public static Date sub15Minutes(Date date){
		Long l = date.getTime() - 1000*60*15L;
		return new Date(l);
	}
	public static Date addOneYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, 1);
		return calendar.getTime();
	}
	public static Date addOneMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH , 1);
		return calendar.getTime();
	}
	/**
	 * 取d的前一天零时
	 * @param d
	 * @return
	 */
	public  static Date getYesterdayEndTime(Long d){
		 Date date = new Date(d);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String sd = sdf.format(date);
		 date = java.sql.Date.valueOf(sd);
		 Calendar calendar = Calendar.getInstance();   
		 calendar.setTime(date);   
		 calendar.add(Calendar.DATE, -1); 
		 date = calendar.getTime();
		 return date;
	}
	/**
	 * 系统初始化时间
	 * @return
	 * @throws ParseException
	 */
	public static Date getSysStart() throws ParseException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Date d = sdf.parse("2010-01-01");
		 return d;
	}
	/**
	 * 取d的零时
	 * @param d
	 * @return
	 */
	public  static Date getDayStartTime(Long d){
		 Date date = new Date(d);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String sd = sdf.format(date);
		 date = java.sql.Date.valueOf(sd);
		 Calendar calendar = Calendar.getInstance();   
		 calendar.setTime(date);   
		 calendar.add(Calendar.DATE, 0); 
		 date = calendar.getTime();
		 return date;
	}
	/**
	 * 取d的后一天零时
	 * @param d
	 * @return
	 */
	public  static Date getTomorrow(Long d){
		 Date date = new Date(d);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String sd = sdf.format(date);
		 date = java.sql.Date.valueOf(sd);
		 Calendar calendar = Calendar.getInstance();   
		 calendar.setTime(date);   
		 calendar.add(Calendar.DATE, 1); 
		 date = calendar.getTime();
		 return date;
	}
     
    /**
     * @param d 待检日期
     * @return 是否周一
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
     * 返回给定日期前面7天日期，依次放在数组中
     * @param d 日期(要求是周一，否则不能返回正好前一周）
     * @return 7元素的日期数组,其中Date[6]是d的前一天0点0分0秒
     */
    public static Date[] getInWeek(Date d){
		Date[] date = new Date[7];
		d = TimeUtil.getDayStartTime(d.getTime());
		Calendar c = Calendar.getInstance();   
		c.setTime(d);
		c.add(Calendar.DAY_OF_WEEK, -1);
		date[6] = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		date[5] = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		date[4] = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		date[3] = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		date[2] = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		date[1] = c.getTime();
		c.add(Calendar.DAY_OF_WEEK, -1);
		date[0] = c.getTime();
		return date; 
	}
   
    /**
     * 返回给定日期所在周的周一开始时间
     * @param d 可以任意的时间
     * @return 周一0点0分0秒
     */
    public static Date getWeekStartTime(Long d){
    	Date thedayBegining = TimeUtil.getDayStartTime(d);
    	Calendar c = Calendar.getInstance();
		c.setTime(thedayBegining);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);//得到所选时间 是星期几
		if(dayOfWeek >= 2){//星期一到六  进行设置
			c.set(Calendar.DAY_OF_WEEK, 2);
			return  c.getTime();
		}else{//周日，往前一周
			c.add(Calendar.DAY_OF_WEEK, -6);
			return c.getTime();
		}
	}
    /**
     * 返回给定日期所在周的周日结束时间
     * @param d 可以任意的时间
     * @return 周日24点0分0秒
     */
    public static Date getWeekEndTime(Long d){
    	Date theDayEndTime = TimeUitl.getQueryEndTime(d);
    	Calendar c = Calendar.getInstance();
		c.setTime(theDayEndTime);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);//得到所选时间 是星期几
		if(dayOfWeek <= 2){//星期日和一  本周
			c.set(Calendar.DAY_OF_WEEK, 2);
			return  c.getTime();
		}else{//周二到六，往后一周
			c.set(Calendar.DAY_OF_WEEK, 2);
			c.add(Calendar.DAY_OF_WEEK, 7);
			return c.getTime();
		}
	}
    		       
    /** 
     * 取得当月天数 
     * */  
    public static int getCurrentMonthLastDay(){  
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    }  
    /** 
     * 得到指定月的天数 
     * */  
    public static int getMonthLastDay(int year, int month){  
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    } 
    public static Date getNow(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String date = sdf.format(new Date());
	    Date d = java.sql.Date.valueOf(date);
	    Calendar calendar = Calendar.getInstance();   
	    calendar.setTime(d);   
	    calendar.add(Calendar.DATE, 0); 
	    return calendar.getTime();
    }
    /**
     * 返回当前年月日
     * @return
     */
    public static String getNowDate(){  
        Date date = new Date();  
        String nowDate = new SimpleDateFormat("yyyy年MM月dd日").format(date);  
        return nowDate;  
    }  
    public static int getWeek(){
    	Date date = new Date();
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int week_of_year = calendar.get(Calendar.WEEK_OF_YEAR);  //这周，在一年内是第几周;
    	return week_of_year;
    }
    public static int getWeek(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int week_of_year = calendar.get(Calendar.WEEK_OF_YEAR);  //这周，在一年内是第几周;
    	return week_of_year;
    }
    /**
     * 返回当前年份
     * @return
     */
    public static int getYear(){  
        Date date = new Date();  
        String year = new SimpleDateFormat("yyyy").format(date);  
        return Integer.parseInt(year);  
    }  
    /**
     * 返回当前年份
     * @return
     */
    public static int getYear(Date date){   
        String year = new SimpleDateFormat("yyyy").format(date);  
        return Integer.parseInt(year);  
    }  
    /**
     * 返回当前月份(1-12 not 0-11)
     * @return
     */
    public static int getMonth(){  
        Date date = new Date();  
        String month = new SimpleDateFormat("MM").format(date);  
        return Integer.parseInt(month);  
    }  
    /**
     * 返回月份(1-12 not 0-11)
     * @param date
     * @return
     */
    public static int getMonth(Date date){  
        String month = new SimpleDateFormat("MM").format(date);  
        return Integer.parseInt(month);  
    } 
	public static Integer getDay(Date date) {
		String day = new SimpleDateFormat("dd").format(date);  
        return Integer.parseInt(day);
	}
    /**
     * 判断闰年  
     * @param year
     * @return
     */
    public static boolean isLeap(int year){  
        if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0))  
            return true;  
        else  
            return false;  
    }  
    /**
     * 返回当月天数 
     * @param year
     * @param month
     * @return
     */
    public static int getDays(int year, int month){  
        int days;  
        int FebDay = 28;  
        if (isLeap(year))  
            FebDay = 29;  
        switch (month)  
        {  
            case 1:  
            case 3:  
            case 5:  
            case 7:  
            case 8:  
            case 10:  
            case 12:  
                days = 31;  
                break;  
            case 4:  
            case 6:  
            case 9:  
            case 11:  
                days = 30;  
                break;  
            case 2:  
                days = FebDay;  
                break;  
            default:  
                days = 0;  
                break;  
        }  
        return days;  
    }  

//    /**
//     * 计算自定义、周、日数据报表查询的开始和结束时间
//     * @param start
//     * @param end
//     * @param reportType
//     * @return
//     * @throws ParseException
//     */
//    public static Date[] getQueryDateByParam(Long start, Long end, int reportType) throws ParseException{
//    	Date[] dates = new Date[2];
//    	if(reportType == ReportItem.REPORTFORM.TYPE_CUSTOMIZE.ordinal()){//自定义报表
//			dates[0] = TimeUtil.getDayStartTime(start);
//			dates[1] = TimeUtil.getDayStartTime(end);
//    	}else if(reportType == ReportItem.REPORTFORM.TYPE_WEEK.ordinal()){//周报
//    		//周报的时候要进行处理
//			dates[0] =  TimeUtil.getWeekStartTime(end);
//			dates[1] =  TimeUtil.getWeekEndTime(end);
//    	}else if(reportType == ReportItem.REPORTFORM.TYPE_DAY.ordinal()){//日报
//    		dates[0] =  TimeUtil.getYesterdayEndTime(end);
//			dates[1] =  TimeUtil.getDayStartTime(end);
//    	}else if(reportType == ReportItem.REPORTFORM.TYPE_YEAR.ordinal()){
//    		dates[0] = TimeUtil.getDayStartTime(end);
//    		dates[1] = TimeUtil.addOneYear(dates[0]);
//    	}
//    	return dates;
//    }
	
	public static Date subOneMonth(Date date) {
		Calendar calendar = Calendar.getInstance();   
		calendar.setTime(date);  
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}
	public static Date subOneYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -1);
		return calendar.getTime();
	}
	public static int getHour(Date date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null != date)
			date = sdf.parse(sdf.format(date));
		else
			date = sdf.parse(sdf.format(new Date()));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	/**
	 * 检查当前时间点是否是整点
	 * @param date
	 * @return
	 */
	public static boolean checkDatePoint(Date date){
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		if((0 == gc.get(Calendar.MINUTE)) && (0 == gc.get(Calendar.SECOND)))
			return true;
		else 
			return false;
	}

	public static Date getMonthlyDay(Date date, Integer monthlyDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, monthlyDay);
		return calendar.getTime();
	}
}
