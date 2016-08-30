package com.fansfunding.internal.social;

import java.util.List;

/**
 * Created by 13616 on 2016/8/27.
 */
public class SingleUserMoment {


    /**
     * result : true
     * errCode : 200
     * data : {"commentNum":1,"postTime":1472295007000,"images":["user/moment/10000046/F8DEB367D589597524DE3207DC6E9E921472295007328.jpeg","user/moment/10000046/AF2E005256072325D9F00B7CEDE070891472295007343.jpeg","user/moment/10000046/C977F910A358619B4CE2DD1984022FED1472295007406.jpeg","user/moment/10000046/3F0CEF57E5C295D0CB7F9288CC9503F11472295007406.jpeg","user/moment/10000046/F034FB1EE0B2BEE221B40E3FFA6700631472295007421.jpeg"],"isLike":false,"forwardNum":0,"origin":0,"project":{"sponsor":10000054,"images":[],"description":"费力不讨好，负能量爆棚","detailId":200,"sum":0,"targetMoney":1,"cover":"project/attachments/1/projects/1471882261675/EE26908BF9629EEB4B37DAC350F4754A1471882261675.png","sponsorHead":"user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411472443956031.jpeg","sponsorNickname":"年轻人啊不要熬夜","createTime":1471882261000,"targetDeadline":1472313600000,"name":"费力不讨好","id":219,"categoryId":1,"status":"1","supportNum":0},"comment":[{"postTime":1472470651000,"replyTo":null,"commentId":8,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"世界室内1"}],"momentId":34,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"删动态者傻汪也😔","likeNum":0}
     * token : 0
     */

    private boolean result;
    private int errCode;
    /**
     * commentNum : 1
     * postTime : 1472295007000
     * images : ["user/moment/10000046/F8DEB367D589597524DE3207DC6E9E921472295007328.jpeg","user/moment/10000046/AF2E005256072325D9F00B7CEDE070891472295007343.jpeg","user/moment/10000046/C977F910A358619B4CE2DD1984022FED1472295007406.jpeg","user/moment/10000046/3F0CEF57E5C295D0CB7F9288CC9503F11472295007406.jpeg","user/moment/10000046/F034FB1EE0B2BEE221B40E3FFA6700631472295007421.jpeg"]
     * isLike : false
     * forwardNum : 0
     * origin : 0
     * project : {"sponsor":10000054,"images":[],"description":"费力不讨好，负能量爆棚","detailId":200,"sum":0,"targetMoney":1,"cover":"project/attachments/1/projects/1471882261675/EE26908BF9629EEB4B37DAC350F4754A1471882261675.png","sponsorHead":"user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411472443956031.jpeg","sponsorNickname":"年轻人啊不要熬夜","createTime":1471882261000,"targetDeadline":1472313600000,"name":"费力不讨好","id":219,"categoryId":1,"status":"1","supportNum":0}
     * comment : [{"postTime":1472470651000,"replyTo":null,"commentId":8,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"世界室内1"}]
     * momentId : 34
     * user : {"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046}
     * content : 删动态者傻汪也😔
     * likeNum : 0
     */

    private DataBean data;
    private int token;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public static class DataBean {
        private int commentNum;
        private long postTime;
        private boolean isLike;
        private int forwardNum;
        private int origin;
        /**
         * sponsor : 10000054
         * images : []
         * description : 费力不讨好，负能量爆棚
         * detailId : 200
         * sum : 0.0
         * targetMoney : 1
         * cover : project/attachments/1/projects/1471882261675/EE26908BF9629EEB4B37DAC350F4754A1471882261675.png
         * sponsorHead : user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411472443956031.jpeg
         * sponsorNickname : 年轻人啊不要熬夜
         * createTime : 1471882261000
         * targetDeadline : 1472313600000
         * name : 费力不讨好
         * id : 219
         * categoryId : 1
         * status : 1
         * supportNum : 0
         */

        private ProjectBean project;
        private int momentId;
        /**
         * head : user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg
         * intro : Dueeu熊
         * nickname : 灵魂歌手我的名字就是这么的长呀呀呀呀
         * id : 10000046
         */

        private UserBean user;
        private String content;
        private int likeNum;
        private List<String> images;
        /**
         * postTime : 1472470651000
         * replyTo : null
         * commentId : 8
         * user : {"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046}
         * content : 世界室内1
         */

        private List<CommentBean> comment;

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public long getPostTime() {
            return postTime;
        }

        public void setPostTime(long postTime) {
            this.postTime = postTime;
        }

        public boolean isIsLike() {
            return isLike;
        }

        public void setIsLike(boolean isLike) {
            this.isLike = isLike;
        }

        public int getForwardNum() {
            return forwardNum;
        }

        public void setForwardNum(int forwardNum) {
            this.forwardNum = forwardNum;
        }

        public int getOrigin() {
            return origin;
        }

        public void setOrigin(int origin) {
            this.origin = origin;
        }

        public ProjectBean getProject() {
            return project;
        }

        public void setProject(ProjectBean project) {
            this.project = project;
        }

        public int getMomentId() {
            return momentId;
        }

        public void setMomentId(int momentId) {
            this.momentId = momentId;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
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

        public static class ProjectBean {
            private int sponsor;
            private String description;
            private int detailId;
            private double sum;
            private int targetMoney;
            private String cover;
            private String sponsorHead;
            private String sponsorNickname;
            private long createTime;
            private long targetDeadline;
            private String name;
            private int id;
            private int categoryId;
            private String status;
            private int supportNum;
            private List<String> images;

            public int getSponsor() {
                return sponsor;
            }

            public void setSponsor(int sponsor) {
                this.sponsor = sponsor;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getDetailId() {
                return detailId;
            }

            public void setDetailId(int detailId) {
                this.detailId = detailId;
            }

            public double getSum() {
                return sum;
            }

            public void setSum(double sum) {
                this.sum = sum;
            }

            public int getTargetMoney() {
                return targetMoney;
            }

            public void setTargetMoney(int targetMoney) {
                this.targetMoney = targetMoney;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getSponsorHead() {
                return sponsorHead;
            }

            public void setSponsorHead(String sponsorHead) {
                this.sponsorHead = sponsorHead;
            }

            public String getSponsorNickname() {
                return sponsorNickname;
            }

            public void setSponsorNickname(String sponsorNickname) {
                this.sponsorNickname = sponsorNickname;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getTargetDeadline() {
                return targetDeadline;
            }

            public void setTargetDeadline(long targetDeadline) {
                this.targetDeadline = targetDeadline;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getSupportNum() {
                return supportNum;
            }

            public void setSupportNum(int supportNum) {
                this.supportNum = supportNum;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }
        }

        public static class UserBean {
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

        public static class CommentBean {
            private long postTime;
            private Object replyTo;
            private int commentId;
            /**
             * head : user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg
             * intro : Dueeu熊
             * nickname : 灵魂歌手我的名字就是这么的长呀呀呀呀
             * id : 10000046
             */

            private UserBean user;
            private String content;

            public long getPostTime() {
                return postTime;
            }

            public void setPostTime(long postTime) {
                this.postTime = postTime;
            }

            public Object getReplyTo() {
                return replyTo;
            }

            public void setReplyTo(Object replyTo) {
                this.replyTo = replyTo;
            }

            public int getCommentId() {
                return commentId;
            }

            public void setCommentId(int commentId) {
                this.commentId = commentId;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public static class UserBean {
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
    }
}
