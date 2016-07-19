package com.fansfunding.project.dao;

import java.util.List;

import com.fansfunding.project.entity.ProjectMoment;

public interface ProjectMomentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectMoment record);

    ProjectMoment selectByPrimaryKey(Integer id);
    
    List<ProjectMoment> selectByProjectId(Integer projectId);

    int updateByPrimaryKey(ProjectMoment record);
}