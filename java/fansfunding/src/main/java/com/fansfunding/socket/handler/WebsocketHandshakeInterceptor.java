package com.fansfunding.socket.handler;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.fansfunding.socket.util.MessageConverter;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

/**
 * Http握手拦截器
 * @author w_angler
 *
 */

@Component
public class WebsocketHandshakeInterceptor implements HandshakeInterceptor {
	@Autowired
	private UserService userService;
	/**
	 * websocket连接建立前的握手，验证参数合法性
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler handler,
			Map<String, Object> attrs) throws Exception {
		HttpServletRequest httpRequest=((ServletServerHttpRequest)request).getServletRequest();
		HttpServletResponse httpResponse=((ServletServerHttpResponse)response).getServletResponse();
		httpResponse.setCharacterEncoding("UTF-8");
		PrintWriter out=((ServletServerHttpResponse)response).getServletResponse().getWriter();
		//用户合法性验证
		String userId =httpRequest.getParameter("userId");
		if(Objects.isNull(userId)||(!userId.matches("\\d+"))){
			out.print(MessageConverter.objectToJson(new Status(false,StatusCode.ERROR_DATA,"参数错误",null)));
			return false;
		}
		//TODO 验证token
		String token = httpRequest.getParameter("token");
		if(Objects.isNull(token)||(!validate(token))){
			out.print(MessageConverter.objectToJson(new Status(false,StatusCode.PERMISSION_LOW,"没有权限",null)));
			return false;
		}
		//验证请求类型
		String type=httpRequest.getParameter("type");
		if(Objects.isNull(token)||(!(type.equals("1")||type.equals("2")))){
			out.print(MessageConverter.objectToJson(new Status(false,StatusCode.ERROR_DATA,"参数错误",null)));
			return false;
		}
		attrs.put("userId", Integer.parseInt(userId));
		attrs.put("type", Integer.parseInt(type));
		
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
