package com.immortals.fans.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.immortals.fans.entity.User;


@Repository
public interface UserDao {
	List<User> selectAll();
	User selectById(int uid);
	User selectByName(String name);
	User selectByPhone(String phone);
	void insertNewUser(User user);
	void updateToken(User user);
	void updatePwd(User user);
}
