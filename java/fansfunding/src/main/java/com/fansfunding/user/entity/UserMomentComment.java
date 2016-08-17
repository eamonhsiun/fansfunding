package com.fansfunding.user.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMomentComment {
    private Integer id;

    private Integer userId;
    
    private Integer momentId;

    private Date createTime;

    private String delFlag;

    private String content;
}