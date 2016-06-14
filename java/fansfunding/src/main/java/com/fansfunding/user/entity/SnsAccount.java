package com.fansfunding.user.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnsAccount {
    private Integer id;

    private Integer userId;

    private Byte type;

    private String account;

    private String accountUserName;

    private Byte isValidated;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String delFlag;

}