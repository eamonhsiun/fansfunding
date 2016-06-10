package com.fansfunding.user.dao;

import com.fansfunding.user.entity.SnsAccount;
import java.util.List;

public interface SnsAccountDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsAccount record);

    SnsAccount selectByPrimaryKey(Integer id);

    List<SnsAccount> selectAll();

    int updateByPrimaryKey(SnsAccount record);
}