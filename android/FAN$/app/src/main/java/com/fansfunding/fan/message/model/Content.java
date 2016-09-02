package com.fansfunding.fan.message.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by RJzz on 2016/9/1.
 */

@Table(name = "Contents")
public class Content extends Model{
    //消息发送时间
    @Column(name = "time")
    private long time;


    //type 1为我的消息，type 2为对方的消息
    @Column(name = "type")
    private int type;

    //私信内容
    @Column(name = "content")
    private String content;

    //外键
    @Column(name = "Message")
    private Message message;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Message getMessage() {
        return message;
    }


    public void setMessage(Message message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
