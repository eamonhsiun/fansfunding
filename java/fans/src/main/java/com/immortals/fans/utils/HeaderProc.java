package com.immortals.fans.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HeaderProc {
	public static final String HEADER_DEFAULT = "http://114.215.128.9:8080/fans/";

	public static String genLoginHeader(String IMEI,int uid,String name,String pwd) throws Exception{
		String header = HEADER_DEFAULT+"user/login?";
		String S1 = MD5Utils.MD5(IMEI);
		String S2 = AESUtils.EncryptByMD5(uid+"", S1);
		String S3 = AESUtils.EncryptByMD5(name, S1);
		String S4 = AESUtils.EncryptByMD5(pwd, S1);
		header +=("IMEI="+S1+"&"+"uid="+S2+"&"+"name="+S3+"&"+"pwd="+S4);
		header = header.replace("+", "%2B");
		return header;
	}
	
	public static String genLogoutHeader(String token,int uid,String name) throws Exception{
		String header = HEADER_DEFAULT+"user/logout?";
		String S2 = uid+"";
		String S3 = AESUtils.EncryptByMD5(name+"", token);
		header +=("uid="+S2+"&"+"name="+S3);
		header = header.replace("+", "%2B");
		return header;
	}
	
	
	public static String genCheckerHeader(String IMEI,String phone) throws Exception{
		String header = HEADER_DEFAULT+"user/gen_checker?";
		String S1 = MD5Utils.MD5(IMEI);
		String S2 = AESUtils.EncryptByMD5(phone, S1);
		header +=("IMEI="+S1+"&"+"phone="+S2);
		header = header.replace("+", "%2B");
		return header;
	}
	
	public static String genRegisterHeader(String IMEI,int id,int check) throws Exception{
		String header = HEADER_DEFAULT+"user/register_check?";
		String S1 = MD5Utils.MD5(IMEI);
		String S2 = AESUtils.EncryptByMD5(check+"",S1);
		header +=("id="+id+"&"+"check="+S2);
		header = header.replace("+", "%2B");
		return header;
	}
	
	public static String genForgetHeader(String IMEI,int id,int check) throws Exception{
		String header = HEADER_DEFAULT+"user/forget_check?";
		String S1 = MD5Utils.MD5(IMEI);
		String S2 = AESUtils.EncryptByMD5(check+"",S1);
		header +=("id="+id+"&"+"check="+S2);
		header = header.replace("+", "%2B");
		return header;
	}
	
	
	public static String genUpPwdHeader(String token,int uid,int cid,int check,String pwd) throws Exception{
		String header = HEADER_DEFAULT+"user/update_pwd?";
		String S1 = uid+"";
		String S2 = AESUtils.EncryptByMD5(cid+"",token);
		String S3 = AESUtils.EncryptByMD5(check+""+pwd,token);
		header +=("uid="+S1+"&"+"cid="+S2+"&"+"pwd="+S3);
		header = header.replace("+", "%2B");
		return header;
	}
	
	
	
	private static String request(String httpUrl) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
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

	
	
	
	public static void main(String[] args) {
		try {
			String IMEI ="99000663283249";
			String name ="17771842186";
			String token ="";
			String pwd ="xym123";
			int uid = 10000001;
			int cid = 10000001;
			
			int check = 100000;
			
			//注册申请第一步，申请验证码
			//System.out.println(request(genCheckerHeader(IMEI, name)));
			//更新
			cid=10000044;
			check=178853;
			
			//注册申请第二步，确认验证码
			//System.out.println(request(genRegisterHeader(IMEI, cid,check)));
			//更新
			uid = 10000023;
			token ="a01ab8f16f4f46cc9a4453882a74daa2";
			//设置
			pwd = "xym12345";
			
			
			//注册申请第三步，更新密码
			//System.out.println(request(genUpPwdHeader(token,uid, cid,check,pwd)));
			//测试错误密码
			//pwd = "12321";
			
			//登陆
			//System.out.println(request(genLoginHeader(IMEI ,uid, name,pwd)));
			//更新
			token ="b9738dfece63478cb4f24f3e554d5dbc";
			
			//登出
			//System.out.println(request(genLogoutHeader(token ,uid, name)));
			
			//更新密码第一步，申请验证码
			//System.out.println(request(genCheckerHeader(IMEI, name)));
			//更新
			cid=10000044;
			check=178853;
			
			//更新密码第二步，确认验证码（仅该请求与注册不同）
			//System.out.println(genForgetHeader(IMEI,cid,check));
			//更新
			uid = 10000023;
			token ="a01ab8f16f4f46cc9a4453882a74daa2";
			//设置
			pwd = "xym123";
			
			//更新密码第三步，更新密码
			//System.out.println(request(genUpPwdHeader(token,uid, cid,check,pwd)));
			
			//登陆
			//System.out.println(request(genLoginHeader(IMEI ,uid, name,pwd)));
			//更新
			token ="b9738dfece63478cb4f24f3e554d5dbc";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
