package com.fansfunding.project.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;

    private Integer projectId;

    private Integer userId;

    private Integer pointTo;

    private String createBy;

    private Date createTime;

    private String delFlag;

    private String content;

}