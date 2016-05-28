package com.immortals.fans.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.immortals.fans.dao.CheckerDao;
import com.immortals.fans.entity.Checker;

@Service
public class CheckerService {
	@Autowired
	private CheckerDao checkerDao;
	
	@Transactional
	public Checker getCheckerByID(int id){
		return checkerDao.selectById(id);
	}
	
	@Transactional
	public Checker getCheckerByPhone(String phone){
		return checkerDao.selectByPhone(phone);
	}
	
	@Transactional
	public Checker getCheckerByIMEI(String IMEI){
		return checkerDao.selectByIMEI(IMEI);
	}
	
	@Transactional
	public int createChecker(String IMEI,String phone,int num,Date date){
		Checker checker = new Checker();
		checker.setIMEI(IMEI);
		checker.setChecknum(num);
		checker.setRequest_time(date);
		checker.setPhone(phone);
		checkerDao.insertNewChecker(checker);
		return checker.getId();
	}
	
	
	@Transactional
	public void deleteById(int id){
		checkerDao.deleteById(id);
	}
	@Transactional
	public void deleteByPhone(String phone){
		checkerDao.deleteByPhone(phone);
	}
	@Transactional
	public void deleteByIMEI(String IMEI){
		checkerDao.deleteByIMEI(IMEI);
	}
	
	public void updateToken(Checker checker){
		checkerDao.updateToken(checker);
	}
	
	
}
