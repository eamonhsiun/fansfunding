package com.fansfunding.socket.util;

import java.io.IOException;

import com.fansfunding.socket.entity.SocketRequest;
import com.fansfunding.socket.entity.SocketResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * websocket消息转换器
 * @author w-angler
 *
 */
public class MessageConverter {
	/**
	 * Jackson转换对象
	 */
	private static ObjectMapper mapper=new ObjectMapper();
	
	private MessageConverter(){
		throw new IllegalArgumentException("You can not new an instance of this class");
	}
	/**
	 * 转换为json
	 * @param obj 待转换对象
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String objectToJson(Object obj)
			throws JsonProcessingException{
		return mapper.writeValueAsString(obj);
	}
	/**
	 * 转换响应为json
	 * @param response 响应信息
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String responseToJson(SocketResponse response)
			throws JsonProcessingException{
		return mapper.writeValueAsString(response);
	}
	/**
	 * 将请求转换为对象
	 * @param request 请求json
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static SocketRequest jsonToRequest(String request)
			throws JsonParseException, JsonMappingException, IOException{
		return mapper.readValue(request, SocketRequest.class);
	}
}
