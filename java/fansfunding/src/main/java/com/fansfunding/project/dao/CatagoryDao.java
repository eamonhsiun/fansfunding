package com.fansfunding.project.dao;

import com.fansfunding.project.entity.Catagory;
import java.util.List;

public interface CatagoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Catagory record);

    Catagory selectByPrimaryKey(Integer id);

    List<Catagory> selectAll();

    int updateByPrimaryKey(Catagory record);
}