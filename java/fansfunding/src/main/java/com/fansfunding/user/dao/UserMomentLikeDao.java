package com.fansfunding.user.dao;

import java.util.List;

import com.fansfunding.user.entity.UserMomentLike;

public interface UserMomentLikeDao {
    int deleteByPrimaryKey(UserMomentLike record);
//
    int insert(UserMomentLike record);
//
//    int updateByPrimaryKey(UserMomentComment record);

	List<UserMomentLike> selectByMomentId(int momentId);
	
	UserMomentLike selectByPrimaryKey(UserMomentLike userMomentLike);
}