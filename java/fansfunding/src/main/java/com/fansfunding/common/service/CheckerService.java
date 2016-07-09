package com.fansfunding.common.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fansfunding.common.dao.CheckerDao;
import com.fansfunding.common.entity.Checker;
import com.fansfunding.utils.MobileChecker;


@Service
public class CheckerService {
	
	public int check;
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
	public boolean isTimeTooShort(String phone){
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
		return false;
	}
	
	/**
	 * 生成请求验证码
	 * @param IMEI
	 * @param phone
	 * @return cid
	 */
	public int genChecker(String phone){
		int checknum = (int)(Math.random()*899999) +100000;
		MobileChecker.sendMsg(phone, checknum);
		Checker checker = new Checker();
		//TODO:这里还需要做token操作
		checker.setChecknum(checknum);
		checker.setPhone(phone);
		
		check=checknum;
		checkerDao.insertNewChecker(checker);
		return checker.getId();
	}
	
	public int genCheckerT(String phone){
		int checknum = (int)(Math.random()*899999) +100000;
		Checker checker = new Checker();
		//TODO:这里还需要做token操作
		checker.setChecknum(checknum);
		checker.setPhone(phone);
		
		check=checknum;
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
	
	
	
	/**
	 * @param id
	 * @return
	 */
	public Checker getCheckerByID(int id){
		return checkerDao.selectById(id);
	}
	
	/**
	 * @param phone
	 * @return
	 */
	public Checker getCheckerByPhone(String phone){
		return checkerDao.selectByPhone(phone);
	}
	
	
	/**
	 * @param id
	 */
	public void deleteById(int id){
		checkerDao.deleteById(id);
	}
	
	/**
	 * @param phone
	 */
	@Transactional
	public void deleteByPhone(String phone){
		checkerDao.deleteByPhone(phone);
	}

	
	/**
	 * @param checker
	 */
	public void updateToken(Checker checker){
		checkerDao.updateToken(checker);
	}
	
	
}
