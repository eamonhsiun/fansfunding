package com.fansfunding.fan.project.entity;

import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by Eamon on 2016/7/23.
 */
public class Reward {
    private double mSupportMoney;
    private String mContent;
    private List<PhotoInfo> mPhotoList;
    private int ceiling;


    public List<PhotoInfo> getPhotoList() {
        return mPhotoList;
    }

    public void setPhotoList(List<PhotoInfo> mPhotoList) {
        this.mPhotoList = mPhotoList;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public double getSupportMoney() {
        return mSupportMoney;
    }

    public void setSupportMoney(double mSupportMoney) {
        this.mSupportMoney = mSupportMoney;
    }


    public int getCeiling() {
        return ceiling;
    }

    public void setCeiling(int ceiling) {
        this.ceiling = ceiling;
    }
}
