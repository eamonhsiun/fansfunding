package com.fansfunding.common.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.common.dao.TokenDao;
import com.fansfunding.common.entity.Token;


@Service
public class TokenService {
	@Autowired
	private TokenDao tokenDao;
	
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
	public Token requestToken(int permission,String phone){

		Token token = new Token();
		token.setValue(UUID.randomUUID().toString().replace("-", ""));
		token.setPermission(permission);
		token.setPhone(phone);
		tokenDao.insertNewToken(token);
		token.setPermission(0);
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
		
	}

}
