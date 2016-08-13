package com.fansfunding.socket.entity;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * websocket请求
 * @author w-angler
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocketRequest {
	/**
	 * 请求时间
	 */
	private long time;
	/**
	 * 内容
	 */
	private int userId;
	/**
	 * Token
	 */
	private String token;
	/**
	 * 请求参数
	 */
	private Map<String,Object> params;
}
