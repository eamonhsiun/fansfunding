package com.fansfunding.fan.message.entity;

import java.util.List;

/**
 * Created by RJzz on 2016/8/30.
 */

public class CommentDynamic  {

    /**
     * head : user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg
     * intro : hhhh
     * nickname : RJzz
     * id : 10000051
     */

    private CommenterBean commenter;
    /**
     * commentNum : 8
     * postTime : 1472534362000
     * images : []
     * isLike : true
     * forwardNum : 0
     * origin : 0
     * project : null
     * comment : [{"postTime":1472537260000,"replyTo":null,"commentId":54,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"ccc"},{"postTime":1472537225000,"replyTo":null,"commentId":53,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"CCC"},{"postTime":1472537215000,"replyTo":null,"commentId":52,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"hhh "},{"postTime":1472537181000,"replyTo":null,"commentId":51,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"ugh GGG"},{"postTime":1472537121000,"replyTo":null,"commentId":50,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"BBC"},{"postTime":1472537064000,"replyTo":null,"commentId":49,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"GGG"},{"postTime":1472536563000,"replyTo":null,"commentId":48,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"😝😝😝😝😝😝😝😝"},{"postTime":1472534370000,"replyTo":null,"commentId":47,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"😃😃😃😃😃"}]
     * momentId : 51
     * user : {"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051}
     * content : ggggg
     * likeNum : 1
     */

    private PointToBean pointTo;
    /**
     * commenter : {"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051}
     * pointTo : {"commentNum":8,"postTime":1472534362000,"images":[],"isLike":true,"forwardNum":0,"origin":0,"project":null,"comment":[{"postTime":1472537260000,"replyTo":null,"commentId":54,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"ccc"},{"postTime":1472537225000,"replyTo":null,"commentId":53,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"CCC"},{"postTime":1472537215000,"replyTo":null,"commentId":52,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"hhh "},{"postTime":1472537181000,"replyTo":null,"commentId":51,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"ugh GGG"},{"postTime":1472537121000,"replyTo":null,"commentId":50,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"BBC"},{"postTime":1472537064000,"replyTo":null,"commentId":49,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"GGG"},{"postTime":1472536563000,"replyTo":null,"commentId":48,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"😝😝😝😝😝😝😝😝"},{"postTime":1472534370000,"replyTo":null,"commentId":47,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"😃😃😃😃😃"}],"momentId":51,"user":{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},"content":"ggggg","likeNum":1}
     * comment : ccc
     * type : 2
     * time : 1472537260937
     */

    private String comment;
    private int type;
    private long time;

    public CommenterBean getCommenter() {
        return commenter;
    }

    public void setCommenter(CommenterBean commenter) {
        this.commenter = commenter;
    }

    public PointToBean getPointTo() {
        return pointTo;
    }

    public void setPointTo(PointToBean pointTo) {
        this.pointTo = pointTo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class CommenterBean {
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

    public static class PointToBean {
        private int commentNum;
        private long postTime;
        private boolean isLike;
        private int forwardNum;
        private int origin;
        private Object project;
        private int momentId;
        /**
         * head : user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg
         * intro : hhhh
         * nickname : RJzz
         * id : 10000051
         */

        private UserBean user;
        private String content;
        private int likeNum;
        private List<?> images;
        /**
         * postTime : 1472537260000
         * replyTo : null
         * commentId : 54
         * user : {"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051}
         * content : ccc
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

        public Object getProject() {
            return project;
        }

        public void setProject(Object project) {
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

        public List<?> getImages() {
            return images;
        }

        public void setImages(List<?> images) {
            this.images = images;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
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
             * head : user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg
             * intro : hhhh
             * nickname : RJzz
             * id : 10000051
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
