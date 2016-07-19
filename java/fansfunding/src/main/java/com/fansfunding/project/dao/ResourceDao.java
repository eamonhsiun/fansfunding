package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Resource;
import java.util.List;

public interface ResourceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Resource record);

    Resource selectByPrimaryKey(Integer id);

    List<Resource> selectProjectImages(int projectId);
    
    List<Resource> selectFeedbackImages(int feedbackId);
    
    List<Resource> selectMomentImages(int momentId);

    int updateByPrimaryKey(Resource record);
    
    int updateByPath(Resource record);
}