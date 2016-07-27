package com.fansfunding.internal.project;

import java.util.List;

/**
 * Created by 13616 on 2016/7/24.
 */
public class ProjectFollower {

    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private DataDetial data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public DataDetial getData() {
        return data;
    }

    public void setData(DataDetial data) {
        this.data = data;
    }

    public class DataDetial{
        //项目列表
        private List<Followers> list;

        //总共关注的人数
        private int total;

        public List<Followers> getList() {
            return list;
        }

        public void setList(List<Followers> list) {
            this.list = list;
        }


        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public class Followers{

        //关注者头像
        private String head;

        //关注着简介
        private String intro;

        //关注者昵称
        private String nickname;

        //关注者id
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
