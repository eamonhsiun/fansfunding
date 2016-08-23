package com.fansfunding.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.fansfunding.socket.handler.WebsocketHandshakeInterceptor;
import com.fansfunding.socket.handler.SocketHandler;

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
        registry.addHandler(socketHandler(),"/websocket").setAllowedOrigins("*")
        		.addInterceptors(new WebsocketHandshakeInterceptor());
        registry.addHandler(socketHandler(), "/sockjs").setAllowedOrigins("*")
        		.addInterceptors(new WebsocketHandshakeInterceptor()).withSockJS();
    }

    /**
     * 配置servlet容器
     * @return
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(2<<16);
        container.setMaxBinaryMessageBufferSize(2<<16);
        return container;
    }
    /**
     * 处理
     * @return
     */
    @Bean
    public WebSocketHandler socketHandler(){
        return new SocketHandler();
    }
}