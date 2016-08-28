package com.fansfunding.fan.message.entity;

import com.fansfunding.internal.ProjectInfo;

/**
 * Created by RJzz on 2016/8/28.
 */

public class NotificationProject {
            //通知的触发者
            private com.fansfunding.fan.message.entity.Notifacation.Causer causer;

            //通知类型
            private int type;

            //通知相关的项目活动态信息
            private ProjectInfo reference;

            private long time;

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public ProjectInfo getReference() {
                return reference;
            }

            public void setReference(ProjectInfo reference) {
                this.reference = reference;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public com.fansfunding.fan.message.entity.Notifacation.Causer getCauser() {
                return causer;
            }

            public void setCauser(com.fansfunding.fan.message.entity.Notifacation.Causer causer) {
                this.causer = causer;
            }

}
