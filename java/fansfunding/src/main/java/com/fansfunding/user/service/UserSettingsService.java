package com.fansfunding.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.user.dao.UserDao;

@Service
public class UserSettingsService {
	@Autowired
	private UserDao userDao;
	
}
