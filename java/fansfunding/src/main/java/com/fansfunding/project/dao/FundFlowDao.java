package com.fansfunding.project.dao;

import com.fansfunding.project.entity.FundFlow;
import java.util.List;

public interface FundFlowDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FundFlow record);

    FundFlow selectByPrimaryKey(Integer id);

    List<FundFlow> selectAll();

    int updateByPrimaryKey(FundFlow record);
}