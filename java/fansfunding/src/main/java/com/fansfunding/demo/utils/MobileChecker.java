package com.fansfunding.demo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MobileChecker {
	private static final String API_KEY="6b2fb3798d404c5685b4680fe0068152";
	
	private static String httpUrl = "http://apis.baidu.com/kingtto_media/106sms/106sms";	

	public static String sendMsg(String mobile,int check){
		String content;
		try {
			content = URLEncoder.encode("【ifan$】您的验证码是"+check+"，有效时间5分钟，请不要告诉他人","UTF-8");
			String httpArg = "mobile="+mobile+"&content="+content;
			return request(httpUrl, httpArg);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	private static String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        //设置api请求头
	        connection.setRequestProperty("apikey",  API_KEY);
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
}
