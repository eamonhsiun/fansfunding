package com.fansfunding.internal.social;

import java.util.List;

/**
 * Created by 13616 on 2016/8/24.
 */
public class UserMoment {
    /**
     * result : true
     * errCode : 200
     * data : [{"userId":10000023,"nickname":"亲爱的儿子请不要改我名字","momentId":10,"postTime":1471371194000,"images":["user/10000046/moment/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg"],"forwardNum":0,"commentNum":1,"likeNum":0,"origin":0,"linkProject":0,"content":"dasf","comment":[{"postTime":1471433926000,"nickname":"亲爱的儿子请不要改我名字","commentId":1,"content":"safdsaf"}]}]
     * token : 0
     */

    private boolean result;
    private int errCode;
    /**
     * userId : 10000023
     * nickname : 亲爱的儿子请不要改我名字
     * momentId : 10
     * postTime : 1471371194000
     * images : ["user/10000046/moment/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg"]
     * forwardNum : 0
     * commentNum : 1
     * likeNum : 0
     * origin : 0
     * linkProject : 0
     * content : dasf
     * comment : [{"postTime":1471433926000,"nickname":"亲爱的儿子请不要改我名字","commentId":1,"content":"safdsaf"}]
     */

    private List<DataBean> data;

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


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int userId;
        private String nickname;
        private int momentId;
        private long postTime;
        private int forwardNum;
        private int commentNum;
        private int likeNum;
        private int origin;
        private int linkCategory;
        private int linkProject;
        private String content;
        private List<String> images;
        /**
         * postTime : 1471433926000
         * nickname : 亲爱的儿子请不要改我名字
         * commentId : 1
         * content : safdsaf
         */

        private List<CommentBean> comment;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getMomentId() {
            return momentId;
        }

        public void setMomentId(int momentId) {
            this.momentId = momentId;
        }

        public long getPostTime() {
            return postTime;
        }

        public void setPostTime(long postTime) {
            this.postTime = postTime;
        }

        public int getForwardNum() {
            return forwardNum;
        }

        public void setForwardNum(int forwardNum) {
            this.forwardNum = forwardNum;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public int getOrigin() {
            return origin;
        }

        public void setOrigin(int origin) {
            this.origin = origin;
        }

        public int getLinkCategory() {
            return linkCategory;
        }

        public void setLinkCategory(int linkCategory) {
            this.linkCategory = linkCategory;
        }

        public int getLinkProject() {
            return linkProject;
        }

        public void setLinkProject(int linkProject) {
            this.linkProject = linkProject;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public static class CommentBean {
            private long postTime;
            private String nickname;
            private int commentId;
            private String content;

            public long getPostTime() {
                return postTime;
            }

            public void setPostTime(long postTime) {
                this.postTime = postTime;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getCommentId() {
                return commentId;
            }

            public void setCommentId(int commentId) {
                this.commentId = commentId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
