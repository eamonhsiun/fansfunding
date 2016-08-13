package com.fansfunding.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.fansfunding.socket.handler.ChatHandler;
import com.fansfunding.socket.handler.NotificationHandler;
import com.fansfunding.socket.interceptor.ChatHandshakeInterceptor;


/**
 * websocket配置
 * 因为跨域请求的接口，需要setAllowedOrigins("*")
 * 两种方式，原生websocket和SockJS
 * @author w_angler
 *
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	/**
	 * 注册websocket处理器
	 */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(),"/websocket/chat").setAllowedOrigins("*")
        		.addInterceptors(new ChatHandshakeInterceptor());
        registry.addHandler(chatHandler(), "/sockjs/chat").setAllowedOrigins("*")
        		.addInterceptors(new ChatHandshakeInterceptor()).withSockJS();
        registry.addHandler(notificationHandler(), "/websocket/notification").setAllowedOrigins("*")
        		.addInterceptors(new ChatHandshakeInterceptor());
        registry.addHandler(chatHandler(), "/sockjs/notification").setAllowedOrigins("*")
				.addInterceptors(new ChatHandshakeInterceptor()).withSockJS();
    }

    /**
     * 配置servlet容器
     * @return
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
    /**
     * 私聊处理
     * @return
     */
    @Bean
    public WebSocketHandler chatHandler(){
        return new ChatHandler();
    }
    /**
     * 通知处理
     * @return
     */
    @Bean
    public WebSocketHandler notificationHandler(){
    	return new NotificationHandler();
    }
}