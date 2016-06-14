package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Comment;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao {
	/**
	 * 删除评论
	 * @param id 评论id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增评论
     * @param record 评论
     * @return
     */
    int insert(Comment record);

    /**
     * 根据id查询评论
     * @param id 评论id
     * @return
     */
    Comment selectByPrimaryKey(Integer id);

    /**
     * 更新评论信息
     * @param record 评论
     * @return
     */
    int updateByPrimaryKey(Comment record);
    
    /**
     * 查询某个项目下的所有评论
     * @param projectId 项目id
     * @return
     */
    List<Comment> selectByProjectId(Integer projectId);
}