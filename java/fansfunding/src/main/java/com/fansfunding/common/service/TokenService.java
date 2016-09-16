package com.fansfunding.common.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.common.dao.TokenDao;
import com.fansfunding.common.entity.Token;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.User;


@Service
public class TokenService {
	@Autowired
	private TokenDao tokenDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 是否过期
	 * @return
	 */
	public boolean isExpired(){
		return true;
	}
	
	/**
	 * 是否过于频繁
	 * @return
	 */
	public boolean isTooFrequency(String phone){
		
		
		return true;
	}
	
	
	
	/**
	 * 请求Token
	 * @return
	 */
	public Token requestToken(int permission,User user,int type){
		Token token = new Token();
		token.setValue(UUID.randomUUID().toString().replace("-", ""));
		token.setPermission(permission);
		token.setPhone(user.getPhone());
		tokenDao.insertNewToken(token);
		token.setPermission(0);
		
		
		if(type==0){
			user.setToken(token.getId());
			userDao.updateToken(user);
		}else if (type==1){
			user.setToken_web(token.getId());
			userDao.updateWebToken(user);
		}
		
		
		
		return token;
	}
	
	/**
	 * Id查找
	 * @return
	 */
	public Token lookUpTokenById(int id){

		return tokenDao.selectByPrimaryKey(id);

	}
	
	/**
	 * 值查找
	 * @return
	 */
	public Token lookUpTokenByValue(){
		return null;
	}

	
	public void setPermission(int id,int permission){
		Token token = new Token();
		token.setId(id);
		token.setPermission(permission);
		tokenDao.updatePermission(token);
		
	}

}
