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
	 * 额外数据
	 */
	private Object data;

	public Status(boolean result, int errCode, Object data) {
		super();
		this.result = result;
		this.errCode = errCode;
		this.data = data;
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
}
