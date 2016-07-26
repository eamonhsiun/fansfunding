package com.fansfunding.utils.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 发送验证邮件，邮件服务器在email.properties配置
 * @author wangle
 *
 */
public class Email {
	private static String user;
	private static String password;
	private static Session session;
	
	private static final String base="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

//	private Email(){
//		throw new IllegalArgumentException("you can not new an instance of this class");
//	}
	/*
	 *加载配置，并初始化会话 
	 */
	static{
		Properties prop=new Properties();
		//加载配置文件
		Resource resource=new ClassPathResource("email.properties");
		try {
			InputStream file = resource.getInputStream();
			prop.load(file);
		} catch (IOException e){
			e.printStackTrace();
		}
		//获取用户名和密码
		user=prop.getProperty("user");
		password=prop.getProperty("password");
		session=Session.getDefaultInstance(prop,new MyAuthenticator(user,password));
		session.setDebug(true);
	}
	
	/**
	 * 发送邮件
	 * @param email 收件人
	 * @return 验证码
	 * @throws MessagingException
	 */
	public static String sendTo(String email) throws MessagingException {
		String validateCode=Email.getValidateCode();
		//发送邮件
		Message message=new MimeMessage(session);
		message.setFrom(new InternetAddress(user));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
		message.setSubject("FAN$-邮箱验证");
		//设置内容
		StringBuffer content=new StringBuffer();
		content.append("<div><p style=\"margin-left:15px\">尊敬的用户，您好:</p></div>");
		content.append("<div style=\"margin-left:21px\"><p style=\"margin-left:15px\">验证码为：<i><b>");
		content.append(validateCode);
		content.append("</b></i><br/>有效期为30分钟，请尽快进行验证</p></div>");
		message.setContent(content.toString(), "text/html;charset=utf-8");
		
		Transport.send(message);
		return validateCode;
	}

	/**
	 * 获取邮箱验证码
	 * @return 验证码
	 */
	private static String getValidateCode(){
		Random random=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<6;i++){
			int k=random.nextInt(62);
			sb.append(base.charAt(k));
		}
		return sb.toString();
	}

	@Test
	public void testSendEmail(){
		try {
			Email.sendTo("1562021005@qq.com");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
/**
 * 
 * @author wangle
 *
 */
class MyAuthenticator extends Authenticator{
	private String user;
	private String password;

	public MyAuthenticator(String user,String password){
		this.user=user;
		this.password=password;
	}
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user,password);
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}