package com.fansfunding.internal.social;

import java.util.List;

/**
 * Created by 13616 on 2016/8/28.
 */
public class MomentPraise {


    /**
     * result : true
     * errCode : 200
     * data : {"list":[{"head":"user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png","intro":"真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证","nickname":"小熊8","id":10000047}],"total":1,"pageSize":10,"pages":1,"pageNum":1,"hasPreviousPage":false,"hasNextPage":false,"firstPage":true,"lastPage":true}
     * token : null
     */

    private boolean result;
    private int errCode;
    /**
     * list : [{"head":"user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png","intro":"真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证","nickname":"小熊8","id":10000047}]
     * total : 1
     * pageSize : 10
     * pages : 1
     * pageNum : 1
     * hasPreviousPage : false
     * hasNextPage : false
     * firstPage : true
     * lastPage : true
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
         * head : user/head/10000047/EE26908BF9629EEB4B37DAC350F4754A1472353195843.png
         * intro : 真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证真实小熊认证
         * nickname : 小熊8
         * id : 10000047
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
