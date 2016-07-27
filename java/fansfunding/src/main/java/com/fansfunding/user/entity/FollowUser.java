package com.fansfunding.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowUser {
    private Integer id;
    private Integer userId;
    private Integer followerId;
}