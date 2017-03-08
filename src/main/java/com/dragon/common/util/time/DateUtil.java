
package com.dragon.common.util.time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

	// static long now = System.currentTimeMillis();
	// public static Date CurrTime = new Date(now);

	public DateUtil() {
	}

	// 取系统时间时一定要用这个方法，否则日期可能不动
	public static Date getCurrDateTime() {
		return new Date(System.currentTimeMillis());
	}
	
	/**
	 * 取得当前系统时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrTime( String format) {
		Date date_time = new Date();
		return FormatDate(date_time, format);
	}
	
	/**
	 * 取得当前系统时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	/*public static String formatDateString( String dateStr,String originalFormat ,String targetFormat) {
		if(StringUtils.isBlank(dateStr)){
			return "";
		}
		return dateToString(stringToDate(dateStr, originalFormat), targetFormat);
	}*/

	/**
	 * 取得当前系统时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrTime() {
		Date date_time = new Date();
		return FormatDate(date_time, "yyyy-MM-dd HH:mm:ss");
	}
	
	
	

	public static String getCurrDate() {
		Date date_time = new Date();
		return FormatDate(date_time, "yyyy-MM-dd");
	}

	/**
	 * 取得日期的年份
	 * 
	 * @param date
	 *            日期
	 * @return yyyy 年份字符串
	 */
	public static String getYear(Date date) {
		return FormatDate(date, "yyyy");
	}

	/**
	 * 取得日期的月份
	 * 
	 * @param date
	 *            日期
	 * @return mm 月份字符串
	 */
	public static String getMonth(Date date) {
		return FormatDate(date, "MM");
	}

	/**
	 * 取得日期的天份
	 * 
	 * @param date
	 *            日期
	 * @return dd 天字符串
	 */
	public static String getDay(Date date) {
		return FormatDate(date, "dd");
	}

	/**
	 * 取得日期的小时
	 * 
	 * @param date
	 *            日期
	 * @return hh 小时字符串
	 */
	public static String getHour(Date date) {
		return FormatDate(date, "HH");
	}

	/**
	 * 取得日期的分钟
	 * 
	 * @param date
	 *            时间
	 * @return mm 分钟字符串
	 */
	public static String getMinute(Date date) {
		return FormatDate(date, "mm");
	}

	/**
	 * 取得时间的秒
	 * 
	 * @param date
	 *            时间
	 * @return ss 秒字符串
	 */
	public static String getSecond(Date date) {
		return FormatDate(date, "ss");
	}

	/**
	 * 取得日以上粒度起始时间
	 * 
	 * @param granularity
	 *            粒度
	 * @param statisticDate
	 *            结束时间
	 * @return 起始时间
	 */
	public String getBeginDate(String granularity, String statisticDate) {
		String beginDate = "";
		Date date = stringToDateShort(statisticDate);
		Date beginDateTemp = null;
		if (granularity.equals("2")) {// 月
			beginDateTemp = this.getMonthBegin(date);
		} else if (granularity.equals("3")) {// 季
			beginDateTemp = this.getSeasonBegin(date);
		} else if (granularity.equals("4")) {// 年
			beginDateTemp = this.getYearBegin(date);
		}
		beginDate = dateToStringShort(beginDateTemp);
		return beginDate;
	}

	/**
	 * 取得时间描述 日 yyyy－mm－dd 月 yyyy年mm月 季 yyyy年第×季度 年 yyyy年
	 * 
	 * @param granularity
	 *            粒度
	 * @param statisticDate
	 *            时间
	 * @return 时间对应粒度的描述
	 */
	public String getTimedes(String granularity, String statisticDate) {
		String timedes = "";
		Date date = stringToDateShort(statisticDate);
		String year = "", month = "01", day = "01";
		year = getYear(date);
		month = getMonth(date);
		day = getDay(date);
		if (granularity.equals("1")) {// 日
			timedes = statisticDate;
		} else if (granularity.equals("4")) {// 月
			timedes = year + "年" + month + "月";

		} else if (granularity.equals("8")) {// 季
			String quarter = month + "-" + day;
			if (quarter.equals("03-31")) {
				timedes = year + "年 第1季度";
			} else if (quarter.equals("06-30")) {
				timedes = year + "年 第2季度";
			} else if (quarter.equals("09-30")) {
				timedes = year + "年 第3季度";
			} else if (quarter.equals("12-31")) {
				timedes = year + "年 第4季度";
			}
		} else if (granularity.equals("32")) {// 年
			timedes = year + "年";
		}
		return timedes;
	}

	/**
	 * 日期转化为字符串
	 * 
	 * @param date
	 *            时间
	 * @return yyyy-MM-dd HH:mm:ss 格式化的时间字符串
	 */
	public static String dateToString(Date date) {
		if (date == null)
			return "";
		return FormatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String dateToString(Date date, String sf,Locale locale) {
		if (date == null)
			return "";
		return FormatDate(date, sf,locale);
	}

	/**
	 * 日期转化为字符串
	 * 
	 * @param date
	 *            时间
	 * @return yyyy-MM-dd 格式化的时间字符串
	 */
	public static String dateToStringShort(Date date) {
		if (date == null)
			return "";
		return FormatDate(date, "yyyy-MM-dd");
	}
	
	public static String dateToString(Date date, String sf) {
		if (date == null)
			return "";
		return FormatDate(date, sf);
	}

	/**
	 * 字符串转换为日期
	 * 
	 * @param dateString
	 *            yyyy-MM-dd HH:mm:ss
	 * @return 日期
	 */
	public static Date stringToDate(String dateString) {
		String sf = "yyyy-MM-dd HH:mm:ss";
		Date dt = stringToDate(dateString, sf);
		return dt;
	}

	public static Date shortstringToDate(String dateString) {
		String sf = "yyyy-MM-dd";
		Date dt = stringToDate(dateString, sf);
		return dt;
	}

	/**
	 * 字符串转换为日期
	 * 
	 * @param dateString
	 *            yyyy-MM-dd
	 * @return 日期
	 */
	public static Date stringToDateShort(String dateString) {
		if (dateString == null)
			return null;
		String sf = "yyyy-MM-dd";
		Date dt = stringToDate(dateString, sf);
		return dt;
	}

	/**
	 * 对日期进行格式化
	 * 
	 * @param date
	 *            日期
	 * @param sf
	 *            日期格式
	 * @return 字符串
	 */
	public static String FormatDate(Date date, String sf,Locale locale) {
		if (date == null)
			return "";
		SimpleDateFormat dateformat = new SimpleDateFormat(sf,locale);
		return dateformat.format(date);
	}
	
	/**
	 * 对日期进行格式化
	 * 
	 * @param date
	 *            日期
	 * @param sf
	 *            日期格式
	 * @return 字符串
	 */
	public static String FormatDate(Date date, String sf) {
		if (date == null)
			return "";
		SimpleDateFormat dateformat = new SimpleDateFormat(sf);
		return dateformat.format(date);
	}
	/**
	 * 字符串转换为日期
	 * 
	 * @param dateString
	 *            日期格式字符串
	 * @param sf
	 *            日期格式化定义
	 * @return 转换后的日期
	 */
	public static Date stringToDate(String dateString, String sf) {
		ParsePosition pos = new ParsePosition(0);
		SimpleDateFormat sdf = new SimpleDateFormat(sf);
		Date dt = sdf.parse(dateString, pos);
		return dt;
	}

	/**
	 * 计算两个日期差（毫秒）
	 * 
	 * @param date1
	 *            时间1
	 * @param date2
	 *            时间2
	 * @return 相差毫秒数
	 */
	public static long diffTwoDate(Date date1, Date date2) {
		long l1 = date1.getTime();
		long l2 = date2.getTime();
		return (l1 - l2);
	}

	/**
	 * 计算两个日期差（天）
	 * 
	 * @param date1
	 *            时间1
	 * @param date2
	 *            时间2
	 * @return 相差天数
	 */
	public static int diffTwoDateDay(Date date1, Date date2) {
		long l1 = date1.getTime();
		long l2 = date2.getTime();
		int diff = Integer.parseInt("" + (l1 - l2) / 3600 / 24 / 1000);
		return diff;
	}

	/**
	 * 
	 * @param currentTime
	 *            计算的日期
	 * @param type
	 *            偏移的类别
	 * @param iQuantity
	 *            偏移数量
	 * @return 偏移后的时间串
	 */
	public String getDateChangeTime(String currentTime, String type,
			int iQuantity) {
		Date curr = stringToDate(currentTime);
		curr = getDateChangeTime(curr, type, iQuantity);
		return dateToString(curr);
	}

	/**
	 * 
	 * @param currentTime
	 *            计算的日期
	 * @param type
	 *            偏移的类别
	 * @param iQuantity
	 *            偏移数量
	 * @return 偏移后的时间
	 */
	public Date getDateChangeTime(Date currentTime, String type, int iQuantity) {
		// System.out.println("currentTime:"+currentTime+"
		// type:"+type+iQuantity);
		int year = Integer.parseInt(FormatDate(currentTime, "yyyy"));
		int month = Integer.parseInt(FormatDate(currentTime, "MM"));
		// 月份修正
		month = month - 1;
		int day = Integer.parseInt(FormatDate(currentTime, "dd"));
		int hour = Integer.parseInt(FormatDate(currentTime, "HH"));
		int mi = Integer.parseInt(FormatDate(currentTime, "mm"));
		int ss = Integer.parseInt(FormatDate(currentTime, "ss"));
		GregorianCalendar gc = new GregorianCalendar(year, month, day, hour,
				mi, ss);
		// 月份修正
		// gc.add(GregorianCalendar.MONTH, -1);
		if (type.equalsIgnoreCase("y")) {
			gc.add(GregorianCalendar.YEAR, iQuantity);
		} else if (type.equalsIgnoreCase("m")) {
			gc.add(GregorianCalendar.MONTH, iQuantity);
		} else if (type.equalsIgnoreCase("d")) {
			gc.add(GregorianCalendar.DATE, iQuantity);
		} else if (type.equalsIgnoreCase("h")) {
			gc.add(GregorianCalendar.HOUR, iQuantity);
		} else if (type.equalsIgnoreCase("mi")) {
			gc.add(GregorianCalendar.MINUTE, iQuantity);
		} else if (type.equalsIgnoreCase("s")) {
			gc.add(GregorianCalendar.SECOND, iQuantity);
		}
		// System.out.println("gc.getTime()"+gc.getTime());
		return gc.getTime();
	}

	/**
	 * 
	 * @param currentTime
	 *            计算日期
	 * @param type
	 *            偏移的类别
	 * @param iQuantity
	 *            偏移数量
	 * @return 偏移后的时间串
	 */
	public String getDateChangeALL(String currentTime, String type,
			int iQuantity) {
		Date curr = null;
		String newtype = "";
		if (currentTime.length() == 10) {
			curr = stringToDateShort(currentTime);
		}
		if (currentTime.length() > 10) {
			curr = stringToDate(currentTime);
		}
		if (type.equals("q")) {
			iQuantity = iQuantity * 3;
			newtype = "m";
		} else {
			newtype = type;
		}
		Date change = getDateChangeTime(curr, newtype, iQuantity);
		if (!type.equals("d")) {
			change = getMonthEnd(change);
		}
		return dateToStringShort(change);
	}

	

	/**
	 * 根据年、月取得月末的日期
	 * 
	 * @param year
	 *            年
	 * @parm month 月
	 * @return time 返回日期格式"yyyy-mm-dd"
	 */
	public static String getTime(String year, String month) {
		String time = "";
		int len = 31;
		int iYear = Integer.parseInt(year);
		int iMonth = Integer.parseInt(month);
		if (iMonth == 4 || iMonth == 6 || iMonth == 9 || iMonth == 11)
			len = 30;
		if (iMonth == 2) {
			len = 28;
			if ((iYear % 4 == 0 && iYear % 100 == 0 && iYear % 400 == 0)
					|| (iYear % 4 == 0 && iYear % 100 != 0)) {
				len = 29;
			}
		}
		time = year + "-" + month + "-" + String.valueOf(len);
		return time;
	}

	/**
	 * 取月初
	 * 
	 * @param date
	 * @return
	 */
	public Date getMonthBegin(Date date) {
		String newDateStr = FormatDate(date, "yyyy-MM") + "-01";
		// FormatDate(date, "yyyy-MM-dd");
		return stringToDateShort(newDateStr);
	}

	/**
	 * 取月末时间
	 * 
	 * @param date
	 *            日期
	 * @return date
	 */
	public static Date getMonthEnd(Date date) {
		int year = Integer.parseInt(FormatDate(date, "yyyy"));
		int month = Integer.parseInt(FormatDate(date, "MM"));
		int day = Integer.parseInt(FormatDate(date, "dd"));

		GregorianCalendar calendar = new GregorianCalendar(year, month - 1,
				day, 0, 0, 0);
		int monthLength = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String newDateStr = FormatDate(date, "yyyy") + "-"
				+ FormatDate(date, "MM") + "-";
		if (monthLength < 10)
			newDateStr += "0" + monthLength;
		else
			newDateStr += "" + monthLength;
		return stringToDateShort(newDateStr);
	}

	/**
	 * 取季初
	 * 
	 * @param date
	 * @return
	 */
	public Date getSeasonBegin(Date date) {
		int month = Integer.parseInt(FormatDate(date, "MM"));
		String newDateStr = FormatDate(date, "yyyy") + "-";
		if (month >= 1 && month <= 3) {
			newDateStr += "01-01";
		} else if (month >= 4 && month <= 6) {
			newDateStr += "04-01";
		} else if (month >= 7 && month <= 9) {
			newDateStr += "07-01";
		} else if (month >= 10 && month <= 12) {
			newDateStr += "10-01";
		}
		return stringToDateShort(newDateStr);
	}

	/**
	 * 取季度末时间
	 * 
	 * @param date
	 *            日期
	 * @return date
	 */
	public static Date getSeasonEnd(Date date) {
		int month = Integer.parseInt(FormatDate(date, "MM"));
		String newDateStr = FormatDate(date, "yyyy") + "-";
		if (month >= 1 && month <= 3) {
			newDateStr += "03-31";
		} else if (month >= 4 && month <= 6) {
			newDateStr += "06-30";
		} else if (month >= 7 && month <= 9) {
			newDateStr += "09-30";
		} else if (month >= 10 && month <= 12) {
			newDateStr += "12-31";
		}
		return stringToDateShort(newDateStr);
	}

	/**
	 * 是否为半年末
	 * 
	 * @param date
	 *            时间
	 * @return
	 */
	public Date getHalfYearEnd(Date date) {
		int month = Integer.parseInt(FormatDate(date, "MM"));
		String newDateStr = FormatDate(date, "yyyy");
		if (month >= 1 && month <= 6) {
			newDateStr += "-6-30";
		} else if (month >= 7 && month <= 12) {
			newDateStr += "-12-31";
		}
		return stringToDateShort(newDateStr);
	}

	/**
	 * 取得年初
	 * 
	 * @param date
	 * @return
	 */
	public Date getYearBegin(Date date) {
		String newDateStr = FormatDate(date, "yyyy") + "-01-01";
		return stringToDateShort(newDateStr);
	}

	/**
	 * 是否为年末
	 * 
	 * @param date
	 *            时间
	 * @return
	 */
	public static Date getYearEnd(Date date) {
		String newDateStr = FormatDate(date, "yyyy") + "-12-31";
		return stringToDateShort(newDateStr);
	}

	/**
	 * 是否为旬末
	 * 
	 * @param date
	 *            时间
	 * @return 是或否
	 */
	public boolean IsXperiodEnd(Date date) {

		boolean flag = false;

		String day = getDay(date);

		if (day.equalsIgnoreCase("10")) {
			flag = true;
		} else if (day.equalsIgnoreCase("20")) {
			flag = true;
		}
		// 月末不算旬末
		// else if( this.getMonthEnd(date).compareTo(date)==0 ){
		// flag = true;
		// }

		return flag;
	}

	// 判断时间date1是否在时间date2之前
	// 时间格式 2007-1-21 16:16:34
	public static boolean isDateBefore(String date1, String date2) {
		try {
			DateFormat df = DateFormat.getDateTimeInstance();
			return df.parse(date1).before(df.parse(date2));
		} catch (ParseException e) {
			System.out.print("[SYS] " + e.getMessage());
			return false;
		}
	}

	// 判断时间date1是否在时间date2之前
	// 时间格式 2007-1-21
	public static boolean isDateBefore2(String date1, String date2) {
		try {
			return stringToDateShort(date1).before(
					stringToDateShort(date2));
		} catch (Exception e) {
			System.out.print("[SYS] " + e.getMessage());
			return false;
		}
	}

	// 判断时间date1是否等于date2
	// 时间格式 2007-1-21
	public static boolean isDateEquals(String date1, String date2) {
		try {
			return stringToDateShort(date1).equals(
					stringToDateShort(date2));
		} catch (Exception e) {
			System.out.print("[SYS] " + e.getMessage());
			return false;
		}
	}

	// 判断当前时间是否在时间date2之前
	// 时间格式 2005-1-21 16:16:34
	public static boolean isDateBefore(String date2) {
		try {
			Date date1 = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			return date1.before(df.parse(date2));
		} catch (ParseException e) {
			System.out.print("[SYS] " + e.getMessage());
			return false;
		}
	}
	
	 /**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
                .getTime());
        return dayBefore;
    }
    
    /**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDayBefore(Date specifiedDay) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        return c.getTime();
    }

    /**
     * 获得指定日期的后一天
     * 
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime());
        return dayAfter;
    }
    
    /**
     * 获得指定日期的后一天
     * 
     * @param specifiedDay
     * @return
     */
    public static Date getSpecifiedDayAfter(Date specifiedDay) {
        Calendar c = Calendar.getInstance();
      
        c.setTime(specifiedDay);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return c.getTime();
    }
	
}
