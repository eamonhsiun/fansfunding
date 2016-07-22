package com.fansfunding.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.system.dao.SysFeedbackDao;
import com.fansfunding.system.entity.SysFeedback;
import com.fansfunding.user.dao.UserDao;

@Service
public class SysFeedbackService {
	@Autowired
	private SysFeedbackDao sysFeedbackDao;
	@Autowired
	private UserDao userDao;

	public void add(String email,String content){
		SysFeedback feedback=new SysFeedback();
		feedback.setIsReplied((byte) 0);
		feedback.setCreateBy(email);
		feedback.setUpdateBy(email);
		feedback.setEmail(email);
		feedback.setContent(content);
		sysFeedbackDao.insert(feedback);
	}
}
