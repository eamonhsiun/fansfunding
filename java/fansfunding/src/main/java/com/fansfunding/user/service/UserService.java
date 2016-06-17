package com.fansfunding.user.service;


import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.User;
import com.fansfunding.utils.encrypt.MD5Utils;


@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	/**
	 * @param id
	 * @param name
	 * @return
	 */
	public User getUser(String id,String name){
		if(!id.equals(""))
			return getUserById(Integer.parseInt(id));
		else 
			return getUserByName(name);
	}
	
	
	/**
	 * @param uid
	 * @return
	 */
	public User getUserById(int uid){
		return userDao.selectById(uid);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public User getUserByName(String name){
		return userDao.selectByName(name);
	}
	

	/**
	 * @param phone
	 * @return
	 */
	public User getUserByPhone(String phone){
		return userDao.selectByPhone(phone);
	}
	
	/**
	 * @param pwd1
	 * @param token
	 * @param pwd2
	 * @return
	 */
	public boolean CheckPwd(String pwd1,String token,String pwd2){
		return MD5Utils.MD5(MD5Utils.MD5(pwd1)+MD5Utils.MD5(token)).equals(pwd2);
	}
	
	
	
	/**
	 * @param phone
	 * @param password
	 * @return
	 */
	public User createUser(String phone,String password,int tokenid){
		User user = new User();	
		user.setName(phone);
		user.setNickname(phone);
		user.setPassword(password);
		user.setPhone(phone);
		user.setIs_red(0);
		user.setHead(UUID.randomUUID().toString().replace("-", ""));
		user.setRemark("");
		user.setDel_flag('0');
		user.setIMEI("");
		user.setToken(tokenid);
		user.setCreate_by("me");
		user.setUpdate_by("me");
		user.setCreate_time(new Date());
		user.setUpdate_time(new Date());
		userDao.insertNewUser(user);
		return user;
	}


	public void updateToken(int id) {
		
	}


	public void updatePwd(User user) {
		userDao.updatePwd(user);
	}
	
	
	
	
}
