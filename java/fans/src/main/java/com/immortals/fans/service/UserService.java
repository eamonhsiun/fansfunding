package com.immortals.fans.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.immortals.fans.dao.UserDao;
import com.immortals.fans.entity.User;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;


	@Transactional
	public void updatePwd(User user){
		userDao.updatePwd(user);
	}
	
	@Transactional
	public void updateToken(User user){
		userDao.updateToken(user);
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
	public void createUser(User user){
		userDao.insertNewUser(user);
		return;
	}
	
	
	
	
}
