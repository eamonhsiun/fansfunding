package com.fansfunding.internal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13616 on 2016/7/22.
 */
public class SearchUser {
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
        private List<ProjectDetail> list;

        public List<ProjectDetail> getList() {
            return list;
        }

        public void setList(List<ProjectDetail> list) {
            this.list = list;
        }
    }

    public class ProjectDetail implements Serializable,Comparable {

        //用户头像
        private String head;

        //用户id
        private int id;

        //用户简介
        private String intro;

        //用户昵称
        private String nickname;


        @Override
        public int compareTo(Object another) {
            return 0;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }

}
