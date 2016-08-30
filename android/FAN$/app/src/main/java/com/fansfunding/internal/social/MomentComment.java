package com.fansfunding.internal.social;

import java.util.List;

/**
 * Created by 13616 on 2016/8/29.
 */
public class MomentComment {


    /**
     * result : true
     * errCode : 200
     * data : {"list":[{"postTime":1472482914000,"replyTo":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"commentId":29,"user":{"head":"user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png","intro":"真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证","nickname":"小熊8","id":10000047},"content":"傻"},{"postTime":1472479433000,"replyTo":null,"commentId":23,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"傻"},{"postTime":1472479433000,"replyTo":null,"commentId":24,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"傻"},{"postTime":1472477226000,"replyTo":null,"commentId":21,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"好吧"},{"postTime":1472476591000,"replyTo":null,"commentId":20,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"时刻"},{"postTime":1472476176000,"replyTo":null,"commentId":19,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"是你吗"},{"postTime":1472473141000,"replyTo":null,"commentId":16,"user":{"head":"user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png","intro":"真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证","nickname":"小熊8","id":10000047},"content":"什么"},{"postTime":1472471865000,"replyTo":null,"commentId":14,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"abc"},{"postTime":1472471803000,"replyTo":null,"commentId":13,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"ab"},{"postTime":1472471502000,"replyTo":null,"commentId":12,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"纳斯卡"}],"total":13,"pageSize":10,"pages":2,"pageNum":1,"hasPreviousPage":false,"hasNextPage":true,"firstPage":true,"lastPage":false}
     * token : 0
     */

    private boolean result;
    private int errCode;
    /**
     * list : [{"postTime":1472482914000,"replyTo":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"commentId":29,"user":{"head":"user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png","intro":"真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证","nickname":"小熊8","id":10000047},"content":"傻"},{"postTime":1472479433000,"replyTo":null,"commentId":23,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"傻"},{"postTime":1472479433000,"replyTo":null,"commentId":24,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"傻"},{"postTime":1472477226000,"replyTo":null,"commentId":21,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"好吧"},{"postTime":1472476591000,"replyTo":null,"commentId":20,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"时刻"},{"postTime":1472476176000,"replyTo":null,"commentId":19,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"是你吗"},{"postTime":1472473141000,"replyTo":null,"commentId":16,"user":{"head":"user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png","intro":"真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证","nickname":"小熊8","id":10000047},"content":"什么"},{"postTime":1472471865000,"replyTo":null,"commentId":14,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"abc"},{"postTime":1472471803000,"replyTo":null,"commentId":13,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"ab"},{"postTime":1472471502000,"replyTo":null,"commentId":12,"user":{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},"content":"纳斯卡"}]
     * total : 13
     * pageSize : 10
     * pages : 2
     * pageNum : 1
     * hasPreviousPage : false
     * hasNextPage : true
     * firstPage : true
     * lastPage : false
     */

    private DataBean data;

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

    public static class DataBean {
        private int total;
        private int pageSize;
        private int pages;
        private int pageNum;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private boolean firstPage;
        private boolean lastPage;
        /**
         * postTime : 1472482914000
         * replyTo : {"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻打开q","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046}
         * commentId : 29
         * user : {"head":"user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png","intro":"真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证","nickname":"小熊8","id":10000047}
         * content : 傻
         */

        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private long postTime;
            /**
             * head : user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg
             * intro : Dueeu熊设计傻打开q
             * nickname : 灵魂歌手我的名字就是这么的长呀呀呀呀
             * id : 10000046
             */

            private ReplyToBean replyTo;
            private int commentId;
            /**
             * head : user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png
             * intro : 真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证
             * nickname : 小熊8
             * id : 10000047
             */

            private UserBean user;
            private String content;

            public long getPostTime() {
                return postTime;
            }

            public void setPostTime(long postTime) {
                this.postTime = postTime;
            }

            public ReplyToBean getReplyTo() {
                return replyTo;
            }

            public void setReplyTo(ReplyToBean replyTo) {
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

            public static class ReplyToBean {
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
