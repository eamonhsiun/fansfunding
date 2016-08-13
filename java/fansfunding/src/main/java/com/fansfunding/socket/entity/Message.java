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
public class Message {
    private Integer id;
    private String content;
    private Integer sender;
    private Integer receiver;
    private String isRead;
    private String createBy;
    private Date createTime;
    private String delFlag;
}