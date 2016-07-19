package com.fansfunding.project.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMoment {
    private Integer id;

    private Integer projectId;

    private Date createTime;

    private String delFlag;

    private String content;
}