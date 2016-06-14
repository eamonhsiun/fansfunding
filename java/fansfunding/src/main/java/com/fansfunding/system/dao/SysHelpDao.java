package com.fansfunding.system.dao;

import com.fansfunding.system.entity.SysHelp;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SysHelpDao {
	/**
	 * 删除帮助
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);
    
    /**
     * 新增帮助
     * @param record
     * @return
     */
    int insert(SysHelp record);

    /**
     * 根据id查询帮助
     * @param id
     * @return
     */
    SysHelp selectByPrimaryKey(Integer id);

    /**
     * 查询所有的帮助
     * @return
     */
    List<SysHelp> selectAll();

    /**
     * 更新帮助
     * @param record
     * @return
     */
    int updateByPrimaryKey(SysHelp record);
}