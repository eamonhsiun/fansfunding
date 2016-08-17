package com.fansfunding.socket.handler;

public interface IRequestHandler {
	/**
	 * 处理请求前需要执行的操作
	 */
	public void before();
	/**
	 * 处理
	 */
	public void handle();
	/**
	 * 处理请求后需要执行的操作
	 */
	public void after();
}
