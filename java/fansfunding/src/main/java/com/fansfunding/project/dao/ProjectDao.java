package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Project;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDao {
	/**
	 * 删除项目
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增项目
     * @param record
     * @return
     */
    int insert(Project record);

    /**
     * 更新项目
     * @param record
     * @return
     */
    int updateByPrimaryKey(Project record);
    
    /**
     * 查询某个分类下的所有项目
     * @param catagoryId 分类id
     * @return
     */
    List<Project> selectByCatagoryId(Integer catagoryId);
    
    /**
     * 根据项目id查询项目
     * @param projectId 项目id
     * @return
     */
    Project selectByProjectId(Integer projectId);
}