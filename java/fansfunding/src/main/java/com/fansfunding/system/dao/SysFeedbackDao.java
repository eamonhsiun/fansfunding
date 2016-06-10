package com.fansfunding.system.dao;

import com.fansfunding.system.entity.SysFeedback;
import java.util.List;

public interface SysFeedbackDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysFeedback record);

    SysFeedback selectByPrimaryKey(Integer id);

    List<SysFeedback> selectAll();

    int updateByPrimaryKey(SysFeedback record);
}