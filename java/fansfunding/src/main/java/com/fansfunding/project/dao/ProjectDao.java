package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Project;
import java.util.List;

public interface ProjectDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Project record);

    Project selectByPrimaryKey(Integer id);

    List<Project> selectAll();

    int updateByPrimaryKey(Project record);
    
    List<Project> selectByCatagoryId(Integer catagoryId);
    
    Project selectByProjectId(Integer projectId);
}