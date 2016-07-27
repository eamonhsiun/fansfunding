package com.fansfunding.internal;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 13616 on 2016/7/19.
 */
public class ProjectDetailComment {
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
        private List<ProjectComment> list;

        public List<ProjectComment> getList() {
            return list;
        }

        public void setList(List<ProjectComment> list) {
            this.list = list;
        }
    }


    public class ProjectComment implements Comparable{


        //评论人头像
        private String commenterHead;

        //评论指向
        private int pointTo;
        //评论指向的名字，即要回复的用户名字
        private String pointToName;

        //评论指向的名字，即要回复的用户名字
        private String pointToNickname;

        //评论人姓名
        private String commenterName;

        //id
        private int id;

        //评论人昵称
        private String commenterNickname;



        //项目id
        private int projectId;

        //评论内容
        private String content;

        //评论人id
        private int commenterId;

        //评论时间
        private long commentTime;




        public String getCommenterHead() {
            return commenterHead;
        }

        public void setCommenterHead(String commenterHead) {
            this.commenterHead = commenterHead;
        }

        public int getPointTo() {
            return pointTo;
        }

        public void setPointTo(int pointTo) {
            this.pointTo = pointTo;
        }

        public String getCommenterName() {
            return commenterName;
        }

        public void setCommenterName(String commenterName) {
            this.commenterName = commenterName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCommenterNickname() {
            return commenterNickname;
        }

        public void setCommenterNickname(String commenterNickname) {
            this.commenterNickname = commenterNickname;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCommenterId() {
            return commenterId;
        }

        public void setCommenterId(int commenterId) {
            this.commenterId = commenterId;
        }

        public long getCommentTime() {
            return commentTime;
        }

        public void setCommentTime(long commentTime) {
            this.commentTime = commentTime;
        }

        public String getPointToName() {
            return pointToName;
        }

        public void setPointToName(String pointToName) {
            this.pointToName = pointToName;
        }


        @Override
        public int compareTo(Object another) {
            ProjectComment other=(ProjectComment)another;
            if(this.getCommentTime()>other.getCommentTime()){
                return -1;
            }
            return 1;
        }

        public String getPointToNickname() {
            return pointToNickname;
        }

        public void setPointToNickname(String pointToNickname) {
            this.pointToNickname = pointToNickname;
        }
    }
}
