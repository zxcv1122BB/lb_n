package com.lb.sys.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	// String字符串类型转换成时间日期格式
	public static Date StringToDate(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date StringToDate2(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	// 将unix时间戳转换成时间日期格式
	public static Date TimeStamp2Date(String timestampString, String formats) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
		Date toDate = DateUtils.StringToDate(date);
		return toDate;
	}
	// 将unix时间戳转换成时间日期格式
	public static String getDateString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
	
	// 将unix时间戳转换成时间日期格式
	public static String getDateString(Date date,String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	//计算时间的差值,返回毫秒数
	public static Long getMillisecond(String timeString,Date end) {
		long t = -1;
		try {
			SimpleDateFormat smdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = smdf.parse(timeString);
			t = (end.getTime() - start.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	
	 /**  
     * 计算两个字符串日期格式之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */  
    public static int daysBetween(String smdate,String bdate){  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(StringToDate(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(StringToDate(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
        return Integer.parseInt(String.valueOf(between_days));     
    } 
    
   
   /**
    * 获取当前上周一与周日时间
    * @return 2017-10-10 
    */
   public static String getLastWeekMonday() {    
	     Calendar calendar1 = Calendar.getInstance();  
	     Calendar calendar2 = Calendar.getInstance();  
	     int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;  
	     int offset1 = 1 - dayOfWeek;  
	     int offset2 = 7 - dayOfWeek;  
	     calendar1.add(Calendar.DATE, offset1 - 7);  
	     calendar2.add(Calendar.DATE, offset2 - 7);
	     String lastBeginDate = getDateString(calendar1.getTime(),"yyyy-MM-dd");  
	     String lastEndDate = getDateString(calendar2.getTime(),"yyyy-MM-dd");  
	     return lastBeginDate + "," + lastEndDate;    
   }
   /** 
	 * 获取现在时间 
	 *  
	 */  
	public static Date getNowDate(Date currentTime) {  
	    try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			String dateString = formatter.format(currentTime);  
			Date currentTime_2 = formatter.parse(dateString);  
			return currentTime_2;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	    return null;
	}
	public static Date ObjectToDate(Object obj)
   {
	   Date date = null;
	   if(obj instanceof Date) {
		   date = (Date) obj;
	   }else if(obj instanceof String && !StringUtil.isBlank(obj)) {
		   try {
			   date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).parse(obj.toString());
		   } catch (ParseException e) {
				e.printStackTrace();
		   }
	   }else if(obj instanceof Long) {
		   date = new Date((Long)obj);
	   }
	   return date;
   }
	/**
	 * 将毫秒数转换成时分秒
	 * @param time
	 * @return
	 */
	public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00分00秒";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second)+"秒";
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                long days = second / 86400;  
                if(days>1) {
                	timeStr = days + "天" + unitFormat(hour) + "小时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
                }else {
                	timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分" + unitFormat(second)+"秒";
                }
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
	
    /**
     * 时间比较
     * @param sDate 时间区间-最小值
     * @param mDate 时间区间-最大值
     * @param formater 格式
     * @param date 需要比较的时间
     * @return
     */
    public static boolean getCompareDate(String sDate,String mDate,String formater,String date) {
 	   boolean bool = false;
 	   SimpleDateFormat sdf = new SimpleDateFormat(formater);
 	   try {
 		   Date s_date = sdf.parse(sDate);
 		   Date m_date = sdf.parse(mDate);
 		   Date c_date = sdf.parse(date);
 		   if(c_date.getTime()>=s_date.getTime() && c_date.getTime()<=m_date.getTime()) {
 			   bool= true;
 		   }
 		} catch (ParseException e) {
 			e.printStackTrace();
 		}
 	   return bool;
    }
}
