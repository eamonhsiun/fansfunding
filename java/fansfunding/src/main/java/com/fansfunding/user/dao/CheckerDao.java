package com.fansfunding.user.dao;


import org.springframework.stereotype.Repository;

import com.fansfunding.user.entity.Checker;


@Repository
public interface CheckerDao {
	Checker selectById(int id);
	Checker selectByPhone(String phone);
	Checker selectByIMEI(String IMEI);
	void insertNewChecker(Checker checker);
	void deleteById(int id);
	void deleteByPhone(String phone);
	void deleteByIMEI(String IMEI);
	void updateToken(Checker checker);
}
