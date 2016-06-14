package com.fansfunding.project.dao;

import com.fansfunding.project.entity.ProjectDetail;

import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDetailDao {
	/**
	 * 删除项目的详情
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增项目详情
     * @param record
     * @return
     */
    int insert(ProjectDetail record);

    /**
     * 更新项目的详情信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(ProjectDetail record);
    
    /**
     * 根据项目id查询其详情信息
     * @param projectId 项目id
     * @return
     */
    ProjectDetail selectByProjectId(Integer projectId);
}