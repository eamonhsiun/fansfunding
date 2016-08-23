package com.fansfunding.socket.entity;

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
	private int receiver;
	private String content;
}
