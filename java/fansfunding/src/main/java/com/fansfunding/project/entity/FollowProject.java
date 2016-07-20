package com.fansfunding.project.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowProject {
    private Integer id;
    private Integer userId;
    private Integer projectId;
    private Date createTime;
    private String delFlag;
}