package com.fansfunding.socket.util;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket会话连接池
 * 嗯嗯……线程安全
 * 简单的装饰器
 * @author w_angler
 * @param <V>
 *
 */
public class SessionPool<V> {
	private ConcurrentHashMap<String,WebSocketSession> sessionPool;
	
	public SessionPool(){
		sessionPool=new ConcurrentHashMap<String,WebSocketSession>();
	}
	/**
	 * 创建有初始大小的连接池
	 * @param initialSize
	 */
	public SessionPool(int initialSize){
		sessionPool=new ConcurrentHashMap<String,WebSocketSession>(initialSize);
	}
	/**
	 * 添加到连接池
	 * @param key 关键字，用户名或者用户id之类的
	 * @param session 会话
	 * @return
	 */
	public WebSocketSession put(String key,WebSocketSession session){
		return sessionPool.put(key, session);
	}
	/**
	 * 移出连接池
	 * @param key 关键字
	 * @return
	 */
	public WebSocketSession remove(String key){
		return sessionPool.remove(key);
	}
	/**
	 * 连接池大小
	 * @return
	 */
	public int size(){
		return sessionPool.size();
	}
	/**
	 * 关键字集
	 * @return
	 */
	public Set<String> keySet(){
		return sessionPool.keySet();
	}
	/**
	 * 连接集
	 * @return
	 */
	public Set<Entry<String, WebSocketSession>> connectionSet(){
		return sessionPool.entrySet();
	}
	/**
	 * 会话集
	 * @return
	 */
	public Collection<WebSocketSession> sessionSet(){
		return sessionPool.values();
	}
}
