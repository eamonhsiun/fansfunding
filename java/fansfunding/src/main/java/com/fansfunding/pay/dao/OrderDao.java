package com.fansfunding.pay.dao;

import java.util.List;

import com.fansfunding.pay.entity.Order;

public interface OrderDao {
    int deleteByPrimaryKey(Integer id);
    int insert(Order order);
    Order selectByPrimaryKey(Integer id);
    List<Order> selectAll();
    int updateByPrimaryKey(Order order);
    int updateReturnTime(Order order);
    int updateNotifyTime(Order order);
    Order selectByOrderNo(String orderId);
    Order selectByTradeNo(String tradeId);
    List<Order> selectByUserId(Integer userId);
    List<Order> selectByFeedbackId(Integer feedbackId);
}
