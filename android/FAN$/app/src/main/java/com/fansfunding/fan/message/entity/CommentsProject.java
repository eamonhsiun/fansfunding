package com.fansfunding.fan.message.entity;

import com.fansfunding.internal.ProjectInfo;

/**
 * Created by RJzz on 2016/8/29.
 */

public class CommentsProject {


    /**
     * head : default/head/avater.png
     * intro : null
     * nickname : 15071238470
     * id : 10000060
     */

    private CommenterBean commenter;
    /**
     * sponsor : 10000051
     * images : ["project/attachments/1/projects/1472345520859/568E35D0BD3ACBC098B19048CEF027901472345520859.jpeg"]
     * description : 乱来了
     * detailId : 207
     * sum : 0
     * targetMoney : 555
     * cover : project/attachments/1/projects/1472345520859/568E35D0BD3ACBC098B19048CEF027901472345520859.jpeg
     * sponsorHead : user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg
     * sponsorNickname : RJzz
     * createTime : 1472345520000
     * targetDeadline : 1472400000000
     * name : 哦哦哦
     * id : 226
     * categoryId : 1
     * status : 1
     * supportNum : 0
     */

    private ProjectInfo pointTo;
    /**
     * commenter : {"head":"default/head/avater.png","intro":null,"nickname":"15071238470","id":10000060}
     * pointTo : {"sponsor":10000051,"images":["project/attachments/1/projects/1472345520859/568E35D0BD3ACBC098B19048CEF027901472345520859.jpeg"],"description":"乱来了","detailId":207,"sum":0,"targetMoney":555,"cover":"project/attachments/1/projects/1472345520859/568E35D0BD3ACBC098B19048CEF027901472345520859.jpeg","sponsorHead":"user/head/10000051/F4BD79BE6646D46F4DA5AA77175229411469204633930.jpeg","sponsorNickname":"RJzz","createTime":1472345520000,"targetDeadline":1472400000000,"name":"哦哦哦","id":226,"categoryId":1,"status":"1","supportNum":0}
     * comment : 乱来了
     * type : 1
     * time : 1472546105140
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
        private Object intro;
        private String nickname;
        private int id;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public Object getIntro() {
            return intro;
        }

        public void setIntro(Object intro) {
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

    public ProjectInfo getPointTo() {
        return pointTo;
    }

    public void setPointTo(ProjectInfo pointTo) {
        this.pointTo = pointTo;
    }
}
