package com.fansfunding.socket.handler;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fansfunding.socket.dao.MessageDao;
import com.fansfunding.socket.dao.NotificationDao;
import com.fansfunding.socket.entity.SessionEntity;
import com.fansfunding.socket.entity.SocketRequest;
import com.fansfunding.socket.util.Dispatcher;
import com.fansfunding.socket.util.MessageConverter;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

import lombok.extern.log4j.Log4j;

/**
 * 
 * 处理器
 * @author wangle
 *
 */
@Component
@Log4j
public class SocketHandler implements WebSocketHandler {
	@Autowired
	private UserService userService;
	@Autowired
	private NotificationDao notificationDao;
	@Autowired
	private MessageDao messageDao;
	//连接建立时
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Map<String,Object> attrs=session.getAttributes();
		int userId=(int)attrs.get("userId");
		if(!userService.isExist(userId)){
			Dispatcher.send(session,new Status(false,StatusCode.USER_NULL,"用户不存在",null));
			session.close();
		}
		else{
			String identification=UUID.randomUUID().toString();
			Dispatcher.add(session,SessionEntity.builder()
					.userId(userId)
					.token((String)attrs.get("token"))
					.identification(identification)
					.build());
			Dispatcher.send(session,new Status(true,StatusCode.SUCCESS,identification,null));
		}
	}
	//收到消息时
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> msg) throws Exception {
		Object message=msg.getPayload();
		if(!(message instanceof String)){
			Dispatcher.send(session, new Status(false,StatusCode.ERROR_DATA,"数据格式错误",null));
		}
		SocketRequest request=MessageConverter.jsonToRequest((String)message);
		RequestExecutor.execute(new RequestHandler(request){
			@Override
			public void before() {
				
			}
			@Override
			public void after() {
				
			}
		});
	}
	//出现错误时
	@Override
	public void handleTransportError(WebSocketSession session, Throwable e) throws Exception {
		if(session.isOpen()){
			session.close();
		}
		log.error("连接出现错误:"+session.getAttributes(), e);
	}
	//连接关闭时
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Dispatcher.remove(session);
	}
	//是否支持分为多个部分发送的消息
	@Override
	public boolean supportsPartialMessages() {
		return true;
	}
}
