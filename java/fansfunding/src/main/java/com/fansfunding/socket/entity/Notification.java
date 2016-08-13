package com.fansfunding.socket.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Integer id;
    private String title;
    private String content;
    private String receiver;
    private String isRead;
    private String createBy;
    private Date createTime;
    private String delFlag;
}