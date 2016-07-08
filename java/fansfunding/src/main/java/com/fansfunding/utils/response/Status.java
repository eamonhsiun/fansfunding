package com.fansfunding.utils.response;


public class Status {
	/**
	 * 状态值
	 */
	private boolean result;
	
	/**
	 * 状态码
	 */
	private int errCode;
	
	/**
	 * 需要让用户知道的数据
	 */
	private Object data;
	
	/**
	 * 加密验证数据（保护内部逻辑）
	 */
	private Object token;


	public Status(boolean result, int errCode, Object data,Object token) {
		super();
		this.result = result;
		this.errCode = errCode;
		this.data = data;
		this.token = token;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getToken() {
		return token;
	}

	public void setToken(Object token) {
		this.token = token;
	}

	
}
