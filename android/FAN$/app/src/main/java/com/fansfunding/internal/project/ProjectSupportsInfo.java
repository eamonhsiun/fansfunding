package com.fansfunding.internal.project;

import java.util.List;

/**
 * Created by 13616 on 2016/8/9.
 */
public class ProjectSupportsInfo {
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

        //总支持人数
        private int total;

        //项目列表
        private List<SupportInfo> list;

        public List<SupportInfo> getList() {
            return list;
        }

        public void setList(List<SupportInfo> list) {
            this.list = list;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public class SupportInfo{
        //支持者头像
        private String head;

        //支持者简介
        private String intro;

        //支持者昵称
        private String nickname;

        //支持者id
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
