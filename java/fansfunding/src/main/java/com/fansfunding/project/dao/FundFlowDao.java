package com.fansfunding.project.dao;

import com.fansfunding.project.entity.FundFlow;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface FundFlowDao {
	/**
	 * 删除资金流
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入新的资金流信息
     * @param record
     * @return
     */
    int insert(FundFlow record);

    /**
     * 根据id查询资金流
     * @param id
     * @return
     */
    FundFlow selectByPrimaryKey(Integer id);

    /**
     * 更新资金流信息
     * @param record
     * @return
     */
    int updateByPrimaryKey(FundFlow record);
    
    /**
     * 查询项目相关的资金流信息
     * @param projectId 项目id
     * @return
     */
    List<FundFlow> selectByProjectId(Integer projectId);
    
    /**
     * 查询用户相关的资金流
     * @param userId 用户id
     * @return
     */
    FundFlow selectByUserId(Integer userId);
}