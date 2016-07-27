package com.fansfunding.internal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 13616 on 2016/7/22.
 */
public class ProjectInfo implements Serializable,Comparable{

    //项目缩略id
    private int id;

    //项目目录id
    private int categoryId;

    //封面
    private String cover;

    //项目描述
    private String description;

    //详情id
    private int detailId;

    //项目名称
    private String name;

    //发起人id
    private int sponsor;

    //发起人昵称
    private String sponsorNickname;

    //发起人头像
    private String sponsorHead;

    //截止日期
    private long targetDeadline;

    //筹集金额
    private BigDecimal targetMoney;

    //已筹金额
    private BigDecimal sum;

    //支持人数
    private int supportNum;

    //创建时期
    private long createTime;

    //项目状态
    private char status;

    //图片
    private List<String> images;


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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getTargetDeadline() {
        return targetDeadline;
    }

    public void setTargetDeadline(long targetDeadline) {
        this.targetDeadline = targetDeadline;
    }

    public BigDecimal getTargetMoney() {
        return targetMoney;
    }

    public void setTargetMoney(BigDecimal targetMoney) {
        this.targetMoney = targetMoney;
    }

    public int getSponsor() {
        return sponsor;
    }

    public void setSponsor(int sponsor) {
        this.sponsor = sponsor;
    }

    public String getSponsorNickname() {
        return sponsorNickname;
    }

    public void setSponsorNickname(String sponsorNickname) {
        this.sponsorNickname = sponsorNickname;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public int getSupportNum() {
        return supportNum;
    }

    public void setSupportNum(int supportNum) {
        this.supportNum = supportNum;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSponsorHead() {
        return sponsorHead;
    }

    public void setSponsorHead(String sponsorHead) {
        this.sponsorHead = sponsorHead;
    }

    @Override
    public int compareTo(Object another) {
        ProjectInfo other=(ProjectInfo)another;
        if(this.getCreateTime()>other.getCreateTime()){
            return -1;
        }
        return 1;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}