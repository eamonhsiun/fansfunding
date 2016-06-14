package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Feedback;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackDao {
	/**
	 * 删除回馈方式
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增回馈方式
     * @param record
     * @return
     */
    int insert(Feedback record);

    /**
     * 根据id查询回馈方式
     * @param id
     * @return
     */
    Feedback selectByPrimaryKey(Integer id);

    /**
     * 更新回馈方式
     * @param record
     * @return
     */
    int updateByPrimaryKey(Feedback record);
    
    /**
     * 根据项目id查询回馈方式
     * @param projectId 项目id
     * @return
     */
    List<Feedback> selectByProjectId(Integer projectId);
}