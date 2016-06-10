package com.fansfunding.user.dao;

import com.fansfunding.user.entity.ShoppingAddress;
import java.util.List;

public interface ShoppingAddressDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ShoppingAddress record);

    ShoppingAddress selectByPrimaryKey(Integer id);

    List<ShoppingAddress> selectAll();

    int updateByPrimaryKey(ShoppingAddress record);
}