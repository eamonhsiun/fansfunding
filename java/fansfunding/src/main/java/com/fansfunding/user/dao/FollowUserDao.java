package com.fansfunding.user.dao;

import com.fansfunding.user.entity.FollowUser;

public interface FollowUserDao {
    int delete(FollowUser follow);
    int insert(FollowUser follow);
    FollowUser select(FollowUser follow);
    int disdelete(FollowUser follow);
}