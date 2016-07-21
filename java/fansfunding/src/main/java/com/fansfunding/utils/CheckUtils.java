package com.fansfunding.utils;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 验证合法性工具
 * @author w-angler
 *
 */
public class CheckUtils {
	private static Pattern email=
			Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
	private static Pattern phone=Pattern.compile("^((13[0-9])|(17[8,0,6,7])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
	
	/**
	 * 验证邮箱合法性
	 * @param content 内容
	 * @return
	 */
	public static boolean isEmail(String content){
	    return email.matcher(content.trim()).matches();
	}
	/**
	 * 验证手机号合法性
	 * @param content 内容
	 * @return
	 */
	public static boolean isPhone(String content){
		return phone.matcher(content.trim()).matches();
	}

    /**
     * 验证时间格式是否符合
     * @param time 时间字符串
     * @param format 时间格式
     * @return
     */
    public static boolean isDateFormatMatch(String time,String format) {
        try {
        	new SimpleDateFormat(format).parse(time);
		} catch (ParseException e) {
			return false;
		}
        return true;
    }
    /**
	 * 验证对象是否为null,空字符串，空数组，空的Collection或Map(只有空格的字符串也认为是空串)
	 * @param obj 被验证的对象
	 * @param message 异常信息
	 */
	public static boolean isNullOrEmpty(Object... objs) {
		for(Object obj:objs){
			if (obj == null){
				return true;
			}
			if (obj instanceof String && obj.toString().trim().length()==0){
				return true;
			}
			if (obj.getClass().isArray() && Array.getLength(obj)==0){
				return true;
			}
			if (obj instanceof Collection && ((Collection<?>)obj).isEmpty()){
				return true;
			}
			if (obj instanceof Map && ((Map<?,?>)obj).isEmpty()){
				return true;
			}
		}
		return false;
	}
    
}
