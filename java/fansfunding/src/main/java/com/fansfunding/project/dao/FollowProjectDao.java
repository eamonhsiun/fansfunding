package com.fansfunding.project.dao;

import com.fansfunding.project.entity.FollowProject;

public interface FollowProjectDao {
    int delete(FollowProject record);
    int insert(FollowProject record);
    FollowProject select(FollowProject record);
    int disdelete(FollowProject record);
}