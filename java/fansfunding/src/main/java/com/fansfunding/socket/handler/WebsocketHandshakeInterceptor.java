package com.fansfunding.socket.handler;

import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * Http握手拦截器
 * @author w_angler
 *
 */

@Component
public class WebsocketHandshakeInterceptor implements HandshakeInterceptor {
	/**
	 * websocket连接建立前的握手，验证参数合法性
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler handler,
			Map<String, Object> attrs) throws Exception {
		HttpServletRequest httpRequest=((ServletServerHttpRequest)request).getServletRequest();
		//用户合法性验证
		String userId =httpRequest.getParameter("userId");
		if(Objects.isNull(userId)||(!userId.matches("\\d+"))){
			return false;
		}
		//TODO 验证token
		String token = httpRequest.getParameter("token");
		if(Objects.isNull(token)||(!validate(token))){
			return false;
		}
		attrs.put("userId", Integer.parseInt(userId));
		attrs.put("token", token);
		return true;
	}
	@Override
	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}
	/**
	 * 验证token
	 * @param token
	 * @return
	 */
	private boolean validate(String token){
		return true;
	}
}
