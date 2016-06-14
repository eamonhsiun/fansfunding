package com.fansfunding.user.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealInfo {
    private Integer id;
    
    private int userId;
    
    private String realName;

    private Byte sex;

    private String idNumber;

    private String birthPlace;

    private Date birthday;

    private String address;

    private Byte isValidated;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String delFlag;

}