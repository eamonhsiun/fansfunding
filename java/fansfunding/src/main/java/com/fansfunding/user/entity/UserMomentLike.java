package com.fansfunding.user.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMomentLike {

    private Integer userId;
    
    private Integer momentId;
    
    private String nickname;

}