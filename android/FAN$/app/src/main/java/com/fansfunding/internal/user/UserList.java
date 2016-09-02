package com.fansfunding.internal.user;

import java.util.List;

/**
 * Created by 13616 on 2016/8/30.
 */
public class UserList {


    /**
     * result : true
     * errCode : 200
     * data : {"list":[{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},{"head":"user/head/10000055/EE26908BF9629EEB4B37DAC350F4754A1472360478000.png","intro":null,"nickname":"Pinckney","id":10000055},{"head":"user/head/10000050/F4BD79BE6646D46F4DA5AA77175229411469093416010.jpeg","intro":"会一直","nickname":"兔子要跳墙啦","id":10000050},{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},{"head":"user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411472443956031.jpeg","intro":"别逗了","nickname":"年轻人啊不要熬夜","id":10000054},{"head":"user/head/10000023/F4BD79BE6646D46F4DA5AA77175229411469302133319.jpeg","intro":"鹏程爸爸呦","nickname":"亲爱的儿子请不要改我名字","id":10000023}],"total":6,"pageSize":10,"pages":1,"pageNum":1,"hasPreviousPage":false,"hasNextPage":false,"lastPage":true,"firstPage":true}
     * token : null
     */

    private boolean result;
    private int errCode;
    /**
     * list : [{"head":"user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg","intro":"Dueeu熊设计傻","nickname":"灵魂歌手我的名字就是这么的长呀呀呀呀","id":10000046},{"head":"user/head/10000055/EE26908BF9629EEB4B37DAC350F4754A1472360478000.png","intro":null,"nickname":"Pinckney","id":10000055},{"head":"user/head/10000050/F4BD79BE6646D46F4DA5AA77175229411469093416010.jpeg","intro":"会一直","nickname":"兔子要跳墙啦","id":10000050},{"head":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","intro":"hhhh","nickname":"RJzz","id":10000051},{"head":"user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411472443956031.jpeg","intro":"别逗了","nickname":"年轻人啊不要熬夜","id":10000054},{"head":"user/head/10000023/F4BD79BE6646D46F4DA5AA77175229411469302133319.jpeg","intro":"鹏程爸爸呦","nickname":"亲爱的儿子请不要改我名字","id":10000023}]
     * total : 6
     * pageSize : 10
     * pages : 1
     * pageNum : 1
     * hasPreviousPage : false
     * hasNextPage : false
     * lastPage : true
     * firstPage : true
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
        private boolean lastPage;
        private boolean firstPage;
        /**
         * head : user/head/10000046/F4BD79BE6646D46F4DA5AA77175229411469079559697.jpeg
         * intro : Dueeu熊设计傻
         * nickname : 灵魂歌手我的名字就是这么的长呀呀呀呀
         * id : 10000046
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

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
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
