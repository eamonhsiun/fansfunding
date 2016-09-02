package com.fansfunding.fan.message.entity;

import java.io.Serializable;

/**
 * Created by RJzz on 2016/9/1.
 */

public class PrivateLetter implements Serializable {

    /**
     * head : user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411469090751135.jpeg
     * intro : 哈哈哈哈
     * nickname : 年轻人啊不要熬夜
     * id : 10000054
     */

    private SenderBean sender;
    /**
     * sender : {"head":"user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411469090751135.jpeg","intro":"哈哈哈哈","nickname":"年轻人啊不要熬夜","id":10000054}
     * content : 你好
     * sendTime : 1472129010488
     */

    private String content;
    private long sendTime;

    public SenderBean getSender() {
        return sender;
    }

    public void setSender(SenderBean sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public static class SenderBean {
        private String head;
        private String intro;
        private String nickname;
        private int id;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
