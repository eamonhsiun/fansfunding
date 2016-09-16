package com.fansfunding.socket.util;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import com.fansfunding.socket.entity.CommentMessage;
import com.fansfunding.socket.entity.NotificationMessage;
import com.fansfunding.socket.entity.SessionEntity;
import com.fansfunding.socket.entity.SocketResponse;
import com.fansfunding.utils.response.StatusCode;

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
	public static int online(){
		return pool.size();
	}
	/**
	 * 是否在线
	 * @param userId
	 * @return
	 */
	public static boolean isOnline(int userId){
		return pool.isOnline(userId);
	}
	public static SessionEntity get(WebSocketSession session){
		return pool.get(session);
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
	 * 发送通知消息
	 * @param receiver
	 * @param notification
	 * @return
	 */
	public static boolean notice(int receiver,NotificationMessage notification){
		return Dispatcher.send(receiver,new SocketResponse(true,3,StatusCode.SUCCESS,notification));
	}
	/**
	 * 发送评论消息
	 * @param receiver
	 * @param notification
	 * @return
	 */
	public static boolean comment(int receiver,CommentMessage comment){
		return Dispatcher.send(receiver,new SocketResponse(true,2,StatusCode.SUCCESS,comment));
	}
	/**
	 * 发送
	 * @param receiver 接收人
	 * @param message 消息
	 * @return
	 */
	public static boolean send(int receiver,String json){
		if(!pool.isOnline(receiver)){
			return false;
		}
		return pool.get(receiver).stream().map(session->{
			try {
				session.sendMessage(new TextMessage(json));
				return true;
			} catch (Exception e) {
				log.error("发送失败："+json, e);
				return false;
			}
		}).reduce(true, (a,b)->a&&b);
	}
	public static boolean send(WebSocketSession session,String json){
		try {
			session.sendMessage(new TextMessage(json));
			return true;
		} catch (Exception e) {
			log.error("发送失败："+json, e);
			return false;
		}
	}
	public static boolean send(int receiver,SocketResponse response){
		return Dispatcher.send(receiver, MessageConverter.objectToJson(response));
	}
	public static boolean send(WebSocketSession session,SocketResponse response){
		return Dispatcher.send(session, MessageConverter.objectToJson(response));
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
		 * 获取实体
		 * @param session
		 * @return
		 */
		public SessionEntity get(WebSocketSession session){
			return sessionPool.get(session);
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
		public List<WebSocketSession> get(int userId){
			return (List<WebSocketSession>) sessionPool.entrySet()
					.parallelStream()
					.filter((conn)->conn.getValue().getUserId()==userId)
					.map(e->e.getKey())
					.collect(Collectors.toList());
		}
	}
}
