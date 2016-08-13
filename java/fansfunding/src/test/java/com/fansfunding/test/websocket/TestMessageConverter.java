package com.fansfunding.test.websocket;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fansfunding.socket.entity.SocketRequest;
import com.fansfunding.socket.entity.SocketResponse;
import com.fansfunding.socket.util.MessageConverter;
import com.fansfunding.utils.response.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TestMessageConverter {
	private SocketRequest request;
	private SocketResponse response;
	@Before
	public void before(){
		Map<String,Object> params=new LinkedHashMap<>();
		params.put("1", 1);
		params.put("2", "2");
		request=SocketRequest.builder().time(System.currentTimeMillis())
				.token("token")
				.userId(10000046)
				.params(params)
				.build();
		response=SocketResponse.builder().result(true)
				.sessionId("会话IdsessionId")
				.statusCode(StatusCode.SUCCESS)
				.content(params)
				.build();
	}

	@Test
	public void testConvertSocketResponse() {
		try {
			System.out.println(MessageConverter.responseToJson(response));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConvertRequest() {
		try {
			String json=MessageConverter.objectToJson(request);
			System.out.println("json:"+json);
			System.out.println(MessageConverter.jsonToRequest(json));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
