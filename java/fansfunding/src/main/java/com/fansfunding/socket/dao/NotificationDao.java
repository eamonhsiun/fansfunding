package com.fansfunding.socket.dao;

import com.fansfunding.socket.entity.Notification;
import java.util.List;

public interface NotificationDao {
    int deleteByPrimaryKey(Integer id);
    int insert(Notification record);
    Notification selectByPrimaryKey(Integer id);
    void read(Integer id);
    void unread(Integer id);
    List<Notification> selectByReceiver(Integer receiver);
    List<Notification> selectyUnread(Integer receiver);
}