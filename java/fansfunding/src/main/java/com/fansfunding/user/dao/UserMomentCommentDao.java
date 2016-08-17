package com.fansfunding.user.dao;

import java.util.List;

import com.fansfunding.user.entity.UserMomentComment;

public interface UserMomentCommentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMomentComment record);

    int updateByPrimaryKey(UserMomentComment record);

	List<UserMomentComment> selectByMomentId(int momentId);
}