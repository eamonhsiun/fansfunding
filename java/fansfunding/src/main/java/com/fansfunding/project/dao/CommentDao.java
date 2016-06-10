package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Comment;
import java.util.List;

public interface CommentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    Comment selectByPrimaryKey(Integer id);

    List<Comment> selectAll();

    int updateByPrimaryKey(Comment record);
}