package com.fansfunding.fan.message.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by RJzz on 2016/8/29.
 */
@Table(name = "Comments")
public class Comments  extends Model{
    //评论的类型
    @Column(name = "type")
    private int type;

    //评论者
    @Column(name = "commenter")
    private String commenter;

    //评论的内容
    @Column(name = "comment")
    private String comment;

    //评论的时间
    @Column(name = "time")
    private long time;

    //是否阅读
    @Column(name = "isRead")
    private boolean isRead;

    //发给谁
    @Column(name = "pointTo")
    private String pointTo;

    //字符串
    @Column(name = "json")
    private String json;


    public String getPointTo() {
        return pointTo;
    }


    public void setPointTo(String pointTo) {
        this.pointTo = pointTo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



}
