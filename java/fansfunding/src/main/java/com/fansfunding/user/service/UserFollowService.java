package com.fansfunding.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.user.dao.FollowUserDao;

@Service
public class UserFollowService {
	@Autowired
	private FollowUserDao followUserDao;
	
}
