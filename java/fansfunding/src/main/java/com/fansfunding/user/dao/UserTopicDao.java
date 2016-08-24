package com.fansfunding.user.dao;

import java.util.List;

import com.fansfunding.user.entity.UserMomentLike;

public interface UserTopicDao {
//    int deleteByPrimaryKey(Integer id);
//
//    int insert(UserMomentComment record);
//
//    int updateByPrimaryKey(UserMomentComment record);

	List<UserMomentLike> selectByMomentId(int momentId);
}