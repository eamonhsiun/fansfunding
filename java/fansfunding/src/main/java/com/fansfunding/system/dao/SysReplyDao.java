package com.fansfunding.system.dao;

import com.fansfunding.system.entity.SysReply;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SysReplyDao {
	/**
	 * 删除用户反馈的回复
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增回复
     * @param record
     * @return
     */
    int insert(SysReply record);

    /**
     * 根据id查询回复
     * @param id
     * @return
     */
    SysReply selectByPrimaryKey(Integer id);

    /**
     * 查询所有的回复
     * @return
     */
    List<SysReply> selectAll();

    /**
     * 更新回复
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysReply record);
}