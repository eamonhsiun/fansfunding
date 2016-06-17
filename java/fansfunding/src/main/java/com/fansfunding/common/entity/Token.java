package com.fansfunding.common.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    /**
     * 主键
     */
    private Integer id;
    
    /**
     * 值
     */
    private String value;
    
    /**
     * 权限
     */
    private int Permission;

    /**
     * 请求时间
     */
    private Date request_time;
    
    /**
     * 过期时间
     */
    private Date expire_time;
    
    /**
     * 关联手机号
     */
    private String phone;
    
}