package com.fansfunding.socket.dao;

import com.fansfunding.socket.entity.Message;
import java.util.List;

public interface MessageDao {
    int deleteByPrimaryKey(Integer id);
    int insert(Message record);
    Message selectByPrimaryKey(Integer id);
    void read(Integer id);
    void unread(Integer id);
    List<Message> selectBySender(Integer sender);
    List<Message> selectByReceiver(Integer receiver);
    List<Message> selectUnread(Integer receiver);
}