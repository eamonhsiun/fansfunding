package com.fansfunding.user.dao;

import java.util.List;

import com.fansfunding.user.entity.UserMomentComment;
import com.fansfunding.user.entity.UserMomentLike;

public interface UserMomentLikeDao {
//    int deleteByPrimaryKey(Integer id);
//
//    int insert(UserMomentComment record);
//
//    int updateByPrimaryKey(UserMomentComment record);

	List<UserMomentLike> selectByMomentId(int momentId);
}