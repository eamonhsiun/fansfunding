package com.fansfunding.socket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * websocket响应
 * @author w-angler
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocketResponse {
	/**
	 * 请求成功与否
	 */
	private boolean result;
	/**
	 * 状态码
	 */
	private int statusCode;
	/**
	 * 会话id
	 */
	private String sessionId;
	/**
	 * 内容
	 */
	private Object content;
}
