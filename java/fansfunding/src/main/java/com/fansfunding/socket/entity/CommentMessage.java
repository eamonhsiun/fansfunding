package com.fansfunding.socket.entity;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentMessage {
	private Map<String,Object> commenter;
	private Map<String,Object> pointTo;
	private String comment;
	private Date time;
}
