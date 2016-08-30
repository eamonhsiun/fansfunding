package com.fansfunding.fan.message.entity;

import java.util.List;

/**
 * Created by RJzz on 2016/8/29.
 */

public class CommentsProject {


    /**
     * head : user/head/10000053/EE26908BF9629EEB4B37DAC350F4754A1469248222631.png
     * intro : 没有什么可以介绍的
     * nickname : 小熊
     * id : 10000053
     */

    private CommenterBean commenter;
    /**
     * sponsor : 10000054
     * images : ["project/attachments/1/projects/1472108638706/7A74557516E48C1AB085F2CFDA45EF7A1472108638706.jpeg"]
     * description : 不清楚哎，怎么回事？
     * detailId : 201
     * sum : 0
     * targetMoney : 2333
     * cover : project/attachments/1/projects/1472108638706/7A74557516E48C1AB085F2CFDA45EF7A1472108638706.jpeg
     * sponsorHead : user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411469090751135.jpeg
     * sponsorNickname : 年轻人啊不要熬夜
     * createTime : 1472108638000
     * targetDeadline : 1472313600000
     * name : 上传有问题了吗？
     * id : 220
     * categoryId : 1
     * status : 2
     * supportNum : 0
     */

    private PointToBean pointTo;
    /**
     * commenter : {"head":"user/head/10000053/EE26908BF9629EEB4B37DAC350F4754A1469248222631.png","intro":"没有什么可以介绍的","nickname":"小熊","id":10000053}
     * pointTo : {"sponsor":10000054,"images":["project/attachments/1/projects/1472108638706/7A74557516E48C1AB085F2CFDA45EF7A1472108638706.jpeg"],"description":"不清楚哎，怎么回事？","detailId":201,"sum":0,"targetMoney":2333,"cover":"project/attachments/1/projects/1472108638706/7A74557516E48C1AB085F2CFDA45EF7A1472108638706.jpeg","sponsorHead":"user/head/10000054/F4BD79BE6646D46F4DA5AA77175229411469090751135.jpeg","sponsorNickname":"年轻人啊不要熬夜","createTime":1472108638000,"targetDeadline":1472313600000,"name":"上传有问题了吗？","id":220,"categoryId":1,"status":"2","supportNum":0}
     * comment : 评论内容
     * type : 1
     * time : 1472117088378
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
        private int sponsor;
        private String description;
        private int detailId;
        private int sum;
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

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
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
}
