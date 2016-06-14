package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Feedback;
import java.util.List;

public interface FeedbackDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Feedback record);

    Feedback selectByPrimaryKey(Integer id);

    List<Feedback> selectAll();

    int updateByPrimaryKey(Feedback record);
    
    List<Feedback> selectByProjectId(Integer projectId);
}