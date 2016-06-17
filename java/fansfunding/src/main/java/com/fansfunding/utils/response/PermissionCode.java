package com.fansfunding.utils.response;

public class PermissionCode {
	/**
	 * 无权限
	 */
	public static final int NO_PERMISSION=10;
	
	/**
	 * 仅验证设备唯一性
	 */
	public static final int VERIFY_DEVICE=11;
	/**
	 * 仅确认验证码
	 */
	public static final int VERIFY_CHECKER=12;
	
	/**
	 * 普通权限
	 */
	public static final int PERMISSION_NORMAL=13;
	
	/**
	 * 用户私人权限
	 */
	public static final int PERMISSION_PRIVATE=14;
	
	/**
	 * 管理员权限
	 */
	public static final int PERMISSION_MANAGER=15;
	
	/**
	 * 钱有关操作权限（一次失效）
	 */
	public static final int PERMISSION_MONEY=16;

}
