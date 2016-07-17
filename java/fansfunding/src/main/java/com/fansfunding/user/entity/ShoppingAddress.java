package com.fansfunding.user.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingAddress {
    private Integer id;

    private Integer userId;
    
    private String phone;
    
    private String name;

    private String province;

    private String city;

    private String district;
    
    private int post_code;

    private String address;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String delFlag;

}