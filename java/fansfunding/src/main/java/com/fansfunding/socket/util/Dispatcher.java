package com.fansfunding.socket.util;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import com.fansfunding.socket.entity.Message;
import com.fansfunding.socket.entity.Notification;
import com.fansfunding.socket.entity.SessionEntity;
import com.fansfunding.utils.response.Status;

/**
 * 消息分发器
 * @author w-angler
 *
 */
@Log4j
public class Dispatcher {
	/**
	 * 会话连接池
	 */
	private static SessionPool pool=new SessionPool(100);
	private Dispatcher(){
		throw new IllegalArgumentException("you can not new an instance of this class");
	}
	/**
	 * 获取在线人数
	 * @return
	 */
	public int online(){
		return pool.size();
	}
	/**
	 * 添加
	 * @param session
	 * @param entity
	 * @return
	 */
	public static SessionEntity add(WebSocketSession session,SessionEntity entity){
		return pool.put(session, entity);
	}
	/**
	 * 移除
	 * @param session
	 * @return
	 */
	public static SessionEntity remove(WebSocketSession session){
		return pool.remove(session);
	}
	/**
	 * 发送通知，成功则返回true，失败（例如用户不在线）返回false
	 * @return
	 */
	public static boolean notification(Notification notification){
		if(!pool.isOnline(notification.getReceiver())){
			return false;
		}
		return pool.get(notification.getReceiver()).map(session->{
			try {
				session.sendMessage(new TextMessage(MessageConverter.objectToJson(notification)));
				return true;
			} catch (Exception e) {
				log.error("通知发送失败："+notification, e);
				return false;
			}
		}).orElse(false);
	}
	/**
	 * 发送消息
	 * @param message
	 * @return
	 */
	public static boolean message(Message message){
		if(!pool.isOnline(message.getReceiver())){
			return false;
		}
		return pool.get(message.getReceiver()).map(session->{
			try {
				session.sendMessage(new TextMessage(MessageConverter.objectToJson(message)));
				return true;
			} catch (Exception e) {
				log.error("消息发送失败："+message, e);
				return false;
			}
		}).orElse(false);
	}
	/**
	 * 发送
	 * @param receiver 接收人
	 * @param message 消息
	 * @return
	 */
	public static boolean send(int receiver,String message){
		if(!pool.isOnline(receiver)){
			return false;
		}
		return pool.get(receiver).map(session->{
			try {
				session.sendMessage(new TextMessage(message));
				return true;
			} catch (Exception e) {
				log.error("发送失败："+message, e);
				return false;
			}
		}).orElse(false);
	}
	public static boolean send(WebSocketSession session,String message){
		try {
			session.sendMessage(new TextMessage(message));
			return true;
		} catch (Exception e) {
			log.error("发送失败："+message, e);
			return false;
		}
	}
	public static boolean send(int receiver,Status status){
		return Dispatcher.send(receiver, MessageConverter.objectToJson(status));
	}
	public static boolean send(WebSocketSession session,Status status){
		return Dispatcher.send(session, MessageConverter.objectToJson(status));
	}
	
	/*————————————————————————————————我只是萌萌哒分割线————————————————————————————————————*/

	/**
	 * WebSocket会话连接池
	 * 嗯嗯……线程安全
	 * 简单的装饰器
	 * @author w_angler
	 *
	 */
	private static class SessionPool{
		private ConcurrentHashMap<WebSocketSession,SessionEntity> sessionPool;

		/**
		 * 创建有初始大小的连接池
		 * @param initialSize
		 */
		public SessionPool(int initialSize){
			sessionPool=new ConcurrentHashMap<>(initialSize);
		}
		/**
		 * 添加到连接池
		 * @param session 会话
		 * @param entity 用户名或者用户id之类的
		 * @return
		 */
		public SessionEntity put(WebSocketSession session,SessionEntity entity){
			return sessionPool.put(session,entity);
		}
		/**
		 * 移出连接池
		 * @param session 会话
		 * @return
		 */
		public SessionEntity remove(WebSocketSession session){
			return sessionPool.remove(session);
		}
		/**
		 * 连接池大小
		 * @return
		 */
		public int size(){
			return sessionPool.size();
		}
		/**
		 * 判断用户是否在线
		 * @param userId 用户id
		 * @return
		 */
		public boolean isOnline(int userId){
			return sessionPool.values()
					.stream()
					.filter((entity)->entity.getUserId()==userId)
					.findAny()
					.isPresent();
		}
		/**
		 * 根据用户id获取会话
		 * @param userId 用户id
		 * @return
		 */
		public Optional<WebSocketSession> get(int userId){
			return sessionPool.entrySet()
					.parallelStream()
					.filter((conn)->conn.getValue().getUserId()==userId)
					.findAny()
					.map(e->e.getKey());
		}
	}
}
