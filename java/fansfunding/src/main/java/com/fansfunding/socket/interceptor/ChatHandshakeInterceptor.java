package com.fansfunding.socket.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * 私聊握手拦截器
 * @author w_angler
 *
 */

@Component
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		String sender = ((ServletServerHttpRequest) request).getServletRequest().getParameter("sender");
		String receiver=((ServletServerHttpRequest) request).getServletRequest().getParameter("receiver");
		String token = ((ServletServerHttpRequest)request).getServletRequest().getParameter("token");
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}
	@Override
	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		super.afterHandshake(request, response, wsHandler, exception);
	}
}
