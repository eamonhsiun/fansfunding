package com.fansfunding.socket.handler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fansfunding.socket.dao.MessageDao;
import com.fansfunding.socket.dao.NotificationDao;
import com.fansfunding.socket.entity.Message;
import com.fansfunding.socket.entity.SessionEntity;
import com.fansfunding.socket.entity.SocketRequest;
import com.fansfunding.socket.entity.SocketResponse;
import com.fansfunding.socket.util.Dispatcher;
import com.fansfunding.socket.util.MessageConverter;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.StatusCode;

/**
 * 
 * 处理器
 * @author wangle
 *
 */
@Log4j
@Component
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
			Dispatcher.send(session,new SocketResponse(false,0,StatusCode.USER_NULL,"用户不存在"));
			session.close();
		}
		else{
			Dispatcher.add(session,SessionEntity.builder()
					.userId(userId)
					.token((String)attrs.get("token"))
					.build());
			//发送未读的私聊消息
			messageDao.selectUnread(userId)
			.stream()
			.peek(msg->messageDao.read(msg.getId()))
			.map(msg->{
				Map<String,Object> data=new LinkedHashMap<>();
				data.put("sender", userService.getUserBasicMap(userService.getUserById(msg.getSender())));
				data.put("content", msg.getContent());
				data.put("sendTime", msg.getCreateTime());
				return new SocketResponse(true,1,StatusCode.SUCCESS,data);
			})
			.forEach(response->Dispatcher.send(session, response));
			//发送未读的评论消息
			notificationDao.selectUnreadComment(userId)
			.stream()
			.peek(notification->notificationDao.read(notification.getId()))
			.forEach(notification->Dispatcher.send(session,notification.getContent()));
			//发送未读的通知
			notificationDao.selectUnreadNotification(userId)
			.stream()
			.peek(notification->notificationDao.read(notification.getId()))
			.forEach(notification->Dispatcher.send(session,notification.getContent()));
		}
	}
	//收到消息时
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> msg) throws Exception {
		Object message=msg.getPayload();
		if(!(message instanceof String)){
			Dispatcher.send(session, new SocketResponse(false,0,StatusCode.ERROR_DATA,"数据格式错误"));
		}
		SocketRequest request=MessageConverter.jsonToRequest((String)message);
		if(Objects.isNull(request)){
			Dispatcher.send(session, new SocketResponse(false,0,StatusCode.ERROR_DATA,"数据格式错误"));
		}
		else{
			if(!userService.isExist(request.getReceiver())){
				Dispatcher.send(session,new SocketResponse(false,0,StatusCode.USER_NULL,"用户不存在"));
			}
			else{
				RequestExecutor.execute(new RequestHandler(request){
					@Override
					public void handle(SocketRequest request) {
						SessionEntity entity=Dispatcher.get(session);
						Message msg=Message.builder().content(request.getContent())
								.createBy(userService.getUserById(entity.getUserId()).getName())
								.receiver(request.getReceiver())
								.sender(entity.getUserId())
								.build();
						if(Dispatcher.isOnline(request.getReceiver())){
							Map<String,Object> data=new LinkedHashMap<>();
							data.put("sender", userService.getUserBasicMap(userService.getUserById(msg.getSender())));
							data.put("content", msg.getContent());
							data.put("sendTime", new Date());
							Dispatcher.send(request.getReceiver(), new SocketResponse(true,1,StatusCode.SUCCESS,data));
							msg.setIsRead("1");
						}
						else{
							msg.setIsRead("0");
						}
						messageDao.insert(msg);
					}
				});
			}
		}
	}
	//出现错误时
	@Override
	public void handleTransportError(WebSocketSession session, Throwable e) throws Exception {
		if(session.isOpen()){
			session.close();
		}
		Dispatcher.send(session,new SocketResponse(false,0,StatusCode.FAILED,"出现错误，连接关闭"));
		Dispatcher.remove(session);
		log.error("连接出现错误:"+session.getAttributes(), e);
		e.printStackTrace();
	}
	//连接关闭时
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Dispatcher.remove(session);
	}
	//是否支持分为多个部分发送的消息
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
