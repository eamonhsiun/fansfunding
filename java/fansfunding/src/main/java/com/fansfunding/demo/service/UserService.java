package com.fansfunding.demo.service;


import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fansfunding.demo.dao.CheckerDao;
import com.fansfunding.demo.dao.UserDao;
import com.fansfunding.demo.entity.Checker;
import com.fansfunding.demo.entity.User;
import com.fansfunding.demo.entity.UserBasic;


@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private CheckerDao checkerDao;

	public User findUserByCheckerId(int id){
		Checker checker = checkerDao.selectById(id);
		return userDao.selectByName(checker.getPhone());
	}
	
	public UserBasic RefreshToken(User user,Checker checker){
		user.setToken(UUID.randomUUID().toString().replace("-", ""));
		checker.setToken(user.getToken());
		checkerDao.updateToken(checker);
		userDao.updateToken(user);
		UserBasic userBaisc = new UserBasic(user);
		userBaisc.setPassword("");
		userBaisc.setIMEI("");
		return userBaisc;
	}
	
	public UserBasic updatePwd(User user,Checker checker){
		RefreshToken(user,checker);
		userDao.updatePwd(user);
		checkerDao.deleteById(checker.getId());
		UserBasic userBaisc = new UserBasic(user);
		userBaisc.setPassword("");
		userBaisc.setIMEI("");
		return userBaisc;
	}
	
	
	
	
	@Transactional
	public UserBasic updateToken(User user){
		userDao.updateToken(user);
		UserBasic userBaisc = new UserBasic(user);
		userBaisc.setPassword("");
		userBaisc.setIMEI("");
		return userBaisc;

	}
	
	
	@Transactional
	public User getUserById(int uid){
		return userDao.selectById(uid);
	}
	
	@Transactional
	public User getUserByName(String name){
		return userDao.selectByName(name);
	}
	
	@Transactional
	public User getUserByPhone(String phone){
		return userDao.selectByPhone(phone);
	}
	
	@Transactional
	public User createUser(String phone,String IMEI){
		User user = new User();
		
		user.setName(phone);
		user.setNickname(phone);
		user.setPassword(UUID.randomUUID().toString().replace("-", ""));
		user.setPhone(phone);
		user.setIs_red(0);
		user.setHead(UUID.randomUUID().toString().replace("-", ""));
		user.setRemark("");
		user.setDel_flag('0');
		user.setIMEI(IMEI);
		user.setToken(UUID.randomUUID().toString().replace("-", ""));
		user.setCreate_by("me");
		user.setUpdate_by("me");
		user.setCreate_time(new Date());
		user.setUpdate_time(new Date());
		userDao.insertNewUser(user);
		
		return user;
	}
	
	
	
	
}
