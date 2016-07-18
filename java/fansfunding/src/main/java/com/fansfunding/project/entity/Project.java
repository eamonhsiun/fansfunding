package com.fansfunding.project.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Integer id;

    private Integer categoryId;

    private Integer sponsor;

    private Integer detailId;

    private Long targetMoney;

    private Date targetDeadline;

    private String cover;

    private String status;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String delFlag;

    private String name;

    private String description;

}