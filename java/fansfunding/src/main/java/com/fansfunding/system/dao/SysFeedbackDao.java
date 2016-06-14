package com.fansfunding.system.dao;

import com.fansfunding.system.entity.SysFeedback;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SysFeedbackDao {
	/**
	 * 删除用户反馈
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增用户反馈
     * @param record
     * @return
     */
    int insert(SysFeedback record);

    /**
     * 根据id查询用户反馈
     * @param id
     * @return
     */
    SysFeedback selectByPrimaryKey(Integer id);

    /**
     * 更新用户反馈
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysFeedback record);
    
    /**
     * 查询已回复的用户反馈
     * @return
     */
    List<SysFeedback> selectReplied();

    /**
     * 查询未回复的用户反馈
     * @return
     */
    List<SysFeedback> selectUnreplied();
}