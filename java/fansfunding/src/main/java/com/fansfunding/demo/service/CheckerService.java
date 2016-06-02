package com.fansfunding.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fansfunding.demo.dao.CheckerDao;
import com.fansfunding.demo.entity.Checker;
import com.fansfunding.demo.utils.MobileChecker;


@Service
public class CheckerService {
	
	
	/**
	 * 最小请求间隔
	 */
	private static final int MIN_INTERVAL = 60000;
	
	
	/**
	 * 最大有效时长
	 */
	private static final int MAX_VALID = 360000;
	
	
	@Autowired
	private CheckerDao checkerDao;
	
	/**
	 * 验证请求间隔是否过短
	 * @param phone
	 * @return
	 */
	public boolean isTimeTooShort(String IMEI,String phone){
		//手机号请求唯一性验证
		Date end = new Date();
		Checker start=checkerDao.selectByPhone(phone);
		if(start!=null){
			if(end.getTime()-start.getRequest_time().getTime()<MIN_INTERVAL){
				return true;
			}else{
				checkerDao.deleteByPhone(phone);
			}
		}
		
		//IMEI请求唯一性验证
		end = new Date();
		start=checkerDao.selectByPhone(IMEI);
		if(start!=null){
			if(end.getTime()-start.getRequest_time().getTime()<MIN_INTERVAL){
				return true;
			}else{
				checkerDao.deleteByIMEI(IMEI);
			}
		}
		
		return false;
	}
	
	/**
	 * 生成请求验证码
	 * @param IMEI
	 * @param phone
	 * @return cid
	 */
	public int genChecker(String IMEI,String phone){
		int checknum = (int)(Math.random()*899999) +100000;
		MobileChecker.sendMsg(phone, checknum);
		Checker checker = new Checker();
		checker.setIMEI(IMEI);
		checker.setChecknum(checknum);
		checker.setRequest_time(new Date());
		checker.setPhone(phone);
		
		checkerDao.insertNewChecker(checker);
		return checker.getId();
	}
	
	
	/**
	 * 验证码过期验证
	 * @return
	 */
	public boolean isValid(int id){
		Checker checker = checkerDao.selectById(id);
		if(checker==null){
			return false;
		}
		Date end = new Date();
		if(end.getTime()-checker.getRequest_time().getTime()>MAX_VALID){
			checkerDao.deleteById(id);
			return false;
		}
		return true;
	}
	
	
	
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
