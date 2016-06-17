package com.fansfunding.common.dao;

import org.springframework.stereotype.Repository;

import com.fansfunding.common.entity.Token;

@Repository
public interface TokenDao {
	/**
	 * 删除Token
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Integer id);

    /**
     * 新增Token
     * @param record
     * @return
     */
    int insertNewToken(Token Token);

    /**
     * 更新权限
     * @param record
     * @return
     */
    int updatePermission(Token Token);
    
    /**
     * 更新过期时间
     * @param record
     * @return
     */
    int updateExpireTime(Token Token);
    
    /**
     * 主键查找Token
     * @param record
     * @return
     */
    Token selectByPrimaryKey(Integer id);
    
    
    /**
     * 值查找Token
     * @param record
     * @return
     */
    Token selectByValue(String value);
    
    
}