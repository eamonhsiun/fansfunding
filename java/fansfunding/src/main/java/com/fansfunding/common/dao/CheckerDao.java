package com.fansfunding.common.dao;


import org.springframework.stereotype.Repository;

import com.fansfunding.common.entity.Checker;


@Repository
public interface CheckerDao {
	Checker selectById(int id);
	Checker selectByPhone(String phone);
	void insertNewChecker(Checker checker);
	void deleteById(int id);
	void deleteByPhone(String phone);
	void updateToken(Checker checker);
}
