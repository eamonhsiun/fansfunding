package com.fansfunding.user.dao;

import com.fansfunding.user.entity.RealInfo;
import java.util.List;

public interface RealInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RealInfo record);

    RealInfo selectByPrimaryKey(Integer id);
    
    RealInfo selectByUserId(Integer id);

    List<RealInfo> selectAll();

    int updateByPrimaryKey(RealInfo record);
    
    
}