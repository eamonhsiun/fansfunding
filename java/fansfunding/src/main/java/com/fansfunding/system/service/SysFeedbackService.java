package com.fansfunding.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.system.dao.SysFeedbackDao;
import com.fansfunding.system.entity.SysFeedback;

@Service
public class SysFeedbackService {
	@Autowired
	private SysFeedbackDao sysFeedbackDao;
	
	public void add(SysFeedback feedback){
		sysFeedbackDao.insert(feedback);
	}
}
