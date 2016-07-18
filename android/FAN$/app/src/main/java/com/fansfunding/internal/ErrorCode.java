package com.fansfunding.internal;

/**
 * Created by 13616 on 2016/7/9.
 */
public class ErrorCode {
    //用来表示请求成功
    public final static int REQUEST_SUCCESS=200;

    //用来表示请求失败
    public final static int REQUEST_FAILURE=201;

    //用来表示请求过于频繁
    public final static int REQUEST_TOO_FRENQUENTLY=202;

    //用来表示参数错误
    public final static int PARAMETER_ERROR=203;

    //用来表示用户不存在
    public final static int USER_NOT_EXIST=204;

    //用来表示用户名已经注册
    public final static int USERNAME_IS_EXIST=205;

    //用来表示验证码过期
    public final static int VERIFICATION_CODE_OVERDUE=206;

    //用来表示验证码错误
    public final static int VERIFICATION_CODE_ERROR=207;

    //用来表示密码错误
    public final static int PASSWORD_ERROR=208;

    //用来表示权限不足
    public final static int AUTHORITY_NOT_ENOUGH=209;




}
