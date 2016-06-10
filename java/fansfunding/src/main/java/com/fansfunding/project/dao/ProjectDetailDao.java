package com.fansfunding.project.dao;

import com.fansfunding.project.entity.ProjectDetail;
import java.util.List;

public interface ProjectDetailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectDetail record);

    ProjectDetail selectByPrimaryKey(Integer id);

    List<ProjectDetail> selectAll();

    int updateByPrimaryKey(ProjectDetail record);
}