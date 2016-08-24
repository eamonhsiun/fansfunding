package com.fansfunding.socket.entity;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotificationMessage {
	private Map<String,Object> causer;
	private int type;
	private Map<String,Object> reference;
}
