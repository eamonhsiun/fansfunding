package com.fansfunding.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fansfunding.user.entity.User;



@Repository
public interface UserDao {
	List<User> selectAll();
	User selectById(int uid);
	User selectByName(String name);
	User selectByPhone(String phone);
	User selectByEmail(String email);
	void insertNewUser(User user);
	void updateToken(User user);
	void updatePwd(User user);
	void updateHead(User user);
	void updateUser(User user);
	void updateNickName(User user);
	List<User> selectByKeyword(String keyword);
}
