package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Category;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface CategoryDao {
	/**
	 * 删除分类
	 * @param id 分类id
	 * @return
	 */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增分类
     * @param category 分类
     * @return
     */
    int insert(Category category);

    /**
     * 根据分类id查询分类信息
     * @param id 分类id
     * @return
     */
    Category selectByPrimaryKey(Integer id);

    /**
     * 查询所有的分类
     * @return
     */
    List<Category> selectAll();

    /**
     * 更新分类
     * @param record 分类id
     * @return
     */
    int updateByPrimaryKey(Category record);
}