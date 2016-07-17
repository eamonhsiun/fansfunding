package com.fansfunding.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import com.fansfunding.utils.encrypt.MD5Utils;

public class HeaderProc {
	private HeaderProc(){}
	public static final String HEADER_DEFAULT = "http://localhost:8080/fansfunding/";
	
	//E807F1FCF82D132F9BB018CA6738A19F
	
	public static String newChecker(String phone){

		String header = HEADER_DEFAULT+"common/newChecker?phone="+phone;
		return header;
	}
	
	public static String newUser(String checker, String password, String token) {
		String header = HEADER_DEFAULT+"user/newUser";
		header += ("?checker="+checker);
		header += ("&password="+MD5Utils.MD5(password));
		header += ("&token="+token);
		return header;	
	}
	
	public static String forgetPwd(String checker, String password, String token) {
		String header = HEADER_DEFAULT+"user/forgetPwd";
		header += ("?checker="+checker);
		header += ("&password="+MD5Utils.MD5(password));
		header += ("&token="+token);
		return header;	
	}
	
	public static String newPwd(String id, String password, String token) {
		String header = HEADER_DEFAULT+"user/"+id+"/newPwd";
		header += ("?password="+MD5Utils.MD5(password));
		header += ("&token="+token);
		return header;	
	}
	
	
	public static String login(String name, String password) {
		String header = HEADER_DEFAULT+"user/login";
		header += ("?name="+name);
		header += ("&password="+MD5Utils.MD5(password));
		return header;
	}

	public static String logout(String id, String token) {
		String header = HEADER_DEFAULT+"user/"+id+"/logout";
		header += ("?token="+token);
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
			
			String phone = "17771842186";
			
			System.out.println(newChecker(phone));
			//System.out.println(request(newChecker(phone)));
			
			String checker="246723";
			String password="qqq";
			String token="ApW930aDcJUwwVimA6z1%2BQ==";
			
			System.out.println(newUser(checker,password,token));
			//System.out.println(request(newUser(checker,password,token)));
			
			System.out.println(login(phone,password));
			//System.out.println(request(login(phone,password)));
			int id =10000023;
			
			token = "oUJG/%2B6HpZQmwRX12B6JEQ==";
			
			System.out.println(logout(id+"",token));
			System.out.println(request(logout(id+"",token)));
			
			//checker="472041";
			password="qqq";
			//token ="9GAMctquO23PA6QE6JVSbQ==";
			System.out.println(forgetPwd(checker,password,token));
			//System.out.println(request(forgetPwd(checker,password,token)));
			
			System.out.println(newPwd(id+"",password,token));
			//System.out.println(request(newPwd(id+"",password,token)));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
