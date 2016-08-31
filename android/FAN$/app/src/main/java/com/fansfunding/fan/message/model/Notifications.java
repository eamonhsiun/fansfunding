package com.fansfunding.fan.message.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by RJzz on 2016/8/28.
 */

@Table(name = "Notifications")
public class Notifications extends Model{
    //    //用户的id,保证每个用户获取到自己相关的数据
    @Column(name = "userId")
    private int userId;


    //通知的类型
    @Column(name = "type")
    private int type;

    //通知的触发者
    @Column(name = "causer")
    private String causer;

    //通知相关的项目
    @Column(name = "reference")
    private String reference;

    //通知的时间
    @Column(name = "time")
    private long time;

    //是否阅读
    @Column(name = "isRead")
    private boolean isRead;

    //字符串
    @Column(name = "json")
    private String json;

    //将要被删除
    @Column(name = "willDelete")
    private boolean willDelete;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setWillDelete(boolean willDelete) {
        this.willDelete = willDelete;
    }

    public boolean getWillDelete() {
        return this.willDelete;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean getRead() {
        return isRead;
    }

    public int getType() {
        return type;
    }

    public long getTime() {
        return time;

    }

    public String getCauser() {
        return causer;
    }

    public String getReference() {
        return reference;
    }

    public void setTime(long time) {
        this.time = time;

    }

    public void setType(int type) {
        this.type = type;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setCauser(String causer) {
        this.causer = causer;
    }
}



