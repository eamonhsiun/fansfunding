package com.fansfunding.socket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SocketResponse {
	private boolean result;
	/**
	 * 消息类型，0请求状态，1私聊，2评论，3通知
	 */
	private int type;
	private int statusCode;
	private Object data;
}
