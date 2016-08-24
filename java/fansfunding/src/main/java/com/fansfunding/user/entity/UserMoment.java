package com.fansfunding.user.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMoment {
    private Integer id;

    private Integer userId;
    
    private String nickname;

    private Date createTime;

    private String delFlag;

    private String content;
    
    private Integer origin;
    
    private Integer linkCategory;
    
    private Integer linkProject;
}