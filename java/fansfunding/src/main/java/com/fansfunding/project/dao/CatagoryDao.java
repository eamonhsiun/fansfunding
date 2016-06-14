package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Catagory;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface CatagoryDao {
	/**
	 * 删除分类
	 * @param id 分类id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增分类
     * @param catagory 分类
     * @return
     */
    int insert(Catagory catagory);

    /**
     * 根据分类id查询分类信息
     * @param id 分类id
     * @return
     */
    Catagory selectByPrimaryKey(Integer id);

    /**
     * 查询所有的分类
     * @return
     */
    List<Catagory> selectAll();

    /**
     * 更新分类
     * @param record 分类id
     * @return
     */
    int updateByPrimaryKey(Catagory record);
}