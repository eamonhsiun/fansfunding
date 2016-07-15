package com.fansfunding.utils.response;

public class StatusCode {
	/**
	 * 请求成功
	 */
	public static final int SUCCESS=200;
	/**
	 * 请求失败
	 */
	public static final int FAILD=201;
	/**
	 * 请求过于频繁
	 */
	public static final int TOO_FREQUENT=202;
	
	/**
	 * 参数错误
	 */
	public static final int ERROR_DATA=203;
	
	/**
	 * 用户名错误（用户不存在）
	 */
	public static final int USER_NULL=204;	
	
	/**
	 * 用户名已注册
	 */
	public static final int USER_EXIST=205;	
	
	/**
	 * 验证码过期
	 */
	public static final int CHECKER_N_VAILD=206;
	
	/**
	 * 验证码错误
	 */
	public static final int CHECKER_ERROR=207;

	/**
	 * 密码错误
	 */
	public static final int PASSWORD_ERROR=208;
	
	/**
	 * 没有权限
	 */
	public static final int PERMISSION_LOW=209;
	
	/**
	 * 文件上传失败
	 */
	public static final int FILEUPLOAD_ERROR=420;
	
	/**
	 * 文件过大
	 */
	public static final int FILE_TOO_LARGE=530;
	/**
	 * 邮箱错误
	 */
	/**
	 * 邮箱已存在
	 */
}