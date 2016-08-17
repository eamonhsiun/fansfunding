package com.fansfunding.test.websocket;

import org.junit.Before;
import org.junit.Test;

import com.fansfunding.socket.entity.SocketRequest;
import com.fansfunding.socket.util.MessageConverter;

public class TestMessageConverter {
	private SocketRequest request;
	@Before
	public void before(){
		request=SocketRequest.builder().content("content").receiver(123).sender(456).type(1).build();
	}
	@Test
	public void testConvertRequest() {
		String json=MessageConverter.objectToJson(request);
		System.out.println("json:"+json);
		System.out.println(MessageConverter.jsonToRequest("{\"type\":1,\"content\":\"hhh\"}"));
	}
}
