package com.fansfunding.internal;

import java.util.List;

/**
 * Created by 13616 on 2016/7/20.
 */
public class ProjectDetailDynamic {
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
        private List<ProjectDynamic> list;

        public List<ProjectDynamic> getList() {
            return list;
        }

        public void setList(List<ProjectDynamic> list) {
            this.list = list;
        }
    }

    public class ProjectDynamic {
        //发起人id
        private int sponsor;

        //图片
        private List<String> images;

        //发起人昵称
        private String sponsorNickname;

        //发起人头像
        private String sponsorHead;

        //动态内容
        private String content;

        //更新时间
        private long updateTime;


        public int getSponsor() {
            return sponsor;
        }

        public void setSponsor(int sponsor) {
            this.sponsor = sponsor;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getSponsorNickname() {
            return sponsorNickname;
        }

        public void setSponsorNickname(String sponsorNickname) {
            this.sponsorNickname = sponsorNickname;
        }

        public String getSponsorHead() {
            return sponsorHead;
        }

        public void setSponsorHead(String sponsorHead) {
            this.sponsorHead = sponsorHead;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
