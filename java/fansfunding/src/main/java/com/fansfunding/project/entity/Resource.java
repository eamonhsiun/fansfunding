package com.fansfunding.project.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private Integer id;

    private String type;

    private String path;

    private Integer mappingId;

    private Date createTime;

    private String delFlag;

}