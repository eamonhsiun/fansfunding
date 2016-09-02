package com.fansfunding.fan.message.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RJzz on 2016/9/1.
 */
@Table(name = "Messages")
public class Message extends Model implements Serializable{

    //将要被删除
    @Column(name = "willDelete")
    private boolean willDelete;


    //是否阅读
    @Column(name = "isRead")
    private boolean isRead;

    @Column(name = "userId")
    private int userId;


    @Column(name = "time")
    private long time;

    @Column(name = "json")
    private String json;

    //    //内容
//    @Column(name = "content")
    private List<Content> contents;

    //发送者id
    @Column(name = "senderId")
    private int senderId;




    public void setWillDelete(boolean willDelete) {
        this.willDelete = willDelete;
    }


    public boolean getWillDelete() {
        return this.willDelete;
    }



    public void setRead(boolean read) {
        isRead = read;
    }
    public boolean getRead() {
        return this.isRead;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public List<Content> contents() {
        return getMany(Content.class, "Message");
    }

}
