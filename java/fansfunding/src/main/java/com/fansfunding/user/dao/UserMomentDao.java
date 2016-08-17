package com.fansfunding.user.dao;

import java.util.List;

import com.fansfunding.user.entity.UserMoment;

public interface UserMomentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMoment record);

    List<UserMoment> selectFriendsByUserId(Integer id);

    int updateByPrimaryKey(UserMoment record);

	List<UserMoment> selectByUserId(int userId);

	UserMoment selectById(int momentId);
}