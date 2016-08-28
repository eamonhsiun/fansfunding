package com.fansfunding.fan.message.entity;

import com.fansfunding.internal.ProjectInfo;

/**
 * Created by RJzz on 2016/8/27.
 */

public class Notifacation {

    //返回的结果
    private boolean result;

    //返回的状态码
    private int statusCode;

    //返回的数据
    private DataDetial data;

    //通知的类型
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public DataDetial getData() {
        return data;
    }

    public void setData(DataDetial data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public class DataDetial{

        //通知的触发者
        private Causer causer;

        //通知类型
        private int type;

        //通知相关的项目活动态信息
        private ProjectInfo reference;

        private long time;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public ProjectInfo getReference() {
            return reference;
        }

        public void setReference(ProjectInfo reference) {
            this.reference = reference;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Causer getCauser() {
            return causer;
        }

        public void setCauser(Causer causer) {
            this.causer = causer;
        }
    }
    public class Causer {
        //用户id
        private int id;

        //用户名
        private String name;

        //用户昵称
        private String nickname;

        //头像url
        private String head;
        //个人介绍
        private String intro;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }


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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
