package com.fansfunding.fan.message.entity;

/**
 * Created by RJzz on 2015/6/2.
 */
public class Msg {
    //1为我发出的消息,2为收到的消息
    public static final int TYPE_RECEIVED = 2;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;
    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
