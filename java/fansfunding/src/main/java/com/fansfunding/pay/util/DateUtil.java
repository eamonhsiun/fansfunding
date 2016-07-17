
package com.fansfunding.pay.util;

import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;

/**
 *具类，可以用作获取系统日期、订单编号等
 */
public class DateUtil {
    /** 
     * 年月日时分秒(无下划线)
     */
    public static final String fullNoUnderline = "yyyyMMddHHmmss";
    private final static SimpleDateFormat fullNoUnderlineFormat=new SimpleDateFormat(fullNoUnderline);
    /**
     * 完整时间
     */
    public static final String full = "yyyy-MM-dd HH:mm:ss";
    private final static SimpleDateFormat fullFormat=new SimpleDateFormat(full);
    /**
     * 年月日(无下划线)
     */
    public static final String dateShort = "yyyyMMdd";
    private final static SimpleDateFormat dateShortFormat=new SimpleDateFormat(dateShort);

	private DateUtil(){
		throw new RuntimeException("You can not new an instance of this class!");
	}
    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * @return 以yyyyMMddHHmmss为格式的当前系统时间
     */
	public static String getOrderNum(){
		return fullNoUnderlineFormat.format(new Date());
	}
	/**
	 * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public  static String getTime(){
		return fullFormat.format(new Date());
	}
	/**
	 * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
	 * @return
	 */
	public static String getDate(){
		return dateShortFormat.format(new Date());
	}
	/**
	 * 产生随机的三位数
	 * @return
	 */
	public static String getThree(){
		return String.valueOf(new Random().nextInt(1000));
	}
}
